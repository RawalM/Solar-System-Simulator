import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;

public class UI {

    SolarSystem solarSystem = Main.solarSystem;
    JFrame window = new JFrame("Solar System Sandbox!");
    JFrame createPlanetFrame = new JFrame("Planet Creator!");
    JPanel solarPanel = new JPanel();
    SolarPanel solarPanelClass = new SolarPanel();
    JComboBox planetSelector = new JComboBox();

    public UI() {}

    public void initialiseUI() {
        int width = 1280;
        int height = 720;

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(width, height);
        window.setLayout(new BorderLayout());
        window.add(sidePanel(), BorderLayout.WEST);
        window.add(topPanel(), BorderLayout.NORTH);
        solarPanel = solarPanelClass;

        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    Main.simPaused = !Main.simPaused;
                }
            }
        });

        window.add(solarPanel);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        createPlanetFrame.setSize((int) (width / 1.25), (int) (height / 1.25));
        createPlanetFrame.setLayout(new BorderLayout());
        createPlanetFrame.setLocationRelativeTo(null);
        createPlanetFrame.setVisible(false);
    }

    public void refreshUI() {
        planetSelector.removeAllItems();
        ArrayList<ImageIcon> planetIcons = new ArrayList<>();

        for (Planet planet : solarSystem.getPlanets()) {
            planetSelector.addItem(planet.getName());
            ImageIcon icon = new ImageIcon(planet.getImage());
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            planetIcons.add(new ImageIcon(scaledImage));
        }
        for (Star star : solarSystem.getStars()) {
            planetSelector.addItem(star.getName());
            ImageIcon icon = new ImageIcon(star.getImage());
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            planetIcons.add(new ImageIcon(scaledImage));
        }

        planetSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel planetLabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                planetLabel.setHorizontalAlignment(SwingConstants.LEFT);
                if (index >= 0 && index < planetIcons.size()) {
                    planetLabel.setIcon(planetIcons.get(index));
                }
                return planetLabel;
            }
        });

        planetSelector.revalidate();
        planetSelector.repaint();
        window.revalidate();
        window.repaint();
    }

    public JButton buttonCreator(Color colour, String imagePath, int xPos, int yPos, int width, int height, String text) {
        JButton button = new JButton();
        if (text != null) {
            button.setText("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        }
        if (colour != null) {
            button.setBackground(colour);
        }
        if (imagePath != null) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setContentAreaFilled(false);
        }
        if (width != 0 && height != 0) {
            button.setPreferredSize(new Dimension(width, height));
            button.setMaximumSize(new Dimension(width, height));
            button.setMinimumSize(new Dimension(width, height));
        }
        return button;
    }

    public JPanel sidePanel() {
        JPanel sidePanel = new JPanel(new BorderLayout());
        sidePanel.setPreferredSize(new Dimension(200, window.getHeight()));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JPanel speedPanel = new JPanel();
        speedPanel.setLayout(new BoxLayout(speedPanel, BoxLayout.X_AXIS));
        speedPanel.add(Box.createHorizontalGlue());
        JButton normalSpeedButton = buttonCreator(null, "src/images/playBTN.png", 0, 0, 30, 30, null);
        normalSpeedButton.addActionListener(e -> {
            Main.deltaT = 10000;
            Main.simPaused = false;
        });
        JButton speedUpBTN = buttonCreator(null, "src/images/speedUpBTN.png", 0, 0, 30, 30, null);
        speedUpBTN.addActionListener(e -> {
            if (Main.deltaT + 10000 <= 1000000) {
                Main.deltaT += 10000;
            }
            Main.simPaused = false;
        });
        JButton slowDownBTN = buttonCreator(null, "src/images/slowDownBTN.png", 0, 0, 30, 30, null);
        slowDownBTN.addActionListener(e -> {
            if (Main.deltaT - 1000 >= 1) {
                Main.deltaT -= 1000;
            }
            Main.simPaused = false;
        });
        JButton pauseBTN = buttonCreator(null, "src/images/pauseBTN.png", 0, 0, 30, 30, null);
        pauseBTN.addActionListener(e -> Main.simPaused = true);

        speedPanel.add(slowDownBTN);
        speedPanel.add(Box.createVerticalStrut(5));
        speedPanel.add(pauseBTN);
        speedPanel.add(normalSpeedButton);
        speedPanel.add(Box.createVerticalStrut(5));
        speedPanel.add(normalSpeedButton);
        speedPanel.add(Box.createVerticalStrut(5));
        speedPanel.add(speedUpBTN);
        speedPanel.add(Box.createHorizontalGlue());

        // Create the speed slider
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 100, 700000, 10000);
        speedSlider.addChangeListener(e -> {
            Main.deltaT = speedSlider.getValue();
        });

        JButton createPlanetBTN = buttonCreator(null, null, 0, 0, 150, 50, "Create Planet");
        createPlanetBTN.addActionListener(e -> {
            openPlanetCreator();
            refreshUI();
        });
        JButton deletePlanetBTN = buttonCreator(null, null, 0, 0, 150, 50, "Delete focussed Planet");
        deletePlanetBTN.addActionListener(e -> {
            if (planetSelector.getSelectedIndex() <= Main.solarSystem.getPlanets().size()) {
                Main.solarSystem.removePlanet(Main.solarSystem.getPlanets().get(planetSelector.getSelectedIndex()));
                refreshUI();
            } else {
                Main.solarSystem.removeStar(Main.solarSystem.getStars().get(0));
                refreshUI();
            }
        });
        JPanel sidePlanetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sidePlanetPanel.add(createPlanetBTN);
        sidePlanetPanel.add(deletePlanetBTN);

        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(speedPanel);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(speedSlider);
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(sidePlanetPanel);
        sidePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return sidePanel;
    }

    public JComboBox createPlanetCombo() {
        ArrayList<ImageIcon> planetIcons = new ArrayList<>();
        for (Planet x : solarSystem.getPlanets()) {
            planetSelector.addItem(x.getName());
            ImageIcon icon = new ImageIcon(x.getImage());
            Image ScaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            planetIcons.add(new ImageIcon(ScaledImage));
        }
        for (Star x : solarSystem.getStars()) {
            planetSelector.addItem(x.getName());
            ImageIcon icon = new ImageIcon(x.getImage());
            Image ScaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            planetIcons.add(new ImageIcon(ScaledImage));
        }

        planetSelector.setPreferredSize(new Dimension(120, 30));
        planetSelector.setMaximumSize(new Dimension(150, 30));

        planetSelector.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel planetLabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                planetLabel.setHorizontalAlignment(SwingConstants.LEFT);
                if (index >= 0 && index < planetIcons.size()) {
                    planetLabel.setIcon(planetIcons.get(index));
                }
                return planetLabel;
            }
        });
        return planetSelector;
    }

    public JPanel topPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(window.getWidth(), 125));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        JLabel dropDownLabel = new JLabel("Select Planet:");
        JComboBox planetSelector = createPlanetCombo();

        JButton focusPlanetBTN = buttonCreator(null, null,0, 0, 110, 50, "Focus on \n selected Planet");
        focusPlanetBTN.addActionListener(e -> {

            solarPanelClass.focusOnPlanet(planetSelector.getSelectedIndex());
        });



        topPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        topPanel.add(dropDownLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(planetSelector);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(focusPlanetBTN);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));



        return topPanel;
    }

    public void openPlanetCreator() {
        createPlanetFrame.getContentPane().removeAll();
        createPlanetFrame.add(new CreatePlanetView(), BorderLayout.CENTER);
        createPlanetFrame.pack();
        createPlanetFrame.setLocationRelativeTo(null);
        createPlanetFrame.setVisible(true);
    }

    public void mousePlacement(Planet planet, boolean stableOrbit, double speed) {
        Main.simPaused = true;
        JPanel panel = solarPanel;

        MouseMotionListener moveListener = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                double simX = ((e.getX() + 510 - panel.getWidth() / 2.0) - solarPanelClass.getOffsetX()) * 5e8 / solarPanelClass.getZoomScale();
                double simY = ((e.getY() + 250 - panel.getHeight() / 2.0) - solarPanelClass.getOffsetY()) * 5e8 / solarPanelClass.getZoomScale();
                planet.setXPosition(simX);
                planet.setYPosition(simY);
                solarPanelClass.setPreviewPlanet(planet);
                panel.repaint();
            }
        };

        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    panel.removeMouseMotionListener(this);
                    panel.removeMouseListener(this);
                    panel.removeMouseMotionListener(moveListener);

                    //double simX = ((e.getX() - panel.getWidth() / 2.0)) * 5e8 / solarPanelClass.getZoomScale();
                    //double simY = ((e.getY() - panel.getHeight() / 2.0)) * 5e8 / solarPanelClass.getZoomScale();
                    //planet.setXPosition(simX);
                    //planet.setYPosition(simY);

                    if (!stableOrbit) {
                        startVelocityDirectionPhase(panel, planet);
                    } else {
                        solarPanelClass.clearPreviewPlanet();
                        Physics.createPlanetPhysics(planet, solarSystem);
                        Main.solarSystem.addPlanet(planet);
                        Main.simPaused = false;
                        panel.repaint();
                    }
                }
            }
        };

        panel.addMouseMotionListener(moveListener);
        panel.addMouseListener(clickListener);
    }

    private void startVelocityDirectionPhase(JPanel panel, Planet planet) {
        MouseMotionAdapter directionLineDrawer = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                double startX = ((planet.getXPosition() * solarPanelClass.getZoomScale()) / 5e8) + solarPanelClass.getOffsetX();
                double startY = ((planet.getYPosition() * solarPanelClass.getZoomScale()) / 5e8) + solarPanelClass.getOffsetY();
                solarPanelClass.setDirectionLine(startX, startY, e.getX(), e.getY());
                panel.repaint();
            }
        };

        MouseAdapter confirmDirection = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    panel.removeMouseMotionListener(this);
                    panel.removeMouseMotionListener(directionLineDrawer);
                    panel.removeMouseListener(this);
                    solarPanelClass.clearDirectionLine();
                    solarPanelClass.clearPreviewPlanet();

                    double mouseSimX = ((e.getX() + 510 - panel.getWidth() / 2.0) - solarPanelClass.getOffsetX()) * 5e8 / solarPanelClass.getZoomScale();
                    double mouseSimY = ((e.getY() + 250 - panel.getHeight() / 2.0) - solarPanelClass.getOffsetY()) * 5e8 / solarPanelClass.getZoomScale();

                    double dx = mouseSimX - planet.getXPosition();
                    double dy = mouseSimY - planet.getYPosition();

                    double angle = Math.atan2(dy, dx);
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    double scaledSpeed = distance * 0.1e-5; //speed

                    planet.setSpeedDirection(angle);
                    planet.setSpeed(scaledSpeed);

                    Main.solarSystem.addPlanet(planet);
                    Main.simPaused = false;
                    panel.repaint();
                }
            }
        };

        panel.addMouseMotionListener(directionLineDrawer);
        panel.addMouseListener(confirmDirection);
    }
}
