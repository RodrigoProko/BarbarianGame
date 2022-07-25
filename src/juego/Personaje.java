package juego;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import entorno.Entorno;
import entorno.Herramientas;

public class Personaje {
	private Rectangulo cuerpo;
	private int ancho;
	private int alto;
	private int speed;
	private int x;
	private int y;
	private int pisoActual;
	public Entorno entorno;
	private boolean movHaciaDerecha;
	private int framesAereos;
	private Escenario escenario;
	private boolean subiendo;
	private boolean bajando;
	private boolean agachado;
	private boolean stop;
	private int vida;
	private boolean estaVivo;
	private char ladoAgachado;
	final int velocidadDisparo = 7;
	private List<Consumible> vidas;
	final int posXCorazon = 250;
	final int posYCorazon = 545;

	public Personaje(int x, int y, int ancho, int alto, int speed, Entorno entorno, Escenario escenario) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.speed = speed;
		this.vida = 3;
		this.entorno = entorno;
		this.cuerpo = new Rectangulo(this.x, this.y, this.ancho, this.alto);
		this.movHaciaDerecha = true;
		this.subiendo = false;
		this.bajando = false;
		this.agachado = false;
		this.stop = false;
		this.framesAereos = 70;
		this.escenario = escenario;
		this.vidas = new ArrayList<Consumible>();
		this.estaVivo = true;
		this.ladoAgachado = 'd';
	}

	public void gravedad() {
		if(tocaPiso() == -1 && !this.subiendo) {
			this.bajando = true;
			this.cuerpo.trasladar(0, this.speed);
		}
		if(tocaTecho() && this.subiendo) {
			this.cuerpo.trasladar(0, this.speed);
			this.bajando = true;
			this.subiendo = tocaTecho() ? true : false;
		}
		else
			this.bajando = false;
	}
	

	public void setCuerpo(int x, int y, int alto, int ancho) {
		this.cuerpo.setX(x);
		this.cuerpo.setY(y);
		this.cuerpo.setAncho(ancho);
		this.cuerpo.setAlto(alto);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void setStop() {
		this.stop = true;
	}
	
	public Rectangulo getCuerpo() {
		return this.cuerpo;
	}

	public void setX(int posicionX) {
		this.x = posicionX;
	}

	public void setY(int posicionY) {
		this.y = posicionY;
	}

	public int getX() {
		return this.cuerpo.getX();
	}

	public int getAlto() {
		return this.cuerpo.getAlto();
	}

	public int getAncho() {
		return this.cuerpo.getAncho();
	}

	public int getY() {
		return this.cuerpo.getY();
	}

	public void moverDerecha() {
		this.ladoAgachado = 'd';
		if (!tocaMargenDerecho() && !this.agachado) {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikinga.png"), this.getX(), this.getY(),
        0);
			this.cuerpo.trasladar(this.speed, 0);
			this.movHaciaDerecha = true;
		}
	}

	public void moverIzquierda() {
		this.ladoAgachado = 'i';
		if (!tocaMargenIzquierdo() && !this.agachado) {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikinga2.png"), this.getX(), this.getY(),
        0);
			this.cuerpo.trasladar(-this.speed, 0);
			this.movHaciaDerecha = false;
		}
	}

	public boolean hayColision(Rectangulo r1) {
		return this.cuerpo.hayInterseccion(r1);
	}

	public void moverAbajo() {
		this.setCuerpo(this.getX(), this.getY(), this.getAncho(), 15);
	}

	public Rayo disparar() {
		return new Rayo(cuerpo.getX(), cuerpo.getY(), this.movHaciaDerecha ? velocidadDisparo : -velocidadDisparo, 10, 10, this.entorno);
	}

	public void moverArriba() {
		if(this.subiendo && !tocaTecho())
		this.cuerpo.trasladar(0, -this.speed);
	}
	
	
	public void alturaMaxima()
	{
		if(this.framesAereos == 0) 
		{
			this.subiendo = false;
		}
		
		if((this.subiendo && tocaTecho()) || (this.subiendo && tocaPiso() != -1)) {
			this.framesAereos = 0;
		}
		else 
		{
			this.framesAereos--;
		}		
	}
	
	public void generarCorazones() {
			Consumible corazones = new Consumible(posXCorazon, posYCorazon, 10, 10, this.entorno);
			vidas.add(corazones);
	}
	public List<Consumible> getCorazonesDeVida() {
		return this.vidas;
	}
	
	public void resetearFramesAereos() 
	{	
		this.framesAereos = 70;
	}

	
	private boolean tocaTecho() {
		int tocaPiso = tocaPiso();
		return tocaPiso != -1 && tocaPiso != pisoActual;
	}
	
	public void mover() {
		if(!estaVivo) return;
		

		gravedad();
		

		if(entorno.estaPresionada(entorno.TECLA_DERECHA) && !this.stop) {
			moverDerecha();
		}
		
		if (entorno.sePresiono(entorno.TECLA_ARRIBA) && tocaPiso() != -1 && !this.stop && !this.agachado) {
			this.subiendo = true;
			this.moverArriba();
		}
		
		if(!entorno.sePresiono(entorno.TECLA_ARRIBA) && tocaPiso() == -1 && !this.stop) {
			this.moverArriba();
			this.alturaMaxima();
		}
		
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && !this.stop) {
			this.moverIzquierda();
		}
		
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO) && !this.subiendo && !this.bajando) {
			this.agachado = true;
			this.moverAbajo();
			if (ladoAgachado == 'd'){
                this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikingaAgachada1.png"), this.getX(), this.getY()-15,
        0);
            }
			else{

                this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikingaAgachada2.png"), this.getX(), this.getY()-15,
        0);
            }
		}
		
		
		
		if ((!entorno.estaPresionada(entorno.TECLA_ABAJO) && !this.agachado) || this.subiendo || this.bajando) {
			this.setCuerpo(this.getX(), this.getY(), this.getAncho(), 70);
			if (ladoAgachado == 'd'){
                this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikinga.png"), this.getX(), this.getY(),
        0);
            }else
            {
                this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/vikinga2.png"), this.getX(), this.getY(),
        0);
            }
		}
		
		
		if (!entorno.estaPresionada(entorno.TECLA_ABAJO) && this.agachado && tocaPiso() != -1 && !this.entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			this.setCuerpo(this.getX(), this.getY()-35, this.getAncho(), 70);
            this.agachado = false;		
		}


		if(this.subiendo == false && this.bajando == false && tocaPiso() != -1)
		{
			this.resetearFramesAereos();
		}
		
	}

	public static boolean tocaPiso(Rectangulo cuerpo, Rectangulo piso) {
		return piso.hayInterseccion(cuerpo);
	}

	public int tocaPiso() {
		for (int i = 0; i < escenario.pisos.length; i++) {
			if (escenario.pisos[i].hayInterseccion(this.cuerpo)) {
				if(!subiendo) {
					this.pisoActual = i;
				}
				return i;
			}
		}
		return -1;
	}
	
	public int tocaPiso(int i) {
		if(i<0 || i>= escenario.pisos.length) return -1;
		if(escenario.pisos[i].hayInterseccion(this.cuerpo)) {
			if(!subiendo) {
				this.pisoActual = i;
			}
			return i;
		}
		return -1;
	}

	public void dibujarVidas() {
		entorno.cambiarFont("Arial" + vida, 20, Color.yellow);
		entorno.escribirTexto("Vidas: ", 200, 550);
		dibujoCorazones();
	}
	
	private void dibujoCorazones() {
		int contador = 30;
			for (Consumible corazones : this.vidas) {
				this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/corazon_opt.png"), corazones.getX() +contador, corazones.getY(), 0);
				contador += 30;
			}			
	}

	public void morir() {
		estaVivo = false;
	}

	public boolean estaVivo() {
		return estaVivo;
	}
	
	public boolean tocaMargenIzquierdo() {
		return cuerpo.getX() < 20;
	}

	public boolean tocaMargenDerecho() {
		return cuerpo.getX() > 780;
	}

	public int getPisoActual() {
		return this.pisoActual;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getVida() {
		return this.vida;
	}

	public void perderVida() {
		vida--;
		this.vidas.remove(0);
		if(vida <= 0 && this.vidas.isEmpty()) {
			morir();
		}
	}
	
	public boolean estaCercaDe(Commodore commodore) {
		return (Math.abs(cuerpo.getY() - commodore.getY()) < 70);
	}

}
