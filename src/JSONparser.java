import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class JSONparser {

    private static HashMap<String, String> holdeplasser = new HashMap<>();

    public static void main(String[] args) throws Exception {
        System.out.print("Skriv inn navn på holdeplassen du vil vite mer om: ");
        initHashMap();
        Scanner sc = new Scanner(System.in);
        String input = holdeplasser.get(sc.nextLine());
        String getDepartures = readUrl("http://reisapi.ruter.no/StopVisit/GetDepartures/" + input);
        Gson gson = new Gson();
        //Avgang bjerke = gson.fromJson(getDepartures, Avgang.class);
        MonitoredStopVisit[] monitoredStopVisits = gson.fromJson(getDepartures, MonitoredStopVisit[].class);

        for (MonitoredStopVisit m : monitoredStopVisits) {
            System.out.println(
                           "Ankomst --------------   " + m.MonitoredVehicleJourney.MonitoredCall.AimedDepartureTime.substring(11,16) +
                    "\n" + "Finnes sanntiddata ---   " + m.MonitoredVehicleJourney.Monitored +
                    "\n" + "Hvem kjører bussen ---   " + m.MonitoredVehicleJourney.OperatorRef +
                    "\n" + "Navn på bussen -------   " + m.MonitoredVehicleJourney.PublishedLineName + " - " + m.MonitoredVehicleJourney.DestinationName +
                    "\n" + "_________________________________________");

        }
    }

    static void initHashMap() throws FileNotFoundException {
        File f = new File("./data/stops.txt");
        Scanner sc = new Scanner(f);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            int counter = 0;
            for (int i = 8; i < s.length(); i++) {
                if (s.substring(i, i + 1).equals(",")) {
                    break;
                }
                counter++;
            }
            holdeplasser.put(s.substring(8, counter + 8), s.substring(0, 7));
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

    private static String readUrl(String urlString) throws Exception {
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
}