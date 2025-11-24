package com.levelup.backendapi.controller;

import com.levelup.backendapi.model.Producto;
import com.levelup.backendapi.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // 1. OBLIGATORIO: Indica que esta clase maneja peticiones REST
@RequestMapping("/api/productos") // 2. OBLIGATORIO: Define la ruta base para tu 404
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository; 

    @GetMapping // 3. OBLIGATORIO: Mapea la petición GET a la ruta base
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }
}