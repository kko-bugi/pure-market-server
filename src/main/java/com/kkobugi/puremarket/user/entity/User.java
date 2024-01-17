package com.kkobugi.puremarket.user.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(length = 30)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;
    private String contact;
}
