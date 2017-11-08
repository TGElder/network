package com.tgelder.network;

public interface Search<T> {
  boolean take(T node, double focusCost);

  boolean done();
}

