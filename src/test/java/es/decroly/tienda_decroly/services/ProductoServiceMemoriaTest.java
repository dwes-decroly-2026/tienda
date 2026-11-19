package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductoServiceMemoriaTest {

    private ProductoService servicio;

    @BeforeEach
    void preparar() {
        servicio = new ProductoServiceMemoria();
    }

    @Test
    void listarDevuelveLosProductosIniciales() {
        assertEquals(3, servicio.listar().size());
    }

    @Test
    void buscarPorIdExistenteDevuelveElProducto() {
        Producto producto = servicio.buscarPorId(1L);
        assertEquals("Teclado mecanico", producto.getNombre());
    }

    @Test
    void buscarPorIdInexistenteLanzaExcepcion() {
        try {
            servicio.buscarPorId(99L);
            fail("Deberia haber lanzado RecursoNoEncontradoException");
        } catch (RecursoNoEncontradoException ex) {
            // correcto
        }
    }

    @Test
    void crearAsignaIdYLoAnade() {
        Producto nuevo = new Producto(null, "Webcam", new BigDecimal("35.0"), 10);
        Producto creado = servicio.crear(nuevo);
        assertNotNull(creado.getId());
        assertEquals(4, servicio.listar().size());
    }

    @Test
    void crearConPrecioNegativoLanzaExcepcion() {
        Producto malo = new Producto(null, "Webcam", new BigDecimal("-5.0"), 10);
        try {
            servicio.crear(malo);
            fail("Deberia haber lanzado PeticionIncorrectaException");
        } catch (PeticionIncorrectaException ex) {
            // correcto
        }
    }

    @Test
    void borrarEliminaElProducto() {
        servicio.borrar(1L);
        try {
            servicio.buscarPorId(1L);
            fail("El producto 1 deberia haberse borrado");
        } catch (RecursoNoEncontradoException ex) {
            // correcto
        }
    }
}
