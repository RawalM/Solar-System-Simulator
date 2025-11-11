

import java.util.*;

public class Main {
    public static SolarSystem solarSystem;

    public static  UI ui;
    public static int interval = 30; // Default simulation interval (ms)
    public static boolean simPaused;
    public static Timer timer;
    private static Physics physics = new Physics(); // Ensure physics object is available
    public static int deltaT = 10000;
    public static ArrayList<Debris> debrisList = new ArrayList<>();

    public Main(){
        solarSystem = new SolarSystem();
        simPaused = false;
        solarSystem.addOurSolarSystem();
        ui = new UI();
        ui.initialiseUI();
        startTimer();


    }

    public static void startTimer(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!simPaused) {

                    physics.runSimulation(solarSystem, deltaT);
                    ui.solarPanel.repaint();
                }else {
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, interval);

    }


    public static void main(String[] args) {
        Main main = new Main();

    }
}

