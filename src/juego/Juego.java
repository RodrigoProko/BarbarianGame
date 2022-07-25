package juego;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	private Entorno entorno;
	private Personaje personaje;
	private List<Enemigo> enemigos;
	private Commodore item;
	private Escenario escenario;
	private List<Rayo> disparos;
	private List<Consumible> consumiblesEnMapa;
	private List<Rayo> rayosEnemigo;
	private List<Consumible> vidasDelPersonaje;
	private int powerUpsMaxEnPantalla = 1;
	private int disparosMaxEnPantalla = 2;
	private int contadorTiempo = 0;
	private int kills = 0;

	Juego()
	{
		this.inicializarEntorno();
		this.inicializarVariables();
		entorno.iniciar();
		
	}
	
	private void inicializarEntorno() {
		this.entorno = new Entorno(this, "Castlevania, Barbarianna Viking Edition", 800, 600);
	}
	
	private void inicializarVariables() {
		this.enemigos = new ArrayList<Enemigo>();
		this.disparos = new ArrayList<Rayo>();
		this.rayosEnemigo = new ArrayList <Rayo>();
		this.consumiblesEnMapa = new ArrayList <Consumible>();
		this.vidasDelPersonaje = new ArrayList<Consumible>();
		this.escenario = new Escenario(entorno);
		this.escenario.generarMargenes();
		this.item = new Commodore(700, 50, this.entorno);
		this.personaje = new Personaje(50, 450, 40, 70, 2, this.entorno, this.escenario);
	}
	
	private void generarEnemigos() {
		if(this.enemigos.isEmpty() && !personaje.estaCercaDe(item)) {	
			this.enemigos.add(new Enemigo(700, 65, 540, 70, this.entorno));
		}
		if (!personaje.estaCercaDe(item)){
			Enemigo ultimoEnemigo = this.enemigos.get(this.enemigos.size()-1);
			if(ultimoEnemigo.estaBajando() && ultimoEnemigo.tocaPiso(this.escenario.pisos)) {
				this.enemigos.add(new Enemigo(700, 65, 540, 70, this.entorno));
				cambiarMovimientoEnemigo(ultimoEnemigo);
			}
		}
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{			
		if(this.personaje.estaVivo()) {
			generarEnemigos();
			dibujarJuego();	
			actualizarEstados();
			procesarEventos();
			chequeoColisiones();
			ganarJuego();
		}
		else {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/game-over.jpg"), 400, 300, 0);
			if (this.entorno.sePresiono(this.entorno.TECLA_DELETE)) {
				this.cerrarJuego();
				}
			if(this.entorno.sePresiono(this.entorno.TECLA_ENTER)) {
				this.inicializarVariables();
			}
		}
	}
	
	private void cerrarJuego() {
		this.entorno.setDefaultCloseOperation(Entorno.EXIT_ON_CLOSE);
		System.exit(0);
	}
	
	private void chequeoColisiones() {
		colisionDeEnemigo();
		colisionDePersonaje();
		colisionDeConsumible();
	}

	private void colisionDeConsumible() {
		LinkedList<Consumible> consumibleAEliminar = new LinkedList<Consumible>();
		for (Consumible corazones : this.consumiblesEnMapa) {
			if (corazones.hayColision(this.personaje.getCuerpo())) {
				consumibleAEliminar.add(corazones);
				this.personaje.setVida(this.personaje.getVida()+1);
			}
		}
		
		for (Consumible corazones : consumibleAEliminar) {
			this.consumiblesEnMapa.remove(corazones);
		}
		
	}

	private void procesarEventos() {
		disparar();
		ataqueEnemigo();
	}

	private void actualizarEstados() {
		movimientoDisparos();
		this.personaje.mover();						
		moverEnemigos();

	}
	private void movimientoDisparos() {
		for (int i = 0; i < this.disparos.size(); i++) {
			Rayo rayo = this.disparos.get(i);
			if (rayo.isVisible()) 
				rayo.mover();
			else {
				this.disparos.remove(i);
			}
		}
		
		for (int i = 0; i < this.rayosEnemigo.size(); i++) {
			Rayo rayo = this.rayosEnemigo.get(i);
			if (rayo.isVisible()) 
				rayo.mover();
			else {
				this.rayosEnemigo.remove(i);
			}
		}
	}

	private void disparar() {
		if (this.disparos.size() <= disparosMaxEnPantalla) {
			if (this.entorno.sePresiono('F')) {
				dispararRayo();		
			}			
		}
	}
	
	private void dibujarEnemigos() {
		for(Enemigo enemigo: this.enemigos) {
			enemigo.dibujarEnemigos();
		}
	}

	public void colisionDeEnemigo() {
		for(int i = 0; i< this.enemigos.size() && i >= 0; i++) {
			for(int j= 0; j< this.disparos.size() && j >=0 && i>=0; j++) {
				if(this.enemigos.get(i).colisionaCon(this.disparos.get(j))) {
					this.enemigos.get(i).perderVida();
					this.disparos.remove(j);
					j--;
				}
				if(this.enemigos.get(i).getVida() == 0) {
					this.enemigos.remove(i);
					i--;
					this.kills++;
				}
			}
		}
	}

	
	public void colisionDePersonaje() {
		for(int i= 0; i< this.rayosEnemigo.size(); i++) {
			if(this.personaje.hayColision(this.rayosEnemigo.get(i).getRayo()) && this.personaje.estaVivo()) {
				this.rayosEnemigo.remove(i);
				this.personaje.perderVida();
			}
		}
		for (Enemigo enemigos : this.enemigos) {
			if(this.personaje.hayColision(enemigos.cuerpo) && this.personaje.estaVivo()) {
				this.personaje.morir();
			}
		}
	}
	
	private void ataqueEnemigo(){
		for(Enemigo enemigo: this.enemigos) {
			if (enemigo.estaCercaDe(this.personaje)) {
				contadorTiempo ++;
				if (this.rayosEnemigo.size() <= disparosMaxEnPantalla && contadorTiempo == 100) {
					this.rayosEnemigo.add(enemigo.disparar(this.entorno));					
				}
				if (contadorTiempo == 100){
					contadorTiempo = 0;
				}
			}
		}
	}
	
	private void moverEnemigos() {
		for(int i = 0; i < this.enemigos.size(); i++) {
			Enemigo enemigo = this.enemigos.get(i);
			enemigo.mover();
			boolean estaBajando = enemigo.estaBajando();
			boolean tocaPiso = enemigo.tocaPiso(this.escenario.pisos);
			if(estaBajando && !tocaPiso) {
				this.enemigos.get(i).bajar();
			}
			if(i != this.enemigos.size() -1 && estaBajando && tocaPiso) {
				cambiarMovimientoEnemigo(enemigo);
			}
		}
	}
	
	private void cambiarMovimientoEnemigo(Enemigo enemigo) {
		enemigo.invertirDireccion();
		enemigo.setBajando(false);
	}
	
	private void dibujarJuego() {
		if (this.vidasDelPersonaje.size() < this.personaje.getVida()) {
			this.personaje.generarCorazones();
			this.vidasDelPersonaje = this.personaje.getCorazonesDeVida();
		}
		dibujarVidas();			
		dibujarMapeado();
		dibujarEnemigos();
		dibujarConsumibles();
		dibujarRayos();
		this.item.Dibujar();
		if (this.consumiblesEnMapa.size() < powerUpsMaxEnPantalla) {
			this.escenario.generarConsumibles();
			this.consumiblesEnMapa = this.escenario.getConsumibles();
		}
	}
	
	private void dibujarMapeado() {
		this.escenario.generarNiveles();
		this.escenario.marcadorNivel();
		this.personaje.dibujarVidas();
		this.escenario.dibujarEscenario();
		entorno.cambiarFont("Arial", 20, Color.yellow);
		entorno.escribirTexto("Kills: " + this.kills, 600, 550);
	}
	
	private void dibujarRayos() {
		
		for (int i = 0; i < this.disparos.size(); i++) {
			Rayo rayo = this.disparos.get(i);
			rayo.dibujarDisparoPersonaje();
		}	
		
		for (int i = 0; i < this.rayosEnemigo.size(); i++) {
			Rayo rayo = this.rayosEnemigo.get(i);
			rayo.dibujarDisparoEnemigo();
		}	
	}
	
	private void dibujarConsumibles() {
		for(Consumible corazones: this.escenario.getConsumibles()) {
			corazones.dibujarConsumible();
		}
	}
	
	private void dibujarVidas() {
		this.personaje.dibujarVidas();
	}

	private void dispararRayo() {
		this.disparos.add(this.personaje.disparar());
	}
	
	private void ganarJuego() {
		if(this.personaje.hayColision(this.item.getCommodore())) {
			this.entorno.dibujarImagen(Herramientas.cargarImagen("juego/gameoverwin.jpg"), 400, 300, 0);
			this.personaje.setStop();
			entorno.cambiarFont("Arial", 20, Color.yellow);
			entorno.escribirTexto("Kills Totales: " + this.kills, 50, 550);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
