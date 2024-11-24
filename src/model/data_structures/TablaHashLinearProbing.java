package model.data_structures;

import java.text.DecimalFormat;
import model.data_structures.RehashUtil;

public class TablaHashLinearProbing <K extends Comparable<K>, V extends Comparable <V>> implements ITablaSimbolos<K, V>{

	private ILista<NodoTS<K,V>> listaNodos;
	private int tamanoAct;
	private int tamanoTabla;
	private int minicial;
	private int cantidadRehash;
	private RehashUtil rehash = new RehashUtil();

	public TablaHashLinearProbing(int tamInicial)
	{
		int m = rehash.nextPrime(tamInicial);
		minicial=m;
		listaNodos=new ArregloDinamico<>(m);
		tamanoAct=0;
		tamanoTabla=m;

		for(int i=1; i<=tamanoTabla; i++)
		{
			try 
			{
				listaNodos.insertElement(null, i);
			} 
			catch (PosException | NullException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void put(K key, V value) 
	{
		int posicion= hash(key);
		try 
		{
			NodoTS<K, V> nodo= listaNodos.getElement(posicion);
			if (nodo!=null && !nodo.isEmpty())
			{
				posicion=getNextEmpty(posicion);
			}

			NodoTS<K, V> nuevo= new NodoTS<K, V>(key, value);
			listaNodos.changeInfo(posicion, nuevo);
			tamanoAct++;

		} 
		catch (PosException | VacioException| NullException e) 
		{
			e.printStackTrace();
		}
		double tam= tamanoAct;
		double tam2=tamanoTabla;
		double tamanoCarga= tam/tam2;

		if (tamanoCarga > 0.75)
		{
			rehash();
		}
		
	}

	public int getNextEmpty(int posicion)
	{
		int posicionRetornar= (posicion % tamanoTabla) +1;
		
		try 
		{
			while(listaNodos.getElement(posicionRetornar)!= null && !listaNodos.getElement(posicion).isEmpty())
			{
				posicionRetornar++;
				if(posicionRetornar> tamanoTabla)
				{
					posicionRetornar=1;
				}
			}
		} 
		catch (PosException | VacioException e) 
		{
			e.printStackTrace();
		}
		return posicionRetornar;
	}

	@Override
	public V get(K key) 
	{
		int posicion=hash(key);
		V retornar=null;
		boolean encontroNull= false;

		while(retornar==null && !encontroNull)
		{
			NodoTS<K, V> nodoActual;
			try 
			{
				nodoActual = listaNodos.getElement(posicion);
				if(nodoActual==null)
				{
					encontroNull=true;
				}
				else if (!nodoActual.isEmpty() && nodoActual.getKey().compareTo(key)==0)
				{
					retornar=nodoActual.getValue();
				}
				else
				{
					posicion++;

					if(posicion>tamanoTabla)
					{
						posicion=1;
					}
				}

			} 
			catch (PosException | VacioException e) 
			{
				e.printStackTrace();
			}
		}

		return retornar;	
	}

	@Override
	public V remove(K key) 
	{
		int posicion=hash(key);
		V retornar=null;
		boolean encontroNull= false;

		try 
		{

			while(retornar==null && !encontroNull)
			{
				NodoTS<K, V> nodoActual;

				nodoActual = listaNodos.getElement(posicion);
				if(nodoActual==null)
				{
					encontroNull=true;
				}
				else if (!nodoActual.isEmpty() && nodoActual.getKey().compareTo(key)==0)
				{
					retornar=nodoActual.getValue();
				}
				else
				{
					posicion++;

					if(posicion>tamanoTabla)
					{
						posicion=1;
					}
				}
			}

			if(retornar!=null)
			{
				listaNodos.getElement(posicion).setEmpty();
			}
			else
			{
				throw new PosException("No está el elemento");
			}
		}
		catch (PosException | VacioException e) 
		{
			e.printStackTrace();
		}

		return retornar;
	}

	@Override
	public boolean contains(K key) {
	    V valor = get(key);
	    return valor != null; // Devuelve true si el valor no es null
	}

	@Override
	public boolean isEmpty() 
	{
		return false;
	}

	@Override
	public int size() {
		return tamanoAct;
	}

	@Override
	public ILista<K> keySet() 
	{

		ILista<K> lista= new ArregloDinamico(1);
		try 
		{
			for (int i=1; i<= tamanoTabla; i++)
			{
				if(listaNodos.getElement(i)!=null)
				{
					lista.insertElement(listaNodos.getElement(i).getKey(), lista.size()+1);
				}
			}
		}
		catch (PosException | NullException | VacioException e) 
		{
			e.printStackTrace();
		}

		return lista;
	}

	@Override
	public ILista<V> valueSet() 
	{
		ILista<V> lista= new ArregloDinamico(1);
		for (int i=1; i<= tamanoTabla; i++)
		{
			try 
			{

				if (listaNodos.getElement(i)!=null)
				{
					lista.insertElement(listaNodos.getElement(i).getValue(), lista.size()+1);
				}
			} 
			catch (PosException | NullException | VacioException e) 
			{
				e.printStackTrace();
			}
		}
		return lista;
	}

	@Override
	public int hash(K key) 
	{
		return Math.abs(key.hashCode()% tamanoTabla) +1;
	}

	@Override
	public ILista<NodoTS<K, V>> darListaNodos() 
	{
		ILista<NodoTS<K, V>> nodos= new ArregloDinamico<NodoTS<K, V>>(1);
		
		try 
		{
			for (int i=1; i<= tamanoTabla; i++)
			{
				NodoTS<K,V> elemento= listaNodos.getElement(i);
				if(elemento!= null && !elemento.isEmpty())
				{
					nodos.insertElement(elemento, nodos.size()+1);
				}
			}
		}
		catch (PosException | NullException | VacioException e) 
		{
			e.printStackTrace();
		}
		
		return nodos;
	}

	public void rehash()
	{
		try
		{
			ILista<NodoTS<K,V>> nodos= darListaNodos();
			
			tamanoAct=0;
			tamanoTabla*=2;
			int m = rehash.nextPrime(tamanoTabla);
			tamanoTabla=m;
			listaNodos=new ArregloDinamico<>(tamanoTabla);
			
			for(int i=1; i<=tamanoTabla; i++)
			{
				listaNodos.insertElement(null, size()+1);
			}
			
			NodoTS<K,V> actual= null;
			for(int i=1; i<= nodos.size();i++)
			{
				actual=nodos.getElement(i);
				put(actual.getKey(), actual.getValue());
			}
		}
		catch (NullException| VacioException| PosException e)
		{
			e.printStackTrace();
		}
		
		cantidadRehash++;
		
	}

	public int darMinicial()
	{
		return minicial;
	}

	public int darMfinal()
	{
		return tamanoTabla;
	}

	

}
