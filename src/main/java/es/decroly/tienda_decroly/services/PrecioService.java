package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.config.TiendaProperties;
import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PrecioDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PrecioService {

    private final ProductoService productoService;
    private final TiendaProperties tienda;
    private static final BigDecimal CIEN = new BigDecimal("100");

    public PrecioDto calcular(Long idProducto) {
        Producto producto = productoService.buscarPorId(idProducto);
        BigDecimal base = producto.getPrecio();
        BigDecimal tipoIva = BigDecimal.valueOf(tienda.iva())
                .divide(CIEN, 4, RoundingMode.HALF_UP);          // 0.2100
        BigDecimal total = base.multiply(BigDecimal.ONE.add(tipoIva))
                .setScale(2, RoundingMode.HALF_UP);             // centimos
        return new PrecioDto(idProducto, base, tienda.iva(), total);
    }

}
