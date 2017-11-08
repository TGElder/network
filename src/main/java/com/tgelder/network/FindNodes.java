package com.tgelder.network;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindNodes<T> implements Search<T> {

  private final double maxCost;
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
}
