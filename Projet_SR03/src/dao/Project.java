package dao;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;

import dto.AchatsObject;
import dto.InfoJeuObject;
import dto.RechercheJeuObject;
import dto.TopVentesObject;

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
					+ " AND j.nom = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, jeu);
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
	
	public ArrayList<RechercheJeuObject> GetRechercheJeu(Connection connection, String jeu) throws Exception {
		ArrayList<RechercheJeuObject> feedData = new ArrayList<RechercheJeuObject>();
		try {
			String query;
			query = "SELECT nom, description "
					+ "FROM jeux "
					+ "WHERE nom LIKE ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, '%'+jeu+'%');
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				RechercheJeuObject feedObject = new RechercheJeuObject();
				feedObject.setNom(rs.getString("nom"));
				feedObject.setDescription(rs.getString("description"));
				feedData.add(feedObject);
			}
		}
		catch(Exception e) {
			throw e;
		}
		return feedData;
	}
	
	public ArrayList<TopVentesObject> GetTopVentes(Connection connection) throws Exception {
		ArrayList<TopVentesObject> feedData = new ArrayList<TopVentesObject>();
		try {
			String query;
			query = "SELECT jeu, COUNT(*) AS nb_ventes "
					+ "FROM achats GROUP BY jeu "
					+ "ORDER BY nb_ventes DESC "
					+ "LIMIT 1";
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				TopVentesObject feedObject = new TopVentesObject();
				feedObject.setJeu(rs.getString("jeu"));
				feedObject.setNb_ventes(rs.getInt("nb_ventes"));
				feedData.add(feedObject);
			}

		}
		catch(Exception e) {
			throw e;
		}
		return feedData;
	}
	
	public Boolean PostCreerCompte(
			Connection connection,
			String login, 
			String mdp,
			String nom,
			String prenom,
			String datenaissance,
			String rue,
			Integer cp,
			String ville,
			String mail) 
					throws Exception {
		Boolean success = false;
		try {
			String query;
			query = "INSERT INTO adherent "
					+ "(login, mdp, nom, prenom, datenaissance, rue, cp, ville, mail) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, mdp);
			ps.setString(3, nom);
			ps.setString(4, prenom);
			ps.setDate(5, java.sql.Date.valueOf(datenaissance));
			ps.setString(6, rue);
			ps.setInt(7, cp);
			ps.setString(8, ville);
			ps.setString(9, mail);
			ps.executeUpdate();
			success = true;
		}
		catch(Exception e) {
			throw e;
		}
		return success;
	}
	
	public void CheckUserInfo(
			Connection connection,
			String login, 
			String mdp) 
					throws Exception {
		try {
			String query;
			query = "SELECT * "
					+ "FROM adherent "
					+ "WHERE login = ? "
					+ "AND mdp = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, login);
			ps.setString(2, mdp);
			ps.executeQuery();
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				// No such user found, we raise an exception
				IOException e = new IOException();
				throw e;
			}
		}
		catch(Exception e) {
			throw e;
		}
		return;
	}
	
	public String GenerateToken(
			Connection connection,
			String login, 
			String mdp) 
					throws Exception {
		String token = "";
		try {
			// Generation of the secure token
			Random random = new SecureRandom();
			token = new BigInteger(130, random).toString(32);
			
			// Update the database with the new token
			String query;
			query = "UPDATE adherent "
					+ "SET token = ? "
					+ "WHERE login = ? "
					+ "AND mdp = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, token);
			ps.setString(2, login);
			ps.setString(3, mdp);
			ps.executeUpdate();
		}
		catch(Exception e) {
			throw e;
		}
		return token;
	}
}
