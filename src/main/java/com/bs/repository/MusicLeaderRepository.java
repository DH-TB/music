package com.bs.repository;

import com.bs.entity.MusicLeaderBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicLeaderRepository extends JpaRepository<MusicLeaderBoard,Long>{
    List<MusicLeaderBoard> findByTypeOrderByHot(String type);
    List<MusicLeaderBoard> findByTagOrderByHot(String tag);
    List<MusicLeaderBoard> findBySignerIdOrderByHot(Long signerId);
    MusicLeaderBoard findByMusicId(Long musicId);

}
