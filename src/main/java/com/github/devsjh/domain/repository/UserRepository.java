package com.github.devsjh.domain.repository;

import com.github.devsjh.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @memo: 계정 레포지토리 클래스.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 계정 ID 중복 검사 쿼리.
    Boolean existsByUsername(String username);

    // 계정 조회 쿼리.
    Optional<User> findByUsername(String username);
}