package Controler;

import Model.Compte;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.time.LocalTime.now;

public class DeposRetreive {

    private Compte compte;
    ConnexionBD connexionBD =new ConnexionBD();




    public boolean login(String username, String password) throws SQLException {
        Statement stmt = connexionBD.con.createStatement();
        String query=" SELECT * FROM client " ;
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            if(rs.getString("login").equals(username)  && rs.getString("password").equals(password)){
                return true;
            }


        }
      return false;
    }
    public void profil(String username, String Password) throws SQLException {
        Statement stmt = connexionBD.con.createStatement();
        String query=" SELECT * FROM client JOIN compte ON (client.id_compte = compte.id) WHERE (login like '" +username+"' AND password like '"+Password+ "')";
        ResultSet rs = stmt.executeQuery(query);
        if(rs.next()){
        System.out.println("Voici les informations de votre compte");
        System.out.println("\t NOM :"+rs.getString("Nom"));
        System.out.println("\t PRENOM :"+rs.getString("Prenom"));
        System.out.println("\t CIN :"+rs.getString("CIN"));
        System.out.println("\t Email :"+rs.getString("Email"));
        System.out.println("\t Numero de compte :"+rs.getString("numero"));
        System.out.println("\t Solde :"+rs.getString("solde")+" DH");


        }

    }

    public boolean deposer(String username, String Password,double soldeDeposer) throws SQLException {
        Statement stmt = connexionBD.con.createStatement();
        String query=" SELECT * FROM client JOIN compte ON (client.id_compte = compte.id) WHERE (login like '" +username+"' AND password like '"+Password+ "')";
        ResultSet rs = stmt.executeQuery(query);
        Long num= Long.valueOf(0);
        Double newSolde = 0.0;
        String type ="veirement" ;
        if(rs.next()){
            num =Long.parseLong(rs.getString("numero"));
           newSolde = Double.parseDouble(rs.getString("solde")) + soldeDeposer ;
           String query3="INSERT INTO operation (id,type,montant) VALUES ('"+rs.getString("id")+"','"+type+"','"+soldeDeposer+"')";
           stmt.executeUpdate(query3);
        }
        String query2 ="UPDATE  compte  SET solde ='"+newSolde+"' WHERE numero ='"+num+"'" ;
        int nbUpdated = stmt.executeUpdate(query2);
        return nbUpdated>0;
    }
    public boolean retriever(String username, String Password,double soldeRetriever) throws SQLException {
        Statement stmt = connexionBD.con.createStatement();
        String query=" SELECT * FROM client JOIN compte ON (client.id_compte = compte.id) WHERE (login like '" +username+"' AND password like '"+Password+ "')";
        ResultSet rs = stmt.executeQuery(query);
        Long num= Long.valueOf(0);
        Double newSolde = 0.0;
        int nbUpdated =0;
        if(rs.next()){
            num =Long.parseLong(rs.getString("numero"));
            if( Double.parseDouble(rs.getString("solde")) >= soldeRetriever){
                newSolde = Double.parseDouble(rs.getString("solde")) - soldeRetriever ;
                String query2 ="UPDATE  compte  SET solde ='"+newSolde+"' WHERE numero ='"+num+"'" ;
                 nbUpdated = stmt.executeUpdate(query2);
            }

        }

        return nbUpdated>0;


    }




}
