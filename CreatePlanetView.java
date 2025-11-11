import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CreatePlanetView extends JPanel {
    private Color selectedColor;
    private boolean createStableOrbit = false;
    private static int lastUsedSpeed = 100;

    public static int getLastUsedSpeed() {
        return lastUsedSpeed;
    }
    public CreatePlanetView() {
        setLayout(new BorderLayout()); // layout manager to split preview and controls
        setPreferredSize(new Dimension(500, 300)); // size of the panel

        PreviewPanel preview = new PreviewPanel(); // create area for previewing planet

        JCheckBox stableOrbitCheckBox = new JCheckBox("Create stable Orbit");
        stableOrbitCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        stableOrbitCheckBox.addItemListener(e -> {
            createStableOrbit = (e.getStateChange() == ItemEvent.SELECTED);
        });

        JPanel orbitBoxWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        orbitBoxWrapper.setOpaque(false);
        orbitBoxWrapper.add(stableOrbitCheckBox);

        JPanel previewContainer = new JPanel(new BorderLayout());
        previewContainer.add(preview, BorderLayout.CENTER);
        previewContainer.add(orbitBoxWrapper, BorderLayout.SOUTH);
        add(previewContainer, BorderLayout.CENTER);

        JPanel sidePanel = new JPanel(); // panel for sliders, dropdowns, etc.
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setPreferredSize(new Dimension(200, 300));
        sidePanel.setBackground(Color.LIGHT_GRAY);


        JButton colorButton = new JButton("Select Colour");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Planet Color", selectedColor);
            if (newColor != null) {
                selectedColor = newColor;
                colorButton.setBackground(selectedColor);
                preview.setPlanetColour(selectedColor);
            }
        });

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(15); // user enters planet name
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));

        JLabel planetSelectLabel = new JLabel("Base Planet:");
        String[] planetOptions = {"Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};
        JComboBox<String> planetCombo = new JComboBox<>(planetOptions); // dropdown to pick planet texture
        planetCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, planetCombo.getPreferredSize().height));
        planetCombo.addActionListener(e -> preview.setBasePlanet((String) planetCombo.getSelectedItem()));
        sidePanel.add(planetSelectLabel);
        sidePanel.add(planetCombo);

        JLabel massLabel = new JLabel("Mass:");
        JSlider massSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 100);
        massSlider.setMajorTickSpacing(100);
        massSlider.setMinorTickSpacing(10);
        massSlider.setPaintTicks(true);
        massSlider.setPaintLabels(false);
        massSlider.addChangeListener(e -> {
            int massValue = massSlider.getValue();
        });

        JLabel speedLabel = new JLabel("Speed:");
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 1000, 100);
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setMinorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(false);
        speedSlider.addChangeListener(e -> {
            int speedValue = speedSlider.getValue();
            lastUsedSpeed = speedValue;
        });

        JLabel sizeLabel = new JLabel("Size:");
        JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, 10);
        sizeSlider.setMajorTickSpacing(10);
        sizeSlider.setMinorTickSpacing(1);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setPaintLabels(false);
        sizeSlider.addChangeListener(e -> {
            int value = sizeSlider.getValue();
            preview.setPlanetSize(value);
        });

        JButton addPlanetButton = new JButton("Add Planet");
        addPlanetButton.addActionListener(e -> {
            double baseOrbitRadius = 2.0e11 + (Main.solarSystem.getPlanets().size() * 1e9);


// Random angle around the Sun
            double angle = Math.random() * 2 * Math.PI;

// Set starting position based on that angle
            double xPosition = baseOrbitRadius * Math.cos(angle);
            double yPosition = baseOrbitRadius * Math.sin(angle);

// Speed direction should be 90 degrees (π/2) rotated from position
            double speedDirection = angle + (Math.PI / 2);

            double mass = massSlider.getValue();
            double speed = speedSlider.getValue();
            double radius = baseOrbitRadius;
            double eccentricity = 0.0;
            double period = 0.0;
            double force = 0.0;
            double forceDirection = 0.0;
            double semiMajorAxis = radius;
            double size = sizeSlider.getValue();
            String planetName = nameField.getText();
            if (planetName == null || planetName.isEmpty()) {
                planetName = "New Planet " + (Main.solarSystem.getPlanets().size() + 1);
            }
            String planetImage = (preview.basePlanet.isEmpty()) ? "src/images/testImageShrunk.png" : "src/images/" + preview.basePlanet + ".png";

            Planet newPlanet = new Planet(
                    xPosition,
                    yPosition,
                    mass,
                    speed,
                    speedDirection,
                    radius,
                    eccentricity,
                    period,
                    force,
                    forceDirection,
                    semiMajorAxis,
                    size,
                    planetImage,
                    planetName,
                    false
            );

            Main.simPaused = true;
            Main.ui.mousePlacement(newPlanet, createStableOrbit, speed);



        });

        sidePanel.add(addPlanetButton);

        sidePanel.add(Box.createRigidArea(new Dimension(20, 20)));
        sidePanel.add(nameLabel);
        sidePanel.add(nameField);
        sidePanel.add(colorButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(massLabel);
        sidePanel.add(massSlider);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(speedLabel);
        sidePanel.add(speedSlider);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(sizeLabel);
        sidePanel.add(sizeSlider);
        sidePanel.add(Box.createVerticalGlue());

        add(sidePanel, BorderLayout.EAST);
    }

    // stable orbit checkbox return value <----
    public boolean isCreateStableOrbitSelected() {
        return createStableOrbit;
    }


    private class PreviewPanel extends JPanel {
        private double planetSize = 10;
        private Color planetColour = Color.WHITE;
        private java.util.List<Point> starPlacements = new java.util.ArrayList<>();
        private boolean starsGenerated = false;
        private String basePlanet = "";
        private Image baseImage;
        private BufferedImage gasTexture;

        private Image adjustPlanetHue(Image base, Color hueColor) {
            BufferedImage img = new BufferedImage(base.getWidth(null), base.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.drawImage(base, 0, 0, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3f)); // blend color softly over planet
            g2.setColor(hueColor);
            g2.fillRect(0, 0, img.getWidth(), img.getHeight());
            g2.dispose();
            return img;
        }

        public PreviewPanel() {
            setPreferredSize(new Dimension(300, 300));
            setBackground(Color.BLACK);
        }

        @Override
        public void addNotify() {
            super.addNotify();
            if (!starsGenerated) {
                starsGenerated = true;
                int w = getWidth()> 0 ? getWidth() : getPreferredSize().width;
                int h = getHeight() > 0 ? getHeight() : getPreferredSize().height;
                int numStars = 650;
                for (int i = 0; i < numStars; i++) {
                    int starX = (int)(Math.random() * w * 6.5);
                    int starY = (int)(Math.random() * h * 6.5);
                    starPlacements.add(new Point(starX, starY)); // randomly scatter stars
                }
            }
        }

        public void setPlanetSize(double newSize) {
            this.planetSize = newSize;
            repaint();
        }

        public void setPlanetColour(Color color) {
            this.planetColour = color;
            repaint();
        }

        public void setBasePlanet(String name) {
            this.basePlanet = name;
            if (name.equals("Sun") || name.equals("Mercury") || name.equals("Venus") || name.equals("Earth") || name.equals("Mars") || name.equals("Jupiter") || name.equals("Saturn") || name.equals("Uranus") || name.equals("Neptune")) {
                try {
                    baseImage = new ImageIcon(getClass().getResource("/images/" + name + ".png")).getImage(); // load planet image
                } catch (Exception ex) {
                    baseImage = null;
                }
            } else {
                baseImage = null;
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (gasTexture == null) {
                int w = getWidth(), h = getHeight();
                gasTexture = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                PerlinNoise noiseGen = new PerlinNoise();
                double scale = 1.0, threshold = 0.4;
                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        double nx = x / (double)w / scale;
                        double ny = y / (double)h / scale;
                        double v = noiseGen.noise(nx, ny);
                        if (v < threshold) {
                            gasTexture.setRGB(x, y, 0x00000000); // transparent background areas
                        } else {
                            double t = (v - threshold) / (1 - threshold);
                            int a = (int)(t * 80);
                            float hue = 0.60f + (float)t * 0.05f;
                            float saturation = 0.8f;
                            float brightness = 0.3f + (float)t * 0.15f;
                            Color c = Color.getHSBColor(hue, saturation, brightness);
                            gasTexture.setRGB(x, y, (a<<24)|(c.getRGB()&0x00FFFFFF)); // colored gas cloud
                        }
                    }
                }
            }
            g2d.drawImage(gasTexture, 0, 0, null);

            g2d.setColor(new Color(238, 230, 197));
            for (Point p : starPlacements) {
                g2d.fillOval(p.x, p.y, 2, 2); // draw stars
            }

            int cx = getWidth()/2, cy = getHeight()/2;
            int d = (int)(planetSize * 2);
            if (baseImage != null) {
                g2d.drawImage(baseImage, cx-d/2, cy-d/2, d, d, this);
                Image tinted = adjustPlanetHue(baseImage, planetColour);
                g2d.drawImage(tinted, cx-d/2, cy-d/2, d, d, this);
            } else {
                g2d.setColor(planetColour);
                g2d.fillOval(cx-d/2, cy-d/2, d, d); // draw plain circle if no base planet
            }
        }
    }
}


