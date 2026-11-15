package es.decroly.tienda_decroly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductoExternoDto(Long id, String title,
                                 double price, int stock) {
}
