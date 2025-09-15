package org.dive2025.qdeep.domain.board.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.board.dto.request.BoardListRequest;
import org.dive2025.qdeep.domain.board.dto.request.BoardRequest;
import org.dive2025.qdeep.domain.board.dto.response.BoardCreationResponse;
import org.dive2025.qdeep.domain.board.entity.Board;
import org.dive2025.qdeep.domain.board.repository.BoardRepository;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public BoardCreationResponse create(BoardRequest boardRequest, List<MultipartFile> files){

        User user = userRepository.getUserByfindByUsernameOnly
                (boardRequest.username(),entityManager);

        Store store = storeRepository.getStoreByIdOnly
                (boardRequest.storeId(),entityManager);

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
        checkFirstReviewer(store,user);

        return new BoardCreationResponse(
                boardRequest.content(),
                user.getNickname().getNickname(),
                board.getPostedTime().toString(),
                store.getName(),
                store.getAddress(),
                store.getHours(),
                s3FileService.getUrls(files,board)
        );
    }

    @Transactional(readOnly = true)
    public List<Board> showBoardByStore(BoardListRequest boardListRequest){

        return boardRepository.findBoardsWithFilesByStoreId
                (boardListRequest.storeId());
    }

    public void checkFirstReviewer(Store store,User user){

        if(store.getBoard().size() == 1) { // 방금 추가했으니까 size == 1이면 첫 작성자
            user.addAmountOfFirst();
            store.setFirstUserId(user.getId());
        } else {
            user.addAmountOfReview();
        }

    }

}
