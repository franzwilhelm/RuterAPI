import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class JSONparser {

    public static void main(String[] args) throws Exception {
        String json = readUrl("http://reisapi.ruter.no/Place/GetStop/3012060");
        System.out.println(json);
        Gson gson = new Gson();
        Stoppested bjerke = gson.fromJson(json, Stoppested.class);
        if (!bjerke.IsHub) {
            System.out.println(bjerke.Y);
        }
    }

    static class Stoppested {
        int X, Y, Zone, ID;
        String ShortName, Name, District, PlaceType;
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