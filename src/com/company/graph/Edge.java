package com.company.graph;

import java.util.Objects;

public class Edge {
    private Vertex start;
    private Vertex end;
    private int weight;

    public Edge(Vertex start, Vertex end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    public Vertex getStart() {
        return start;
    }

    public Vertex getEnd() {
        return end;
    }

    public int getWeight(){
        return weight;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Edge edge = (Edge) object;
        return (start.equals(edge.start) && end.equals(edge.end)) ||
                (start.equals(edge.end) && end.equals(edge.start));
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "<" + start + ", " + end + ", " + weight + ">";
    }
}
