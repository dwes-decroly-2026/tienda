package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.config.TiendaProperties;
import es.decroly.tienda_decroly.dto.PrecioDto;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PrecioServiceTest {

    @Test
    void calcularAplicaElIvaConfigurado() {
        TiendaProperties config = new TiendaProperties("Prueba", 21, 50);
        PrecioService precios =
                new PrecioService(new ProductoServiceMemoria(), config);
        PrecioDto precio = precios.calcular(3L); // monitor de 139 EUR
        assertEquals(new BigDecimal("168.19"), precio.total());
    }

    @Test
    void calcularConIvaDelDiez() {
        TiendaProperties config = new TiendaProperties("Prueba", 10, 50);
        PrecioService precios =
                new PrecioService(new ProductoServiceMemoria(), config);
        PrecioDto precio = precios.calcular(3L);
        assertEquals(new BigDecimal("152.90"), precio.total());
    }

    @Test
    void precioDeProductoInexistenteLanzaExcepcion() {
        TiendaProperties config = new TiendaProperties("Prueba", 21, 50);
        PrecioService precios =
                new PrecioService(new ProductoServiceMemoria(), config);
        try {
            precios.calcular(99L);
            fail("Deberia haber lanzado RecursoNoEncontradoException");
        } catch (RecursoNoEncontradoException ex) {
            // correcto
        }
    }
}
