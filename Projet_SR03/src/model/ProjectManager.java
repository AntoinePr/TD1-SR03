package model;

import java.sql.Connection;
import java.util.ArrayList;

import dao.ConnexionBDD;
import dao.Project;

import dto.AchatsObject;
import dto.InfoJeuObject;
import dto.RechercheJeuObject;
import dto.TopVentesObject;

public class ProjectManager {
	
	public ArrayList<AchatsObject> GetAchats() throws Exception {
		ArrayList<AchatsObject> feeds = null;
		try {
			Connection connection =  ConnexionBDD.getInstance().getCnx();
			Project project= new Project();
			feeds=project.GetAchats(connection);
		}
		catch (Exception e){
			throw e;
		}
		return feeds;
	}
	
	public ArrayList<InfoJeuObject> GetInfoJeu(String jeu) throws Exception {
		ArrayList<InfoJeuObject> feeds = null;
		try {
			Connection connection =  ConnexionBDD.getInstance().getCnx();
			Project project= new Project();
			feeds=project.GetInfoJeu(connection, jeu);
		}
		catch (Exception e){
			throw e;
		}
		return feeds;
	}
	
	public ArrayList<RechercheJeuObject> GetRechercheJeu(String jeu) throws Exception {
		ArrayList<RechercheJeuObject> feeds = null;
		try {
			Connection connection =  ConnexionBDD.getInstance().getCnx();
			Project project= new Project();
			feeds=project.GetRechercheJeu(connection, jeu);
		}
		catch (Exception e){
			throw e;
		}
		return feeds;
	}
	
	public ArrayList<TopVentesObject> GetTopVentes() throws Exception {
		ArrayList<TopVentesObject> feeds = null;
		try {
			Connection connection =  ConnexionBDD.getInstance().getCnx();
			Project project= new Project();
			feeds=project.GetTopVentes(connection);
		}
		catch (Exception e){
			throw e;
		}
		return feeds;
	}
}
