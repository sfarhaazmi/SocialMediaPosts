package com.example.socialmediaposts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISpringDataPostRepository extends JpaRepository<PostEntity, Long> {
}
