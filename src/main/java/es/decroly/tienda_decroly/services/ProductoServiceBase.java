package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PaginaProductosDto;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public abstract class ProductoServiceBase implements ProductoService {

    private final List<Producto> productos;
    private final AtomicLong secuencia;

    protected ProductoServiceBase(List<Producto> iniciales) {
        this.productos = new ArrayList<>(iniciales);
        long maxId = 0; // mayor id de los iniciales
        for (Producto p : iniciales) {
            if (p.getId() > maxId) {
                maxId = p.getId();
            }
        }
        this.secuencia = new AtomicLong(maxId);
    }

    @Override
    public List<Producto> listar() {
        return productos;
    }

    @Override
    public Producto buscarPorId(Long id) {
        for (Producto p : productos) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new RecursoNoEncontradoException(
                "No existe el producto " + id);
    }

    @Override
    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(secuencia.incrementAndGet());
        productos.add(producto);
        log.info("Producto creado: {}", producto.getId());
        return producto;
    }

    @Override
    public Producto modificar(Long id, Producto datos) {
        validar(datos);
        Producto producto = buscarPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        return producto;
    }

    @Override
    public void borrar(Long id) {
        Producto producto = buscarPorId(id);
        productos.remove(producto);
    }

    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new PeticionIncorrectaException("Nombre obligatorio");
        }
        if (p.getPrecio() == null || p.getPrecio().signum() < 0) {
            throw new PeticionIncorrectaException("Precio negativo");
        }
    }

    //Nuevos metodos añadidos para jpa pero ya no mantenemos memoria ni json, están sin implementar
    @Override
    public List<Producto> buscar(String texto, BigDecimal precioMaximo) {
        return List.of();
    }

    @Override
    public List<Producto> listarConStockBajo(int limite) {
        return List.of();
    }
    @Override
    public List<Producto> listarMasCarosQueLaMedia() {
        return List.of();
    }

    @Override
    public List<Producto> buscarEntrePrecios(BigDecimal min, BigDecimal max) {
        return List.of();
    }
    @Override
    public PaginaProductosDto listarPagina(int pagina, int tamano, String orden) {
        return null;
    }
}
