package org.dive2025.qdeep.domain.board.repository;

import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findBoardByUser(User user);

    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.s3File " +
            "WHERE b.store.id = :storeId")
    List<Board> findBoardsWithFilesByStoreId(@Param("storeId") Long storeId);


}

