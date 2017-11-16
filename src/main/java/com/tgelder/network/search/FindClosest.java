package com.tgelder.network.search;

import lombok.RequiredArgsConstructor;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class FindClosest<T> implements Search<T> {

  private final Predicate<T> stoppingCondition;
  private boolean done = false;
  private Double closestCost = null;

  @Override
  public boolean take(T node, double focusCost) {
    if (stoppingCondition.test(node)) {
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
}
