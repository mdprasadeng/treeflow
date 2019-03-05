package dev.durga.treeflow.core;

import org.junit.Test;

import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class TreeFlowTest {

    @Test
    public void name() {

        Supplier<DataA> supplier = new Supplier<DataA>() {

            int count = 10;

            @Override
            public DataA get() {
                count--;
                if (count < 0) return null;
                return new DataA(UUID.randomUUID().toString());
            }
        };

        Node<DataA, DataA> rootNode = Node.<DataA>newNode()
                .map(a -> new DataB(a.getFieldA()))
                .map(b -> new DataC(b.getFieldB()))
                .mapWithEnrich(enriched -> {
                    return new DataD(enriched.getData(DataC.class).getFieldC());
                })
                .child(
                        Node.<DataC>newNode()
                        .map(e -> {
                            if (e.getFieldC().hashCode() %3 == 0) {
                                System.out.println("in 0 " + e);
                                return e;
                            } else {
                                return null;
                            }
                        })
                        .getRoot()

                )
                .child(
                        Node.<DataC>newNode()
                                .map(e -> {
                                    if (e.getFieldC().hashCode() %3 == 1) {
                                        System.out.println("in 1 " + e);
                                        return e;
                                    } else {
                                        return null;
                                    }
                                })
                                .getRoot()

                )
                .child(
                        Node.<DataC>newNode()
                                .map(e -> {
                                    if (e.getFieldC().hashCode() %3 == 2) {
                                        System.out.println("in 2 " + e);
                                        return e;
                                    } else {
                                        return null;
                                    }
                                })
                                .getRoot()

                )
                .getRoot();


        new TreeFlow<DataA>(
                supplier,
                rootNode,
                null
        ).flow();


    }
}