package aed;

public interface IHeap {
    public int cardinal(); //O(1)
    public int maximo(); //O(1)
    public void encolar(int elem); //O(log n)
    public void desencolar(); //O(log n)
    public int segundoMaximo(); //O(1)
}
