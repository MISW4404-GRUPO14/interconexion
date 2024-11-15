package controller;

import java.io.IOException;
import java.util.Scanner;
import model.logic.Modelo;
import view.View;

public class Controller {

    private Modelo modelo;

    private View view;

    public Controller(Modelo modelo, View view) {
        this.modelo = modelo;
        this.view = view;
    }

    public void run() {
        Scanner lector = new Scanner(System.in).useDelimiter("\n");
        boolean fin = false;

        while (!fin) {
            view.printMenu();
            int option = lector.nextInt();

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
                    view.printMessage("--------- \n Hasta pronto !! \n---------");
                    fin = true;
                    break;
                default:
                    view.printMessage("--------- \n Opcion Invalida !! \n---------");
                    break;
            }
        }
        lector.close();
    }

    private void cargarDatos() {
        view.printMessage("--------- \nCargar datos");
        try {
            modelo.cargar();
            view.printModelo(modelo);
        } catch (IOException e) {
            view.printMessage("Error cargando datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void ejecutarReq1(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del primer punto de conexión");
        String punto1 = lector.next();
        lector.nextLine();

        view.printMessage("--------- \nIngrese el nombre del segundo punto de conexión");
        String punto2 = lector.next();
        lector.nextLine();

        String resultado = modelo.req1String(punto1, punto2);
        view.printMessage(resultado);
    }

    private void ejecutarReq2() {
        String resultado = modelo.req2String();
        view.printMessage(resultado);
    }

    private void ejecutarReq3(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del primer país");
        String pais1 = lector.next();
        lector.nextLine();

        view.printMessage("--------- \nIngrese el nombre del segundo país");
        String pais2 = lector.next();
        lector.nextLine();

        String resultado = modelo.req3String(pais1, pais2);
        view.printMessage(resultado);
    }

    private void ejecutarReq4() {
        String resultado = modelo.req4String();
        view.printMessage(resultado);
    }

    private void ejecutarReq5(Scanner lector) {
        view.printMessage("--------- \nIngrese el nombre del punto de conexión");
        String landing = lector.next();
        lector.nextLine();

        String resultado = modelo.req5String(landing);
        view.printMessage(resultado);
    }
}