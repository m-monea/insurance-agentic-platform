package com.insurance.claim;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SinistroRepository extends JpaRepository<Sinistro, Long> {
    List<Sinistro> findByClienteId(Long clienteId);
}