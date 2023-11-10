package aed;

import aed.SistemaCNE.PartidoXVoto;

public class Diputados {
    private int[][] votosXDistrito; // Primer Array distrito, segundo array votos de cada partido.
    private PriorityQueueTupla[] heapXDistrito;
    private int[] total;
    private int[][] resultadosPrecalculados;
    private boolean[] heapValido;

    // INV REP :

    // Vale el invariante de representación de PriorityQueueTupla.

    // Para toda posición de VotosXDistrito, se cumple que todos los elementos del
    // arreglo en esa posición son mayores o iguales a 0.
    // Todos los arreglos en VotosXDistrito tienen la misma longitud. Aquí guardamos
    // los votos de cada partido en cada distrito.

    // En la posicion i-ésima del arreglo total está la suma de los elementos del
    // arreglo votosXDistrito en la posicion i-ésima;
    // es decir, los votos totales del distrito son la suma de los votos del
    // distrito.
    // El largo de VotosXDistrito es igual al tamaño de heapXDistrito.
    // El largo de heapValido es igual al largo de heapxDistrito.
    // El largo de resultadosPrecalculados es igual al largo de VotosXDistrito,
    // y todos sus elementos tienen la longitud de la cantidad de partidos menos el
    // blanco
    // (cantidad de partidos siendo la longitud del elemento votosXDistirto[0])

    // El heapValido i-ésimo es True si solo si para cada posición de
    // votosXDistrito, se cumple que todos los elementos
    // (exceptuando los votos en blanco) que sean superior el 3% del total i-ésimo
    // de ese arreglo están en la representación del heapXdistirto i-ésimo.
    // Es decir, en el heap guardamos los votos de aquellos partidos que pasan el
    // umbral del 3% en su distrito, y heapValido nos indica si ese heap está
    // intacto o no. Si no lo estuviese, utilizamos el resultado precalculado
    // i-ésimo (que corresponde al distrito).

    // Complejidad : O(D*P)
    Diputados(int longitudDistritos, int longitudPartidos) {
        // Crear un arreglo de longitud n tiene complejidad Theta(n).
        // Si creamos D arreglos de longitud P, nuestra complejidad es de Theta(D*P).
        // Las demás creaciones de arreglos no afectan ya que Theta(D + D*P) pertenece a
        // Theta(D*P).

        votosXDistrito = new int[longitudDistritos][longitudPartidos];
        heapValido = new boolean[longitudDistritos];
        heapXDistrito = new PriorityQueueTupla[longitudDistritos];
        resultadosPrecalculados = new int[longitudDistritos][longitudPartidos - 1];
        total = new int[longitudDistritos];
    }

    // O(1), indexación de arreglos.
    int get(int idDistrito, int idPartido) {
        return votosXDistrito[idDistrito][idPartido];
    }

    // Complejidad : O(P)
    void actualizarDistrito(int idDistrito, PartidoXVoto[] votosMesa) {
        int pasa3PorCiento = 0;
        // Theta(P), votosMesa tiene longitud de los Partidos.
        for (int i = 0; i < votosMesa.length; i++) {
            votosXDistrito[idDistrito][i] += votosMesa[i].Votos;
            total[idDistrito] += votosMesa[i].Votos;
        }

        // Contamos cuántos pasan el 3% para definir la longitud del arreglo que será el
        // Heap, sin votos en blanco.
        // Theta(P)
        for (int i = 0; i < votosXDistrito[idDistrito].length - 1; i++) {
            // Cálculo del 3% ajustado para evitar floating point.
            if (votosXDistrito[idDistrito][i] * 100 >= total[idDistrito] * 3) {
                pasa3PorCiento++;
            }
        }

        // Creamos el array con aquellos que pasan el 3%, que luego será el heap.
        // Necesitamos un índice separado para el segundo arreglo, ya que no se
        // corresponden las posiciones con el original
        // a la hora de asignar.
        // Theta(P), observar que crear un Nuevo "PartidoXVoto" es O(1), opera como una
        // tupla.
        PartidoXVoto[] votosPasanMargen = new PartidoXVoto[pasa3PorCiento];
        int indice_arreglo = 0;
        for (int i = 0; i < votosXDistrito[idDistrito].length - 1; i++) {
            if (votosXDistrito[idDistrito][i] * 100 >= total[idDistrito] * 3) {
                votosPasanMargen[indice_arreglo] = new PartidoXVoto(i, votosXDistrito[idDistrito][i]);
                votosPasanMargen[indice_arreglo].Votos = votosXDistrito[idDistrito][i];
                votosPasanMargen[indice_arreglo].idPartido = i;
                indice_arreglo++;
            }
        }
        // Internamente el HeapXDistrito utiliza el algoritmo de Floyd cuando se le pasa
        // un arreglo, de complejidad lineal.
        // O(P)
        heapXDistrito[idDistrito] = new PriorityQueueTupla(votosPasanMargen);

        // Theta(P), seteamos los resultados anteriores en este distrito a 0.
        for (int i = 0; i < resultadosPrecalculados[idDistrito].length; i++) {
            resultadosPrecalculados[idDistrito][i] = 0;
        }
        heapValido[idDistrito] = true;

    }

    // Complejidad : O(Dd*log(P))
    int[] devolverBancas(int idDistrito, int cantidadBancas) {
        // Chequeamos si ya calculamos el resultado de este distrito.
        // si el heap no es valido, devolvemos el resultado precalculado
        // Esto es necesario en el caso de que se la llame a esta funcion dos veces
        // seguidas,sin antes registrar una nueva mesa en este distrito.

        // O(Dd*log(P))
        if (heapValido[idDistrito]) {

            // Realizamos el cálculo De D'Hondt:
            // Tomamos el máximo elemento del heap, lo dividimos por la cantidad de bancas
            // obtenidas por ese partido hasta el momento + 1,
            // le sumamos una banca más a ese partido, y lo reinsertamos con su nuevo valor
            // en el heap.
            // Repetimos el proceso hasta agotar las bancas disponibles.

            // O(Dd*log(P)), con P la cantidad de partidos y Dd la cantidad de bancas
            // disponibles.
            for (int i = 0; i < cantidadBancas; i++) {
                // O(log(P)), en el peor caso todos los partidos pasan el márgen del 3%.
                PartidoXVoto max = heapXDistrito[idDistrito].desencolar();

                if (max != null) {
                    resultadosPrecalculados[idDistrito][max.idPartido] += 1;

                    PartidoXVoto recalculado = new PartidoXVoto(max.idPartido,
                            (votosXDistrito[idDistrito][max.idPartido])
                                    / (resultadosPrecalculados[idDistrito][max.idPartido] + 1));
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
