
package com.mycompany.doornal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;

public class CalendarPanel extends JPanel {

    private YearMonth currentMonth;
    private LocalDate selectedDate;

    private JLabel monthLabel;
    private JPanel datesPanel;

    private JLabel totalEntriesLabel;
    private JLabel mostFrequentLabel;
    private JLabel daysTrackedLabel;

    private JPanel weeklyChartPanel;
    private JPanel recentEntriesPanel;
    private JLabel quoteLabel;

    private Map<LocalDate, String> moodEntries = new HashMap<>();

    // DOORNAL COLORS

    private final Color BLUSH = new Color(0xFC, 0xE1, 0xDD);
    private final Color PEACH = new Color(0xFC, 0xD3, 0xBF);
    private final Color LAVENDER = new Color(0xD2, 0xC5, 0xF0);
    private final Color PERIWINKLE = new Color(0xC0, 0xB4, 0xEF);
    private final Color PURPLE = new Color(0x8E, 0x8A, 0xD9);
    private final Color MAUVE = new Color(0xC1, 0x89, 0xAF);
    private final Color DEEP_PLUM = new Color(0x29, 0x22, 0x3F);
    private final Color WHITE = new Color(255, 252, 252);

    // EMOTION COLORS

    private final Color HAPPY_COLOR = new Color(255, 224, 145);
    private final Color SAD_COLOR = new Color(166, 205, 244);
    private final Color TIRED_COLOR = new Color(205, 205, 215);
    private final Color ENVY_COLOR = new Color(174, 218, 180);
    private final Color FEAR_COLOR = new Color(202, 184, 232);
    private final Color ANGRY_COLOR = new Color(244, 174, 177);

    private final String[] emotions = {
        "Happy", "Sad", "Tired", "Envy", "Fear", "Angry"
    };

    // CONSTRUCTOR

    public CalendarPanel() {

        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();

        setLayout(new BorderLayout(14, 14));
        setOpaque(false);

        setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        loadMoodEntries();

        createDashboard();

        updateCalendar();
        refreshDashboard();
    }

    // MAIN DASHBOARD

    private void createDashboard() {

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(
            new BoxLayout(leftPanel, BoxLayout.Y_AXIS)
        );
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(205, 0));

