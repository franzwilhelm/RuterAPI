import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RuterAPI {
    private final HashMap<String, String> stops = new HashMap<>();

    RuterAPI() {
        initHashMap();
    }

    MonitoredStopVisit[] getRightDirectionDepartures(String stop, String direction) {
        ArrayList<MonitoredStopVisit> tmpDirection1 = new ArrayList<>();
        RuterAPI.MonitoredStopVisit[] rightDirection = null;
        RuterAPI.MonitoredStopVisit[] ullevål = getStopVisit(stop);

        for (RuterAPI.MonitoredStopVisit m : ullevål) {
            if (m.MonitoredVehicleJourney.Monitored) {
                if (m.MonitoredVehicleJourney.DirectionRef.equals(direction)) {
                    tmpDirection1.add(m);
                }
            }
            rightDirection = new RuterAPI.MonitoredStopVisit[tmpDirection1.size()];
        }

        for (int i = 0; i < tmpDirection1.size(); i++) {
            rightDirection[i] = tmpDirection1.get(i);
        }
        return rightDirection;
    }

    private String getId(String name) {
        return stops.get(name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    MonitoredStopVisit[] getStopVisit(String stop) {
        String getDepartures = null;
        try {
            getDepartures = readUrl("http://reisapi.ruter.no/StopVisit/GetDepartures/" + getId(stop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(getDepartures, MonitoredStopVisit[].class);
    }

    static class Departure {
        private static final int HOUR = 1;
        private static final int MINUTE = 2;
        String expHour;
        String expMinute;
        String aimHour;
        String aimMinute;
        String currHour;
        String currMinute;
        String currSec;
        String destName;
        String lineRef;
        int hDif;
        int mDif;

        void initDeparture(MonitoredStopVisit[] departures, int i) {
            String exp = departures[i].MonitoredVehicleJourney.MonitoredCall.ExpectedDepartureTime;
            String aim = departures[i].MonitoredVehicleJourney.MonitoredCall.AimedDepartureTime;
            String rec = departures[i].RecordedAtTime;
            lineRef = departures[i].MonitoredVehicleJourney.LineRef;
            destName = departures[i].MonitoredVehicleJourney.DestinationName;

            expHour = regEx(exp, HOUR);
            expMinute = regEx(exp, MINUTE);
            aimHour = regEx(aim, HOUR);
            aimMinute = regEx(aim, MINUTE);
            currHour = regEx(rec, HOUR);
            currMinute = regEx(rec, MINUTE);
            currSec = regEx2(rec);
            setDif();
        }

        String regEx(String comp, int group) {
            String reg = "T(\\d\\d):(\\d\\d)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(comp);
            m.find();
            return m.group(group);
        }

        String regEx2(String comp) {
            String reg = "T\\d\\d:\\d\\d:(\\d\\d)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(comp);
            m.find();
            return m.group(1);
        }

        private void setDif() {
            int inc = 0;
            if (Integer.parseInt(currSec) > 40) inc = 1;
            int currMinute = Integer.parseInt(this.currMinute) + inc;
            int currHour = Integer.parseInt(this.currHour);
            int expHour = Integer.parseInt(this.expHour);
            int expMinute = Integer.parseInt(this.expMinute);

            if (expHour == currHour) mDif = expMinute - currMinute;
            if (expHour == currHour+1 || ((currHour == 23) && expHour == currHour+1)) mDif = (60 - currMinute) + expMinute;
            else {
                hDif = expHour - currHour;
                mDif = expMinute - currMinute;
            }
        }
    }

    class MonitoredStopVisit {
        MonitoredVehicleJourney MonitoredVehicleJourney;
        String RecordedAtTime;
    }

    class MonitoredVehicleJourney {
        MonitoredCall MonitoredCall;
        String DestinationName;
        String LineRef;
        String DirectionRef;
        boolean Monitored;
    }

    class MonitoredCall {
        String AimedDepartureTime;
        String ExpectedDepartureTime;
    }

    private class Stoppested {
        //Place/GetStop/{Id}
        int X, Y, ID;
        String Zone, ShortName, Name, District, PlaceType;
        boolean IsHub;
    }

    private String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    private void initHashMap() {
        File f = new File("./data/stops.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            int counter = 0;
            for (int i = 8; i < s.length(); i++) {
                if (s.substring(i, i + 2).matches("," + "[\\d]")) {
                    break;
                }
                counter++;
            }
            stops.put(s.substring(8, counter + 8), s.substring(0, 7));
        }
    }

}
