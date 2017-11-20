package com.tgelder.network.search;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EuclideanHeuristic implements Heuristic {

  private final int gridHeight;
  private final int gridWidth;
  private final double minCost;

  @Override
  public double calculate(Integer to, Integer from) {
    int ty = to / gridHeight;
    int tx = to % gridWidth;
    int fy = from / gridHeight;
    int fx = from % gridWidth;
    return Math.sqrt(Math.pow(tx - fx, 2) + Math.pow(ty - fy, 2)) * minCost;
  }

}
