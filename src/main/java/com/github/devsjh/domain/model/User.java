package com.github.devsjh.domain.model;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @memo: 계정 엔터티 클래스.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 생성 전략을 DB에 위임한다.
    private Long id;

    @NotBlank
    @Size(min = 5, max = 30)
    @Column(unique = true)
    private String username; // 계정 ID.

    @NotBlank
    @Size(max = 100)
    private String password; // 계정 비밀번호.

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( // User 엔터티의 기본키와 Role 엔터티의 기본 키를 사용해 매핑 테이블을 생성한다.
            name = "user_roles", // 매핑 테이블 이름.
            joinColumns = @JoinColumn(name = "user_id"), // 매핑 테이블에서 컬럼 이름은 user_id.
            inverseJoinColumns = @JoinColumn(name = "role_id")) // 매핑 테이블에서 컬럼 이름은 role_id.
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}