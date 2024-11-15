// utils/SortAlgorithm.java
package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;
import model.data_structures.NullException;

public interface SortAlgorithm<T extends Comparable<T>> {
    void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException, NullException;
}