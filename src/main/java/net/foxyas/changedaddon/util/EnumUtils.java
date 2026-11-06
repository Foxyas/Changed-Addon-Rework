package net.foxyas.changedaddon.util;

public class EnumUtils {
    
    /**
     * Retorna o próximo valor do enum, voltando para o primeiro ao chegar no fim.
     */
    public static <T extends Enum<T>> T next(T current) {
        T[] values = current.getDeclaringClass().getEnumConstants();
        return values[(current.ordinal() + 1) % values.length];
    }
}