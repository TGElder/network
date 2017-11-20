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
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 11, 13, heuristic);

    assertThat(path.get().size()).isEqualTo(2);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(12, 13);
  }

  @Test
  public void obstacle() {
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(12, 17, 22));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 11, 13, heuristic);

    assertThat(path.get().size()).isEqualTo(4);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(6,
                                                                                                              7,
                                                                                                              8,
                                                                                                              13);
  }

  @Test
  public void obstacleSlow() {
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(12, 17, 22), 3);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 11, 13, heuristic);

    assertThat(path.get().size()).isEqualTo(4);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(6,
                                                                                                              7,
                                                                                                              8,
                                                                                                              13);
  }

  @Test
  public void hasToMoveAwayFromGoal() {
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(8, 12, 13));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 7, 18, heuristic);

    assertThat(path.get().size()).isEqualTo(5);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(6,
                                                                                                              11,
                                                                                                              16,
                                                                                                              17,
                                                                                                              18);
  }

  @Test
  public void hasToMoveAwayFromGoalSlow() {
    Network<Integer> network = Network.createGridNetwork(5, 5, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(8, 12, 13), 3);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(5, 5, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 7, 18, heuristic);

    assertThat(path.get().size()).isEqualTo(5);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(6,
                                                                                                              11,
                                                                                                              16,
                                                                                                              17,
                                                                                                              18);
  }


  @Test
  public void spiral5x5() {
    Network<Integer> network = Network.createGridNetwork(7, 7, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(4, 11, 16, 18, 23, 25, 30, 31, 32));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(7, 7, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 24, 12, heuristic);

    assertThat(path.get().size()).isEqualTo(16);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(17,
                                                                                                              10,
                                                                                                              9,
                                                                                                              8,
                                                                                                              15,
                                                                                                              22,
                                                                                                              29,
                                                                                                              36,
                                                                                                              37,
                                                                                                              38,
                                                                                                              39,
                                                                                                              40,
                                                                                                              33,
                                                                                                              26,
                                                                                                              19,
                                                                                                              12);
  }

  @Test
  public void spiral5x5Slow() {
    Network<Integer> network = Network.createGridNetwork(7, 7, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(4, 11, 16, 18, 23, 25, 30, 31, 32), 8);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(7, 7, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 24, 12, heuristic);

    assertThat(path.get().size()).isEqualTo(16);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(17,
                                                                                                              10,
                                                                                                              9,
                                                                                                              8,
                                                                                                              15,
                                                                                                              22,
                                                                                                              29,
                                                                                                              36,
                                                                                                              37,
                                                                                                              38,
                                                                                                              39,
                                                                                                              40,
                                                                                                              33,
                                                                                                              26,
                                                                                                              19,
                                                                                                              12);
  }

  @Test
  public void spiral5x5SlowIsBetter() {
    Network<Integer> network = Network.createGridNetwork(7, 7, neighbourDXs, neighbourDYs);
    network = network.setCosts(ImmutableSet.of(4, 11, 16, 18, 23, 25, 30, 31, 32), 6);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(7, 7, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 24, 12, heuristic);

    assertThat(path.get().size()).isEqualTo(4);
    assertThat(path.get().stream().map(Edge::getTo).collect(ImmutableList.toImmutableList())).containsExactly(25,
                                                                                                              26,
                                                                                                              19,
                                                                                                              12);
  }

  @Test
  public void toEqualsFrom() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);

    EuclideanHeuristic heuristic = new EuclideanHeuristic(3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 4, 4, heuristic);

    assertThat(path.get()).isEmpty();
  }

  @Test
  public void noPath() {
    Network<Integer> network = Network.createGridNetwork(3, 3, neighbourDXs, neighbourDYs);
    network = network.disconnect(ImmutableSet.of(1, 4, 7));

    EuclideanHeuristic heuristic = new EuclideanHeuristic(3, 3, 1);

    Optional<ImmutableList<Edge<Integer>>> path = Pathfinder.find(network, 0, 8, heuristic);

    assertThat(path.isPresent()).isFalse();
  }

}
