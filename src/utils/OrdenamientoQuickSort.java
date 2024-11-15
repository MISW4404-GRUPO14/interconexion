package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;

public class OrdenamientoQuickSort<T extends Comparable<T>> implements SortAlgorithm<T> {

    private int partition(ILista<T> lista, Comparator<T> criterio, boolean ascendente, int lo, int hi) throws PosException, VacioException {
        int follower = lo;
        int leader = lo;

        while (leader < hi) {
            int factorComparacion = (ascendente ? 1 : -1) * criterio.compare(lista.getElement(leader), lista.getElement(hi));
            if (factorComparacion < 0) {
                lista.exchange(follower, leader);
                follower++;
            }
            leader++;
        }
        lista.exchange(follower, hi);
        return follower;
    }

    private void quickSort(ILista<T> lista, Comparator<T> criterio, boolean ascendente, int lo, int hi) throws PosException, VacioException {
        if (lo >= hi) return;
        int pivot = partition(lista, criterio, ascendente, lo, hi);
        quickSort(lista, criterio, ascendente, lo, pivot - 1);
        quickSort(lista, criterio, ascendente, pivot + 1, hi);
    }

    @Override
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException {
        quickSort(lista, criterio, ascendente, 1, lista.size());
    }
}