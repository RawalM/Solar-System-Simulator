import java.awt.*;

/**
 * The Black Hole class represents a black hole in a 2D space.
 * and is used to initialize such an object
 */
public class BlackHole {
    private String name;
    private double xPosition;  // X-coordinate of the black hole
    private double yPosition;  // Y-coordinate of the black hole
    private double mass;       // Mass of the black hole
    private double speed;      // Speed of the black hole
    private double direction;  // Direction of movement in degrees
    private double size;       // Size of the black hole
    private String image;       // String representation of the black hole

    /**
     * Constructor to initialize an BlackHole object.
     *
     * @param xPosition  Initial X-coordinate
     * @param yPosition  Initial Y-coordinate
     * @param mass       Mass of the black hole
     * @param speed      Speed of the black hole
     * @param direction  Direction of movement in degrees
     * @param size       Size of the black hole
     * @param image      String representation of the black hole
     * @param name       Label for the black hole
     */
    public BlackHole(double xPosition, double yPosition, double mass, double speed, double direction, double size, String image, String name) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.mass = mass;
        this.speed = speed;
        this.direction = direction;
        this.size = size;
        this.image = image;
        this.name = name;
    }

    // Getter methods to retrieve black hole properties
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

    public double getDirection() {
        return direction;
    }

    public double getSize() {
        return size;
    }

    public String getString() {
        return image;
    }

    public String getName() {return name; }

    // Setter methods to modify black hole properties
    public void setString(String image) {
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

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setName(String name) {this.name = name;}
}
