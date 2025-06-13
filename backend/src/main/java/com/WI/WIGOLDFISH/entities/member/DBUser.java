package com.WI.WIGOLDFISH.entities.member;

import com.WI.WIGOLDFISH.entities.hunting.Hunting;
import com.WI.WIGOLDFISH.entities.ranking.Ranking;
import com.WI.WIGOLDFISH.enums.IndentityDocumentType;
import com.WI.WIGOLDFISH.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "member")
public class DBUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;
    private String username;
    private String familyName;
    private String password;
    private LocalDate accessionDate;
    private String nationality;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private IndentityDocumentType indentityDocumentType;
    private String indentityNumber;
    @OneToMany(mappedBy = "member")
    private List<Ranking> rankings;
    @OneToMany(mappedBy = "member")
    private List<Hunting> huntings;
    public DBUser(UUID num) {
        this.id = num;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
