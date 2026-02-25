package net.zaharenko424.cmrs.util;

import java.util.ArrayList;
import java.util.List;

public abstract class NonPoolablePool <E> {

    List<E> pool = new ArrayList<>(8);

    protected abstract E newObject();

    protected abstract void reset(E element);

    public E obtain(){
        if(pool.isEmpty()) return newObject();

        return pool.remove(pool.size() - 1);
    }

    public void free(E element){
        reset(element);
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
}