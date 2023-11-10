package aed;



public class SistemaCNE {
    private Presidencial votosPresidente;
    private Diputados votosDiputados;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el último elemento de la mesa, no incluido.

    // INV REP :
    // Valen los invariantes de las clases Presidencial Y Diputados en las variables votosPresidente y VotosDiputados.
    // Hay tantos nombres de partidos como la longitud de _arrPresidente en votosPresidente, y no hay elementos repetidos.
    // Hay tantos nombres de distritos como elementos en Mesas, y ningún elemento está repetido.
    // Hay tantos elementos en bancasXDistrito como en mesas, y sus elementos son mayores o iguales a 0.
    // En mesas todos los elementos son diferentes, están en orden creciente, y son mayores o iguales a 0.
    // La longitud de votosDiputados.votosXDistrito es igual a la de nomDistrito.

    public class  PartidoXVoto implements Comparable<PartidoXVoto>{
           int idPartido;
           int Votos;
           // INV REP :
           // Tanto idPartido como Votos son mayores o iguales a 0.
           
           // Complejidad : O(1)
           PartidoXVoto(int numPartido, int votos){
                this.idPartido = numPartido;
                this.Votos = votos;
            }
        
            // Complejidad : O(1), comparación.
            @Override
            public int compareTo(PartidoXVoto a){
                // Definimos criterio de comparación en las tuplas como la comparación numérica de sus primeros elementos,
                // para así poder utilizarla en el heap.
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
        // INV REP: El idPrimero es la posición de _arrPresidente en la cual se encuentra el elemento mas grande del
        // arreglo, salvando la última posició; es decir, es el partido (sin contar los votos en blanco) con más votos.
        // El idSegundo es la posición de _arrPresidencial en la cual se encuentra el elemento que es menor o igual al más grande,
        // pero mayor a todos los demás elementos, salvando la última posición; es decir, es el segundo partido (sin contar los votos en blanco) con más votos.
        // El total es la suma de todos los elementos de _arrPresidencial, es decir, es el total de votos.
        // _arrPresidencial es un arreglo en el cual en la posición i-ésima se encuentra la cantidad de votos para presidente que obtuvo
        // el i-ésimo partido. En la última posición del arreglo se encuentra la cantidad de votos en blanco. Todos los votos son números mayores o iguales a 0.
        
        // Complejidad : Theta(n), siendo n el parámetro "longitud".
        Presidencial(int longitud){
            this._arrPresidente = new int[longitud];
            _Total = 0;
            idPrimero = 0;
            idSegundo = 1;
        }
        
        // Complejidad : O(1), indexar arreglo.
        int get(int id){
            return _arrPresidente[id];
        }
        
        // Complejidad : O(1), indexar arreglo.
        int max(){
         return _arrPresidente[idPrimero];
       } 
        
        // Complejidad : O(1), indexar arreglo.
        int sdoMax(){
            return _arrPresidente[idSegundo];
        }
      
        // Complejidad : O(1), retornar int.
        int total(){
            return _Total;
        }

        // Complejidad : O(P)
        void actualizar(int[] votos){
            
        // O(P), se recorre e indexa el arreglo.
        for(int i = 0;i < votos.length;i++){
            _Total += votos[i];
            _arrPresidente[i] += votos[i];
           if(i != votos.length-1){ // No queremos que los votos en blanco puedan ser primeros.
            
            
            if(_arrPresidente[i] > _arrPresidente[idPrimero]){
                idSegundo = idPrimero;
                idPrimero = i;
           }
           // Debemos utilizar un mayor o igual en la segunda componente de la guarda porque sino puede ocurrir que el primero sea el mismo partido que el segundo.
           else if (_arrPresidente[i] > _arrPresidente[idSegundo] && !(_arrPresidente[i] >= _arrPresidente[idPrimero])){
                idSegundo = i;
           }
        }
        }
        
        }
    }
    public class Diputados{
        private int[][] votosXDistrito; // Primer Array distrito, segundo array votos de cada partido.
        private PriorityQueueTupla[] heapXDistrito;
        private int[] total;
        private int[][] resultadosPrecalculados;
        private boolean[] heapValido;

        // INV REP :
        
        // Vale el invariante de representación de PriorityQueueTupla.
        
        // Para toda posición de VotosXDistrito, se cumple que todos los elementos del arreglo en esa posición son mayores o iguales a 0.
        // Todos los arreglos en VotosXDistrito tienen la misma longitud. Aquí guardamos los votos de cada partido en cada distrito.
        
        // En la posicion i-ésima del arreglo total está la suma de los elementos del arreglo votosXDistrito en la posicion i-ésima;
        // es decir, los votos totales del distrito son la suma de los votos del distrito.
        // El largo de VotosXDistrito es igual al tamaño de heapXDistrito.
        // El largo de heapValido es igual al largo de heapxDistrito.
        // El largo de resultadosPrecalculados es igual al largo de VotosXDistrito, 
        // y todos sus elementos tienen la longitud de la cantidad de partidos menos el blanco
        // (cantidad de partidos siendo la longitud del elemento votosXDistirto[0])
        
