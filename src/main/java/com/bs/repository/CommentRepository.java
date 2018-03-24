package com.bs.repository;

import com.bs.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>{
    List<Comment> findByMusicIdOrderByIsLike(Long musicId);
}
