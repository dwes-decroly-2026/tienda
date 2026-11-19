package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.dto.PrecioDto;
import es.decroly.tienda_decroly.dto.PrecioMonedaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        BigDecimal total = enEuros.total().multiply(BigDecimal.valueOf(tipo)).setScale(2, RoundingMode.HALF_UP);
        return new PrecioMonedaDto(idProducto, codigo, tipo, total);
    }
}
