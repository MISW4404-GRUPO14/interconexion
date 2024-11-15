package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;

public class OrdenamientoShell<T extends Comparable<T>> implements SortAlgorithm<T> {
    @Override
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException {
        int n = lista.size();
        int h = 1;

        while (h < (n / 3)) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h + 1; i <= n; i++) {
                boolean enPosicion = false;
                for (int j = i; j > h && !enPosicion; j -= h) {
                    int factorComparacion = (ascendente ? 1 : -1) * criterio.compare(lista.getElement(j), lista.getElement(j - h));
                    if (factorComparacion < 0) {
                        lista.exchange(j, j - h);
                    } else {
                        enPosicion = true;
                    }
                }
            }
            h /= 3;
        }
    }
}