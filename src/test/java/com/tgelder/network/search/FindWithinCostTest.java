package com.tgelder.network.search;

import com.google.common.collect.ImmutableSet;
import com.tgelder.network.Edge;
import com.tgelder.network.Network;
import com.tgelder.network.TestUtils;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class FindWithinCostTest {

  @Test
  public void testFindWithinCost() {
    ImmutableSet<Integer> nodes = TestUtils.generateNodes(10);
    ImmutableSet<Edge<Integer>> edges = ImmutableSet.of(
            new Edge<>(0, 1, 1),
            new Edge<>(0, 6, 5),
            new Edge<>(0, 7, 6),
            new Edge<>(0, 8, 7),
            new Edge<>(1, 2, 10),
            new Edge<>(1, 3, 1),
            new Edge<>(2, 5, 1),
            new Edge<>(3, 4, 1),
            new Edge<>(4, 5, 1),
            new Edge<>(5, 2, 10));

    Network<Integer> network = new Network<>(nodes, edges);
    Set<Integer> result = NetworkSearch.search(network, 0, new FindWithinCost<>(6));

    assertThat(result).containsExactlyInAnyOrder(0, 1, 3, 4, 5, 6);
  }

}
