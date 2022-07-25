package juego;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import entorno.Entorno;
import entorno.Herramientas;

public class Escenario {

	private int nivel;
	public Entorno entorno;
	Rectangulo[] pisos = new Rectangulo[5];
	Rectangulo[] margenes = new Rectangulo[4];
	private List<Consumible> consumibles;

	public Escenario(Entorno entorno) {
		this.nivel = 1;
		this.entorno = entorno;
		this.consumibles = new ArrayList<Consumible>();
	}

	public void generarMargenes() {
		this.margenes[0] = new Rectangulo(0, 0, 20, 2000);
		this.margenes[1] = new Rectangulo(0, 0, 2000, 20);
		this.margenes[2] = new Rectangulo(800, 0, 20, 2000);
		this.margenes[3] = new Rectangulo(0, 600, 2000, 20);
	}

	public void setNivel(int numeroNivel) {
		this.nivel = numeroNivel;
	}

	public void generarNiveles() {
		if (!esNivelPar()) {
			generarPisos(600, 0, -100, 900, 15);
		} 
		else {
			generarPisos(0, 600, -100, 900, 15);
		}
	}
	
	private void generarPisos(int variacionX1, int variacionX2, int variacionY, int ancho, int alto)
	{
		this.pisos[0] = new Rectangulo(30, 500, 2000, 10);
		for (int i = 1; i < this.pisos.length; i++) {
			if (i % 2 == 0) {
				this.pisos[i] = new Rectangulo(this.pisos[0].getX() + variacionX1, this.pisos[i - 1].getY() + variacionY, ancho,
						alto);
			} else {
				this.pisos[i] = new Rectangulo(this.pisos[0].getX() + variacionX2, this.pisos[i - 1].getY() + variacionY, ancho, alto);
			}
		}
	}
	
	private boolean esNivelPar() 
	{
		return this.nivel % 2 == 0;
	}

	public void dibujarEscenario() {
		dibujarCastillo();
		dibujarPisos();
		dibujarMargenes();
	}

	public void dibujarPisos() {
		for(Rectangulo piso: pisos) {
			entorno.dibujarRectangulo(piso.getX(), piso.getY(), piso.getAncho(), piso.getAlto(), 0, Color.gray);
		}
	}

	public void dibujarCastillo() {
		this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/CastilloFondo.jpeg"), 400, 111, 0);
	}

	public void dibujarMargenes() {
		for(Rectangulo margen: margenes) {
			entorno.dibujarRectangulo(margen.getX(), margen.getY(), margen.getAncho(), margen.getAlto(), 0, Color.DARK_GRAY);
		}
	}

	public List<Consumible> getConsumibles() {
		return this.consumibles;
	}

	public void generarConsumibles() {
		for (int i = 1; i < this.pisos.length; i++) {
			Consumible corazones = new Consumible(this.pisos[0].getX() + 400, this.pisos[i - 1].getY() - 135, 38, 38, this.entorno);
			this.consumibles.add(corazones);
		}
	}

	public void marcadorNivel() {
		entorno.cambiarFont("Nivel: " + nivel, 20, Color.yellow);
		entorno.escribirTexto("Nivel: " + nivel, 50, 550);
	}

	public int getNivel() {
		return this.nivel;
	}

	public int getAncho() {
		return 0;
	}

	public int getX() {
		return 0;
	}
}