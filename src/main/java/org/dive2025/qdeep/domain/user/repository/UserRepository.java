package org.dive2025.qdeep.domain.user.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.user.Vo.Nickname;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.dive2025.qdeep.domain.user.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);

    @Query("SELECT u.id FROM User u WHERE u.id =:id")
    Optional<Long> findByIdOnly(@Param("id") Long id);


    Optional<User> findByUsername(String username);

    @Query("SELECT u.id FROM User u WHERE u.username = : username")
    Optional<Long> findByUsernameOnly(@Param("username") String username);

    @Query("SELECT u.username FROM User u WHERE u.username =:username")
    Optional<User> checkDuplicationUsername(@Param("username") String username);

    @Query("SELECT u.nickname FROM User u WHERE u.nickname =:nickname")
    Optional<User> checkDuplicationNickname(@Param("nickname") Nickname nickname);

    @Query("SELECT u FROM User u ORDER BY u.amountOfFirst DESC")
    List<User> findTop10ByOrderByAmountOfFirstDesc(Pageable pageable);

    // 헬퍼 메소드 도입
    default User getUserByfindByUsernameOnly(String username,EntityManager entityManager){
        Long id = findByUsernameOnly(username)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        return entityManager.getReference(User.class,id);

    }

    default User getUserByfindByIdOnly(Long userId,EntityManager entityManager){
        Long id = findByIdOnly(userId)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        return entityManager.getReference(User.class,id);
    }
}
