package ar.edu.undec.tp_integrador_biblioteca.repository;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Prestamos;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PrestamosRepository extends JpaRepository<Prestamos, Long> {
    @EntityGraph(attributePaths = {"libro"})
    List<Prestamos> findAllBySocioId(Long socioId);

    List<Prestamos> findAllBySocioIdAndEstadoIn(Long socioId, List<Estado>estados);

    List<Prestamos> findAllBySocioIdAndMultaGreaterThan(Long socioId, BigDecimal montoMinimo);
    boolean existsByLibroIdAndEstadoIn(Long libroId, List<Estado> estados);
}
