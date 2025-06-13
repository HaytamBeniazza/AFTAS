package com.WI.WIGOLDFISH.repositories;


import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DBUserRepository extends JpaRepository<DBUser, UUID> {
    Optional<DBUser> findByUsername(String username);
    List<DBUser> findAllByRole(Role role);
    Optional<DBUser> findByIdAndRole(UUID id, Role role);
}
