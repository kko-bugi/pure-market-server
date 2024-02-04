package com.kkobugi.puremarket.giveaway.repository;

import com.kkobugi.puremarket.giveaway.domain.entity.Giveaway;
import com.kkobugi.puremarket.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiveawayRepository extends JpaRepository<Giveaway, Long> {
    List<Giveaway> findTop4ByUserAndStatusEqualsOrderByCreatedDateDesc(User user, String status);
    List<Giveaway> findByStatusEqualsOrderByCreatedDateDesc(String status);
}
