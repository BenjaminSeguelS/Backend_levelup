package com.levelup.backendapi;

import com.levelup.backendapi.model.Producto;
import com.levelup.backendapi.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository repository;

    public DatosIniciales(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Si la tabla está vacía, carga estos datos
        if (repository.count() == 0) {
            repository.save(new Producto("PC Gamer AMD Ryzen 7 8700G", "32GB RAM DDR5, 500GB NVMe 4.0", 1200000.0));
            repository.save(new Producto("PC Intel Core i7 RTX 4070", "16GB RAM, 1TB SSD", 1500000.0));
            repository.save(new Producto("Monitor Xiaomi G24i", "24\", Full HD 1920×1080", 150000.0));
            repository.save(new Producto("Mouse Razer Viper V3 Hyperspeed", "Inalámbrico de alta precisión", 89990.0));
            System.out.println(">>> 4 Productos iniciales cargados a la base de datos.");
        }
    }
}