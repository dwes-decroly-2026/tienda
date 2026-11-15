package es.decroly.tienda_decroly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TipoCambioDto(String base, String date,
                            Map<String, Double> rates) {
}
