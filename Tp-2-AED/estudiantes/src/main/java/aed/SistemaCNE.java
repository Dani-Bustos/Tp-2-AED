package aed;

import aed.SistemaCNE.PartidoXVoto;

public class SistemaCNE {
    private Presidencial votosPresidente;
    private Diputados votosDiputados;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el ultimo elemento de la mesa, no incluido
    public class  PartidoXVoto implements Comparable<PartidoXVoto>{
           int idPartido;
           int Votos;
            PartidoXVoto(int numPartido, int votos){
                this.idPartido = numPartido;
                this.Votos = votos;
            }
        
            @Override
            public int compareTo(PartidoXVoto a){
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
        private PartidoXVoto Primero;
        private PartidoXVoto Segundo;
        Presidencial(int longitud){
            this._arrPresidente = new int[longitud];
            _Total = 0;
            Primero = new PartidoXVoto(0, 0);
            Segundo = new PartidoXVoto(1, 0);
        }
       
        int get(int id){
            return _arrPresidente[id];
        }
        int max(){
         return Primero.Votos;
       } 

        int sdoMax(){
            return Segundo.Votos;
        }
       
       int total(){
            return _Total;
        }
       
        void actualizar(int[] votos){
        
        for(int i = 0;i < votos.length;i++){
            _Total += votos[i];
            _arrPresidente[i] += votos[i];
           if(i != votos.length-1){ //No queremos que los votos en blanco puedan ser primeros
            
            
            if(_arrPresidente[i] > Primero.Votos){
                Segundo = Primero;
                Primero = new PartidoXVoto(i, _arrPresidente[i]);
           }
           else if (_arrPresidente[i] > Segundo.Votos && !(_arrPresidente[i] > Primero.Votos)){
                Segundo = new PartidoXVoto(i, _arrPresidente[i]);
           }
        }
        }
        
        }
    }
    public class Diputados{
        private int[][] votosXDistrito; //Primer Array distrito, segundo array votos de cada partido 
        
        private Heap[] heapXDistrito;
        private int total;
        private int[][] resultadosPrecalculados;
        private boolean[] heapValido;
        
        
        
        Diputados(int longitudDistritos, int longitudPartidos){
          votosXDistrito = new int[longitudDistritos][longitudPartidos]; //se escribe asi?
          heapValido = new boolean[longitudDistritos];
          heapXDistrito = new Heap[longitudDistritos];
          resultadosPrecalculados = new int[longitudDistritos][longitudPartidos-1];
        }
        
        int get(int idDistrito, int idPartido){
              return votosXDistrito[idDistrito][idPartido];
        }
    
        void actualizarDistrito(int idDistrito,PartidoXVoto[] votosMesa){
            int pasa3PorCiento = 0;
            for(int i = 0; i < votosMesa.length;i++){
                votosXDistrito[idDistrito][i] += votosMesa[i].Votos;
                total += votosMesa[i].Votos;
            }
            
            //Contamos cuantos pasan el 3% para definir la longitud del arreglo que sera el Heap, sin votos en blanco
            for(int i = 0; i<votosXDistrito[idDistrito].length-1;i++){
                if(votosXDistrito[idDistrito][i]*100 >= total*3){ //calculo del 3% ajustado para evitar floating point
                     pasa3PorCiento ++;
                }
            }
            
            //Creamos el array con aquellos q pasan el 3 por ciento, que luego sera el heap;
            //Necesitamso un indice separado para el segundo arreglo,ya que no se corresponden las posiciones con el original
            //a la hora de asignar

            PartidoXVoto[] votosPasanMargen = new PartidoXVoto[pasa3PorCiento];
            int indice_arreglo = 0;
            for(int i = 0; i < votosXDistrito[idDistrito].length-1;i++){
                if (votosXDistrito[idDistrito][i] * 100 >= total * 3) {
                    votosPasanMargen[indice_arreglo] = new PartidoXVoto(i, votosXDistrito[idDistrito][i]);
                    votosPasanMargen[indice_arreglo].Votos = votosXDistrito[idDistrito][i];
                    votosPasanMargen[indice_arreglo].idPartido = i;
                    indice_arreglo++;
                }
            }
            
            heapXDistrito[idDistrito] = new Heap(votosPasanMargen);
           
            heapValido[idDistrito] = true;
            
        }
       
        int[] devolverBancas(int idDistrito,int cantidadBancas) {
            if (heapValido[idDistrito]) {
                int[] bancas = new int[votosXDistrito[0].length - 1];
                
                for (int i = 0; i < cantidadBancas; i++) {
                    PartidoXVoto max = heapXDistrito[idDistrito].maximo();
                    bancas[max.idPartido] += 1;
                    PartidoXVoto recalculado = new PartidoXVoto(max.idPartido,
                            (votosXDistrito[idDistrito][max.idPartido]) / (bancas[max.idPartido] + 1));
                    heapXDistrito[idDistrito].encolar(recalculado);
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
        VotosPartido(int presidente, int diputados)
        {
            this.presidente = presidente; this.diputados = diputados;
        }
        public int votosPresidente(){return presidente;}
        public int votosDiputados(){return diputados;}
    }

    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos, int[] ultimasMesasDistritos) {
     nomDistrito = nombresDistritos;
     bancasXDistrito = diputadosPorDistrito;
     nomPartido = nombresPartidos;
     mesas = ultimasMesasDistritos;
     
     votosPresidente = new Presidencial(nombresPartidos.length);
     votosDiputados = new Diputados(nombresDistritos.length,nombresPartidos.length);
    }

    public String nombrePartido(int idPartido) {
        return nomPartido[idPartido];
    }

    public String nombreDistrito(int idDistrito) {
        return nomDistrito[idDistrito];
    }

    public int diputadosEnDisputa(int idDistrito) {
        return bancasXDistrito[idDistrito];
    }

    public String distritoDeMesa(int idMesa) {
        int pos = BusquedaBinariaEnRango(mesas, idMesa);
        return nombreDistrito(pos);
    }
    private int BusquedaBinariaEnRango(int[] arreglo,int elem){
        int l = 0;
        int r = arreglo.length;
        int m = (l + r)/2;
        while(l != r){
           if(arreglo[m] == elem){
            return m + 1;   // el arreglo es no incluido, con lo cual si lo encontramos, es la siguiente pos de mesa
           }else if(r-l == 1 && elem != r && elem != l){ //caso en el que no pertenezeca a la lista directamente
            if(elem < arreglo[l]){
            return l;
            } 
            else{
            return r;
            }
           }else if(arreglo[m] > elem){
            r = m;
            
           }else if (arreglo[m] < elem){
            l = m;
            
           }
            m = (r + l)/2;    
        }
        return m;
    }
    

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        //Actualizamos Votos Presidenciales
        int []vPres = new int[actaMesa.length];
        for(int i = 0;i < actaMesa.length;i++){
            vPres[i] = i;
            vPres[i] = actaMesa[i].presidente;
        }
        votosPresidente.actualizar(vPres);
       
        //Ahora Diputados
        int distritoMesa = BusquedaBinariaEnRango(mesas,idMesa);
        PartidoXVoto[] vDiputados = new PartidoXVoto[actaMesa.length];
        for (int i = 0; i < actaMesa.length; i++) {
            vDiputados[i] = new PartidoXVoto(i, actaMesa[i].diputados);
            
        }
        votosDiputados.actualizarDistrito(distritoMesa,vDiputados);
    }

    public int votosPresidenciales(int idPartido) {
        return votosPresidente.get(idPartido);
    }

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
        if (votosPresidente.max() = 0) {
            return True;
        }
    }
}

