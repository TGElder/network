package com.tgelder.network.search;

import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public class EuclideanHeuristic implements Function<Integer, Double> {

  public final Integer to;
  public final int gridHeight;
  public final int gridWidth;
  public final int tx;
  public final int ty;
  public final double minCost;

  public EuclideanHeuristic(Integer to, int gridHeight, int gridWidth, double minCost) {
    this.to = to;
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;
    this.minCost = minCost;
    ty = to / gridHeight;
    tx = to % gridWidth;
  }

  @Override
  public Double apply(Integer focus) {
    int fy = focus / gridHeight;
    int fx = focus / gridWidth;
    return Math.sqrt(Math.pow(tx - fx, 2) + Math.pow(ty - fy, 2)) * minCost;
  }

}
