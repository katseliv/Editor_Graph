package com.company.edit;

import com.company.graph.Edge;
import com.company.graph.Graph;
import com.company.graph.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Editor extends JPanel {
    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);
    private static final int STROKE_WIDTH = 4;
    private static final int LINE_WIDTH = 35;
    private static final int LINE_HEIGHT = 5;

    private List<Node> nodes = new LinkedList<>();
    private List<Line> lines = new LinkedList<>();
    private CreatingVertex creatingVertex = new CreatingVertex();
    private ConnectingVertex connectingVertex = new ConnectingVertex();
    private ChangeColor changeColor = new ChangeColor();
    private Deleting deleting = new Deleting();
    private Graph graph = new Graph();

    public Editor() {
        setCreatingMode();
    }

    public void paint(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        paintSettings(graphics2D);
        paintLines(graphics2D);
        paintVertices(graphics2D);
    }

    private void paintSettings(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setStroke(new BasicStroke(STROKE_WIDTH));
        graphics2D.setFont(FONT);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics2D.setColor(Color.BLACK);
    }

    //мой метод
    void checkBipartite(){
        if (graph.isBipartite()) {
            JOptionPane.showMessageDialog(null, "Граф двудольный!",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            graph.clearColors();
            JOptionPane.showMessageDialog(null, "Граф не двудольный!",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        repaint();
    }

    void clear() {
        graph.clear();
        nodes.clear();
        lines.clear();
        repaint();
    }

    private void paintLines(Graphics2D graphics2D) {
        for (Line line : lines) {
            graphics2D.setColor(new Color(125, 0, 200));
            graphics2D.draw(line.getLine());
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawString(String.valueOf(line.length), line.getXOfCenter() + 10, line.getYOfCenter() + 10);
        }
    }

    private void paintVertices(Graphics2D graphics2D) {
        Circle circle;
        Vertex vertex;
        for (Node node : nodes) {
            circle = node.getCircle();
            vertex = node.getVertex();

            if (node.equals(connectingVertex.startNode)) {
                graphics2D.setColor(Color.ORANGE);
            } else {
                graphics2D.setColor(vertex.getColor());
            }

            graphics2D.draw(circle);
            graphics2D.setColor(Color.BLACK);

            if (vertex.getColor() != null) {
                graphics2D.setColor(vertex.getColor());
            }

            graphics2D.fill(node.getCircle());
            graphics2D.setColor(Color.BLACK);
        }
    }

    private void removeAllListeners() {
        connectingVertex.clear();
        removeMouseListener(creatingVertex);
        removeMouseListener(connectingVertex);
        removeMouseListener(deleting);
        removeMouseListener(changeColor);
        repaint();
    }

    void setCreatingMode() {
        removeAllListeners();
        addMouseListener(creatingVertex);
    }

    void setConnectingEdge() {
        removeAllListeners();
        addMouseListener(connectingVertex);
    }

    void setChangeColor(int i) {
        changeColor.setI(i);
        removeAllListeners();
        addMouseListener(changeColor);
    }

    void setDeletingMode() {
        removeAllListeners();
        addMouseListener(deleting);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes) {
            stringBuilder.append(node).append(graph.getAdjacencyMap().get(node.getVertex()));
            stringBuilder.append("\n");
        }
        for (Line line : lines) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private Node readNodeFromFile(String string) throws Exception {
        double x, y;
        int vertexNumber;
        Pattern pattern = Pattern.compile("\\{([\\d]+)}");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            vertexNumber = Integer.parseInt(matcher.group(1));
        } else {
            throw new Exception("WRONG FORMAT");
        }
        pattern = Pattern.compile("\\(+([\\d]+\\.0), ([\\d]+\\.0)\\)");
        matcher = pattern.matcher(string);
        if (matcher.find()) {
            x = Double.parseDouble(matcher.group(1));
            y = Double.parseDouble(matcher.group(2));
        } else {
            throw new Exception("WRONG FORMAT");
        }
        Vertex newVertex = new Vertex(vertexNumber);
        Circle newCircle = new Circle(x, y);
        return new Node(newVertex, newCircle);
    }

    private void readAdjacentListFromFile(Vertex vertex, String string) throws Exception {
        Pattern pattern = Pattern.compile("\\[]");
        Matcher matcher = pattern.matcher(string);
        if (!matcher.find()) {
            pattern = Pattern.compile("\\[(\\{\\d*}|(, ))*]");
            matcher = pattern.matcher(string);
            if (matcher.find()) {
                pattern = Pattern.compile("\\{([\\d]+)}[, \\]]");
                matcher = pattern.matcher(matcher.group(1));
                while (matcher.find()) {
                    graph.getAdjacencyMap().get(vertex).add(new Vertex(Integer.parseInt(matcher.group(1))));
                }
            } else throw new Exception("WRONG FORMAT");
        }
    }

    private void readEdgeFromFile(String string) throws Exception {
        int startVertex, secondVertex, weight;
        Pattern pattern = Pattern.compile("<\\{([\\d]+)}, \\{([\\d]+)}, ([\\d]+)>");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            startVertex = Integer.parseInt(matcher.group(1));
            secondVertex = Integer.parseInt(matcher.group(2));
            weight = Integer.parseInt(matcher.group(3));
        } else {
            throw new Exception("WRONG FORMAT");
        }
        Node firstNode = nodes.get(graph.getVertices().get(startVertex).getNumber());
        Node secondNode = nodes.get(graph.getVertices().get(secondVertex).getNumber());
        graph.addEdge(graph.getVertices().get(startVertex), graph.getVertices().get(secondVertex), weight);
        lines.add(setNewLine(firstNode, secondNode));
    }

    void readGraphFromFile(Scanner scanner) throws Exception {
        clear();
        String string;
        while (scanner.hasNextLine()) {
            string = scanner.nextLine();
            if (string.startsWith("Node:")) {
                Node newNode = readNodeFromFile(string);
                Vertex newVertex = newNode.getVertex();
                readAdjacentListFromFile(newVertex, string);
                graph.addVertex(newVertex);
                nodes.add(newNode);
            } else if (string.startsWith("Line:")) readEdgeFromFile(string);
        }
    }

    private Line setNewLine(Node startNode, Node endNode) {
        Circle circleStart = startNode.getCircle();
        Circle circleEnd = endNode.getCircle();
        double radius = Circle.RADIUS;
        int length = Line.countLength(circleStart.getX() + radius,
                circleStart.getY() + radius,
                circleEnd.getX() + radius,
                circleEnd.getY() + radius);
        return new Line(new Line2D.Double(
                circleStart.getX() + radius,
                circleStart.getY() + radius,
                circleEnd.getX() + radius,
                circleEnd.getY() + radius),
                new Edge(startNode.getVertex(), endNode.getVertex(), length));
    }

    private class CreatingVertex extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent event) {
            double x = event.getX() - Circle.RADIUS;
            double y = event.getY() - Circle.RADIUS;
            Vertex newVertex = new Vertex();
            graph.addVertex(newVertex);
            nodes.add(new Node(newVertex, new Circle(x, y)));
            repaint();
        }
    }

    private class ConnectingVertex extends MouseAdapter {
        Node startNode;
        Line newLine;
        boolean vertexClicked = false;

        @Override
        public void mouseClicked(MouseEvent e) {
            for (Node node : nodes) {
                if (!vertexClicked) {
                    if (node.getCircle().contains(e.getX(), e.getY())) {
                        startNode = node;
                        vertexClicked = true;
                        break;
                    }
                } else if (startNode.getCircle().contains(e.getX(), e.getY())) {
                    clear();
                    break;
                } else if (node.getCircle().contains(e.getX(), e.getY())) {
                    setNewLine(node);
                    lines.add(newLine);
                    graph.addEdge(startNode.getVertex(), node.getVertex(), newLine.getEdge().getWeight());
                    clear();
                    break;
                }
            }
            repaint();
        }

        private void setNewLine(Node node) {
            Circle circleStart = startNode.getCircle();
            Circle circleEnd = node.getCircle();
            double radius = Circle.RADIUS;
            int length = Line.countLength(circleStart.getX() + radius,
                    circleStart.getY() + radius,
                    circleEnd.getX() + radius,
                    circleEnd.getY() + radius);
            newLine = new Line(new Line2D.Double(
                    circleStart.getX() + radius,
                    circleStart.getY() + radius,
                    circleEnd.getX() + radius,
                    circleEnd.getY() + radius),
                    new Edge(startNode.getVertex(), node.getVertex(), length));
        }

        private void clear() {
            startNode = null;
            newLine = null;
            vertexClicked = false;
        }
    }

    private class ChangeColor extends MouseAdapter {
        private int i;

        ChangeColor(int i) {
            this.i = i;
        }

        ChangeColor() {
            this(0);
        }

        void setI(int i) {
            this.i = i;
        }

        int getI() {
            return i;
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            double x = event.getX() - Circle.RADIUS;
            double y = event.getY() - Circle.RADIUS;

            Vertex newVertex = new Vertex();
            if (i == 1) {
                newVertex.setColor(Color.GREEN);
            } else if (i == 2) {
                newVertex.setColor(new Color(187, 0, 255));
            } else if (i == 3) {
                newVertex.setColor(new Color(1, 192, 198));
            } else {
                newVertex.setColor(Color.BLACK);
            }

            graph.addVertex(newVertex);
            nodes.add(new Node(newVertex, new Circle(x, y)));
            repaint();
        }
    }

    private class Deleting extends MouseAdapter {
        Node deletingNode = null;
        Line deletingLine = null;
        boolean vertexClicked = false;
        List<Line> deletingLines = new LinkedList<>();

        @Override
        public void mouseClicked(MouseEvent event) {
            for (Node node : nodes) {
                if (node.getCircle().contains(event.getX(), event.getY())) {
                    deletingNode = node;
                    vertexClicked = true;
                }
            }
            for (Line line : lines) {
                if (vertexClicked) {
                    if (containsLine(deletingNode, line)) deletingLines.add(line);
                } else if (line.getLine().intersects(event.getX(), event.getY(), LINE_WIDTH, LINE_HEIGHT))
                    deletingLine = line;
            }
            if (vertexClicked) removeNode();
            else if (deletingLine != null) removeLine();
            repaint();
            clear();
        }

        private void clear() {
            deletingNode = null;
            deletingLine = null;
            deletingLines.clear();
            vertexClicked = false;
        }

        private void removeNode() {
            graph.removeVertex(deletingNode.getVertex());
            nodes.remove(deletingNode);
            deleteLines(deletingLines);
        }

        private void removeLine() {
            graph.removeEdge(deletingLine.getEdge().getStart(), deletingLine.getEdge().getEnd());
            lines.remove(deletingLine);
        }

        private void deleteLines(List<Line> deletingLines) {
            for (Line line : deletingLines) {
                lines.remove(line);
            }
        }

        private boolean containsLine(Node node, Line line) {
            return node.getCircle().contains(line.getLine().getX1(), line.getLine().getY1()) || node.getCircle().contains(line.getLine().getX2(), line.getLine().getY2());
        }
    }
}
