package dev.durga.treeflow.core;

import java.util.List;

public interface Flow<IN, OUT> {

    default void start(){};

    OUT apply(IN in, Enriched enriched);

    default OUT flow(IN in, Enriched enriched) {
        OUT out = apply(in, enriched);
        enriched.enrichWith(out);
        return out;
    }

    default void end() {};

}
