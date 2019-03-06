package dev.durga.treeflow.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Node<IN, OUT> {

    public static <IN> Node<IN, IN> newNode() {
        Node<IN, IN> node = new Node<>((in, enriched) -> in);
        node.root = node;
        return node;
    }

    private Node root;

    private Flow<IN, OUT> flow;

    private List<Node<OUT, ?>> children = new ArrayList<>();

    private Node(Flow<IN, OUT> flow) {
        this.flow = flow;
    }

    private void addChild(Node<OUT, ?> child) {
        child.root = this.root;
        this.children.add(child);
    }

    public <X,Y> Node<X,Y> getRoot() {
        return root;
    }

    public <X,Y> Node<X,Y> sink(Sink<OUT> flow) {
        Node<OUT, Void> outNode = new Node<>(flow);
        addChild(outNode);
        return this.root;
    }

    public <DATA> Node<OUT, DATA> map(Function<OUT, DATA> func) {
        Node<OUT, DATA> outNode = new Node<>((out, enriched) -> func.apply(out));
        addChild(outNode);
        return outNode;
    }


    public <DATA> Node<OUT, OUT> enrich(Function<Enriched, DATA> func) {
        Node<OUT, OUT> outNode = new Node<>((out, enriched) -> {
            DATA data = func.apply(enriched);
            enriched.enrichWith(data);
            return out;
        });
        addChild(outNode);
        return outNode;
    }

    public <DATA> Node<OUT, DATA> mapWithEnrich(Function<Enriched, DATA> func) {
        Node<OUT, DATA> outNode = new Node<>((out, enriched) -> {
            DATA data = func.apply(enriched);
            enriched.enrichWith(data);
            return data;
        });
        addChild(outNode);
        return outNode;
    }



    public <EXT, DATA> Node<OUT, DATA> reconcile(
        Function<OUT, String> outId,
        Map<String,EXT> externalData,
        BiFunction<OUT, EXT, DATA> biFlow,
        TreeFlow<EXT> lefOverTreeFlow
        ) {
        Node<OUT, DATA> outNode = new Node<>(BiFlow.create(outId, externalData, biFlow, lefOverTreeFlow));
        addChild(outNode);
        return outNode;
    }

    public Node<IN, OUT> child(Node<OUT, ?> childNode) {
        addChild(childNode);
        return this;
    }

    public void start() {
        if (flow != null) {
            flow.start();
        }

        this.children.forEach(Node::start);
    }

    public void flow(IN in, Enriched enriched) {
        if (flow != null) {
            OUT out = this.flow.flow(in, enriched);
            if (out != null) {
                this.children.forEach(child -> child.flow(out, enriched));
            }
        }
    }

    public void end() {
        if (flow != null) {
            flow.end();
        }

        this.children.forEach(Node::end);
    }



}
