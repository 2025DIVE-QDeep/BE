package org.dive2025.qdeep.domain.file.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.board.entity.Board;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",nullable = false)
    @JsonBackReference
    private Board board;

    @Column(name = "s3File_key",nullable = false)
    private String key;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false,updatable = false)
    private LocalDateTime uploadedAt;


}
