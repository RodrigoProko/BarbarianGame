package juego;
import entorno.Entorno;
import entorno.Herramientas;

public class Rayo {
	static final int _anchoMaxPantalla = 780;
	static final int _anchoMinPantalla = 15;
	int x, y;
	int ancho, alto;
	boolean visible;
	private Rectangulo rayo;
	private int velocidadRayo;
	private Entorno entorno;
	
	public Rayo(int x, int y, int velocidadRayo, int ancho, int alto,Entorno entorno) 
	{
		this.x = x;
		this.y = y;
		this.visible = true;
		this.ancho = ancho;
		this.alto = alto;
		this.rayo = new Rectangulo(this.x, this.y, this.ancho, this.alto);
		this.velocidadRayo = velocidadRayo;
		this.entorno = entorno;
	}
	
	
	public int getX() 
	{
		return this.rayo.getX();
	}
	public int getY() 
	{
		return this.rayo.getY();
	}
	
	public void setX(int dx) {
		this.rayo.setX(dx);
	}
	
	public void setY(int dy) {
		this.rayo.setY(dy);
	}
	
	public Rectangulo getRayo() {
		return this.rayo;
	}
	
	public void dibujarDisparoPersonaje() {
		this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/rayo_new.png"), this.getX(), this.getY(), 150);
	}
	
	public void dibujarDisparoEnemigo() {
		this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/rayo_laser.png"), this.rayo.getX(), this.rayo.getY(), 150);
	}
	
	public boolean hayColision(Rectangulo r1) {
		return this.rayo.hayInterseccion(r1);
	}

	public void mover() 
	{
		this.rayo.trasladar(velocidadRayo, 0);			
		if (this.getX() >= _anchoMaxPantalla) {
			this.visible = false;
		}
		if (this.getX() <= _anchoMinPantalla) {
			this.visible = false;
		}
	}

	public boolean isVisible() {
		return visible;
	}
}
