
package com.mycompany.doornal;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

public class LettersPanel extends JPanel {

    private JPanel lettersContainer;
    private JButton addButton;

    private ArrayList<Letter> letters = new ArrayList<>();

    private final String SAVE_FILE = "letters.dat";

    // DOORNAL COLOR PALETTE
    private final Color BLUSH = new Color(252, 225, 221);
    private final Color PEACH = new Color(252, 211, 191);
    private final Color LAVENDER = new Color(210, 197, 240);
    private final Color PERIWINKLE = new Color(192, 180, 239);
    private final Color PURPLE = new Color(142, 138, 217);
    private final Color MAUVE = new Color(193, 137, 175);
    private final Color PLUM = new Color(41, 34, 63);
    private final Color WHITE = new Color(255, 252, 252);

    private final Font TITLE_FONT =
            new Font("Segoe UI", Font.BOLD, 29);

    private final Font BODY_FONT =
            new Font("Segoe UI", Font.PLAIN, 14);

    private final Font SMALL_FONT =
            new Font("Segoe UI", Font.PLAIN, 12);

    // LETTER DATA
    private static class Letter implements Serializable {

        private static final long serialVersionUID = 1L;

        String title;
        String message;

        Letter(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }

    public LettersPanel() {

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
                        0, 0,
                        new Color(255, 247, 247),
                        getWidth(), getHeight(),
                        new Color(237, 231, 250)
                );

                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                paintGlow(
                        g2, 100, 120, 280,
                        new Color(239, 184, 220, 75)
                );

                paintGlow(
                        g2, getWidth() - 100, 350, 300,
                        new Color(178, 164, 239, 70)
                );

