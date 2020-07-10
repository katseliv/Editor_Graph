package com.company.graph;

import java.awt.*;
import java.util.*;

public class Vertex {
    private int number;
    private Color color;

    public Vertex() {}

    public Vertex(int number) {
        this.number = number;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    void setNullColor() {
        this.color = null;
    }

    public int getNumber() {
        return number;
    }

    void setNumber(int number) {
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vertex vertex = (Vertex) object;
        return number == vertex.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "{" + number + "}";
    }
}
