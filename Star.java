import java.awt.*;

/**
 * The Star class represents a star in a 2D space.
 * and is used to initialize such an object
 */
public class Star {
    private String name;
    private double xPosition;  // X-coordinate of the star
    private double yPosition;  // Y-coordinate of the star
    private double mass;       // Mass of the star
    private double speed;      // Speed of the star
    private double direction;  // Direction of movement in degrees
    private double size;       // Size of the star
    private String image;       // Image representation of the star
    private boolean showInfoTile;

    /**
     * Constructor to initialize a star object.
     *
     * @param name      Label for the star
     * @param xPosition Initial X-coordinate
     * @param yPosition Initial Y-coordinate
     * @param mass      Mass of the star
     * @param speed     Speed of the star
     * @param direction Direction of movement in degrees
     * @param size      Size of the star
     * @param image     Image representation of the star
     */
    public Star(double xPosition, double yPosition, double mass, double speed, double direction, double size, String image, String name, boolean showInfoTile) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.mass = mass;
        this.speed = speed;
        this.direction = direction;
        this.size = size;
        this.image = image;
        this.name = name;
        this.showInfoTile = showInfoTile;
    }

    // Getter methods to retrieve star properties
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

    public boolean getShowInfoTile() {
        return showInfoTile;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    // Setter methods to modify star properties
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

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShowInfoTile(boolean showInfoTile) {
        this.showInfoTile = showInfoTile;
    }
}