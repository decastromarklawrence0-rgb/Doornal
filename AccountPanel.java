
package com.mycompany.doornal;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;

public class AccountPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JPasswordField confirmPasswordField;

    private JFrame loginFrame;

    private static final String USER_FILE = "users.dat";

    private ArrayList<User> users;

    // =====================================
    // DOORNAL OFFICIAL COLOR PALETTE
    // =====================================

    private final Color BLUSH =
            new Color(0xFC, 0xE1, 0xDD);

    private final Color PEACH =
            new Color(0xFC, 0xD3, 0xBF);

    private final Color LAVENDER =
            new Color(0xD2, 0xC5, 0xF0);

    private final Color PERIWINKLE =
            new Color(0xC0, 0xB4, 0xEF);

    private final Color PINK_LAVENDER =
            new Color(0xE8, 0xD3, 0xE5);

    private final Color PURPLE =
            new Color(0x8E, 0x8A, 0xD9);

    private final Color MAUVE =
            new Color(0xC1, 0x89, 0xAF);

    private final Color DEEP_PLUM =
            new Color(0x29, 0x22, 0x3F);

    private final Color WHITE =
            new Color(0xFF, 0xFC, 0xFC);

    private final Color FIELD_BACKGROUND =
            new Color(0xFF, 0xF8, 0xF8, 125);

    // =====================================
    // MAIN COMPONENTS
    // =====================================

    private JPanel card;
    private JPanel loginForm;
    private JPanel signupForm;

    private WelcomePanel welcomePanel;

    private Timer slideTimer;

    private boolean showingLogin = true;

    private final int PANEL_WIDTH = 450;
    private final int PANEL_HEIGHT = 500;

    private int targetX;

    public AccountPanel(JFrame loginFrame) {

        this.loginFrame = loginFrame;
        this.users = loadUsers();

        setLayout(null);
        setOpaque(false);

        setPreferredSize(new Dimension(1000, 700));

        createMainCard();
        createLoginForm();
        createSignupForm();
        createWelcomePanel();
    }

    // =====================================
    // AUTOMATIC CENTER POSITIONING
    // =====================================

    @Override
    public void doLayout() {

        super.doLayout();

        if (card != null) {

            int cardWidth = 900;
            int cardHeight = PANEL_HEIGHT;

            int x = (getWidth() - cardWidth) / 2;
            int y = (getHeight() - cardHeight) / 2;

            card.setBounds(
                    x,
                    y,
                    cardWidth,
                    cardHeight
            );
        }
    }

    // =====================================
    // BACKGROUND WITH SOFT GLASS LIGHTING
    // =====================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int width = getWidth();
        int height = getHeight();

        GradientPaint gradient = new GradientPaint(
                0, 0, BLUSH,
                width, height, LAVENDER
        );

        g2.setPaint(gradient);
        g2.fillRect(0, 0, width, height);

        // Soft lavender ambient circle

        g2.setColor(new Color(
                PERIWINKLE.getRed(),
                PERIWINKLE.getGreen(),
                PERIWINKLE.getBlue(),
                65
        ));

        g2.fill(
                new Ellipse2D.Float(
                        width - 180,
                        -90,
                        230,
                        230
                )
        );

        // Soft peach ambient circle

        g2.setColor(new Color(
                PEACH.getRed(),
                PEACH.getGreen(),
                PEACH.getBlue(),
                85
        ));

        g2.fill(
                new Ellipse2D.Float(
                        -90,
                        height - 130,
                        230,
                        230
                )
        );

        // Subtle center lighting

        g2.setColor(new Color(
                WHITE.getRed(),
                WHITE.getGreen(),
                WHITE.getBlue(),
                35
        ));

        g2.fillOval(
                width / 2 - 155,
                height / 2 - 155,
                310,
                310
        );

        // =================================
        // CENTERED OUTER GLASS CARD GLOW
        // =================================

        int cardX = (width - 900) / 2;
        int cardY = (height - 500) / 2;

        RoundRectangle2D glowShape =
                new RoundRectangle2D.Float(
                        cardX,
                        cardY,
                        900,
                        500,
                        38,
                        38
                );

        // Outer mauve glow

        g2.setColor(new Color(
                MAUVE.getRed(),
                MAUVE.getGreen(),
                MAUVE.getBlue(),
                22
        ));

        g2.setStroke(new BasicStroke(24f));
        g2.draw(glowShape);

        // Middle lavender glow

        g2.setColor(new Color(
                LAVENDER.getRed(),
                LAVENDER.getGreen(),
                LAVENDER.getBlue(),
                42
        ));

        g2.setStroke(new BasicStroke(14f));
        g2.draw(glowShape);

        // Inner white glow

        g2.setColor(new Color(
                WHITE.getRed(),
                WHITE.getGreen(),
                WHITE.getBlue(),
                90
        ));

        g2.setStroke(new BasicStroke(3f));
        g2.draw(glowShape);

        g2.dispose();
    }

    // =====================================
    // MAIN FROSTED GLASS CARD
    // =====================================

    private void createMainCard() {

        card = new RoundedPanel(30);

        card.setLayout(null);

        card.setBounds(
                0, 0,
                900, PANEL_HEIGHT
        );

        add(card);
    }

    // =====================================
    // LOGIN FORM
    // =====================================

    private void createLoginForm() {

        loginForm = new JPanel(null);
        loginForm.setOpaque(false);

        loginForm.setBounds(
                0, 0,
                PANEL_WIDTH,
                PANEL_HEIGHT
        );

        JLabel title = new JLabel("Sign In");

        title.setFont(new Font(
                "Segoe UI", Font.BOLD, 30
        ));

        title.setForeground(DEEP_PLUM);

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        title.setBounds(40, 65, 370, 45);

        JLabel subtitle = new JLabel(
                "Welcome back to Doornal!"
        );

        subtitle.setFont(new Font(
                "Segoe UI", Font.PLAIN, 13
        ));

        subtitle.setForeground(MAUVE);

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        subtitle.setBounds(40, 112, 370, 25);

        JLabel usernameLabel =
                createLabel("Username");

        usernameLabel.setBounds(
                65, 175, 320, 25
        );

        usernameField = createTextField();

        usernameField.setBounds(
                65, 205, 320, 42
        );

        JLabel passwordLabel =
                createLabel("Password");

        passwordLabel.setBounds(
                65, 270, 320, 25
        );

        passwordField = createPasswordField();

        passwordField.setBounds(
                65, 300, 320, 42
        );

        JButton loginButton = createButton(
                "SIGN IN", PURPLE
        );

        loginButton.setBounds(
                65, 375, 320, 45
        );

        JLabel footer = new JLabel(
                "Your personal space is waiting for you."
        );

        footer.setFont(new Font(
                "Segoe UI", Font.PLAIN, 11
        ));

        footer.setForeground(MAUVE);

        footer.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        footer.setBounds(
                45, 435, 370, 25
        );

        loginButton.addActionListener(e -> login());

        passwordField.addActionListener(e -> login());

        usernameField.addActionListener(
                e -> passwordField.requestFocus()
        );

        loginForm.add(title);
        loginForm.add(subtitle);
        loginForm.add(usernameLabel);
        loginForm.add(usernameField);
        loginForm.add(passwordLabel);
        loginForm.add(passwordField);
        loginForm.add(loginButton);
        loginForm.add(footer);

        card.add(loginForm);
    }

    // =====================================
    // SIGN UP FORM
    // =====================================

    private void createSignupForm() {

        signupForm = new JPanel(null);
        signupForm.setOpaque(false);

        signupForm.setBounds(
                PANEL_WIDTH, 0,
                PANEL_WIDTH,
                PANEL_HEIGHT
        );

        JLabel title = new JLabel("Create Account");

        title.setFont(new Font(
                "Segoe UI", Font.BOLD, 28
        ));

        title.setForeground(DEEP_PLUM);

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        title.setBounds(
                35, 45, 380, 45
        );

        JLabel subtitle = new JLabel(
                "Start your personal journaling journey."
        );

        subtitle.setFont(new Font(
                "Segoe UI", Font.PLAIN, 12
        ));

        subtitle.setForeground(MAUVE);

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        subtitle.setBounds(
                35, 90, 380, 25
        );

        JLabel usernameLabel =
                createLabel("Username");

        usernameLabel.setBounds(
                65, 140, 320, 22
        );

        signupUsernameField = createTextField();

        signupUsernameField.setBounds(
                65, 165, 320, 38
        );

        JLabel passwordLabel =
                createLabel("Password");

        passwordLabel.setBounds(
                65, 215, 320, 22
        );

        signupPasswordField = createPasswordField();

        signupPasswordField.setBounds(
                65, 240, 320, 38
        );

        JLabel confirmLabel =
                createLabel("Confirm Password");

        confirmLabel.setBounds(
                65, 290, 320, 22
        );

        confirmPasswordField = createPasswordField();

        confirmPasswordField.setBounds(
                65, 315, 320, 38
        );

        JButton signupButton = createButton(
                "CREATE ACCOUNT", PURPLE
        );

        signupButton.setBounds(
                65, 375, 320, 45
        );

        signupButton.addActionListener(e -> signup());

        confirmPasswordField.addActionListener(
                e -> signup()
        );

        signupForm.add(title);
        signupForm.add(subtitle);
        signupForm.add(usernameLabel);
        signupForm.add(signupUsernameField);
        signupForm.add(passwordLabel);
        signupForm.add(signupPasswordField);
        signupForm.add(confirmLabel);
        signupForm.add(confirmPasswordField);
        signupForm.add(signupButton);

        card.add(signupForm);
    }

    // =====================================
    // WELCOME GLASS PANEL
    // =====================================

    private void createWelcomePanel() {

        welcomePanel = new WelcomePanel();

        welcomePanel.setBounds(
                PANEL_WIDTH, 0,
                PANEL_WIDTH,
                PANEL_HEIGHT
        );

        card.add(welcomePanel);

        card.setComponentZOrder(welcomePanel, 0);

        welcomePanel.setWelcomeContent(
                "Hello, Friend!",
                "Register with your personal details\n"
                + "to use all of our features.",
                "SIGN UP"
        );

        welcomePanel.actionButton.addActionListener(
                e -> toggleForm()
        );
    }

    // =====================================
    // SLIDING ANIMATION
    // =====================================

    private void toggleForm() {

        if (slideTimer != null
                && slideTimer.isRunning()) {
            return;
        }

        showingLogin = !showingLogin;

        targetX = showingLogin ? PANEL_WIDTH : 0;

        welcomePanel.setWelcomeContent(

                showingLogin
                ? "Hello, Friend!"
                : "Welcome Back!",

                showingLogin
                ? "Register with your personal details\n"
                  + "to use all of our features."
                : "Sign in to continue your\n"
                  + "personal journaling journey.",

                showingLogin ? "SIGN UP" : "SIGN IN"
        );

        slideTimer = new Timer(10, null);

        slideTimer.addActionListener(e -> {

            int currentX = welcomePanel.getX();

            int speed = 12;

            if (currentX < targetX) {

                currentX = Math.min(
                        currentX + speed,
                        targetX
                );

            } else if (currentX > targetX) {

                currentX = Math.max(
                        currentX - speed,
                        targetX
                );
            }

            welcomePanel.setLocation(
                    currentX, 0
            );

            if (currentX == targetX) {
                slideTimer.stop();
            }
        });

        slideTimer.start();
    }

    // =====================================
    // FORM LABELS
    // =====================================

    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font(
                "Segoe UI", Font.BOLD, 13
        ));

        label.setForeground(DEEP_PLUM);

        return label;
    }

    // =====================================
    // GLASS TEXT FIELDS
    // =====================================

    private JTextField createTextField() {

        JTextField field = new JTextField() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(FIELD_BACKGROUND);

                g2.fillRoundRect(
                        0, 0,
                        getWidth(),
                        getHeight(),
                        24, 24
                );

                g2.setColor(new Color(
                        WHITE.getRed(),
                        WHITE.getGreen(),
                        WHITE.getBlue(),
                        75
                ));

                g2.fillRoundRect(
                        3, 2,
                        getWidth() - 6,
                        10,
                        18, 18
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        styleField(field);

        return field;
    }

    private JPasswordField createPasswordField() {

        JPasswordField field = new JPasswordField() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(FIELD_BACKGROUND);

                g2.fillRoundRect(
                        0, 0,
                        getWidth(),
                        getHeight(),
                        24, 24
                );

                g2.setColor(new Color(
                        WHITE.getRed(),
                        WHITE.getGreen(),
                        WHITE.getBlue(),
                        75
                ));

                g2.fillRoundRect(
                        3, 2,
                        getWidth() - 6,
                        10,
                        18, 18
                );

                g2.dispose();

                super.paintComponent(g);
            }
        };

        styleField(field);

        return field;
    }

    private void styleField(JTextField field) {

        field.setFont(new Font(
                "Segoe UI", Font.PLAIN, 14
        ));

        field.setForeground(DEEP_PLUM);

        field.setBackground(FIELD_BACKGROUND);

        field.setCaretColor(PURPLE);

        field.setOpaque(false);

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new GlassFieldBorder(),
                        new EmptyBorder(8, 12, 8, 12)
                )
        );
    }

    // =====================================
    // GLASS FIELD BORDER
    // =====================================

    private class GlassFieldBorder extends AbstractBorder {

        @Override
        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    210
            ));

            g2.setStroke(new BasicStroke(2f));

            g2.drawRoundRect(
                    x + 1, y + 1,
                    width - 3,
                    height - 3,
                    24, 24
            );

            g2.setColor(new Color(
                    LAVENDER.getRed(),
                    LAVENDER.getGreen(),
                    LAVENDER.getBlue(),
                    115
            ));

            g2.setStroke(new BasicStroke(1f));

            g2.drawRoundRect(
                    x + 3, y + 3,
                    width - 7,
                    height - 7,
                    20, 20
            );

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 6, 4, 6);
        }
    }

    // =====================================
    // GRADIENT GLASS BUTTONS
    // =====================================

    private JButton createButton(
            String text, Color color) {

        return new GlassButton(
                text,
                color,
                WHITE
        );
    }

    private class GlassButton extends JButton {

        private Color baseColor;
        private Color textColor;
        private boolean hovered = false;

        public GlassButton(
                String text,
                Color baseColor,
                Color textColor) {

            super(text);

            this.baseColor = baseColor;
            this.textColor = textColor;

            setFont(new Font(
                    "Segoe UI", Font.BOLD, 13
            ));

            setForeground(textColor);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(new Cursor(
                    Cursor.HAND_CURSOR
            ));

            setBorder(new EmptyBorder(
                    8, 15, 8, 15
            ));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
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

            g2.setColor(new Color(
                    PURPLE.getRed(),
                    PURPLE.getGreen(),
                    PURPLE.getBlue(),
                    45
            ));

            g2.fillRoundRect(
                    2, 4,
                    w - 4,
                    h - 4,
                    26, 26
            );

            Color top = hovered ? MAUVE : baseColor;
            Color bottom = hovered ? PURPLE : MAUVE;

            GradientPaint gradient = new GradientPaint(
                    0, 0, top,
                    0, h, bottom
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                    0, 0,
                    w - 2,
                    h - 5,
                    25, 25
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    hovered ? 65 : 45
            ));

            g2.fillRoundRect(
                    3, 2,
                    w - 8,
                    12,
                    18, 18
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    150
            ));

            g2.setStroke(new BasicStroke(1.3f));

            g2.drawRoundRect(
                    1, 1,
                    w - 4,
                    h - 7,
                    24, 24
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =====================================
    // SIGN UP FUNCTION
    // =====================================

    private void signup() {

        String username =
                signupUsernameField.getText().trim();

        String password =
                String.valueOf(
                        signupPasswordField.getPassword()
                );

        String confirmPassword =
                String.valueOf(
                        confirmPasswordField.getPassword()
                );

        if (username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields."
            );

            return;
        }

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match."
            );

            return;
        }

        for (User user : users) {

            if (user.getUsername()
                    .equalsIgnoreCase(username)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Username already exists."
                );

                return;
            }
        }

        User newUser = new User(username, password);

        users.add(newUser);

        saveUsers(users);

        JOptionPane.showMessageDialog(
                this,
                "Account created successfully!"
        );

        signupUsernameField.setText("");
        signupPasswordField.setText("");
        confirmPasswordField.setText("");

        if (!showingLogin) {
            toggleForm();
        }

        usernameField.setText(username);
        passwordField.setText("");
    }

    // =====================================
    // LOGIN FUNCTION
    // =====================================

    private void login() {

        String username =
                usernameField.getText().trim();

        String password =
                String.valueOf(
                        passwordField.getPassword()
                );

        for (User user : users) {

            if (user.getUsername().equals(username)
                    && user.getPassword().equals(password)) {

                CurrentUser.login(user);

                JOptionPane.showMessageDialog(
                        this,
                        "Welcome " + user.getDisplayName()
                );

                loginFrame.dispose();

                SwingUtilities.invokeLater(() ->
                        new MainFrameD()
                );

                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                "Invalid username or password."
        );
    }

    // =====================================
    // SAVE USERS
    // =====================================

    public static void saveUsers(
            ArrayList<User> users) {

        try (ObjectOutputStream out =
                new ObjectOutputStream(
                        new FileOutputStream(USER_FILE))) {

            out.writeObject(users);

        } catch (IOException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Unable to save users."
            );
        }
    }

    // =====================================
    // LOAD USERS
    // =====================================

    @SuppressWarnings("unchecked")
    private ArrayList<User> loadUsers() {

        File file = new File(USER_FILE);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(file))) {

            return (ArrayList<User>) in.readObject();

        } catch (IOException |
                ClassNotFoundException e) {

            return new ArrayList<>();
        }
    }

    // =====================================
    // GET USERS
    // =====================================

    @SuppressWarnings("unchecked")
    public static ArrayList<User> getUsers() {

        File file = new File(USER_FILE);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in =
                new ObjectInputStream(
                        new FileInputStream(file))) {

            return (ArrayList<User>) in.readObject();

        } catch (IOException |
                ClassNotFoundException e) {

            return new ArrayList<>();
        }
    }

    // =====================================
    // ROUNDED FROSTED GLASS MAIN CARD
    // =====================================

    private class RoundedPanel extends JPanel {

        private int radius;

        public RoundedPanel(int radius) {

            this.radius = radius;

            setOpaque(false);
        }

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

            GradientPaint glassGradient = new GradientPaint(
                    0, 0,
                    new Color(
                            WHITE.getRed(),
                            WHITE.getGreen(),
                            WHITE.getBlue(),
                            205
                    ),

                    w, h,
                    new Color(
                            BLUSH.getRed(),
                            BLUSH.getGreen(),
                            BLUSH.getBlue(),
                            150
                    )
            );

            g2.setPaint(glassGradient);

            g2.fillRoundRect(
                    0, 0,
                    w, h,
                    radius, radius
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    75
            ));

            g2.fillRoundRect(
                    5, 4,
                    w - 10,
                    75,
                    radius, radius
            );

            GradientPaint lowerTint = new GradientPaint(
                    0, h - 100,
                    new Color(
                            LAVENDER.getRed(),
                            LAVENDER.getGreen(),
                            LAVENDER.getBlue(),
                            35
                    ),

                    0, h,
                    new Color(
                            MAUVE.getRed(),
                            MAUVE.getGreen(),
                            MAUVE.getBlue(),
                            25
                    )
            );

            g2.setPaint(lowerTint);

            g2.fillRoundRect(
                    2, h - 110,
                    w - 4, 108,
                    radius, radius
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    225
            ));

            g2.setStroke(new BasicStroke(2f));

            g2.drawRoundRect(
                    1, 1,
                    w - 3, h - 3,
                    radius, radius
            );

            g2.setColor(new Color(
                    LAVENDER.getRed(),
                    LAVENDER.getGreen(),
                    LAVENDER.getBlue(),
                    95
            ));

            g2.setStroke(new BasicStroke(1f));

            g2.drawRoundRect(
                    4, 4,
                    w - 9, h - 9,
                    radius - 5, radius - 5
            );

            g2.dispose();
        }
    }

    // =====================================
    // WELCOME PANEL WITH GLASS GLOW
    // =====================================

    private class WelcomePanel extends JPanel {

        private JLabel titleLabel;
        private JLabel messageLabel;

        private JButton actionButton;

        public WelcomePanel() {

            setLayout(null);
            setOpaque(false);

            titleLabel = new JLabel(
                    "Hello, Friend!",
                    SwingConstants.CENTER
            );

            titleLabel.setFont(new Font(
                    "Segoe UI", Font.BOLD, 27
            ));

            titleLabel.setForeground(WHITE);

            titleLabel.setBounds(
                    25, 165, 400, 45
            );

            messageLabel = new JLabel(
                    "",
                    SwingConstants.CENTER
            );

            messageLabel.setFont(new Font(
                    "Segoe UI", Font.PLAIN, 12
            ));

            messageLabel.setForeground(WHITE);

            messageLabel.setBounds(
                    25, 215, 400, 50
            );

            actionButton = new GlassButton(
                    "SIGN UP",
                    BLUSH,
                    DEEP_PLUM
            );

            actionButton.setBounds(
                    150, 295, 150, 40
            );

            add(titleLabel);
            add(messageLabel);
            add(actionButton);
        }

        public void setWelcomeContent(
                String title,
                String message,
                String buttonText) {

            titleLabel.setText(title);

            messageLabel.setText(
                    "<html><center>"
                    + message.replace("\n", "<br>")
                    + "</center></html>"
            );

            actionButton.setText(buttonText);
        }

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

            GradientPaint gradient = new GradientPaint(
                    0, 0, PURPLE,
                    w, h, PERIWINKLE
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                    0, 0,
                    w, h,
                    35, 35
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    45
            ));

            g2.fillRoundRect(
                    4, 3,
                    w - 8,
                    85,
                    32, 32
            );

            g2.setColor(new Color(
                    PINK_LAVENDER.getRed(),
                    PINK_LAVENDER.getGreen(),
                    PINK_LAVENDER.getBlue(),
                    45
            ));

            g2.fillOval(
                    w - 125,
                    -65,
                    170,
                    170
            );

            g2.setColor(new Color(
                    MAUVE.getRed(),
                    MAUVE.getGreen(),
                    MAUVE.getBlue(),
                    45
            ));

            g2.fillOval(
                    -75,
                    h - 115,
                    170,
                    170
            );

            g2.setColor(new Color(
                    WHITE.getRed(),
                    WHITE.getGreen(),
                    WHITE.getBlue(),
                    190
            ));

            g2.setStroke(new BasicStroke(2f));

            g2.drawRoundRect(
                    2, 2,
                    w - 5,
                    h - 5,
                    34, 34
            );

            g2.setColor(new Color(
                    BLUSH.getRed(),
                    BLUSH.getGreen(),
                    BLUSH.getBlue(),
                    75
            ));

            g2.setStroke(new BasicStroke(1f));

            g2.drawRoundRect(
                    5, 5,
                    w - 11,
                    h - 11,
                    30, 30
            );

            g2.dispose();
        }
    }
}
