package aed;
public class SistemaCNE {
    private int[] vPresidente;
    //heapPresidente;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el ultimo elemento de la mesa, no incluido
    private int[][] votosXDistrito; //Primer Array distrito, segundo array votos de cada partido 
    private boolean[] heapDistritoValido;
    // private array de heaps, cada pos es un distrito
    

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
     heapDistritoValido = new boolean[nombresPartidos.length];
     vPresidente = new int[nombresPartidos.length];
     votosXDistrito = new int[nombresDistritos.length][nombresPartidos.length]; //no sabemos si es asi o al reves
     //falta el de heaps
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

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosPresidenciales(int idPartido) {
        return vPresidente[idPartido];
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        return votosXDistrito[idDistrito][idPartido];
    }

    public int[] resultadosDiputados(int idDistrito){
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        throw new UnsupportedOperationException("No implementada aun");
    }
}

