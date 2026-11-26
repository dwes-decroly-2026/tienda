package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.PaginaProductosDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Operaciones del catalogo de productos de la tienda.
 */
public interface ProductoService {

    /**
     * Devuelve todos los productos del catalogo.
     *
     * @return la lista de productos (puede estar vacia)
     */
    List<Producto> listar();

    /**
     * Busca un producto por su identificador.
     *
     * @param id identificador del producto
     * @return el producto encontrado
     * @throws es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException si no existe
     */
    Producto buscarPorId(Long id);

    /**
     * Da de alta un producto nuevo. El id lo asigna la tienda.
     *
     * @param producto datos del producto; su id se ignora
     * @return el producto creado, con su id
     * @throws es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException si el nombre
     *                                                                          esta vacio o el precio es negativo
     */
    Producto crear(Producto producto);

    /**
     * Modifica el nombre, el precio y el stock de un producto.
     *
     * @param id    identificador del producto que se modifica
     * @param datos datos nuevos; su id se ignora
     * @return el producto modificado
     * @throws es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException  si los datos no son validos
     * @throws es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException si no existe
     */
    Producto modificar(Long id, Producto datos);

    /**
     * Borra un producto.
     *
     * @param id identificador del producto
     * @throws es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException si no existe
     */
    void borrar(Long id);

    List<Producto> buscar(String texto, BigDecimal precioMaximo);

    List<Producto> listarConStockBajo(int limite);


    List<Producto> listarMasCarosQueLaMedia();

    List<Producto> buscarEntrePrecios(BigDecimal min, BigDecimal max);

    PaginaProductosDto listarPagina(int pagina, int tamano, String orden);
}
