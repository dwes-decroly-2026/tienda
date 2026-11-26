package es.decroly.tienda_decroly.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Familia de productos de la tienda")
@Entity
@Table(name = "familias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Familia {

    @Schema(description = "Identificador asignado por MySQL",
            example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre de la familia", example = "Perifericos")
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Schema(description = "Descripcion opcional", example = "Teclados, ratones...")
    @Column(length = 255)
    private String descripcion;
}

