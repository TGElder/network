package com.tgelder.network.search;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.tgelder.network.Edge;
import com.tgelder.network.Network;
import org.junit.Test;

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

  @Test
  public void obstacle() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(4, 7));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(8, 3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 6, 8, heuristic);

    assertThat(path.get().size()).isEqualTo(6);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(3,
                                                                                                                 0,
                                                                                                                 1,
                                                                                                                 2,
                                                                                                                 5,
                                                                                                                 8);
  }

  @Test
  public void spiral5x5() {
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(3, 6, 8, 11, 13, 16, 17, 18));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(4, 5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 12, 4, heuristic);

    assertThat(path.get().size()).isEqualTo(16);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(7,
                                                                                                                 2,
                                                                                                                 1,
                                                                                                                 0,
                                                                                                                 5,
                                                                                                                 10,
                                                                                                                 15,
                                                                                                                 20,
                                                                                                                 21,
                                                                                                                 22,
                                                                                                                 23,
                                                                                                                 24,
                                                                                                                 19,
                                                                                                                 14,
                                                                                                                 9,
                                                                                                                 4);
  }

  @Test
  public void hasToMoveAwayFromGoal() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(2, 4, 5));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(8, 3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 1, 8, heuristic);

    assertThat(path.get().size()).isEqualTo(5);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(0,
                                                                                                                 3,
                                                                                                                 6,
                                                                                                                 7,
                                                                                                                 8);
  }

  @Test
  public void obstacleSlow() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(4, 7), 10);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(8, 3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 6, 8, heuristic);

    assertThat(path.get().size()).isEqualTo(6);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(3,
                                                                                                                 0,
                                                                                                                 1,
                                                                                                                 2,
                                                                                                                 5,
                                                                                                                 8);
  }

  @Test
  public void hasToMoveAwayFromGoalSlow() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(2, 4, 5), 10);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(8, 3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 1, 8, heuristic);

    assertThat(path.get().size()).isEqualTo(5);
    assertThat(path.get().stream().map(e -> e.getTo()).collect(ImmutableList.toImmutableList())).containsExactly(0,
                                                                                                                 3,
                                                                                                                 6,
                                                                                                                 7,
                                                                                                                 8);
  }

  // TODO add tests with more empty dead space around
  // TODO add basic tests (start finish same spot, no path)

}
