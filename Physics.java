import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Physics {

    private static final double gravConst = 6.67e-11; // Gravitational constant

    public static void totalForceCalc(ArrayList<Planet> planetArray, Star sun) {
        for (Planet planet : planetArray) {
            Vector2D totalForce = new Vector2D(0, 0);

            // Force due to Sun
            totalForce = totalForce.add(gravitationalForce(planet, sun.getMass(), sun.getXPosition(), sun.getYPosition()));
            // Forces due to other planets
            for (Planet otherPlanet : planetArray) {
                if (otherPlanet != planet) {
                    totalForce = totalForce.add(gravitationalForce(planet, otherPlanet.getMass(), otherPlanet.getXPosition(), otherPlanet.getYPosition()));
                }
            }

            // Set force values on the planet
            planet.setForce(totalForce.magnitude());
            planet.setForceDirection(totalForce.angle());
        }
    }

    private static void handleCollision(SolarSystem solarSystem) {
        ArrayList<Planet> toDelete = new ArrayList<>();
        for (Planet planet1 : solarSystem.getPlanets()) {
            for (Planet planet2 : solarSystem.getPlanets()) {
                double dx = (planet1.getXPosition() - planet2.getXPosition())/5e8;
                double dy = (planet1.getYPosition() - planet2.getYPosition())/5e8;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double dir1 = planet1.getSpeedDirection();
                double dir2 = planet2.getSpeedDirection();
                if (planet2 == planet1) {
                    break;
                }

                if (distance < (64)) {

                        double newMass = planet1.getMass() + planet2.getMass();
                        double vx = (planet1.getSpeed() * Math.cos(dir1) * planet1.getMass() +
                                planet2.getSpeed() * Math.cos(dir2) * planet2.getMass()) / newMass;
                        double vy = (planet1.getSpeed() * Math.sin(dir1) * planet1.getMass() +
                                planet2.getSpeed() * Math.sin(dir2) * planet2.getMass()) / newMass;
                        if (planet1.getMass()*planet1.getSpeed() > planet2.getMass() * planet2.getSpeed()) {
                            planet1.setMass(newMass);
                            planet1.setSpeed(Math.sqrt(vx * vx + vy * vy));
                            planet1.setSpeedDirection(Math.atan2(vy, vx));

                            planet1.setSize(planet1.getSize() * 1.2);
                            generateDebris(planet1.getXPosition(), planet1.getYPosition());
                            generateDebris(planet2.getXPosition(), planet2.getYPosition());
                            toDelete.add(planet2);
                        }else{
                            planet2.setMass(newMass);
                            planet2.setSpeed(Math.sqrt(vx * vx + vy * vy));
                            planet2.setSpeedDirection(Math.atan2(vy, vx));

                            planet2.setSize(planet1.getSize() * 1.2);
                            generateDebris(planet1.getXPosition(), planet1.getYPosition());
                            generateDebris(planet2.getXPosition(), planet2.getYPosition());
                            toDelete.add(planet1);
                        }

                    }

            }
            for (Star star : solarSystem.getStars()) {
                double dx = (planet1.getXPosition() - star.getXPosition())/5e8;
                double dy = (planet1.getYPosition() - star.getYPosition())/5e8;
                double distance = Math.sqrt(dx * dx + dy * dy);
                double dir1 = planet1.getSpeedDirection();
                double dir2 = 0;


                if (distance < (64)) {
                    double angleBetween = Math.atan2(dy, dx); // Angle from planet1 to planet2 (collision axis)

                    double relativeAngle1 = Math.abs(angleBetween - planet1.getSpeedDirection());
                    double relativeAngle2 = Math.abs(angleBetween);

                    boolean isHeadOn = (relativeAngle1 > Math.PI * 0.75 && relativeAngle2 < Math.PI * 1.25);

                    if (isHeadOn) {
                        double newMass = planet1.getMass() + star.getMass();
                        double vx = (planet1.getSpeed() * Math.cos(dir1) * planet1.getMass() +
                                star.getSpeed() * Math.cos(dir2) * star.getMass()) / newMass;
                        double vy = (planet1.getSpeed() * Math.sin(dir1) * planet1.getMass() +
                                star.getSpeed() * Math.sin(dir2) * star.getMass()) / newMass;

                        planet1.setMass(newMass);
                        planet1.setSpeed(Math.sqrt(vx * vx + vy * vy));
                        planet1.setSpeedDirection(Math.atan2(vy, vx));

                        planet1.setSize(planet1.getSize() * 1.2);
                        generateDebris(planet1.getXPosition(), planet1.getYPosition());
                        generateDebris(star.getXPosition(), star.getYPosition());
                        toDelete.add(planet1);

                    } else {

                        double lostMass = planet1.getMass() * 0.1;
                        planet1.setMass(planet1.getMass() - lostMass);
                        star.setMass(star.getMass() - lostMass);


                        planet1.setSpeedDirection(-planet1.getSpeedDirection());

                    }
                }


            }
        }
        for (Planet planet : toDelete) {
            solarSystem.removePlanet(planet);
            Main.ui.refreshUI();
        }
    }

    private static void generateDebris(double x, double y) {
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            double angle = rand.nextDouble() * 2 * Math.PI;
            double speed = rand.nextDouble() * 3e9; // adjust for visual effect
            double dx = speed * Math.cos(angle);
            double dy = speed * Math.sin(angle);
            int lifespan = 500 + rand.nextInt(300);
            int size = 10 + rand.nextInt(10);
            Color color = Color.GRAY;

            Main.debrisList.add(new Debris(x, y, dx, dy, size, size, color));
        }
    }

    private static Vector2D gravitationalForce(Planet planet, double mass, double x2, double y2) {
        double x1 = planet.getXPosition();
        double y1 = planet.getYPosition();
        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));

        if (distance == 0) return new Vector2D(0, 0); // Avoid divide by zero

        double force = (gravConst * planet.getMass() * mass) / (distance * distance);
        double angle = Math.atan2(y2 - y1, x2 - x1);

        return new Vector2D(force * Math.cos(angle), force * Math.sin(angle));
    }

    public static void newXAndY(Planet planet,Star sun,  double deltaT) {
        double acceleration = planet.getForce() / planet.getMass();
        double forceAngle = planet.getForceDirection();

        Vector2D initialVelocity = new Vector2D(planet.getSpeed() * Math.cos(planet.getSpeedDirection()),planet.getSpeed() * Math.sin(planet.getSpeedDirection()));

        Vector2D accelerationVector = new Vector2D(acceleration * Math.cos(forceAngle), acceleration * Math.sin(forceAngle));

        Vector2D finalVelocity = initialVelocity.add(accelerationVector.scale(deltaT));
        Vector2D newPosition = new Vector2D(planet.getXPosition(), planet.getYPosition()).add(finalVelocity.scale(deltaT));

        double newRadius = Math.sqrt(Math.pow(newPosition.x - sun.getXPosition(), 2) + Math.pow(newPosition.y - sun.getYPosition(), 2));
        planet.setRadius(newRadius);

        planet.setXPosition(newPosition.x);
        planet.setYPosition(newPosition.y);
        planet.setSpeed(finalVelocity.magnitude());
        planet.setSpeedDirection(finalVelocity.angle());
    }


    public static double radiusCalc(double x, double y, Star sun) {
        return (Math.sqrt(Math.pow(x - sun.getXPosition(), 2) + Math.pow(y - sun.getYPosition(), 2)));
    }

    public static void runSimulation(SolarSystem solarSystem, double deltaT) {
        Star sun = solarSystem.getStars().get(0);
        ArrayList<Planet> planets = solarSystem.getPlanets();

        totalForceCalc(planets, sun);
        handleCollision(solarSystem);

        for (Planet planet : planets) {

            //eccentricityCalc(planet, sun);
            newXAndY(planet, sun, deltaT);

        }
    }

    public static void eccentricityCalc(Planet planet, Star sun) {
        double h = specificAngularMomentum(planet);
        double E = specificOrbitalEnergy(planet, sun);
        //double e = Math.sqrt(1 + ((2 * E * h * h) / (gravConst * gravConst * sun.getMass() * sun.getMass())));
        double e = Math.sqrt(1 - ((h*h)/(gravConst*sun.getMass()*planet.getSemiMajorAxis())));
        planet.setEccentricity(e);
    }

    public static double specificAngularMomentum(Planet planet) {
        return planet.getRadius() * planet.getSpeed();
    }

    public static double specificOrbitalEnergy(Planet planet, Star sun) {
        return (planet.getSpeed() * planet.getSpeed()) / 2 - (gravConst * sun.getMass()) / planet.getRadius();
    }

    public static void createPlanetPhysics(Planet newPlanet, SolarSystem solarSystem) {
        Star sun = solarSystem.getStars().get(0);
        double x = newPlanet.getXPosition();
        double y = newPlanet.getYPosition();
        double xs = sun.getXPosition();
        double ys = sun.getYPosition();

        double diffX = x - xs;
        double diffY = y - ys;
        System.out.println("x" +x);
        System.out.println("y" +y);
        double radius = Math.sqrt(diffX * diffX + diffY * diffY);
        newPlanet.setRadius(radius);
        System.out.println("radius" +radius);

        // STEP 1: Set perpendicular velocity direction
        double angleToSun = Math.atan2(diffY, diffX);
        double perpendicularAngle = angleToSun - Math.PI / 2;  // Tangential direction
        newPlanet.setSpeedDirection(perpendicularAngle);
        System.out.println("direction" +perpendicularAngle);

        // STEP 2: Set exact circular speed
        double massSun = sun.getMass();
        double speed = Math.sqrt(gravConst * massSun / radius);
        newPlanet.setSpeed(speed);
        System.out.println("speed" +speed);

        // STEP 3: Semi-major axis = radius (for circle)
        newPlanet.setSemiMajorAxis(radius);
        System.out.println("radius" +radius);
        // STEP 4: Compute period from Kepler's Third Law
        double pi = Math.PI;
        double period = 2 * pi * Math.sqrt((radius * radius * radius) / (gravConst * massSun));
        newPlanet.setPeriod(period);
        System.out.println("period" +period);

        // STEP 5: Optional — recompute eccentricity (should be 0)
        eccentricityCalc(newPlanet, sun);
        System.out.println("eccentricity" + newPlanet.getEccentricity());
    }

    //Functions for creating

    public static void visViva(Planet planet, Star sun) {
        double mass1 = sun.getMass();
        double radius = planet.getRadius();
        double a = planet.getSemiMajorAxis();


        //double speedAtR = Math.sqrt(gravConst * mass1 * ((2 / radius) - (1 / a)));
        double speedAtR = Math.sqrt((gravConst*mass1)/radius);
        planet.setSpeed(speedAtR);
        // Used to calc speed at a given distance from star in secure orbit
    }

    public static void keplersThirdLaw(Planet planet, Star sun) {
        double mass1 = sun.getMass();
        double pi = Math.PI;

        double period = planet.getPeriod(); //check on direction? (based on angular velocity)
        double a = Math.cbrt((period * period * gravConst * mass1) / (4 * pi * pi));

        planet.setSemiMajorAxis(a);
    }

    public static double newtonsLawGrav(Planet planet, double mass1, double radius) {
        double mass2 = planet.getMass();
        if (radius == -1) {
            radius = planet.getRadius();
        }


        // check if correct equation for context
        return (gravConst * mass1 * mass2) / radius; // Force in Newtons
    }

    public static void periodCalc(Planet planet, Star sun) {
        double pi = Math.PI;
        double radius = planet.getRadius();
        double mass1 = sun.getMass();
        double mass2 = planet.getMass();
        double x2 = sun.getXPosition();
        double y2 = sun.getYPosition();

        Vector2D totalForce =gravitationalForce(planet, mass1, x2, y2);
        planet.setForce(totalForce.magnitude());
        planet.setForceDirection(totalForce.angle());
        double force_from_sun = planet.getForce();

        double period = Math.sqrt((4*pi*pi*mass2*radius)/force_from_sun);
        planet.setPeriod(period);
    }
}