        leftPanel.add(createMonthlySummary());
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(createEmotionLegend());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(
            new BoxLayout(rightPanel, BoxLayout.Y_AXIS)
        );
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(215, 0));

        rightPanel.add(createWeeklyOverview());
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(createRecentEntries());

        JPanel centerPanel = createCalendarCard();

        JPanel bottomPanel = createReflectionCard();

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // MONTHLY SUMMARY CARD

    private JPanel createMonthlySummary() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(205, 220));
        card.setMaximumSize(new Dimension(205, 220));

        JLabel title = createTitle("Monthly Summary");

        totalEntriesLabel = createBigNumber("0");
        mostFrequentLabel = createInfoLabel("No entries yet");
        daysTrackedLabel = createInfoLabel("0 days");

        card.add(title);
        card.add(Box.createVerticalStrut(17));

        card.add(createSmallLabel("TOTAL ENTRIES"));
        card.add(totalEntriesLabel);

        card.add(Box.createVerticalStrut(12));

        card.add(createSmallLabel("MOST FREQUENT MOOD"));
        card.add(mostFrequentLabel);

        card.add(Box.createVerticalStrut(12));

        card.add(createSmallLabel("DAYS TRACKED"));
        card.add(daysTrackedLabel);

        return card;
    }

    // EMOTION LEGEND

    private JPanel createEmotionLegend() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(205, 250));
        card.setMaximumSize(new Dimension(205, 250));

        card.add(createTitle("Emotion Legend"));
        card.add(Box.createVerticalStrut(17));

        addLegendItem(card, "Happy", HAPPY_COLOR);
        addLegendItem(card, "Sad", SAD_COLOR);
        addLegendItem(card, "Tired", TIRED_COLOR);
        addLegendItem(card, "Envy", ENVY_COLOR);
        addLegendItem(card, "Fear", FEAR_COLOR);
        addLegendItem(card, "Angry", ANGRY_COLOR);

        return card;
    }

    private void addLegendItem(
        JPanel panel,
        String name,
        Color color
    ) {

        JPanel item = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 9, 4)
        );

        item.setOpaque(false);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel dot = new JPanel();
        dot.setBackground(color);
        dot.setPreferredSize(new Dimension(13, 13));
        dot.setBorder(
            BorderFactory.createLineBorder(
                new Color(255, 255, 255, 180)
            )
        );

        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(DEEP_PLUM);

        item.add(dot);
        item.add(label);

        panel.add(item);
    }

    // WEEKLY OVERVIEW

    private JPanel createWeeklyOverview() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(215, 285));
        card.setMaximumSize(new Dimension(215, 285));

        card.add(createTitle("Weekly Overview"));
        card.add(Box.createVerticalStrut(5));

        JLabel subtitle = createSmallLabel("THIS WEEK");
        card.add(subtitle);

        card.add(Box.createVerticalStrut(12));

        weeklyChartPanel = new JPanel();

        weeklyChartPanel.setLayout(
            new BoxLayout(weeklyChartPanel, BoxLayout.Y_AXIS)
        );

        weeklyChartPanel.setOpaque(false);

        card.add(weeklyChartPanel);

        return card;
    }

    // RECENT ENTRIES

    private JPanel createRecentEntries() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(215, 220));
        card.setMaximumSize(new Dimension(215, 220));

        card.add(createTitle("Recent Entries"));
        card.add(Box.createVerticalStrut(12));

        recentEntriesPanel = new JPanel();

        recentEntriesPanel.setLayout(
            new BoxLayout(recentEntriesPanel, BoxLayout.Y_AXIS)
        );

        recentEntriesPanel.setOpaque(false);

        card.add(recentEntriesPanel);

        return card;
    }

    // CALENDAR CARD

    private JPanel createCalendarCard() {

        GlassCard card = new GlassCard();

        card.setLayout(new BorderLayout(12, 18));

        JPanel header = new JPanel(
            new BorderLayout(10, 0)
        );

        header.setOpaque(false);

        JButton previousButton = new JButton("‹");
        JButton nextButton = new JButton("›");

        styleArrowButton(previousButton);
        styleArrowButton(nextButton);

        monthLabel = new JLabel(
            "",
            SwingConstants.CENTER
        );

        monthLabel.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        monthLabel.setForeground(DEEP_PLUM);

        previousButton.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
            refreshDashboard();
        });

        nextButton.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
            refreshDashboard();
        });

        header.add(previousButton, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(nextButton, BorderLayout.EAST);

        JPanel calendarContent = new JPanel(
            new BorderLayout(0, 15)
        );

        calendarContent.setOpaque(false);

        JPanel weekdays = new JPanel(
            new GridLayout(1, 7, 8, 8)
        );

        weekdays.setOpaque(false);

        String[] weekdayNames = {
            "SUN", "MON", "TUE", "WED",
            "THU", "FRI", "SAT"
        };

        for (String day : weekdayNames) {

            JLabel label = new JLabel(
                day,
                SwingConstants.CENTER
            );

            label.setFont(
                new Font("Arial", Font.BOLD, 12)
            );

            label.setForeground(MAUVE);

            weekdays.add(label);
        }

        datesPanel = new JPanel(
            new GridLayout(6, 7, 9, 9)
        );

        datesPanel.setOpaque(false);

        calendarContent.add(weekdays, BorderLayout.NORTH);
        calendarContent.add(datesPanel, BorderLayout.CENTER);

        card.add(header, BorderLayout.NORTH);
        card.add(calendarContent, BorderLayout.CENTER);

        return card;
    }

    // DAILY REFLECTION

    private JPanel createReflectionCard() {

        GlassCard card = new GlassCard();

        card.setLayout(new BorderLayout(12, 0));
        card.setPreferredSize(new Dimension(0, 75));

        JLabel icon = new JLabel("✦");
        icon.setFont(new Font("Arial", Font.BOLD, 25));
        icon.setForeground(PURPLE);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(
            new BoxLayout(textPanel, BoxLayout.Y_AXIS)
        );
        textPanel.setOpaque(false);

        JLabel title = new JLabel("A LITTLE REMINDER");
        title.setFont(new Font("Arial", Font.BOLD, 11));
        title.setForeground(MAUVE);

        quoteLabel = new JLabel();
        quoteLabel.setFont(
            new Font("Arial", Font.ITALIC, 14)
        );
        quoteLabel.setForeground(DEEP_PLUM);

        textPanel.add(title);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(quoteLabel);

        card.add(icon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    // REFRESH DASHBOARD DATA

    public void refreshDashboard() {

        loadMoodEntries();

        int total = 0;
        int daysTracked = 0;

        Map<String, Integer> counts = new HashMap<>();

        for (String emotion : emotions) {
            counts.put(emotion, 0);
        }

        for (Map.Entry<LocalDate, String> entry
                : moodEntries.entrySet()) {

            if (YearMonth.from(entry.getKey()).equals(currentMonth)) {

                String emotion = entry.getValue();

                if (counts.containsKey(emotion)) {
                    counts.put(
                        emotion,
                        counts.get(emotion) + 1
                    );

                    total++;
                    daysTracked++;
                }
            }
        }

        String mostFrequent = "No entries yet";
        int highest = 0;

        for (String emotion : emotions) {

            int count = counts.get(emotion);

            if (count > highest) {
                highest = count;
                mostFrequent = emotion;
            }
        }

        totalEntriesLabel.setText(String.valueOf(total));
        mostFrequentLabel.setText(mostFrequent);
        daysTrackedLabel.setText(daysTracked + " days");

        updateWeeklyChart();
        updateRecentEntries();
        updateReflection();

        revalidate();
        repaint();
    }

    // LOAD SAVED EMOTIONS

    private void loadMoodEntries() {

        moodEntries.clear();

        String username = CurrentUser.username;

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        Path folder = Path.of(
            System.getProperty("user.home"),
            "Doornal",
            username,
            "Journals"
        );

        if (!Files.exists(folder)) {
            return;
        }

        try (DirectoryStream<Path> stream =
                Files.newDirectoryStream(folder, "*.emotion")) {

            for (Path file : stream) {

                String filename =
                    file.getFileName().toString();

                String dateText = filename.replace(
                    ".emotion", ""
                );

                try {

                    LocalDate date = LocalDate.parse(dateText);

                    String emotion = Files.readString(file).trim();

                    if (Arrays.asList(emotions).contains(emotion)) {
                        moodEntries.put(date, emotion);
                    }

                } catch (Exception ignored) {
                    // Skip files with invalid dates or contents.
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // WEEKLY CHART

    private void updateWeeklyChart() {

        weeklyChartPanel.removeAll();

        LocalDate today = LocalDate.now();

        LocalDate monday = today.with(
            DayOfWeek.MONDAY
        );

        Map<String, Integer> counts = new HashMap<>();

        for (String emotion : emotions) {
            counts.put(emotion, 0);
        }

        for (int i = 0; i < 7; i++) {

            LocalDate date = monday.plusDays(i);

            String emotion = moodEntries.get(date);

            if (emotion != null && counts.containsKey(emotion)) {
                counts.put(
                    emotion,
                    counts.get(emotion) + 1
                );
            }
        }

        for (String emotion : emotions) {

            int count = counts.get(emotion);

            JPanel row = new JPanel(
                new BorderLayout(7, 0)
            );

            row.setOpaque(false);
            row.setMaximumSize(new Dimension(190, 28));

            JLabel name = new JLabel(emotion);

            name.setFont(
                new Font("Arial", Font.PLAIN, 11)
            );

            name.setForeground(DEEP_PLUM);

            JLabel number = new JLabel(
                String.valueOf(count)
            );

            number.setFont(
                new Font("Arial", Font.BOLD, 11)
            );

            number.setForeground(DEEP_PLUM);

            JPanel bar = new JPanel() {

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

                    g2.setColor(new Color(230, 221, 239));

                    g2.fillRoundRect(
                        0, 0, width, height, 10, 10
                    );

                    if (count > 0) {

                        int fillWidth = Math.max(
                            5,
                            (int) (width * count / 7.0)
                        );

                        g2.setColor(getEmotionColor(emotion));

                        g2.fillRoundRect(
                            0, 0,
                            fillWidth,
                            height,
                            10, 10
                        );
                    }

                    g2.dispose();
                }
            };

            bar.setOpaque(false);
            bar.setPreferredSize(new Dimension(80, 9));

            JPanel middle = new JPanel(new BorderLayout());
            middle.setOpaque(false);
            middle.add(bar, BorderLayout.CENTER);

            row.add(name, BorderLayout.WEST);
            row.add(middle, BorderLayout.CENTER);
            row.add(number, BorderLayout.EAST);

            weeklyChartPanel.add(row);
            weeklyChartPanel.add(Box.createVerticalStrut(9));
        }

        weeklyChartPanel.revalidate();
        weeklyChartPanel.repaint();
    }

    // RECENT JOURNAL ENTRIES

    private void updateRecentEntries() {

        recentEntriesPanel.removeAll();

        List<LocalDate> dates = new ArrayList<>(
            moodEntries.keySet()
        );

        dates.sort(Comparator.reverseOrder());

        int shown = 0;

        for (LocalDate date : dates) {

            if (shown >= 3) {
                break;
            }

            String emotion = moodEntries.get(date);

            JButton entryButton = new JButton(
                "<html><b>" + date.format(
                    java.time.format.DateTimeFormatter.ofPattern("MMM d")
                ) + "</b><br><font color='#8E8AD9'>"
                + emotion + "</font></html>"
            );

            entryButton.setHorizontalAlignment(
                SwingConstants.LEFT
            );

            entryButton.setFont(
                new Font("Arial", Font.PLAIN, 12)
            );

            entryButton.setForeground(DEEP_PLUM);
            entryButton.setBackground(new Color(255, 255, 255, 130));

            entryButton.setFocusPainted(false);
            entryButton.setBorder(
                BorderFactory.createEmptyBorder(7, 9, 7, 9)
            );

            entryButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );

            entryButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
            );

            entryButton.addActionListener(e -> openJournal(date));

            recentEntriesPanel.add(entryButton);
            recentEntriesPanel.add(Box.createVerticalStrut(7));

            shown++;
        }

        if (shown == 0) {

            JLabel empty = new JLabel("No journal entries yet.");

            empty.setFont(
                new Font("Arial", Font.ITALIC, 11)
            );

            empty.setForeground(MAUVE);

            recentEntriesPanel.add(empty);
        }

        recentEntriesPanel.revalidate();
        recentEntriesPanel.repaint();
    }

    // REFLECTION QUOTES

    private void updateReflection() {

        String[] quotes = {
            "Every day is a new page in your story.",
            "It's okay to feel more than one emotion.",
            "Small steps are still progress.",
            "Give yourself time to grow.",
            "Your feelings deserve to be heard.",
            "You are allowed to have a slow day.",
            "Every feeling is part of your journey."
        };

        int index = LocalDate.now().getDayOfYear()
            % quotes.length;

        quoteLabel.setText("\"" + quotes[index] + "\"");
    }

    // OPEN JOURNAL

    private void openJournal(LocalDate date) {

        selectedDate = date;

        JournalEditor editor = new JournalEditor(date);

        editor.addWindowListener(
            new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    updateCalendar();
                    refreshDashboard();
                }
            }
        );

        editor.setVisible(true);
    }

    // UPDATE CALENDAR

    private void updateCalendar() {

        datesPanel.removeAll();

        monthLabel.setText(
            currentMonth.getMonth().getDisplayName(
                java.time.format.TextStyle.FULL,
                Locale.ENGLISH
            ) + " " + currentMonth.getYear()
        );

        LocalDate firstDay = currentMonth.atDay(1);

        int startingDay =
            firstDay.getDayOfWeek().getValue() % 7;

        int daysInMonth = currentMonth.lengthOfMonth();

        LocalDate today = LocalDate.now();

        for (int i = 0; i < startingDay; i++) {

            JPanel empty = new JPanel();
            empty.setOpaque(false);

            datesPanel.add(empty);
        }

        for (int day = 1; day <= daysInMonth; day++) {

            LocalDate date = currentMonth.atDay(day);

            RoundedDateButton button =
                new RoundedDateButton(String.valueOf(day));

            button.setBackground(
                getEmotionColor(moodEntries.get(date))
            );

            button.setToday(date.equals(today));
            button.setSelectedDate(date.equals(selectedDate));

            String emotion = moodEntries.get(date);

            button.setToolTipText(
                emotion == null
                ? date.toString()
                : date + " • " + emotion
            );

            button.addActionListener(e -> openJournal(date));

            datesPanel.add(button);
        }

        while (datesPanel.getComponentCount() < 42) {

            JPanel empty = new JPanel();
            empty.setOpaque(false);

            datesPanel.add(empty);
        }

        datesPanel.revalidate();
        datesPanel.repaint();
    }

    // EMOTION COLOR LOOKUP

    private Color getEmotionColor(String emotion) {

        if (emotion == null) {
            return BLUSH;
        }

        switch (emotion) {
            case "Happy": return HAPPY_COLOR;
            case "Sad": return SAD_COLOR;
            case "Tired": return TIRED_COLOR;
            case "Envy": return ENVY_COLOR;
            case "Fear": return FEAR_COLOR;
            case "Angry": return ANGRY_COLOR;
            default: return BLUSH;
        }
    }

    // BUTTON STYLING

    private void styleArrowButton(JButton button) {

        button.setFont(
            new Font("Arial", Font.BOLD, 38)
        );

        button.setForeground(PURPLE);
        button.setPreferredSize(new Dimension(48, 48));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );
    }

    // LABEL HELPERS

    private JLabel createTitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        label.setForeground(DEEP_PLUM);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JLabel createSmallLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 9)
        );

        label.setForeground(MAUVE);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JLabel createBigNumber(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        label.setForeground(PURPLE);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JLabel createInfoLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(DEEP_PLUM);

        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    // GLASS CARD

    private class GlassCard extends JPanel {

        public GlassCard() {

            setOpaque(false);

            setBorder(
                BorderFactory.createEmptyBorder(
                    17, 15, 17, 15
                )
            );
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            // SHADOW

            g2.setColor(new Color(70, 45, 90, 25));

            g2.fillRoundRect(
                3, 5,
                width - 7,
                height - 8,
                25, 25
            );

            // GLASS SURFACE

            GradientPaint gradient = new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 245),
                width, height,
                new Color(249, 239, 250, 235)
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                0, 0,
                width - 5,
                height - 7,
                25, 25
            );

            // GLASS BORDER

            g2.setColor(new Color(255, 255, 255, 210));

            g2.setStroke(new BasicStroke(1.5f));

            g2.drawRoundRect(
                1, 1,
                width - 7,
                height - 9,
                25, 25
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // DATE BUTTON

    private class RoundedDateButton extends JButton {

        private boolean today;
        private boolean selectedDate;

        public RoundedDateButton(String text) {

            super(text);

            setFont(
                new Font("Arial", Font.BOLD, 17)
            );

            setForeground(DEEP_PLUM);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );
        }

        public void setToday(boolean value) {
            today = value;
        }

        public void setSelectedDate(boolean value) {
            selectedDate = value;
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            // SHADOW

            g2.setColor(new Color(60, 40, 90, 25));

            g2.fillRoundRect(
                2, 4,
                width - 4,
                height - 6,
                19, 19
            );

            // DATE BACKGROUND

            g2.setColor(getBackground());

            g2.fillRoundRect(
                1, 1,
                width - 3,
                height - 7,
                19, 19
            );

            // HIGHLIGHT

            g2.setColor(new Color(255, 255, 255, 90));

            g2.drawRoundRect(
                2, 2,
                width - 5,
                height - 9,
                18, 18
            );

            // TODAY BORDER

            if (today) {

                g2.setColor(PURPLE);

                g2.setStroke(new BasicStroke(3));

                g2.drawRoundRect(
                    3, 3,
                    width - 7,
                    height - 11,
                    18, 18
                );
            }

            // SELECTED DATE

            if (selectedDate) {

                g2.setColor(DEEP_PLUM);

                g2.setStroke(new BasicStroke(2));

                g2.drawRoundRect(
                    5, 5,
                    width - 11,
                    height - 15,
                    16, 16
                );
            }

            // DATE NUMBER

            g2.setFont(getFont());
            g2.setColor(getForeground());

            FontMetrics fm = g2.getFontMetrics();

            String text = getText();

            int x = (width - fm.stringWidth(text)) / 2;

            int y = (height - fm.getHeight()) / 2
                + fm.getAscent() - 2;

            g2.drawString(text, x, y);

            // TODAY DOT

            if (today) {

                g2.setColor(PURPLE);

                g2.fillOval(
                    width / 2 - 3,
                    height - 17,
                    6, 6
                );
            }

            g2.dispose();
        }
    }

    // MAIN BACKGROUND

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        GradientPaint gradient = new GradientPaint(
            0, 0, BLUSH,
            getWidth(), getHeight(), LAVENDER
        );

        g2.setPaint(gradient);

        g2.fillRect(
            0, 0,
            getWidth(), getHeight()
        );

        g2.dispose();
    }
}
