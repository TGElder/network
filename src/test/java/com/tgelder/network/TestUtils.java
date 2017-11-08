package com.tgelder.network;

import com.google.common.collect.ImmutableSet;
import org.assertj.core.data.Offset;

import java.util.stream.IntStream;

public class TestUtils {

  public static final Offset<Double> PRECISION = Offset.offset(0.00001);

  public static ImmutableSet<Integer> generateNodes(int howMany) {
    return IntStream.range(0, howMany).boxed().collect(ImmutableSet.toImmutableSet());
  }

}
