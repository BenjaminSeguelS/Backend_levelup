package com.levelup.backendapi.controller;

import com.levelup.backendapi.model.Producto;
import com.levelup.backendapi.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation; // Para Swagger
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"http://localhost:3000", "http://18.206.237.91", "http://18.206.237.91:3000"})
@Tag(name = "Productos", description = "Catálogo y gestión de productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. OBTENER TODOS
    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. OBTENER UNO POR ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID")
    public Producto obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // 3. CREAR PRODUCTO
    @PostMapping
    @Operation(summary = "Agregar un nuevo producto")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }

    // 4. ACTUALIZAR PRODUCTO
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto detalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizamos los campos (Asegúrate que tu modelo Producto tenga estos campos)
        producto.setNombre(detalles.getNombre());
        producto.setDescripcion(detalles.getDescripcion());
        producto.setPrecio(detalles.getPrecio());
        // producto.setImagen(detalles.getImagen()); // Si tienes campo de imagen

        return productoRepository.save(producto);
    }

    // 5. ELIMINAR PRODUCTO
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public void eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
    }
}