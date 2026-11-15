package es.decroly.tienda_decroly.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apis")
public record ApisProperties(String catalogoUrl,
                             String divisasUrl,
                             int timeoutSegundos) {
}
