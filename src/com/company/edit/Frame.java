package com.company.edit;
import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 760;
    private static final int MENU_PANEL_WIDTH = 220;
    private static final int PANEL_HEIGHT = 90;

    private FileManager fileManager;
    private Editor editor;
    private JPanel menuPanel;
    private JPanel panel;


    public Frame() {
        //frame
        this.setTitle("Проверка двудольности графа");
        this.setSize(WIDTH, HEIGHT);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //elements
        panel();
        menuPanel.setBounds(WIDTH - MENU_PANEL_WIDTH, 0, MENU_PANEL_WIDTH, this.getHeight());
        panel.setBounds(0, HEIGHT - PANEL_HEIGHT, WIDTH - MENU_PANEL_WIDTH, PANEL_HEIGHT );
        editor = new Editor();
        editor.setLayout(null);
        editor.setBounds(0, 0, WIDTH - MENU_PANEL_WIDTH, HEIGHT - PANEL_HEIGHT);
        this.add(editor);
        this.add(menuPanel);
        this.add(panel);
        fileManager = new FileManager(editor);
    }

    private void panel() {
        menuPanel = new JPanel();
        panel = new JPanel();
        menuPanel.setBackground(new Color(132, 160, 88));
        panel.setBackground(new Color(132, 160, 88));

        JRadioButton creatingButton = new JRadioButton("Создать узел", true);
        creatingButton.setFont(FONT);
        creatingButton.addActionListener(e -> editor.setCreatingMode());
        menuPanel.add(creatingButton);

        JRadioButton createEdgeButton = new JRadioButton("Создать рёбра", false);
        createEdgeButton.setFont(FONT);
        createEdgeButton.addActionListener(e -> editor.setConnectingEdge());
        menuPanel.add(createEdgeButton);

        JRadioButton deletingButton = new JRadioButton("Удалить узел", false);
        deletingButton.setFont(FONT);
        deletingButton.addActionListener(e -> editor.setDeletingMode());
        menuPanel.add(deletingButton);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(creatingButton);
        buttonGroup.add(createEdgeButton);
        buttonGroup.add(deletingButton);

        Label label = new Label("Выбрать цвет: ");
        label.setFont(FONT);
        menuPanel.add(label);

        JRadioButton green = new JRadioButton("Зелёный", false);
        green.setFont(FONT);
        green.addActionListener(e -> editor.setChangeColor(1));
        menuPanel.add(green);

        JRadioButton violet = new JRadioButton("Фиолетовый", false);
        violet.setFont(FONT);
        violet.addActionListener(e -> editor.setChangeColor(2));
        menuPanel.add(violet);

        JRadioButton turquoise = new JRadioButton("Бирюзовый", false);
        turquoise.setFont(FONT);
        turquoise.addActionListener(e -> editor.setChangeColor(3));
        menuPanel.add(turquoise);

        ButtonGroup buttonColor = new ButtonGroup();
        buttonColor.add(green);
        buttonColor.add(violet);
        buttonColor.add(turquoise);

        JButton helpButton = new JButton("Как пользоваться?");
        helpButton.setLocation(10, HEIGHT);
        helpButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "Создать узел: щёлкни с помощью мыши в любое место.\n" +
                            "Создать рёбра: нажать на два узла.\n" +
                            "Удалить узел: щёлкни на узел и он исчезнет с панели.\n",
                    " Инструкция", JOptionPane.INFORMATION_MESSAGE);
        });
        helpButton.setFont(FONT);
        menuPanel.add(helpButton);

        JButton check = new JButton("Проверить");
        check.setFont(FONT);
        check.addActionListener(e -> editor.checkBipartite());
        panel.add(check);

        JButton clearButton = new JButton("Очистить");
        clearButton.setFont(FONT);
        clearButton.addActionListener(e -> editor.clear());
        panel.add(clearButton);

        JButton readFileButton = new JButton("Открыть файл");
        readFileButton.setFont(FONT);
        readFileButton.addActionListener(e -> fileManager.readGraphFromFile());
        panel.add(readFileButton);

        JButton saveFileButton = new JButton("Сохранить файл");
        saveFileButton.setFont(FONT);
        saveFileButton.addActionListener(e -> fileManager.saveGraphToFile());
        panel.add(saveFileButton);

        JButton saveButton = new JButton("Сохранить картинку");
        saveButton.setFont(FONT);
        saveButton.addActionListener(e -> fileManager.saveImage());
        panel.add(saveButton);

    }
}
