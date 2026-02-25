package net.zaharenko424.cmrs.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Pool <E extends Pool.Poolable> {

    protected final List<E> pool = new ArrayList<>(8);

    protected abstract E newObject();

    public E obtain(){
        if(pool.isEmpty()) return newObject();

        return pool.remove(pool.size() - 1);
    }

    public void free(E element){
        element.reset();
        pool.add(element);
    }

    public void fill(int minSize){
        int toFill = minSize - pool.size();
        if(toFill <= 0) return;

        for(; toFill > 0; toFill--){
            pool.add(newObject());
        }
    }

    public int size(){
        return pool.size();
    }

    public void clear(){
        pool.clear();
    }

    public interface Poolable {

        void reset();
    }
}