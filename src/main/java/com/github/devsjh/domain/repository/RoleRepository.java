package com.github.devsjh.domain.repository;

import com.github.devsjh.domain.model.Role;
import com.github.devsjh.domain.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @memo: 계정 권한 레포지토리 클래스.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 계정 권한 조회 쿼리.
    Optional<Role> findByName(RoleType roleType);
}