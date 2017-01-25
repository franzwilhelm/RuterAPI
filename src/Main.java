//import org.sintef.jarduino.JArduino;

class Main {
    public static void main(String[] args) throws Exception {
        String gmailUsername = "franz.vonderlippe@gmail.com";
        RuterAPI api = new RuterAPI();
        RuterAPI.Departure dep = new RuterAPI.Departure();
        RuterAPI.MonitoredStopVisit[] rightDepartures = api.getRightDirectionDepartures("ullevål stadion,", "1");
        Mail ifttt = new Mail("-----------------------------------------------------------------------------", gmailUsername);
        ifttt.send();

        for (int i = 0; i < rightDepartures.length; i++) {
            dep.initDeparture(rightDepartures, i);

            if (dep.mDif < 4) {
                System.out.println(dep.lineRef + " " + dep.destName + " om " + dep.mDif + "min");
                Mail ifttut = new Mail("lol", gmailUsername);
                ifttt.send();
                System.exit(0);
            }
        }

        //JArduino arduino = new Blink("COM3");
        //arduino.runArduinoProcess();
    }
}