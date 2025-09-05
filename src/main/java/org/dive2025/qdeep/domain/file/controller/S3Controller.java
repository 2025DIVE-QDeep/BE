package org.dive2025.qdeep.domain.file.controller;

import org.dive2025.qdeep.domain.file.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(List<MultipartFile> multipartFiles){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3Service.uploadFile(multipartFiles));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam String param){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3Service.deleteFile(param));
    }
}
