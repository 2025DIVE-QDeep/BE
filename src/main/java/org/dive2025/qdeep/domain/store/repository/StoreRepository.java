package org.dive2025.qdeep.domain.store.repository;

import org.dive2025.qdeep.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long> {

    boolean existsByNameAndAddress(String name,String address);
    List<Store> findByAddressContaining(String addressPart);

    @Query("SELECT DISTINCT s FROM Store s LEFT JOIN FETCH s.board b LEFT JOIN FETCH b.user WHERE s.id = :storeId")
    Optional<Store> findByIdWithBoardsAndUsers(@Param("storeId") Long storeId);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.board WHERE s.id = :id")
    Optional<Store> findByIdWithBoards(@Param("id") Long id);

}
