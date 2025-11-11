import java.awt.*;

/**
 * The Planet class represents a planet in a 2D space.
 * and is used to initialize such an object
 */
public class Planet {
    private String name;
    private double xPosition;         // X-coordinate of the planet
    private double yPosition;         // Y-coordinate of the planet
    private double mass;              // Mass of the planet
    private double speed;             // Speed of the planet
    private double speedDirection;    // Direction of movement in degrees
    private double radius;            // Distance of planet from the sun
    private double eccentricity;      // Eccentricity value of the orbit of the planet
    private double period;            // orbit time in seconds
    private double force;             // Gravitational Force
    private double forceDirection;    // Direction of Gravitational force
    private double semiMajorAxis;     // Semi Major axis of the orbit of the planet
    private double size;              // Size of the planet
    private String image;             // Image representation of the planet
    private boolean showInfoTile;


    /**
     * Constructor to initialize a Planet object.
     *
     * @param name           Label for the planet
     * @param xPosition      Initial X-coordinate
     * @param yPosition      Initial Y-coordinate
     * @param mass           Mass of the planet
     * @param speed          Speed of the planet
     * @param speedDirection Direction of movement in degrees
     * @param radius         Distance between Sun and the planet
     * @param eccentricity   Eccentricity of the orbit
     * @param period         Revolution time of the planet
     * @param force          Gravitational force on the planet
     * @param forceDirection Direction of the force on the planet
     * @param semiMajorAxis  Semi Major Axis of the orbit
     * @param size           Size of the planet
     * @param image          Image representation of the planet
     */
    public Planet(double xPosition, double yPosition, double mass, double speed, double speedDirection, double radius, double eccentricity, double period, double force, double forceDirection, double semiMajorAxis, double size, String image, String name, boolean showInfoTile) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.mass = mass;
        this.speed = speed;
        this.speedDirection = speedDirection;
        this.radius = radius;
        this.eccentricity = eccentricity;
        this.period = period;
        this.force = force;
        this.forceDirection = forceDirection;
        this.semiMajorAxis = semiMajorAxis;
        this.size = size;
        this.image = image;
        this.name = name;
        this.showInfoTile = showInfoTile;
    }

    // Getter methods to retrieve planet properties
    public double getXPosition() {
        return xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public double getMass() {
        return mass;
    }

    public double getSpeed() {
        return speed;
    }

    public double getSpeedDirection() {
        return speedDirection;
    }

    public double getRadius() {
        return radius;
    }

    public double getEccentricity() {
        return eccentricity;
    }

    public double getPeriod() {
        return period;
    }

    public double getForce() {
        return force;
    }

    public double getForceDirection() {
        return forceDirection;
    }

    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    public double getSize() {
        return size;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public boolean getShowInfoTile() {return showInfoTile;
    }

    // Setter methods to modify planet properties
    public void setImage(String image) {
        this.image = image;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setSpeedDirection(double speedDirection) {
        this.speedDirection = speedDirection;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setEccentricity(double eccentricity) {
        this.eccentricity = eccentricity;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public void setForce(double force) {
        this.force = force;
    }

    public void setForceDirection(double forceDirection) {
        this.forceDirection = forceDirection;
    }

    public void setSemiMajorAxis(double semiMajorAxis) {
        this.semiMajorAxis = semiMajorAxis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShowInfoTile(boolean showInfoTile) { this.showInfoTile = showInfoTile;}

}
