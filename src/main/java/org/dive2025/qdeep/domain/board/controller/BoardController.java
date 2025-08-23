package org.dive2025.qdeep.domain.board.controller;

import org.dive2025.qdeep.domain.board.dto.request.BoardListRequest;
import org.dive2025.qdeep.domain.board.dto.request.BoardRequest;
import org.dive2025.qdeep.domain.board.dto.response.BoardCreationResponse;
import org.dive2025.qdeep.domain.board.dto.response.BoardListResponse;
import org.dive2025.qdeep.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    // 글을 생성하는 메소드
    @PostMapping("/create")
    public ResponseEntity<BoardCreationResponse> create
    (@RequestBody BoardRequest boardRequest){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(boardService.create(boardRequest));
    }

    // Store과 관련된 board(리뷰)를 보여주는 메소드
    @GetMapping ("/list")
    ResponseEntity<BoardListResponse> showBoardByStore
            (@RequestBody BoardListRequest boardListRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(boardService.showBoardByStore(boardListRequest));
    }




}
