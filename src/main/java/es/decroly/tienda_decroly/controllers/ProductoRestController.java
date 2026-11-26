package es.decroly.tienda_decroly.controllers;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PaginaProductosDto;
import es.decroly.tienda_decroly.dto.PrecioMonedaDto;
import es.decroly.tienda_decroly.dto.ProductoPeticionDto;
import es.decroly.tienda_decroly.services.PrecioDivisaService;
import es.decroly.tienda_decroly.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Tag(name = "Productos", description = "Catalogo de productos")
@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductoService productoService;
    private final PrecioDivisaService precioDivisaService;

    @Operation(summary = "Listar todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }


    @Operation(summary = "Consultar un producto por su id")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404",
            description = "No existe el producto",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}")
    public Producto buscarPorId(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable Long id) {
        return productoService.buscarPorId(id);
    }


    @Operation(summary = "Crear un producto",
            description = "El id lo asigna la tienda; si el cuerpo trae uno, se ignora")
    @ApiResponse(responseCode = "201", description = "Producto creado")
    @ApiResponse(responseCode = "400",
            description = "Nombre vacio o precio negativo",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoPeticionDto producto) {
        Producto creado = productoService.crear(producto);
        URI direccion = URI.create("/api/productos/" + creado.getId());
        return ResponseEntity.created(direccion).body(creado);
    }


    @Operation(summary = "Modificar un producto")
    @ApiResponse(responseCode = "200", description = "Producto modificado")
    @ApiResponse(responseCode = "400",
            description = "Nombre vacio o precio negativo",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404",
            description = "No existe el producto",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @PutMapping("/{id}")
    public Producto modificar(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductoPeticionDto datos) {
        return productoService.modificar(id, datos);
    }

    @Operation(summary = "Borrar un producto")
    @ApiResponse(responseCode = "204", description = "Producto borrado")
    @ApiResponse(responseCode = "404",
            description = "No existe el producto",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable Long id) {
        productoService.borrar(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Precio con IVA de un producto, en euros o en otra moneda")
    @ApiResponse(responseCode = "200", description = "Precio calculado")
    @ApiResponse(responseCode = "400",
            description = "Moneda no admitida",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404",
            description = "No existe el producto",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "503",
            description = "El servicio de divisas no esta disponible",
            content = @Content(
                    schema = @Schema(implementation = ProblemDetail.class)))
    @GetMapping("/{id}/precio")
    public PrecioMonedaDto precio(
            @Parameter(description = "Id del producto", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Codigo de la moneda", example = "USD")
            @RequestParam(defaultValue = "EUR") String moneda) {
        return precioDivisaService.calcular(id, moneda);
    }

    // GET /api/productos/buscar?texto=raton
    // GET /api/productos/buscar?precioMaximo=20
    @GetMapping("/buscar")
    public List<Producto> buscar(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) BigDecimal precioMaximo) {
        return productoService.buscar(texto, precioMaximo);
    }

    // GET /api/productos/stock-bajo?limite=10
    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo(
            @RequestParam(defaultValue = "5") int limite) {
        return productoService.listarConStockBajo(limite);
    }

    // GET /api/productos/mas-caros
    @GetMapping("/mas-caros")
    public List<Producto> productosMasCaros() {
        return productoService.listarMasCarosQueLaMedia();
    }

    // GET /api/productos/sentre-precios?min=10&max=50
    @GetMapping("/entre-precios")
    public List<Producto> productosEntrePrecios(
            @RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        return productoService.buscarEntrePrecios(min, max);
    }

    // GET /api/productos/paginado?pagina=0&tamano=5&orden=precio
    @GetMapping("/paginado")
    public PaginaProductosDto paginado(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamano,
            @RequestParam(defaultValue = "nombre") String orden) {
        return productoService.listarPagina(pagina, tamano, orden);
    }

}
