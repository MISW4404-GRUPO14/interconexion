// utils/OrdenamientoInsercion.java
package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;

public class OrdenamientoInsercion<T extends Comparable<T>> implements SortAlgorithm<T> {
    @Override
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException {
        for (int i = 2; i <= lista.size(); i++) {
            boolean enPosicion = false;
            for (int j = i; j > 1 && !enPosicion; j--) {
                int factorComparacion = (ascendente ? 1 : -1) * criterio.compare(lista.getElement(j), lista.getElement(j - 1));
                if (factorComparacion < 0) {
                    lista.exchange(j, j - 1);
                } else {
                    enPosicion = true;
                }
            }
        }
    }
}