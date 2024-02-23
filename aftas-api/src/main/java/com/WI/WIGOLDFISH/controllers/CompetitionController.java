package com.WI.WIGOLDFISH.controllers;

import com.WI.WIGOLDFISH.entities.competition.CompetitionDtoReq;
import com.WI.WIGOLDFISH.enums.FilterCompetition;
import com.WI.WIGOLDFISH.services.interfaces.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {
    private final CompetitionService competitionServiceImpl;
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> createCompetition(@Valid @RequestBody CompetitionDtoReq competitionDtoReq) {
       competitionDtoReq = competitionServiceImpl.save(competitionDtoReq);
        Map<String, Object> response = new HashMap<>();
        response.put("data", competitionDtoReq);
        response.put("message", "Competition created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> getCompetitions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "all") String filter){
        Pageable pageable = PageRequest.of(page, size);
        FilterCompetition  filterCompetition = FilterCompetition.valueOf(filter.toUpperCase());
        return ResponseEntity.ok(competitionServiceImpl.findAll(pageable, filterCompetition));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> getCompetition(@PathVariable String id) {
        return ResponseEntity.ok(competitionServiceImpl.findOne(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> updateCompetition(@PathVariable String id, @Valid @RequestBody CompetitionDtoReq competitionDtoReq) {
        competitionDtoReq = competitionServiceImpl.update(competitionDtoReq, id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", competitionDtoReq);
        response.put("message", "Competition updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> deleteCompetition(@PathVariable String id) {
        competitionServiceImpl.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Competition deleted successfully");
        return ResponseEntity.ok(response);
    }
}
