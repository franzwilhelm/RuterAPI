import org.sintef.jarduino.JArduino;

import java.util.Scanner;

public class Main {
    private static RuterAPI ruterAPI;
    private static int currentHour;
    private static int currentMinute;


    public static void main(String[] args) throws Exception {
        currentHour = 16;
        currentMinute = 43;
        ruterAPI = new RuterAPI();
        RuterAPI.NextDeparture nextDeparture;
        RuterAPI.MonitoredStopVisit[] direction1;

        while (true) {
            RuterAPI.MonitoredStopVisit[] u = ruterAPI.getStopVisit("ullevål stadion,");

            Scanner sc = new Scanner(System.in);
            String a = sc.nextLine();
            int b = Integer.parseInt(a);

            if (a.equalsIgnoreCase("x")) return;
            else {
                for (RuterAPI.MonitoredStopVisit m : u) {
                    if (m.MonitoredVehicleJourney.DirectionRef == "1"){

                    }
                }
                nextDeparture = new RuterAPI.NextDeparture(u, b);
            }

            System.out.println("Linename: " + nextDeparture.getLineRef() + " - " + nextDeparture.getDestinationName());
            System.out.println("Aimed: " + nextDeparture.getAimHour() + ":" + nextDeparture.getAimMinute());
            System.out.println(" Real: " + nextDeparture.getExpHour() + ":" + nextDeparture.getExpMinute());
        }


    }

    /*static private void loop() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String a = sc.nextLine();

            if (a.equalsIgnoreCase("exit")) return;
            else ruterAPI.printDepartures(a);
        }
    }*/

    void runArduino(String com) {
        JArduino arduino = new Blink(com);
        arduino.runArduinoProcess();
    }
}