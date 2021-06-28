package it.polito.tdp.PremierLeague.model;

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
	
}
