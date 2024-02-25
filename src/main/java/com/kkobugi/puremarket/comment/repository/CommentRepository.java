package com.kkobugi.puremarket.comment.repository;

import com.kkobugi.puremarket.comment.domain.entity.Comment;
import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByGiveawayOrderByCreatedDateAsc(Giveaway giveaway);
}
