package dev.durga.treeflow.core;


import java.util.function.Supplier;

public class TreeFlow<IN> {

    private Supplier<IN> source;
    private Node<IN, ?> node;

    private Flow<IN, Void> errFlow;

    public TreeFlow(Supplier<IN> source, Node node, Flow<IN, Void> errFlow) {
        this.source = source;
        this.node = node;
        this.errFlow = errFlow;
    }

    public void flow() {
        this.node.start();

        while(true) {

            IN in = source.get();
            Enriched enriched = new Enriched();

            try {
                this.node.flow(in, enriched);
            } catch (Exception ex) {
                ex.printStackTrace();
                if (errFlow != null) {
                    errFlow.flow(in , enriched);
                }
            }

            if (in == null) {
                break;
            }
        }

        this.node.end();
    }
}
