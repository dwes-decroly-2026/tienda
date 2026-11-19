package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.config.ApisProperties;
import es.decroly.tienda_decroly.domain.Producto;
import es.decroly.tienda_decroly.dto.ProductoExternoDto;
import es.decroly.tienda_decroly.dto.RespuestaProductosDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@ConditionalOnProperty(name = "tienda.productos.origen",
        havingValue = "api")
public class ProductoServiceApi extends ProductoServiceBase {

    public ProductoServiceApi(ApisProperties apis) {
        super(cargar(apis.catalogoUrl()));
    }

    private static List<Producto> cargar(String urlBase) {
        RestClient cliente = RestClient.create(urlBase);
        RespuestaProductosDto respuesta = cliente.get()
                .uri("/products?limit={limite}", 20)
                .retrieve()
                .body(RespuestaProductosDto.class);
        // Convertir cada producto externo en un producto de la tienda
        List<Producto> lista = new ArrayList<>();
        for (ProductoExternoDto p : respuesta.products()) {
            lista.add(new Producto(p.id(), p.title(),
                    p.price(), p.stock()));
        }
        return lista;
    }
}
