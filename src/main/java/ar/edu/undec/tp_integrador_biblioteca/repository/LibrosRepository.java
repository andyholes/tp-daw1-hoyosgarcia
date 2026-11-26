package ar.edu.undec.tp_integrador_biblioteca.repository;

import ar.edu.undec.tp_integrador_biblioteca.dto.LibrosResponse;
import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.entity.Libros;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibrosRepository extends JpaRepository<Libros, Long>, JpaSpecificationExecutor<Libros> {
    boolean existsByIsbn(String isbn);

    @Query("""
                SELECT l FROM Libros l
                WHERE NOT EXISTS (
                    SELECT 1 FROM Prestamos p
                    WHERE p.libro = l
                      AND p.estado IN :estados
                )
            """)
    List<Libros> getAllLibrosSinPrestamosEnEstados(List<Estado> estados);

    @Query("""
                SELECT new ar.edu.undec.tp_integrador_biblioteca.dto.LibrosResponse(
                    l.id,
                    l.titulo,
                    l.autor,
                    l.isbn,
                    CAST(COUNT(p.id) AS integer)
                )
                FROM Libros l
                LEFT JOIN Prestamos p ON p.libro = l
                GROUP BY l.id, l.titulo, l.autor, l.isbn
                ORDER BY COUNT(p.id) DESC, l.titulo ASC
            """)
    List<LibrosResponse> findLibrosMasPrestados(Pageable pageable);
}
