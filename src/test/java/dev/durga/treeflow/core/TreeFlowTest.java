package dev.durga.treeflow.core;

import dev.durga.treeflow.core.flow.Sink;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class TreeFlowTest {

  @Test
  public void name() {

    List<DataA> dataAList = Arrays.asList(
        new DataA(UUID.randomUUID().toString()),
        new DataA(UUID.randomUUID().toString()),
        new DataA(UUID.randomUUID().toString())
    );

    Sink<DataC> sinkZero = new Sink<DataC>();
    Sink<DataC> sinkOne = new Sink<DataC>();
    Sink<DataC> sinkTwo = new Sink<DataC>();

    Sink<DataD> globalSink = new Sink<DataD>();

    Node<DataA, DataA> rootNode = Node.<DataA>newNode()
        .map(a -> new DataB(a.getFieldA()))
        .map(b -> new DataC(b.getFieldB()))
        .child(
            Node.<DataC>newNode()
                .map(e -> Math.abs(e.getFieldC().hashCode() % 3) == 0 ? e : null)
                .sink(sinkZero)
        )
        .child(
            Node.<DataC>newNode()
                .map(e -> Math.abs(e.getFieldC().hashCode() % 3) == 1 ? e : null)
                .sink(sinkOne)

        )
        .child(
            Node.<DataC>newNode()
                .map(e -> Math.abs(e.getFieldC().hashCode() % 3) == 2 ? e : null)
                .sink(sinkTwo)
        )
        .mapWithEnrich(e -> e.getData(DataD.class))
        .sink(globalSink);


    TreeFlow.create(rootNode).startFlow(dataAList);



    System.out.println("Zero data");
    System.out.println(sinkZero.getAllData());

    System.out.println("One data");
    System.out.println(sinkOne.getAllData());

    System.out.println("Two data");
    System.out.println(sinkTwo.getAllData());

    System.out.println("Global Sink");
    System.out.println(globalSink.getAllData());

  }
}