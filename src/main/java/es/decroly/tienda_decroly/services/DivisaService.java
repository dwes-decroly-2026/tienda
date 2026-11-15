package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.config.ApisProperties;
import es.decroly.tienda_decroly.dto.TipoCambioDto;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.ServicioExternoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Slf4j
@Service
public class DivisaService {

    private final RestClient cliente;

    public DivisaService(ApisProperties apis) {
        Duration espera = Duration.ofSeconds(apis.timeoutSegundos());
        SimpleClientHttpRequestFactory fabrica =
                new SimpleClientHttpRequestFactory();
        fabrica.setConnectTimeout(espera);
        fabrica.setReadTimeout(espera);
        this.cliente = RestClient.builder()
                .baseUrl(apis.divisasUrl())
                .requestFactory(fabrica)
                .build();
    }

    public double tipoDeCambio(String moneda) {
        try {
            TipoCambioDto respuesta = cliente.get()
                    .uri("/latest?base=EUR&symbols={moneda}", moneda)
                    .retrieve()
                    .body(TipoCambioDto.class);
            Double tipo = respuesta.rates().get(moneda);
            if (tipo == null) {
                throw new PeticionIncorrectaException(
                        "Moneda no admitida: " + moneda);
            }
            return tipo;
        } catch (HttpClientErrorException ex) {
            // 4xx: la API no reconoce la moneda que nos han pedido
            throw new PeticionIncorrectaException(
                    "Moneda no admitida: " + moneda);
        } catch (RestClientException ex) {
            // 5xx, tiempo de espera agotado o sin conexion
            log.error("Fallo al consultar el servicio de divisas", ex);
            throw new ServicioExternoException(
                    "El servicio de divisas no esta disponible");
        }
    }
}
