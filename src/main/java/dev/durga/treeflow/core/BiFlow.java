package dev.durga.treeflow.core;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BiFlow<LEFT_IN,RIGHT_IN, OUT> implements Flow<LEFT_IN,OUT> {

    private Function<LEFT_IN, String> inId;
    private Map<String, RIGHT_IN> externalData;

    public BiFlow(Function<LEFT_IN, String> inId, Map<String, RIGHT_IN> externalData) {
        this.inId = inId;
        this.externalData = externalData;
    }

    public abstract OUT biFlow(LEFT_IN leftIn, RIGHT_IN rightIn);

    @Override
    public OUT apply(LEFT_IN leftIn, Enriched enriched) {
        String id = inId.apply(leftIn);
        RIGHT_IN rightIn = externalData.get(id);
        OUT out = biFlow(leftIn, rightIn);
        externalData.remove(id);
        return out;
    }

    @Override
    public void end() {
        externalData.values().forEach( rightIn ->
                biFlow(null, rightIn)
        );
    }

    public static <L, R, O> BiFlow<L, R, O> create(
            Function<L, String> inId,
            Map<String, R> externalData,
            BiFunction<L, R, O> biFunction) {
        return new BiFlow<L, R, O>(inId, externalData) {
            @Override
            public O biFlow(L l, R r) {
                return biFunction.apply(l, r);
            }
        };
    }
}
