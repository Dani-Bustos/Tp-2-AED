package aed;

public interface IHeap {
    public int proximo(); //O(1)
    public void encolar(int elem); //O(log n)
    public void desencolar(); //O(log n)
    public int segundoMaximo(); //O(1)
}
