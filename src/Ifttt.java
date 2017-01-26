import java.io.FileNotFoundException;

class Ifttt {
    private String gmailUsername;
    private int run, walk;
    private boolean isRunTrue, isWalkTrue;

    Ifttt(String gmailUsername) {
        this.gmailUsername = gmailUsername;
        isRunTrue = getBetween(2, 4, "run");
        isWalkTrue = getBetween(5, 40, "walk");
        System.out.println("isWalkTrue = " + isWalkTrue);
        System.out.println("isRunTrue = " + isRunTrue);
        System.out.println("RuterAPI.Departures.currTime = " + RuterAPI.Departures.currTime);
    }

    void toSlack() throws FileNotFoundException {
        String s = " " + "_*" + RuterAPI.Departures.currTime + "*_" + "        _est._        " + getArrivalTime(walk) + " \n" +
                "Hjem --------------> UiO IFI\n" +
                " " + "_*" + RuterAPI.Departures.currTime + "*_" + "        _tidl._        ";
        if (isRunTrue) s += getArrivalTime(run);
        else s += getArrivalTime(walk);
        Mail ifttt = new Mail("#tilSlack", gmailUsername, "        Franz er på vei", s);
        ifttt.send();
    }


    void toiPhone() throws FileNotFoundException {
        String body = "";
        if (isRunTrue)
            body += "LØP: " + RuterAPI.Departures.lineRef.get(run) + " " + RuterAPI.Departures.destName.get(run) + " om " + RuterAPI.Departures.mDif.get(run) + " min\n";
        if (isWalkTrue)
            body += "GÅ: " + RuterAPI.Departures.lineRef.get(walk) + " " + RuterAPI.Departures.destName.get(walk) + " om " + RuterAPI.Departures.mDif.get(walk) + " min";
        if (!isWalkTrue && !isRunTrue) body += "Fant ingen avganger de neste 20 minuttene";
        Mail ifttt = new Mail("#tiliPhone", gmailUsername, "none", body);
        ifttt.send();
    }

    private boolean getBetween(int min, int max, String type) {
        for (int i = 0; i < RuterAPI.Departures.numberOfDepartures; i++) {
            if (min <= RuterAPI.Departures.mDif.get(i) && RuterAPI.Departures.mDif.get(i) <= max) {
                if (type.equals("walk")) walk = i;
                if (type.equals("run")) run = i;
                return true;
            }
        }
        return false;
    }

    private String getArrivalTime(int i) {
        int ankMinInt = RuterAPI.Departures.expMinute.get(i) + 8;
        int ankHourInt = RuterAPI.Departures.currHour;
        if (RuterAPI.Departures.hDif.get(i) == 1) {
            ankHourInt++;
        }
        String ankomstMin = "" + ankMinInt;
        String ankomstHour = "" + ankHourInt;
        if (ankHourInt < 10) ankomstHour = "0" + ankHourInt;
        if (ankMinInt < 10) ankomstMin = "0" + ankMinInt;
        return "_* " + ankomstHour + ":" + ankomstMin + "*_";
    }
}