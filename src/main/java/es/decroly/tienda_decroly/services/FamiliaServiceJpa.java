package es.decroly.tienda_decroly.services;

import es.decroly.tienda_decroly.domain.Familia;
import es.decroly.tienda_decroly.exceptions.PeticionIncorrectaException;
import es.decroly.tienda_decroly.exceptions.RecursoNoEncontradoException;
import es.decroly.tienda_decroly.repositories.FamiliaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamiliaServiceJpa  implements  FamiliaService{
    private final FamiliaRepository familiaRepository;
    @Override
    public List<Familia> listar() {
        return familiaRepository.findAll();
    }

    @Override
    public Familia buscarPorId(Long id) {
        Familia familia = familiaRepository.findById(id).orElse(null);
        if (familia == null) {
            throw new RecursoNoEncontradoException(
                    "No existe la familia " + id);
        }
        return familia;
    }

    @Override
    public Familia crear(Familia familia) {
        validar(familia);
        familia.setId(null);                  // el id lo asigna MySQL
        return familiaRepository.save(familia);

    }

    @Override
    public Familia modificar(Long id, Familia datos) {
        validar(datos);
        Familia familia = buscarPorId(id);    // 404 si no existe
        familia.setNombre(datos.getNombre());
        familia.setDescripcion(datos.getDescripcion());
        return familiaRepository.save(familia);
    }

    @Override
    public void borrar(Long id) {
        if (!familiaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe la familia " + id);
        }
        familiaRepository.deleteById(id);

    }
    private void validar(Familia f) {
        if (f.getNombre() == null || f.getNombre().isBlank()) {
            throw new PeticionIncorrectaException("Nombre obligatorio");
        }
    }

}
