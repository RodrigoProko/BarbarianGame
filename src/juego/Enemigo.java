package juego;

import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	public Rectangulo cuerpo;
	public int x;
	public int y;
	public int ancho, alto;
	public int vida;
	public int xFinal;
	public int xInicial;
	public boolean derecha;
	public boolean izquierda;
	public boolean avanzar;
	private boolean bajando;
	private Entorno entorno;
	public int velocidadDisparo;
	public int velocidad;
	
	public Enemigo(int x, int y, int xInicial, int xFinal, Entorno entorno) {
		this.ancho = 80;
		this.alto = 60;
		this.cuerpo = new Rectangulo(this.x, this.y, this.ancho, this.alto);
		this.x = x;
		this.y = y;
		this.xInicial = xInicial;
		this.xFinal = xFinal;
		this.derecha = true;
		this.izquierda = false;
		this.avanzar = true;
		this.bajando = false;
		this.vida = 3;
		this.entorno = entorno;
		this.velocidad = 2;
		this.velocidadDisparo = 5;
	}

	public void mover() {
		trasladar();
		if (xInicial < xFinal) {
			derecha = true;
			izquierda = false;
		}
		if (xInicial > xFinal) {
			derecha = false;
			izquierda = true;
		}
		if (xFinal == x) {
			if (xFinal > xInicial && derecha) {
				bajando = true;
			} 
			if ( xFinal < xInicial && izquierda) {
				bajando = true;
			} 
		}
		if(!bajando) {
			if (derecha) {
				x+= velocidad;
			} else {
				x-= velocidad;
			}			
		}
	}
	
	public void bajar() {
		y++;
		trasladar();
	}
	
	public boolean estaBajando() 
	{
		return bajando;
	}
	
	public boolean tocaPiso(Rectangulo[] pisos) {
		for (int i = 0; i < pisos.length; i++) {
			if (pisos[i].hayInterseccion(this.cuerpo)) {
				 return true;
			}
		}
		return false;
	}
	
	public void setBajando(boolean bajando) {
		this.bajando = bajando;
	}
	
	public void invertirDireccion() {
		int auxXFinal = xFinal;
		xFinal = xInicial;
		xInicial = auxXFinal;
	}

	public boolean estaCercaDe(Personaje personaje) {
		if (xFinal < xInicial) {
			if (Math.abs(personaje.getY() - y) < 40 && izquierda) {
				return true;
			}
		}
		if (xFinal > xInicial) {
			if (Math.abs(personaje.getY() - y) < 40 && derecha) {
				return true;
			}
		}
		return false;
	}

	public boolean colisionaCon(Rayo rayo) {
		return this.cuerpo.hayInterseccion(rayo.getRayo());
	}

	public void dibujarEnemigos() {
		if(this.izquierda) {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/raptor2.png"), this.getX(), this.getY(), 0);
		}
		else {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/raptor.png"), this.getX(), this.getY(), 0);
		}
	}

	public int getAncho() {
		return ancho;
	}

	public int getAlto() {
		return alto;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean vaHaciaIzquierda() {
		return izquierda;
	}

	public void perderVida() {
		vida--;
	}

	public int getVida() {
		return vida;
	}

	public Rectangulo getCuerpo() {
		return this.cuerpo;
	}

	public void trasladar() {
		cuerpo.setX(x);
		cuerpo.setY(y);
	}

	public boolean estaDetenido() {
		return !avanzar;
	}

	public Rayo disparar(Entorno entorno) {
		return new Rayo(cuerpo.getX(), cuerpo.getY()-30, this.derecha ? velocidadDisparo : -velocidadDisparo, 10, 10, entorno);
	}

}
