import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class RuterAPI {
    private HashMap<String, String> stops = new HashMap<>();

    RuterAPI() {
        initHashMap();
    }

    void printDepartures(String name) {
        String id = stops.get(name.substring(0, 1).toUpperCase() + name.substring(1));
        String getDepartures = null;
        try {
            getDepartures = readUrl("http://reisapi.ruter.no/StopVisit/GetDepartures/" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        MonitoredStopVisit[] monitoredStopVisits = gson.fromJson(getDepartures, MonitoredStopVisit[].class);

        for (MonitoredStopVisit m : monitoredStopVisits) {
            System.out.println(
                    "Ankomst --------------   " + m.MonitoredVehicleJourney.MonitoredCall.AimedDepartureTime.substring(11, 16) +
                            "\n" + "Finnes sanntiddata ---   " + m.MonitoredVehicleJourney.Monitored +
                            "\n" + "Hvem kjører bussen ---   " + m.MonitoredVehicleJourney.OperatorRef +
                            "\n" + "Navn på bussen -------   " + m.MonitoredVehicleJourney.PublishedLineName + " - " + m.MonitoredVehicleJourney.DestinationName +
                            "\n" + "_________________________________________");

        }
    }

    class MonitoredStopVisit {

        MonitoredVehicleJourney MonitoredVehicleJourney;
    }

    class MonitoredVehicleJourney {

        MonitoredCall MonitoredCall;
        String PublishedLineName;
        String OperatorRef;
        String DestinationName;
        boolean InCongestion;
        boolean Monitored;
    }

    class MonitoredCall {

        String AimedDepartureTime;
        String EstimatedDepartureTime;
    }

    class Stoppested {
        //Place/GetStop/{Id}
        int X, Y, ID;
        String Zone, ShortName, Name, District, PlaceType;
        boolean IsHub;
    }

    String readUrl(String urlString) throws Exception {
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
