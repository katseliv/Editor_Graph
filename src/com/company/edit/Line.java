package com.company.edit;

import com.company.graph.Edge;
import java.awt.geom.Line2D;

public class Line {
    private Line2D line;
    private Edge edge;
    int length;

    Line(Line2D line, Edge edge) {
        this.line = line;
        this.edge = edge;
        this.length = countLength(line.getX1(), line.getY1(), line.getX2(), line.getY2());
    }

    Line2D getLine() {
        return line;
    }

    public Edge getEdge() {
        return edge;
    }

    public int getLength() {
        return this.length;
    }

    float getXOfCenter() {
        return (float) ((line.getX1() + line.getX2()) / 2.0);
    }

    float getYOfCenter() {
        return (float) ((line.getY1() + line.getY2()) / 2.0);
    }

    public static int countLength(double x1, double y1, double x2, double y2) {
        int length;
        double x = Math.pow((x2 - x1), 2);
        double y = Math.pow((y2 - y1), 2);
        length = (int) Math.sqrt(x + y) / 38;
        return length;
    }

    @Override
    public String toString() {
        return "Line: " + edge;
    }
}
