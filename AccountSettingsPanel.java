
package com.mycompany.doornal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;

public class AccountSettingsPanel extends JPanel {

    private JLabel usernameLabel;
    private JLabel displayNameLabel;
    private JLabel joinDateLabel;
    private JLabel journalCountLabel;

    private final Color BLUSH = new Color(252, 225, 221);
    private final Color PEACH = new Color(252, 211, 191);
    private final Color LAVENDER = new Color(210, 197, 240);
    private final Color PERIWINKLE = new Color(192, 180, 239);
    private final Color PURPLE = new Color(142, 138, 217);
    private final Color MAUVE = new Color(193, 137, 175);
    private final Color PLUM = new Color(41, 34, 63);
    private final Color WHITE = new Color(255, 252, 252);

    private final Font headingFont =
            new Font("Segoe UI", Font.BOLD, 27);

    private final Font bodyFont =
            new Font("Segoe UI", Font.PLAIN, 14);

    private final Font smallFont =
            new Font("Segoe UI", Font.PLAIN, 12);

    public AccountSettingsPanel() {

        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel background = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 247, 247),
                        getWidth(), getHeight(),
                        new Color(237, 231, 250)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                paintGlow(g2, 100, 100, 260,
                        new Color(239, 184, 220, 90));

                paintGlow(g2, getWidth() - 100, 300, 300,
                        new Color(178, 164, 239, 75));

                paintGlow(g2, getWidth() / 2, getHeight() - 40, 250,
                        new Color(255, 207, 184, 55));

