package ar.edu.undec.tp_integrador_biblioteca.repository;

import ar.edu.undec.tp_integrador_biblioteca.entity.Socios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SociosRepository extends JpaRepository<Socios, Long> {
    boolean existsByEmail(String email);
}
