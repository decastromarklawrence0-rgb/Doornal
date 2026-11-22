
package com.mycompany.doornal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.net.URL;
import java.io.File;

public class LoginFrame extends JFrame {

    private static final Color PLUM =
            new Color(48, 35, 72);

    private static final Color PURPLE =
            new Color(133, 105, 194);

    private static final Color LAVENDER =
            new Color(194, 176, 239);

    private static final Color PINK =
            new Color(245, 177, 211);

    private static final Color PEACH =
            new Color(255, 211, 190);

    private static final Color WHITE =
            new Color(255, 249, 253);

    public LoginFrame() {
        setTitle("Doornal | Your Personal Journal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showSplashScreen();
    }

    // =========================================
    // LOGO LOADER
    // =========================================

    private Image loadLogo() {

        String[] paths = {
            "/images/img/logoo.png",
            "/images/img/logo.png",
            "/images/logoo.png",
            "/images/logo.png",
            "/image/logoo.png",
            "/image/logo.png",
            "/com/mycompany/doornal/image/logoo.png",
            "/com/mycompany/doornal/images/img/logoo.png",
            "/com/mycompany/doornal/logoo.png"
        };

        for (String path : paths) {

            URL url = getClass().getResource(path);

            if (url != null) {
                System.out.println("DOORNAL LOGO FOUND: " + path);
                return new ImageIcon(url).getImage();
            }
        }

        String[] filePaths = {
            "src/main/resources/images/img/logoo.png",
            "src/main/resources/images/img/logo.png",
            "src/main/resources/images/logoo.png",
            "src/main/resources/logo.png"
        };

        for (String path : filePaths) {

            File file = new File(path);

            if (file.exists()) {
                System.out.println(
                        "DOORNAL LOGO FOUND: " +
                        file.getAbsolutePath()
                );

                return new ImageIcon(
                        file.getAbsolutePath()
                ).getImage();
            }
        }

        System.out.println(
                "DOORNAL LOGO NOT FOUND. CHECK RESOURCE PATH."
        );

        return null;
    }

    // =========================================
    // FULL SCREEN SPLASH SCREEN
    // =========================================

    private void showSplashScreen() {

        JWindow splash = new JWindow();

        SplashPanel panel = new SplashPanel(loadLogo());

        splash.setContentPane(panel);

        GraphicsDevice device =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();

        Rectangle screenBounds =
                device.getDefaultConfiguration().getBounds();

        splash.setBounds(screenBounds);
        splash.setVisible(true);

        long start = System.nanoTime();

        Timer timer = new Timer(16, null);

        timer.addActionListener(e -> {

            float elapsed =
                    (System.nanoTime() - start) / 1_000_000_000f;

            float progress = Math.min(elapsed / 6.5f, 1f);

            float fade = Math.max(
                    0f,
                    Math.min((elapsed - 6.5f) / 0.5f, 1f)
            );

            panel.setAnimation(elapsed, progress, fade);

            if (elapsed >= 7f) {

                timer.stop();
                splash.dispose();
                showLoginScreen();
            }
        });

        timer.start();
    }

    // =========================================
    // FULL SCREEN LOGIN SCREEN
    // =========================================

    private void showLoginScreen() {

        AccountPanel accountPanel = new AccountPanel(this);

        setContentPane(accountPanel);

        setMinimumSize(new Dimension(1000, 700));
        setResizable(true);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);

        revalidate();
        repaint();
    }

    // =========================================
    // SPLASH PANEL
    // =========================================

    private class SplashPanel extends JPanel {

        private Image logo;

        private float progress = 0f;
        private float animationTime = 0f;
        private float fade = 0f;

        private final int PARTICLES = 85;

        private final float[] px = new float[PARTICLES];
        private final float[] py = new float[PARTICLES];
        private final float[] size = new float[PARTICLES];
        private final float[] speed = new float[PARTICLES];
        private final float[] phase = new float[PARTICLES];

        public SplashPanel(Image logo) {

            this.logo = logo;

            setOpaque(true);
            setBackground(WHITE);

            for (int i = 0; i < PARTICLES; i++) {

                px[i] = (float) Math.random();
                py[i] = (float) Math.random();

                size[i] = 1.5f +
                        (float) Math.random() * 4f;

                speed[i] = 0.2f +
                        (float) Math.random() * 0.7f;

                phase[i] = (float) (
                        Math.random() * Math.PI * 2
                );
            }
        }

