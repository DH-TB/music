package com.bs.controller;

import com.bs.entity.Music;
import com.bs.entity.MusicStyle;
import com.bs.repository.MusicRepository;
import com.bs.repository.MusicStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MusicStyleController {
    @Autowired
    private MusicStyleRepository musicStyleRepository;

    //给音乐贴标签
    @PutMapping("/musicStyle")
    public ResponseEntity setMusicStyle(@RequestBody MusicStyle music){
        MusicStyle musics = musicStyleRepository.findOneByMusicId(music.getMusicId());

        musics.setBlues(musics.getBlues() + music.getBlues());
        musics.setClassical(musics.getClassical() + music.getClassical());
        musics.setFolk(musics.getFolk() + music.getFolk());
        musics.setHeavyMetal(musics.getHeavyMetal() + music.getHeavyMetal());
        musics.setHiphop(musics.getHiphop() + music.getHiphop());
        musics.setJazz(musics.getJazz() + music.getJazz());
        musics.setLight(musics.getLight() + music.getLight());
        musics.setPop(musics.getPop() + music.getPop());
        musics.setRock(musics.getRock() + music.getRock());
        musics.setRap(musics.getRap() + music.getRap());

        musicStyleRepository.save(musics);
        return new ResponseEntity(HttpStatus.OK);
    }
}
