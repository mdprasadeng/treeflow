package dev.durga.treeflow.core;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Enriched {


    private Map<Class, Object> enriched = new HashMap<>();

    public <OUT> void enrichWith(OUT out) {

        if (out == null || enriched.get(out.getClass()) == out) return;

        if (enriched.get(out.getClass()) != null) {
            throw new IllegalStateException("Already enriched with class" + out.getClass().getSimpleName());
        }

        enriched.put(out.getClass(), out);
    }

    public <T> T getData(Class<T> tClass) {
        return (T) enriched.get(tClass);
    }
}


