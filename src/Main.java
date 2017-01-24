
import org.sintef.jarduino.JArduino;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static RuterAPI ruterAPI;

    public static void main(String[] args) throws Exception {
        ruterAPI = new RuterAPI();
        ruterAPI.printDepartures("ullevål stadion,");
    }

    static private void loop() {
        while (true) {
            Scanner sc = new Scanner(System.in);
            String a = sc.nextLine();

            if (a.equalsIgnoreCase("exit")) return;
            else ruterAPI.printDepartures(a);
        }
    }

    void runArduino(String com) {
        JArduino arduino = new Blink(com);
        arduino.runArduinoProcess();
    }
}