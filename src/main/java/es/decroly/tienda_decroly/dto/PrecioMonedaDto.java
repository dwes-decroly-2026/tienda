package es.decroly.tienda_decroly.dto;

public record PrecioMonedaDto(Long idProducto, String moneda,
                              double tipoCambio, double total) {
}
