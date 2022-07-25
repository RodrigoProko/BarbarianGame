package juego;
import java.util.ArrayList;
import entorno.Entorno;
import entorno.Herramientas;


public class Commodore {
	private int x, y;
	private int ancho, largo;
	ArrayList<Commodore> commodores = new ArrayList<Commodore>();
	private Entorno entorno;
	private Rectangulo commodore;

	public Commodore(int x, int y, Entorno entorno) 
	{
		this.x = x;
		this.y = y;
		this.entorno = entorno;
		this.commodore = new Rectangulo(this.x, this.y, ancho, largo);
	}
	public void Dibujar() 
	{
		this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/commodore-64.png"), this.commodore.getX(), 
				this.commodore.getY(), 0);
	}

	public int getX() {
		return this.commodore.getX();
	}

	public int getY() {
		return this.commodore.getY();
	}
	
	public void setX(int dx) {
		this.x = dx;
	}
	
	public void setY(int dy) {
		this.y = dy;
	}
	
	public Rectangulo getCommodore() {
		return this.commodore;
	}
	
	public void getIntersection(Rectangulo r1) {
		this.commodore.hayInterseccion(r1);
	}
}



