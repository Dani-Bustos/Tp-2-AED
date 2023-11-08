package aed;

import aed.SistemaCNE.PartidoXVoto;

public class SistemaCNE {
    private Presidencial votosPresidente;
    private Diputados votosDiputados;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el ultimo elemento de la mesa, no incluido
    // INV REP: A TERMINAR
    public class  PartidoXVoto implements Comparable<PartidoXVoto>{
           int idPartido;
           int Votos;
           // Inv Rep
           // Tanto id partido como votos son numeros positivos o 0.
           
           //Complejidad O(1)
           PartidoXVoto(int numPartido, int votos){
                this.idPartido = numPartido;
                this.Votos = votos;
            }
        
             //Complejidad O(1), comparacion
            @Override
            public int compareTo(PartidoXVoto a){
                //Definimos criterio de comparación en las tuplas como la comparación numerica de sus 1eros elementos
                //para poder asi utilizarla en el heap
                if(this.Votos > a.Votos){
                    return 1;
                }else if(this.Votos < a.Votos){
                    return -1;
                }else{
                    return 0;
                }
            }
        
        } 
    
    public class Presidencial{
        private int[] _arrPresidente;
        private int _Total;
        private int idPrimero;
        private int idSegundo;
        //BORRADOR 
        // INV REP: El idPrimero es la posicion de _arrPresidente en la cual se encuentra el elemento mas grande del
        // arreglo salvo la ultima posicion, es decir, es el partido (sin contar los votos en blanco) con mas votos.
        // El idSegundo es la posicion de _arrPresidencial en la cual se encuentra el elemento que es menor o igual al mas grande,
        // pero mayor a todos los demas elementos salvo la ultima posicion, es decir, es el segundo partido (sin contar los votos en blanco) con mas votos.
        // El total es la suma de todos los elementos de _arrPresidencial, es decir, el total de votos.
        // _arrPresidencial es un arreglo en el cual en la posicion i-esima se encuentra la cantidad de votos para presidente que obtuvo
        // el i-esimo partido. En la ultima posicion del arreglo se encuentra la cantidad de votos en blanco. Todos los votos son numeros positivos o 0.
        
        //Complejidad Theta(n), siendo n el parametro "longitud"
        Presidencial(int longitud){
            this._arrPresidente = new int[longitud];
            _Total = 0;
            idPrimero = 0;
            idSegundo = 1;
        }
        
        //Complejidad O(1), indexar arreglo
        int get(int id){
            return _arrPresidente[id];
        }
        
        //Complejidad O(1), indexar arreglo
        int max(){
         return _arrPresidente[idPrimero];
       } 
        
       //Complejidad O(1), indexar arreglo
        int sdoMax(){
            return _arrPresidente[idSegundo];
        }
      
        //Complejdad O(1), retornar int
       int total(){
            return _Total;
        }
        //Complejidad O(P),
        void actualizar(int[] votos){
        // O(P), se recorre e indexa el arreglo
        for(int i = 0;i < votos.length;i++){
            _Total += votos[i];
            _arrPresidente[i] += votos[i];
           if(i != votos.length-1){ //No queremos que los votos en blanco puedan ser primeros
            
            
            if(_arrPresidente[i] > _arrPresidente[idPrimero]){
                idSegundo = idPrimero;
                idPrimero = i;
           }
           //debemos utiliza Mayor igual que el primero porque sino puede ocurrir que el primero sea el mismo partido que el segundo
           else if (_arrPresidente[i] > _arrPresidente[idSegundo] && !(_arrPresidente[i] >= _arrPresidente[idPrimero])){
                idSegundo = i;
           }
        }
        }
        
        }
    }
    public class Diputados{
        private int[][] votosXDistrito; //Primer Array distrito, segundo array votos de cada partido 
        private PriorityQueueTupla[] heapXDistrito;
        private int[] total;
        private int[][] resultadosPrecalculados;
        private boolean[] heapValido;
        // Inv Rep
        // Para toda posiicon de VotosXDistrito, se cumple que todos los elementos de el arreglo en esa posicion, son positivos o 0
        // Todos los arreglos en VotosXDistrito tienen la misma longitud. Aqui guardamos los votos de cada partido en cada distrito
        
        // La posicion iesima del arreglo total estan la suma de los elementos del arreglo votosXDistrito en la posicion iesima.
        // Es decir los votos totales del distrito son la suma de los votos del distrito
        // El largo de VotosXDistrito es igual  al tamaño de heapXDistrito. El largo de heapValido es igual al largo de heapxDistrito;
        // El largo de resultados precalculados es igual al largo de VotosXDistrito, y todos sus elementos tienen la longitud de la cantidad de partidos menos el blanco
        // (cantidad de partidos siento la longitud de el elemento votosXDistirto[0])
        
