package com.levelup.backendapi.repository;

import com.levelup.backendapi.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Hereda métodos como findAll(), save(), etc.
}