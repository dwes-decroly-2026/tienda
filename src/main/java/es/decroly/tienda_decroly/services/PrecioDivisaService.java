package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.dto.PrecioDto;
import es.decroly.tienda_decroly.dto.PrecioMonedaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrecioDivisaService {

    private final PrecioService precioService;
    private final DivisaService divisaService;

    public PrecioMonedaDto calcular(Long idProducto, String moneda) {
        PrecioDto enEuros = precioService.calcular(idProducto);
        if (moneda.equalsIgnoreCase("EUR")) {
            return new PrecioMonedaDto(idProducto, "EUR", 1.0,
                    enEuros.total());
        }
        String codigo = moneda.toUpperCase();
        double tipo = divisaService.tipoDeCambio(codigo);
        double total = Math.round(enEuros.total() * tipo * 100) / 100.0;
        return new PrecioMonedaDto(idProducto, codigo, tipo, total);
    }
}
