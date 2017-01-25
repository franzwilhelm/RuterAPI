//import org.sintef.jarduino.JArduino;

class Main {
    public static void main(String[] args) throws Exception {
        String gmailUsername = "franz.vonderlippe@gmail.com";
        RuterAPI api = new RuterAPI();
        RuterAPI.Departure dep = new RuterAPI.Departure();
        RuterAPI.MonitoredStopVisit[] rightDepartures = api.getRightDirectionDepartures("ullevål stadion,", "1");
        String gå = "GÅ: ";
        String løp = "LØP: ";
        boolean enBool = true;
        for (int i = 0; i < rightDepartures.length; i++) {
            dep.initDeparture(rightDepartures, i);
            if (dep.mDif >= 5 && dep.mDif < 10 && enBool) {
                gå += dep.lineRef + " " + dep.destName + " om " + dep.mDif + " min";
                enBool = false;
            }
            if (dep.mDif < 5 && dep.mDif > 1) {
                løp += dep.lineRef + " " + dep.destName + " om " + dep.mDif + " min";
                Mail ifttt = new Mail(løp, gmailUsername);
                ifttt.send();
            }
            
        }
        if (!enBool) {
            Mail ifttt = new Mail(gå, gmailUsername);
            ifttt.send();
        }
        //JArduino arduino = new Blink("COM3");
        //arduino.runArduinoProcess();
    }
}