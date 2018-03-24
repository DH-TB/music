package com.bs.controller;

import com.bs.entity.Collection;
import com.bs.repository.CollectionRepository;
import com.bs.repository.SignerLeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SignerLeaderController {
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SignerLeaderRepository signerLeaderRepository;

    List<Map> signerBoardArray = new ArrayList<>();

    public ResponseEntity setSignerLeaderBoard() {
        List<Collection> collections = collectionRepository.findAll();
        collections.forEach(c -> {
            HashMap<Long, Integer> signerBoardMap = new HashMap<Long, Integer>();
            //todo

            signerBoardArray.add(signerBoardMap);
        });
        return null;
    }

}
