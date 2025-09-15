package org.dive2025.qdeep.domain.favorite.repository;

import jakarta.persistence.EntityManager;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.favorite.entity.Favorite;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    @Query("SELECT f.id FROM Favorite f WHERE f.user.id = :userId AND f.store.id = :storeId")
    Optional<Long> findByUserIdAndStoreId(
            @Param("userId") Long userId,
            @Param("storeId") Long storeId
    );


}
