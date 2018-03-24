package com.bs.controller;

import com.bs.entity.Collection;
import com.bs.repository.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MusicLeaderController {
    @Autowired
    private CollectionRepository collectionRepository;

}
