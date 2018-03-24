package com.bs.repository;

import com.bs.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music,Long>{
    List<Music> findByMusicNameContaining(String musicName);
    Music findByMusicName(String musicName);

}
