package es.decroly.tienda_decroly.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Producto del catalogo de la tienda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Schema(description = "Identificador asignado por la tienda",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre comercial", example = "Teclado mecanico")
    private String nombre;

    @Schema(description = "Precio sin IVA en euros", example = "49.90")
    private double precio;

    @Schema(description = "Unidades disponibles", example = "15")
    private int stock;

}
