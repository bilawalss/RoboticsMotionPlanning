package utils;

/* convenient class for return two values */
public class PairRes<T, V> {
    public T first;
    public V second;

    public PairRes(T first, V second) {
        this.first = first;
        this.second = second;
    }
}
