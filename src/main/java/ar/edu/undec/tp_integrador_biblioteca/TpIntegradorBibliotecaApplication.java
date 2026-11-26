package ar.edu.undec.tp_integrador_biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TpIntegradorBibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpIntegradorBibliotecaApplication.class, args);
	}

}
