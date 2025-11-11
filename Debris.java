import java.awt.*;

public class Debris {
    double x, y;
    double dx, dy;
    int size;
    int lifespan; // frames until it fades out
    Color colour;

    public Debris(double x, double y, double dx, double dy, int size,int lifespan, Color color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.lifespan = lifespan;
        this.colour = color;
        this.size = size;
    }

    public void update() {
        x += dx;
        y += dy;
        lifespan--;

    }

    public boolean isAlive() {
        return lifespan > 0;
    }
}
