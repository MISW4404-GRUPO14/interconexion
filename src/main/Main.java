package main;
import controller.Controller;
import model.logic.Modelo;
import view.View;

public class Main {
	
	public static void main(String[] args) 
	{
		Modelo modelo = new Modelo(1);
        View view = new View();
		Controller controler = new Controller(modelo, view);
		controler.run();
	}
}
