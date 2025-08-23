package org.dive2025.qdeep.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.user.Vo.Nickname;
import org.springframework.stereotype.Service;

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

    @Embedded
    private Nickname nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "creationDate",nullable = false)
    private LocalDateTime creationDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true) // JPA 고아객체 자동삭제
    private List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    private Integer amountOfFirst;
    private Integer amountOfReview;

    public void updateNickname(Nickname nickname){
        this.nickname = nickname;
    }

    public void addBoard(Board board){
        this.board.add(board);
        board.setUser(this);
    }

    public void addStore(Store store){
        if (!stores.contains(store)) {
            stores.add(store); // User의 리스트에 추가 store.setUser(this); // Store의 user 필드도 설정 } }
            store.setUser(this);
        }

    }

    public void removeStore(Store store) {
        if (stores.contains(store)) {
            stores.remove(store);
            store.setUser(null);    // 연관관계 끊기
        }
    }

    public void addAmountOfFirst() {
        if (this.amountOfFirst == null) this.amountOfFirst = 0;
        this.amountOfFirst += 1;
    }

    public void addAmountOfReview() {
        if (this.amountOfReview == null) this.amountOfReview = 0;
        this.amountOfReview += 1;
    }



}
