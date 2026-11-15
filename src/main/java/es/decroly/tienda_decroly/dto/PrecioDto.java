package es.decroly.tienda_decroly.dto;

import java.math.BigDecimal;

public record PrecioDto(Long idProducto, double base,
                        double iva, double total) {
}
