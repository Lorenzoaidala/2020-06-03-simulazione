package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {

	public void listAllPlayers(Map<Integer,Player> idMap){
		String sql = "SELECT * FROM Players";

		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("PlayerID"))) {
					Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));

					idMap.put(res.getInt("PlayerID"), player);
				}
			}
			conn.close();


		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));

				result.add(action);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Player> getVertici(Map<Integer,Player>idMap,double soglia){
		String sql ="SELECT PlayerID "
				+ "FROM actions "
				+ "GROUP BY PlayerID "
				+ "HAVING AVG (Goals)>?";
		List<Player> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, soglia);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(idMap.containsKey(res.getInt("PlayerID")))
					result.add(idMap.get(res.getInt("PlayerID")));
			}
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public List<Adiacenza> getAdiacenze(Map<Integer,Player>idMap,double soglia){
		String sql ="SELECT a1.PlayerID AS id1,a2.PlayerID AS id2, SUM(a1.TimePlayed)-SUM(a2.TimePlayed) AS peso "
				+ "FROM actions a1, actions a2 "
				+ "WHERE a1.MatchID=a2.MatchID "
				+ "AND a1.`Starts`=a2.`Starts` AND a1.`Starts`=1 "
				+ "AND a1.PlayerID<>a2.PlayerID "
				+ "AND a1.TeamID<>a2.TeamID "
				+ "GROUP BY a1.PlayerID,a2.PlayerID "
				+ "HAVING peso>0";
		List<Adiacenza> result = new ArrayList<>();
		List<Player> giocatoriValidi = this.getVertici(idMap, soglia);
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(giocatoriValidi.contains(idMap.get(res.getInt("id1")))&&giocatoriValidi.contains(idMap.get(res.getInt("id2")))) {
					result.add(new Adiacenza(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), res.getDouble("peso")));
				}
			}
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}
