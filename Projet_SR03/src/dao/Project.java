package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import dto.AchatsObject;
import dto.InfoJeuObject;

public class Project {
	
	public ArrayList<AchatsObject> GetAchats(Connection connection) throws Exception {
		ArrayList<AchatsObject> feedData = new ArrayList<AchatsObject>();
		try {
			String query;
			query = "SELECT adh, jeu FROM achats";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				AchatsObject feedObject = new AchatsObject();
				feedObject.setAdh(rs.getString("adh"));
				feedObject.setJeu(rs.getString("jeu"));
				feedData.add(feedObject);
			}
		}
		catch(Exception e) {
			throw e;
		}
		return feedData;
	}
	
	public ArrayList<InfoJeuObject> GetInfoJeu(Connection connection, String jeu) throws Exception {
		ArrayList<InfoJeuObject> feedData = new ArrayList<InfoJeuObject>();
		try {
			String query;
			query = "SELECT j.nom, j.prix, j.description, j.datesortie, e.raisonsociale"
					+ " FROM jeux AS j, editeur AS e"
					+ "	WHERE j.editeur = e.siret"
					+ " AND j.nom = '" + jeu + "'";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				InfoJeuObject feedObject = new InfoJeuObject();
				feedObject.setNom(rs.getString("nom"));
				feedObject.setPrix(rs.getString("prix"));
				feedObject.setDescription(rs.getString("description"));
				feedObject.setDatesortie(rs.getString("datesortie"));
				feedObject.setRaisonsociale(rs.getString("raisonsociale"));
				feedData.add(feedObject);
			}
		}
		catch(Exception e) {
			throw e;
		}
		return feedData;
	}
}