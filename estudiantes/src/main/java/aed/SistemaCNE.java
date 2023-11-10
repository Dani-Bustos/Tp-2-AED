package aed;

import aed.Presidencial;

public class SistemaCNE {
    private Presidencial votosPresidente;
    private Diputados votosDiputados;
    private String[] nomPartido;
    private String[] nomDistrito;
    private int[] bancasXDistrito;
    private int[] mesas; // guardamos la ultima mesa del distrito + 1.

    // INV REP :
    // Valen los invariantes de las clases Presidencial Y Diputados en las variable
    // votosPresidente y VotosDiputados.
    // Hay tantos nombres de partidos como la longitud de
    // votosPresidente._arrPresidente y no hay elementos repetidos.
    // Hay tantos nombres de distritos como elementos en Mesas, y ningún elemento
    // está repetido.
    // Hay tantos elementos en bancasXDistrito como en mesas, y sus elementos son
    // mayores o iguales a 0.
    // En mesas todos los elementos son diferentes, están en orden creciente, y son
    // mayores o iguales a 0.
    // La longitud de votosDiputados.votosXDistrito es igual a la de nomDistrito.

    public static class PartidoXVoto implements Comparable<PartidoXVoto> {
        int idPartido;
        int Votos;
        // INV REP :
        // Tanto idPartido como Votos son mayores o iguales a 0.

        // Complejidad : O(1)
        PartidoXVoto(int numPartido, int votos) {
            this.idPartido = numPartido;
            this.Votos = votos;
        }

        // Complejidad : O(1), comparación.
        @Override
        public int compareTo(PartidoXVoto a) {
            // Definimos criterio de comparación en las tuplas como la comparación numérica
            // de sus primeros elementos, para así poder utilizarla en el heap.
            if (this.Votos > a.Votos) {
                return 1;
            } else if (this.Votos < a.Votos) {
                return -1;
            } else {
                return 0;
            }
        }

    }

    public class VotosPartido {
        private int presidente;
        private int diputados;
        // INV REP :
        // Tanto presidente como diputados son números mayores o iguales a 0.

        // Complejidad : O(1), asignación de valores.
        VotosPartido(int presidente, int diputados) {
            this.presidente = presidente;
            this.diputados = diputados;
        }

        // Complejidad : O(1)
        public int votosPresidente() {
            return presidente;
        }

        // Complejidad : O(1)
        public int votosDiputados() {
            return diputados;
        }
    }

    // Complejidad : O(D*P), siendo D la cantidad de Distritos y P la de Partidos.
    public SistemaCNE(String[] nombresDistritos, int[] diputadosPorDistrito, String[] nombresPartidos,
            int[] ultimasMesasDistritos) {

        nomDistrito = nombresDistritos;
        bancasXDistrito = diputadosPorDistrito;
        nomPartido = nombresPartidos;
        mesas = ultimasMesasDistritos;
        // O(P) , justificado en la clase.
        votosPresidente = new Presidencial(nombresPartidos.length);
        // O(D*P), justificado en la clase.
        votosDiputados = new Diputados(nombresDistritos.length, nombresPartidos.length);
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
        int[] vPres = new int[actaMesa.length];
        for (int i = 0; i < actaMesa.length; i++) {
            vPres[i] = i;
            vPres[i] = actaMesa[i].presidente;
        }
        // Complejidad : O(P), justificación en clase VotosPresidente.
        votosPresidente.actualizar(vPres);

        // Actualizamos Votos Diputados.

        // O(log(D)), ObteneridDistrito usa una búsqueda binaria sobre un arreglo
        // ordenado, de longitud distritos.
        int distritoMesa = ObtenerIdDistrito(idMesa);

        // O(D), creación de tupla es O(1), realizado tantas veces como la cantidad de
        // distritos.
        PartidoXVoto[] vDiputados = new PartidoXVoto[actaMesa.length];
        for (int i = 0; i < actaMesa.length; i++) {
            vDiputados[i] = new PartidoXVoto(i, actaMesa[i].diputados);

        }
        // Complejidad : O(P), justificación en la clase VotosDiputados.
        votosDiputados.actualizarDistrito(distritoMesa, vDiputados);
    }

    // Complejidad : O(log(n)), siendo n la longitud del arreglo pasado.
    // Utilizamos una búsqueda binaria para obtenerlo, ya que según la
    // especificación y el invariante de representación las mesas son siempre un
    // arreglo ordenado.
    // La búsqueda binaria tiene complejidad logarítimica, ya que reduce el rango de
    // búsqueda a la mitad en cada iteración del while.
    private int ObtenerIdDistrito(int elem) {
        int izq = 0;
        int der = mesas.length;
        int medio = (izq + der) / 2;
        while (izq != der) {
            if (mesas[medio] == elem) {
                return medio + 1; // El arreglo es no incluido, con lo cual si lo encontramos, es la siguiente pos
                                  // de mesa.
            } else if (der - izq == 1 && elem != mesas[der] && elem != mesas[izq]) { // Caso en el que no pertenezeca a
                                                                                     // la lista directamente.
                if (elem < mesas[izq]) {
                    return izq;
                } else {
                    return der;
                }
            } else if (mesas[medio] > elem) {
                der = medio;

            } else if (mesas[medio] < elem) {
                izq = medio;

            }
            medio = (der + izq) / 2;
        }
        return medio;
    }

    // Complejidad : O(1)
    public int votosPresidenciales(int idPartido) {
        return votosPresidente.get(idPartido);
    }

    // Complejidad : O(1)
    public int votosDiputados(int idPartido, int idDistrito) {
        return votosDiputados.get(idDistrito, idPartido);
    }

    // Complejidad : O(Dd*log(P)), con Dd las bancas disponibles en el distrito y P
    // la cantidad de partidos.
    public int[] resultadosDiputados(int idDistrito) {

        int cantidadBancas = bancasXDistrito[idDistrito];
        // O(Dd*Log(P))
        int[] res = votosDiputados.devolverBancas(idDistrito, cantidadBancas);
        return res;
    }

    // Complejidad : O(1)
    public boolean hayBallotage() {
        // Si ningun partido tiene votos, es verdadero.
        boolean res;
        if (votosPresidente.max() == 0) {
            res = true;
        } else {
            // Multiplicamos por 100 para no utilizar floating points.
            // O(1), comparaciones y se accede a valores en arrays dentro de las clases.
            if (votosPresidente.max() * 100 > votosPresidente.total() * 45 ||
                    (votosPresidente.max() * 100 > votosPresidente.total() * 40
                            && (votosPresidente.max() - votosPresidente.sdoMax()) * 100 > votosPresidente.total()
                                    * 10)) {
                res = false;
            } else {
                res = true;
            }
        }
        return res;
    }
}