package com.tgelder.network;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Edge<T> {

  private final T from;
  private final T to;
  private double cost;

}
