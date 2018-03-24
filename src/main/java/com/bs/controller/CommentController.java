package com.bs.controller;

import com.bs.entity.Comment;
import com.bs.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

//   添加一条评论
    @PostMapping("/comment")
    public ResponseEntity addComment(@RequestBody Comment comment) {
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

//   获取当前歌曲的评论
    @GetMapping("/comment/{musicId}")
    public ResponseEntity getMusicComment(@PathVariable("musicId") Long musicId) {
        List<Comment> commentList = commentRepository.findByMusicIdOrderByIsLike(musicId);
        return new ResponseEntity<>(commentList,HttpStatus.OK);
    }

//   给评论点赞
    @PutMapping("/comment/{commentId}")
    public ResponseEntity deleteUser(@PathVariable("commentId") Long commentId){
        Comment comment = commentRepository.findOne(commentId);
        comment.setIsLike(comment.getIsLike()+1);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//  取消点赞
//  删除评论

}