        // =====================================
        // UPDATE ANIMATION
        // =====================================

        public void setAnimation(
                float time,
                float value,
                float fadeValue) {

            animationTime = time;
            progress = value;
            fade = fadeValue;

            repaint();
        }

        // =====================================
        // ANIMATION MATH
        // =====================================

        private float clamp(float v) {
            return Math.max(0f, Math.min(v, 1f));
        }

        private float smooth(float v) {

            v = clamp(v);
            return v * v * (3f - 2f * v);
        }

        private float easeOut(float v) {

            v = clamp(v);
            return 1f - (float) Math.pow(1f - v, 3);
        }

        private float elastic(float v) {

            v = clamp(v);

            if (v == 0f || v == 1f) {
                return v;
            }

            return (float) (
                    Math.pow(2, -10 * v) *
                    Math.sin((v * 10 - 0.75) *
                    (2 * Math.PI / 3)) + 1
            );
        }

        // =====================================
        // PREMIUM BACKGROUND
        // =====================================

        private void drawBackground(Graphics2D g2) {

            int w = getWidth();
            int h = getHeight();

            GradientPaint bg = new GradientPaint(
                    0, 0,
                    new Color(255, 249, 253),
                    w, h,
                    new Color(224, 214, 246)
            );

            g2.setPaint(bg);
            g2.fillRect(0, 0, w, h);

            float wave1 =
                    (float) Math.sin(animationTime * 0.45f);

            float wave2 =
                    (float) Math.cos(animationTime * 0.38f);

            int pinkX = (int) (90 + wave1 * 70);
            int pinkY = (int) (100 + wave2 * 55);

            int peachX = (int) (w - 80 + wave2 * 60);
            int peachY = (int) (h - 90 + wave1 * 45);

            drawGlow(g2, pinkX, pinkY, 300, PINK, 95);
            drawGlow(g2, peachX, peachY, 300, PEACH, 95);
            drawGlow(g2, w / 2, h / 2, 390, LAVENDER, 75);

            Graphics2D spotlight =
                    (Graphics2D) g2.create();

            spotlight.rotate(
                    Math.sin(animationTime * 0.3) * 0.08,
                    w / 2,
                    h / 2
            );

            GradientPaint light = new GradientPaint(
                    0, h / 2,
                    new Color(255, 255, 255, 0),
                    w, h / 2,
                    new Color(255, 255, 255, 65)
            );

            spotlight.setPaint(light);
            spotlight.fillRect(0, 0, w, h);
            spotlight.dispose();

            g2.setColor(
                    new Color(255, 255, 255, 155)
            );

            g2.setStroke(new BasicStroke(1.2f));

            g2.drawRoundRect(
                    13, 13,
                    w - 26,
                    h - 26,
                    30, 30
            );

            g2.setColor(
                    new Color(181, 158, 220, 45)
            );

            g2.drawRoundRect(
                    20, 20,
                    w - 40,
                    h - 40,
                    27, 27
            );
        }

        // =====================================
        // AMBIENT GLOW
        // =====================================

        private void drawGlow(
                Graphics2D g2,
                int x,
                int y,
                int radius,
                Color color,
                int opacity) {

            for (int i = 12; i >= 1; i--) {

                int diameter = radius + i * 20;
                int alpha = opacity / (i + 2);

                g2.setColor(
                        new Color(
                                color.getRed(),
                                color.getGreen(),
                                color.getBlue(),
                                alpha
                        )
                );

                g2.fillOval(
                        x - diameter / 2,
                        y - diameter / 2,
                        diameter,
                        diameter
                );
            }
        }

        // =====================================
        // GLASS PLATFORM
        // =====================================

