package com.WI.WIGOLDFISH.repositories;

import com.WI.WIGOLDFISH.entities.competition.Competition;
import com.WI.WIGOLDFISH.entities.fish.Fish;
import com.WI.WIGOLDFISH.entities.hunting.Hunting;

import com.WI.WIGOLDFISH.entities.member.DBUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HuntingRepository extends JpaRepository<Hunting, Long> {
    Optional<Hunting> findByMemberAndCompetitionAndFish(DBUser member, Competition competition, Fish fish);
}
