import org.sintef.jarduino.*;

class Blink extends JArduino {
    private boolean running = true;

    Blink(String port) {
        super(port);
    }

    protected void setup() {
        pinMode(DigitalPin.PIN_2, PinMode.INPUT);
        delay(2000);
    }

    protected void loop() {
        short switchState = analogRead(AnalogPin.A_1);
        System.out.println(switchState);
        if (switchState < 700) {
            running = false;
            this.stopArduinoProcess();
        }
    }

    public boolean isRunning() {
        return running;
    }
}