        // El heapValido iesimo es True si solo si para cada posicion de votosXDistrito, se cumple que todos elementos 
        //(salvo los votos en blanco) que sean superior el 3% de el total iesimo de ese arreglo estan en la representacion del heapXdistirto iesimo.
        // Es decir, en el heap guardamos los votos de aquellos partidos que pasan el umbral de 3% en su distrito, Y heap valido nos indica si ese heap esta 
        // intacto o no. Si no lo estuviese utilizamos el resultado precalculado iesimo(que corresponde al distrito). 
        
        //Complejidad de O(D*P)
        Diputados(int longitudDistritos, int longitudPartidos){
          // Crear un arreglo de longitud n tiene complejidad Theta(n)
          // Si creamos D arreglos de longitud P, nuestra complejidad es de Theta(D*P)
          // Las demas creaciones de arreglos no afectan ya que Theta(D + D*P) pertenece a Theta(D*P)
          
          votosXDistrito = new int[longitudDistritos][longitudPartidos]; 
          heapValido = new boolean[longitudDistritos];
          heapXDistrito = new PriorityQueueTupla[longitudDistritos];
          resultadosPrecalculados = new int[longitudDistritos][longitudPartidos-1];
          total = new int[longitudDistritos];    
        }
        
        //O(1), indexacion de arreglos
        int get(int idDistrito, int idPartido){
              return votosXDistrito[idDistrito][idPartido];
        }
       
                
        // Complejidad O(P)
        void actualizarDistrito(int idDistrito,PartidoXVoto[] votosMesa){
            int pasa3PorCiento = 0;
            // Theta(P), votosMesa tiene longitud de los Partidos
            for(int i = 0; i < votosMesa.length;i++){
                votosXDistrito[idDistrito][i] += votosMesa[i].Votos;
                total[idDistrito] += votosMesa[i].Votos;
            }
            
            //Contamos cuantos pasan el 3% para definir la longitud del arreglo que sera el Heap, sin votos en blanco
            // Theta(P)
            for(int i = 0; i<votosXDistrito[idDistrito].length-1;i++){
                //calculo del 3% ajustado para evitar floating point
                if(votosXDistrito[idDistrito][i]*100 >= total[idDistrito]*3){ 
                     pasa3PorCiento ++;
                }
            }
            
            //Creamos el array con aquellos q pasan el 3 por ciento, que luego sera el heap;
            //Necesitamos un indice separado para el segundo arreglo, ya que no se corresponden las posiciones con el original
            //a la hora de asignar
            //Theta(P), observar que crear un Nuevo "PartidoXVoto" es O(1), opera como una tupla
            PartidoXVoto[] votosPasanMargen = new PartidoXVoto[pasa3PorCiento];
            int indice_arreglo = 0;
            for(int i = 0; i < votosXDistrito[idDistrito].length-1;i++){
                if (votosXDistrito[idDistrito][i] * 100 >= total[idDistrito] * 3) {
                    votosPasanMargen[indice_arreglo] = new PartidoXVoto(i, votosXDistrito[idDistrito][i]);
                    votosPasanMargen[indice_arreglo].Votos = votosXDistrito[idDistrito][i];
                    votosPasanMargen[indice_arreglo].idPartido = i;
                    indice_arreglo++;
                }
            }
            //Internamente el HeapXDistrito utiliza el algoritmo de Floyd cuando se le pasa un arreglo, de complejidad lineal
            //O(P)
            heapXDistrito[idDistrito] = new PriorityQueueTupla(votosPasanMargen);
           
            heapValido[idDistrito] = true;
            
        }
       
        int[] devolverBancas(int idDistrito,int cantidadBancas) {
            // Chequeamos si ya calculamos el resultado de este distrito
            if (heapValido[idDistrito]) {
                //O(P)
                int[] bancas = new int[votosXDistrito[0].length - 1];
                
                for (int i = 0; i < cantidadBancas; i++) {
                    //O(log(P)), en el peor caso todos los partidos pasan el margen de 3 por ciento
                    PartidoXVoto max = heapXDistrito[idDistrito].desencolar();
                    if(max != null){

                    
                    bancas[max.idPartido] += 1;
                    PartidoXVoto recalculado = new PartidoXVoto(max.idPartido,
                            (votosXDistrito[idDistrito][max.idPartido]) / (bancas[max.idPartido] + 1));
                    heapXDistrito[idDistrito].encolar(recalculado);
                    }
                }
                resultadosPrecalculados[idDistrito] = bancas;
                heapValido[idDistrito] = false;
                return bancas;
            
            
            } else {
                return resultadosPrecalculados[idDistrito];
            }
        }
    }
    
    
    public class VotosPartido{
        private int presidente;
        private int diputados;
        // Inv Rep:
        // Tanto presidentes como Diputados son numeros positivos o 0
        
        
        //Complejidad O(1), asignacion de valores 2 veces;
        VotosPartido(int presidente, int diputados)
        {
            this.presidente = presidente; this.diputados = diputados;
        }
        
