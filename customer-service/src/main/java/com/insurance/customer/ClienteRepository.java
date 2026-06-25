package com.insurance.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ClienteRepository extends JpaRepository<Cliente, Long> { List<Cliente> findByCognomeContainingIgnoreCase(String cognome); }