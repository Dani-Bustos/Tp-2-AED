package aed;

public interface IHeap <T>{
    public int cardinal(); // O(1)
    public T maximo(); // O(1)
    public void encolar(T elem); // O(log n)
    public T  desencolar(); // O(log n)
    
}