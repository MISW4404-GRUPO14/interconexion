package main;
import controller.Controller;
import model.logic.Modelo;

public class Main {
	
	public static void main(String[] args) 
	{
		Modelo modelo = new Modelo(1);
		Controller controler = new Controller(modelo);
		controler.run();
	}
}
