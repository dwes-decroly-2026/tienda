package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Producto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "tienda.productos.origen",
        havingValue = "memoria", matchIfMissing = true)
public class ProductoServiceMemoria extends ProductoServiceBase {

    public ProductoServiceMemoria() {
        super(List.of(
                new Producto(1L, "Teclado mecanico", 49.90, 15),
                new Producto(2L, "Raton inalambrico", 19.95, 40),
                new Producto(3L, "Monitor 24 pulgadas", 139.00, 8)));
    }
}
