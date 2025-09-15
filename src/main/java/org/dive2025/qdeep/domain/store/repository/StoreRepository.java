package org.dive2025.qdeep.domain.store.repository;

import jakarta.persistence.EntityManager;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.board.entity.Board;
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

    @Query("SELECT a.id FROM Store a WHERE a.id = :id")
    Optional<Long> findByIdOnly(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Store s LEFT JOIN FETCH s.board b LEFT JOIN FETCH b.user WHERE s.id = :storeId")
    Optional<Long> findByIdWithBoardsAndUsers(@Param("storeId") Long storeId);

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.board WHERE s.id = :id")
    Optional<Store> findByIdWithBoards(@Param("id") Long id);

    default Store getStoreByIdOnly(Long id, EntityManager entityManager){
        Long Storeid = findByIdOnly(id)
                .orElseThrow(()->new CustomException(ErrorCode.STORE_NOT_FOUND));

        return entityManager.getReference(Store.class,Storeid);
    }


}
