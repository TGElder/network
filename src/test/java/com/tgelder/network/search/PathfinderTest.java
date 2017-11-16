package com.tgelder.network.search;

import com.google.common.collect.ImmutableList;
import com.tgelder.network.Edge;
import com.tgelder.network.Network;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class PathfinderTest {

  private static int[] neighbourDXs = {-1, 0, 1, 0};
  private static int[] neighbourDYs = {0, -1, 0, 1};

  @Test
  public void basic() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 3, 5, heuristic);

    assertThat(path.get().size()).isEqualTo(2);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(4, 5);

  }

}
