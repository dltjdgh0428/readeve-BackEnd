package com.book_everywhere.domain.follow.repository;

import com.book_everywhere.domain.follow.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
}
