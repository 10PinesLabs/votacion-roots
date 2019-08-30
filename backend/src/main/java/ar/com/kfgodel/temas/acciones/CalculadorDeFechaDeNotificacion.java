package ar.com.kfgodel.temas.acciones;

import convention.persistent.Minuta;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class CalculadorDeFechaDeNotificacion {

    public static LocalDate calcularParaTemasNoTratados(Minuta unaMinuta) {
        return proximoDiaDeSemana(unaMinuta.getFecha());
    }

    private static LocalDate proximoDiaDeSemana(LocalDate unaFecha) {
        return unaFecha.getDayOfWeek() == DayOfWeek.FRIDAY
                ? unaFecha.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                : unaFecha.plusDays(1);
    }
}
