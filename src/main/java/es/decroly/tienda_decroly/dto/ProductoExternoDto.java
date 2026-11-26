package es.decroly.tienda_decroly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductoExternoDto(Long id, String title,
                                 BigDecimal price, int stock, int stock2) {

}