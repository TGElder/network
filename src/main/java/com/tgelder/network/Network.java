package com.tgelder.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public class Network<T> {

  private final ImmutableSet<T> nodes;
  private final ImmutableSet<Edge<T>> edges;

  private final Map<T, ImmutableSet<Edge<T>>> out;
  private final Map<T, ImmutableSet<Edge<T>>> in;

  public Network(ImmutableSet<T> nodes, ImmutableSet<Edge<T>> edges) {
    this(nodes, edges,
            edges.stream().collect(Collectors.groupingBy(Edge::getFrom, ImmutableSet.toImmutableSet())),
            edges.stream().collect(Collectors.groupingBy(Edge::getTo, ImmutableSet.toImmutableSet())));
  }

  public Stream<T> getBelow(T node) {
    return getOut(node).map(Edge::getTo).distinct();
  }

  public Stream<T> getAbove(T node) {
    return getIn(node).map(Edge::getFrom).distinct();
  }

  public Stream<Edge<T>> getOut(T node) {
    Set<Edge<T>> edges = out.get(node);
    if (edges == null) {
      return Stream.empty();
    } else {
      return edges.stream();
    }
  }

  public Stream<Edge<T>> getIn(T node) {
    Set<Edge<T>> edges = in.get(node);
    if (edges == null) {
      return Stream.empty();
    } else {
      return edges.stream();
    }
  }

  public Stream<Edge<T>> getEdges(T from, T to) {
    return getOut(from).filter(e -> e.getTo().equals(to));
  }

  public Stream<Edge<T>> getReverses(Edge<T> edge) {
    return getEdges(edge.getTo(), edge.getFrom());
  }

  public Network<T> merge(Network<T> other) {
    ImmutableSet<T> nodes = ImmutableSet.<T>builder().addAll(getNodes()).addAll(other.getNodes()).build();
    ImmutableSet.Builder<Edge<T>> edgeBuilder = ImmutableSet.<Edge<T>>builder().addAll(other.getEdges());
    getEdges().stream()
            .filter(e -> other.getEdges(e.getFrom(), e.getTo()).count() == 0)
            .forEach(edgeBuilder::add);
    return new Network<>(nodes, edgeBuilder.build());
  }

  public static Network<Integer> createGridNetwork(int width,
                                                   int height,
                                                   int[] neighbourDXs,
                                                   int[] neighbourDYs) {
    ImmutableSet<Integer> nodes = IntStream
            .range(0, width * height).boxed()
            .collect(ImmutableSet.toImmutableSet());

    ImmutableSet.Builder<Edge<Integer>> edgeBuilder = ImmutableSet.builder();

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        for (int n = 0; n < neighbourDXs.length; n++) {

          int dx = neighbourDXs[n];
          int dy = neighbourDYs[n];

          int nx = x + dx;
          int ny = y + dy;

          int index = (width * y) + x;
          int nindex = (width * ny) + nx;

          double cost = Math.sqrt(dx * dx + dy * dy);

          if (nx >= 0 && ny >= 0 && nx < width && ny < height) {
            edgeBuilder.add(new Edge<>(index, nindex, cost));
          }
        }
      }
    }

    return new Network<>(nodes, edgeBuilder.build());
  }

  public Network<T> disconnect(Set<T> disconnectNodes) {
    return new Network<>(nodes, edges.stream()
            .filter(e -> !(disconnectNodes.contains(e.getFrom()) || disconnectNodes.contains(e.getTo())))
            .collect(ImmutableSet.toImmutableSet()));
  }

  public Network<T> setCosts(Set<T> costNodes, double cost) {
    return new Network<>(nodes, edges.stream()
            .map(e -> (costNodes.contains(e.getFrom()) || costNodes.contains(e.getTo()))
                    ? new Edge<>(e.getFrom(), e.getTo(), cost)
                    : e)
            .collect(ImmutableSet.toImmutableSet()));
  }

}
