package com.tgelder.network;

import com.google.common.collect.ImmutableSet;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"unchecked"})
public class NetworkTest {

  private static Network<Integer> network;
  private static ImmutableSet<Integer> nodes;
  private static Edge<Integer> edge01;
  private static Edge<Integer> edge02a;
  private static Edge<Integer> edge02b;
  private static Edge<Integer> edge13;
  private static Edge<Integer> edge23a;
  private static Edge<Integer> edge23b;
  private static Edge<Integer> edge56;
  private static Edge<Integer> edge65a;
  private static Edge<Integer> edge65b;
  private static Edge<Integer> edge77;

  @BeforeClass
  public static void beforeClass() {
    nodes = TestUtils.generateNodes(8);

    edge01 = new Edge<>(0, 1, 1);
    edge02a = new Edge<>(0, 2, 1);
    edge02b = new Edge<>(0, 2, 1);
    edge13 = new Edge<>(1, 3, 1);
    edge23a = new Edge<>(2, 3, 1);
    edge23b = new Edge<>(2, 3, 1);
    edge56 = new Edge<>(5, 6, 1);
    edge65a = new Edge<>(6, 5, 1);
    edge65b = new Edge<>(6, 5, 1);
    edge77 = new Edge<>(7, 7, 1);

    network = new Network<>(nodes,
            ImmutableSet.of(edge01, edge02a, edge02b, edge13, edge23a, edge23b, edge56, edge65a, edge65b, edge77));
  }

  @Test
  public void testGetBelow() {
    assertThat(network.getBelow(0)).containsExactlyInAnyOrder(1, 2);
    assertThat(network.getBelow(1)).containsExactly(3);
    assertThat(network.getBelow(2)).containsExactly(3);
    assertThat(network.getBelow(3)).isEmpty();
    assertThat(network.getBelow(4)).isEmpty();
    assertThat(network.getBelow(5)).containsExactly(6);
    assertThat(network.getBelow(6)).containsExactly(5);
    assertThat(network.getBelow(7)).containsExactly(7);
  }

  @Test
  public void testGetAbove() {
    assertThat(network.getAbove(0)).isEmpty();
    assertThat(network.getAbove(1)).containsExactly(0);
    assertThat(network.getAbove(2)).containsExactly(0);
    assertThat(network.getAbove(3)).containsExactlyInAnyOrder(1, 2);
    assertThat(network.getAbove(4)).isEmpty();
    assertThat(network.getAbove(5)).containsExactly(6);
    assertThat(network.getAbove(6)).containsExactly(5);
    assertThat(network.getAbove(7)).containsExactly(7);
  }

  @Test
  public void testGetOut() {
    assertThat(network.getOut(0)).containsExactlyInAnyOrder(edge01, edge02a, edge02b);
    assertThat(network.getOut(1)).containsExactly(edge13);
    assertThat(network.getOut(2)).containsExactlyInAnyOrder(edge23a, edge23b);
    assertThat(network.getOut(3)).isEmpty();
    assertThat(network.getOut(4)).isEmpty();
    assertThat(network.getOut(5)).containsExactly(edge56);
    assertThat(network.getOut(6)).containsExactlyInAnyOrder(edge65a, edge65b);
    assertThat(network.getOut(7)).containsExactly(edge77);
  }

  @Test
  public void testGetIn() {
    assertThat(network.getIn(0)).isEmpty();
    assertThat(network.getIn(1)).containsExactly(edge01);
    assertThat(network.getIn(2)).containsExactlyInAnyOrder(edge02a, edge02b);
    assertThat(network.getIn(3)).containsExactlyInAnyOrder(edge13, edge23a, edge23b);
    assertThat(network.getIn(4)).isEmpty();
    assertThat(network.getIn(5)).containsExactlyInAnyOrder(edge65a, edge65b);
    assertThat(network.getIn(6)).containsExactly(edge56);
    assertThat(network.getIn(7)).containsExactly(edge77);
  }

