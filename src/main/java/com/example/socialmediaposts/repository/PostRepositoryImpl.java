package com.example.socialmediaposts.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class PostRepositoryImpl implements IPostRepository {
    private final ISpringDataPostRepository springDataPostRepository;

    @Autowired
    public PostRepositoryImpl(ISpringDataPostRepository springDataPostRepository) {
        this.springDataPostRepository = springDataPostRepository;
    }

    @Override
    public Page<PostEntity> findAllByOrderByCreatedAtDesc(Pageable pageable) {
        return springDataPostRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    @Transactional
    public PostEntity save(PostEntity postEntity) {
        return springDataPostRepository.save(postEntity);
    }

    @Override
    public Optional<PostEntity> findById(Long id) {
        return springDataPostRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataPostRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        springDataPostRepository.deleteById(id);
    }
}