                g2.dispose();
            }
        };

        background.setOpaque(false);
        background.setBorder(
                new EmptyBorder(35, 45, 25, 45)
        );

        // MAIN CONTENT
        JPanel mainContent = new JPanel();
        mainContent.setOpaque(false);
        mainContent.setLayout(
                new BoxLayout(mainContent, BoxLayout.Y_AXIS)
        );

        // HEADER
        JLabel eyebrow = new JLabel("YOUR PERSONAL COLLECTION");
        eyebrow.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        eyebrow.setForeground(MAUVE);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Letters");
        title.setFont(TITLE_FONT);
        title.setForeground(PLUM);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Keep your thoughts, memories, and heartfelt messages."
        );
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(new Color(130, 117, 148));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainContent.add(eyebrow);
        mainContent.add(Box.createVerticalStrut(5));
        mainContent.add(title);
        mainContent.add(Box.createVerticalStrut(5));
        mainContent.add(subtitle);
        mainContent.add(Box.createVerticalStrut(22));

        // INTRO GLASS CARD
        GlassCard introCard = new GlassCard(24);
        introCard.setLayout(new BorderLayout(15, 0));
        introCard.setBorder(
                new EmptyBorder(19, 23, 19, 23)
        );
        introCard.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105)
        );
        introCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel introText = new JPanel();
        introText.setOpaque(false);
        introText.setLayout(
                new BoxLayout(introText, BoxLayout.Y_AXIS)
        );

        JLabel introTitle = new JLabel("A little space for your words");
        introTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );
        introTitle.setForeground(PLUM);

        JLabel introDescription = new JLabel(
                "Write something meaningful and keep it here."
        );
        introDescription.setFont(SMALL_FONT);
        introDescription.setForeground(
                new Color(139, 125, 155)
        );

        introText.add(introTitle);
        introText.add(Box.createVerticalStrut(5));
        introText.add(introDescription);

        JLabel decorativeIcon = new JLabel("✉");
        decorativeIcon.setFont(
                new Font("Segoe UI Symbol", Font.PLAIN, 36)
        );
        decorativeIcon.setForeground(MAUVE);

        introCard.add(introText, BorderLayout.CENTER);
        introCard.add(decorativeIcon, BorderLayout.EAST);

        mainContent.add(introCard);
        mainContent.add(Box.createVerticalStrut(25));

        // LETTERS SECTION HEADER
        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setOpaque(false);
        sectionHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionHeader.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 25)
        );

        JLabel sectionTitle = new JLabel("YOUR LETTERS");
        sectionTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        sectionTitle.setForeground(
                new Color(151, 129, 173)
        );

        sectionHeader.add(sectionTitle, BorderLayout.WEST);

        mainContent.add(sectionHeader);
        mainContent.add(Box.createVerticalStrut(12));

        // LETTERS CONTAINER
        lettersContainer = new JPanel();
        lettersContainer.setOpaque(false);
        lettersContainer.setLayout(
                new BoxLayout(lettersContainer, BoxLayout.Y_AXIS)
        );

        mainContent.add(lettersContainer);

        // SCROLL PANE
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        background.add(scrollPane, BorderLayout.CENTER);

        // BOTTOM ADD BUTTON
        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 8)
        );
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(
                new EmptyBorder(5, 45, 20, 45)
        );

        addButton = new GradientButton(
                "＋   Create New Letter",
                PURPLE,
                MAUVE
        );

        addButton.setPreferredSize(
                new Dimension(210, 48)
        );

        addButton.addActionListener(e -> openEditor(null));

        bottomPanel.add(addButton);

        background.add(bottomPanel, BorderLayout.SOUTH);

        add(background, BorderLayout.CENTER);

        loadLetters();
        refreshLetters();
    }

    // =========================================
    // BACKGROUND GLOW
    // =========================================

    private void paintGlow(
            Graphics2D g2,
            int x,
            int y,
            int radius,
            Color color) {

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

    // =========================================
    // SAVE LETTERS
    // =========================================

    private void saveLetters() {

        try (ObjectOutputStream output =
                     new ObjectOutputStream(
                             new FileOutputStream(SAVE_FILE))) {

            output.writeObject(letters);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save letters: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================
    // LOAD LETTERS
    // =========================================

    @SuppressWarnings("unchecked")
    private void loadLetters() {

        File file = new File(SAVE_FILE);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             new FileInputStream(file))) {

            letters = (ArrayList<Letter>) input.readObject();

        } catch (IOException | ClassNotFoundException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not load saved letters: " + e.getMessage(),
                    "Loading Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================
    // REFRESH LETTERS
    // =========================================

    private void refreshLetters() {

        lettersContainer.removeAll();

        if (letters.isEmpty()) {

            GlassCard emptyCard = new GlassCard(23);
            emptyCard.setLayout(
                    new BoxLayout(emptyCard, BoxLayout.Y_AXIS)
            );
            emptyCard.setBorder(
                    new EmptyBorder(30, 20, 30, 20)
            );
            emptyCard.setAlignmentX(Component.LEFT_ALIGNMENT);
            emptyCard.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 145)
            );

            JLabel emptyIcon = new JLabel("✧");
            emptyIcon.setFont(
                    new Font("Segoe UI Symbol", Font.PLAIN, 30)
            );
            emptyIcon.setForeground(MAUVE);
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptyTitle = new JLabel("No letters yet");
            emptyTitle.setFont(
                    new Font("Segoe UI", Font.BOLD, 16)
            );
            emptyTitle.setForeground(PLUM);
            emptyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel emptyText = new JLabel(
                    "Your letters will appear here once you create one."
            );
            emptyText.setFont(SMALL_FONT);
            emptyText.setForeground(
                    new Color(145, 133, 157)
            );
            emptyText.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyCard.add(emptyIcon);
            emptyCard.add(Box.createVerticalStrut(7));
            emptyCard.add(emptyTitle);
            emptyCard.add(Box.createVerticalStrut(5));
            emptyCard.add(emptyText);

            lettersContainer.add(emptyCard);

        } else {

            for (Letter letter : letters) {
                addLetterNotification(letter);
                lettersContainer.add(Box.createVerticalStrut(13));
            }
        }

        lettersContainer.revalidate();
        lettersContainer.repaint();
    }

    // =========================================
    // LETTER CARD
    // =========================================

    private void addLetterNotification(Letter letter) {

        GlassCard card = new GlassCard(22);
        card.setLayout(new BorderLayout(15, 5));
        card.setBorder(
                new EmptyBorder(17, 20, 17, 20)
        );

        card.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 105)
        );

        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        // LETTER ICON
        JLabel icon = new JLabel("✉");
        icon.setFont(
                new Font("Segoe UI Symbol", Font.PLAIN, 27)
        );
        icon.setForeground(MAUVE);
        icon.setPreferredSize(new Dimension(38, 40));

        // LETTER DETAILS
        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(
                new BoxLayout(details, BoxLayout.Y_AXIS)
        );

        JLabel titleLabel = new JLabel(letter.title);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );
        titleLabel.setForeground(PLUM);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel previewLabel = new JLabel(
                truncateText(letter.message, 85)
        );
        previewLabel.setFont(SMALL_FONT);
        previewLabel.setForeground(
                new Color(139, 127, 151)
        );
        previewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        details.add(titleLabel);
        details.add(Box.createVerticalStrut(7));
        details.add(previewLabel);

        JLabel arrow = new JLabel("›");
        arrow.setFont(
                new Font("Segoe UI", Font.PLAIN, 28)
        );
        arrow.setForeground(
                new Color(164, 145, 184)
        );

        card.add(icon, BorderLayout.WEST);
        card.add(details, BorderLayout.CENTER);
        card.add(arrow, BorderLayout.EAST);

        MouseAdapter openListener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                viewLetter(letter);
            }
        };

        card.addMouseListener(openListener);
        icon.addMouseListener(openListener);
        details.addMouseListener(openListener);
        titleLabel.addMouseListener(openListener);
        previewLabel.addMouseListener(openListener);
        arrow.addMouseListener(openListener);

        lettersContainer.add(card);
    }

    // =========================================
    // TRUNCATE PREVIEW
    // =========================================

    private String truncateText(String text, int maxLength) {

        if (text == null) {
            return "";
        }

        text = text.replace("\n", " ");

        if (text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }

        return text;
    }

    // =========================================
    // VIEW LETTER
    // =========================================

    private void viewLetter(Letter letter) {

        JDialog dialog = createDialog(
                letter.title,
                750,
                560
        );

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setOpaque(false);
        content.setBorder(
                new EmptyBorder(28, 30, 20, 30)
        );

        JLabel eyebrow = new JLabel("A MESSAGE TO REMEMBER");
        eyebrow.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        eyebrow.setForeground(MAUVE);

        JLabel titleLabel = new JLabel(letter.title);
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );
        titleLabel.setForeground(PLUM);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.add(eyebrow);
        header.add(Box.createVerticalStrut(6));
        header.add(titleLabel);

        GlassCard paperCard = new GlassCard(20);
        paperCard.setLayout(new BorderLayout());
        paperCard.setBorder(
                new EmptyBorder(15, 18, 15, 18)
        );

        JTextArea messageArea = new JTextArea(letter.message);
        messageArea.setFont(
                new Font("Segoe UI", Font.PLAIN, 16)
        );
        messageArea.setForeground(
                new Color(76, 65, 91)
        );
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setBorder(null);
        messageArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(null);
        messageScroll.setOpaque(false);
        messageScroll.getViewport().setOpaque(false);
        messageScroll.getVerticalScrollBar().setUnitIncrement(15);

        paperCard.add(messageScroll, BorderLayout.CENTER);

        JButton editButton = new GradientButton(
                "✎   Edit Letter",
                PURPLE,
                MAUVE
        );
        editButton.setPreferredSize(
                new Dimension(155, 43)
        );

        editButton.addActionListener(e -> {
            dialog.dispose();
            openEditor(letter);
        });

        JButton closeButton = new SoftButton("Close");
        closeButton.setPreferredSize(
                new Dimension(105, 43)
        );

        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        buttons.setOpaque(false);
        buttons.add(closeButton);
        buttons.add(editButton);

        content.add(header, BorderLayout.NORTH);
        content.add(paperCard, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // =========================================
    // CREATE / EDIT LETTER
    // =========================================

    private void openEditor(Letter existingLetter) {

        boolean editing = existingLetter != null;

        JDialog dialog = createDialog(
                editing ? "Edit Letter" : "Create New Letter",
                750,
                610
        );

        JPanel content = new JPanel(new BorderLayout(15, 15));
        content.setOpaque(false);
        content.setBorder(
                new EmptyBorder(27, 30, 22, 30)
        );

        // HEADER
        JLabel eyebrow = new JLabel(
                editing ? "MAKE IT JUST RIGHT" : "PUT YOUR THOUGHTS INTO WORDS"
        );
        eyebrow.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        eyebrow.setForeground(MAUVE);

        JLabel heading = new JLabel(
                editing ? "Edit your letter" : "Create a new letter"
        );
        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 25)
        );
        heading.setForeground(PLUM);

        JLabel subtitle = new JLabel(
                "Take your time. Every word matters."
        );
        subtitle.setFont(SMALL_FONT);
        subtitle.setForeground(
                new Color(140, 127, 154)
        );

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(
                new BoxLayout(header, BoxLayout.Y_AXIS)
        );

        header.add(eyebrow);
        header.add(Box.createVerticalStrut(5));
        header.add(heading);
        header.add(Box.createVerticalStrut(4));
        header.add(subtitle);

        // TITLE INPUT
        JLabel titleLabel = new JLabel("LETTER TITLE");
        titleLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        titleLabel.setForeground(
                new Color(142, 124, 164)
        );

        JTextField titleField = new JTextField();
        styleTextField(titleField);
        titleField.setPreferredSize(
                new Dimension(500, 47)
        );

        // MESSAGE INPUT
        JLabel messageLabel = new JLabel("YOUR MESSAGE");
        messageLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 10)
        );
        messageLabel.setForeground(
                new Color(142, 124, 164)
        );

        JTextArea messageArea = new JTextArea();
        messageArea.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );
        messageArea.setForeground(
                new Color(76, 65, 91)
        );
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(
                new EmptyBorder(14, 15, 14, 15)
        );
        messageArea.setBackground(
                new Color(255, 255, 255, 235)
        );
        messageArea.setCaretColor(PURPLE);

        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(214, 199, 230),
                        1
                )
        );
        messageScroll.getViewport().setBackground(WHITE);
        messageScroll.getVerticalScrollBar().setUnitIncrement(15);

        // LOAD EXISTING CONTENT
        if (editing) {
            titleField.setText(existingLetter.title);
            messageArea.setText(existingLetter.message);
        }

        JPanel titlePanel = new JPanel(new BorderLayout(0, 8));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(titleField, BorderLayout.CENTER);

        JPanel messagePanel = new JPanel(new BorderLayout(0, 8));
        messagePanel.setOpaque(false);
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(messageScroll, BorderLayout.CENTER);

        JPanel fields = new JPanel(new BorderLayout(0, 17));
        fields.setOpaque(false);
        fields.add(titlePanel, BorderLayout.NORTH);
        fields.add(messagePanel, BorderLayout.CENTER);

        // FOOTER BUTTONS
        JButton cancelButton = new SoftButton("Cancel");
        cancelButton.setPreferredSize(
                new Dimension(110, 43)
        );
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton saveButton = new GradientButton(
                editing ? "Save Changes" : "Save Letter",
                PURPLE,
                MAUVE
        );
        saveButton.setPreferredSize(
                new Dimension(155, 43)
        );

        saveButton.addActionListener(e -> {

            String newTitle = titleField.getText().trim();
            String newMessage = messageArea.getText().trim();

            if (newTitle.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a letter title.",
                        "Missing Title",
                        JOptionPane.WARNING_MESSAGE
                );
                titleField.requestFocus();
                return;
            }

            if (newMessage.isEmpty()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a letter message.",
                        "Missing Message",
                        JOptionPane.WARNING_MESSAGE
                );
                messageArea.requestFocus();
                return;
            }

            if (editing) {
                existingLetter.title = newTitle;
                existingLetter.message = newMessage;
            } else {
                letters.add(new Letter(newTitle, newMessage));
            }

            saveLetters();
            refreshLetters();

            dialog.dispose();
        });

        JPanel footer = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );
        footer.setOpaque(false);
        footer.add(cancelButton);
        footer.add(saveButton);

        content.add(header, BorderLayout.NORTH);
        content.add(fields, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);

        dialog.add(content, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // =========================================
    // DIALOG DESIGN
    // =========================================

    private JDialog createDialog(
            String title,
            int width,
            int height) {

        Window owner = SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(
                owner,
                title,
                Dialog.ModalityType.APPLICATION_MODAL
        );

        dialog.setSize(width, height);
        dialog.setMinimumSize(new Dimension(600, 480));
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel background = new JPanel(new BorderLayout()) {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setPaint(new GradientPaint(
                        0, 0,
                        new Color(255, 247, 247),
                        getWidth(), getHeight(),
                        new Color(237, 231, 250)
                ));

                g2.fillRect(0, 0, getWidth(), getHeight());

                paintGlow(
                        g2, getWidth() - 50, 100, 220,
                        new Color(193, 137, 175, 65)
                );

                g2.dispose();
            }
        };

        background.setOpaque(false);
        dialog.setContentPane(background);

        return dialog;
    }

    // =========================================
    // TEXT FIELD DESIGN
    // =========================================

    private void styleTextField(JTextField field) {

        field.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        field.setForeground(PLUM);
        field.setCaretColor(PURPLE);
        field.setBackground(WHITE);
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(214, 199, 230),
                                1
                        ),
                        new EmptyBorder(10, 13, 10, 13)
                )
        );
    }

    // =========================================
    // GLASS CARD
    // =========================================

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
            g2.setColor(new Color(91, 73, 125, 23));
            g2.fillRoundRect(
                    3, 6, w - 7, h - 8,
                    arc, arc
            );

            // Glass body
            g2.setPaint(new GradientPaint(
                    0, 0,
                    new Color(255, 255, 255, 235),
                    0, h,
                    new Color(255, 248, 255, 190)
            ));

            g2.fillRoundRect(
                    0, 0, w - 5, h - 6,
                    arc, arc
            );

            // Glossy highlight
            g2.setPaint(new GradientPaint(
                    0, 0,
                    new Color(255, 255, 255, 135),
                    0, h / 2,
                    new Color(255, 255, 255, 0)
            ));

            g2.fillRoundRect(
                    2, 2, w - 9, h / 2,
                    arc - 2, arc - 2
            );

            // Glass border
            g2.setColor(new Color(255, 255, 255, 240));
            g2.setStroke(new BasicStroke(1.3f));
            g2.drawRoundRect(
                    1, 1, w - 8, h - 8,
                    arc, arc
            );

            g2.setColor(new Color(193, 170, 221, 40));
            g2.drawRoundRect(
                    3, 3, w - 12, h - 12,
                    arc - 4, arc - 4
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================
    // GRADIENT BUTTON
    // =========================================

    private class GradientButton extends JButton {

        private final Color first;
        private final Color second;

        private boolean hovered;
        private boolean pressed;

        GradientButton(
                String text,
                Color first,
                Color second) {

            super(text);

            this.first = first;
            this.second = second;

            setFont(
                    new Font("Segoe UI", Font.BOLD, 13)
            );

            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

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

            int offset = pressed ? 2 : 0;

            // Shadow
            g2.setColor(new Color(76, 55, 111, 45));
            g2.fillRoundRect(
                    2, 5, w - 5, h - 7,
                    18, 18
            );

            // Gradient
            Color c1 = hovered ? first.brighter() : first;
            Color c2 = hovered ? second.brighter() : second;

            g2.setPaint(new GradientPaint(
                    0, 0, c1,
                    w, h, c2
            ));

            g2.fillRoundRect(
                    0, offset, w - 4, h - 7,
                    17, 17
            );

            // Highlight
            g2.setPaint(new GradientPaint(
                    0, offset,
                    new Color(255, 255, 255, 105),
                    0, h / 2,
                    new Color(255, 255, 255, 0)
            ));

            g2.fillRoundRect(
                    2, offset + 2, w - 8, h / 2,
                    15, 15
            );

            // Border
            g2.setColor(new Color(255, 255, 255, 110));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(
                    1, offset + 1, w - 6, h - 9,
                    16, 16
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================
    // SOFT SECONDARY BUTTON
    // =========================================

    private class SoftButton extends JButton {

        private boolean hovered;

        SoftButton(String text) {

            super(text);

            setFont(
                    new Font("Segoe UI", Font.BOLD, 13)
            );

            setForeground(PLUM);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

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

            g2.setColor(
                    hovered
                            ? new Color(225, 214, 242, 245)
                            : new Color(255, 255, 255, 210)
            );

            g2.fillRoundRect(
                    0, 0, getWidth() - 3, getHeight() - 5,
                    15, 15
            );

            g2.setColor(new Color(255, 255, 255, 230));
            g2.drawRoundRect(
                    1, 1, getWidth() - 5, getHeight() - 7,
                    14, 14
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
