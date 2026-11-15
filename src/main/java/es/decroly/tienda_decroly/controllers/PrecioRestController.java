package es.decroly.tienda_decroly.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrecioRestController {

    private static final Logger log =
            LoggerFactory.getLogger(PrecioRestController.class);

    // Ejemplo: http://localhost:8080/precio/100?iva=21
    @GetMapping("/precio/{base}")
    public String calcularPrecio(
            @PathVariable double base,
            @RequestParam(defaultValue = "21") int iva) {
        log.info("Calculando precio: base={} iva={}", base, iva);
        double total = base * (1 + iva / 100.0);
        return "Sin IVA: " + base + " EUR. Con IVA: " + total + " EUR";
    }
}
