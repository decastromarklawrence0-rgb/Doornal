/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package com.mycompany.doornal;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class MoodInsightsPanel extends JPanel {

    private final Color BLUSH = new Color(252, 225, 221);
    private final Color PEACH = new Color(252, 211, 191);
    private final Color LAVENDER = new Color(210, 197, 240);
    private final Color PERIWINKLE = new Color(192, 180, 239);
    private final Color PINK_LAVENDER = new Color(232, 211, 229);
    private final Color PURPLE = new Color(142, 138, 217);
    private final Color MAUVE = new Color(193, 137, 175);
    private final Color DEEP_PLUM = new Color(41, 34, 63);

    private final String[] emotions = {
        "Happy", "Sad", "Tired", "Envy", "Fear", "Angry"
    };

    private final Map<String, Integer> emotionCounts =
            new LinkedHashMap<>();

    private final Map<String, EmotionRow> emotionRows =
            new LinkedHashMap<>();

    private boolean monthlyMode = false;

    private JLabel totalValue;
    private JLabel topMoodValue;
    private JLabel daysValue;
    private JLabel topMoodDescription;
    private JLabel periodDescription;
    private JLabel chartSubtitle;
    private JLabel insightMessage;

    private JButton weeklyButton;
    private JButton monthlyButton;

    private JPanel distributionContainer;

    private String currentPeriod = "";

    public MoodInsightsPanel() {

        setLayout(new BorderLayout(0, 18));
        setBorder(new EmptyBorder(25, 30, 25, 30));
        setOpaque(false);

        initializeEmotionCounts();
        buildInterface();
        refreshStats();
    }

    // INITIALIZE EMOTION COUNTS

    private void initializeEmotionCounts() {

        emotionCounts.clear();

        for (String emotion : emotions) {
            emotionCounts.put(emotion, 0);
        }
    }

    // MAIN INTERFACE

    private void buildInterface() {

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // HEADER

    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(15, 10));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);

        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );

        JLabel title = new JLabel("Mood Insights");

        title.setFont(new Font("Arial", Font.BOLD, 31));
        title.setForeground(DEEP_PLUM);

        JLabel subtitle = new JLabel(
                "A little overview of how you've been feeling"
        );

        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(105, 89, 124));

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        // WEEKLY AND MONTHLY BUTTONS

        JPanel modePanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 5)
        );

        modePanel.setOpaque(false);

        weeklyButton = new ModeButton("Weekly");
        monthlyButton = new ModeButton("Monthly");

        weeklyButton.addActionListener(e -> {

            monthlyMode = false;
            updateModeButtons();
            refreshStats();

        });

        monthlyButton.addActionListener(e -> {

            monthlyMode = true;
            updateModeButtons();
            refreshStats();

        });

        modePanel.add(weeklyButton);
        modePanel.add(monthlyButton);

        updateModeButtons();

        header.add(titlePanel, BorderLayout.CENTER);
        header.add(modePanel, BorderLayout.EAST);

        return header;
    }

    // MAIN CONTENT

    private JPanel createMainContent() {

        JPanel content = new JPanel();
        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(content, BoxLayout.Y_AXIS)
        );

        // SUMMARY CARDS

        JPanel summaryPanel = new JPanel(
                new GridLayout(1, 3, 15, 0)
        );

        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 125)
        );

        GlassCard totalCard = createSummaryCard(
                "TOTAL MOOD RECORDS",
                "0",
                "Saved emotions"
        );

        GlassCard topMoodCard = createSummaryCard(
                "MOST FREQUENT MOOD",
                "No data",
                "Your top emotion"
        );

        GlassCard daysCard = createSummaryCard(
                "DAYS TRACKED",
                "0",
                "Days with saved moods"
        );

        totalValue = (JLabel) totalCard.getClientProperty("valueLabel");
        topMoodValue = (JLabel) topMoodCard.getClientProperty("valueLabel");
        daysValue = (JLabel) daysCard.getClientProperty("valueLabel");
        topMoodDescription = (JLabel) topMoodCard.getClientProperty("descriptionLabel");

        summaryPanel.add(totalCard);
        summaryPanel.add(topMoodCard);
        summaryPanel.add(daysCard);

        content.add(summaryPanel);
        content.add(Box.createVerticalStrut(18));

        // DISTRIBUTION CARD

        GlassCard distributionCard = new GlassCard(
                new BorderLayout(10, 15)
        );

        distributionCard.setBorder(
                new EmptyBorder(20, 24, 20, 24)
        );

        JPanel chartHeader = new JPanel(
                new BorderLayout(5, 5)
        );

        chartHeader.setOpaque(false);

        JLabel chartTitle = new JLabel("Emotion Distribution");

        chartTitle.setFont(
                new Font("Arial", Font.BOLD, 19)
        );

        chartTitle.setForeground(DEEP_PLUM);

        chartSubtitle = new JLabel("Your saved emotions this week");

        chartSubtitle.setFont(
                new Font("Arial", Font.PLAIN, 12)
        );

        chartSubtitle.setForeground(new Color(112, 97, 129));

        JPanel chartTitlePanel = new JPanel();

        chartTitlePanel.setOpaque(false);

        chartTitlePanel.setLayout(
                new BoxLayout(chartTitlePanel, BoxLayout.Y_AXIS)
        );

        chartTitlePanel.add(chartTitle);
        chartTitlePanel.add(Box.createVerticalStrut(4));
        chartTitlePanel.add(chartSubtitle);

        chartHeader.add(chartTitlePanel, BorderLayout.CENTER);

        distributionCard.add(
                chartHeader,
                BorderLayout.NORTH
        );

        distributionContainer = new JPanel();

        distributionContainer.setOpaque(false);

        distributionContainer.setLayout(
                new BoxLayout(
                        distributionContainer,
                        BoxLayout.Y_AXIS
                )
        );

        for (String emotion : emotions) {

            EmotionRow row = new EmotionRow(
                    emotion,
                    getEmotionColor(emotion)
            );

            emotionRows.put(emotion, row);

            distributionContainer.add(row);
            distributionContainer.add(
                    Box.createVerticalStrut(13)
            );
        }

        distributionCard.add(
                distributionContainer,
                BorderLayout.CENTER
        );

        // INSIGHT MESSAGE

        insightMessage = new JLabel(
                "Your mood story will appear here."
        );

        insightMessage.setFont(
                new Font("Arial", Font.BOLD, 13)
        );

        insightMessage.setForeground(DEEP_PLUM);

        GlassCard insightCard = new GlassCard(
                new BorderLayout()
        );

        insightCard.setBorder(
                new EmptyBorder(15, 20, 15, 20)
        );

        insightCard.add(
                insightMessage,
                BorderLayout.CENTER
        );

        content.add(distributionCard);
        content.add(Box.createVerticalStrut(15));
        content.add(insightCard);

        JScrollPane scrollPane = new JScrollPane(content);

        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scrollPane, BorderLayout.CENTER);

        return wrapper;
    }

    // SUMMARY CARD CREATOR

    private GlassCard createSummaryCard(
            String heading,
            String value,
            String description) {

        GlassCard card = new GlassCard(
                new BorderLayout(5, 8)
        );

        card.setBorder(
                new EmptyBorder(15, 17, 15, 17)
        );

        JPanel inside = new JPanel();

        inside.setOpaque(false);

        inside.setLayout(
                new BoxLayout(inside, BoxLayout.Y_AXIS)
        );

        JLabel headingLabel = new JLabel(heading);

        headingLabel.setFont(
                new Font("Arial", Font.BOLD, 10)
        );

        headingLabel.setForeground(
                new Color(112, 97, 129)
        );

        JLabel valueLabel = new JLabel(value);

        valueLabel.setFont(
                new Font("Arial", Font.BOLD, 25)
        );

        valueLabel.setForeground(DEEP_PLUM);

        JLabel descriptionLabel = new JLabel(description);

        descriptionLabel.setFont(
                new Font("Arial", Font.PLAIN, 10)
        );

        descriptionLabel.setForeground(
                new Color(112, 97, 129)
        );

        inside.add(headingLabel);
        inside.add(Box.createVerticalStrut(8));
        inside.add(valueLabel);
        inside.add(Box.createVerticalStrut(4));
        inside.add(descriptionLabel);

        card.add(inside, BorderLayout.CENTER);

        card.putClientProperty("valueLabel", valueLabel);
        card.putClientProperty("descriptionLabel", descriptionLabel);

        return card;
    }

    // REFRESH STATISTICS
    // Called whenever the user opens Mood Insights.

    public void refreshStats() {

        initializeEmotionCounts();

        LocalDate today = LocalDate.now();

        LocalDate startDate;
        LocalDate endDate;

        if (monthlyMode) {

            YearMonth month = YearMonth.from(today);

            startDate = month.atDay(1);
            endDate = month.atEndOfMonth();

            currentPeriod = month.getMonth().getDisplayName(
                    java.time.format.TextStyle.FULL,
                    java.util.Locale.ENGLISH
            ) + " " + month.getYear();

        } else {

            startDate = today.with(
                    java.time.DayOfWeek.MONDAY
            );

            endDate = startDate.plusDays(6);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("MMM dd");

            currentPeriod =
                    startDate.format(formatter)
                    + " - "
                    + endDate.format(formatter)
                    + ", "
                    + endDate.getYear();
        }

        readEmotionFiles(startDate, endDate);

        updateStatistics();
    }

    // READ ACTUAL SAVED EMOTION FILES

    private void readEmotionFiles(
            LocalDate startDate,
            LocalDate endDate) {

        if (CurrentUser.username == null
                || CurrentUser.username.trim().isEmpty()) {
            return;
        }

        Path journalFolder = Path.of(
                System.getProperty("user.home"),
                "Doornal",
                CurrentUser.username,
                "Journals"
        );

        if (!Files.exists(journalFolder)) {
            return;
        }

        try (DirectoryStream<Path> files =
                     Files.newDirectoryStream(
                             journalFolder,
                             "*.emotion"
                     )) {

            for (Path file : files) {

                String fileName =
                        file.getFileName().toString();

                if (!fileName.endsWith(".emotion")) {
                    continue;
                }

                String dateText = fileName.substring(
                        0,
                        fileName.length() - ".emotion".length()
                );

                try {

                    LocalDate savedDate =
                            LocalDate.parse(dateText);

                    if (savedDate.isBefore(startDate)
                            || savedDate.isAfter(endDate)) {
                        continue;
                    }

                    String savedEmotion =
                            Files.readString(file).trim();

                    if (emotionCounts.containsKey(savedEmotion)) {

                        emotionCounts.put(
                                savedEmotion,
                                emotionCounts.get(savedEmotion) + 1
                        );
                    }

                } catch (Exception ignored) {
                    // Ignore files that do not contain a valid date.
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // UPDATE ALL DISPLAYED STATISTICS

    private void updateStatistics() {

        int total = 0;
        int highestCount = 0;

        String mostFrequentEmotion = "No data";

        for (String emotion : emotions) {

            int count = emotionCounts.get(emotion);

            total += count;

            if (count > highestCount) {

                highestCount = count;
                mostFrequentEmotion = emotion;
            }
        }

        totalValue.setText(String.valueOf(total));

        daysValue.setText(String.valueOf(total));

        topMoodValue.setText(mostFrequentEmotion);

        if (total == 0) {

            topMoodValue.setText("No data");

            topMoodDescription.setText(
                    "No saved emotions yet"
            );

            insightMessage.setText(
                    "Your mood story will appear here once you save an emotion."
            );

        } else {

            int percentage = (int) Math.round(
                    (highestCount * 100.0) / total
            );

            topMoodDescription.setText(
                    highestCount + " record(s) • " + percentage + "%"
            );

            insightMessage.setText(
                    "Your most frequently recorded mood is "
                    + mostFrequentEmotion
                    + " with "
                    + highestCount
                    + " record(s) during this period."
            );
        }

        chartSubtitle.setText(
                monthlyMode
                ? "Your saved emotions for " + currentPeriod
                : "Your saved emotions for " + currentPeriod
        );

        int maxCount = 0;

        for (int count : emotionCounts.values()) {
            maxCount = Math.max(maxCount, count);
        }

        for (String emotion : emotions) {

            EmotionRow row = emotionRows.get(emotion);

            if (row != null) {

                row.updateCount(
                        emotionCounts.get(emotion),
                        maxCount
                );
            }
        }

        revalidate();
        repaint();
    }

    // WEEKLY / MONTHLY BUTTON STYLE

    private void updateModeButtons() {

        if (weeklyButton == null || monthlyButton == null) {
            return;
        }

        styleModeButton(
                weeklyButton,
                !monthlyMode
        );

        styleModeButton(
                monthlyButton,
                monthlyMode
        );
    }

    private void styleModeButton(
            JButton button,
            boolean selected) {

        button.putClientProperty("selected", selected);
        button.repaint();
    }

    private class ModeButton extends JButton {

        public ModeButton(String text) {

            super(text);

            setFont(
                    new Font("Arial", Font.BOLD, 12)
            );

            setPreferredSize(
                    new Dimension(95, 38)
            );

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            boolean selected = Boolean.TRUE.equals(
                    getClientProperty("selected")
            );

            RoundRectangle2D shape =
                    new RoundRectangle2D.Float(
                            1, 1,
                            getWidth() - 3,
                            getHeight() - 3,
                            22, 22
                    );

            if (selected) {

                g2.setPaint(
                        new GradientPaint(
                                0, 0, PURPLE,
                                getWidth(), getHeight(), MAUVE
                        )
                );

                g2.fill(shape);

                g2.setColor(
                        new Color(255, 255, 255, 190)
                );

                g2.draw(shape);

                setForeground(Color.WHITE);

            } else {

                g2.setColor(
                        new Color(255, 255, 255, 145)
                );

                g2.fill(shape);

                g2.setColor(
                        new Color(255, 255, 255, 220)
                );

                g2.draw(shape);

                setForeground(DEEP_PLUM);
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // EMOTION COLOR

    private Color getEmotionColor(String emotion) {

        switch (emotion) {

            case "Happy":
                return new Color(255, 224, 145);

            case "Sad":
                return new Color(166, 205, 244);

            case "Tired":
                return new Color(205, 205, 215);

            case "Envy":
                return new Color(174, 218, 180);

            case "Fear":
                return new Color(202, 184, 232);

            case "Angry":
                return new Color(244, 174, 177);

            default:
                return PURPLE;
        }
    }

    // EMOTION DISTRIBUTION ROW

    private class EmotionRow extends JPanel {

        private final String emotion;
        private final Color emotionColor;

        private JLabel countLabel;
        private MoodBar moodBar;

        public EmotionRow(
                String emotion,
                Color emotionColor) {

            this.emotion = emotion;
            this.emotionColor = emotionColor;

            setOpaque(false);
            setLayout(new BorderLayout(12, 0));

            setPreferredSize(
                    new Dimension(500, 31)
            );

            setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 31)
            );

            JLabel nameLabel = new JLabel(emotion);

            nameLabel.setFont(
                    new Font("Arial", Font.BOLD, 12)
            );

            nameLabel.setForeground(DEEP_PLUM);

            nameLabel.setPreferredSize(
                    new Dimension(75, 25)
            );

            moodBar = new MoodBar(emotionColor);

            countLabel = new JLabel("0");

            countLabel.setFont(
                    new Font("Arial", Font.BOLD, 12)
            );

            countLabel.setForeground(DEEP_PLUM);

            countLabel.setHorizontalAlignment(
                    SwingConstants.RIGHT
            );

            countLabel.setPreferredSize(
                    new Dimension(35, 25)
            );

            add(nameLabel, BorderLayout.WEST);
            add(moodBar, BorderLayout.CENTER);
            add(countLabel, BorderLayout.EAST);
        }

        public void updateCount(int count, int maximum) {

            countLabel.setText(String.valueOf(count));

            moodBar.setProgress(
                    maximum == 0
                    ? 0
                    : count / (double) maximum
            );
        }
    }

    // CUSTOM MOOD BAR

    private class MoodBar extends JPanel {

        private final Color barColor;
        private double progress = 0;

        public MoodBar(Color color) {

            barColor = color;
            setOpaque(false);

            setPreferredSize(
                    new Dimension(200, 25)
            );
        }

        public void setProgress(double value) {

            progress = Math.max(
                    0,
                    Math.min(1, value)
            );

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = 12;

            int y = (getHeight() - height) / 2;

            // TRACK

            g2.setColor(
                    new Color(255, 255, 255, 180)
            );

            g2.fillRoundRect(
                    0, y,
                    width,
                    height,
                    20, 20
            );

            // PROGRESS

            int progressWidth = (int) (
                    width * progress
            );

            if (progressWidth > 0) {

                g2.setPaint(
                        new GradientPaint(
                                0, y,
                                barColor,
                                progressWidth, y + height,
                                barColor.darker()
                        )
                );

                g2.fillRoundRect(
                        0, y,
                        progressWidth,
                        height,
                        20, 20
                );
            }

            g2.dispose();
        }
    }

    // GLASS CARD

    private class GlassCard extends JPanel {

        public GlassCard(LayoutManager layout) {

            super(layout);

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

            // SOFT SHADOW

            g2.setColor(
                    new Color(65, 48, 92, 22)
            );

            g2.fillRoundRect(
                    4, 7,
                    w - 9,
                    h - 12,
                    28, 28
            );

            // GLASS BODY

            g2.setPaint(
                    new GradientPaint(
                            0, 0,
                            new Color(255, 255, 255, 205),
                            w, h,
                            new Color(255, 255, 255, 105)
                    )
            );

            g2.fillRoundRect(
                    1, 1,
                    w - 3,
                    h - 4,
                    28, 28
            );

            // GLASS EDGE

            g2.setStroke(
                    new BasicStroke(1.3f)
            );

            g2.setColor(
                    new Color(255, 255, 255, 225)
            );

            g2.drawRoundRect(
                    1, 1,
                    w - 3,
                    h - 4,
                    28, 28
            );

            // SUBTLE PURPLE EDGE

            g2.setColor(
                    new Color(142, 138, 217, 45)
            );

            g2.drawRoundRect(
                    4, 4,
                    w - 9,
                    h - 10,
                    25, 25
            );

            // TOP REFLECTION

            g2.setColor(
                    new Color(255, 255, 255, 105)
            );

            g2.fillRoundRect(
                    14, 6,
                    Math.max(0, w - 28),
                    3,
                    10, 10
            );

            g2.dispose();
        }
    }

    // BACKGROUND

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
                0, 0, BLUSH,
                w, h, LAVENDER
        );

        g2.setPaint(gradient);
        g2.fillRect(0, 0, w, h);

        // TOP RIGHT LIGHT

        g2.setColor(
                new Color(192, 180, 239, 65)
        );

        g2.fillOval(
                w - 300, -100,
                350, 350
        );

        // BOTTOM LEFT LIGHT

        g2.setColor(
                new Color(252, 211, 191, 85)
        );

        g2.fillOval(
                -130, h - 250,
                350, 350
        );

        // SOFT WHITE BLOOM

        g2.setColor(
                new Color(255, 255, 255, 45)
        );

        g2.fillOval(
                w / 2, h / 2,
                250, 250
        );

        g2.dispose();
    }
}
