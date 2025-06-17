package com.WI.WIGOLDFISH.controllers;

import com.WI.WIGOLDFISH.entities.fish.FishDtoReq;
import com.WI.WIGOLDFISH.services.impl.FishServiceImpl;
import com.WI.WIGOLDFISH.services.interfaces.FishService;
import com.WI.WIGOLDFISH.services.interfaces.FishImageService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fish")
@RequiredArgsConstructor
public class FishController {
    private final FishService fishServiceImpl;
    private final FishImageService fishImageService;

    @PostMapping
    public ResponseEntity<?> createFish(@Valid @RequestBody FishDtoReq fishDtoReq) {
        fishDtoReq = fishServiceImpl.save(fishDtoReq);
        Map<String, Object> response = new HashMap<>();
        response.put("data", fishDtoReq);
        response.put("message", "Fish created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getFishs() {
        return ResponseEntity.ok(fishServiceImpl.findAll());
    }

    @GetMapping("/paginated")
    public ResponseEntity<?> getFishsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(fishServiceImpl.findAllPaginated(pageable));
    }

    @PostMapping("/import-all")
    public ResponseEntity<?> importAllFishSpecies() {
        try {
            fishServiceImpl.importAllFishSpecies();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Fish import started successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Failed to start fish import: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/test-import")
    public ResponseEntity<?> testImport() {
        try {
            fishServiceImpl.createTestFish();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Test fish created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Failed to create test fish: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/fetch-images")
    public ResponseEntity<?> fetchImagesForAllFish() {
        try {
            fishServiceImpl.fetchImagesForAllFish();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image fetching started for all fish");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Failed to start image fetching: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/fetch-image/{fishName}")
    public ResponseEntity<?> fetchImageForFish(@PathVariable String fishName) {
        try {
            String imageUrl = fishImageService.fetchFishImage(fishName);
            Map<String, Object> response = new HashMap<>();
            response.put("fishName", fishName);
            response.put("imageUrl", imageUrl);
            response.put("message", "Image fetched successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Failed to fetch image for " + fishName + ": " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> getFish(@PathVariable String id) {
        return ResponseEntity.ok(fishServiceImpl.findOne(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_JURY', 'ROLE_MANAGER')")
    public ResponseEntity<?> updateFish(@PathVariable String id, @Valid @RequestBody FishDtoReq fishDtoReq) {
        fishDtoReq = fishServiceImpl.update(fishDtoReq, id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", fishDtoReq);
        response.put("message", "Fish updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_JURY', 'ROLE_MANAGER')")
    public ResponseEntity<?> deleteFish(@PathVariable String id) {
        fishServiceImpl.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Fish deleted successfully");
        return ResponseEntity.ok(response);
    }
}