        //Complejidad O(1)
        public int votosPresidente(){return presidente;}
        
        //Complejidad O(1)
        public int votosDiputados(){return diputados;}
    }

    //Complejidad O(D*P), siendo D la cantidad de Distritos y P la de Partidos
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
     nomDistrito = nombresDistritos;
     bancasXDistrito = diputadosPorDistrito;
     nomPartido = nombresPartidos;
     mesas = ultimasMesasDistritos;
     //O(P) , justificado en la clase
     votosPresidente = new Presidencial(nombresPartidos.length);
     //O(D*P), justificado en la clase
     votosDiputados = new Diputados(nombresDistritos.length,nombresPartidos.length);
    }
    
    //Complejidad O(1), indexar arreglo
    public String nombrePartido(int idPartido) {
        return nomPartido[idPartido];
    }
    
    //Complejidad O(1), indexar arreglo
    public String nombreDistrito(int idDistrito) {
        return nomDistrito[idDistrito];
    }
    
    //Complejidad O(1), indexar arreglo
    public int diputadosEnDisputa(int idDistrito) {
        return bancasXDistrito[idDistrito];
    }
    
    //Complejidad O(1), indexar arreglo
    public String distritoDeMesa(int idMesa) {
        int pos = ObtenerIdDistrito(idMesa);
        return nombreDistrito(pos);
    }
    
    
    
    // Complejidad O(P + log(D))
    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        //Actualizamos Votos Presidenciales
        
        //Complejidad O(P)
        int []vPres = new int[actaMesa.length];
        for(int i = 0;i < actaMesa.length;i++){
            vPres[i] = i;
            vPres[i] = actaMesa[i].presidente;
        }
        //Complejidad O(P), justificacion en clase VotosPresidente
        votosPresidente.actualizar(vPres);
       
        //Actualizamos Votos Diputados
        
        //O(log(D)) Obtener id Distrito usa una busqueda binaria sobre un arreglo ordenado , de longitud distritos
        int distritoMesa = ObtenerIdDistrito(idMesa);
        //O(D), creacion de tupla es O(1), realizado tantas veces como la cantidad de distritos
        PartidoXVoto[] vDiputados = new PartidoXVoto[actaMesa.length];
        for (int i = 0; i < actaMesa.length; i++) {
            vDiputados[i] = new PartidoXVoto(i, actaMesa[i].diputados);
            
        }
        //Complejidad : O(P), justificacion en la clase VotosDiputados
        votosDiputados.actualizarDistrito(distritoMesa,vDiputados);
    }
 
    //Complejidad O(log(n)), siendo  n la longitud del arreglo pasado
    // Utilizamos una busqueda binaria para obtenerlo, ya que segun el requiere las mesas son siempre un arreglo ordenado.
    // La busqueda binaria tiene complejidad logaritimica, ya que reduce el rango de busqueda a la mitad en cada iteracion del while
    private int ObtenerIdDistrito(int elem){
        int izq = 0;
        int der = mesas.length;
        int medio = (izq + der)/2;
        while(izq != der){
           if(mesas[medio] == elem){
            return medio + 1;   // el arreglo es no incluido, con lo cual si lo encontramos, es la siguiente pos de mesa
           }else if(der - izq == 1 && elem != mesas[der] && elem != mesas[izq]){ //caso en el que no pertenezeca a la lista directamente
            if(elem < mesas[izq]){
            return izq;
            } 
            else{
            return der;
            }
           }else if(mesas[medio] > elem){
            der = medio;
            
           }else if (mesas[medio] < elem){
            izq = medio;
            
           }
            medio = (der + izq)/2;    
        }
        return medio;
    }

    //Complejidad O(1)
    public int votosPresidenciales(int idPartido) {
        return votosPresidente.get(idPartido);
    }
    //Complejidad O(1)
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados.get(idDistrito,idPartido);
    }

    public int[] resultadosDiputados(int idDistrito){
        
        int cantidadBancas = bancasXDistrito[idDistrito];
        int[] res = votosDiputados.devolverBancas(idDistrito, cantidadBancas);
        return res;
    }

    public boolean hayBallotage(){
        //si esta vacio, es verdadero
        boolean res;
        if (votosPresidente.max() == 0) {
            res = true;
        }else{
            //multiplicamos por 100 para no utilizar floating points
            if (votosPresidente.max()*100 > votosPresidente.total()*45 || 
              (votosPresidente.max()*100 > votosPresidente.total()*40 && (votosPresidente.max() - votosPresidente.sdoMax())*100 > votosPresidente.total()*10) ){
                  res = false;
            }else{
                res = true;
            }
        }
       return res;
    } 
}

