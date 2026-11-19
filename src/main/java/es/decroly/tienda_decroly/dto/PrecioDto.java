package es.decroly.tienda_decroly.dto;

import java.math.BigDecimal;

public record PrecioDto(Long idProducto, BigDecimal base,
                        double iva, BigDecimal total) {
}
