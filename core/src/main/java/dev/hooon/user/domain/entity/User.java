package dev.hooon.user.domain.entity;

import dev.hooon.common.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Table(name = "user_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Enumerated(STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;
}
