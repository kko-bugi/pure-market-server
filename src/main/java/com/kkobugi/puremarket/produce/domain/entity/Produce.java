package com.kkobugi.puremarket.produce.domain.entity;

import com.kkobugi.puremarket.common.BaseEntity;
import com.kkobugi.puremarket.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.Produce.FOR_SALE;
import static com.kkobugi.puremarket.common.constants.Constant.Produce.SOLD_OUT;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Produce extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long produceIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false, length = 32)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String produceImage;

    @Builder
    public Produce(User user, String title, String content, Integer price, String produceImage) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.price = price;
        this.produceImage = produceImage;
    }

    public void changeStatus(String currentStatus) {
        if (currentStatus.equals(FOR_SALE)) this.setStatus(SOLD_OUT);
        else if (currentStatus.equals(SOLD_OUT)) this.setStatus(FOR_SALE);
    }

    public void delete() {
        this.setStatus(INACTIVE);
    }
    public void modifyTitle(String title) { this.title = title; }
    public void modifyContent(String content) { this.content = content; }
    public void modifyPrice(Integer price) { this.price = price; }
    public void modifyImage(String imageUrl) { this.produceImage = imageUrl; }
}
