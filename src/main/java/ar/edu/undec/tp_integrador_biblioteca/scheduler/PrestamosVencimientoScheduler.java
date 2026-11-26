package ar.edu.undec.tp_integrador_biblioteca.scheduler;

import ar.edu.undec.tp_integrador_biblioteca.entity.Estado;
import ar.edu.undec.tp_integrador_biblioteca.repository.PrestamosRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrestamosVencimientoScheduler {
    private final PrestamosRepository prestamosRepository;

    // Todos los dias a las 00:05 de Argentina
    @Scheduled(cron = "0 5 0 * * *", zone = "America/Argentina/Buenos_Aires")
    @Transactional
    public void marcarPrestamosVencidos() {
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        int actualizados = prestamosRepository.marcarVencidosPorFecha(
                Estado.ACTIVO,
                Estado.VENCIDO,
                hoy
        );

        log.info("Revision de vencimientos ({}): {} prestamos pasados a VENCIDO", hoy, actualizados);
    }
}