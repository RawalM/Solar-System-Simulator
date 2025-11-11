import java.awt.*;

public class RandomPlanets {
    private Point position;
    private int size;
    private Color colour;

    public RandomPlanets(Point position, int size, Color color) {
        this.position = position;
        this.size = size;
        this.colour = color;
    }

    // Getter for position
    public Point getPosition() {
        return position;
    }

    // Getter for size
    public int getSize() {
        return size;
    }

    // Getter for color
    public Color getColor() {
        return colour;
    }
}
