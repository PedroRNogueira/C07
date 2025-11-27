package app;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Relogio {

    private static volatile String horario = "--:--:--";

    public static void iniciar() {
        Thread t = new Thread(() -> {
            while (true) {
                horario = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                try { Thread.sleep(1000); } catch (Exception e) {}
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static String getHorario() {
        return horario;
    }
}
