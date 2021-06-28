package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	private Graph<Player,DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Player> idMap;
	private List<Player> dreamTeam;
	private double bestGrado;

	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new TreeMap<Integer,Player>();
		dao.listAllPlayers(idMap);
	}

	public void creaGrafo(double soglia) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(idMap, soglia));

		for(Adiacenza a: dao.getAdiacenze(idMap, soglia)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
		}
	}

	public int getNVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}

	public Player getTopPlayer() {
		Player best = null;
		int battuti=0;
		for(Player p:this.grafo.vertexSet()) {
			int provvisorio = this.grafo.outgoingEdgesOf(p).size();
			if(provvisorio>battuti) {
				battuti=provvisorio;
				best = p;
			}
		}
		return best;
	} 
	public List<Adiacenza> getSconfitti(Player best){	
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(best)) {
			result.add(new Adiacenza(best, Graphs.getOppositeVertex(this.grafo, e, best),this.grafo.getEdgeWeight(e)));
		}
		return result;
	}
	
	public List<Player> trovaDreamTeam(int k){
		bestGrado=0.0;
		this.dreamTeam=null;
		List<Player> parziale = new ArrayList<>();
		cerca(parziale, new ArrayList<Player>(this.grafo.vertexSet()), k);
		return dreamTeam;
	}

	private void cerca(List<Player> parziale, List<Player> giocatoriDisponibili, int k) {
		if(parziale.size()==k) {
			if(this.getGradoSquadra(parziale)>this.getGradoSquadra(dreamTeam)) {
				this.bestGrado=this.getGradoSquadra(parziale);
				this.dreamTeam = new ArrayList<Player>(parziale);
				return;
			}
		}
		for(Player p : giocatoriDisponibili) {
			if(!parziale.contains(p)) {
				parziale.add(p);
				List<Player> doppione = new ArrayList<Player>(giocatoriDisponibili);
				doppione.removeAll(Graphs.successorListOf(this.grafo, p));
				cerca(parziale,doppione,k);
				parziale.remove(p);
			}
		}
		
	}
	public double getGradoSquadra(List<Player> squadra) {
		double grado =0.0;		
		for(Player p : squadra) {
			double in =0.0;
			double out = 0.0;
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				in += this.grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				out += this.grafo.getEdgeWeight(e);
			}
			grado += out-in;
			
		}
		return grado;
		
	}


}
