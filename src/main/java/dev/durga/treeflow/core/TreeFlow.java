package dev.durga.treeflow.core;

import dev.durga.treeflow.core.flow.Sink;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class TreeFlow<IN> {

    public static <IN> TreeFlow<IN> create(Node<IN, ?> node, Sink<IN> errFlow) {
        return new TreeFlow<>(node, errFlow);
    }

    public static <IN> TreeFlow<IN> create(Node<IN, ?> node) {
        return new TreeFlow<>(node, null);
    }

    private Sink<IN> errFlow;
    private Node<IN, ?> node;


    private TreeFlow(Node<IN, ?> node, Sink<IN> errFlow) {
        this.node = node;
        this.errFlow = errFlow;
    }

    public void startFlow(Collection<IN> ins) {
        this.node.start();

      Supplier<IN> source = getSupplier(ins);

      while(true) {

            IN in = source.get();
            Enriched enriched = new Enriched();

            try {
                this.node.flow(in, enriched);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (this.errFlow != null) {
                    this.errFlow.flow(in, enriched);
                }
            }

            if (in == null) {
                break;
            }
        }

        this.node.end();
    }

  private Supplier<IN> getSupplier(Collection<IN> ins) {
    return new Supplier<IN>() {
              private final Iterator<IN> iterator = ins.iterator();
              @Override
              public IN get() {
                  return this.iterator.hasNext() ? this.iterator.next() : null;
              }
          };
  }


}
