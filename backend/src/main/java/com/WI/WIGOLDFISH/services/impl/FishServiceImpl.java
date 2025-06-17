package com.WI.WIGOLDFISH.services.impl;

import com.WI.WIGOLDFISH.entities.fish.Fish;
import com.WI.WIGOLDFISH.entities.fish.FishDtoReq;
import com.WI.WIGOLDFISH.entities.fish.FishDtoRes;
import com.WI.WIGOLDFISH.exceptions.ResourceNotFound;
import com.WI.WIGOLDFISH.repositories.FishRepository;
import com.WI.WIGOLDFISH.repositories.LevelRepository;
import com.WI.WIGOLDFISH.services.interfaces.FishService;
import com.WI.WIGOLDFISH.services.interfaces.FishImageService;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import com.WI.WIGOLDFISH.entities.level.Level;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FishServiceImpl implements FishService {
    private final FishRepository fishRepository;
    private final ModelMapper modelMapper;
    private final LevelRepository levelRepository;
    private final FishImageService fishImageService;
    private final RestTemplate restTemplate = new RestTemplate();
    @Override
    public FishDtoReq save(FishDtoReq dtoMini) {
        levelRepository.findById(dtoMini.getLevel_id()).orElseThrow(() -> new ResourceNotFound("Level not found"));
        fishRepository.findByName(dtoMini.getName()).ifPresent(fish -> {
            throw new ResourceNotFound("Fish already exists");
        });
        Fish fish = modelMapper.map(dtoMini, Fish.class);
        fish.setLevel(new Level(dtoMini.getLevel_id()));
        fish = fishRepository.save(fish);
        FishDtoReq fishDtoReq = modelMapper.map(fish, FishDtoReq.class);
        fishDtoReq.setLevel_id(fish.getLevel().getCode());
        return fishDtoReq;
    }

    @Override
    public FishDtoReq update(FishDtoReq dtoMini, String s) {
        fishRepository.findById(s).orElseThrow(() -> new ResourceNotFound("Fish not found"));
        levelRepository.findById(dtoMini.getLevel_id()).orElseThrow(() -> new ResourceNotFound("Level not found"));
        Fish fish = modelMapper.map(dtoMini, Fish.class);
        fish.setName(s);
        fish.setLevel(new Level(dtoMini.getLevel_id()));
        fish = fishRepository.save(fish);
        return modelMapper.map(fish, FishDtoReq.class);
    }

    @Override
    public Boolean delete(String s) {
        fishRepository.findById(s).orElseThrow(() -> new ResourceNotFound("Fish not found"));
        fishRepository.deleteById(s);
        if (fishRepository.findById(s).isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public FishDtoRes findOne(String s) {
        Fish fish = fishRepository.findById(s).orElseThrow(() -> new ResourceNotFound("Fish not found"));
        return modelMapper.map(fish, FishDtoRes.class);
    }

    @Override
    public List<FishDtoRes> findAll() {
        List<FishDtoRes> fishDtoRes = fishRepository.findAll().stream().map(fish -> modelMapper.map(fish, FishDtoRes.class)).toList();
        return fishDtoRes;
    }

    @Override
    public Page<FishDtoRes> findAllPaginated(Pageable pageable) {
        Page<Fish> fishPage = fishRepository.findAll(pageable);
        return fishPage.map(fish -> modelMapper.map(fish, FishDtoRes.class));
    }

    @Override
    public void importAllFishSpecies() {
        System.out.println("🌍 Starting comprehensive fish import from external APIs...");
        
        // Get all levels
        List<Level> levels = levelRepository.findAll();
        if (levels.isEmpty()) {
            throw new RuntimeException("No levels found. Please create levels first.");
        }

        Set<String> fishNames = new HashSet<>(); // For deduplication
        List<Fish> allFish = new ArrayList<>();
        
        try {
            // 1. Import from FishBase API (Most comprehensive)
            System.out.println("📡 Importing from FishBase API...");
            List<Fish> fishBaseData = importFromFishBase(levels);
            addUniqueFish(allFish, fishBaseData, fishNames);

            // 2. Import from iNaturalist API
            System.out.println("📡 Importing from iNaturalist API...");
            List<Fish> iNaturalistData = importFromiNaturalist(levels);
            addUniqueFish(allFish, iNaturalistData, fishNames);

            // 3. Import from GBIF API
            System.out.println("📡 Importing from GBIF API...");
            List<Fish> gbifData = importFromGBIF(levels);
            addUniqueFish(allFish, gbifData, fishNames);

            // 4. Save all unique fish to database
            System.out.println("💾 Saving " + allFish.size() + " unique fish species to database...");
            int savedCount = batchSaveFish(allFish);

            System.out.println("🎉 Fish import completed! Imported " + savedCount + " new fish species.");
            
        } catch (Exception e) {
            System.err.println("❌ Error during fish import: " + e.getMessage());
            throw new RuntimeException("Fish import failed: " + e.getMessage());
        }
    }

    private List<Fish> importFromFishBase(List<Level> levels) {
        List<Fish> fishList = new ArrayList<>();
        
        try {
            // FishBase API - get multiple batches for comprehensive data
            int batchSize = 100;
            int maxBatches = 50; // Get up to 5000 fish
            
            for (int i = 0; i < maxBatches; i++) {
                int offset = i * batchSize;
                String url = "https://fishbase.ropensci.org/species?limit=" + batchSize + "&offset=" + offset;
                
                try {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                    
                    if (response == null || response.isEmpty()) {
                        break; // No more data
                    }
                    
                    for (Map<String, Object> fishData : response) {
                        try {
                            String name = cleanFishName(getStringValue(fishData, "Species"));
                            if (name != null && !name.isEmpty() && name.length() > 2) {
                                double weight = parseWeight(fishData.get("Weight"));
                                if (weight <= 0) {
                                    weight = estimateWeightFromLength(getDoubleValue(fishData, "Length"));
                                }
                                
                                Fish fish = new Fish();
                                fish.setName(name);
                                fish.setAverageWeight(weight);
                                fish.setLevel(assignLevelByWeight(weight, levels));
                                // Fetch image for this fish
                                try {
                                    String imageUrl = fishImageService.fetchFishImage(name);
                                    fish.setImageUrl(imageUrl);
                                } catch (Exception e) {
                                    System.err.println("Failed to fetch image for " + name + ": " + e.getMessage());
                                }
                                
                                fishList.add(fish);
                            }
                        } catch (Exception e) {
                            // Skip individual fish that fail
                            continue;
                        }
                    }
                    
                    // Small delay to be respectful to the API
                    Thread.sleep(100);
                    
                } catch (RestClientException e) {
                    System.err.println("FishBase API error at offset " + offset + ": " + e.getMessage());
                    break; // Stop on API errors
                }
            }
            
        } catch (Exception e) {
            System.err.println("FishBase import error: " + e.getMessage());
        }
        
        System.out.println("FishBase: Found " + fishList.size() + " fish species");
        return fishList;
    }

    private List<Fish> importFromiNaturalist(List<Level> levels) {
        List<Fish> fishList = new ArrayList<>();
        
        try {
            String[] fishClasses = {"Actinopterygii", "Chondrichthyes", "Agnatha"};
            
            for (String fishClass : fishClasses) {
                String url = "https://api.inaturalist.org/v1/taxa?q=" + fishClass + 
                           "&rank=species&per_page=200&iconic_taxa=Actinopterygii";
                
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                    
                    if (response != null && response.containsKey("results")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                        
                        for (Map<String, Object> fishData : results) {
                            try {
                                String name = cleanFishName(getStringValue(fishData, "name"));
                                if (name == null || name.isEmpty()) {
                                    name = cleanFishName(getStringValue(fishData, "preferred_common_name"));
                                }
                                
                                if (name != null && !name.isEmpty() && name.length() > 2) {
                                    double weight = estimateWeightFromName(name);
                                    
                                    Fish fish = new Fish();
                                    fish.setName(name);
                                    fish.setAverageWeight(weight);
                                    fish.setLevel(assignLevelByWeight(weight, levels));
                                    
                                    fishList.add(fish);
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                    
                    Thread.sleep(200); // Be respectful to API
                    
                } catch (RestClientException e) {
                    System.err.println("iNaturalist API error for " + fishClass + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("iNaturalist import error: " + e.getMessage());
        }
        
        System.out.println("iNaturalist: Found " + fishList.size() + " fish species");
        return fishList;
    }

    private List<Fish> importFromGBIF(List<Level> levels) {
        List<Fish> fishList = new ArrayList<>();
        
        try {
            String[] fishOrders = {"Perciformes", "Cypriniformes", "Siluriformes", "Salmoniformes", "Gadiformes"};
            
            for (String order : fishOrders) {
                String url = "https://api.gbif.org/v1/species/search?q=" + order + 
                           "&rank=SPECIES&limit=200&kingdom=Animalia&phylum=Chordata&class=Actinopterygii";
                
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                    
                    if (response != null && response.containsKey("results")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                        
                        for (Map<String, Object> fishData : results) {
                            try {
                                String name = cleanFishName(getStringValue(fishData, "scientificName"));
                                if (name == null || name.isEmpty()) {
                                    name = cleanFishName(getStringValue(fishData, "canonicalName"));
                                }
                                
                                if (name != null && !name.isEmpty() && name.length() > 2) {
                                    double weight = estimateWeightFromName(name);
                                    
                                    Fish fish = new Fish();
                                    fish.setName(name);
                                    fish.setAverageWeight(weight);
                                    fish.setLevel(assignLevelByWeight(weight, levels));
                                    
                                    fishList.add(fish);
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                    
                    Thread.sleep(150); // Be respectful to API
                    
                } catch (RestClientException e) {
                    System.err.println("GBIF API error for " + order + ": " + e.getMessage());
                }
            }
            
        } catch (Exception e) {
            System.err.println("GBIF import error: " + e.getMessage());
        }
        
        System.out.println("GBIF: Found " + fishList.size() + " fish species");
        return fishList;
    }

    private void addUniqueFish(List<Fish> allFish, List<Fish> newFish, Set<String> fishNames) {
        for (Fish fish : newFish) {
            String cleanName = cleanFishName(fish.getName());
            if (!fishNames.contains(cleanName) && cleanName.length() > 2) {
                fishNames.add(cleanName);
                fish.setName(cleanName);
                allFish.add(fish);
            }
        }
    }

    private int batchSaveFish(List<Fish> fishList) {
        int savedCount = 0;
        int batchSize = 10;
        
        for (int i = 0; i < fishList.size(); i += batchSize) {
            List<Fish> batch = fishList.subList(i, Math.min(i + batchSize, fishList.size()));
            
            for (Fish fish : batch) {
                try {
                    // Check if fish already exists
                    if (fishRepository.findByName(fish.getName()).isPresent()) {
                        continue; // Skip if already exists
                    }
                    
                    fishRepository.save(fish);
                    savedCount++;
                    
                } catch (Exception e) {
                    System.err.println("Failed to save fish: " + fish.getName() + " - " + e.getMessage());
                }
            }
            
            // Small delay between batches
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return savedCount;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private double getDoubleValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0.0;
        
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private String cleanFishName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        
        String cleaned = name.replaceAll("[^a-zA-Z0-9\\s-]", "") // Remove special characters
                            .replaceAll("\\s+", " ") // Replace multiple spaces with single space
                            .trim();
        
        return cleaned.substring(0, Math.min(cleaned.length(), 50)); // Limit length
    }

    private double parseWeight(Object weight) {
        if (weight == null) return 0.0;
        
        try {
            double parsed = Double.parseDouble(weight.toString());
            return Math.max(0.1, parsed);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double estimateWeightFromLength(double length) {
        if (length <= 0) return generateRandomWeight();
        
        // Rough estimation: weight = length^3 * 0.1 (very approximate)
        return Math.max(0.1, Math.pow(length / 10, 3) * 0.1);
    }

    private double estimateWeightFromName(String name) {
        if (name == null) return generateRandomWeight();
        
        String lowerName = name.toLowerCase();
        
        // Large fish
        if (lowerName.contains("shark") || lowerName.contains("tuna") || lowerName.contains("marlin") 
            || lowerName.contains("swordfish") || lowerName.contains("halibut")) {
            return 20.0 + Math.random() * 80.0; // 20-100 kg
        }
        
        // Medium-large fish
        if (lowerName.contains("salmon") || lowerName.contains("bass") || lowerName.contains("pike")
            || lowerName.contains("cod") || lowerName.contains("grouper")) {
            return 2.0 + Math.random() * 18.0; // 2-20 kg
        }
        
        // Medium fish
        if (lowerName.contains("trout") || lowerName.contains("perch") || lowerName.contains("mackerel")
            || lowerName.contains("snapper")) {
            return 0.5 + Math.random() * 4.5; // 0.5-5 kg
        }
        
        // Small fish
        if (lowerName.contains("sardine") || lowerName.contains("anchovy") || lowerName.contains("herring")
            || lowerName.contains("goldfish")) {
            return 0.1 + Math.random() * 0.9; // 0.1-1 kg
        }
        
        // Default medium fish
        return 1.0 + Math.random() * 9.0; // 1-10 kg
    }

    private double generateRandomWeight() {
        // Generate realistic fish weights (0.1kg to 50kg, with most being smaller)
        double random = Math.random();
        if (random < 0.6) return Math.random() * 2 + 0.1; // 60% small fish (0.1-2kg)
        if (random < 0.9) return Math.random() * 10 + 2; // 30% medium fish (2-12kg)
        return Math.random() * 40 + 10; // 10% large fish (10-50kg)
    }

    private Level assignLevelByWeight(double weight, List<Level> levels) {
        // Sort levels by points (assuming higher points = higher level)
        levels.sort((a, b) -> Long.compare(a.getPoints(), b.getPoints()));
        
        if (weight < 1.0) return levels.get(0); // Small fish
        if (weight < 5.0) return levels.get(Math.min(1, levels.size() - 1)); // Medium fish
        if (weight < 15.0) return levels.get(Math.min(2, levels.size() - 1)); // Large fish
        if (weight < 30.0) return levels.get(Math.min(3, levels.size() - 1)); // Very large fish
        return levels.get(levels.size() - 1); // Giant fish
    }

    @Override
    public void createTestFish() {
        System.out.println("Creating test fish...");
        
        // Get all levels for assignment
        List<Level> levels = levelRepository.findAll();
        if (levels.isEmpty()) {
            throw new RuntimeException("No levels found. Please create levels first.");
        }
        
        // Create a few test fish
        List<FishDtoReq> testFish = Arrays.asList(
            new FishDtoReq("Test Salmon", 2.5, null, levels.get(0).getCode()),
            new FishDtoReq("Test Tuna", 15.0, null, levels.get(Math.min(1, levels.size()-1)).getCode()),
            new FishDtoReq("Test Shark", 50.0, null, levels.get(Math.min(2, levels.size()-1)).getCode())
        );
        
        for (FishDtoReq fish : testFish) {
            try {
                save(fish);
                System.out.println("Created test fish: " + fish.getName());
            } catch (Exception e) {
                System.err.println("Failed to create test fish " + fish.getName() + ": " + e.getMessage());
                throw e;
            }
        }
        
        System.out.println("Test fish creation completed!");
    }

    @Override
    public void fetchImagesForAllFish() {
        System.out.println("🖼️ Starting image fetching for all fish...");
        
        List<Fish> allFish = fishRepository.findAll();
        System.out.println("Found " + allFish.size() + " fish to fetch images for");
        
        int processed = 0;
        int updated = 0;
        
        for (Fish fish : allFish) {
            try {
                // Skip if fish already has an image
                if (fish.getImageUrl() != null && !fish.getImageUrl().isEmpty()) {
                    processed++;
                    continue;
                }
                
                // Fetch image for this fish
                String imageUrl = fishImageService.fetchFishImage(fish.getName());
                
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    fish.setImageUrl(imageUrl);
                    fishRepository.save(fish);
                    updated++;
                    System.out.println("✅ Updated image for: " + fish.getName());
                } else {
                    System.out.println("❌ No image found for: " + fish.getName());
                }
                
                processed++;
                
                // Progress update every 50 fish
                if (processed % 50 == 0) {
                    System.out.println("📊 Progress: " + processed + "/" + allFish.size() + " processed, " + updated + " updated");
                }
                
                // Small delay to be respectful to APIs
                Thread.sleep(500);
                
            } catch (Exception e) {
                System.err.println("Error fetching image for " + fish.getName() + ": " + e.getMessage());
                processed++;
            }
        }
        
        System.out.println("🎉 Image fetching completed! Processed: " + processed + ", Updated: " + updated);
    }
}