  @Test
  public void testGetEdges() {
    assertThat(network.getEdges(0, 1)).containsExactly(edge01);
    assertThat(network.getEdges(0, 2)).containsExactlyInAnyOrder(edge02a, edge02b);
    assertThat(network.getEdges(1, 0)).isEmpty();
    assertThat(network.getEdges(7, 7)).containsExactly(edge77);
  }

  @Test
  public void testGetReverses() {
    assertThat(network.getReverses(edge56)).containsExactlyInAnyOrder(edge65a, edge65b);
    assertThat(network.getReverses(edge65a)).containsExactly(edge56);
    assertThat(network.getReverses(edge01)).isEmpty();
    assertThat(network.getReverses(edge77)).containsExactly(edge77);
  }

  @Test
  public void testGetNodes() {
    ImmutableSet<Integer> nodes = TestUtils.generateNodes(10);
    ImmutableSet<Edge<Integer>> edges = ImmutableSet.of(
            new Edge(0, 1, 1),
            new Edge(0, 6, 5),
            new Edge(0, 7, 6),
            new Edge(0, 8, 7),
            new Edge(1, 2, 10),
            new Edge(1, 3, 1),
            new Edge(2, 5, 1),
            new Edge(3, 4, 1),
            new Edge(4, 5, 1),
            new Edge(5, 2, 10));

    Network<Integer> network = new Network<>(nodes, edges);

    assertThat(network.getNodes(0, 6)).containsExactlyInAnyOrder(0, 1, 3, 4, 5, 6);
  }

  @Test
  public void testMerge() {
    ImmutableSet<Integer> nodesA = TestUtils.generateNodes(3);
    ImmutableSet<Edge<Integer>> edgesA = ImmutableSet.of(
            new Edge(0, 1, 1),
            new Edge(0, 2, 1),
            new Edge(1, 0, 1),
            new Edge(1, 3, 1),
            new Edge(2, 0, 1),
            new Edge(2, 3, 1),
            new Edge(3, 1, 1),
            new Edge(3, 2, 1)
    );

    Network<Integer> networkA = new Network(nodesA, edgesA);

    ImmutableSet<Integer> nodesB = TestUtils.generateNodes(4);
    ImmutableSet<Edge<Integer>> edgesB = ImmutableSet.of(
            new Edge(0, 2, 2),
            new Edge(2, 0, 2),
            new Edge(1, 3, 2),
            new Edge(2, 3, 2),
            new Edge(3, 1, 2),
            new Edge(3, 2, 2)
    );

    Network<Integer> networkB = new Network(nodesB, edgesB);

    Network<Integer> networkMerged = networkA.merge(networkB);

    assertThat(networkMerged.getOut(0).count()).isEqualTo(2);
    assertThat(networkMerged.getEdges(0, 1).findFirst().get().getCost()).isEqualTo(1);
    assertThat(networkMerged.getEdges(0, 2).findFirst().get().getCost()).isEqualTo(2);

    assertThat(networkMerged.getOut(0).count()).isEqualTo(2);
    assertThat(networkMerged.getEdges(1, 0).findFirst().get().getCost()).isEqualTo(1);
    assertThat(networkMerged.getEdges(1, 3).findFirst().get().getCost()).isEqualTo(2);

    assertThat(networkMerged.getOut(0).count()).isEqualTo(2);
    assertThat(networkMerged.getEdges(2, 0).findFirst().get().getCost()).isEqualTo(2);
    assertThat(networkMerged.getEdges(2, 3).findFirst().get().getCost()).isEqualTo(2);

    assertThat(networkMerged.getOut(0).count()).isEqualTo(2);
    assertThat(networkMerged.getEdges(3, 1).findFirst().get().getCost()).isEqualTo(2);
    assertThat(networkMerged.getEdges(3, 2).findFirst().get().getCost()).isEqualTo(2);
  }

}
