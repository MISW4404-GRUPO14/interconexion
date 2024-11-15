package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;
import model.data_structures.NullException;

public class OrdenamientoContexto<T extends Comparable<T>> {
    private SortAlgorithm<T> algoritmo;

    // Método para establecer el algoritmo en tiempo de ejecución
    public void setAlgoritmo(SortAlgorithm<T> algoritmo) {
        this.algoritmo = algoritmo;
    }

    // Método para realizar el ordenamiento
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException, NullException {
        if (algoritmo == null) {
            throw new IllegalStateException("No se ha establecido un algoritmo de ordenamiento");
        }
        algoritmo.ordenar(lista, criterio, ascendente);
    }
}
