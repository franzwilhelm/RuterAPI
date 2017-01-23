import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class JSONparser {

    public static void main(String[] args) throws Exception {

        String json = readUrl("http://reisapi.ruter.no/Place/GetStop/3012060");
        System.out.println(json);
        Gson gson = new Gson();
        Page page = gson.fromJson(json, Page.class);

        System.out.println(page.title);

        for (Item item : page.items)
            System.out.println("    " + item.title);
    }

    static class Item {
        String title;
        String link;
        String description;

    }

    static class Page {
        String title;
        String link;
        String description;
        String language;
        List<Item> items;
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