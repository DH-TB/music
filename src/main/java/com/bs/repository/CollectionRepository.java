package com.bs.repository;

import com.bs.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findByUserIdOrderByLove(Long userId);

    List<Collection> findByUserIdOrderByPlayCount(Long userId);

    List<Collection> findByMusicIdOrderByPlayCount(Long musicId);

    List<Collection> findByUserId(Long userId);
}
