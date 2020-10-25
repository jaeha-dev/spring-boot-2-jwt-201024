package com.github.devsjh.domain.model;

import lombok.*;
import javax.persistence.*;

/**
 * @memo: 계정 권한 엔터티 클래스.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private RoleType name;
}