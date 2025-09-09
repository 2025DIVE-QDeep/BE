package org.dive2025.qdeep.domain.board.service;

import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.board.dto.request.BoardListRequest;
import org.dive2025.qdeep.domain.board.dto.request.BoardRequest;
import org.dive2025.qdeep.domain.board.dto.response.BoardCreationResponse;
import org.dive2025.qdeep.domain.board.dto.response.BoardListResponse;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.board.repository.BoardRepository;
import org.dive2025.qdeep.domain.file.entity.S3File;
import org.dive2025.qdeep.domain.file.service.S3FileService;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.dive2025.qdeep.domain.user.entity.User;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3FileService s3FileService;

    @Transactional
    public BoardCreationResponse create(BoardRequest boardRequest, List<MultipartFile> files){

        Store store = storeRepository.findById(boardRequest.storeId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        User user = userRepository.findByUsername(boardRequest.username())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        Board board = Board.builder()
                .postedTime(LocalDateTime.now())
                .content(boardRequest.content())
                .store(store)
                .user(user)
                .build();

        user.addBoard(board); // oneToMany에 대해서 board 필드 추가
        store.addBoard(board); // oneToMany에 대해서 board 필드 추가

        try {
            boardRepository.save(board); // 또는 userRepository.save(user) + storeRepository.save(store)
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.ONLY_ONCE_REVIEW_PER_USER);
        }

        // 첫 작성자 체크
        if(store.getBoard().size() == 1) { // 방금 추가했으니까 size == 1이면 첫 작성자
            user.addAmountOfFirst();
            store.setFirstUserId(user.getId());
        } else {
            user.addAmountOfReview();
        }

        userRepository.save(user);
        storeRepository.save(store);

        List<S3File> uploadedFiles = s3FileService.uploadFile(files,board);
        boardRepository.save(board);

        List<String> urls = uploadedFiles
                .stream()
                .map(S3File::getKey)
                .collect(Collectors.toList());

        return new BoardCreationResponse(
                boardRequest.content(),
                user.getNickname().getNickname(),
                board.getPostedTime().toString(),
                store.getName(),
                store.getAddress(),
                store.getHours(),
                urls
        );
    }

    @Transactional(readOnly = true)
    public BoardListResponse showBoardByStore(BoardListRequest boardListRequest){

        Store store = storeRepository.findByIdWithBoards(boardListRequest.storeId())
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        return new BoardListResponse(store.getBoard());
    }



}
