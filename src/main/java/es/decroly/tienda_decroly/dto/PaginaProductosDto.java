package es.decroly.tienda_decroly.dto;

import es.decroly.tienda_decroly.domain.Producto;
import java.util.List;

public record PaginaProductosDto(List<Producto> contenido,
                                 int pagina,
                                 int tamano,
                                 long totalElementos,
                                 int totalPaginas) {
}

