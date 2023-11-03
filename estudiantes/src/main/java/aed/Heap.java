package aed;

public class Heap implements IHeap {

    private int tamaño;
    private int[] rep;

    public Heap(int buffer) {
        tamaño = 0;
        rep = new int[buffer];
    }

    public Heap(int[] arreglo) {
        //FALTA
        rep = new int[arreglo.length];
    }

    public int maximo() { return rep[0]; }

    public int segundoMaximo() { 
        if (tamaño == 2) return rep[1];
        else return ((rep[1] > rep[2]) ? rep[1] : rep[2]);  
    }

    public void encolar(int elem) {
        rep[tamaño] = elem;
        int ielem = tamaño;
        tamaño += 1;
        int ipadre = (ielem - 1) / 2;
        while (rep[ielem] > rep[ipadre]) {
            intercambiar(ielem, ipadre);
            ielem = ipadre;
            ipadre = (ielem - 1) / 2;
        }
    }

    public void desencolar() {
        rep[0] = rep[tamaño - 1];
        tamaño -= 1;
        int ielem = 0;
        int ihijo = iHijoMayor(ielem);
        while (rep[ielem] < rep[ihijo]) {
            intercambiar(ielem, ihijo);
            ielem = ihijo;
            ihijo = iHijoMayor(ihijo);
        }
    }

    private void intercambiar(int i, int j) {
        int temp = rep[i];
        rep[i] = rep[j];
        rep[j] = temp;
    }

    private int iHijoMayor(int i) {
        int iHijoIzq = i * 2 + 1;
        if (iHijoIzq >= tamaño) return i; //cuidado
        int iHijoDer = i * 2 + 2;
        if (iHijoDer >= tamaño) return iHijoIzq;
        else return (rep[iHijoDer] > rep[iHijoIzq]) ? iHijoDer : iHijoIzq;
    }

}
