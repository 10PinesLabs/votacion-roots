package ar.com.kfgodel.temas.helpers;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class ClockMock {

    private Clock clock;

    public ClockMock(LocalDateTime unLocalDateTime) {
        clock = mock(Clock.class);
        setTiempo(unLocalDateTime);
    }

    public void setTiempo(LocalDateTime unLocalDateTime) {
        when(clock.instant()).thenReturn(unLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Clock getClock() {
        return clock;
    }
}
