package org.dive2025.qdeep.domain.user.repository;

import org.dive2025.qdeep.domain.user.Vo.Nickname;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.dive2025.qdeep.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(Nickname nickname);

    @Query("SELECT u FROM User u ORDER BY u.amountOfFirst DESC")
    List<User> findTop10ByOrderByAmountOfFirstDesc(Pageable pageable);
}
