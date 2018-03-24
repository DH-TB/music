package com.bs.controller;

import com.bs.entity.Music;
import com.bs.entity.MusicLeaderBoard;
import com.bs.entity.MusicStyle;
import com.bs.entity.Signer;
import com.bs.exceptions.BusinessException;
import com.bs.repository.MusicLeaderRepository;
import com.bs.repository.MusicRepository;
import com.bs.repository.MusicStyleRepository;
import com.bs.repository.SignerRepository;
import com.bs.service.MusicCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/")
public class MusicController {
    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private SignerRepository signerRepository;

    @Autowired
    private MusicLeaderRepository musicLeaderRepository;

    @Autowired
    private MusicStyleRepository musicStyleRepository;

    @Autowired
    private MusicCenterService musicCenterService;


    //搜索时从本地获取返回歌名
    public List<String> getMusicName(List<Music> music, List<String> musicInfo) {
        music.forEach(m -> {
            String musicName1 = m.getMusicName();
            musicInfo.add(musicName1);
        });
        return musicInfo;
    }

    //搜索时模糊匹配歌曲
    @GetMapping("/music/{musicName}")
    public ResponseEntity getMusicContaining(@PathVariable("musicName") String musicName) throws BusinessException {
        List<Music> musics = musicRepository.findByMusicNameContaining(musicName);
        List<String> musicInfo = new ArrayList<>();

        if (musics.size() == 0) {
            List<Map> findMusic = musicCenterService.findMusic(musicName);
            if (findMusic.size() == 0) {
                throw new BusinessException("没有找到该音乐");
            } else {
                for (Map music1 : findMusic) {
                    Music newMusic = new Music();
                    Signer newSigner = new Signer();
                    MusicLeaderBoard newMusicLeaderBoard = new MusicLeaderBoard();
                    MusicStyle newMusicStyle = new MusicStyle();

                    newMusic.setMusicName((String) music1.get("music"));
                    String signerName = (String) music1.get("artist");

                    Signer isHaveSigner = signerRepository.findBySigner(signerName);

                    //如果没有该歌手，则保存,否则直接保存音乐
                    if (isHaveSigner == null) {
                        newSigner.setSigner(signerName);
                        //添加一条歌手
                        signerRepository.save(newSigner);

                    } else {
                        newMusic.setSingerId(isHaveSigner.getId());
                    }

                    //添加一条music
                    musicRepository.save(newMusic);
                    //添加一条排行榜
                    Music music2 = musicRepository.findByMusicName(newMusic.getMusicName());
                    Signer signer = signerRepository.findBySigner(newSigner.getSigner());
                    if (signer != null) {
                        newMusicLeaderBoard.setSignerId(signer.getId());
                    }
                    newMusicLeaderBoard.setMusicId(music2.getId());
                    musicLeaderRepository.save(newMusicLeaderBoard);

                    //初始化音乐style = 0
                    newMusicStyle.setMusicId(music2.getId());
                    newMusicStyle.setRap(0);
                    newMusicStyle.setRock(0);
                    newMusicStyle.setPop(0);
                    newMusicStyle.setLight(0);
                    newMusicStyle.setJazz(0);
                    newMusicStyle.setHiphop(0);
                    newMusicStyle.setHeavyMetal(0);
                    newMusicStyle.setFolk(0);
                    newMusicStyle.setClassical(0);
                    newMusicStyle.setBlues(0);
                    musicStyleRepository.save(newMusicStyle);
                }
            }
            List<Music> music1 = musicRepository.findByMusicNameContaining(musicName);

            return new ResponseEntity<>(getMusicName(music1, musicInfo), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(getMusicName(musics, musicInfo), HttpStatus.OK);
        }
    }

//    //向node service发请求获取音乐数据
//    public void getMusicFromNode(String musicName) throws BusinessException {
//
//    }
//

    //根据音乐名称获取信息
    @GetMapping("/musics/{music}")
    public ResponseEntity getOneMusic(@PathVariable("music") String music) throws BusinessException {
        Music music1 = musicRepository.findByMusicName(music);
        String album = music1.getAlbum();
        String musicName1 = music1.getMusicName();
        Signer signerInfo = signerRepository.findOne(music1.getSingerId());
        String signer = signerInfo.getSigner();

        Map musicInfo = new HashMap();
        musicInfo.put("album", album);
        musicInfo.put("signer", signer);
        musicInfo.put("music", musicName1);

        return new ResponseEntity<>(musicInfo, HttpStatus.OK);
    }
}
