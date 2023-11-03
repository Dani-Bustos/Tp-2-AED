package aed;
public class SistemaCNE {
    private int[] vPresidente;
    private String[] nomPartido;
    private int[] bancasXDistrito;
    private int[] mesas; //guardamos el ultimo elemento de la mesa, no incluido
    private int[][] votosXDistrito; //Primer Array distrito, segundo array votos de cada partido 
    // private array de heaps, cada pos es un distrito

    private int BusquedaBinariaEnRango(int[] arreglo,int elem){
        int l = 0;
        int r = arreglo.length;
        int m = (l + r)/2;
        while(l != r){
           if(arreglo[m] == elem){
            return m;
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
        throw new UnsupportedOperationException("No implementada aun");
    }

    public String nombrePartido(int idPartido) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public String nombreDistrito(int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int diputadosEnDisputa(int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public String distritoDeMesa(int idMesa) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public void registrarMesa(int idMesa, VotosPartido[] actaMesa) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosPresidenciales(int idPartido) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int votosDiputados(int idPartido, int idDistrito) {
        throw new UnsupportedOperationException("No implementada aun");
    }

    public int[] resultadosDiputados(int idDistrito){
        throw new UnsupportedOperationException("No implementada aun");
    }

    public boolean hayBallotage(){
        throw new UnsupportedOperationException("No implementada aun");
    }
}

