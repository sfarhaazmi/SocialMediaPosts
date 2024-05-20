package com.example.socialmediaposts.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpringDataPostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
