package es.decroly.tienda_decroly.repositories;

import es.decroly.tienda_decroly.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String texto);
    List<Producto> findByPrecioLessThanEqual(BigDecimal precioMaximo);
    List<Producto> findByExistenciasLessThanOrderByExistenciasAsc(int limite);
}

