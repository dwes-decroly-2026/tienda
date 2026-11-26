package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Familia;

import java.util.List;

public interface FamiliaService {

    List<Familia> listar();

    Familia buscarPorId(Long id);

    Familia crear(Familia familia);

    Familia modificar(Long id, Familia datos);

    void borrar(Long id);
}