        private void drawGlassPlatform(
                Graphics2D g2,
                int cx,
                int cy,
                float alpha) {

            Graphics2D glass =
                    (Graphics2D) g2.create();

            glass.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            glass.setColor(
                    new Color(89, 68, 128, 28)
            );

            glass.fillRoundRect(
                    cx - 175,
                    cy - 145,
                    350,
                    290,
                    95, 95
            );

            GradientPaint body = new GradientPaint(
                    cx - 150,
                    cy - 125,
                    new Color(255, 255, 255, 205),
                    cx + 150,
                    cy + 125,
                    new Color(225, 213, 247, 115)
            );

            glass.setPaint(body);

            glass.fillRoundRect(
                    cx - 155,
                    cy - 125,
                    310,
                    250,
                    80, 80
            );

            glass.setColor(
                    new Color(255, 255, 255, 210)
            );

            glass.setStroke(new BasicStroke(2f));

            glass.drawRoundRect(
                    cx - 155,
                    cy - 125,
                    310,
                    250,
                    80, 80
            );

            glass.setColor(
                    new Color(255, 255, 255, 85)
            );

            glass.drawRoundRect(
                    cx - 148,
                    cy - 118,
                    296,
                    236,
                    75, 75
            );

            glass.setStroke(
                    new BasicStroke(
                            3f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            glass.setColor(
                    new Color(255, 255, 255, 125)
            );

            glass.drawArc(
                    cx - 125,
                    cy - 112,
                    250,
                    100,
                    20,
                    140
            );

            glass.setStroke(new BasicStroke(1.5f));

            glass.setColor(
                    new Color(193, 176, 239, 105)
            );

            glass.drawArc(
                    cx - 100,
                    cy + 55,
                    200,
                    65,
                    190,
                    160
            );

            glass.dispose();
        }

        // =====================================
        // FLOATING PARTICLES
        // =====================================

        private void drawParticles(Graphics2D g2) {

            int w = getWidth();
            int h = getHeight();

            Color[] colors = {
                LAVENDER, PINK, PEACH, PURPLE
            };

            for (int i = 0; i < PARTICLES; i++) {

                float drift =
                        (float) Math.sin(
                                animationTime * 0.7f + phase[i]
                        ) * 13f;

                float x = px[i] * w + drift;

                float y = py[i] * h -
                        animationTime * speed[i] * 16f;

                y %= h;

                if (y < 0) {
                    y += h;
                }

                float blink = 0.5f +
                        0.5f * (float) Math.sin(
                                animationTime * 2.5f + phase[i]
                        );

                int alpha = (int) (130 * blink);

                Color c = colors[i % colors.length];

                g2.setColor(
                        new Color(
                                c.getRed(),
                                c.getGreen(),
                                c.getBlue(),
                                alpha
                        )
                );

                float s = size[i];

                g2.fillOval(
                        (int) x,
                        (int) y,
                        (int) s + 1,
                        (int) s + 1
                );

                if (i % 5 == 0) {

                    drawSparkle(
                            g2,
                            (int) x,
                            (int) y,
                            5 + (int) (blink * 4),
                            alpha
                    );
                }
            }
        }

        // =====================================
        // STAR SPARKLES
        // =====================================

        private void drawSparkle(
                Graphics2D g2,
                int x,
                int y,
                int radius,
                int alpha) {

            Graphics2D sg =
                    (Graphics2D) g2.create();

            sg.setColor(
                    new Color(255, 255, 255, alpha)
            );

            sg.setStroke(
                    new BasicStroke(
                            1.2f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            sg.drawLine(x - radius, y, x + radius, y);
            sg.drawLine(x, y - radius, x, y + radius);

            sg.fillOval(x - 2, y - 2, 4, 4);

            sg.dispose();
        }

        // =====================================
        // ROTATING ORBIT RINGS
        // =====================================

        private void drawOrbit(
                Graphics2D g2,
                int cx,
                int cy,
                float alpha) {

            Graphics2D orbit =
                    (Graphics2D) g2.create();

            orbit.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            orbit.translate(cx, cy);

            orbit.rotate(animationTime * 0.42);

            orbit.setStroke(new BasicStroke(2f));

            orbit.setPaint(
                    new GradientPaint(
                            -180, -100, PINK,
                            180, 100, PURPLE
                    )
            );

            orbit.drawOval(-185, -132, 370, 264);

            orbit.rotate(-animationTime * 0.8);

            orbit.setStroke(
                    new BasicStroke(
                            1.5f,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND,
                            10f,
                            new float[]{5f, 12f},
                            0f
                    )
            );

            orbit.setColor(
                    new Color(133, 105, 194, 180)
            );

            orbit.drawOval(-200, -105, 400, 210);

            orbit.rotate(animationTime * 0.3);

            orbit.setStroke(new BasicStroke(1f));

            orbit.setColor(
                    new Color(255, 255, 255, 155)
            );

            orbit.drawOval(-215, -115, 430, 230);

            double angle = animationTime * 1.2;

            int dotX = (int) (Math.cos(angle) * 190);
            int dotY = (int) (Math.sin(angle) * 115);

            drawGlow(orbit, dotX, dotY, 22, PINK, 100);

            orbit.setColor(Color.WHITE);

            orbit.fillOval(dotX - 4, dotY - 4, 8, 8);

            double angle2 = -animationTime * 0.85 + 2.2;

            int dotX2 = (int) (Math.cos(angle2) * 205);
            int dotY2 = (int) (Math.sin(angle2) * 110);

            orbit.setColor(
                    new Color(255, 255, 255, 200)
            );

            orbit.fillOval(dotX2 - 3, dotY2 - 3, 6, 6);

            orbit.dispose();
        }

        // =====================================
        // LOGO ANIMATION WITH ROUNDED EDGES
        // =====================================

        private void drawLogo(
                Graphics2D g2,
                int cx,
                int cy,
                float fadeAlpha) {

            if (logo == null) {
                return;
            }

            float entrance = elastic(progress / 0.32f);

            float breathing =
                    (float) Math.sin(animationTime * 1.8f);

            float scale =
                    0.18f + entrance * 0.82f
                    + breathing * 0.012f;

            float floating =
                    (float) Math.sin(animationTime * 1.3f) * 5f;

            float rotation =
                    (1f - entrance) * -0.18f
                    + (float) Math.sin(animationTime * 0.7f) * 0.008f;

            int logoSize = 205;

            Graphics2D lg =
                    (Graphics2D) g2.create();

            lg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(fadeAlpha * entrance)
                    )
            );

            lg.translate(cx, cy + floating);
            lg.rotate(rotation);
            lg.scale(scale, scale);

            // Logo shadow

            lg.setColor(
                    new Color(69, 50, 100, 40)
            );

            lg.fillOval(-108, -62, 216, 216);

            // Logo ambient glow

            drawGlow(lg, 0, 0, 145, LAVENDER, 100);

            // =================================
            // ROUNDED LOGO IMAGE
            // =================================

            int logoX = -logoSize / 2;
            int logoY = -logoSize / 2;

            RoundRectangle2D roundedShape =
                    new RoundRectangle2D.Float(
                            logoX,
                            logoY,
                            logoSize,
                            logoSize,
                            38,
                            38
                    );

            // Soft shadow behind rounded image

            lg.setColor(
                    new Color(70, 48, 100, 35)
            );

            lg.fill(
                    new RoundRectangle2D.Float(
                            logoX - 3,
                            logoY + 5,
                            logoSize + 6,
                            logoSize + 6,
                            42,
                            42
                    )
            );

            // Clip logo corners

            Shape previousClip = lg.getClip();

            lg.clip(roundedShape);

            lg.drawImage(
                    logo,
                    logoX,
                    logoY,
                    logoSize,
                    logoSize,
                    this
            );

            lg.setClip(previousClip);

            // =================================
            // GLASS EDGE OUTLINE
            // =================================

            lg.setColor(
                    new Color(255, 255, 255, 210)
            );

            lg.setStroke(new BasicStroke(2f));

            lg.draw(roundedShape);

            // Inner lavender outline

            lg.setColor(
                    new Color(194, 176, 239, 120)
            );

            lg.setStroke(new BasicStroke(1f));

            lg.draw(
                    new RoundRectangle2D.Float(
                            logoX + 3,
                            logoY + 3,
                            logoSize - 6,
                            logoSize - 6,
                            34,
                            34
                    )
            );

            // =================================
            // MOVING LOGO REFLECTION
            // =================================

            float shinePosition =
                    (progress * 1.7f) % 1.4f;

            int shineX = -140 +
                    (int) (shinePosition * 280);

            Graphics2D reflection =
                    (Graphics2D) lg.create();

            reflection.clip(roundedShape);

            reflection.setComposite(
                    AlphaComposite.SrcAtop.derive(0.30f)
            );

            GradientPaint shine = new GradientPaint(
                    shineX - 35,
                    -100,
                    new Color(255, 255, 255, 0),
                    shineX + 35,
                    100,
                    new Color(255, 255, 255, 150)
            );

            reflection.setPaint(shine);

            Polygon sweep = new Polygon();

            sweep.addPoint(shineX - 50, -120);
            sweep.addPoint(shineX + 5, -120);
            sweep.addPoint(shineX + 110, 120);
            sweep.addPoint(shineX + 55, 120);

            reflection.fillPolygon(sweep);

            reflection.dispose();

            lg.dispose();
        }

        // =====================================
        // PREMIUM 3D TITLE
        // =====================================

        private void drawTitle(
                Graphics2D g2,
                int cx,
                int y,
                float alpha) {

            if (alpha <= 0f) {
                return;
            }

            Graphics2D tg =
                    (Graphics2D) g2.create();

            tg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            tg.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            String title = "DOORNAL";

            Font font = new Font(
                    "Serif",
                    Font.BOLD,
                    56
            );

            tg.setFont(font);

            FontMetrics fm = tg.getFontMetrics();

            int fullWidth = fm.stringWidth(title);
            int x = cx - fullWidth / 2;

            int visibleWidth = (int) (
                    fullWidth * smooth(
                            (progress - 0.32f) / 0.30f
                    )
            );

            // 3D depth

            for (int i = 10; i >= 1; i--) {

                tg.setColor(new Color(57, 40, 84));

                tg.drawString(
                        title,
                        x + i,
                        y + i
                );
            }

            GradientPaint metal = new GradientPaint(
                    0, y - 55,
                    Color.WHITE,
                    0, y + 10,
                    new Color(126, 96, 177)
            );

            tg.setPaint(metal);

            Shape oldClip = tg.getClip();

            tg.clipRect(
                    x,
                    y - 60,
                    visibleWidth,
                    75
            );

            tg.drawString(title, x, y);

            tg.setColor(
                    new Color(255, 255, 255, 190)
            );

            tg.drawString(title, x, y - 1);

            tg.setClip(oldClip);

            // Metallic shine

            float shinePosition =
                    (progress * 2.1f) % 1.4f;

            int shineX = x - 100 +
                    (int) (
                            shinePosition * (fullWidth + 200)
                    );

            tg.clipRect(
                    x,
                    y - 58,
                    fullWidth,
                    70
            );

            GradientPaint shine = new GradientPaint(
                    shineX - 35,
                    y - 20,
                    new Color(255, 255, 255, 0),
                    shineX + 35,
                    y - 20,
                    new Color(255, 255, 255, 165)
            );

            tg.setPaint(shine);
            tg.drawString(title, x, y);

            tg.setClip(oldClip);

            // Underline accent

            int lineWidth = (int) (
                    fullWidth * smooth(
                            (progress - 0.42f) / 0.22f
                    )
            );

            GradientPaint line = new GradientPaint(
                    cx - lineWidth / 2,
                    y + 13,
                    PINK,
                    cx + lineWidth / 2,
                    y + 13,
                    PURPLE
            );

            tg.setPaint(line);

            tg.fillRoundRect(
                    cx - lineWidth / 2,
                    y + 12,
                    lineWidth,
                    2,
                    10,
                    10
            );

            tg.dispose();
        }

        // =====================================
        // SUBTITLE
        // =====================================

        private void drawSubtitle(
                Graphics2D g2,
                int cx,
                int y,
                float alpha) {

            if (alpha <= 0f) {
                return;
            }

            Graphics2D tg =
                    (Graphics2D) g2.create();

            tg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            String subtitle =
                    "Y O U R   P E R S O N A L   J O U R N A L";

            tg.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            10
                    )
            );

            FontMetrics fm = tg.getFontMetrics();

            int x = cx - fm.stringWidth(subtitle) / 2;

            tg.setColor(
                    new Color(103, 81, 151)
            );

            tg.drawString(subtitle, x, y);

            tg.dispose();
        }

