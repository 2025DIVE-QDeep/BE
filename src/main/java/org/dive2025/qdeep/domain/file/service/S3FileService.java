package org.dive2025.qdeep.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.board.repository.BoardRepository;
import org.dive2025.qdeep.domain.file.entity.S3File;
import org.dive2025.qdeep.domain.file.repository.S3FileRepository;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3FileService {

    private final AmazonS3 amazonS3;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private S3FileRepository s3FileRepository;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<S3File> uploadFile(List<MultipartFile> multipartFiles, Board board){

        return Optional.ofNullable(multipartFiles)
                .orElse(Collections.emptyList())
                .stream()
                .map(
                        file->{

                            String key = "board/" + board.getId() + "/" + createFileUUID(file.getOriginalFilename());
                            ObjectMetadata objectMetadata = new ObjectMetadata();
                            objectMetadata.setContentLength(file.getSize());
                            objectMetadata.setContentType(file.getContentType());

                            try(InputStream inputStream = file.getInputStream()){
                                amazonS3.putObject(new PutObjectRequest(bucket,
                                        key,
                                        inputStream,
                                        objectMetadata)
                                        .withCannedAcl(CannedAccessControlList.PublicRead));

                            }catch (IOException e){
                                throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
                            }

                            S3File s3File = S3File
                                    .builder()
                                    .key(key)
                                    .uploadedAt(LocalDateTime.now())
                                    .size(file.getSize())
                                    .build();

                            board.addS3File(s3File);

                            return s3File;

                        }

                ).collect(Collectors.toList());

    }

    // 파일명 난수화 하기
    public String createFileUUID(String filename){
        return UUID.randomUUID().toString().concat(getFileExtension(filename));
    }

    // "." 유무 판단하기
    public String getFileExtension(String filename){
        try {
            return filename.substring(filename.indexOf("."));
        }catch (StringIndexOutOfBoundsException e){
            throw new CustomException(ErrorCode.UNSUITABLE_FILE_MATCHED);
        }
    }

    // 파일 삭제기능
    public String deleteFile(String fileName){
        amazonS3.deleteObject(new DeleteObjectRequest(bucket,fileName));
        log.info("[ 이미지 제거 실행 ]"+ " : "
                + fileName + " -> "
                + LocalDateTime.now());

        return "[ 삭제된 파일 ] : " + fileName;
    }


    public List<String> getImagesURL(Long boardId){

        List<S3File> files = s3FileRepository
                .findByBoardId(boardId);

        String baseURL = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/";

        return files.stream()
                .map(file->baseURL+file.getKey())
                .collect(Collectors.toList());
    }

    public List<String> getUrls(List<MultipartFile> files,Board board){

        List<S3File> uploadedFiles = uploadFile(files,board);
        boardRepository.save(board);

        return uploadedFiles
                .stream()
                .map(S3File::getKey)
                .collect(Collectors.toList());

    }
}
