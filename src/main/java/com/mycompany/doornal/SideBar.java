
package com.mycompany.doornal;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SideBar extends JPanel {

    private JButton accountButton;
    private JButton journalButton;
    private JButton lettersButton;
    private JButton insightsButton;

    private GlassNavButton activeButton;

    private static final Color PERIWINKLE = new Color(192, 180, 239);
    private static final Color PURPLE = new Color(142, 138, 217);
    private static final Color DEEP_PLUM = new Color(41, 34, 63);

    private float glowAlpha = 0.35f;
    private boolean glowIncreasing = true;
    private Timer glowTimer;

    public SideBar() {

        setPreferredSize(new Dimension(215, 0));
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(new EmptyBorder(28, 15, 20, 15));

        // LOGO
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoPanel.setMaximumSize(new Dimension(175, 155));

        JLabel logoImage = new JLabel();
        logoImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon logo = loadImage("logo.png", 110, 110);

        if (logo != null) {
            logoImage.setIcon(logo);
        } else {
            logoImage.setText("D");
            logoImage.setFont(new Font("Serif", Font.BOLD, 65));
            logoImage.setForeground(PURPLE);
        }

        JLabel logoText = new JLabel("DOORNAL");
        logoText.setFont(new Font("Serif", Font.BOLD, 23));
        logoText.setForeground(DEEP_PLUM);
        logoText.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoPanel.add(logoImage);
        logoPanel.add(Box.createVerticalStrut(3));
        logoPanel.add(logoText);

        topPanel.add(logoPanel);
        topPanel.add(Box.createVerticalStrut(38));

        // NAVIGATION BUTTONS
        accountButton = createButton("Account", "button1.png");
        journalButton = createButton("Journal", "button2.png");
        lettersButton = createButton("Letters", "button3.png");
        insightsButton = createButton("Mood Insights", null);

        addNavigationButton(topPanel, accountButton);
        addNavigationButton(topPanel, journalButton);
        addNavigationButton(topPanel, lettersButton);
        addNavigationButton(topPanel, insightsButton);

        add(topPanel, BorderLayout.NORTH);

        // GLOW ANIMATION
        glowTimer = new Timer(35, e -> {

            if (glowIncreasing) {
                glowAlpha += 0.006f;

                if (glowAlpha >= 0.65f) {
                    glowAlpha = 0.65f;
                    glowIncreasing = false;
                }

            } else {
                glowAlpha -= 0.006f;

                if (glowAlpha <= 0.25f) {
                    glowAlpha = 0.25f;
                    glowIncreasing = true;
                }
            }

            repaintButtons();
        });
    }

    private void addNavigationButton(JPanel panel, JButton button) {

        panel.add(button);
        panel.add(Box.createVerticalStrut(15));
    }

    // IMAGE LOADER
    private ImageIcon loadImage(String fileName, int width, int height) {

        String path = "/images/img/" + fileName;
        URL imageURL = getClass().getResource(path);

        if (imageURL == null) {
            System.out.println("IMAGE NOT FOUND: " + path);
            return null;
        }

        ImageIcon original = new ImageIcon(imageURL);

        Image scaled = original.getImage().getScaledInstance(
                width,
                height,
                Image.SCALE_SMOOTH
        );

        return new ImageIcon(scaled);
    }

    private ImageIcon loadNavIcon(String fileName) {
        return loadImage(fileName, 27, 27);
    }

    // CREATE BUTTON
    private JButton createButton(String text, String imageName) {

        GlassNavButton button = new GlassNavButton(text, imageName);

        button.addActionListener(e -> setActiveButton(button));

        return button;
    }

    // ACTIVE BUTTON
    private void setActiveButton(GlassNavButton button) {

        activeButton = button;

        if (glowTimer != null && !glowTimer.isRunning()) {
            glowTimer.start();
        }

        repaintButtons();
    }

    private void repaintButtons() {

        if (accountButton != null) accountButton.repaint();
        if (journalButton != null) journalButton.repaint();
        if (lettersButton != null) lettersButton.repaint();
        if (insightsButton != null) insightsButton.repaint();
    }

    // SET ACTIVE PAGE FROM MAIN FRAME
    public void setActivePage(String page) {

        switch (page) {

            case "account":
                setActiveButton((GlassNavButton) accountButton);
                break;

            case "journal":
                setActiveButton((GlassNavButton) journalButton);
                break;

            case "letters":
                setActiveButton((GlassNavButton) lettersButton);
                break;

            case "insights":
                setActiveButton((GlassNavButton) insightsButton);
                break;
        }
    }

    // GETTERS
    public JButton getAccountButton() {
        return accountButton;
    }

    public JButton getJournalButton() {
        return journalButton;
    }

    public JButton getLettersButton() {
        return lettersButton;
    }

    public JButton getInsightsButton() {
        return insightsButton;
    }

    // SIDEBAR GLASS BACKGROUND
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        GradientPaint base = new GradientPaint(
                0, 0,
                new Color(255, 252, 252, 245),
                w, h,
                new Color(210, 197, 240, 225)
        );

        g2.setPaint(base);
        g2.fillRect(0, 0, w, h);

        g2.setPaint(new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 175),
                0, 270,
                new Color(255, 255, 255, 0)
        ));

        g2.fillRect(0, 0, w, 270);

        g2.setColor(new Color(142, 138, 217, 35));
        g2.fillOval(-100, 150, 260, 260);

        g2.setColor(new Color(252, 211, 191, 65));
        g2.fillOval(-100, -100, 250, 250);

        g2.setColor(new Color(192, 180, 239, 65));
        g2.fillOval(-80, h - 260, 260, 260);

        g2.setPaint(new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 90),
                w, 0,
                new Color(255, 255, 255, 0)
        ));

        g2.fillRect(0, 0, w, h);

        g2.setStroke(new BasicStroke(3f));
        g2.setColor(new Color(255, 255, 255, 150));
        g2.drawLine(w - 2, 0, w - 2, h);

        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(142, 138, 217, 110));
        g2.drawLine(w - 5, 0, w - 5, h);

        g2.dispose();
    }

    // CUSTOM CHART ICON
    private class BarChartIcon implements Icon {

        @Override
        public int getIconWidth() {
            return 27;
        }

        @Override
        public int getIconHeight() {
            return 27;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(PURPLE);
            g2.setStroke(new BasicStroke(2.2f));

            g2.drawLine(x + 3, y + 23, x + 25, y + 23);

            g2.fillRoundRect(x + 5, y + 14, 4, 9, 3, 3);
            g2.fillRoundRect(x + 12, y + 8, 4, 15, 3, 3);
            g2.fillRoundRect(x + 19, y + 3, 4, 20, 3, 3);

            g2.dispose();
        }
    }

    // GLASS NAVIGATION BUTTON
    private class GlassNavButton extends JButton {

        private boolean hovered = false;

        public GlassNavButton(String text, String imageName) {

            super(text);

            setFont(new Font("SansSerif", Font.BOLD, 14));
            setForeground(DEEP_PLUM);

            setHorizontalAlignment(SwingConstants.LEFT);
            setHorizontalTextPosition(SwingConstants.RIGHT);
            setVerticalAlignment(SwingConstants.CENTER);
            setIconTextGap(14);

            setPreferredSize(new Dimension(175, 59));
            setMaximumSize(new Dimension(175, 59));
            setMinimumSize(new Dimension(175, 59));

            setAlignmentX(Component.CENTER_ALIGNMENT);

            setBorder(new EmptyBorder(8, 15, 8, 10));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (imageName != null) {
                ImageIcon icon = loadNavIcon(imageName);

                if (icon != null) {
                    setIcon(icon);
                }
            } else {
                setIcon(new BarChartIcon());
            }

            addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            boolean selected = this == activeButton;

            RoundRectangle2D shape = new RoundRectangle2D.Float(
                    3, 3, w - 7, h - 7, 22, 22
            );

            if (selected) {

                int alpha = (int) (glowAlpha * 100);

                g2.setColor(new Color(142, 138, 217, alpha));

                g2.fillRoundRect(
                        -3, -3,
                        w + 3, h + 3,
                        30, 30
                );

                g2.setColor(new Color(255, 255, 255, 65));
                g2.setStroke(new BasicStroke(5f));

                g2.drawRoundRect(
                        1, 1,
                        w - 3, h - 3,
                        25, 25
                );

            } else if (hovered) {

                g2.setColor(new Color(192, 180, 239, 90));

                g2.fillRoundRect(
                        0, 0, w, h, 25, 25
                );
            }

            if (selected) {

                g2.setPaint(new GradientPaint(
                        0, 0,
                        new Color(255, 255, 255, 230),
                        0, h,
                        new Color(192, 180, 239, 220)
                ));

            } else if (hovered) {

                g2.setPaint(new GradientPaint(
                        0, 0,
                        new Color(255, 255, 255, 235),
                        0, h,
                        new Color(232, 211, 229, 175)
                ));

            } else {

                g2.setPaint(new GradientPaint(
                        0, 0,
                        new Color(255, 255, 255, 155),
                        0, h,
                        new Color(255, 255, 255, 85)
                ));
            }

            g2.fill(shape);

            g2.setColor(new Color(255, 255, 255, 105));

            g2.fillRoundRect(
                    8, 6,
                    w - 17, 15,
                    18, 18
            );

            g2.setStroke(new BasicStroke(1.3f));

            g2.setColor(selected
                    ? new Color(255, 255, 255, 245)
                    : new Color(255, 255, 255, 205));

            g2.draw(shape);

            g2.setStroke(new BasicStroke(1f));
            g2.setColor(new Color(
                    142, 138, 217,
                    selected ? 145 : 65
            ));

            g2.drawRoundRect(
                    4, 4,
                    w - 9, h - 9,
                    21, 21
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
