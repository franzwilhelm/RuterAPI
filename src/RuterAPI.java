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
        ArrayList<MonitoredStopVisit> tmpDirection = new ArrayList<>();
        RuterAPI.MonitoredStopVisit[] rightDirection;
        RuterAPI.MonitoredStopVisit[] ullevål = getStopVisit(stop);

        for (RuterAPI.MonitoredStopVisit m : ullevål) {
            if (m.MonitoredVehicleJourney.Monitored) {
                if (m.MonitoredVehicleJourney.DirectionRef.equals(direction)) {
                    tmpDirection.add(m);
                }
            }
        }

        rightDirection = new RuterAPI.MonitoredStopVisit[tmpDirection.size()];
        for (int i = 0; i < tmpDirection.size(); i++) {
            rightDirection[i] = tmpDirection.get(i);
        }
        return rightDirection;
    }

    private String getId(String name) {
        return stops.get(name.substring(0, 1).toUpperCase() + name.substring(1));
    }

    private MonitoredStopVisit[] getStopVisit(String stop) {
        String getDepartures = null;
        try {
            getDepartures = readUrl("http://reisapi.ruter.no/StopVisit/GetDepartures/" + getId(stop));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        return gson.fromJson(getDepartures, MonitoredStopVisit[].class);
    }

    static class Departures {
        private static final int
                HOUR = 1,
                MINUTE = 2,
                SECONDS = 3;
        static int
                numberOfDepartures;
        static ArrayList<String>
                destName = new ArrayList<>(),
                lineRef = new ArrayList<>(),
                expTime = new ArrayList<>(),
                aimTime = new ArrayList<>();

        static ArrayList<Integer>
                expHour = new ArrayList<>(),
                expMinute = new ArrayList<>(),
                aimHour = new ArrayList<>(),
                aimMinute = new ArrayList<>(),
                hDif = new ArrayList<>(),
                mDif = new ArrayList<>();
        static String
                currTime;
        static int
                currHour,
                currMinute,
                currSec;

        static void initDepartures(MonitoredStopVisit[] departures, int numbOfDeps) {
            numberOfDepartures = numbOfDeps;
            System.out.println("----- INITIALIZING " + numbOfDeps + " DEPARTURES -----");

            String currReal = departures[0].RecordedAtTime;
            currTime = (regEx2(currReal));
            currHour = (regEx(currReal, HOUR));
            currMinute = (regEx(currReal, MINUTE));
            currSec = (regEx(currReal, SECONDS));

            for (int i = 0; i < numbOfDeps; i++) {
                String exp = departures[i].MonitoredVehicleJourney.MonitoredCall.ExpectedDepartureTime;
                String aim = departures[i].MonitoredVehicleJourney.MonitoredCall.AimedDepartureTime;
                destName.add(departures[i].MonitoredVehicleJourney.DestinationName);
                lineRef.add(departures[i].MonitoredVehicleJourney.LineRef);

                expTime.add(regEx2(exp));
                expHour.add(regEx(exp, HOUR));
                expMinute.add(regEx(exp, MINUTE));

                aimTime.add(regEx2(aim));
                aimHour.add(regEx(aim, HOUR));
                aimMinute.add(regEx(aim, MINUTE));

                setDif(i);
                System.out.println(expTime.get(i) + " --> " + lineRef.get(i) + " - " + destName.get(i) + " (aim: " + aimTime.get(i) + ")");
            }
        }

        private static int regEx(String comp, int group) {
            String reg = "T(\\d\\d):(\\d\\d):(\\d\\d)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(comp);
            m.find();
            return Integer.parseInt(m.group(group));
        }

        private static String regEx2(String comp) {
            String reg = "T(\\d\\d:\\d\\d):\\d\\d";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(comp);
            m.find();
            return m.group(1);
        }

        private static void setDif(int i) {
            int difM = 0;
            int difH = 0;
            int inc = 0;
            if (currSec > 30) inc = 1;
            int min = currMinute + inc;
            int hour = currHour;
            int eHour = expHour.get(i);
            int eMin = expMinute.get(i);
            if (eHour == hour) difM = eMin - min;
            if (eHour == hour + 1 || ((hour == 23) && eHour == 00)) {
                difM = 60 - min + eMin;
                difH = 1;
            } else {
                difH = eHour - hour;
                difM = eMin - min;
            }
            mDif.add(difM);
            hDif.add(difH);
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
        try {
            Scanner sc = new Scanner(f);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
