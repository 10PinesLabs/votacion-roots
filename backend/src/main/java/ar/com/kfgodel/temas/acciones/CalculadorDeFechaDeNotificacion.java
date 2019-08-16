package ar.com.kfgodel.temas.acciones;

import convention.persistent.Minuta;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class CalculadorDeFechaDeNotificacion {

    public static LocalDate calcularParaTemasNoTratados(Minuta unaMinuta) {
        return proximoDiaDeSemana(unaMinuta.getFecha());
    }

    private static LocalDate proximoDiaDeSemana(LocalDate unaFecha) {
        LocalDate proximoDiaDeSemana = unaFecha;
        do {
            proximoDiaDeSemana = proximoDiaDeSemana.plusDays(1);
        } while (esFinDeSemana(proximoDiaDeSemana));
        return proximoDiaDeSemana;
    }

    private static Boolean esFinDeSemana(LocalDate fechaDeReunion) {
        return fechaDeReunion.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                fechaDeReunion.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }
}
