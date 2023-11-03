package aed;

public interface IHeap<T> {
    public T proximo(); //O(1)
    public void encolar(T elem); //O(log n)
    public void desencolar(); //O(log n)
}