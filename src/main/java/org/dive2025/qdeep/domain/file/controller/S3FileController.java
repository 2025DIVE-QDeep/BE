package org.dive2025.qdeep.domain.file.controller;

import org.dive2025.qdeep.domain.file.dto.request.LoadImagesRequest;
import org.dive2025.qdeep.domain.file.service.S3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/S3File")
public class S3FileController {

    @Autowired
    private S3FileService s3FileService;

    @GetMapping("/getImages")
    public ResponseEntity<?> getImages(@RequestBody LoadImagesRequest loadImagesRequest){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(s3FileService
                        .getImagesURL(loadImagesRequest.boardId()));
    }
}
