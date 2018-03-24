package com.bs.repository;

import com.bs.entity.SignerLeaderBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignerLeaderRepository extends JpaRepository<SignerLeaderBoard,Long>{
    List<SignerLeaderBoard> findByTagOrderByHot(String tag);


}
