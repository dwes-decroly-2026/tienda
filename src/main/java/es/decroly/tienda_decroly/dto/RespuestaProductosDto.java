package es.decroly.tienda_decroly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RespuestaProductosDto(List<ProductoExternoDto> products,
                                    int total) {
}
