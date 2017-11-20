package com.tgelder.network.search;

import com.google.common.collect.ImmutableList;
import com.tgelder.network.Edge;
import com.tgelder.network.Network;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Pathfinder {

  @SuppressWarnings("unchecked")
  public static Optional<ImmutableList<Edge<Integer>>> find(Network<Integer> network,
                                                            Integer from,
                                                            Integer to,
                                                            Heuristic heuristic) {
    int nodes = network.getNodes().size();
    boolean[] open = new boolean[nodes];
    boolean[] closed = new boolean[nodes];
    double[] G = new double[nodes];
    double[] F = new double[nodes];
    Edge<Integer>[] parents = new Edge[nodes];

    Comparator<Integer> comparator = Comparator.<Integer>comparingDouble(i -> F[i]).thenComparing(Comparator.comparingInt(i -> i));

    TreeSet<Integer> tree = new TreeSet<>(comparator);

    tree.add(from);

    while (!tree.isEmpty()) {

      Integer focus = tree.pollFirst();
      double focusCost = G[focus];

      closed[focus] = true;

      if (focus.equals(to)) {
        return Optional.of(buildPath(parents, from, to));
      }

      Map<Integer, Optional<Edge<Integer>>> neighbours = network.getOut(focus)
              .filter(e -> !closed[e.getTo()])
              .collect(Collectors.groupingBy(Edge::getTo, Collectors.minBy(Comparator.comparingDouble(Edge::getCost))));

      neighbours.forEach((neighbour, edgeOptional) -> {
        double neighbourCost = focusCost + edgeOptional.get().getCost();
        if (!open[neighbour] || neighbourCost < G[neighbour]) {
          G[neighbour] = neighbourCost;
          parents[neighbour] = edgeOptional.get();
          G[neighbour] = neighbourCost;
          F[neighbour] = neighbourCost + heuristic.calculate(neighbour, to);
          if (!open[neighbour]) {
            tree.remove(neighbour);
          } else {
            open[neighbour] = true;
          }

          tree.add(neighbour);
        }
      });
    }

    return Optional.empty();
  }

  @SuppressWarnings("unchecked")
  private static ImmutableList<Edge<Integer>> buildPath(Edge[] parents, Integer from, Integer to) {
    Integer focus = to;

    ImmutableList.Builder<Edge<Integer>> out = ImmutableList.builder();
    while (!focus.equals(from)) {
      out.add(parents[focus]);
      focus = (Integer) parents[focus].getFrom();
    }
    return out.build().reverse();
  }

}
