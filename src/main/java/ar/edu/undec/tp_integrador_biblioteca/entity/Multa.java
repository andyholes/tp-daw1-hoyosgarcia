package ar.edu.undec.tp_integrador_biblioteca.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Multa {
    private boolean saldado;
    private BigDecimal monto;

    public Multa(BigDecimal monto) {
        this.monto = monto;
    }
}
