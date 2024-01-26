package com.kkobugi.puremarket.user.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.kkobugi.puremarket.common.constants.Constant.*;

@Entity
@Getter
@NoArgsConstructor
@DynamicInsert
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;
    private String contact;

    //TODO: 프로필 이미지 추가
    private String profileImage;

    @Builder
    public User(String nickname, String loginId, String password, String contact) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
        this.contact = contact;
    }

    public void login() {
        this.setStatus(ACTIVE);
    }

    public void logout() {
        this.setStatus(LOGOUT);
    }
    public void signout() { this.setStatus(INACTIVE); }
}
