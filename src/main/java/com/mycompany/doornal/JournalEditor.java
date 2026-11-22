
package com.mycompany.doornal;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JournalEditor extends JFrame {

    private JTextArea journalArea;
    private JLabel statusLabel;
    private JLabel selectedEmotionLabel;
    private JButton selectEmotionButton;

    private LocalDate journalDate;
    private Path journalFile;
    private Path emotionFile;

    private String selectedEmotion = "";

    private GradientPanel background;
    private GlassPanel headerCard;
    private GlassPanel journalCard;
    private GlassPanel emotionCard;
    private GlassPanel footerCard;

    private final String[] emotions = {
        "Happy", "Sad", "Tired",
        "Envy", "Fear", "Angry"
    };

    private final String[] emotionImages = {
        "happy.jpg", "sad.jpg", "tired.jpg",
        "envy.jpg", "fear.jpg", "angry.jpg"
    };

    private Color bgStart = new Color(252, 225, 221);
    private Color bgEnd = new Color(192, 180, 239);

    private Color accent = new Color(142, 138, 217);
    private Color accent2 = new Color(193, 137, 175);

    private Color glassTop = new Color(255, 255, 255, 175);
    private Color glassBottom = new Color(255, 255, 255, 85);
    private Color glassBorder = new Color(255, 255, 255, 225);

    private final Color DEEP_PLUM =
            new Color(41, 34, 63);

    public JournalEditor(LocalDate date) {

        journalDate = date;

        setTitle("Doornal - Journal");
        setSize(850, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Path journalFolder = Path.of(
                System.getProperty("user.home"),
                "Doornal",
                CurrentUser.username,
                "Journals"
        );

        journalFile = journalFolder.resolve(date + ".txt");
        emotionFile = journalFolder.resolve(date + ".emotion");

        // MAIN BACKGROUND

        background = new GradientPanel();
        background.setLayout(new BorderLayout(15, 15));

        background.setBorder(
                BorderFactory.createEmptyBorder(
                        22, 25, 22, 25
                )
        );

        setContentPane(background);

        // HEADER

        headerCard = new GlassPanel(new BorderLayout());

        headerCard.setBorder(
                BorderFactory.createEmptyBorder(
                        17, 22, 17, 22
                )
        );

        JLabel dateLabel = new JLabel(
                date.format(
                        DateTimeFormatter.ofPattern(
                                "MMMM dd, yyyy"
                        )
                )
        );

        dateLabel.setFont(
                new Font("Arial", Font.BOLD, 25)
        );

        dateLabel.setForeground(DEEP_PLUM);

        JLabel subtitle = new JLabel(
                "Your personal space"
        );

        subtitle.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        subtitle.setForeground(DEEP_PLUM);

        JPanel headerText = new JPanel();

        headerText.setOpaque(false);

        headerText.setLayout(
                new BoxLayout(
                        headerText,
                        BoxLayout.Y_AXIS
                )
        );

        headerText.add(dateLabel);
        headerText.add(Box.createVerticalStrut(4));
        headerText.add(subtitle);

        headerCard.add(
                headerText,
                BorderLayout.WEST
        );

        background.add(
                headerCard,
                BorderLayout.NORTH
        );

        // CENTER CONTENT

        JPanel content = new JPanel(
                new BorderLayout(15, 0)
        );

        content.setOpaque(false);

        // JOURNAL CARD

        journalCard = new GlassPanel(
                new BorderLayout(10, 12)
        );

        journalCard.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 20, 20, 20
                )
        );

        JLabel journalTitle = new JLabel("My Journal");

        journalTitle.setFont(
                new Font("Arial", Font.BOLD, 19)
        );

        journalTitle.setForeground(DEEP_PLUM);

        JLabel journalHint = new JLabel(
                "Write down your thoughts for today..."
        );

        journalHint.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        journalHint.setForeground(
                new Color(91, 76, 112)
        );

        JPanel writingHeader = new JPanel();

        writingHeader.setOpaque(false);

        writingHeader.setLayout(
                new BoxLayout(
                        writingHeader,
                        BoxLayout.Y_AXIS
                )
        );

        writingHeader.add(journalTitle);
        writingHeader.add(Box.createVerticalStrut(5));
        writingHeader.add(journalHint);

        journalCard.add(
                writingHeader,
                BorderLayout.NORTH
        );

        journalArea = new JTextArea();

        journalArea.setFont(
                new Font("Arial", Font.PLAIN, 16)
        );

        journalArea.setForeground(DEEP_PLUM);
        journalArea.setCaretColor(accent);

        journalArea.setLineWrap(true);
        journalArea.setWrapStyleWord(true);
        journalArea.setOpaque(false);

        journalArea.setMargin(
                new Insets(15, 15, 15, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(journalArea);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(255, 255, 255, 180),
                        1
                )
        );

        journalCard.add(
                scrollPane,
                BorderLayout.CENTER
        );

        content.add(
                journalCard,
                BorderLayout.CENTER
        );

        // EMOTION CARD

        emotionCard = new GlassPanel(
                new BorderLayout(10, 15)
        );

        emotionCard.setPreferredSize(
                new Dimension(205, 0)
        );

        emotionCard.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 15, 20, 15
                )
        );

        JLabel emotionTitle = new JLabel(
                "YOUR MOOD",
                SwingConstants.CENTER
        );

        emotionTitle.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        emotionTitle.setForeground(DEEP_PLUM);

        JLabel emotionSubtitle = new JLabel(
                "Choose how you feel",
                SwingConstants.CENTER
        );

        emotionSubtitle.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );

        emotionSubtitle.setForeground(DEEP_PLUM);

        JPanel emotionHeading = new JPanel();

        emotionHeading.setOpaque(false);

        emotionHeading.setLayout(
                new BoxLayout(
                        emotionHeading,
                        BoxLayout.Y_AXIS
                )
        );

        emotionTitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        emotionSubtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        emotionHeading.add(emotionTitle);
        emotionHeading.add(Box.createVerticalStrut(5));
        emotionHeading.add(emotionSubtitle);

        emotionCard.add(
                emotionHeading,
                BorderLayout.NORTH
        );

        JPanel emotionCenter = new JPanel();

        emotionCenter.setOpaque(false);

        emotionCenter.setLayout(
                new BoxLayout(
                        emotionCenter,
                        BoxLayout.Y_AXIS
                )
        );

        selectedEmotionLabel = new JLabel(
                "No mood selected",
                SwingConstants.CENTER
        );

        selectedEmotionLabel.setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        selectedEmotionLabel.setForeground(DEEP_PLUM);

        selectedEmotionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        selectEmotionButton =
                new GlassButton("Select Emotion");

        selectEmotionButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        selectEmotionButton.addActionListener(
                e -> showEmotionPicker()
        );

        emotionCenter.add(Box.createVerticalGlue());
        emotionCenter.add(selectEmotionButton);

        emotionCenter.add(
                Box.createVerticalStrut(12)
        );

        emotionCenter.add(selectedEmotionLabel);
        emotionCenter.add(Box.createVerticalGlue());

        emotionCard.add(
                emotionCenter,
                BorderLayout.CENTER
        );

        content.add(
                emotionCard,
                BorderLayout.EAST
        );

        background.add(
                content,
                BorderLayout.CENTER
        );

        // FOOTER

        footerCard = new GlassPanel(
                new BorderLayout()
        );

        footerCard.setBorder(
                BorderFactory.createEmptyBorder(
                        12, 18, 12, 18
                )
        );

        statusLabel = new JLabel("Saved");

        statusLabel.setForeground(DEEP_PLUM);

        statusLabel.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        GlassButton saveButton =
                new GlassButton("Save");

        saveButton.addActionListener(e -> {

            if (saveEntry()) {

                saveEmotion();

                statusLabel.setText(
                        "Saved successfully!"
                );
            }
        });

        footerCard.add(
                statusLabel,
                BorderLayout.WEST
        );

        footerCard.add(
                saveButton,
                BorderLayout.EAST
        );

        background.add(
                footerCard,
                BorderLayout.SOUTH
        );

        // LOAD DATA

        loadEntry();
        loadEmotion();

        applyEmotionTheme();
        updateSelectedEmotion();

        // AUTOSAVE JOURNAL

        journalArea.getDocument().addDocumentListener(
                new DocumentListener() {

                    private void autoSave() {

                        if (saveEntry()) {
                            statusLabel.setText("Saved");
                        } else {
                            statusLabel.setText(
                                    "Unable to save"
                            );
                        }
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        autoSave();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        autoSave();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        autoSave();
                    }
                }
        );

        // SAVE WHEN CLOSING

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                saveEntry();
                saveEmotion();
            }
        });
    }

    // EMOTION PICKER

    private void showEmotionPicker() {

        JDialog dialog = new JDialog(
                this,
                "Choose Your Emotion",
                true
        );

        dialog.setSize(300, 520);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);

        GradientPanel dialogBackground =
                new GradientPanel();

        dialogBackground.setLayout(
                new BorderLayout()
        );

        dialogBackground.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 18, 18, 18
                )
        );

        GlassPanel pickerCard = new GlassPanel(
                new BorderLayout(10, 15)
        );

        pickerCard.setBorder(
                BorderFactory.createEmptyBorder(
                        18, 15, 18, 15
                )
        );

        JLabel title = new JLabel(
                "How are you feeling?",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font("Arial", Font.BOLD, 17)
        );

        title.setForeground(DEEP_PLUM);

        pickerCard.add(
                title,
                BorderLayout.NORTH
        );

        JPanel choices = new JPanel();

        choices.setOpaque(false);

        choices.setLayout(
                new BoxLayout(
                        choices,
                        BoxLayout.Y_AXIS
                )
        );

        for (int i = 0; i < emotions.length; i++) {

            final int index = i;

            GlassEmotionButton button =
                    new GlassEmotionButton(
                            emotions[i]
                    );

            button.setIcon(
                    getEmotionIcon(
                            emotionImages[i],
                            43,
                            43
                    )
            );

            button.addActionListener(e -> {

                selectedEmotion = emotions[index];

                saveEmotion();

                applyEmotionTheme();
                updateSelectedEmotion();

                statusLabel.setText(
                        "Mood saved: " + selectedEmotion
                );

                dialog.dispose();
            });

            choices.add(button);

            choices.add(
                    Box.createVerticalStrut(9)
            );
        }

        pickerCard.add(
                choices,
                BorderLayout.CENTER
        );

        dialogBackground.add(
                pickerCard,
                BorderLayout.CENTER
        );

        dialog.setContentPane(dialogBackground);
        dialog.setVisible(true);
    }

    // EMOTION THEME COLORS

    private void applyEmotionTheme() {

        switch (selectedEmotion) {

            case "Happy":

                bgStart = new Color(255, 239, 197);
                bgEnd = new Color(255, 211, 177);

                accent = new Color(235, 174, 95);
                accent2 = new Color(244, 183, 146);

                break;

            case "Sad":

                bgStart = new Color(214, 231, 250);
                bgEnd = new Color(177, 201, 238);

                accent = new Color(110, 151, 210);
                accent2 = new Color(151, 184, 226);

                break;

            case "Tired":

                bgStart = new Color(231, 231, 239);
                bgEnd = new Color(203, 203, 220);

                accent = new Color(143, 145, 172);
                accent2 = new Color(174, 174, 193);

                break;

            case "Envy":

                bgStart = new Color(220, 240, 215);
                bgEnd = new Color(177, 217, 185);

                accent = new Color(100, 163, 116);
                accent2 = new Color(151, 195, 152);

                break;

            case "Fear":

                bgStart = new Color(231, 219, 247);
                bgEnd = new Color(193, 177, 226);

                accent = new Color(135, 111, 183);
                accent2 = new Color(170, 148, 209);

                break;

            case "Angry":

                bgStart = new Color(250, 218, 220);
                bgEnd = new Color(231, 165, 177);

                accent = new Color(193, 83, 111);
                accent2 = new Color(226, 132, 150);

                break;

            default:

                bgStart = new Color(252, 225, 221);
                bgEnd = new Color(192, 180, 239);

                accent = new Color(142, 138, 217);
                accent2 = new Color(193, 137, 175);

                break;
        }

        glassTop = new Color(255, 255, 255, 175);
        glassBottom = new Color(255, 255, 255, 85);
        glassBorder = new Color(255, 255, 255, 225);

        if (background != null) {
            background.repaint();
        }

        if (selectEmotionButton != null) {
            selectEmotionButton.repaint();
        }

        if (headerCard != null) {
            headerCard.repaint();
        }

        if (journalCard != null) {
            journalCard.repaint();
        }

        if (emotionCard != null) {
            emotionCard.repaint();
        }

        if (footerCard != null) {
            footerCard.repaint();
        }
    }

    // IMAGE LOADER

    private ImageIcon getEmotionIcon(
            String filename,
            int width,
            int height) {

        String imagePath =
                "/images/img/" + filename;

        java.net.URL imageURL =
                getClass().getResource(imagePath);

        if (imageURL == null) {

            System.out.println(
                    "EMOTION IMAGE NOT FOUND: " + imagePath
            );

            return null;
        }

        ImageIcon original =
                new ImageIcon(imageURL);

        Image scaledImage =
                original.getImage().getScaledInstance(
                        width,
                        height,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(scaledImage);
    }

    // UPDATE SELECTED EMOTION

    private void updateSelectedEmotion() {

        if (selectedEmotionLabel == null) {
            return;
        }

        if (selectedEmotion.isEmpty()) {

            selectedEmotionLabel.setText(
                    "No mood selected"
            );

            selectEmotionButton.setText(
                    "Select Emotion"
            );

            selectEmotionButton.setIcon(null);

            return;
        }

        for (int i = 0; i < emotions.length; i++) {

            if (emotions[i].equals(selectedEmotion)) {

                selectedEmotionLabel.setText(
                        selectedEmotion
                );

                selectEmotionButton.setText(
                        "Change Emotion"
                );

                selectEmotionButton.setIcon(
                        getEmotionIcon(
                                emotionImages[i],
                                28,
                                28
                        )
                );

                return;
            }
        }

        selectedEmotion = "";

        selectedEmotionLabel.setText(
                "No mood selected"
        );

        selectEmotionButton.setText(
                "Select Emotion"
        );

        selectEmotionButton.setIcon(null);
    }

    // LOAD JOURNAL

    private void loadEntry() {

        try {

            Files.createDirectories(
                    journalFile.getParent()
            );

            if (Files.exists(journalFile)) {

                journalArea.setText(
                        Files.readString(journalFile)
                );
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load journal entry.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // SAVE JOURNAL

    private boolean saveEntry() {

        try {

            Files.createDirectories(
                    journalFile.getParent()
            );

            Files.writeString(
                    journalFile,
                    journalArea.getText()
            );

            return true;

        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }

    // LOAD EMOTION

    private void loadEmotion() {

        try {

            if (Files.exists(emotionFile)) {

                selectedEmotion =
                        Files.readString(
                                emotionFile
                        ).trim();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // SAVE EMOTION

    private void saveEmotion() {

        try {

            Files.createDirectories(
                    emotionFile.getParent()
            );

            Files.writeString(
                    emotionFile,
                    selectedEmotion
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // LIQUID GLASS BACKGROUND

    private class GradientPanel extends JPanel {

        public GradientPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                    new GradientPaint(
                            0, 0, bgStart,
                            getWidth(), getHeight(), bgEnd
                    );

            g2.setPaint(gradient);

            g2.fillRect(
                    0, 0,
                    getWidth(),
                    getHeight()
            );

            g2.setColor(
                    new Color(255, 255, 255, 85)
            );

            g2.fillOval(
                    getWidth() - 260,
                    15,
                    300,
                    300
            );

            g2.setColor(
                    new Color(
                            accent.getRed(),
                            accent.getGreen(),
                            accent.getBlue(),
                            35
                    )
            );

            g2.fillOval(
                    -120,
                    getHeight() - 240,
                    330,
                    330
            );

            g2.dispose();
        }
    }

    // GLASS PANEL

    private class GlassPanel extends JPanel {

        public GlassPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // SHADOW

            g2.setColor(
                    new Color(50, 40, 75, 35)
            );

            g2.fillRoundRect(
                    5, 9,
                    w - 10,
                    h - 13,
                    30, 30
            );

            // GLASS BODY

            GradientPaint glass =
                    new GradientPaint(
                            0, 0, glassTop,
                            w, h, glassBottom
                    );

            g2.setPaint(glass);

            g2.fillRoundRect(
                    1, 1,
                    w - 3,
                    h - 4,
                    30, 30
            );

            // COLORED EDGE

            g2.setColor(
                    new Color(
                            accent.getRed(),
                            accent.getGreen(),
                            accent.getBlue(),
                            75
                    )
            );

            g2.setStroke(
                    new BasicStroke(2f)
            );

            g2.drawRoundRect(
                    1, 1,
                    w - 3,
                    h - 4,
                    30, 30
            );

            // GLASS REFLECTION

            g2.setColor(glassBorder);

            g2.setStroke(
                    new BasicStroke(1.2f)
            );

            g2.drawRoundRect(
                    5, 5,
                    w - 11,
                    h - 12,
                    26, 26
            );

            g2.setColor(
                    new Color(255, 255, 255, 65)
            );

            g2.fillRoundRect(
                    12, 5,
                    Math.max(0, w - 24),
                    3,
                    10, 10
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // GLOSSY BUTTON

    private class GlassButton extends JButton {

        private boolean hover = false;

        public GlassButton(String text) {

            super(text);

            setFont(
                    new Font("Arial", Font.BOLD, 13)
            );

            setForeground(DEEP_PLUM);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setPreferredSize(
                    new Dimension(155, 50)
            );

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // BUTTON SHADOW

            g2.setColor(
                    new Color(50, 40, 75, 35)
            );

            g2.fillRoundRect(
                    4, 7,
                    getWidth() - 9,
                    getHeight() - 13,
                    30, 30
            );

            // GRADIENT BUTTON

            GradientPaint gradient =
                    new GradientPaint(
                            0, 0,
                            hover ? accent2 : accent,
                            getWidth(), getHeight(),
                            hover ? accent : accent2
                    );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                    1, 1,
                    getWidth() - 4,
                    getHeight() - 7,
                    30, 30
            );

            // GLOSSY EDGE

            g2.setColor(
                    new Color(255, 255, 255, 220)
            );

            g2.setStroke(
                    new BasicStroke(1.5f)
            );

            g2.drawRoundRect(
                    1, 1,
                    getWidth() - 4,
                    getHeight() - 7,
                    30, 30
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // GLASS EMOTION CHOICES

    private class GlassEmotionButton extends JButton {

        private boolean hover = false;

        public GlassEmotionButton(String text) {

            super(text);

            setFont(
                    new Font("Arial", Font.BOLD, 13)
            );

            setForeground(DEEP_PLUM);

            setHorizontalAlignment(
                    SwingConstants.LEFT
            );

            setHorizontalTextPosition(
                    SwingConstants.RIGHT
            );

            setIconTextGap(14);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setPreferredSize(
                    new Dimension(220, 60)
            );

            setMaximumSize(
                    new Dimension(220, 60)
            );

            setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // SHADOW

            g2.setColor(
                    new Color(50, 40, 75, 30)
            );

            g2.fillRoundRect(
                    3, 6,
                    getWidth() - 7,
                    getHeight() - 10,
                    23, 23
            );

            // TRANSLUCENT GLASS

            g2.setPaint(
                    new GradientPaint(
                            0, 0,
                            hover
                            ? new Color(255, 255, 255, 230)
                            : new Color(255, 255, 255, 165),
                            getWidth(), getHeight(),
                            hover
                            ? new Color(
                                    accent.getRed(),
                                    accent.getGreen(),
                                    accent.getBlue(),
                                    130
                            )
                            : new Color(255, 255, 255, 90)
                    )
            );

            g2.fillRoundRect(
                    1, 1,
                    getWidth() - 4,
                    getHeight() - 7,
                    23, 23
            );

            // EDGE

            g2.setColor(
                    new Color(255, 255, 255, 230)
            );

            g2.setStroke(
                    new BasicStroke(1.4f)
            );

            g2.drawRoundRect(
                    1, 1,
                    getWidth() - 4,
                    getHeight() - 7,
                    23, 23
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }
}
