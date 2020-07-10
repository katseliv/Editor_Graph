package com.company.edit;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileManager {
    private Editor editor;
    private FileFilter graphFilter = new FileNameExtensionFilter("graph.Graph files (*.graph)", "graph");
    private FileFilter filterImage = new FileNameExtensionFilter("PNG files (*.png)", "png");
    private JFileChooser saveImageChooser = new JFileChooser();
    private JFileChooser graphSaveFileChooser = new JFileChooser();
    private JFileChooser saveFileChooser = new JFileChooser();

    FileManager(Editor editor) {
        this.editor = editor;
    }

    void saveImage() {
        saveImageChooser.setCurrentDirectory(new File("./img"));
        saveImageChooser.addChoosableFileFilter(filterImage);
        saveImageChooser.setAcceptAllFileFilterUsed(false);
        if (saveImageChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            saveImageGraph(saveImageChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void saveImageGraph(String fileName) {
        BufferedImage imageGraph = (BufferedImage)
                editor.createImage(editor.getWidth(), editor.getHeight());
        Graphics2D graphics2D = imageGraph.createGraphics();
        editor.paint(graphics2D);
        graphics2D.dispose();
        try {
            ImageIO.write(imageGraph, "png", new File(checkImageName(fileName)));
        } catch (IOException io) {
            JOptionPane.showMessageDialog(null, "E R R O R !",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String checkImageName(String imageName) {
        if (!imageName.endsWith(".png")) {
            imageName += ".png";
        }
        return imageName;
    }

    private String checkFileName(String fileName) {
        if (!fileName.endsWith(".graph")) {
            fileName += ".graph";
        }
        return fileName;
    }

    void saveGraphToFile() {
        graphSaveFileChooser.setCurrentDirectory(new File("./files"));
        graphSaveFileChooser.addChoosableFileFilter(graphFilter);
        graphSaveFileChooser.setAcceptAllFileFilterUsed(false);
        graphSaveFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        if (graphSaveFileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(
                    new File(checkFileName(graphSaveFileChooser.getSelectedFile().getAbsolutePath())))) {
                writer.write(editor.toString());
            } catch (Exception exp) {
                JOptionPane.showMessageDialog(null, "FILE NOT FOUND",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void readGraphFromFile() {
        saveFileChooser.setCurrentDirectory(new File("./files"));
        saveFileChooser.addChoosableFileFilter(graphFilter);
        saveFileChooser.setAcceptAllFileFilterUsed(false);
        saveFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

        if (saveFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (Scanner scanner = new Scanner(saveFileChooser.getSelectedFile())) {
                editor.readGraphFromFile(scanner);
                editor.repaint();
            } catch (Exception exc) {
                editor.clear();
                JOptionPane.showMessageDialog(null, "WRONG FILE!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
