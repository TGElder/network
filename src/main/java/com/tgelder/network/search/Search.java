package com.tgelder.network.search;

public interface Search<Integer> {
  boolean take(Integer node, double focusCost);

  boolean done();
}

