package ar.edu.undec.tp_integrador_biblioteca.repository.specification;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Prestamos;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PrestamosSpecs {
    public static Specification<Prestamos> conSocioId(Long socioId) {
        return (root, query, cb) ->
                socioId == null ? null : cb.equal(root.get("socio").get("id"), socioId);
    }

    public static Specification<Prestamos> conEstados(List<Estado> estados) {
        return (root, query, cb) ->
                (estados == null || estados.isEmpty()) ? null : root.get("estado").in(estados);
    }

    public static Specification<Prestamos> conEstados(Estado estado) {
        return (root, query, cb) ->
                (estado == null) ? null : root.get("estado").equalTo(estado);
    }

    public static Specification<Prestamos> conMultaImpaga() {
        return (root, query, cb) ->
                cb.equal(root.get("multa").get("saldado"), false);
    }
}