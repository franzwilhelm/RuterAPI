import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Regex {
    static int a(String comp, int group) {
        String reg = "T(\\d\\d):(\\d\\d):(\\d\\d)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(comp);
        m.find();
        return Integer.parseInt(m.group(group));
    }

    static String b(String comp) {
        String reg = "T(\\d\\d:\\d\\d):\\d\\d";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(comp);
        m.find();
        return m.group(1);
    }

}