                g2.dispose();
            }
        };

        background.setOpaque(false);
        background.setBorder(
                new EmptyBorder(35, 45, 35, 45)
        );

        JPanel main = new JPanel();
        main.setOpaque(false);
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        // HEADER
        JLabel eyebrow = new JLabel("YOUR PERSONAL SPACE");
        eyebrow.setFont(new Font("Segoe UI", Font.BOLD, 10));
        eyebrow.setForeground(MAUVE);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Account Settings");
        title.setFont(headingFont);
        title.setForeground(PLUM);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Manage your profile and account preferences."
        );
        subtitle.setFont(bodyFont);
        subtitle.setForeground(new Color(125, 115, 143));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(eyebrow);
        main.add(Box.createVerticalStrut(5));
        main.add(title);
        main.add(Box.createVerticalStrut(5));
        main.add(subtitle);
        main.add(Box.createVerticalStrut(27));

        // PROFILE CARD
        GlassCard profileCard = new GlassCard(25);
        profileCard.setLayout(new BorderLayout(20, 0));
        profileCard.setBorder(
                new EmptyBorder(23, 25, 23, 25)
        );
        profileCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 145)
        );
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel avatar = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                GradientPaint gp = new GradientPaint(
                        0, 0, LAVENDER,
                        getWidth(), getHeight(), MAUVE
                );

                g2.setPaint(gp);
                g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);

                g2.setColor(new Color(255, 255, 255, 95));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(3, 3, getWidth() - 7, getHeight() - 7);

                g2.dispose();
            }
        };

        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(82, 82));

        JLabel avatarLetter = new JLabel(
                getInitial()
        );
        avatarLetter.setFont(
                new Font("Segoe UI", Font.BOLD, 32)
        );
        avatarLetter.setForeground(WHITE);
        avatar.add(avatarLetter);

        JPanel profileInfo = new JPanel();
        profileInfo.setOpaque(false);
        profileInfo.setLayout(
                new BoxLayout(profileInfo, BoxLayout.Y_AXIS)
        );

        JLabel profileCaption = new JLabel("YOUR PROFILE");
        profileCaption.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        profileCaption.setForeground(MAUVE);

        displayNameLabel = new JLabel(
                safeText(CurrentUser.user.getDisplayName())
        );
        displayNameLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 20)
        );
        displayNameLabel.setForeground(PLUM);

        usernameLabel = new JLabel(
                "@" + safeText(CurrentUser.user.getUsername())
        );
        usernameLabel.setFont(bodyFont);
        usernameLabel.setForeground(new Color(133, 120, 151));

        joinDateLabel = new JLabel(
                "Member since " +
                safeText(String.valueOf(CurrentUser.user.getJoinDate()))
        );
        joinDateLabel.setFont(smallFont);
        joinDateLabel.setForeground(new Color(160, 147, 169));

        profileInfo.add(profileCaption);
        profileInfo.add(Box.createVerticalStrut(5));
        profileInfo.add(displayNameLabel);
        profileInfo.add(Box.createVerticalStrut(3));
        profileInfo.add(usernameLabel);
        profileInfo.add(Box.createVerticalStrut(7));
        profileInfo.add(joinDateLabel);

        profileCard.add(avatar, BorderLayout.WEST);
        profileCard.add(profileInfo, BorderLayout.CENTER);

        main.add(profileCard);
        main.add(Box.createVerticalStrut(20));

        // JOURNAL STATISTICS
        JLabel overviewTitle = sectionTitle("ACCOUNT OVERVIEW");
        overviewTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(overviewTitle);
        main.add(Box.createVerticalStrut(10));

        GlassCard statsCard = new GlassCard(22);
        statsCard.setLayout(new BorderLayout());
        statsCard.setBorder(
                new EmptyBorder(19, 23, 19, 23)
        );
        statsCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105)
        );
        statsCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsInfo = new JPanel();
        statsInfo.setOpaque(false);
        statsInfo.setLayout(
                new BoxLayout(statsInfo, BoxLayout.Y_AXIS)
        );

        JLabel statsCaption = new JLabel("TOTAL JOURNAL ENTRIES");
        statsCaption.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        statsCaption.setForeground(new Color(146, 128, 171));

        journalCountLabel = new JLabel(
                String.valueOf(countJournalEntries())
        );
        journalCountLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 29)
        );
        journalCountLabel.setForeground(PURPLE);

        statsInfo.add(statsCaption);
        statsInfo.add(Box.createVerticalStrut(3));
        statsInfo.add(journalCountLabel);

        JLabel journalIcon = new JLabel("✦");
        journalIcon.setFont(
                new Font("Segoe UI Symbol", Font.PLAIN, 34)
        );
        journalIcon.setForeground(MAUVE);

        statsCard.add(statsInfo, BorderLayout.CENTER);
        statsCard.add(journalIcon, BorderLayout.EAST);

        main.add(statsCard);
        main.add(Box.createVerticalStrut(24));

        // SETTINGS SECTION
        JLabel settingsTitle = sectionTitle("PROFILE MANAGEMENT");
        settingsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(settingsTitle);
        main.add(Box.createVerticalStrut(10));

        addSettingButton(main, "✧", "Change Display Name",
                "Update the name shown on your profile.",
                this::changeDisplayName);

        addSettingButton(main, "◎", "Change Username",
                "Choose a new username for your account.",
                this::changeUsername);

        addSettingButton(main, "◇", "Change Password",
                "Keep your account access up to date.",
                this::changePassword);

        main.add(Box.createVerticalStrut(18));

        JLabel accountTitle = sectionTitle("ACCOUNT ACTIONS");
        accountTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        main.add(accountTitle);
        main.add(Box.createVerticalStrut(10));

        GlassActionButton logoutButton =
                new GlassActionButton(
                        "↪", "Logout",
                        "Sign out of your Doornal account.",
                        MAUVE, false
                );

        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutButton.addActionListener(e -> logout());

        GlassActionButton deleteButton =
                new GlassActionButton(
                        "×", "Delete Account",
                        "Permanently remove your account.",
                        new Color(205, 105, 126), true
                );

        deleteButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        deleteButton.addActionListener(e -> deleteAccount());

        main.add(logoutButton);
        main.add(Box.createVerticalStrut(10));
        main.add(deleteButton);

        background.add(main, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(background);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.getVerticalScrollBar().setUnitIncrement(18);

        add(scroll, BorderLayout.CENTER);
    }

    private void paintGlow(Graphics2D g2,
                           int x, int y, int radius, Color color) {

        for (int i = 8; i >= 1; i--) {
            int size = radius * i / 4;
            int alpha = color.getAlpha() / (i + 2);

            g2.setColor(new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    Math.max(1, alpha)
            ));

            g2.fillOval(
                    x - size / 2,
                    y - size / 2,
                    size,
                    size
            );
        }
    }

    private String getInitial() {
        String name = CurrentUser.user.getDisplayName();

        if (name == null || name.trim().isEmpty()) {
            name = CurrentUser.user.getUsername();
        }

        if (name == null || name.trim().isEmpty()) {
            return "D";
        }

        return name.substring(0, 1).toUpperCase();
    }

    private String safeText(String value) {
        return value == null || value.trim().isEmpty()
                ? "Not set"
                : value;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        label.setForeground(new Color(151, 129, 173));
        return label;
    }

    private void addSettingButton(
            JPanel parent,
            String icon,
            String title,
            String description,
            Runnable action) {

        GlassActionButton button =
                new GlassActionButton(
                        icon, title, description,
                        PURPLE, false
                );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(e -> action.run());

        parent.add(button);
        parent.add(Box.createVerticalStrut(9));
    }

    // GLASS CARD
    private class GlassCard extends JPanel {

        private final int arc;

        GlassCard(int arc) {
            this.arc = arc;
            setOpaque(false);
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

            // Soft shadow
            g2.setColor(new Color(91, 73, 125, 24));
            g2.fillRoundRect(3, 6, w - 6, h - 6, arc, arc);

            // Glass base
            GradientPaint glass = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 225),
                    0, h, new Color(255, 248, 255, 175)
            );

            g2.setPaint(glass);
            g2.fillRoundRect(0, 0, w - 4, h - 5, arc, arc);

            // Glossy upper highlight
            g2.setPaint(new GradientPaint(
                    0, 0, new Color(255, 255, 255, 125),
                    0, h / 2, new Color(255, 255, 255, 0)
            ));

            g2.fillRoundRect(
                    2, 2, w - 8, h / 2, arc, arc
            );

            // Border
            g2.setColor(new Color(255, 255, 255, 235));
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawRoundRect(1, 1, w - 7, h - 7, arc, arc);

            g2.setColor(new Color(193, 170, 221, 45));
            g2.drawRoundRect(3, 3, w - 11, h - 11, arc - 4, arc - 4);

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // GLASS BUTTON
    private class GlassActionButton extends JButton {

        private boolean hovered = false;
        private boolean pressed = false;

        private final Color accent;
        private final boolean danger;

        GlassActionButton(
                String icon,
                String title,
                String description,
                Color accent,
                boolean danger) {

            this.accent = accent;
            this.danger = danger;

            setLayout(new BorderLayout(15, 0));
            setBorder(new EmptyBorder(13, 17, 13, 17));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(400, 75));
            setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 75)
            );

            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(
                    new Font("Segoe UI Symbol", Font.PLAIN, 25)
            );
            iconLabel.setForeground(accent);
            iconLabel.setPreferredSize(new Dimension(30, 35));

            JPanel textPanel = new JPanel();
            textPanel.setOpaque(false);
            textPanel.setLayout(
                    new BoxLayout(textPanel, BoxLayout.Y_AXIS)
            );

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(
                    new Font("Segoe UI", Font.BOLD, 14)
            );
            titleLabel.setForeground(
                    danger ? new Color(174, 79, 107) : PLUM
            );

            JLabel descriptionLabel = new JLabel(description);
            descriptionLabel.setFont(smallFont);
            descriptionLabel.setForeground(
                    new Color(145, 133, 157)
            );

            textPanel.add(titleLabel);
            textPanel.add(Box.createVerticalStrut(4));
            textPanel.add(descriptionLabel);

            JLabel arrow = new JLabel("›");
            arrow.setFont(
                    new Font("Segoe UI", Font.PLAIN, 27)
            );
            arrow.setForeground(
                    new Color(165, 147, 183)
            );

            add(iconLabel, BorderLayout.WEST);
            add(textPanel, BorderLayout.CENTER);
            add(arrow, BorderLayout.EAST);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
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

            // Outer glow on hover
            if (hovered) {
                g2.setColor(new Color(
                        accent.getRed(),
                        accent.getGreen(),
                        accent.getBlue(),
                        45
                ));

                g2.fillRoundRect(
                        0, 0, w - 2, h - 2, 22, 22
                );
            }

            int offset = pressed ? 2 : 0;

            // Shadow
            g2.setColor(new Color(75, 57, 103, 25));
            g2.fillRoundRect(
                    2, 5 + offset, w - 7, h - 8,
                    20, 20
            );

            // Button glass gradient
            GradientPaint gradient = new GradientPaint(
                    0, 0,
                    hovered
                            ? new Color(255, 255, 255, 245)
                            : new Color(255, 255, 255, 225),
                    0, h,
                    hovered
                            ? new Color(246, 238, 255, 235)
                            : new Color(250, 245, 252, 190)
            );

            g2.setPaint(gradient);
            g2.fillRoundRect(
                    0, offset, w - 5, h - 6,
                    20, 20
            );

            // Soft accent line
            g2.setColor(new Color(
                    accent.getRed(),
                    accent.getGreen(),
                    accent.getBlue(),
                    hovered ? 125 : 65
            ));

            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(
                    1, 1 + offset, w - 8, h - 8,
                    19, 19
            );

            // Glossy highlight
            g2.setPaint(new GradientPaint(
                    0, 0,
                    new Color(255, 255, 255, 135),
                    0, h / 2,
                    new Color(255, 255, 255, 0)
            ));

            g2.fillRoundRect(
                    3, 2 + offset, w - 12, h / 2,
                    17, 17
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // EXISTING ACCOUNT FUNCTIONS

    private void changeDisplayName() {

        String displayName = JOptionPane.showInputDialog(
                this,
                "New Display Name:",
                "Update Display Name",
                JOptionPane.PLAIN_MESSAGE
        );

        if (displayName == null ||
                displayName.isBlank()) {
            return;
        }

        CurrentUser.user.setDisplayName(displayName.trim());

        displayNameLabel.setText(displayName.trim());

        saveCurrentUsers();
    }

    private void changeUsername() {

        String username = JOptionPane.showInputDialog(
                this,
                "New Username:",
                "Update Username",
                JOptionPane.PLAIN_MESSAGE
        );

        if (username == null || username.isBlank()) {
            return;
        }

        String newUsername = username.trim();

        ArrayList<User> users = AccountPanel.getUsers();

        for (User user : users) {
            if (user != CurrentUser.user &&
                    user.getUsername().equalsIgnoreCase(newUsername)) {

                JOptionPane.showMessageDialog(
                        this,
                        "That username is already taken.",
                        "Username Unavailable",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        CurrentUser.user.setUsername(newUsername);
        CurrentUser.username = newUsername;

        usernameLabel.setText("@" + newUsername);

        saveCurrentUsers();
    }

    private void changePassword() {

        JPasswordField passwordField = new JPasswordField();

        int result = JOptionPane.showConfirmDialog(
                this,
                passwordField,
                "Enter New Password",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(
                passwordField.getPassword()
        );

        if (password.isBlank()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Password cannot be empty."
            );
            return;
        }

        CurrentUser.user.setPassword(password);

        saveCurrentUsers();

        JOptionPane.showMessageDialog(
                this,
                "Your password has been updated.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logout() {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        CurrentUser.logout();

        Window window =
                SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispose();
        }

        new LoginFrame();
    }

    private void deleteAccount() {

        int result = JOptionPane.showConfirmDialog(
                this,
                "Delete this account permanently?",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        ArrayList<User> users = AccountPanel.getUsers();

        users.removeIf(user ->
                user.getUsername().equals(
                        CurrentUser.user.getUsername()
                )
        );

        AccountPanel.saveUsers(users);

        CurrentUser.logout();

        Window window =
                SwingUtilities.getWindowAncestor(this);

        if (window != null) {
            window.dispose();
        }

        new LoginFrame();
    }

    private int countJournalEntries() {

        File folder = new File(
                System.getProperty("user.home")
                        + File.separator
                        + "Doornal"
                        + File.separator
                        + CurrentUser.username
                        + File.separator
                        + "Journals"
        );

        if (!folder.exists()) {
            return 0;
        }

        File[] files = folder.listFiles();

        return files == null ? 0 : files.length;
    }

    private void saveCurrentUsers() {

        ArrayList<User> users = AccountPanel.getUsers();

        AccountPanel.saveUsers(users);
    }
}
