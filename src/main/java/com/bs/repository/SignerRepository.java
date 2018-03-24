package com.bs.repository;

import com.bs.entity.Signer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignerRepository extends JpaRepository<Signer, Long> {
    Signer findBySigner(String signer);
}
