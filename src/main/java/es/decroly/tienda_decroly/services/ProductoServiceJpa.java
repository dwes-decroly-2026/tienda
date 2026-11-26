package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Familia;
import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PaginaProductosDto;
import es.decroly.tienda_decroly.dto.ProductoPeticionDto;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import es.decroly.tienda_decroly.repositories.FamiliaRepository;
import es.decroly.tienda_decroly.repositories.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final FamiliaRepository familiaRepository;
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
    public Producto crear(ProductoPeticionDto productoDto) {
        Producto producto = new Producto();        // nuevo: id null
        copiarDatos(productoDto, producto);
        return productoRepository.save(producto);

    }

    @Override
    public Producto modificar(Long id, ProductoPeticionDto datos) {
        Producto producto = buscarPorId(id);       // 404 si no existe
        copiarDatos(datos, producto);
        return productoRepository.save(producto);
    }

    /*@Override
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
    }*/

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

    @Override
    public List<Producto> listarMasCarosQueLaMedia() {
       return productoRepository.buscarMasCarosQueLaMedia();

    }

    @Override
    public List<Producto> buscarEntrePrecios(BigDecimal min, BigDecimal max) {
        if (min.compareTo(max) > 0) {
            throw new PeticionIncorrectaException(
                    "El precio minimo no puede ser mayor que el maximo");
        }
        return productoRepository.buscarEntrePrecios(min, max);
    }

    @Override
    public PaginaProductosDto listarPagina(int pagina, int tamano, String orden) {
        if (pagina < 0 || tamano < 1 || tamano > 50) {
            throw new PeticionIncorrectaException(
                    "Pagina o tamano no validos (tamano entre 1 y 50)");
        }
        if (!orden.equals("nombre") && !orden.equals("precio")
                && !orden.equals("stock")) {
            throw new PeticionIncorrectaException(
                    "Solo se puede ordenar por nombre, precio o stock");
        }
        PageRequest peticion = PageRequest.of(pagina, tamano, Sort.by(orden));
        Page<Producto> resultado = productoRepository.findAll(peticion);
        return new PaginaProductosDto(resultado.getContent(),
                resultado.getNumber(), resultado.getSize(),
                resultado.getTotalElements(), resultado.getTotalPages());

    }

    @Override
    public List<Producto> listarPorFamilia(Long familiaId) {
        return productoRepository.findByFamiliaId(familiaId);
    }


    private void copiarDatos(ProductoPeticionDto peticion, Producto producto) {
        Familia familia =
                familiaRepository.findById(peticion.familiaId()).orElse(null);
        if (familia == null) {
            throw new PeticionIncorrectaException(
                    "No existe la familia " + peticion.familiaId());
        }
        producto.setNombre(peticion.nombre());
        producto.setPrecio(peticion.precio());
        producto.setStock(peticion.stock());
        producto.setFamilia(familia);
    }


}
