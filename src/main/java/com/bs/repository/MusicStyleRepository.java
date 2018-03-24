package com.bs.repository;

import com.bs.entity.MusicStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicStyleRepository extends JpaRepository<MusicStyle,Long>{
    MusicStyle findOneByMusicId(Long musicId);

}
