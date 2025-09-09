package org.dive2025.qdeep.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.favorite.entity.Favorite;

import org.dive2025.qdeep.domain.user.Vo.Nickname;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    private Integer amountOfFirst;
    private Integer amountOfReview;

    @Embedded
    private Nickname nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "creationDate",nullable = false)
    private LocalDateTime creationDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true) // JPA 고아객체 자동삭제
    private List<Board> board = new ArrayList<>();

    public void addBoard(Board board){
        this.board.add(board);
        board.setUser(this);
    }

    @JsonManagedReference
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Favorite> favorite = new ArrayList<>();

    public void addFavorite(Favorite favorite){
        this.favorite.add(favorite);
        favorite.setUser(this);
    }

    public void updateNickname(Nickname nickname){
        this.nickname = nickname;
    }


    public void addAmountOfFirst() {
        if (this.amountOfFirst == null) this.amountOfFirst = 0;
        this.amountOfFirst += 1;
    }

    public void addAmountOfReview() {
        if (this.amountOfReview == null) this.amountOfReview = 0;
        this.amountOfReview += 1;
    }

    public void deleteFavorite(Favorite favorite){
        this.favorite.remove(favorite);
        favorite.setUser(this);

    }

}
