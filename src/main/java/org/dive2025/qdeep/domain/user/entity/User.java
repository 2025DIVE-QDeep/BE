package org.dive2025.qdeep.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.dive2025.qdeep.domain.user.Vo.Nickname;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Embedded
    private Nickname nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "creationDate",nullable = false)
    private LocalDateTime creationDate;

    public void updateNickname(Nickname nickname){
        this.nickname = nickname;
    }

}
