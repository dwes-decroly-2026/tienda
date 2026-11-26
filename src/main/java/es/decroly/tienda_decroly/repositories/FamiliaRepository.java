package es.decroly.tienda_decroly.repositories;

import es.decroly.tienda_decroly.domain.Familia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamiliaRepository extends  JpaRepository <Familia, Long> {
    Familia findByNombreIgnoreCase(String nombre);
}
