class Main {
    private static final String GMAIL_USERNAME = "franz.vonderlippe@gmail.com";
    private static final String MY_NAME = "Franz von der Lippe";
    private static final String stop = "ullevål stadion,";

    public static void main(String[] args) throws Exception {
        /*Blink arduino = new Blink("COM4");
        arduino.runArduinoProcess();
        while (arduino.isRunning()) {
            Thread.sleep(300);
        }*/

        RuterAPI ruterAPI = new RuterAPI();
        CalendarAPI calendarAPI = new CalendarAPI();
        calendarAPI.init();
        System.out.println(calendarAPI.eventName.get(0) + calendarAPI.eventStart.get(0));

        RuterAPI.MonitoredStopVisit[] rightDepartures = ruterAPI.getRightDirectionDepartures(stop, "1");
        RuterAPI.Departures.initDepartures(rightDepartures, 3);

        Ifttt ifttt = new Ifttt(GMAIL_USERNAME);
        ifttt.toSlack();
        ifttt.toiPhone();
    }
}