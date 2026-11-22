
package com.mycompany.doornal;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;

public class MainFrameD extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainContent;

    // DOORNAL COLOR PALETTE

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

    private final Color MAUVE =
            new Color(0xC1, 0x89, 0xAF);


    // CONSTRUCTOR

    public MainFrameD() {

        setTitle("Doornal - " + CurrentUser.username);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // MAIN CARD LAYOUT

        cardLayout = new CardLayout();

        mainContent = new JPanel(cardLayout);

        mainContent.setOpaque(false);


        // ACCOUNT PAGE

        AccountSettingsPanel accountPanel =
                new AccountSettingsPanel();


        // JOURNAL PAGE

        JPanel journalPanel =
                createJournalPanel();


        // LETTERS PAGE

        LettersPanel lettersPanel =
                new LettersPanel();


        // MOOD INSIGHTS / STATISTICS PAGE

        MoodInsightsPanel insightsPanel =
                new MoodInsightsPanel();


        // REGISTER ALL PAGES

        mainContent.add(accountPanel, "account");

        mainContent.add(journalPanel, "journal");

        mainContent.add(lettersPanel, "letters");

        mainContent.add(insightsPanel, "insights");


        // SIDEBAR

        SideBar sidebar = new SideBar();


        // ACCOUNT NAVIGATION

        sidebar.getAccountButton().addActionListener(e -> {

            cardLayout.show(mainContent, "account");

            sidebar.setActivePage("account");

        });


        // JOURNAL NAVIGATION

        sidebar.getJournalButton().addActionListener(e -> {

            cardLayout.show(mainContent, "journal");

            sidebar.setActivePage("journal");

        });


        // LETTERS NAVIGATION

        sidebar.getLettersButton().addActionListener(e -> {

            cardLayout.show(mainContent, "letters");

            sidebar.setActivePage("letters");

        });


        // MOOD INSIGHTS NAVIGATION

        sidebar.getInsightsButton().addActionListener(e -> {

            cardLayout.show(mainContent, "insights");

            sidebar.setActivePage("insights");

            // Refresh actual saved emotion records
            insightsPanel.refreshStats();

        });


        // FRAME LAYOUT

        add(sidebar, BorderLayout.WEST);

        add(mainContent, BorderLayout.CENTER);


        // DEFAULT PAGE

        cardLayout.show(mainContent, "journal");

        sidebar.setActivePage("journal");


        // WINDOW SETTINGS

        setMinimumSize(new Dimension(1100, 750));

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setLocationRelativeTo(null);

        setVisible(true);
    }


    // CREATE JOURNAL DASHBOARD

    private JPanel createJournalPanel() {

        JPanel panel = new JPanel(new GridBagLayout()) {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 =
                        (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );

                int width = getWidth();

                int height = getHeight();


                // MAIN PASTEL GRADIENT

                GradientPaint mainGradient =
                        new GradientPaint(
                                0, 0,
                                BLUSH,
                                width, height,
                                LAVENDER
                        );

                g2.setPaint(mainGradient);

                g2.fillRect(
                        0, 0,
                        width,
                        height
                );


                // BOTTOM GRADIENT

                int startY =
                        (int) (height * 0.78);

                GradientPaint bottomGradient =
                        new GradientPaint(
                                0, startY,
                                PEACH,
                                width, height,
                                PINK_LAVENDER
                        );

                g2.setPaint(bottomGradient);

                g2.fillRect(
                        0,
                        startY,
                        width,
                        height - startY
                );


                // TOP RIGHT GLOW

                g2.setColor(
                        new Color(
                                PERIWINKLE.getRed(),
                                PERIWINKLE.getGreen(),
                                PERIWINKLE.getBlue(),
                                55
                        )
                );

                g2.fill(
                        new Ellipse2D.Float(
                                width - 250,
                                30,
                                220,
                                220
                        )
                );


                // BOTTOM LEFT GLOW

                g2.setColor(
                        new Color(
                                PEACH.getRed(),
                                PEACH.getGreen(),
                                PEACH.getBlue(),
                                90
                        )
                );

                g2.fill(
                        new Ellipse2D.Float(
                                -100,
                                height - 180,
                                300,
                                250
                        )
                );


                // ADDITIONAL SOFT GLOW

                g2.setColor(
                        new Color(
                                MAUVE.getRed(),
                                MAUVE.getGreen(),
                                MAUVE.getBlue(),
                                30
                        )
                );

                g2.fill(
                        new Ellipse2D.Float(
                                width / 2 - 150,
                                height - 100,
                                300,
                                180
                        )
                );


                g2.dispose();
            }
        };


        panel.setOpaque(false);


        // CALENDAR DASHBOARD

        CalendarPanel calendar =
                new CalendarPanel();


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;

        gbc.gridy = 0;

        gbc.weightx = 1.0;

        gbc.weighty = 1.0;

        gbc.anchor = GridBagConstraints.CENTER;

        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets =
                new Insets(10, 10, 10, 10);


        panel.add(calendar, gbc);


        return panel;
    }
}
