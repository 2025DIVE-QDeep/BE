package org.dive2025.qdeep.domain.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String address;

    private String hours;

    private String description;

    private double latitude;

    private double longtitude;

    @JsonManagedReference
    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Board> board = new ArrayList<>(); // ⭐ 초기화 필수

    public void addBoard(Board board) {
        this.board.add(board);
        board.setStore(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private Long firstUserId;


}
