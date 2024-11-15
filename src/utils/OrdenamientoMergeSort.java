package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;
import model.data_structures.NullException;

public class OrdenamientoMergeSort<T extends Comparable<T>> implements SortAlgorithm<T> {
    @Override
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException, NullException {
        int size = lista.size();
        if (size > 1) {
            int mid = size / 2;
            ILista<T> leftList = lista.sublista(1, mid);
            ILista<T> rightList = lista.sublista(mid + 1, size - mid);

            ordenar(leftList, criterio, ascendente);
            ordenar(rightList, criterio, ascendente);

            merge(lista, leftList, rightList, criterio, ascendente);
        }
    }

    private void merge(ILista<T> lista, ILista<T> leftList, ILista<T> rightList, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException, NullException {
        int i = 1, j = 1, k = 1;
        while (i <= leftList.size() && j <= rightList.size()) {
            T elemi = leftList.getElement(i);
            T elemj = rightList.getElement(j);
            int factorComparacion = (ascendente ? 1 : -1) * criterio.compare(elemi, elemj);
            if (factorComparacion <= 0) {
                lista.changeInfo(k++, elemi);
                i++;
            } else {
                lista.changeInfo(k++, elemj);
                j++;
            }
        }
        while (i <= leftList.size()) {
            lista.changeInfo(k++, leftList.getElement(i++));
        }
        while (j <= rightList.size()) {
            lista.changeInfo(k++, rightList.getElement(j++));
        }
    }
}