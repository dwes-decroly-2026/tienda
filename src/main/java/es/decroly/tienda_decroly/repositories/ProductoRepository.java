package es.decroly.tienda_decroly.repositories;

import es.decroly.tienda_decroly.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreContainingIgnoreCase(String texto);
    List<Producto> findByPrecioLessThanEqual(BigDecimal precioMaximo);
    List<Producto> findByStockLessThanOrderByStockAsc(int limite);

    @Query("SELECT p FROM Producto p WHERE p.precio > "
            + "(SELECT AVG(p2.precio) FROM Producto p2) ORDER BY p.precio DESC")
    List<Producto> buscarMasCarosQueLaMedia();


    @Query("SELECT p FROM Producto p "
            + "WHERE p.precio BETWEEN :min AND :max ORDER BY p.precio")
    List<Producto> buscarEntrePrecios(@Param("min") BigDecimal min,
                                      @Param("max") BigDecimal max);

    List<Producto> findByFamiliaId(Long familiaId);
}

