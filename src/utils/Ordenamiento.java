package utils;

import java.util.Comparator;
import model.data_structures.ILista;
import model.data_structures.PosException;
import model.data_structures.VacioException;
import model.data_structures.NullException;

public class Ordenamiento<T extends Comparable<T>> {
    
    public void ordenar(ILista<T> lista, Comparator<T> criterio, boolean ascendente, String tipoOrdenamiento) 
        throws PosException, VacioException, NullException {
        
        SortAlgorithm<T> algoritmo;
        
        switch (tipoOrdenamiento) {
            case "seleccion":
                algoritmo = new OrdenamientoSeleccion<>();
                break;
            case "insercion":
                algoritmo = new OrdenamientoInsercion<>();
                break;
            case "shell":
                algoritmo = new OrdenamientoShell<>();
                break;
            case "quicksort":
                algoritmo = new OrdenamientoQuickSort<>();
                break;
            case "mergesort":
                algoritmo = new OrdenamientoMergeSort<>();
                break;
            default:
                throw new IllegalArgumentException("Tipo de ordenamiento no válido");
        }
        
        algoritmo.ordenar(lista, criterio, ascendente);
    }
}
