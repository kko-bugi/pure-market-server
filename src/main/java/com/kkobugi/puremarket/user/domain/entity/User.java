package com.kkobugi.puremarket.user.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.kkobugi.puremarket.common.constants.Constant.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private String profileImage;

    @Builder
    public User(String nickname, String loginId, String password, String contact, String profileImage) {
        this.nickname = nickname;
        this.loginId = loginId;
        this.password = password;
        this.contact = contact;
        this.profileImage = profileImage;
    }

    public void login() {
        this.setStatus(ACTIVE);
    }

    public void logout() {
        this.setStatus(LOGOUT);
    }
    public void signout() { this.setStatus(INACTIVE); }
}