        // El heapValido i-ésimo es True si solo si para cada posición de votosXDistrito, se cumple que todos los elementos 
        // (exceptuando los votos en blanco) que sean superior el 3% del total i-ésimo de ese arreglo están en la representación del heapXdistirto i-ésimo.
        // Es decir, en el heap guardamos los votos de aquellos partidos que pasan el umbral del 3% en su distrito, y heapValido nos indica si ese heap está
        // intacto o no. Si no lo estuviese, utilizamos el resultado precalculado i-ésimo (que corresponde al distrito). 
        
        // Complejidad : O(D*P)
        Diputados(int longitudDistritos, int longitudPartidos){
          // Crear un arreglo de longitud n tiene complejidad Theta(n).
          // Si creamos D arreglos de longitud P, nuestra complejidad es de Theta(D*P).
          // Las demás creaciones de arreglos no afectan ya que Theta(D + D*P) pertenece a Theta(D*P).
          
          votosXDistrito = new int[longitudDistritos][longitudPartidos]; 
          heapValido = new boolean[longitudDistritos];
          heapXDistrito = new PriorityQueueTupla[longitudDistritos];
          resultadosPrecalculados = new int[longitudDistritos][longitudPartidos-1];
          total = new int[longitudDistritos];    
        }
        
        // O(1), indexación de arreglos.
        int get(int idDistrito, int idPartido){
              return votosXDistrito[idDistrito][idPartido];
        }
       
                
        // Complejidad : O(P)
        void actualizarDistrito(int idDistrito,PartidoXVoto[] votosMesa){
            int pasa3PorCiento = 0;
            // Theta(P), votosMesa tiene longitud de los Partidos.
            for(int i = 0; i < votosMesa.length;i++){
                votosXDistrito[idDistrito][i] += votosMesa[i].Votos;
                total[idDistrito] += votosMesa[i].Votos;
            }
            


            // Contamos cuántos pasan el 3% para definir la longitud del arreglo que será el Heap, sin votos en blanco.
            // Theta(P)
            for(int i = 0; i<votosXDistrito[idDistrito].length-1;i++){
                // Cálculo del 3% ajustado para evitar floating point.
                if(votosXDistrito[idDistrito][i]*100 >= total[idDistrito]*3){ 
                     pasa3PorCiento ++;
                }
            }
            
            // Creamos el array con aquellos que pasan el 3%, que luego será el heap.
            // Necesitamos un índice separado para el segundo arreglo, ya que no se corresponden las posiciones con el original
            // a la hora de asignar.
            // Theta(P), observar que crear un Nuevo "PartidoXVoto" es O(1), opera como una tupla.
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
            // Internamente el HeapXDistrito utiliza el algoritmo de Floyd cuando se le pasa un arreglo, de complejidad lineal.
            // O(P)
            heapXDistrito[idDistrito] = new PriorityQueueTupla(votosPasanMargen);
          
            // Theta(P), seteamos los resultados anteriores en este distrito a 0.
            for(int i = 0; i < resultadosPrecalculados[idDistrito].length;i++){
                 resultadosPrecalculados[idDistrito][i] = 0;
            }
            heapValido[idDistrito] = true;
            
        }
        // Complejidad : O(Dd*log(P))
        int[] devolverBancas(int idDistrito,int cantidadBancas) {
            // Chequeamos si ya calculamos el resultado de este distrito.
            // O(Dd*log(P)) 
            if (heapValido[idDistrito]) {
                
                
                // Realizamos el cálculo De D'Hondt:
                // Tomamos el máximo elemento del heap, lo dividimos por la cantidad de bancas obtenidas por ese partido hasta el momento + 1,
                // le sumamos una banca más a ese partido, y lo reinsertamos con su nuevo valor en el heap.
                // Repetimos el proceso hasta agotar las bancas disponibles.

             
                // O(Dd*log(P)), con P la cantidad de partidos y Dd la cantidad de bancas disponibles.
                for (int i = 0; i < cantidadBancas; i++) {
                    // O(log(P)), en el peor caso todos los partidos pasan el márgen del 3%.
                    PartidoXVoto max = heapXDistrito[idDistrito].desencolar();
                    
                    if(max != null){                      
                    resultadosPrecalculados[idDistrito][max.idPartido] += 1;
                    
                    PartidoXVoto recalculado = new PartidoXVoto(max.idPartido,
                            (votosXDistrito[idDistrito][max.idPartido]) / (resultadosPrecalculados[idDistrito][max.idPartido] + 1));
                    // O(log(P))
                            heapXDistrito[idDistrito].encolar(recalculado);
                    }
                }
            
                heapValido[idDistrito] = false;
                return resultadosPrecalculados[idDistrito];
            
            
            } else {
                // O(1)
                return resultadosPrecalculados[idDistrito];
            }
        }
    }
    
    
    public class VotosPartido{
        private int presidente;
        private int diputados;
        // INV REP :
        // Tanto presidente como diputados son números mayores o iguales a 0.
        
        
        // Complejidad : O(1), asignación de valores.
        VotosPartido(int presidente, int diputados)
        {
            this.presidente = presidente; this.diputados = diputados;
        }
        
