import org.sintef.jarduino.JArduino;

import java.io.FileNotFoundException;

class Main {
    private static final String GMAIL_USERNAME = "franz.vonderlippe@gmail.com";
    private static final String MY_NAME = "Franz von der Lippe";
    private static final String stop = "jernbanetorget,B.Gunnerus g.";
    private static final int
            RUN = 1,
            WALK = 2,
            BOTH = 3;

    public static void main(String[] args) throws Exception {
        RuterAPI api = new RuterAPI();
        //JArduino arduino = new Blink("COM4");
        //arduino.runArduinoProcess();

        RuterAPI.MonitoredStopVisit[] rightDepartures = api.getRightDirectionDepartures(stop, "1");
        RuterAPI.Departures.initDepartures(rightDepartures, 1);
        Ifttt ifttt = new Ifttt(GMAIL_USERNAME);
        ifttt.toSlack();
        ifttt.toiPhone();
    }
}