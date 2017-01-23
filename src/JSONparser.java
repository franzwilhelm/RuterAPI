import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class JSONparser {

    public static void main(String[] args) throws Exception {
        String getStop = readUrl("http://reisapi.ruter.no/Place/GetStop/3012060");
        Gson gson = new Gson();
        System.out.println(getStop);
        Stoppested bjerke = gson.fromJson(getStop, Stoppested.class);
        Stoppested løren = gson.fromJson("{\"X\":503928}", Stoppested.class);
        if (!bjerke.IsHub) {
            System.out.println(bjerke.Y);
        }

        System.out.println(løren.X);
        //String GetLinesByStop = readUrl("http://reisapi.ruter.no/GET Travel/GetTravels?fromPlace={fromPlace}&amp;toPlace={toPlace}&amp;isafter={isafter}&amp;time={time}&amp;changemargin={changemargin}&amp;changepunish={changepunish}&amp;walkingfactor={walkingfactor}&amp;proposals={proposals}&amp;transporttypes={transporttypes}&amp;maxwalkingminutes={maxwalkingminutes}&amp;linenames={linenames}&amp;walkreluctance={walkreluctance}&amp;waitAtBeginningFactor={waitAtBeginningFactor}");
        //System.out.println(GetLinesByStop);
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