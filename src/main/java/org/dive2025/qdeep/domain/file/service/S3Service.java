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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadFile(List<MultipartFile> multipartFiles){
        List<String> fileNameList = multipartFiles.stream()
                .map(file->{
                    String fileName = createFileUUID(file.getOriginalFilename());
                    ObjectMetadata objectMetadata = new ObjectMetadata();
                    objectMetadata.setContentLength(file.getSize());
                    objectMetadata.setContentType(file.getContentType());

                    try(InputStream inputStream = file.getInputStream()){
                        amazonS3.putObject(new PutObjectRequest(bucket,
                                fileName,
                                inputStream,
                                objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));
                    }catch (IOException e){
                        throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
                    }

                    return fileName;
                })
                .collect(Collectors.toList());

        return fileNameList;
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


}
