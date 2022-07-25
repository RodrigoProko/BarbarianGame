package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Consumible {
	private int x,y;
	private int ancho,alto;
	private Entorno entorno;
	private Rectangulo consumible;
	
	public Consumible(int x, int y, int ancho, int alto, Entorno entorno) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.consumible = new Rectangulo(this.x, this.y, this.ancho, this.alto);
		this.entorno = entorno;
	}
	
	public Rectangulo getConsumible() {
		return this.consumible;
	}
	
	public int getX() {
		return this.consumible.getX();
	}
	
	public int getY() {
		return this.consumible.getY();
	}
	
	public boolean hayColision(Rectangulo r1) {
		return this.consumible.hayInterseccion(r1);
	}
	
	public void dibujarConsumible() {
		this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/corazon_opt.png"), this.getX(), this.getY(), 0);
	}
}
