//import org.sintef.jarduino.JArduino;

import java.io.FileNotFoundException;

class Main {
    private static RuterAPI.Departure dep;
    private static final String gmailUsername = "franz.vonderlippe@gmail.com";
    private static final String stop = "ullevål stadion,";
    private static String ankomstMin;
    private static String ankomstHour;
    private static String gå = "GÅ: ";
    private static String løp = "LØP: ";
    private static String dro = "Dro hjemmefra --------- *kl ";
    private static String senest = "Ankommer senest ----- *kl ";
    private static String tidligst = "Ankommer tidligst ----- *kl ";

    public static void main(String[] args) throws Exception {
        RuterAPI api = new RuterAPI();
        dep = new RuterAPI.Departure();
        RuterAPI.MonitoredStopVisit[] rightDepartures = api.getRightDirectionDepartures(stop, "1");

        boolean gåFinnes = false;
        boolean løpFinnes = false;
        dep.initDeparture(rightDepartures, 0);
        dro += dep.currTime + "*";

        for (int i = 0; i < rightDepartures.length; i++) {
            dep.initDeparture(rightDepartures, i);
            int ankMinInt = Integer.parseInt(dep.expMinute) + 8;
            int ankHourInt = Integer.parseInt(dep.currHour);
            if (ankMinInt >= 60) ankMinInt -= 60;
            ankHourInt++;
            ankomstMin = "" + ankMinInt;
            ankomstHour = "" + ankHourInt;
            if (ankHourInt < 10) ankomstHour = "0" + ankHourInt;
            if (ankMinInt < 10) ankomstMin = "0" + ankMinInt;
            if (dep.mDif >= 5 && dep.mDif < 15 && !gåFinnes) gåFinnes = iftttToiPhone(gå);
            if (dep.mDif < 5 && dep.mDif > 1 && !løpFinnes) løpFinnes = iftttToiPhone(løp);
        }

        if (!løpFinnes) tidligst = ""; System.out.println(tidligst);
        if (!gåFinnes) senest = "";
        String subject = "#tilSlack" + " Franz";
        Mail slack = new Mail(subject, "franz.vonderlippe@gmail.com", dro + "\n" + tidligst + "\n" + senest);
        slack.send();
    }

    private static boolean iftttToiPhone(String type) throws FileNotFoundException {
        type += dep.lineRef + " " + dep.destName + " om " + dep.mDif + " min";
        senest += ankomstHour + ":" + ankomstMin + "*";
        Mail ifttt = new Mail("#tiliPhone " + type, gmailUsername);
        ifttt.send();
        return true;
    }
}

//JArduino arduino = new Blink("COM3");
//arduino.runArduinoProcess();