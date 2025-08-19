package org.dive2025.qdeep.domain.user.service;

import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.user.Vo.Nickname;
import org.dive2025.qdeep.domain.user.dto.request.DuplicationCheckRequest;
import org.dive2025.qdeep.domain.user.dto.request.UserCreateRequest;
import org.dive2025.qdeep.domain.user.dto.response.UserCreateResponse;
import org.dive2025.qdeep.domain.user.entity.Role;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.dive2025.qdeep.domain.user.entity.User;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

}
