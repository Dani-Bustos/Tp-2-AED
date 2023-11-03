package aed;
public class SistemaCNE {
    private Presidencial votosPresidente;
    private Diputados votosDiputados;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el ultimo elemento de la mesa, no incluido
     
    
    public class Presidencial{
        private int[] _arrPresidente;
        private Heap _hPresidendete;
        private int _Total;
        Presidencial(int longitud){
            this._arrPresidente = new int[longitud];
            this._hPresidendete = null;
            _Total = 0;
        }
       
        int get(int id){
            return _arrPresidente[id];
        }
        int max(){
        return _hPresidendete.maximo();
       } 
       
       int total(){
            return _Total;
        }
       
        void actualizar(int[] votos){
        
        for(int i = 0;i < votos.length;i++){
            _Total += votos[i];
            _arrPresidente[i] += votos[i];
        }
       _hPresidendete = new Heap(votos);   
        }
    }
    public class Diputados{
        private int[][] votosXDistrito; //Primer Array distrito, segundo array votos de cada partido 
        private boolean[] heapValido;
        private Heap[] heapXDistrito;
        
        Diputados(int longitudDistritos, int longitudPartidos){
          votosXDistrito = new int[longitudDistritos][longitudPartidos]; //se escribe asi?
          heapValido = new boolean[longitudDistritos];
          heapXDistrito = new Heap[longitudDistritos];

        }
        
        int get(int idDistrito, int idPartido){
              return votosXDistrito[idDistrito][idPartido];
        }
    
        void actualizarDistrito(int idDistrito,int[] votos){
            for(int i = 0; i < votos.length;i++){
                votosXDistrito[idDistrito][i] += votos[i];
            }
            heapXDistrito[idDistrito] = new Heap(votosXDistrito[idDistrito]);
            heapValido[idDistrito] = true;
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
        int []vPres = new int [actaMesa.length];
        for(int i = 0;i < actaMesa.length;i++){
            vPres[i] = actaMesa[i].presidente;
        }
        votosPresidente.actualizar(vPres);
       
        //Ahora Diputados
        int distritoMesa = BusquedaBinariaEnRango(mesas,idMesa);
        int[] vDiputados = new int [actaMesa.length];
        for(int i = 0; i< actaMesa.length;i++){
            vDiputados[i] = actaMesa[i].diputados;
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
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        //si esta vacio, es verdadero
        throw new UnsupportedOperationException("No implementada aun");
    }
}

