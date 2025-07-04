package com.WI.WIGOLDFISH.controllers;

import com.WI.WIGOLDFISH.entities.member.MemberDtoReq;
import com.WI.WIGOLDFISH.enums.Role;
import com.WI.WIGOLDFISH.services.impl.MemberServiceImpl;
import com.WI.WIGOLDFISH.services.interfaces.MemberService;
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
import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberServiceImpl;

    @PostMapping
    public ResponseEntity<?> createMember(@Valid @RequestBody MemberDtoReq memberDtoReq) {
        memberDtoReq = memberServiceImpl.save(memberDtoReq);
        Map<String, Object> response = new HashMap<>();
        response.put("data", memberDtoReq);
        response.put("message", "Member created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getMembers() {
        return ResponseEntity.ok(memberServiceImpl.findAll());
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> getPendingMembers() {
        Map<String, Object> response = new HashMap<>();
        response.put("data", memberServiceImpl.findPendingMembers());
        response.put("message", "Pending members retrieved successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> approveMember(@PathVariable UUID id, @RequestParam Role role) {
        memberServiceImpl.approveMember(id, role);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member approved successfully with role: " + role.name());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER')")
    public ResponseEntity<?> rejectMember(@PathVariable UUID id) {
        memberServiceImpl.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member application rejected and removed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMember(@PathVariable UUID id) {
        return ResponseEntity.ok(memberServiceImpl.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable UUID id, @Valid @RequestBody MemberDtoReq memberDtoReq) {
        memberDtoReq = memberServiceImpl.update(memberDtoReq, id);
        Map<String, Object> response = new HashMap<>();
        response.put("data", memberDtoReq);
        response.put("message", "Member updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable UUID id) {
        memberServiceImpl.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Member deleted successfully");
        return ResponseEntity.ok(response);
    }
}
