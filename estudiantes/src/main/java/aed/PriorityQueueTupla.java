package aed;

import aed.SistemaCNE.PartidoXVoto;

public class PriorityQueueTupla implements IHeap<PartidoXVoto> {
    //Inv rep
    // La longitud del arreglo representacion es siempre igual al tamaño 
    // A PREGUNTAR , SI TENEMOS QUE REAJUSTAR EL ARREGLO PARA QUE SEA UN INVARIANTE MAS "PROPIO"
    private int tamaño;
    private PartidoXVoto[] representacion;

    public PriorityQueueTupla(int buffer) {
        tamaño = 0;
        representacion = new PartidoXVoto[buffer];
    }

    public PriorityQueueTupla(PartidoXVoto[] arreglo) { 
        tamaño = arreglo.length;
        representacion = new PartidoXVoto[tamaño];
        for (int i = 0; i < arreglo.length; i++) {
            representacion[i] = arreglo[i];
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

    public PartidoXVoto maximo() { return representacion[0]; }

    public PartidoXVoto segundoMaximo() { 
        if (tamaño == 2) return representacion[1];
        else return ((representacion[1].compareTo(representacion[2]) > 0) ? representacion[1] : representacion[2]);  
    }

    public void encolar(PartidoXVoto elem) {
        representacion[tamaño] = elem;
        int ielem = tamaño;
        tamaño += 1;
        int ipadre = (ielem - 1) / 2;
        while (representacion[ielem].compareTo(representacion[ipadre]) > 0) {
            intercambiar(ielem, ipadre);
            ielem = ipadre;
            ipadre = (ielem - 1) / 2;
        }
    }

    public PartidoXVoto desencolar() {
        PartidoXVoto res;
        if(tamaño == 0){
           res = null;
        }else{
          res = representacion[0];
        representacion[0] = representacion[tamaño - 1];
        representacion[tamaño-1] = null;
        tamaño -= 1;
        bajar(0);
        }
        return res;
    }

    private void bajar(int raiz) {
        int ihijo = iHijoMayor(raiz);
        while (representacion[ihijo] != null && representacion[raiz].compareTo(representacion[ihijo]) < 0) {
            intercambiar(raiz, ihijo);
            raiz = ihijo;
            ihijo = iHijoMayor(ihijo);
        }
    }

    private void intercambiar(int i, int j) {
        PartidoXVoto temp = representacion[i];
        representacion[i] = representacion[j];
        representacion[j] = temp;
    }

    private int iHijoMayor(int i) {
        int iHijoIzq = i * 2 + 1;
        if (iHijoIzq >= tamaño) return i; //Como los casos son excluyentes, retornamos antes
        int iHijoDer = i * 2 + 2;
        if (iHijoDer >= tamaño) return iHijoIzq;
        else if  (representacion[iHijoDer].compareTo(representacion[iHijoIzq]) > 0)  {return iHijoDer;} else {return iHijoIzq;}
    }

    
}


