package dev.durga.treeflow.core.flow;

import dev.durga.treeflow.core.Enriched;
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
