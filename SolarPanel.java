import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;

public class SolarPanel extends JPanel {
    private Planet selectedPlanet = null;
    private Point dragStart = null;
    private int offsetX = 0, offsetY = 0;
    private int prevMouseX, prevMouseY;
    private boolean planetFocussed = false;
    private int focussedPlanetIndex = 0;
    private double zoomScale = 1.0;
    private ArrayList<Point> starPlacements = new ArrayList<>();
    private ArrayList<RandomPlanets> randomPlanetPlacements = new ArrayList<>();
    private boolean starsGenerated = false;
    private boolean randomPlanetsGenerated = false;
    private final int starRange = 90000;
    private BufferedImage gasCloudTexture;
    private BufferedImage galaxyTexture; // (Pre‑generated version, not used now)
    private Planet previewPlanet = null;
    private boolean showDirectionLine = false;
    private double lineStartX, lineStartY, lineEndX, lineEndY;

    public void setDirectionLine(double startX, double startY, double endX, double endY) {
        this.showDirectionLine = true;
        this.lineStartX = startX;
        this.lineStartY = startY;
        this.lineEndX = endX;
        this.lineEndY = endY;
    }

    public void clearDirectionLine() {
        this.showDirectionLine = false;
    }

    public void setPreviewPlanet(Planet p) {
        this.previewPlanet = p;
        repaint();
    }
    public double getZoomScale() {
        return zoomScale;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void clearPreviewPlanet() {
        this.previewPlanet = null;
        repaint();
    }
    public SolarPanel() {
        setBackground(Color.black);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    planetFocussed = false;
                    prevMouseX = e.getX();
                    prevMouseY = e.getY();
                    for (Planet planet : Main.solarSystem.getPlanets()) {
                        int x = (int)(((planet.getXPosition() * zoomScale) / 5e8) + offsetX);
                        int y = (int)(((planet.getYPosition() * zoomScale) / 5e8) + offsetY);
                        int size = (planet.getSize() > 500) ? (int)(64 * zoomScale) : (int)(planet.getSize() * zoomScale);
                        int radius = size / 2;
                        int centerX = x + radius;
                        int centerY = y + radius;
                        double dx = e.getX() - centerX;
                        double dy = e.getY() - centerY;
                        if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                            planet.setShowInfoTile(!planet.getShowInfoTile());
                            break;
                        }
                    }
                    for (Star planet : Main.solarSystem.getStars()) {
                        int x = (int)(((planet.getXPosition() * zoomScale) / 5e8) + offsetX);
                        int y = (int)(((planet.getYPosition() * zoomScale) / 5e8) + offsetY);
                        int size = (int)(64 * zoomScale);
                        int radius = size / 2;
                        int centerX = x + radius;
                        int centerY = y + radius;
                        double dx = e.getX() - centerX;
                        double dy = e.getY() - centerY;
                        if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                            planet.setShowInfoTile(!planet.getShowInfoTile());
                            break;
                        }
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) { // right-click
                    for (Planet planet : Main.solarSystem.getPlanets()) {
                        int x = (int)(((planet.getXPosition() * zoomScale) / 5e8) + offsetX);
                        int y = (int)(((planet.getYPosition() * zoomScale) / 5e8) + offsetY);
                        int size = (planet.getSize() > 500) ? (int)(64 * zoomScale) : (int)(planet.getSize() * zoomScale);
                        int radius = size / 2;
                        int centerX = x + radius;
                        int centerY = y + radius;
                        double dx = e.getX() - centerX;
                        double dy = e.getY() - centerY;
                        if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                            selectedPlanet = planet;
                            dragStart = e.getPoint();
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && selectedPlanet != null) {
                    selectedPlanet = null;
                    dragStart = null;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && selectedPlanet != null) {
                    Point dragEnd = e.getPoint();
                    double dx = dragEnd.x - dragStart.x;
                    double dy = dragEnd.y - dragStart.y;

                    double newSpeed = Math.sqrt(dx * dx + dy * dy) * 1e2; // adjust scaling factor if needed
                    double newDirection = Math.atan2(dy, dx);

                    selectedPlanet.setSpeed(newSpeed);
                    selectedPlanet.setSpeedDirection(newDirection);

                    repaint();
                } else {
                    int dx = e.getX() - prevMouseX;
                    int dy = e.getY() - prevMouseY;
                    offsetX += dx;
                    offsetY += dy;
                    double scaledRange = starRange * zoomScale * 0.15;
                    int minOffsetX = (int)(-scaledRange - getWidth());
                    int maxOffsetX = (int)(scaledRange);
                    int minOffsetY = (int)(-scaledRange - getHeight());
                    int maxOffsetY = (int)(scaledRange);
                    offsetX = Math.max(minOffsetX, Math.min(offsetX, maxOffsetX));
                    offsetY = Math.max(minOffsetY, Math.min(offsetY, maxOffsetY));
                    prevMouseX = e.getX();
                    prevMouseY = e.getY();
                    repaint();
                }
            }
        });
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                double zoomStep = 0.1;
                if (rotation < 0) {
                    zoomScale *= (1 + zoomStep);
                } else {
                    zoomScale /= (1 + zoomStep);
                }
                repaint();
            }
        });

    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (!starsGenerated) {
            starsGenerated = true;
            int numStars = 99999;
            for (int i = 0; i < numStars; i++) {
                int starX = (int)(Math.random() * starRange - starRange / 2);
                int starY = (int)(Math.random() * starRange - starRange / 2);
                starPlacements.add(new Point(starX, starY));
            }
        }
        if (!randomPlanetsGenerated) {
            randomPlanetsGenerated = true;
            int numPlanets = 33999;
            Random rnd = new Random();
            for (int i = 0; i < numPlanets; i++) {
                int planetX = (int)(Math.random() * starRange - starRange / 2);
                int planetY = (int)(Math.random() * starRange - starRange / 2);
                int size = rnd.nextInt(6) + 1;
                int red = rnd.nextInt(75);
                int green = rnd.nextInt(44);
                int blue = rnd.nextInt(76);
                Color color = new Color(red, green, blue);
                randomPlanetPlacements.add(new RandomPlanets(new Point(planetX, planetY), size, color));
            }
        }
        int panelWidth = getWidth() > 0 ? getWidth() : 1000;
        int panelHeight = getHeight() > 0 ? getHeight() : 1000;
        galaxyTexture = generateGalaxyTexture(panelWidth, panelHeight, 10.0, offsetX, offsetY, zoomScale);

        // galaxyTexture is now generated dynamically in paintComponent
    }

    public BufferedImage generateGalaxyTexture(int width, int height, double scale, int offX, int offY, double zoom) {
        if (width <= 0 || height <= 0) {
            width = getPreferredSize().width > 0 ? getPreferredSize().width : 800;
            height = getPreferredSize().height > 0 ? getPreferredSize().height : 600;
        }
        BufferedImage galaxyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        PerlinNoise noiseGen = new PerlinNoise();
        double threshold = 0.65;
        int maxAlpha = 430;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                double nx = (((i - offX) / (double)width) / zoom) * scale;
                double ny = (((j - offY) / (double)height) / zoom) * scale;
                double noiseVal = noiseGen.noise(nx * scale * 2, ny * scale * 2);
                if (noiseVal < threshold) {
                    galaxyImage.setRGB(i, j, 0x00000000);
                } else {
                    double t = (noiseVal - threshold) / (1.0 - threshold);
                    int alpha = (int)(t * maxAlpha);
                    float hue = 0.60f + (float)t * 0.05f;
                    float saturation = 0.8f;
                    float brightness = 0.3f + (float)t * 0.15f;
                    Color c = Color.getHSBColor(hue, saturation, brightness);
                    int rgba = (alpha << 24) | (c.getRGB() & 0x00FFFFFF);
                    galaxyImage.setRGB(i, j, rgba);
                }
            }
        }
        return galaxyImage;
    }

    public void focusOnPlanet(int index) {
        this.focussedPlanetIndex = index;
        planetFocussed = true;
        zoomScale = 1.0;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        BufferedImage noiseBG = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        double perlinScale = 2.0;
        double threshold = 0.5;
        PerlinNoise noiseGen = new PerlinNoise();
        for (int sx = 0; sx < w; sx++) {
            for (int sy = 0; sy < h; sy++) {
                double worldX = (((sx - offsetX) * 0.02) / zoomScale);
                double worldY = (((sy - offsetY) * 0.02) / zoomScale);
                double nx = worldX / perlinScale;
                double ny = worldY / perlinScale;
                double noiseVal = noiseGen.noise(nx, ny);
                if (noiseVal < threshold) {
                    noiseBG.setRGB(sx, sy, 0x00000000);
                } else {
                    int alpha = (int)(noiseVal * 20);
                    double range = 1.0 - threshold;
                    double t = (noiseVal - threshold) / range;
                    float hue = 0.65f + (float)t * 0.15f;
                    float saturation = 0.8f;
                    float brightness = 0.5f + (float)t * 0.3f;
                    Color c = Color.getHSBColor(hue, saturation, brightness);
                    int rgba = (alpha << 24) | (c.getRGB() & 0x00FFFFFF);
                    noiseBG.setRGB(sx, sy, rgba);
                }

            }
            if (showDirectionLine) {
                g.setColor(Color.CYAN);
                g.drawLine((int) lineStartX, (int) lineStartY, (int) lineEndX, (int) lineEndY);

                // Calculate the angle of the line
                double angle = Math.atan2(lineEndY - lineStartY, lineEndX - lineStartX);

                // Length of the arrowhead
                int arrowSize = 10;

                // Calculate the two points that form the arrowhead
                int arrowX1 = (int) (lineEndX - arrowSize * Math.cos(angle - Math.PI / 6));
                int arrowY1 = (int) (lineEndY - arrowSize * Math.sin(angle - Math.PI / 6));
                int arrowX2 = (int) (lineEndX - arrowSize * Math.cos(angle + Math.PI / 6));
                int arrowY2 = (int) (lineEndY - arrowSize * Math.sin(angle + Math.PI / 6));

                // Draw the arrowhead
                g.drawLine((int) lineEndX, (int) lineEndY, arrowX1, arrowY1);
                g.drawLine((int) lineEndX, (int) lineEndY, arrowX2, arrowY2);
            }




        }

        if (previewPlanet != null) {
            int px = (int)((previewPlanet.getXPosition() * zoomScale / 5e8) + offsetX);
            int py = (int)((previewPlanet.getYPosition() * zoomScale / 5e8) + offsetY);
            int size = (int)(previewPlanet.getSize() * zoomScale);

            g2d.setColor(Color.CYAN);
            g2d.drawOval(px - size / 2, py - size / 2, size, size);
        }


        g2d.drawImage(noiseBG, 0, 0, w, h, null);
        // Generate and draw galaxy texture dynamically using current offset and zoom
        BufferedImage dynamicGalaxy = generateGalaxyTexture(w, h, 0.5, offsetX, offsetY, zoomScale);
        g2d.drawImage(dynamicGalaxy, 0, 0, w, h, null);
        if (planetFocussed) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            System.out.print(focussedPlanetIndex);
            if (focussedPlanetIndex == Main.solarSystem.getPlanets().size()) {
                Star body = Main.solarSystem.getStars().get(0);
                offsetX = (int) ((panelWidth / 2) - ((body.getXPosition() * zoomScale)/ 5e8) - (32 * zoomScale));
                offsetY = (int) ((panelHeight / 2) - ((body.getYPosition() * zoomScale)/5e8) - (32 * zoomScale));
            }else{

                Planet body = Main.solarSystem.getPlanets().get(focussedPlanetIndex);
                offsetX = (int) ((panelWidth / 2) - ((body.getXPosition() * zoomScale)/ 5e8) - (32 * zoomScale));
                offsetY = (int) ((panelHeight / 2) - ((body.getYPosition() * zoomScale)/5e8) - (32 * zoomScale));

            }
            }


        g2d.setColor(new Color(238,230,197));
        for (Point p : starPlacements) {
            double panParallaxFactor = 0.05;
            double parallaxFactor = 0.90;
            int drawX = (int)((p.x * Math.max(zoomScale,0.05) * parallaxFactor) + (offsetX * panParallaxFactor));
            int drawY = (int)((p.y * Math.max(zoomScale,0.05) * parallaxFactor) + (offsetY * panParallaxFactor));
            if (drawX >= -5 && drawX <= getWidth() + 5 && drawY >= -5 && drawY <= getHeight() + 5) {
                g2d.fillOval(drawX, drawY, 2, 2);
            }
        }
        for (RandomPlanets rp : randomPlanetPlacements) {
            double panParallaxFactor = 0.05;
            double parallaxFactor = 0.95;
            int drawX = (int)((rp.getPosition().x * Math.max(zoomScale,0.05) * parallaxFactor) + (offsetX * panParallaxFactor));
            int drawY = (int)((rp.getPosition().y * Math.max(zoomScale,0.05) * parallaxFactor) + (offsetY * panParallaxFactor));
            if (drawX >= -5 && drawX <= getWidth() + 5 && drawY >= -5 && drawY <= getHeight() + 5) {
                g2d.setColor(rp.getColor());
                g2d.fillOval(drawX, drawY, rp.getSize(), rp.getSize());
            }
        }
        for (Planet planet : Main.solarSystem.getPlanets()) {
            Image img = new ImageIcon(planet.getImage()).getImage();
            int x = (int)(((planet.getXPosition() * zoomScale) / 5e8) + offsetX);
            int y = (int)(((planet.getYPosition() * zoomScale) / 5e8) + offsetY);


            int size;
            if (planet.getSize() > 500) { // if it's a real large planet like Earth (size > 500 km)
                size = (int)(64 * zoomScale); // draw it as normal 64px
            } else {
                size = (int)(planet.getSize() * zoomScale); // else use custom slider size
            }





            g2d.drawImage(img, x, y, size, size, this);
            Color semiOpaque = new Color(0.2f, 0.2f, 0.2f, .8f);
            if (planet.getShowInfoTile()) {
                int panelWidth = 160;
                int panelHeight = 105;
                int panelX = x + size + 10;
                int panelY = y;
                g2d.setColor(semiOpaque);
                g2d.fillRect(panelX, panelY, panelWidth, panelHeight);
                DecimalFormat df = new DecimalFormat("#.##");
                g2d.setColor(Color.WHITE);
                g2d.drawString("Name: " +planet.getName(), panelX + 2, panelY+15);
                g2d.drawString("Mass: " + planet.getMass() + " kg", panelX + 2, panelY + 30);
                g2d.drawString("Speed: " + df.format(planet.getSpeed()) + " km/s", panelX + 2, panelY + 45);
                g2d.drawString("Direction: " + df.format(planet.getSpeedDirection()) + " radians", panelX + 2, panelY + 60);
                g2d.drawString("Radius: " + df.format(planet.getSize()) + "m", panelX + 2, panelY + 75);
                DecimalFormat sciFormat = new DecimalFormat("0.00E0");
                g2d.drawString("distance from sun: " + sciFormat.format(planet.getRadius()) + "m", panelX + 2, panelY + 90);

            }
        }
        for (Star star : Main.solarSystem.getStars()) {
            Image img = new ImageIcon(star.getImage()).getImage();
            int x = (int)(star.getXPosition() * zoomScale) + offsetX;
            int y = (int)(star.getYPosition() * zoomScale) + offsetY;
            int size = (int)(64 * zoomScale);
            g2d.drawImage(img, x, y, size, size, this);
            g2d.drawImage(img, x, y, size, size, this);
            Color semiOpaque = new Color(0.2f, 0.2f, 0.2f, .8f);
            if (star.getShowInfoTile()) {
                int panelWidth = 130;
                int panelHeight = 90;
                int panelX = x + size + 10;
                int panelY = y - size / 2 + 20;
                g2d.setColor(semiOpaque);
                g2d.fillRect(panelX, panelY, panelWidth, panelHeight);
                DecimalFormat df = new DecimalFormat("#.##");
                g2d.setColor(Color.WHITE);
                g2d.drawString("Name: " +star.getName(), panelX + 2, panelY+15);
                g2d.drawString("Mass: " + star.getMass() + " kg", panelX + 2, panelY + 30);
                g2d.drawString("Speed: " + df.format(star.getSpeed()) + " km/s", panelX + 2, panelY + 45);
                g2d.drawString("Direction: " + df.format(star.getDirection()) + " radians", panelX + 2, panelY + 60);
                g2d.drawString("Radius: " + df.format(star.getSize()) + "m", panelX + 2, panelY + 75);
            }
        }
        ArrayList<Debris> toRemove = new ArrayList<>();
        for (Debris p : Main.debrisList) {
            p.update();
            if (!p.isAlive()) {
                toRemove.add(p);
                continue;
            }
            g2d.setColor(new Color(0.2f, 0.2f, 0.2f, (float)p.lifespan / 100));
            int drawX = (int)((p.x * zoomScale / 5e8) + offsetX);
            int drawY = (int)((p.y * zoomScale / 5e8) + offsetY);
            int pSize = (p.lifespan * p.size);
            g2d.fillOval(drawX, drawY, pSize, pSize);
            pSize = p.size;
            g2d.setColor(new Color(0.2f, 0.2f, 0.2f, (float)Math.random()));
            g2d.fillOval(drawX, drawY, pSize, pSize);
        }
        Main.debrisList.removeAll(toRemove);
    }
}
