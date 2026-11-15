package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.config.TiendaProperties;
import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PrecioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrecioService {

    private final ProductoService productoService;
    private final TiendaProperties tienda;

    public PrecioDto calcular(Long idProducto) {
        Producto producto = productoService.buscarPorId(idProducto);
        double base = producto.getPrecio();
        double total = base * (1 + tienda.iva() / 100);
        total = Math.round(total * 100) / 100.0; // redondeo a centimos
        return new PrecioDto(idProducto, base, tienda.iva(), total);
    }
}
