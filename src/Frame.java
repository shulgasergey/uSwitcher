import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Frame {

    // Создаем переменные для сохранения выбранных языков
    public static String availableLanguage1 = null;
    public static String availableLanguage2 = null;
    private static TrayIcon trayIconObj;
    public static boolean runningProgramm = true; // Флаг работы программы

    public void frameStarter(String[] availableLanguages) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createTrayWindow(availableLanguages);
            }
        });
    }

    private static void createTrayWindow(String[] availableLanguages) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported on this platform.");
            return;
        }

        // Создаем выпадающее меню для трея
        PopupMenu popup = new PopupMenu();

        MenuItem button1Item = new MenuItem("Обрати мови для зміни");
        button1Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Доступні мови");
                frame.setSize(300, 200);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);

                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.anchor = GridBagConstraints.CENTER;
                gbc.insets = new Insets(5, 5, 5, 5);

                Font font = new Font("Tahoma", Font.PLAIN, 14);

                final int[] selectedCount = {0};

                for (String language : availableLanguages) {
                    JCheckBox checkBox = new JCheckBox(language);
                    checkBox.setFont(font);
                    gbc.gridy++;
                    panel.add(checkBox, gbc);

                    checkBox.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (checkBox.isSelected()) {
                                if (selectedCount[0] == 0) {
                                    availableLanguage1 = checkBox.getText();
                                } else if (selectedCount[0] == 1) {
                                    availableLanguage2 = checkBox.getText();
                                }
                                selectedCount[0]++;
                            } else {
                                if (selectedCount[0] == 1 && checkBox.getText().equals(availableLanguage1)) {
                                    availableLanguage1 = null;
                                } else if (selectedCount[0] == 1 && checkBox.getText().equals(availableLanguage2)) {
                                    availableLanguage2 = null;
                                } else if (selectedCount[0] == 2 && checkBox.getText().equals(availableLanguage1)) {
                                    availableLanguage1 = availableLanguage2;
                                    availableLanguage2 = null;
                                }
                                selectedCount[0]--;
                            }
                        }
                    });
                }

                JButton okButton = new JButton("Зберегти");
                okButton.setFont(font);
                okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (selectedCount[0] != 2) {
                            JOptionPane.showMessageDialog(null, "Будь-ласка, оберіть дві мови");
                        } else {
                            JOptionPane.showMessageDialog(null, "Обрані мови: " + availableLanguage1 + ", " + availableLanguage2);
                            System.out.println("Available Language 1: " + availableLanguage1);
                            System.out.println("Available Language 2: " + availableLanguage2);
                        }
                        frame.dispose();
                    }
                });

                gbc.gridy++;
                panel.add(okButton, gbc);

                frame.add(panel);
                frame.setVisible(true);
            }
        });
        popup.add(button1Item);

        // -/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-
        // Запуск ТУТ
        MenuItem button2Item = new MenuItem("Запуск програми");
        button2Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Program has been started");
                KeyboardListener keyboardListener = new KeyboardListener();
                keyboardListener.mainListener();

            }
        });
        popup.add(button2Item);

        MenuItem pauseItem = new MenuItem("Пауза");
        pauseItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Действия при нажатии кнопки "Пауза"
                System.out.println("Program paused");
                runningProgramm = false;
            }
        });
        popup.add(pauseItem);

        MenuItem resumeItem = new MenuItem("Продовжити");
        resumeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Действия при нажатии кнопки "Продолжить"
                System.out.println("Program resumed");
                runningProgramm = true;
            }
        });
        popup.add(resumeItem);

        MenuItem button3Item = new MenuItem("Зв'язок з розробником");
        button3Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://t.me/userfromworld"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        popup.add(button3Item);

        MenuItem button4Item = new MenuItem("Інструкції з використання");
        button4Item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Создаем окошко с текстом инструкций
                JFrame instructionsFrame = new JFrame("Інструкції");
                instructionsFrame.setSize(400, 300);
                instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                instructionsFrame.setLocationRelativeTo(null);
                instructionsFrame.setResizable(false);

                JTextArea instructionsText = new JTextArea();
                instructionsText.setFont(new Font("Verdana", Font.PLAIN, 14));
                instructionsText.setEditable(false);
                instructionsText.setLineWrap(true);
                instructionsText.setWrapStyleWord(true);
                instructionsText.setText("Для запуску програми натисніть кнопку \"Запуск\"\n\n"
                        + "Якщо Вам потрібно на певний час зупинити програму, натисніть клавішу \"0\"\n\n"
                        + "Якщо Вам потрібно відновити роботу програми, натисніть клавішу \"1\"\n\n"
                        + "Для виходу з програми натисніть кнопку \"Вихід\"\n\n"
                        + "Дякую за використання uSwitcher. Все буде Україна!");

                instructionsText.setBorder(new EmptyBorder(10, 10, 10, 10)); // Добавляем отступы текста от краев окна

                instructionsFrame.add(instructionsText);
                instructionsFrame.setVisible(true);
            }
        });
        popup.add(button4Item);

        MenuItem exitItem = new MenuItem("Вихід");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SystemTray.getSystemTray().remove(trayIconObj);
                System.exit(0);
            }
        });
        popup.add(exitItem);

        // Создаем иконку для трея
        Image trayIcon = Toolkit.getDefaultToolkit().getImage("uSwitcherLogo.png");

        // Создаем трей
        SystemTray tray = SystemTray.getSystemTray();
        trayIconObj = new TrayIcon(trayIcon, "uSwitcher", popup);

        try {
            tray.add(trayIconObj);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }
}