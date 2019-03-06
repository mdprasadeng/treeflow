package dev.durga.treeflow.core.flow;

import dev.durga.treeflow.core.Enriched;
import java.util.ArrayList;
import java.util.List;

public class Sink<IN> implements Flow<IN, Void>{

    private List<IN> allData = new ArrayList<>();

    @Override
    public Void apply(IN in, Enriched enriched) {
        allData.add(in);
        return null;
    };

    public List<IN> getAllData() {
        return allData;
    }
}
