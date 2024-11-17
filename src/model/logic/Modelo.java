package model.logic;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import model.data_structures.ArregloDinamico;
import model.data_structures.Country;
import model.data_structures.Country.ComparadorXKm;
import model.data_structures.Edge;
import model.data_structures.GrafoListaAdyacencia;
import model.data_structures.ILista;
import model.data_structures.ITablaSimbolos;
import model.data_structures.Landing;
import model.data_structures.ListaEncadenada;
import model.data_structures.NodoTS;
import model.data_structures.NullException;
import model.data_structures.PilaEncadenada;
import model.data_structures.PosException;
import model.data_structures.TablaHashLinearProbing;
import model.data_structures.TablaHashSeparteChaining;
import model.data_structures.VacioException;
import model.data_structures.Vertex;
import model.data_structures.YoutubeVideo;
import utils.OrdenamientoContexto;

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo {
	/**
	 * Atributos del modelo del mundo
	 */
	private ILista datos;

	private GrafoListaAdyacencia grafo;

	private ITablaSimbolos paises;

	private ITablaSimbolos points;

	private ITablaSimbolos landingidtabla;

	private ITablaSimbolos nombrecodigo;

	/**
	 * Constructor del modelo del mundo con capacidad dada
	 *
	 * @param tamano
	 */
	public Modelo(int capacidad) {
		this.datos = new ArregloDinamico<>(capacidad);
		this.grafo = new GrafoListaAdyacencia(capacidad);
		this.paises = new TablaHashLinearProbing(capacidad);
		this.points = new TablaHashLinearProbing(capacidad);
		this.landingidtabla = new TablaHashSeparteChaining(capacidad);
		this.nombrecodigo = new TablaHashSeparteChaining(capacidad);
	}

	/**
	 * Servicio de consulta de numero de elementos presentes en el modelo
	 *
	 * @return numero de elementos presentes en el modelo
	 */
	public int darTamano() {
		return datos.size();
	}

	/**
	 * Requerimiento buscar dato
	 *
	 * @param dato Dato a buscar
	 * @return dato encontrado
	 * @throws VacioException
	 * @throws PosException
	 */
	public YoutubeVideo getElement(int i) throws PosException, VacioException {
		Object element = datos.getElement(i);
		if (element instanceof YoutubeVideo) {
			return (YoutubeVideo) element;
		} else {
			throw new ClassCastException("Element is not of type YoutubeVideo");
		}
	}

	@Override
	public String toString() {
		StringBuilder fragmento = new StringBuilder("Info básica:");
		final String NOMBRE = "\n Nombre: ";

		fragmento.append("\n El número total de conexiones (arcos) en el grafo es: ").append(grafo.edges().size());
		fragmento.append("\n El número total de puntos de conexión (landing points) en el grafo: ")
				.append(grafo.vertices().size());
		fragmento.append("\n La cantidad total de países es: ").append(paises.size());

		try {
			Landing primerLanding = (Landing) ((NodoTS<String, Landing>) points.darListaNodos().getElement(1))
					.getValue();
			fragmento.append("\n Info primer landing point ")
					.append("\n Identificador: ").append(primerLanding.getId())
					.append(NOMBRE).append(primerLanding.getName())
					.append(" \n Latitud ").append(primerLanding.getLatitude())
					.append(" \n Longitud ").append(primerLanding.getLongitude());

			Country ultimoPais = (Country) ((NodoTS<String, Country>) paises.darListaNodos()
					.getElement(paises.darListaNodos().size()))
					.getValue();
			fragmento.append("\n Info último país: ")
					.append("\n Capital: ").append(ultimoPais.getCapitalName())
					.append("\n Población: ").append(ultimoPais.getPopulation())
					.append("\n Usuarios: ").append(ultimoPais.getUsers());
		} catch (PosException | VacioException e) {
			fragmento.append("\n Error al obtener información: ").append(e.getMessage());
		}

		return fragmento.toString();
	}

	public String req1String(String punto1, String punto2) {
		ITablaSimbolos tablaSimbolos = grafo.getSSC();
		ILista<Integer> listaValores = tablaSimbolos.valueSet();
		int maxComponentes = 0;

		// Encontrar el valor máximo en la lista de valores
		for (int i = 1; i <= listaValores.size(); i++) {
			try {
				int valorActual = listaValores.getElement(i);
				if (valorActual > maxComponentes) {
					maxComponentes = valorActual;
				}
			} catch (PosException | VacioException e) {
				// Usar un logger en lugar de System.out.println
				Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		StringBuilder resultado = new StringBuilder();
		resultado.append("La cantidad de componentes conectados es: ").append(maxComponentes);

		try {
			String codigo1 = nombrecodigo.get(punto1);
			String codigo2 = nombrecodigo.get(punto2);
			Vertex vertice1 = ((ILista<Vertex>) landingidtabla.get(codigo1)).getElement(1);
			Vertex vertice2 = ((ILista<Vertex>) landingidtabla.get(codigo2)).getElement(1);

			int idVertice1 = tablaSimbolos.get(vertice1.getId());
			int idVertice2 = tablaSimbolos.get(vertice2.getId());

			if (idVertice1 == idVertice2) {
				resultado.append("\nLos landing points pertenecen al mismo clúster");
			} else {
				resultado.append("\nLos landing points no pertenecen al mismo clúster");
			}
		} catch (PosException | VacioException e) {
			// Usar un logger en lugar de e.printStackTrace()
			Logger.getLogger(Modelo.class.getName()).log(Level.SEVERE, null, e);
		}

		return resultado.toString();
	}

	public String req2String() {
		StringBuilder fragmento = new StringBuilder();
		ILista lista = landingidtabla.valueSet();
		int contador = 0;

		for (int i = 1; i <= lista.size(); i++) {
			try {
				ILista sublist = (ILista) lista.getElement(i);
				if (sublist.size() > 1 && contador <= 10) {
					Landing landing = (Landing) ((Vertex) sublist.getElement(1)).getInfo();
					int cantidad = 0;

					for (int j = 1; j <= sublist.size(); j++) {
						cantidad += ((Vertex) sublist.getElement(j)).edges().size();
					}

					fragmento.append("\n Landing ")
							 .append("\n Nombre: ").append(landing.getName())
							 .append("\n País: ").append(landing.getPais())
							 .append("\n Id: ").append(landing.getId())
							 .append("\n Cantidad: ").append(cantidad);

					contador++;
				}
			} catch (PosException | VacioException e) {
				e.printStackTrace();
			}
		}

		return fragmento.toString();
	}

	public String req3String(String pais1, String pais2) {
		Country pais11 = (Country) paises.get(pais1);
		Country pais22 = (Country) paises.get(pais2);
		String capital1 = pais11.getCapitalName();
		String capital2 = pais22.getCapitalName();

		PilaEncadenada pila = grafo.minPath(capital1, capital2);

		float distancia = 0;
		StringBuilder fragmento = new StringBuilder("Ruta: ");
		float disttotal = 0;

		while (!pila.isEmpty()) {
			Edge arco = (Edge) pila.pop();
			Object sourceInfo = arco.getSource().getInfo();
			Object destInfo = arco.getDestination().getInfo();

			double longorigen = 0, longdestino = 0, latorigen = 0, latdestino = 0;
			String origennombre = "", destinonombre = "";

			if (sourceInfo instanceof Landing) {
				Landing landing = (Landing) sourceInfo;
				longorigen = landing.getLongitude();
				latorigen = landing.getLatitude();
				origennombre = landing.getLandingId();
			} else if (sourceInfo instanceof Country) {
				Country country = (Country) sourceInfo;
				longorigen = country.getLongitude();
				latorigen = country.getLatitude();
				origennombre = country.getCapitalName();
			}

			if (destInfo instanceof Landing) {
				Landing landing = (Landing) destInfo;
				longdestino = landing.getLongitude();
				latdestino = landing.getLatitude();
				destinonombre = landing.getLandingId();
			} else if (destInfo instanceof Country) {
				Country country = (Country) destInfo;
				longdestino = country.getLongitude();
				latdestino = country.getLatitude();
				destinonombre = country.getCapitalName();
			}

			distancia = distancia(longdestino, latdestino, longorigen, latorigen);
			fragmento.append("\n \n Origen: ").append(origennombre)
					.append("  Destino: ").append(destinonombre)
					.append("  Distancia: ").append(distancia);
			disttotal += distancia;
		}

		fragmento.append("\n Distancia total: ").append(disttotal);

		return fragmento.toString();
	}

	public String req4String() {
		StringBuilder fragmento = new StringBuilder();
		ILista listaVertices = landingidtabla.valueSet();
		String llave = "";
		int distanciaTotal = 0;

		try {
			int maxVertices = 0;
			for (int i = 1; i <= listaVertices.size(); i++) {
				ILista listaActual = (ILista) listaVertices.getElement(i);
				if (listaActual.size() > maxVertices) {
					maxVertices = listaActual.size();
					llave = (String) ((Vertex) listaActual.getElement(1)).getId();
				}
			}

			ILista arcosMST = grafo.mstPrimLazy(llave);
			ITablaSimbolos tablaSimbolos = new TablaHashSeparteChaining<>(2);
			ILista candidatos = new ArregloDinamico<>(1);

			for (int i = 1; i <= arcosMST.size(); i++) {
				Edge arco = (Edge) arcosMST.getElement(i);
				distanciaTotal += arco.getWeight();

				candidatos.insertElement(arco.getSource(), candidatos.size() + 1);
				candidatos.insertElement(arco.getDestination(), candidatos.size() + 1);

				tablaSimbolos.put(arco.getDestination().getId(), arco.getSource());
			}

			ILista verticesUnificados = unificar(candidatos, "Vertice");
			fragmento.append(" La cantidad de nodos conectada a la red de expansión mínima es: ")
					.append(verticesUnificados.size())
					.append("\n El costo total es de: ")
					.append(distanciaTotal);

			int maximoCamino = 0;
			PilaEncadenada caminoMaximo = new PilaEncadenada();

			for (int i = 1; i <= verticesUnificados.size(); i++) {
				PilaEncadenada caminoActual = new PilaEncadenada();
				String idBusqueda = (String) ((Vertex) verticesUnificados.getElement(i)).getId();
				Vertex actual;
				int contador = 0;

				while ((actual = (Vertex) tablaSimbolos.get(idBusqueda)) != null && actual.getInfo() != null) {
					caminoActual.push(actual);
					idBusqueda = (String) actual.getId();
					contador++;
				}

				if (contador > maximoCamino) {
					maximoCamino = contador;
					caminoMaximo = caminoActual;
				}
			}

			fragmento.append("\n La rama más larga está dada por los vértices: ");
			for (int i = 1; i <= caminoMaximo.size(); i++) {
				Vertex vertice = (Vertex) caminoMaximo.pop();
				fragmento.append("\n Id ").append(i).append(" : ").append(vertice.getId());
			}
		} catch (PosException | VacioException | NullException e) {
			e.printStackTrace();
		}

		return fragmento.length() == 0 ? "No hay ninguna rama" : fragmento.toString();
	}

	public ILista req5(String punto) {
		String codigo = (String) nombrecodigo.get(punto);
		ILista lista = (ILista) landingidtabla.get(codigo);

		ILista countries = new ArregloDinamico<>(1);
		try {
			Country paisoriginal = obtenerPaisOriginal(lista);
			countries.insertElement(paisoriginal, countries.size() + 1);
		} catch (PosException | VacioException | NullException e1) {
			manejarExcepcion(e1);
		}

		for (int i = 1; i <= lista.size(); i++) {
			try {
				Vertex vertice = (Vertex) lista.getElement(i);
				ILista arcos = vertice.edges();

				for (int j = 1; j <= arcos.size(); j++) {
					Vertex vertice2 = ((Edge) arcos.getElement(j)).getDestination();
					procesarVertice(vertice2, countries);
				}

			} catch (PosException | VacioException | NullException e) {
				manejarExcepcion(e);
			}
		}

		ILista unificado = unificar(countries, "Country");

		Comparator<Country> comparador = new ComparadorXKm();
		OrdenamientoContexto<Country> algsOrdenamientoEventos = new OrdenamientoContexto<>();

		try {
			if (lista != null) {
				algsOrdenamientoEventos.ordenar(lista, comparador, false);
			}
		} catch (PosException | VacioException | NullException e) {
			manejarExcepcion(e);
		}

		return unificado;
	}

	private Country obtenerPaisOriginal(ILista lista) throws PosException, VacioException, NullException {
		return (Country) paises.get(((Landing) ((Vertex) lista.getElement(1)).getInfo()).getPais());
	}

	private void procesarVertice(Vertex vertice2, ILista countries) throws PosException, VacioException, NullException {
		Country pais = null;
		if (vertice2.getInfo().getClass().getName().equals("model.data_structures.Landing")) {
			Landing landing = (Landing) vertice2.getInfo();
			pais = (Country) paises.get(landing.getPais());
			countries.insertElement(pais, countries.size() + 1);

			float distancia = distancia(pais.getLongitude(), pais.getLatitude(), landing.getLongitude(), landing.getLatitude());
			pais.setDistlan(distancia);
		} else {
			pais = (Country) vertice2.getInfo();
		}
	}

	private void manejarExcepcion(Exception e) {
		// Manejo adecuado de excepciones
		e.printStackTrace();
	}

	public String req5String(String punto) {
		ILista afectados = req5(punto);

		StringBuilder fragmento = new StringBuilder(
				"La cantidad de paises afectados es: " + afectados.size() + "\n Los paises afectados son: ");

		for (int i = 1; i <= afectados.size(); i++) {
			try {
				Country country = (Country) afectados.getElement(i);
				fragmento.append("\n Nombre: ").append(country.getCountryName())
						.append("\n Distancia al landing point: ").append(country.getDistlan());
			} catch (PosException | VacioException e) {
				// Manejar la excepción de manera adecuada
				fragmento.append("\n Error al obtener el país en la posición ").append(i).append(": ")
						.append(e.getMessage());
			}
		}

		return fragmento.toString();
	}

	public ILista unificar(ILista lista, String criterio) {
		ILista lista2 = new ArregloDinamico(1);

		if (lista == null) {
			return lista2;
		}

		try {
			if (criterio.equals("Vertice")) {
				Comparator<Vertex<String, Landing>> comparador = new Vertex.ComparadorXKey();
				OrdenamientoContexto<Vertex<String, Landing>> algsOrdenamientoEventos = new OrdenamientoContexto<>();
				algsOrdenamientoEventos.ordenar(lista, comparador, false);
				unificarLista(lista, lista2, comparador);
			} else {
				Comparator<Country> comparador = new Country.ComparadorXNombre();
				OrdenamientoContexto<Country> algsOrdenamientoEventos = new OrdenamientoContexto<>();
				algsOrdenamientoEventos.ordenar(lista, comparador, false);
				unificarLista(lista, lista2, comparador);
			}
		} catch (PosException | VacioException | NullException e) {
			e.printStackTrace();
		}

		return lista2;
	}

	private <T> void unificarLista(ILista lista, ILista lista2, Comparator<T> comparador)
			throws PosException, VacioException, NullException {
		for (int i = 1; i <= lista.size(); i++) {
			T actual = (T) lista.getElement(i);
			T siguiente = (T) lista.getElement(i + 1);

			if (siguiente != null) {
				if (comparador.compare(actual, siguiente) != 0) {
					lista2.insertElement(actual, lista2.size() + 1);
				}
			} else {
				T anterior = (T) lista.getElement(i - 1);

				if (anterior != null) {
					if (comparador.compare(anterior, actual) != 0) {
						lista2.insertElement(actual, lista2.size() + 1);
					}
				} else {
					lista2.insertElement(actual, lista2.size() + 1);
				}
			}
		}
	}

	public ITablaSimbolos unificarHash(ILista lista) {

		Comparator<Vertex<String, Landing>> comparador = null;

		OrdenamientoContexto<Vertex<String, Landing>> algsOrdenamientoEventos = new OrdenamientoContexto<Vertex<String, Landing>>();
		;

		comparador = new Vertex.ComparadorXKey();

		ITablaSimbolos tabla = new TablaHashSeparteChaining<>(2);

		try {

			if (lista != null) {
				algsOrdenamientoEventos.ordenar(lista, comparador, false);

				for (int i = 1; i <= lista.size(); i++) {
					Vertex actual = (Vertex) lista.getElement(i);
					Vertex siguiente = (Vertex) lista.getElement(i + 1);

					if (siguiente != null) {
						if (comparador.compare(actual, siguiente) != 0) {
							tabla.put(actual.getId(), actual);
						}
					} else {
						Vertex anterior = (Vertex) lista.getElement(i - 1);

						if (anterior != null) {
							if (comparador.compare(anterior, actual) != 0) {
								tabla.put(actual.getId(), actual);
							}
						} else {
							tabla.put(actual.getId(), actual);
						}
					}

				}
			}
		} catch (PosException | VacioException | NullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tabla;
	}

	public void cargar() throws IOException {
		grafo = new GrafoListaAdyacencia(2);
		paises = new TablaHashLinearProbing(2);
		points = new TablaHashLinearProbing(2);
		landingidtabla = new TablaHashSeparteChaining(2);
		nombrecodigo = new TablaHashSeparteChaining(2);

		Reader in = new FileReader("./data/countries.csv");
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withHeader().parse(in);
		int contador = 1;
		for (CSVRecord record : records) {
			if (!record.get(0).equals("")) {
				String countryName = record.get(0);

				String capitalName = record.get(1);

				double latitude = Double.parseDouble(record.get(2));

				double longitude = Double.parseDouble(record.get(3));

				String code = record.get(4);

				String continentName = record.get(5);

				float population = Float.parseFloat(record.get(6).replace(".", ""));

				double users = Double.parseDouble(record.get(7).replace(".", ""));
				;

				Country pais = new Country(countryName, capitalName, latitude, longitude, code, continentName,
						population, users);

				grafo.insertVertex(capitalName, pais);
				paises.put(countryName, pais);

				contador++;
			}

		}

		Reader in2 = new FileReader("./data/landing_points.csv");
		Iterable<CSVRecord> records2 = CSVFormat.RFC4180.withHeader().parse(in2);

		int contador2 = 1;

		for (CSVRecord record2 : records2) {

			String landingId = record2.get(0);

			String id = record2.get(1);

			String[] x = record2.get(2).split(", ");

			String name = x[0];

			String paisnombre = x[x.length - 1];

			double latitude = Double.parseDouble(record2.get(3));

			double longitude = Double.parseDouble(record2.get(4));

			Landing landing = new Landing(landingId, id, name, paisnombre, latitude, longitude);

			points.put(landingId, landing);

			Country pais = null;
		}

		Reader in3 = new FileReader("./data/connections.csv");
		Iterable<CSVRecord> records3 = CSVFormat.RFC4180.withHeader().parse(in3);

		int contador3 = 1;
		for (CSVRecord record3 : records3) {
			String origin = record3.get(0);

			String destination = record3.get(1);

			String cableid = record3.get(3);

			String[] lengths = record3.get(4).split(" ");

			String length = lengths[0];

			Landing landing1 = (Landing) points.get(origin);

			grafo.insertVertex(landing1.getLandingId() + cableid, landing1);

			Vertex vertice1 = grafo.getVertex(landing1.getLandingId() + cableid);

			Landing landing2 = (Landing) points.get(destination);

			grafo.insertVertex(landing2.getLandingId() + cableid, landing2);

			Vertex vertice2 = grafo.getVertex(landing2.getLandingId() + cableid);

			String nombrepais1 = landing1.getPais();

			String nombrepais2 = landing2.getPais();

			Country pais1 = null;
			Country pais2 = null;
			if (nombrepais1.equals("Côte d'Ivoire")) {
				pais1 = (Country) paises.get("Cote d'Ivoire");
			} else if (nombrepais2.equals("Côte d'Ivoire")) {
				pais2 = (Country) paises.get("Cote d'Ivoire");
			} else {
				pais1 = (Country) paises.get(nombrepais1);
				pais2 = (Country) paises.get(nombrepais2);
			}

			if (pais1 != null) {
				float weight = distancia(pais1.getLongitude(), pais1.getLatitude(), landing1.getLongitude(),
						landing1.getLatitude());

				grafo.addEdge(pais1.getCapitalName(), landing1.getLandingId() + cableid, weight);
			}

			if (pais2 != null) {
				float weight2 = distancia(pais2.getLongitude(), pais2.getLatitude(), landing1.getLongitude(),
						landing1.getLatitude());

				grafo.addEdge(pais2.getCapitalName(), landing2.getLandingId() + cableid, weight2);

			}

			if (landing1 != null) {
				if (landing2 != null) {
					Edge existe1 = grafo.getEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid);

					if (existe1 == null) {
						float weight3 = distancia(landing1.getLongitude(), landing1.getLatitude(),
								landing2.getLongitude(), landing2.getLatitude());
						grafo.addEdge(landing1.getLandingId() + cableid, landing2.getLandingId() + cableid, weight3);
					} else {
						float weight3 = distancia(landing1.getLongitude(), landing1.getLatitude(),
								landing2.getLongitude(), landing2.getLatitude());
						float peso3 = existe1.getWeight();

						if (weight3 > peso3) {
							existe1.setWeight(weight3);
						}
					}
				}
			}

			try {

				ILista elementopc = (ILista) landingidtabla.get(landing1.getLandingId());
				if (elementopc == null) {
					ILista valores = new ArregloDinamico(1);
					valores.insertElement(vertice1, valores.size() + 1);

					landingidtabla.put(landing1.getLandingId(), valores);

				} else if (elementopc != null) {
					elementopc.insertElement(vertice1, elementopc.size() + 1);
				}

				elementopc = (ILista) landingidtabla.get(landing2.getLandingId());

				if (elementopc == null) {
					ILista valores = new ArregloDinamico(1);
					valores.insertElement(vertice2, valores.size() + 1);

					landingidtabla.put(landing2.getLandingId(), valores);

				} else if (elementopc != null) {
					elementopc.insertElement(vertice2, elementopc.size() + 1);

				}

				elementopc = (ILista) nombrecodigo.get(landing1.getLandingId());

				if (elementopc == null) {
					String nombre = landing1.getName();
					String codigo = landing1.getLandingId();

					nombrecodigo.put(nombre, codigo);

				}
			} catch (PosException | NullException e) {
				e.printStackTrace();
			}

		}

		try {
			ILista valores = landingidtabla.valueSet();

			for (int i = 1; i <= valores.size(); i++) {
				for (int j = 1; j <= ((ILista) valores.getElement(i)).size(); j++) {
					Vertex vertice1;
					if ((ILista) valores.getElement(i) != null) {
						vertice1 = (Vertex) ((ILista) valores.getElement(i)).getElement(j);
						for (int k = 2; k <= ((ILista) valores.getElement(i)).size(); k++) {
							Vertex vertice2 = (Vertex) ((ILista) valores.getElement(i)).getElement(k);
							grafo.addEdge(vertice1.getId(), vertice2.getId(), 100);
						}
					}
				}
			}
		} catch (PosException | VacioException e) {
			e.printStackTrace();
		}

	}

	private static float distancia(double lon1, double lat1, double lon2, double lat2) {

		double earthRadius = 6371; //

		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		double dlon = (lon2 - lon1);
		double dlat = (lat2 - lat1);

		double sinlat = Math.sin(dlat / 2);
		double sinlon = Math.sin(dlon / 2);

		double a = (sinlat * sinlat) + Math.cos(lat1) * Math.cos(lat2) * (sinlon * sinlon);
		double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));

		double distance = earthRadius * c;

		return (int) distance;

	}

}
