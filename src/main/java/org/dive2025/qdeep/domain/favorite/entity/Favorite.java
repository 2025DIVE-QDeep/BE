package org.dive2025.qdeep.domain.favorite.entity;

import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;

    private LocalDateTime addTime;

}
