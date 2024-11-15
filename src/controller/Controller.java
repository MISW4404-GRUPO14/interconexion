package controller;

import java.io.IOException;
import java.util.Scanner;
import model.logic.Modelo;

public class Controller {

    private Modelo modelo;

    public Controller(Modelo modelo) {
        this.modelo = modelo;
    }

    public void run() {
        Scanner lector = new Scanner(System.in).useDelimiter("\n");
        boolean fin = false;

        while (!fin) {
            printMenu();
            int option = Integer.parseInt(lector.nextLine().trim());

            switch (option) {
                case 1:
                    cargarDatos();
                    break;
                case 2:
                    ejecutarReq1(lector);
                    break;
                case 3:
                    ejecutarReq2();
                    break;
                case 4:
                    ejecutarReq3(lector);
                    break;
                case 5:
                    ejecutarReq4();
                    break;
                case 6:
                    ejecutarReq5(lector);
                    break;
                case 7:
                    printMessage("--------- \n Hasta pronto !! \n---------");
                    fin = true;
                    break;
                default:
                    printMessage("--------- \n Opcion Invalida !! \n---------");
                    break;
            }
        }
        lector.close();
    }

    private void cargarDatos() {
        printMessage("--------- \nCargar datos");
        try {
            modelo.cargar();
            printModelo(modelo);
        } catch (IOException e) {
            printMessage("Error cargando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ejecutarReq1(Scanner lector) {
        printMessage("--------- \nIngrese el nombre del primer punto de conexión");
        String punto1 = lector.next();
        lector.nextLine();

        printMessage("--------- \nIngrese el nombre del segundo punto de conexión");
        String punto2 = lector.next();
        lector.nextLine();

        String resultado = modelo.req1String(punto1, punto2);
        printMessage(resultado);
    }

    private void ejecutarReq2() {
        String resultado = modelo.req2String();
        printMessage(resultado);
    }

    private void ejecutarReq3(Scanner lector) {
        printMessage("--------- \nIngrese el nombre del primer país");
        String pais1 = lector.next();
        lector.nextLine();

        printMessage("--------- \nIngrese el nombre del segundo país");
        String pais2 = lector.next();
        lector.nextLine();

        String resultado = modelo.req3String(pais1, pais2);
        printMessage(resultado);
    }

    private void ejecutarReq4() {
        String resultado = modelo.req4String();
        printMessage(resultado);
    }

    private void ejecutarReq5(Scanner lector) {
        printMessage("--------- \nIngrese el nombre del punto de conexión");
        String landing = lector.next();
        lector.nextLine();

        String resultado = modelo.req5String(landing);
        printMessage(resultado);
    }
    
    public void printMenu()
	{
		System.out.println("1. Cargar datos");
		System.out.println("2. Componentes conectados");
		System.out.println("3. Encontrar landings interconexión");
		System.out.println("4. Ruta mínima");
		System.out.println("5. Red de expansión mínima");
		System.out.println("6. Fallas en conexión");
		System.out.println("7. Exit");
		System.out.println("Dar el numero de opcion a resolver, luego oprimir tecla Return: (e.g., 1):");
	}

	public void printMessage(String mensaje) {

		System.out.println(mensaje);
	}		
	
	public void printModelo(Modelo modelo)
	{
		System.out.println(modelo);
	}
}