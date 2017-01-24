import org.sintef.jarduino.*;

public class Blink extends JArduino {
    //Constructor taking a String describing the serial port where the Arduino Board is connected (eg, "COM7")
    Blink(String port) {
        super(port);
    }

    protected void setup() {
        // initialize the digital pin as an output.
        // Pin 13 has an LED connected on most Arduino boards:
        pinMode(DigitalPin.PIN_13, PinMode.OUTPUT);
    }

    protected void loop() {
        // set the LED on
        digitalWrite(DigitalPin.PIN_13, DigitalState.HIGH);
        delay(200); // wait for a second
        // set the LED off
        digitalWrite(DigitalPin.PIN_13, DigitalState.LOW);
        delay(200); // wait for a second
    }
}