        // =====================================
        // DECORATIVE FOOTER
        // =====================================

        private void drawFooter(
                Graphics2D g2,
                int width,
                int height,
                float alpha) {

            Graphics2D fg =
                    (Graphics2D) g2.create();

            fg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            fg.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            10
                    )
            );

            fg.setColor(
                    new Color(112, 93, 151, 190)
            );

            String text =
                    "A LITTLE SPACE THAT'S YOURS";

            FontMetrics fm = fg.getFontMetrics();

            fg.drawString(
                    text,
                    width / 2 - fm.stringWidth(text) / 2,
                    height - 83
            );

            fg.dispose();
        }

        // =====================================
        // PREMIUM LOADING BAR
        // =====================================

        private void drawLoading(
                Graphics2D g2,
                int cx,
                int height,
                float alpha) {

            Graphics2D lg =
                    (Graphics2D) g2.create();

            lg.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            clamp(alpha)
                    )
            );

            int width = 190;
            int x = cx - width / 2;
            int y = height - 48;

            lg.setColor(
                    new Color(255, 255, 255, 175)
            );

            lg.fillRoundRect(
                    x - 2,
                    y - 2,
                    width + 4,
                    9,
                    20,
                    20
            );

            lg.setColor(
                    new Color(176, 155, 222, 100)
            );

            lg.fillRoundRect(
                    x,
                    y,
                    width,
                    5,
                    20,
                    20
            );

            int filled = (int) (
                    width * progress
            );

            GradientPaint gradient = new GradientPaint(
                    x,
                    y,
                    PINK,
                    x + width,
                    y,
                    PURPLE
            );

            lg.setPaint(gradient);

            lg.fillRoundRect(
                    x,
                    y,
                    filled,
                    5,
                    20,
                    20
            );

            if (filled > 8) {

                drawGlow(
                        lg,
                        x + filled,
                        y + 2,
                        25,
                        PINK,
                        100
                );

                lg.setColor(Color.WHITE);

                lg.fillOval(
                        x + filled - 4,
                        y - 2,
                        9,
                        9
                );
            }

            lg.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            9
                    )
            );

            lg.setColor(
                    new Color(112, 93, 151, 190)
            );

            String percent =
                    (int) (progress * 100) + "%";

            FontMetrics fm = lg.getFontMetrics();

            lg.drawString(
                    percent,
                    cx - fm.stringWidth(percent) / 2,
                    y - 12
            );

            lg.dispose();
        }

        // =====================================
        // MAIN PAINT
        // =====================================

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
            );

            int w = getWidth();
            int h = getHeight();

            int cx = w / 2;
            int cy = (int) (h * 0.43);

            drawBackground(g2);
            drawParticles(g2);

            float platformAlpha = easeOut(
                    (progress - 0.02f) / 0.30f
            );

            float platformScale =
                    0.75f + 0.25f * platformAlpha;

            Graphics2D platform =
                    (Graphics2D) g2.create();

            platform.translate(cx, cy);
            platform.scale(platformScale, platformScale);
            platform.translate(-cx, -cy);

            drawGlassPlatform(
                    platform,
                    cx,
                    cy,
                    platformAlpha
            );

            platform.dispose();

            float glowPulse =
                    0.8f +
                    0.2f * (float) Math.sin(
                            animationTime * 1.4f
                    );

            drawGlow(
                    g2,
                    cx,
                    cy,
                    220,
                    PINK,
                    (int) (90 * glowPulse)
            );

            drawGlow(
                    g2,
                    cx,
                    cy,
                    190,
                    LAVENDER,
                    110
            );

            float orbitAlpha = easeOut(
                    (progress - 0.08f) / 0.30f
            );

            drawOrbit(g2, cx, cy, orbitAlpha);

            drawLogo(
                    g2,
                    cx,
                    cy,
                    easeOut(progress / 0.12f)
            );

            float titleAlpha = easeOut(
                    (progress - 0.28f) / 0.24f
            );

            drawTitle(
                    g2,
                    cx,
                    cy + 187,
                    titleAlpha
            );

            float subtitleAlpha = easeOut(
                    (progress - 0.52f) / 0.20f
            );

            drawSubtitle(
                    g2,
                    cx,
                    cy + 222,
                    subtitleAlpha
            );

            float footerAlpha = easeOut(
                    (progress - 0.67f) / 0.18f
            );

            drawFooter(
                    g2,
                    w,
                    h,
                    footerAlpha
            );

            drawLoading(g2, cx, h, 1f);

            // Final smooth fade

            if (fade > 0f) {

                int alpha = (int) (
                        smooth(fade) * 255
                );

                g2.setColor(
                        new Color(255, 249, 253, alpha)
                );

                g2.fillRect(0, 0, w, h);
            }

            g2.dispose();
        }
    }
}
