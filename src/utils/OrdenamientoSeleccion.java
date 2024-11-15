package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;

public class OrdenamientoSeleccion<T extends Comparable<T>> implements SortAlgorithm<T> {
    @Override
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente) throws PosException, VacioException {
        for (int i = 1; i <= lista.size(); i++) {
            int posMayorMenor = i;
            for (int j = i + 1; j <= lista.size(); j++) {
                int factorComparacion = (ascendente ? 1 : -1) * criterio.compare(lista.getElement(posMayorMenor), lista.getElement(j));
                if (factorComparacion > 0) {
                    posMayorMenor = j;
                }
            }
            lista.exchange(posMayorMenor, i);
        }
    }
}