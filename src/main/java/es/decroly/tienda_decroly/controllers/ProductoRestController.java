package es.decroly.tienda_decroly.controllers;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PrecioMonedaDto;
import es.decroly.tienda_decroly.services.PrecioDivisaService;
import es.decroly.tienda_decroly.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Producto> crear(@RequestBody Producto producto) {
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
            @RequestBody Producto datos) {
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

}
