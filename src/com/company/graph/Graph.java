package com.company.graph;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Graph {
    private Map<Vertex, List<Vertex>> adjacencyMap = new TreeMap<>(Comparator.comparingInt(Vertex::getNumber));
    private List<Vertex> vertices = new LinkedList<>();
    private List<Edge> edges = new LinkedList<>();

    private boolean isBipartite = false;

    private void check() {
        if (vertices.isEmpty()) return;
        sortVertices();
        if (adjacencyMap.get(vertices.get(0)).isEmpty()) return;
        isBipartite = true;
        boolean[] visited = new boolean[vertices.size()];

        class Inner {
            private void checker(Vertex currentVertex) {
                if (currentVertex.getColor() == null) {
                    currentVertex.setColor(Color.YELLOW);
                }
                visited[currentVertex.getNumber()] = true;
                for (Vertex vertex : adjacent(currentVertex)) {
                    if (!visited[vertex.getNumber()]) {
                        if (currentVertex.getColor().equals(Color.YELLOW)) {
                            vertex.setColor(new Color(125, 0, 200));
                        } else if (currentVertex.getColor().equals(new Color(125, 0, 200))) {
                            vertex.setColor(Color.YELLOW);
                        }
                        checker(vertex);
                    } else if (currentVertex.getColor().equals(vertex.getColor())) {
                        isBipartite = false;
                    }
                }
            }
        }
        Inner inner = new Inner();
        inner.checker(vertices.get(0));
    }

    public boolean isBipartite() {
        check();
        return isBipartite;
    }

    public void clearColors() {
        for (Vertex vertex : vertices) {
            vertex.setNullColor();
        }
    }

    private void sortVertices() {
        vertices.sort((vertex, otherVertex) ->
                Integer.compare(adjacencyMap.get(otherVertex).size(), adjacencyMap.get(vertex).size()));
    }

    public void addVertex(Vertex vertex) {
        vertex.setNumber(vertices.size());
        adjacencyMap.put(vertex, new LinkedList<>());
        vertices.add(vertex);
    }

    public boolean isAdjacent(Vertex firstVertex, Vertex secondVertex) {
        for (Vertex adjacent : adjacent(firstVertex)) {
            if (adjacent.equals(secondVertex)) {
                return true;
            }
        }
        return false;
    }

    public void addEdge(Vertex start, Vertex end, int weight) {
        if (!isAdjacent(start, end)) {
            adjacencyMap.get(start).add(end);
            adjacencyMap.get(end).add(start);
            edges.add(new Edge(start, end, weight));
        }
    }

    public void removeVertex(Vertex deletingVertex) {
        vertices.remove(deletingVertex);
        for (Vertex adjacentVertex : adjacencyMap.get(deletingVertex)) {
            List<Vertex> adjacentVertices = adjacencyMap.get(adjacentVertex);
            adjacentVertices.removeIf(currentVertex -> currentVertex.equals(deletingVertex));
        }
        edges.removeIf(deletingEdge -> deletingEdge.getStart().equals(deletingVertex) || deletingEdge.getEnd().equals(deletingVertex));
        adjacencyMap.remove(deletingVertex);
        recountIndex(deletingVertex);
    }

    private void recountIndex(Vertex deletingVertex) {
        for (Vertex currentVertex : vertices) {
            if (deletingVertex.getNumber() < currentVertex.getNumber()) {
                currentVertex.setNumber(currentVertex.getNumber() - 1);
            }
        }
    }

    public void removeEdge(Vertex start, Vertex end) {
        Edge deletingEdge = new Edge(start, end, 0);
        edges.removeIf(edge -> edge.equals(deletingEdge));
        adjacencyMap.get(start).removeIf(adjacentVertex -> adjacentVertex.equals(end));
        adjacencyMap.get(end).removeIf(adjacentVertex -> adjacentVertex.equals(start));
    }

    public void clear() {
        if (adjacencyMap != null) adjacencyMap.clear();
        if (vertices != null) vertices.clear();
        if (edges != null) edges.clear();
    }

    private Iterable<Vertex> adjacent(Vertex vertex) {
        return adjacencyMap.get(vertex);
    }

    public Map<Vertex, List<Vertex>> getAdjacencyMap() {
        return adjacencyMap;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }
}
