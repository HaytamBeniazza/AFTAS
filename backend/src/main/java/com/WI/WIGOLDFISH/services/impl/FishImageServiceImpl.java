package com.WI.WIGOLDFISH.services.impl;

import com.WI.WIGOLDFISH.services.interfaces.FishImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.List;

@Service
public class FishImageServiceImpl implements FishImageService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Override
    public String fetchFishImage(String fishName) {
        if (fishName == null || fishName.trim().isEmpty()) {
            return getDefaultFishImage();
        }
        
        // Try multiple image sources
        String imageUrl = null;
        
        // 1. Try Unsplash API (free, high quality)
        imageUrl = fetchFromUnsplash(fishName);
        if (imageUrl != null) return imageUrl;
        
        // 2. Try Pixabay API (free, good quality)
        imageUrl = fetchFromPixabay(fishName);
        if (imageUrl != null) return imageUrl;
        
        // 3. Try iNaturalist API (scientific images)
        imageUrl = fetchFromINaturalist(fishName);
        if (imageUrl != null) return imageUrl;
        
        // 4. Return default fish image if all fail
        return getDefaultFishImage();
    }
    
    private String fetchFromUnsplash(String fishName) {
        // Skip Unsplash for now since it requires API key
        return null;
    }
    
    private String fetchFromPixabay(String fishName) {
        // Skip Pixabay for now since it requires API key
        return null;
    }
    
    private String fetchFromINaturalist(String fishName) {
        try {
            String query = cleanFishName(fishName).replace("test ", "");
            
            // Try multiple search strategies for better results
            String[] searchUrls = {
                "https://api.inaturalist.org/v1/taxa?q=" + query + "&per_page=3&rank=species&iconic_taxa=Actinopterygii",
                "https://api.inaturalist.org/v1/taxa?q=" + query + " fish&per_page=3&rank=species",
                "https://api.inaturalist.org/v1/taxa?q=" + query.split(" ")[0] + "&per_page=5&rank=species&iconic_taxa=Actinopterygii"
            };
            
            for (String url : searchUrls) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> response = restTemplate.getForObject(url, Map.class);
                    
                    if (response != null && response.containsKey("results")) {
                        @SuppressWarnings("unchecked")
                        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
                        
                        for (Map<String, Object> result : results) {
                            if (result.containsKey("default_photo")) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> photo = (Map<String, Object>) result.get("default_photo");
                                if (photo != null && photo.containsKey("medium_url")) {
                                    String imageUrl = photo.get("medium_url").toString();
                                    System.out.println("✅ Found iNaturalist image for " + fishName + ": " + imageUrl);
                                    return imageUrl;
                                }
                            }
                        }
                    }
                    
                    Thread.sleep(200); // Be respectful to API
                } catch (RestClientException e) {
                    System.err.println("iNaturalist search failed for URL: " + url);
                    continue;
                }
            }
        } catch (Exception e) {
            System.err.println("iNaturalist image API error for " + fishName + ": " + e.getMessage());
        }
        return null;
    }
    
    private String cleanFishName(String fishName) {
        if (fishName == null) return "";
        return fishName.replaceAll("[^a-zA-Z0-9\\s]", "")
                      .replaceAll("\\s+", " ")
                      .trim()
                      .toLowerCase();
    }
    
    private String getDefaultFishImage() {
        // Return different default fish images for variety
        String[] defaultImages = {
            "https://images.unsplash.com/photo-1544551763-46a013bb70d5?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80", // Generic fish
            "https://images.unsplash.com/photo-1520637836862-4d197d17c43a?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80", // Colorful fish
            "https://images.unsplash.com/photo-1559827260-dc66d52bef19?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80", // Tropical fish
            "https://images.unsplash.com/photo-1535591273668-578e31182c4f?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80", // Ocean fish
            "https://images.unsplash.com/photo-1583212292454-1fe6229603b7?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=80"  // Swimming fish
        };
        
        // Return a random default image for variety
        int randomIndex = (int) (Math.random() * defaultImages.length);
        return defaultImages[randomIndex];
    }
} 