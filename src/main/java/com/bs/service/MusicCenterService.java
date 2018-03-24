package com.bs.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MusicCenterService {

    public List findMusic(String musicName) {
        String url = "http://localhost:8888/music/"+musicName;
        RestTemplate template = new RestTemplate();
        ResponseEntity<List> result = template.getForEntity(url, List.class);
        return result.getBody();
    }
}
