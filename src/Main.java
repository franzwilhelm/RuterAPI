import org.sintef.jarduino.JArduino;

public class Main {
    private static RuterAPI ruterAPI;
    private static int currentHour;
    private static int currentMinute;

    public static void main(String[] args) throws Exception {
        currentHour = 16;
        currentMinute = 43;

        ruterAPI = new RuterAPI();
        RuterAPI.MonitoredStopVisit[] ullevål = ruterAPI.getDepartures("ullevål stadion,");
        System.out.println("Aimed: " + ruterAPI.getAimDepHour(ullevål) + ":" + ruterAPI.getAimDepMinute(ullevål));
        if(ruterAPI.hasRealTime(ullevål)) {
            System.out.println(" Real: " + ruterAPI.getEstDepHour(ullevål) + ":" + ruterAPI.getEstDepMinute(ullevål));
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