
import java.util.ArrayList;



public class SolarSystem {
    // ArrayLists to hold the current orbital bodies
    public ArrayList<Planet> planetArray = new ArrayList<Planet>();

    public ArrayList<Star> starArray = new ArrayList<Star>();

    public ArrayList<Asteroid> asteroidArray = new ArrayList<Asteroid>();

    public ArrayList<BlackHole> blackHoleArray = new ArrayList<BlackHole>();

    // Constructor, nothing is in here as we can instantiate an empty solar system
    public SolarSystem(){
    }

    // radius(done), eccentricity, eriod, force, force_direction, semi_major_axis,size
    // Functions for adding pre-set planets from our solar system
    public void addSun(){
        Star theSun = new Star(0.0, 0.0, 1.989e30, 0.0, 0.0, 1.3927e6, "src/images/Sun.png", "Sun", false);
        addStar(theSun);
    }

    public void addMercury() {
        Planet mercury = new Planet(69.818E9, 0.0, 0.330e24, 38864.7570, -Math.PI/2, 69.818E9, 0.206, 7600521.6, 0.0, 0.0, 57.909E9, 4879.0, "src/images/Mercury.png", "Mercury", false);
        addPlanet(mercury);
    }
    

    public void addVenus() {
        Planet venus = new Planet(108.9E9, 0.0, 4.87e24, 34790.1818, -Math.PI/2, 108.9E9, 0.007, 19414166.4, 0.0, 0.0, 108.210E9, 12104.0, "src/images/Venus.png", "Venus", false);
        addPlanet(venus);
    }

    public void addEarth(){
        Planet earth = new Planet(152.1E9, 0.0, 5.97e24, 29295.1810, -Math.PI/2,152.1E9,0.017,31558118.4,0.0,0.0,149.5978707E9, 12756.0 , "src/images/Earth.png", "Earth", false);
        addPlanet(earth);
    }

    public void addMars() {
        Planet mars = new Planet(249.3E9, 0.0, 0.642e24, 21974.9314, -Math.PI/2, 249.3E9, 0.093, 59355072, 0.0, 0.0, 227.956E9, 6792.0, "src/images/Mars.png", "Mars", false);
        addPlanet(mars);
    }

    public void addJupiter() {
        Planet jupiter = new Planet(816.4E9, 0.0, 1898e24, 12434.5425, -Math.PI/2, 816.4E9, 0.049, 374335689.6, 0.0, 0.0, 778.479E9, 142984.0, "src/images/Jupiter.png", "Jupiter", false);
        addPlanet(jupiter);
    }

    public void addSaturn() {
        Planet saturn = new Planet(1506.5E9, 0.0, 568e24, 9100.3757, -Math.PI/2, 1506.5E9, 0.056, 929292393.6, 0.0, 0.0, 1432.041E9, 120536.0, "src/images/Saturn.png", "Saturn", false);
        addPlanet(saturn);
    }

    public void addUranus() {
        Planet uranus = new Planet(3001.4E9, 0.0, 86.8e24, 6484.6825, -Math.PI/2, 3001.4E9, 0.046, 2651218560.0, 0.0, 0.0, 2867.043E9, 51118.0, "src/images/Uranus.png", "Uranus", false);
        addPlanet(uranus);
    }

    public void addNeptune() {
        Planet neptune = new Planet(4558.9E9, 0.0, 102e24, 5383.9657, -Math.PI/2, 4558.9E9, 0.010, 5200331155.2, 0.0, 0.0, 4514.953E9, 49528.0, "src/images/Neptune.png", "Neptune", false);
        addPlanet(neptune);
    }


    // Function for adding all of our solar system at once
    public void addOurSolarSystem(){
        addSun();
        addMercury();
        addVenus();
        addEarth();
        addMars();
        addJupiter();
        addSaturn();
        addUranus();
        addNeptune();
        //addPluto();
    }

    // Functions for adding created bodies to the relevant ArrayLists
    public void addPlanet(Planet planet) {
        this.planetArray.add(planet);
    }
    public void removePlanet(Planet planet) {
        this.planetArray.remove(planet);
    }

    public void addStar(Star star) {
        this.starArray.add(star);
    }
    public void removeStar(Star star) {
        this.starArray.remove(star);
    }

    public void addAsteroid(Asteroid asteroid) {
        this.asteroidArray.add(asteroid);
    }
    public void removeAsteroid(Asteroid asteroid) {
        this.asteroidArray.remove(asteroid);
    }

    public void addBlackHole(BlackHole blackhole){
        this.blackHoleArray.add(blackhole);
    }
    public void removeBlackHole(BlackHole blackhole){
        this.blackHoleArray.remove(blackhole);
    }

    /*
    Getter functions for returning objects in Solar System
    Can change this to return individual planets quite easily, either by giving the function the name attribute
    or the index
    */
    public ArrayList<Planet> getPlanets() {
        return planetArray;
    }
    public ArrayList<Star> getStars() {
        return starArray;
    }
    public ArrayList<Asteroid> getAsteroids() {
        return asteroidArray;
    }
    public ArrayList<BlackHole> getBlackHoles() {
        return blackHoleArray;
    }
}