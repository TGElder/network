package com.tgelder.network.search;

import com.tgelder.network.Edge;
import com.tgelder.network.Network;

import java.util.*;
import java.util.stream.Collectors;

public class NetworkSearch {

  public static Set<Integer> search(Network<Integer> network, Integer start, Search<Integer> search) {
    int nodes = network.getNodes().size();
    boolean[] open = new boolean[nodes];
    boolean[] closed = new boolean[nodes];
    double[] cost = new double[nodes];

    Comparator<Integer> comparator = Comparator.<Integer>comparingDouble(i -> cost[i]).thenComparing(Comparator.comparingInt(i -> i));

    TreeSet<Integer> tree = new TreeSet<>(comparator);

    tree.add(start);

    Set<Integer> out = new HashSet<>();

    while (!tree.isEmpty()) {

      Integer focus = tree.pollFirst();
      double focusCost = cost[focus];

      closed[focus] = true;

      if (search.take(focus, focusCost)) {
        out.add(focus);
      }
      if (search.done()) {
        return out;
      }

      Map<Integer, Optional<Edge<Integer>>> neighbours = network.getOut(focus)
              .filter(e -> !closed[e.getTo()])
              .collect(Collectors.groupingBy(Edge::getTo, Collectors.minBy(Comparator.comparingDouble(Edge::getCost))));

      neighbours.forEach((neighbour, edgeOptional) -> {
        double neighbourCost = focusCost + edgeOptional.get().getCost();
        if (open[neighbour]) {
          if (neighbourCost < cost[neighbour]) {
            cost[neighbour] = neighbourCost;
            tree.remove(neighbour);
            tree.add(neighbour);
          }
        } else {
          open[neighbour] = true;
          cost[neighbour] = neighbourCost;
          tree.add(neighbour);
        }

      });
    }

    return out;
  }

}

