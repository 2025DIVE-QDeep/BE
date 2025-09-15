package org.dive2025.qdeep.domain.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.dive2025.qdeep.domain.file.entity.S3File;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","store_id"})
})
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime postedTime;
    private String content;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="store_id")
    private Store store;


    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<S3File> s3File = new ArrayList<>();

    public void addS3File(S3File file){
        if (this.s3File == null) {
            this.s3File = new ArrayList<>();
        }
        this.s3File.add(file);
        file.setBoard(this);
    }

}
