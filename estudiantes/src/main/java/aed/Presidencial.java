package aed;

public class Presidencial {
    private int[] _arrPresidente;
    private int _Total;
    private int idPrimero;
    private int idSegundo;

    // INV REP: El idPrimero es la posición de _arrPresidente en la cual se
    // encuentra el elemento mas grande del
    // arreglo, salvando la última posició; es decir, es el partido (sin contar los
    // votos en blanco) con más votos.
    // El idSegundo es la posición de _arrPresidencial en la cual se encuentra el
    // elemento que es menor o igual al más grande,
    // pero mayor a todos los demás elementos, salvando la última posición; es
    // decir, es el segundo partido (sin contar los votos en blanco) con más votos.
    // El total es la suma de todos los elementos de _arrPresidencial, es decir, es
    // el total de votos.
    // _arrPresidencial es un arreglo en el cual en la posición i-ésima se encuentra
    // la cantidad de votos para presidente que obtuvo
    // el i-ésimo partido. En la última posición del arreglo se encuentra la
    // cantidad de votos en blanco. Todos los votos son números mayores o iguales a
    // 0.

    // Complejidad : Theta(n), siendo n el parámetro "longitud".
    Presidencial(int longitud) {
        this._arrPresidente = new int[longitud];
        _Total = 0;
        idPrimero = 0;
        idSegundo = 1;
    }

    // Complejidad : O(1), indexar arreglo.
    int get(int id) {
        return _arrPresidente[id];
    }

    // Complejidad : O(1), indexar arreglo.
    int max() {
        return _arrPresidente[idPrimero];
    }

    // Complejidad : O(1), indexar arreglo.
    int sdoMax() {
        return _arrPresidente[idSegundo];
    }

    // Complejidad : O(1), retornar int.
    int total() {
        return _Total;
    }

    // Complejidad : O(P)
    void actualizar(int[] votos) {

        // O(P), se recorre e indexa el arreglo.
        for (int i = 0; i < votos.length; i++) {
            _Total += votos[i];
            _arrPresidente[i] += votos[i];
            //Nos guardamos el partido ganador y el segundo de este distrito
            if (i != votos.length - 1) { // No queremos que los votos en blanco puedan ser primeros.

                if (_arrPresidente[i] > _arrPresidente[idPrimero]) {
                    idSegundo = idPrimero;
                    idPrimero = i;
                }
                // Debemos utilizar un mayor o igual en la segunda componente de la guarda
                // porque sino puede ocurrir que el primero sea el mismo partido que el segundo.
                else if (_arrPresidente[i] > _arrPresidente[idSegundo]
                        && !(_arrPresidente[i] >= _arrPresidente[idPrimero])) {
                    idSegundo = i;
                }
            }
        }

    }
}