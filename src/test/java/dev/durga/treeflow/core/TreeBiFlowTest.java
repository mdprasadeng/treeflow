package dev.durga.treeflow.core;

import dev.durga.treeflow.core.flow.Sink;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class TreeBiFlowTest {

  @Test
  public void name() {

    List<DataA> dataAList = Arrays.asList(
        new DataA("ONE"),
        new DataA("TWO"),
        new DataA("THREE")
    );

    Map<String, DataB> dataMap = new HashMap<>();
    dataMap.put("TWO", new DataB("TWO"));
    dataMap.put("THREE", new DataB("THREE"));
    dataMap.put("FOUR", new DataB("FOUR"));
    dataMap.put("FIVE", new DataB("FIVE"));

    Sink<DataA> sinkA = new Sink<DataA>();
    Sink<DataB> sinkB = new Sink<DataB>();

    Node<DataA, DataA> rootNode = Node.<DataA>newNode()
        .reconcile(
            DataA::getFieldA,
            dataMap,
            (dataA, dataB) -> {
              return dataA != null && dataB == null ? dataA : null;
            },
            TreeFlow.create(Node.<DataB>newNode().sink(sinkB))
            )
        .sink(sinkA);

    TreeFlow.create(rootNode).startFlow(dataAList);


    System.out.println("Sink A data");
    System.out.println(sinkA.getAllData());

    System.out.println("Sink B data");
    System.out.println(sinkB.getAllData());



  }
}