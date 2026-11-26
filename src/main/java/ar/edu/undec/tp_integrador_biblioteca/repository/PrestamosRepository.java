package ar.edu.undec.tp_integrador_biblioteca.repository;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Prestamos;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamosRepository extends JpaRepository<Prestamos, Long>, JpaSpecificationExecutor<Prestamos> {
    @EntityGraph(attributePaths = {"libro"})
    List<Prestamos> findAllBySocioId(Long socioId);

    @Override
    @EntityGraph(attributePaths = {"libro", "socio"})
    List<Prestamos> findAll(Specification<Prestamos> spec);

    @EntityGraph(attributePaths = {"libro", "socio"})
    Optional<Prestamos> findWithLibroAndSocioById(Long id);

    boolean existsByLibroIdAndEstadoIn(Long libroId, List<Estado> estados);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                UPDATE Prestamos p
                SET p.estado = :nuevoEstado
                WHERE p.estado = :estadoActual
                  AND p.fechaDevolucion < :fechaActual
            """)
    int marcarVencidosPorFecha(Estado estadoActual, Estado nuevoEstado, LocalDate fechaActual);
}
