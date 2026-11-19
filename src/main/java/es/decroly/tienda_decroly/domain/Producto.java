package es.decroly.tienda_decroly.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(description = "Producto del catalogo de la tienda")
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Schema(description = "Identificador asignado por la tienda",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Schema(description = "Nombre comercial", example = "Teclado mecanico")
    @Column(nullable = false, length = 150)
    private String nombre;

    @Schema(description = "Precio sin IVA en euros", example = "49.90")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Schema(description = "Unidades disponibles", example = "15")
    @Column(nullable = false)
    private int stock ;

}
