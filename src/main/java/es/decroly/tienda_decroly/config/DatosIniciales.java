package es.decroly.tienda_decroly.config;

import es.decroly.tienda_decroly.domain.Familia;
import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.repositories.FamiliaRepository;
import es.decroly.tienda_decroly.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatosIniciales implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final FamiliaRepository familiaRepository;
    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("La base de datos ya tiene datos");
            return;
        }
        Familia perifericos = familia("Perifericos", "Teclados, ratones...");
        Familia monitores = familia("Monitores", "Pantallas");
        Familia almacenamiento = familia("Almacenamiento", "Discos y USB");

        producto("Teclado mecanico", "49.90", 15, perifericos);
        producto("Raton inalambrico", "19.95", 40, perifericos);
        producto("Alfombrilla gaming", "12.50", 60, perifericos);
        producto("Monitor 24 pulgadas", "139.00", 8, monitores);
        producto("Disco SSD 1 TB", "89.95", 12, almacenamiento);
        log.info("Datos iniciales cargados");
    }

    private Familia familia(String nombre, String descripcion) {
        Familia existente = familiaRepository.findByNombreIgnoreCase(nombre);
        if (existente != null) {
            return existente;                // ya estaba: la reutilizamos
        }
        return familiaRepository.save(new Familia(null, nombre, descripcion));
    }

    private void producto(String nombre, String precio, int stock,
                          Familia familia) {
        productoRepository.save(new Producto(null, nombre,
                new BigDecimal(precio), stock, familia));
    }

}
