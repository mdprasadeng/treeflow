package dev.durga.treeflow.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class Source<OUT> implements Supplier<OUT> {

    private final Iterator<OUT> iterator;

    public Source(Collection<OUT> sourceData) {
        this.iterator = sourceData.iterator();
    }

    @Override
    public OUT get() {
        if (this.iterator.hasNext()) {
            return this.iterator.next();
        } else {
            return null;
        }
    }
}
