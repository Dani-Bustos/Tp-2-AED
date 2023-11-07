package aed;

import aed.SistemaCNE.PartidoXVoto;

public class Heap implements IHeap<PartidoXVoto> {
    
    private int tamaño;
    private PartidoXVoto[] rep;

    public Heap(int buffer) {
        tamaño = 0;
        rep = new PartidoXVoto[buffer];
    }

    public Heap(PartidoXVoto[] arreglo) { 
        tamaño = arreglo.length;
        rep = new PartidoXVoto[tamaño];
        for (int i = 0; i < arreglo.length; i++) {
            rep[i] = arreglo[i];
        }
        for (int h = altura() - 1; h > 0; h--) { //algoritmo de Floyd
            for (int i = (int)Math.pow(2, h-1) - 1; i < (int)Math.pow(2, h) - 1; i++) {
                bajar(i);
            }
        }
    }

    public int altura() {
        int altura = 0;
        for (int i = 0; i < tamaño; i = i * 2 + 1) altura++;
        return altura;
    }

    public int cardinal() { return tamaño; }

    public PartidoXVoto maximo() { return rep[0]; }

    public PartidoXVoto segundoMaximo() { 
        if (tamaño == 2) return rep[1];
        else return ((rep[1].compareTo(rep[2]) > 0) ? rep[1] : rep[2]);  
    }

    public void encolar(PartidoXVoto elem) {
        rep[tamaño] = elem;
        int ielem = tamaño;
        tamaño += 1;
        int ipadre = (ielem - 1) / 2;
        while (rep[ielem].compareTo(rep[ipadre]) > 0) {
            intercambiar(ielem, ipadre);
            ielem = ipadre;
            ipadre = (ielem - 1) / 2;
        }
    }

    public void desencolar() {
        rep[0] = rep[tamaño - 1];
        tamaño -= 1;
        bajar(tamaño);
    }

    private void bajar(int raiz) {
        int ihijo = iHijoMayor(raiz);
        while (rep[raiz].compareTo(rep[ihijo]) < 0) {
            intercambiar(raiz, ihijo);
            raiz = ihijo;
            ihijo = iHijoMayor(ihijo);
        }
    }

    private void intercambiar(int i, int j) {
        PartidoXVoto temp = rep[i];
        rep[i] = rep[j];
        rep[j] = temp;
    }

    private int iHijoMayor(int i) {
        int iHijoIzq = i * 2 + 1;
        if (iHijoIzq >= tamaño) return i; //cuidado
        int iHijoDer = i * 2 + 2;
        if (iHijoDer >= tamaño) return iHijoIzq;
        else return (rep[iHijoDer].compareTo(rep[iHijoIzq]) > 0) ? iHijoDer : iHijoIzq;
    }

    
}


