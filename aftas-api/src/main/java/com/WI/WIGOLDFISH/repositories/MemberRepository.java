package com.WI.WIGOLDFISH.repositories;

import com.WI.WIGOLDFISH.entities.member.DBUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<DBUser, UUID> {
}
