package com.kkobugi.puremarket.user.repository;

import com.kkobugi.puremarket.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<User> findByLoginId(String loginId);
    Optional<User> findByUserIdx(Long userIdx);
    Optional<User> findByUserIdxAndStatusEquals(Long userIdx, String status);
    Optional<User> findByLoginIdAndStatusEquals(String loginId, String status);
}
