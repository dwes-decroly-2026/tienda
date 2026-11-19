package es.decroly.tienda_decroly.config;

import es.decroly.tienda_decroly.domain.Producto;
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

    @Override
    public void run(String... args) {
        if (productoRepository.count() > 0) {
            log.info("La base de datos ya tiene productos");
            return;
        }
        List<Producto> guardados = productoRepository.saveAll(List.of(
                producto("Teclado mecanico", "49.90", 15),
                producto("Raton inalambrico", "19.95", 40),
                producto("Monitor 24 pulgadas", "139.00", 8),
                producto("Alfombrilla XL", "12.50", 60),
                producto("Webcam Full HD", "35.00", 20)));
        log.info("Cargados {} productos en MySQL", guardados.size());
    }

    private Producto producto(String nombre, String precio, int stock) {
        return new Producto(null, nombre, new BigDecimal(precio), stock);
    }
}
