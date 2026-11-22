
package com.mycompany.doornal;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.IOException;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private JLabel streakLabel;

    private JPanel weeklyChartPanel;
    private JPanel recentEntriesPanel;
    private JLabel quoteLabel;

    // NEW FEATURES
    private MoodDistributionPanel moodDistributionPanel;
    private JLabel previewDateLabel;
    private JLabel previewMoodLabel;
    private JLabel previewTextLabel;

    private Map<LocalDate, String> moodEntries = new HashMap<>();

    // =========================================
    // DOORNAL GLASS COLOR PALETTE
    // =========================================

    private final Color BLUSH = new Color(0xFC, 0xE1, 0xDD);
    private final Color PEACH = new Color(0xFC, 0xD3, 0xBF);
    private final Color LAVENDER = new Color(0xD2, 0xC5, 0xF0);
    private final Color PERIWINKLE = new Color(0xC0, 0xB4, 0xEF);
    private final Color PURPLE = new Color(0x8E, 0x8A, 0xD9);
    private final Color MAUVE = new Color(0xC1, 0x89, 0xAF);
    private final Color DEEP_PLUM = new Color(0x29, 0x22, 0x3F);
    private final Color WHITE = new Color(255, 252, 252);

    private final Color CYAN = new Color(116, 235, 232);
    private final Color LIGHT_CYAN = new Color(198, 255, 250);

    // =========================================
    // EMOTION COLORS
    // =========================================

    private final Color HAPPY_COLOR = new Color(255, 224, 145);
    private final Color SAD_COLOR = new Color(166, 205, 244);
    private final Color TIRED_COLOR = new Color(205, 205, 215);
    private final Color ENVY_COLOR = new Color(174, 218, 180);
    private final Color FEAR_COLOR = new Color(202, 184, 232);
    private final Color ANGRY_COLOR = new Color(244, 174, 177);

    private final String[] emotions = {
        "Happy", "Sad", "Tired", "Envy", "Fear", "Angry"
    };

    // =========================================
    // CONSTRUCTOR
    // =========================================

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

    // =========================================
    // MAIN DASHBOARD
    // =========================================

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

    // =========================================
    // MONTHLY SUMMARY
    // =========================================

    private JPanel createMonthlySummary() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(205, 255));
        card.setMaximumSize(new Dimension(205, 255));

        totalEntriesLabel = createBigNumber("0");
        mostFrequentLabel = createInfoLabel("No entries yet");
        daysTrackedLabel = createInfoLabel("0 days");

        // NEW: STREAK LABEL
        streakLabel = createInfoLabel("0 days");

        card.add(createTitle("Monthly Summary"));
        card.add(Box.createVerticalStrut(17));

        card.add(createSmallLabel("TOTAL ENTRIES"));
        card.add(totalEntriesLabel);

        card.add(Box.createVerticalStrut(12));

        card.add(createSmallLabel("MOST FREQUENT MOOD"));
        card.add(mostFrequentLabel);

        card.add(Box.createVerticalStrut(12));

        card.add(createSmallLabel("DAYS TRACKED"));
        card.add(daysTrackedLabel);

        // NEW: JOURNALING STREAK
        card.add(Box.createVerticalStrut(12));
        card.add(createSmallLabel("CURRENT STREAK"));
        card.add(streakLabel);

        return card;
    }

    // =========================================
    // EMOTION LEGEND + MOOD DISTRIBUTION
    // =========================================

    private JPanel createEmotionLegend() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(205, 390));
        card.setMaximumSize(new Dimension(205, 390));

        card.add(createTitle("Emotion Legend"));
        card.add(Box.createVerticalStrut(12));

        addLegendItem(card, "Happy", HAPPY_COLOR);
        addLegendItem(card, "Sad", SAD_COLOR);
        addLegendItem(card, "Tired", TIRED_COLOR);
        addLegendItem(card, "Envy", ENVY_COLOR);
        addLegendItem(card, "Fear", FEAR_COLOR);
        addLegendItem(card, "Angry", ANGRY_COLOR);

        // NEW: DONUT CHART TITLE
        card.add(Box.createVerticalStrut(10));
        card.add(createTitle("Mood Distribution"));
        card.add(Box.createVerticalStrut(5));

        // NEW: DONUT CHART
        moodDistributionPanel = new MoodDistributionPanel();

        moodDistributionPanel.setPreferredSize(
            new Dimension(170, 125)
        );

        moodDistributionPanel.setMaximumSize(
            new Dimension(170, 125)
        );

        moodDistributionPanel.setAlignmentX(
            Component.LEFT_ALIGNMENT
        );

        card.add(moodDistributionPanel);

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

        JPanel dot = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(color);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                g2.setColor(new Color(255, 255, 255, 220));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);

                g2.dispose();
            }
        };

        dot.setOpaque(false);
        dot.setPreferredSize(new Dimension(14, 14));

        JLabel label = new JLabel(name);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setForeground(DEEP_PLUM);

        item.add(dot);
        item.add(label);

        panel.add(item);
    }

    // =========================================
    // WEEKLY OVERVIEW
    // =========================================

    private JPanel createWeeklyOverview() {

        GlassCard card = new GlassCard();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setPreferredSize(new Dimension(215, 285));
        card.setMaximumSize(new Dimension(215, 285));

        card.add(createTitle("Weekly Overview"));
        card.add(Box.createVerticalStrut(5));
        card.add(createSmallLabel("THIS WEEK"));
        card.add(Box.createVerticalStrut(15));

        weeklyChartPanel = new JPanel();

        weeklyChartPanel.setLayout(
            new BoxLayout(weeklyChartPanel, BoxLayout.Y_AXIS)
        );

        weeklyChartPanel.setOpaque(false);

        card.add(weeklyChartPanel);

        return card;
    }

    // =========================================
    // RECENT ENTRIES
    // =========================================

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

    // =========================================
    // MAIN CALENDAR CARD
    // =========================================

    private JPanel createCalendarCard() {

        GlassCard card = new GlassCard();

        card.setLayout(new BorderLayout(12, 12));

        // HEADER

        JPanel header = new JPanel(
            new BorderLayout(12, 0)
        );

        header.setOpaque(false);

        JButton previousButton = new GlassArrowButton("‹");
        JButton nextButton = new GlassArrowButton("›");

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

        // CALENDAR CONTENT

        JPanel calendarContent = new JPanel(
            new BorderLayout(0, 17)
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

        // NEW: SELECTED DATE PREVIEW
        JPanel previewPanel = createSelectedDatePreview();

        card.add(previewPanel, BorderLayout.SOUTH);

        return card;
    }

    // =========================================
    // NEW: SELECTED DATE PREVIEW
    // =========================================

    private JPanel createSelectedDatePreview() {

        JPanel preview = new JPanel(
            new BorderLayout(10, 3)
        );

        preview.setOpaque(false);

        preview.setBorder(
            BorderFactory.createCompoundBorder(
                new MatteBorder(
                    1, 0, 0, 0,
                    new Color(255, 255, 255, 190)
                ),
                BorderFactory.createEmptyBorder(
                    9, 5, 2, 5
                )
            )
        );

        JPanel left = new JPanel();

        left.setOpaque(false);

        left.setLayout(
            new BoxLayout(left, BoxLayout.Y_AXIS)
        );

        previewDateLabel = new JLabel("Selected Date");

        previewDateLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        previewDateLabel.setForeground(DEEP_PLUM);

        previewMoodLabel = new JLabel("No mood selected");

        previewMoodLabel.setFont(
            new Font("Arial", Font.BOLD, 11)
        );

        previewMoodLabel.setForeground(PURPLE);

        previewTextLabel = new JLabel(
            "Select a date to preview your journal."
        );

        previewTextLabel.setFont(
            new Font("Arial", Font.ITALIC, 11)
        );

        previewTextLabel.setForeground(
            new Color(91, 76, 112)
        );

        left.add(previewDateLabel);
        left.add(Box.createVerticalStrut(3));
        left.add(previewMoodLabel);

        preview.add(left, BorderLayout.WEST);
        preview.add(previewTextLabel, BorderLayout.CENTER);

        return preview;
    }

    // =========================================
    // UPDATE SELECTED DATE PREVIEW
    // =========================================

    private void updateSelectedDatePreview() {

        if (selectedDate == null ||
            previewDateLabel == null ||
            previewMoodLabel == null ||
            previewTextLabel == null) {

            return;
        }

        previewDateLabel.setText(
            selectedDate.format(
                DateTimeFormatter.ofPattern("MMMM dd, yyyy")
            )
        );

        String emotion = moodEntries.get(selectedDate);

        if (emotion == null) {

            previewMoodLabel.setText("No mood selected");

        } else {

            previewMoodLabel.setText("Mood: " + emotion);
            previewMoodLabel.setForeground(
                getEmotionColor(emotion).darker()
            );
        }

        String username = CurrentUser.username;

        if (username == null || username.trim().isEmpty()) {

            previewTextLabel.setText(
                "No journal preview available."
            );

            return;
        }

        Path journalFile = Path.of(
            System.getProperty("user.home"),
            "Doornal",
            username,
            "Journals",
            selectedDate + ".txt"
        );

        try {

            if (Files.exists(journalFile)) {

                String content = Files.readString(
                    journalFile
                ).trim();

                content = content.replaceAll("\\s+", " ");

                if (content.isEmpty()) {

                    previewTextLabel.setText(
                        "Your journal is currently empty."
                    );

                } else {

                    if (content.length() > 100) {
                        content = content.substring(0, 97) + "...";
                    }

                    previewTextLabel.setText(content);
                }

            } else {

                previewTextLabel.setText(
                    "No journal entry for this date yet."
                );
            }

        } catch (IOException e) {

            previewTextLabel.setText(
                "Unable to load journal preview."
            );
        }
    }

    // =========================================
    // DAILY REFLECTION
    // =========================================

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

    // =========================================
    // REFRESH DASHBOARD DATA
    // =========================================

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

        // NEW: UPDATE STREAK
        updateStreak();

        // NEW: UPDATE DONUT CHART
        if (moodDistributionPanel != null) {
            moodDistributionPanel.setCounts(counts, total);
        }

        updateWeeklyChart();
        updateRecentEntries();
        updateReflection();

        // NEW: UPDATE SELECTED DATE PREVIEW
        updateSelectedDatePreview();

        revalidate();
        repaint();
    }

    // =========================================
    // NEW: JOURNALING STREAK TRACKER
    // =========================================

    private void updateStreak() {

        if (streakLabel == null) {
            return;
        }

        int streak = calculateCurrentStreak();

        streakLabel.setText(
            streak + (streak == 1 ? " day" : " days")
        );
    }

    private int calculateCurrentStreak() {

        LocalDate date = LocalDate.now();

        // If today has no mood entry, allow the streak
        // to continue from yesterday.
        if (!moodEntries.containsKey(date)) {
            date = date.minusDays(1);
        }

        int streak = 0;

        while (moodEntries.containsKey(date)) {

            streak++;

            date = date.minusDays(1);
        }

        return streak;
    }

    // =========================================
    // LOAD SAVED EMOTIONS
    // =========================================

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
                    // Skip invalid files.
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // WEEKLY CHART
    // =========================================

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

                    Graphics2D g2 = (Graphics2D) g.create();

                    g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                    );

                    int width = getWidth();
                    int height = getHeight();

                    g2.setColor(
                        new Color(225, 218, 240, 180)
                    );

                    g2.fillRoundRect(
                        0, 0, width, height, 12, 12
                    );

                    if (count > 0) {

                        int fillWidth = Math.max(
                            5,
                            (int) (width * count / 7.0)
                        );

                        Color emotionColor =
                            getEmotionColor(emotion);

                        GradientPaint gradient = new GradientPaint(
                            0, 0,
                            emotionColor.brighter(),
                            fillWidth, height,
                            emotionColor
                        );

                        g2.setPaint(gradient);

                        g2.fillRoundRect(
                            0, 0,
                            fillWidth,
                            height,
                            12, 12
                        );

                        g2.setColor(
                            new Color(255, 255, 255, 150)
                        );

                        g2.drawRoundRect(
                            1, 1,
                            Math.max(1, fillWidth - 2),
                            height - 3,
                            12, 12
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

    // =========================================
    // RECENT JOURNAL ENTRIES
    // =========================================

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

            JButton entryButton = new GlassEntryButton(
                date.format(
                    DateTimeFormatter.ofPattern("MMM d")
                ),
                emotion,
                getEmotionColor(emotion)
            );

            entryButton.addActionListener(e -> openJournal(date));

            entryButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48)
            );

            entryButton.setAlignmentX(Component.LEFT_ALIGNMENT);

            recentEntriesPanel.add(entryButton);
            recentEntriesPanel.add(Box.createVerticalStrut(8));

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

    // =========================================
    // REFLECTION QUOTES
    // =========================================

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

    // =========================================
    // OPEN JOURNAL
    // =========================================

    private void openJournal(LocalDate date) {

        selectedDate = date;

        // Update preview before opening the editor.
        updateCalendar();
        updateSelectedDatePreview();

        JournalEditor editor = new JournalEditor(date);

        editor.addWindowListener(
            new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {
                    loadMoodEntries();
                    updateCalendar();
                    refreshDashboard();
                }
            }
        );

        editor.setVisible(true);
    }

    // =========================================
    // UPDATE CALENDAR
    // =========================================

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

            button.setHasMood(
                moodEntries.containsKey(date)
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

    // =========================================
    // EMOTION COLOR LOOKUP
    // =========================================

    private Color getEmotionColor(String emotion) {

        if (emotion == null) {
            return new Color(255, 255, 255, 175);
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

    // =========================================
    // LABEL HELPERS
    // =========================================

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

    // =========================================
    // NEW: MOOD DISTRIBUTION DONUT CHART
    // =========================================

    private class MoodDistributionPanel extends JPanel {

        private Map<String, Integer> counts = new HashMap<>();
        private int total = 0;

        public MoodDistributionPanel() {
            setOpaque(false);

            for (String emotion : emotions) {
                counts.put(emotion, 0);
            }
        }

        public void setCounts(
            Map<String, Integer> newCounts,
            int newTotal
        ) {

            counts = new HashMap<>(newCounts);
            total = newTotal;

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

            int size = Math.min(
                getHeight() - 8,
                getWidth() - 35
            );

            if (size <= 0) {
                g2.dispose();
                return;
            }

            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            double startAngle = 90;

            if (total == 0) {

                g2.setColor(
                    new Color(225, 218, 240, 210)
                );

                g2.fill(new Ellipse2D.Double(
                    x, y, size, size
                ));

            } else {

                for (String emotion : emotions) {

                    int count = counts.getOrDefault(
                        emotion, 0
                    );

                    if (count <= 0) {
                        continue;
                    }

                    double extent =
                        count / (double) total * 360.0;

                    g2.setColor(getEmotionColor(emotion));

                    g2.fill(
                        new Arc2D.Double(
                            x, y,
                            size, size,
                            startAngle,
                            -extent,
                            Arc2D.PIE
                        )
                    );

                    startAngle -= extent;
                }
            }

            // CENTER HOLE

            int holeSize = (int) (size * 0.57);

            int holeX = x + (size - holeSize) / 2;
            int holeY = y + (size - holeSize) / 2;

            g2.setColor(new Color(255, 252, 252, 245));

            g2.fillOval(
                holeX,
                holeY,
                holeSize,
                holeSize
            );

            // CENTER TEXT

            String totalText = String.valueOf(total);

            g2.setFont(
                new Font("Arial", Font.BOLD, 18)
            );

            g2.setColor(DEEP_PLUM);

            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(
                totalText,
                getWidth() / 2 - fm.stringWidth(totalText) / 2,
                getHeight() / 2 + 2
            );

            g2.setFont(
                new Font("Arial", Font.PLAIN, 9)
            );

            g2.setColor(MAUVE);

            String subtitle = "entries";

            fm = g2.getFontMetrics();

            g2.drawString(
                subtitle,
                getWidth() / 2 - fm.stringWidth(subtitle) / 2,
                getHeight() / 2 + 16
            );

            g2.dispose();
        }
    }

    // =========================================
    // GLASS CARD
    // =========================================

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

            // SOFT SHADOW

            g2.setColor(new Color(55, 42, 86, 35));

            g2.fillRoundRect(
                3, 6,
                width - 7,
                height - 9,
                30, 30
            );

            // OUTER GLOW

            g2.setColor(new Color(194, 176, 239, 25));

            g2.setStroke(new BasicStroke(5f));

            g2.drawRoundRect(
                2, 2,
                width - 7,
                height - 9,
                30, 30
            );

            // GLASS GRADIENT

            GradientPaint glass = new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 205),
                width, height,
                new Color(230, 220, 246, 155)
            );

            g2.setPaint(glass);

            g2.fillRoundRect(
                0, 0,
                width - 6,
                height - 8,
                29, 29
            );

            // TOP GLASS HIGHLIGHT

            GradientPaint highlight = new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 115),
                0, height / 2,
                new Color(255, 255, 255, 0)
            );

            g2.setPaint(highlight);

            g2.fillRoundRect(
                3, 3,
                width - 12,
                height / 2,
                25, 25
            );

            // OUTER WHITE BORDER

            g2.setColor(new Color(255, 255, 255, 220));

            g2.setStroke(new BasicStroke(1.7f));

            g2.drawRoundRect(
                1, 1,
                width - 8,
                height - 10,
                29, 29
            );

            // INNER LAVENDER BORDER

            g2.setColor(new Color(194, 176, 239, 100));

            g2.setStroke(new BasicStroke(1f));

            g2.drawRoundRect(
                4, 4,
                width - 14,
                height - 16,
                25, 25
            );

            // SMALL TOP REFLECTION

            g2.setColor(new Color(255, 255, 255, 115));

            g2.setStroke(
                new BasicStroke(
                    2f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
                )
            );

            g2.drawArc(
                15, 5,
                width - 35,
                28,
                10,
                150
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    // =========================================
    // GLASS ARROW BUTTON
    // =========================================

    private class GlassArrowButton extends JButton {

        private boolean hovering = false;
        private boolean pressed = false;

        public GlassArrowButton(String text) {

            super(text);

            setFont(new Font("Arial", Font.BOLD, 32));
            setForeground(PURPLE);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );

            setPreferredSize(new Dimension(49, 45));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovering = false;
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

            int inset = pressed ? 3 : 1;

            // SHADOW

            g2.setColor(new Color(69, 48, 100, 35));

            g2.fillRoundRect(
                inset + 1,
                inset + 3,
                w - 7,
                h - 7,
                22, 22
            );

            // GLASS BUTTON

            GradientPaint gradient = new GradientPaint(
                0, 0,
                hovering
                    ? new Color(255, 255, 255, 245)
                    : new Color(255, 255, 255, 205),
                w, h,
                hovering
                    ? new Color(207, 194, 245, 230)
                    : new Color(229, 219, 247, 180)
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                inset,
                inset,
                w - 7,
                h - 8,
                22, 22
            );

            // BORDER

            g2.setColor(
                hovering
                    ? new Color(255, 255, 255, 255)
                    : new Color(255, 255, 255, 205)
            );

            g2.setStroke(new BasicStroke(1.6f));

            g2.drawRoundRect(
                inset,
                inset,
                w - 7,
                h - 8,
                22, 22
            );

            // ARROW

            g2.setFont(getFont());
            g2.setColor(PURPLE);

            FontMetrics fm = g2.getFontMetrics();

            String text = getText();

            int x = (w - fm.stringWidth(text)) / 2;
            int y = (h - fm.getHeight()) / 2 + fm.getAscent() - 2;

            g2.drawString(text, x, y);

            g2.dispose();
        }
    }

    // =========================================
    // GLASS RECENT ENTRY BUTTON
    // =========================================

    private class GlassEntryButton extends JButton {

        private final String dateText;
        private final String emotion;
        private final Color emotionColor;

        private boolean hovering = false;

        public GlassEntryButton(
            String dateText,
            String emotion,
            Color emotionColor
        ) {

            this.dateText = dateText;
            this.emotion = emotion;
            this.emotionColor = emotionColor;

            setText("");
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);

            setCursor(
                new Cursor(Cursor.HAND_CURSOR)
            );

            setPreferredSize(new Dimension(180, 48));

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovering = false;
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

            // SHADOW

            g2.setColor(new Color(65, 45, 90, 25));

            g2.fillRoundRect(
                2, 4,
                w - 5,
                h - 6,
                18, 18
            );

            // GLASS SURFACE

            GradientPaint gradient = new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 225),
                w, h,
                new Color(226, 216, 244, 175)
            );

            g2.setPaint(gradient);

            g2.fillRoundRect(
                0, 0,
                w - 5,
                h - 7,
                18, 18
            );

            // EMOTION ACCENT

            g2.setColor(emotionColor);

            g2.fillRoundRect(
                5, 9,
                5,
                h - 25,
                8, 8
            );

            // BORDER

            g2.setColor(new Color(255, 255, 255, 220));

            g2.setStroke(new BasicStroke(1.3f));

            g2.drawRoundRect(
                1, 1,
                w - 7,
                h - 9,
                18, 18
            );

            // TEXT

            g2.setColor(DEEP_PLUM);

            g2.setFont(
                new Font("Arial", Font.BOLD, 12)
            );

            g2.drawString(dateText, 17, 20);

            g2.setFont(
                new Font("Arial", Font.PLAIN, 11)
            );

            g2.setColor(PURPLE);

            g2.drawString(emotion, 17, 36);

            // HOVER HIGHLIGHT

            if (hovering) {

                g2.setColor(
                    new Color(255, 255, 255, 75)
                );

                g2.fillRoundRect(
                    2, 2,
                    w - 9,
                    h / 2,
                    16, 16
                );
            }

            g2.dispose();
        }
    }

    // =========================================
    // ROUNDED GLASS DATE BUTTON
    // =========================================

    private class RoundedDateButton extends JButton {

        private boolean today;
        private boolean selectedDate;
        private boolean hovering = false;
        private boolean pressed = false;

        // NEW: INDICATES WHETHER THE DATE HAS A MOOD
        private boolean hasMood = false;

        private Color moodColor = new Color(
            255, 255, 255, 180
        );

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

            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    hovering = false;
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
        public void setBackground(Color color) {
            super.setBackground(color);

            if (color != null) {
                moodColor = color;
            }
        }

        public void setToday(boolean value) {
            today = value;
        }

        public void setSelectedDate(boolean value) {
            selectedDate = value;
        }

        // NEW: SET MOOD INDICATOR
        public void setHasMood(boolean value) {
            hasMood = value;
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

            int inset = pressed ? 4 : 1;

            // =================================
            // SELECTED DATE PURPLE GLOW
            // =================================

            if (selectedDate) {

                g2.setColor(
                    new Color(142, 138, 217, 65)
                );

                g2.setStroke(new BasicStroke(6f));

                g2.drawRoundRect(
                    1, 1,
                    width - 5,
                    height - 7,
                    21, 21
                );
            }

            // =================================
            // SOFT SHADOW
            // =================================

            g2.setColor(
                new Color(59, 43, 88, hovering ? 45 : 28)
            );

            g2.fillRoundRect(
                inset + 2,
                inset + 5,
                width - 7,
                height - 10,
                20, 20
            );

            // =================================
            // SELECTED DATE GRADIENT
            // =================================

            if (selectedDate) {

                GradientPaint selectedGradient =
                    new GradientPaint(
                        0, 0,
                        new Color(173, 127, 255),
                        width, height,
                        new Color(115, 92, 220)
                    );

                g2.setPaint(selectedGradient);

            } else {

                Color base = moodColor;

                if (hovering) {
                    base = new Color(
                        Math.min(255, base.getRed() + 18),
                        Math.min(255, base.getGreen() + 18),
                        Math.min(255, base.getBlue() + 18),
                        base.getAlpha()
                    );
                }

                GradientPaint glass = new GradientPaint(
                    0, 0,
                    new Color(
                        Math.min(255, base.getRed() + 25),
                        Math.min(255, base.getGreen() + 25),
                        Math.min(255, base.getBlue() + 25),
                        235
                    ),
                    width, height,
                    new Color(
                        base.getRed(),
                        base.getGreen(),
                        base.getBlue(),
                        190
                    )
                );

                g2.setPaint(glass);
            }

            g2.fillRoundRect(
                inset,
                inset,
                width - 6,
                height - 10,
                20, 20
            );

            // =================================
            // TOP GLASS REFLECTION
            // =================================

            GradientPaint highlight = new GradientPaint(
                0, 0,
                new Color(255, 255, 255, 135),
                0, height / 2,
                new Color(255, 255, 255, 0)
            );

            g2.setPaint(highlight);

            g2.fillRoundRect(
                inset + 2,
                inset + 2,
                width - 10,
                height / 2,
                17, 17
            );

            // =================================
            // WHITE GLASS BORDER
            // =================================

            g2.setColor(
                selectedDate
                    ? new Color(255, 255, 255, 235)
                    : new Color(255, 255, 255, 225)
            );

            g2.setStroke(new BasicStroke(1.6f));

            g2.drawRoundRect(
                inset,
                inset,
                width - 6,
                height - 10,
                20, 20
            );

            // =================================
            // TODAY CYAN/PURPLE ACCENT
            // =================================

            if (today && !selectedDate) {

                g2.setColor(CYAN);

                g2.setStroke(new BasicStroke(2.2f));

                g2.drawRoundRect(
                    3, 3,
                    width - 10,
                    height - 14,
                    17, 17
                );
            }

            // =================================
            // DATE NUMBER
            // =================================

            g2.setFont(getFont());

            g2.setColor(
                selectedDate
                    ? Color.WHITE
                    : DEEP_PLUM
            );

            FontMetrics fm = g2.getFontMetrics();

            String text = getText();

            int x = (width - fm.stringWidth(text)) / 2;

            int y = (height - fm.getHeight()) / 2
                + fm.getAscent() - 3;

            g2.drawString(text, x, y);

            // =================================
            // TODAY INDICATOR
            // =================================

            if (today) {

                g2.setColor(
                    selectedDate ? Color.WHITE : PURPLE
                );

                g2.fillOval(
                    width / 2 - 3,
                    height - 18,
                    6, 6
                );
            }

            // =================================
            // NEW: MOOD INDICATOR DOT
            // =================================

            if (hasMood) {

                g2.setColor(
                    selectedDate
                        ? Color.WHITE
                        : getEmotionDotColor()
                );

                g2.fillOval(
                    width / 2 - 2,
                    height - 10,
                    5, 5
                );

                g2.setColor(
                    new Color(255, 255, 255, 200)
                );

                g2.setStroke(new BasicStroke(0.8f));

                g2.drawOval(
                    width / 2 - 2,
                    height - 10,
                    5, 5
                );
            }

            g2.dispose();
        }

        private Color getEmotionDotColor() {
            return moodColor.darker();
        }
    }

    // =========================================
    // BACKGROUND
    // =========================================

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
            0, 0,
            new Color(255, 239, 237),
            width, height,
            new Color(205, 194, 239)
        );

        g2.setPaint(gradient);

        g2.fillRect(
            0, 0,
            width, height
        );

        // SOFT PINK AMBIENT LIGHT

        g2.setColor(new Color(255, 255, 255, 30));

        g2.fillOval(
            -100,
            -100,
            350,
            350
        );

        // LAVENDER AMBIENT LIGHT

        g2.setColor(new Color(174, 157, 231, 25));

        g2.fillOval(
            width - 300,
            height - 300,
            400,
            400
        );

        g2.dispose();
    }
}
