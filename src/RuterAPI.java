import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuterAPI {
    private HashMap<String, String> stops = new HashMap<>();

    RuterAPI() {
        initHashMap();
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
        MonitoredStopVisit[] monitoredStopVisits = gson.fromJson(getDepartures, MonitoredStopVisit[].class);
        return monitoredStopVisits;
    }

    static class NextDeparture {
        private int expHour;
        private int expMinute;
        private int aimHour;
        private int aimMinute;
        private String DestinationName;
        private String LineRef;

        NextDeparture(MonitoredStopVisit[] departures, int i) {
            String exp = departures[i].MonitoredVehicleJourney.MonitoredCall.ExpectedDepartureTime;
            String aim = departures[i].MonitoredVehicleJourney.MonitoredCall.AimedDepartureTime;
            LineRef = departures[i].MonitoredVehicleJourney.LineRef;
            DestinationName = departures[i].MonitoredVehicleJourney.DestinationName;
            String reg = "T(\\d\\d):(\\d\\d)";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(exp);
            Matcher m2 = p.matcher(aim);
            m.find();
            m2.find();
            expHour = Integer.parseInt(m.group(1));
            expMinute = Integer.parseInt(m.group(2));
            aimHour = Integer.parseInt(m2.group(1));
            aimMinute = Integer.parseInt(m2.group(2));
        }

        public int getExpHour() {
            return expHour;
        }

        public int getExpMinute() {
            return expMinute;
        }

        public int getAimHour() {
            return aimHour;
        }

        public int getAimMinute() {
            return aimMinute;
        }

        public String getLineRef() {
            return LineRef;
        }

        public String getDestinationName() {
            return DestinationName;
        }
    }

    class MonitoredStopVisit {
        MonitoredVehicleJourney MonitoredVehicleJourney;
    }

    class MonitoredVehicleJourney {
        MonitoredCall MonitoredCall;
        String DestinationName;
        String LineRef;
        String DirectionRef;
    }

    class MonitoredCall {
        String AimedDepartureTime;
        String ExpectedDepartureTime;
    }

    class Stoppested {
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
            StringBuffer buffer = new StringBuffer();
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
