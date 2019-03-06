package dev.durga.treeflow.core;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class BiFlow<LEFT_IN,RIGHT_IN, OUT> implements Flow<LEFT_IN,OUT> {

    private Function<LEFT_IN, String> inId;
    private Map<String, RIGHT_IN> externalData;
    private TreeFlow<RIGHT_IN> leftOverTreeFlow;

    public BiFlow(Function<LEFT_IN, String> inId, Map<String, RIGHT_IN> externalData,
        TreeFlow<RIGHT_IN> leftOverTreeFlow) {
        this.inId = inId;
        this.externalData = new TreeMap<>(externalData);
        this.leftOverTreeFlow = leftOverTreeFlow;
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
        if (leftOverTreeFlow != null) {
            leftOverTreeFlow.startFlow(externalData.values());
        }
    }

    public static <L, R, O> BiFlow<L, R, O> create(
            Function<L, String> inId,
            Map<String, R> externalData,
            BiFunction<L, R, O> biFunction,
            TreeFlow<R> leftOverTreeFlow
    ) {
        return new BiFlow<L, R, O>(inId, externalData, leftOverTreeFlow) {
            @Override
            public O biFlow(L l, R r) {
                return biFunction.apply(l, r);
            }
        };
    }
}
