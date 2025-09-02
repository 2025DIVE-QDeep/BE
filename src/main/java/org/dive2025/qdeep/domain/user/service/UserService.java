package org.dive2025.qdeep.domain.user.service;

import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.board.repository.BoardRepository;
import org.dive2025.qdeep.domain.user.Vo.Nickname;
import org.dive2025.qdeep.domain.user.dto.request.DuplicationCheckRequest;
import org.dive2025.qdeep.domain.user.dto.request.UserCreateRequest;
import org.dive2025.qdeep.domain.user.dto.response.*;
import org.dive2025.qdeep.domain.user.entity.Role;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dive2025.qdeep.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request){

        User user = User.builder()
                .username(request.username())
                .nickname(new Nickname(request.nickname()))
                .password(bCryptPasswordEncoder.encode(request.password()))
                .creationDate(LocalDateTime.now())
                .role(Role.USER)
                .build();

        UserCreateResponse response =
                new UserCreateResponse(request.username(),
                        request.nickname(),
                        LocalDateTime.now().toString());

        userRepository.save(user);
        return response;
    }

    @Transactional(readOnly = true)
    public void checkUsername(DuplicationCheckRequest request){

        if(userRepository.findByUsername(request.content()).isPresent()){
            throw new CustomException(ErrorCode.USER_USERNAME_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public void checkNickname(DuplicationCheckRequest request){
        if(userRepository.findByNickname(new Nickname(request.content())).isPresent()){
            throw new CustomException(ErrorCode.USER_NICKNAME_DUPLICATED);
        }
    }

    @Transactional(readOnly = true)
    public UserInformationResponse showInformation(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserInformationResponse(user.getNickname().getNickname(),
                user.getAmountOfFirst(),
                user.getAmountOfReview());
    }

    @Transactional(readOnly = true)
    public UserReviewResponse showReviewListOfMine(String username){

        return new UserReviewResponse(boardRepository
                .findBoardByUser(userRepository
                        .findByUsername(username)
                        .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND))));

    }

    @Transactional(readOnly = true)
    public ShowRankingResponse showRanking(){

        Pageable pageable = PageRequest.of(0,10);
        List<User> topUsers = userRepository.findTop10ByOrderByAmountOfFirstDesc(pageable);

        List<UserRankingInfo> ranking = topUsers.stream()
                .map(user->new UserRankingInfo(
                        user.getNickname().getNickname(),
                        user.getAmountOfFirst(),
                        user.getAmountOfReview()
                )).collect(Collectors.toList());

        return new ShowRankingResponse(ranking);
    }

}