        // Complejidad : O(1)
        public int votosPresidente(){return presidente;}
        
        // Complejidad : O(1)
        public int votosDiputados(){return diputados;}
    }

    // Complejidad : O(D*P), siendo D la cantidad de Distritos y P la de Partidos.
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
     nomDistrito = nombresDistritos;
     bancasXDistrito = diputadosPorDistrito;
     nomPartido = nombresPartidos;
     mesas = ultimasMesasDistritos;
     // O(P) , justificado en la clase.
     votosPresidente = new Presidencial(nombresPartidos.length);
     // O(D*P), justificado en la clase.
     votosDiputados = new Diputados(nombresDistritos.length,nombresPartidos.length);
    }
    
    // Complejidad : O(1), indexar arreglo.
    public String nombrePartido(int idPartido) {
        return nomPartido[idPartido];
    }
    
    // Complejidad : O(1), indexar arreglo.
    public String nombreDistrito(int idDistrito) {
        return nomDistrito[idDistrito];
    }
    
    // Complejidad : O(1), indexar arreglo.
    public int diputadosEnDisputa(int idDistrito) {
        return bancasXDistrito[idDistrito];
    }
    
    // Complejidad : O(1), indexar arreglo.
    public String distritoDeMesa(int idMesa) {
        int pos = ObtenerIdDistrito(idMesa);
        return nombreDistrito(pos);
    }
    
    
    
    // Complejidad : O(P + log(D))
    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        // Actualizamos Votos Presidenciales.
        
        // Theta(P)
        int []vPres = new int[actaMesa.length];
        for(int i = 0;i < actaMesa.length;i++){
            vPres[i] = i;
            vPres[i] = actaMesa[i].presidente;
        }
        // Complejidad : O(P), justificación en clase VotosPresidente.
        votosPresidente.actualizar(vPres);
       
        // Actualizamos Votos Diputados.
        
        // O(log(D)), ObteneridDistrito usa una búsqueda binaria sobre un arreglo ordenado, de longitud distritos.
        int distritoMesa = ObtenerIdDistrito(idMesa);
        // O(D), creación de tupla es O(1), realizado tantas veces como la cantidad de distritos.
        PartidoXVoto[] vDiputados = new PartidoXVoto[actaMesa.length];
        for (int i = 0; i < actaMesa.length; i++) {
            vDiputados[i] = new PartidoXVoto(i, actaMesa[i].diputados);
            
        }
        // Complejidad : O(P), justificación en la clase VotosDiputados.
        votosDiputados.actualizarDistrito(distritoMesa,vDiputados);
    }
 
    // Complejidad : O(log(n)), siendo n la longitud del arreglo pasado.
    // Utilizamos una búsqueda binaria para obtenerlo, ya que según la especificación y el invariante de representación las mesas son siempre un arreglo ordenado.
    // La búsqueda binaria tiene complejidad logarítimica, ya que reduce el rango de búsqueda a la mitad en cada iteración del while.
    private int ObtenerIdDistrito(int elem){
        int izq = 0;
        int der = mesas.length;
        int medio = (izq + der)/2;
        while(izq != der){
           if(mesas[medio] == elem){
            return medio + 1;   // El arreglo es no incluido, con lo cual si lo encontramos, es la siguiente pos de mesa.
           }else if(der - izq == 1 && elem != mesas[der] && elem != mesas[izq]){ // Caso en el que no pertenezeca a la lista directamente.
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

    // Complejidad : O(1)
    public int votosPresidenciales(int idPartido) {
        return votosPresidente.get(idPartido);
    }
    // Complejidad : O(1)
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados.get(idDistrito,idPartido);
    }
    
    // Complejidad : O(Dd*log(P)), con Dd las bancas disponibles en el distrito y P la cantidad de partidos.
    public int[] resultadosDiputados(int idDistrito){
        
        int cantidadBancas = bancasXDistrito[idDistrito];
        //O(Dd*Log(P))
        int[] res = votosDiputados.devolverBancas(idDistrito, cantidadBancas);
        return res;
    }

    // Complejidad : O(1)
    public boolean hayBallotage(){
        // Si está vacio, es verdadero.
        boolean res;
        if (votosPresidente.max() == 0) {
            res = true;
        }else{
            // Multiplicamos por 100 para no utilizar floating points.
            // O(1), comparaciones y se accede a valores en arrays dentro de las clases.
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