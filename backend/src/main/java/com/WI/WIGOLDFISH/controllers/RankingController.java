package com.WI.WIGOLDFISH.controllers;

import com.WI.WIGOLDFISH.entities.competition.Competition;
import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.entities.ranking.RankingDtoReq;
import com.WI.WIGOLDFISH.ids.RankingId;
import com.WI.WIGOLDFISH.services.interfaces.RankingService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('JURY', 'MANAGER')")
public class RankingController {
    private final RankingService rankingServiceImpl;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> createRanking(@Valid @RequestBody RankingDtoReq rankingDtoReq) {
        rankingDtoReq = rankingServiceImpl.save(rankingDtoReq);
        Map<String, Object> response = new HashMap<>();
        response.put("data", rankingDtoReq);
        response.put("message", "Ranking created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> getRankings() {
        return ResponseEntity.ok(rankingServiceImpl.findAll());
    }

    @GetMapping("/{member_id}/{competition_id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> getRanking(@PathVariable UUID member_id, @PathVariable String competition_id) {
        RankingId s = new RankingId();
        DBUser m = new DBUser();
        m.setId(member_id);
        Competition c = new Competition();
        c.setCode(competition_id);
        s.setMember(m);
        s.setCompetition(c);
        return ResponseEntity.ok(rankingServiceImpl.findOne(s));
    }
    @GetMapping("/competition/{competitionCode}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY','ROLE_ADHERENT')")
    public ResponseEntity<?> findAllByCompetition_CodeOrderByScoreDesc(@PathVariable String competitionCode) {
        return ResponseEntity.ok(rankingServiceImpl.findAllByCompetition_CodeOrderByScoreDesc(competitionCode));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> updateRanking(@PathVariable RankingId s, @Valid @RequestBody RankingDtoReq rankingDtoReq) {
        rankingDtoReq = rankingServiceImpl.update(rankingDtoReq, s);
        Map<String, Object> response = new HashMap<>();
        response.put("data", rankingDtoReq);
        response.put("message", "Ranking updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{member_id}/{competition_id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_JURY')")
    public ResponseEntity<?> deleteRanking(@PathVariable UUID member_id, @PathVariable String competition_id) {
        RankingId s = new RankingId();
        DBUser m = new DBUser();
        m.setId(member_id);
        Competition c = new Competition();
        c.setCode(competition_id);
        s.setMember(m);
        s.setCompetition(c);
        rankingServiceImpl.delete(s);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ranking deleted successfully");
        return ResponseEntity.ok(response);
    }
}
