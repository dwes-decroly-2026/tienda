package es.decroly.tienda_decroly.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorGlobalErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ProblemDetail noEncontrado(RecursoNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso no encontrado");
        return problema;
    }

    @ExceptionHandler(PeticionIncorrectaException.class)
    public ProblemDetail incorrecta(PeticionIncorrectaException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        problema.setTitle("Peticion incorrecta");
        return problema;
    }

    @ExceptionHandler(ServicioExternoException.class)
    public ProblemDetail servicioExterno(ServicioExternoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        problema.setTitle("Servicio externo no disponible");
        return problema;
    }
}
