package juego;

public class Rectangulo {
    private int x;
    private int y;
    private int ancho;
    private int alto;

    public Rectangulo (int x, int y, int ancho, int alto){
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
    }

    public void trasladar(int dx, int dy){
        this.x += dx;
        this.y += dy;
    }
    
    public void setAncho(int ancho) {
    	this.ancho = ancho;
    }
    
    public void setAlto(int alto) {
    	this.alto = alto;
    }
    
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public void setX(int dx) {
    	this.x = dx;
    }
    
    public void setY(int dy) {
    	this.y = dy;
    }

    public int getAncho() {
        return this.ancho;
    }

    public int getAlto() {
        return this.alto;
    }

    public boolean colisionaArriba(Rectangulo r1) {
    	return !(this.getY() - (this.getAlto()/2) > r1.getY() + (r1.getAlto()/2));
    }
    
    public boolean colisionaAbajo(Rectangulo r1) {
    	return !(this.getY() + (this.getAlto()/2) < r1.getY() - (r1.getAlto()/2));
    }
    
    public boolean hayInterseccion(Rectangulo r1){
    	boolean sinColisionArriba = this.getY() - (this.getAlto()/2) > r1.getY() + (r1.getAlto()/2);
        boolean sinColisionAbajo = this.getY() + (this.getAlto()/2) < r1.getY() - (r1.getAlto()/2);
        boolean sinColisionIzquierda = this.getX() - this.getAncho()/2 > r1.getX() + r1.getAncho()/2;
        boolean sinColisionDerecha = this.getX() + (this.getAncho()/2) < r1.getX() - (r1.getAncho()/2);
        return !(sinColisionArriba || sinColisionAbajo || sinColisionDerecha || sinColisionIzquierda);
    }
 
}
