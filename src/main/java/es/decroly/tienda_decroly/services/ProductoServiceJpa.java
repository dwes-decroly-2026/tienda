package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import es.decroly.tienda_decroly.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

@Service
@ConditionalOnProperty(name = "tienda.productos.origen", havingValue = "jpa")
@RequiredArgsConstructor
public class ProductoServiceJpa implements ProductoService {
    private final ProductoRepository productoRepository;

    @Override
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Override
    public Producto buscarPorId(Long id) {
        Producto producto =  productoRepository.findById(id).orElse(null);
        if (producto == null){
            throw new RecursoNoEncontradoException("No existe el producto con id " + id);
        }
        return producto;
    }

    @Override
    public Producto crear(Producto producto) {
        validar(producto);
        producto.setId(null);
        return productoRepository.save(producto);
    }

    @Override
    public Producto modificar(Long id, Producto datos) {
        validar(datos);
        Producto producto = buscarPorId(id);

        producto.setNombre( datos.getNombre());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        return productoRepository.save(producto);
    }

    @Override
    public void borrar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe el producto con id " + id);
        }
        //Otra forma alternativa de buscar si el producto existe para eliminarlo
        /*Producto producto = buscarPorId(id);
        if (producto==null){
            throw new RecursoNoEncontradoException("No existe el producto con id " + id);
        }}*/

        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> buscar(String texto, BigDecimal precioMaximo) {
        if (texto != null && !texto.isBlank()) {
            return productoRepository.findByNombreContainingIgnoreCase(texto);
        }
        if (precioMaximo != null) {
            return productoRepository.findByPrecioLessThanEqual(precioMaximo);
        }
        return productoRepository.findAll();      // sin filtros: todos
    }

    @Override
    public List<Producto> listarConStockBajo(int limite) {
        if (limite < 0) {
            throw new PeticionIncorrectaException("Limite negativo");
        }
        return productoRepository.findByStockLessThanOrderByStockAsc(limite);
    }


    private void validar(Producto p) {
        if (p.getNombre() == null || p.getNombre().isBlank()) {
            throw new PeticionIncorrectaException("Nombre obligatorio");
        }
        if (p.getPrecio() == null) {
            throw new PeticionIncorrectaException("Precio obligatorio");
        }
        if (p.getPrecio().signum() < 0) {
            throw new PeticionIncorrectaException("Precio negativo");
        }
    }

}
