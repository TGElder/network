package com.tgelder.network;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
    return new Network(nodes, edgeBuilder.build());
  }

  private Set<T> search(T start, Search<T> search) {
    Map<T, Double> open = new HashMap<>();
    Set<T> closed = new HashSet<>();

    open.put(start, 0.0);

    Set<T> out = new HashSet<>();

    while (!open.isEmpty()) {
      T focus = Collections.min(open.keySet(), Comparator.comparingDouble(open::get));
      double focusCost = open.get(focus);

      open.remove(focus);
      closed.add(focus);

      if (search.take(focus, focusCost)) {
        out.add(focus);
      }
      if (search.done()) {
        return out;
      }

      Map<T, Optional<Edge<T>>> neighbours = getOut(focus)
              .filter(e -> !closed.contains(e.getTo()))
              .collect(Collectors.groupingBy(Edge::getTo, Collectors.minBy(Comparator.comparingDouble(Edge::getCost))));

      neighbours.forEach((node, edgeOptional) -> {
        double cost = focusCost + edgeOptional.get().getCost();
        open.merge(node, cost, Math::min);
      });
    }

    return out;
  }

  public Set<T> findClosest(T start, Predicate<T> stoppingCondition) {

    return search(start, new Search<T>() {

      private boolean done = false;
      private Double closestCost = null;

      @Override
      public boolean take(T node, double focusCost) {
        if ((!node.equals(start)) && stoppingCondition.test(node)) {
          if (closestCost == null) {
            closestCost = focusCost;
            return true;
          } else if (focusCost == closestCost) {
            return true;
          } else if (focusCost > closestCost) {
            done = true;
            return false;
          } else {
            throw new RuntimeException("Invalid state");
          }
        } else {
          return false;
        }
      }

      @Override
      public boolean done() {
        return done;
      }
    });
  }

  public Set<T> getNodes(T start, int maxCost) {
    return search(start, new Search<T>() {

      private boolean done = false;

      @Override
      public boolean take(T node, double focusCost) {
        if (focusCost >= maxCost) {
          done = true;
          return false;
        } else {
          return true;
        }
      }

      @Override
      public boolean done() {
        return done;
      }
    });
  }

  private interface Search<T> {
    boolean take(T node, double focusCost);

    boolean done();
  }

  @AllArgsConstructor
  @Getter
  private class Exit {
    private final T neighbour;
    private final Edge edge;
  }

}
