package aed;

import aed.SistemaCNE.PartidoXVoto;

public class PriorityQueueTupla implements IHeap<PartidoXVoto> {
    //Inv rep
    // La longitud del arreglo representacion es siempre igual al tamaño 
    // El primer elemento de representacion es el mayor, se cumple el invariante del heap en la representacion
    // Es decir: El ultimo nivel esta "lleno" desde la izquierda, es izquierdista. 
    // Valiendo que todos los padres son mayores o iguales que sus hijos, si los tiene.
    // Si una posicion de representacion es null, todas las que le siguen seran tambien null
    // La representacion es un arbol binario perfectamente balanceado, implementado sobre arreglo
    private int tamaño;
    private PartidoXVoto[] representacion;
    
    //Complejidad O(1)
    public PriorityQueueTupla(int buffer) {
        tamaño = 0;
        representacion = new PartidoXVoto[buffer];
    }
   
    //Complejidad : O(n), siendo n la longitud del arreglo. 
    public PriorityQueueTupla(PartidoXVoto[] arreglo) { 
        tamaño = arreglo.length;
        //Evitamos aliasing
        representacion = new PartidoXVoto[tamaño];
        for (int i = 0; i < arreglo.length; i++) {
            representacion[i] = arreglo[i];
        }
        //Observar que la altura es menor o igual a la longitud del arreglo
        for (int h = altura() - 1; h > 0; h--) { //Algoritmo de Floyd para Array2heap
            for (int i = (int)Math.pow(2, h-1) - 1; i < (int)Math.pow(2, h) - 1; i++) {
                bajar(i);
            }
        }
    }

    // Complejidad : O(n)
    public int altura() {
        int altura = 0;
        for (int i = 0; i < tamaño; i = i * 2 + 1) altura++;
        return altura;
    }
     
    // Complejidad : O(1)
    public int cardinal() { return tamaño; }
    
    // Complejidad : O(1), indexar arreglo
    public PartidoXVoto maximo() { return representacion[0]; }
    
    // Complejidad : O(1), indexar arreglo y comparacion
    public PartidoXVoto segundoMaximo() { 
        if (tamaño == 2) return representacion[1];
        else {if (representacion[1].compareTo(representacion[2]) > 0){  
            return  representacion[1]; 
            } 
            else {  
                return representacion[2];
            }  
        } 
    }
    
    //Complejidad O(log(n))
    public void encolar(PartidoXVoto elem) {
        
        representacion[tamaño] = elem;
        int ielem = tamaño;
        tamaño += 1;
        int ipadre = (ielem - 1) / 2;
        // Lo maximo que podemos "trepar" por el arbol es su altura esta siendo log(n), con n la longitud de la representacion
        while (representacion[ielem].compareTo(representacion[ipadre]) > 0) {
            intercambiar(ielem, ipadre);
            ielem = ipadre;
            ipadre = (ielem - 1) / 2;
        }
    }
    //Complejidad O(log(n))
    public PartidoXVoto desencolar() {
        PartidoXVoto res;
        if(tamaño == 0){
           res = null;
        }else{
          res = representacion[0];
        representacion[0] = representacion[tamaño - 1];
        representacion[tamaño-1] = null;
        tamaño -= 1;
        //O(log(n))
        bajar(0);
        }
        return res;
    }
    
    //Complejidad O(log(n))
    private void bajar(int raiz) {
        //O(log(n)) , lo maximo que podemos "bajar" en el arbol desde la raiz es su altura
        // esta siendo log(n), con n el tamaño de la representacion
        int ihijo = iHijoMayor(raiz);
        while (representacion[ihijo] != null && representacion[raiz].compareTo(representacion[ihijo]) < 0) {
            intercambiar(raiz, ihijo);
            raiz = ihijo;
            ihijo = iHijoMayor(ihijo);
        }
    }
    
      //Complejidad O(1)
    private void intercambiar(int i, int j) {
        //O(1), indexacion de arreglo
        PartidoXVoto temp = representacion[i];
        representacion[i] = representacion[j];
        representacion[j] = temp;
    }
    
    //Complejidad O(1)
    private int iHijoMayor(int i) {
        //O(1), comparacion e indexacion de arreglo
        int iHijoIzq = i * 2 + 1;
        if (iHijoIzq >= tamaño) return i; //Como los casos son excluyentes, retornamos antes
        int iHijoDer = i * 2 + 2;
        if (iHijoDer >= tamaño) return iHijoIzq;
        else if  (representacion[iHijoDer].compareTo(representacion[iHijoIzq]) > 0)  {return iHijoDer;} else {return iHijoIzq;}
    }

    
}


