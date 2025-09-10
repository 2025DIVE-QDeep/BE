package org.dive2025.qdeep.domain.file.repository;

import org.dive2025.qdeep.domain.file.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface S3FileRepository extends JpaRepository<S3File,Long> {


    List<S3File> findByBoardId(Long boardId);

}
