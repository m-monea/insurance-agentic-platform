package com.insurance.ai;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalisiAiRepository extends JpaRepository<AnalisiAi, Long> {
    List<AnalisiAi> findBySinistroIdOrderByCreataIlDesc(Long sinistroId);
}