package com.kkobugi.puremarket.produce.repository;

import com.kkobugi.puremarket.produce.domain.entity.Produce;
import com.kkobugi.puremarket.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProduceRepository extends JpaRepository<Produce, Long> {
    List<Produce> findTop4ByUserAndStatusInOrderByCreatedDateDesc(User user, List<String> statuses);

    List<Produce> findByStatusEqualsOrderByCreatedDateDesc(String status);
    List<Produce> findTop4ByStatusEqualsOrderByCreatedDateDesc(String status);

}
