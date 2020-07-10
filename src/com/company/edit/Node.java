package com.company.edit;

import com.company.graph.Vertex;

public class Node {
    private Vertex vertex;
    private Circle circle;

    Node(Vertex vertex, Circle circle) {
        this.vertex = vertex;
        this.circle = circle;
    }

    float getNumberX() {
        return (float) (circle.getX() + Circle.DIAMETER / 3.0);
    }

    float getNumberY() {
        return (float) (circle.getY() + Circle.DIAMETER / 1.5);
    }

    Vertex getVertex() {
        return vertex;
    }

    Circle getCircle() {
        return circle;
    }

    @Override
    public String toString() {
        return "Node: " + vertex.toString() + " (" + circle.getX() + ", " + circle.getY() + ") ";
    }
}
