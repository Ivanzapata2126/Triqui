/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.universidad.triqui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author USUARIO
 */
public class Connection_DB {
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    public String[] nombres = new String[20];
    public String[] nombresRanking = new String[20];
    public String[] victorias = new String[20];
    public String[] derrotas = new String[20];
    public String[] empates = new String[20];
    public String[] puntaje = new String[20];
    public String[] partidas = new String[20];
    private final String usuario = "root";
    private final  String clave = "";
    private final String url = "jdbc:mysql://localhost:3306/triqui";
    
    public void insertarNombre(String nombre){
        try {
            con = DriverManager.getConnection(this.url,this.usuario,this.clave);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM jugadores WHERE nombre_jugador ='"+nombre+"'");
            if(!rs.next()){
                stmt.executeUpdate("INSERT INTO jugadores VALUES(null,'"+nombre+"',0,0,0,0,0)");
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Ocurrió un error!");
        }
    }
    public static void conectar(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void descargar(){
        try {
            con = DriverManager.getConnection(this.url,this.usuario,this.clave);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT id_jugador,nombre_jugador FROM jugadores");
            rs.next();
            int contador = 0;
            do{
                nombres[contador] = rs.getString("nombre_jugador");
                contador++;
            }while(rs.next());
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Ocurrió un error!");
        }
    } 
    
    public void subirResultados(String nombre,int victorias,int derrotas,int empates){
        try {
            con = DriverManager.getConnection(this.url,this.usuario,this.clave);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM jugadores WHERE nombre_jugador ='"+nombre+"'");
            rs.next();
            int id_jugador = Integer.parseInt(rs.getString("id_jugador"));
            float partidas_db = Integer.parseInt(rs.getString("partidas_jugador"));
            float victorias_db = Integer.parseInt(rs.getString("victorias_jugador"));
            int derrotas_db = Integer.parseInt(rs.getString("derrotas_jugador"));
            int empates_db = Integer.parseInt(rs.getString("empates_jugador"));
            
            partidas_db++;
            derrotas_db+=derrotas;
            victorias_db+=victorias;
            empates_db+=empates;
            int puntaje_db = (int)((victorias_db / partidas_db)*100000);            
            stmt.executeUpdate("UPDATE jugadores SET partidas_jugador = '"+partidas_db+"',victorias_jugador = '"+victorias_db+"',derrotas_jugador = '"+derrotas_db+"',empates_jugador = '"+empates_db+"',puntaje_jugador = '"+puntaje_db+"' WHERE jugadores.id_jugador = '"+id_jugador+"'");
            
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Ocurrió un error!");
        }
    }
    
    public void Ranking(){
        try {
            con = DriverManager.getConnection(this.url,this.usuario,this.clave);
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT nombre_jugador,victorias_jugador,derrotas_jugador,empates_jugador,partidas_jugador,puntaje_jugador, RANK() OVER(ORDER BY puntaje_jugador DESC) as 'Posicion' FROM `jugadores`");
            rs.next();
            int contador = 0;
            do{
                nombresRanking[contador] = rs.getString("nombre_jugador");
                victorias[contador] = rs.getString("victorias_jugador");
                derrotas[contador] = rs.getString("derrotas_jugador");
                empates[contador] = rs.getString("empates_jugador");
                partidas[contador] = rs.getString("partidas_jugador");
                puntaje[contador] = rs.getString("puntaje_jugador");
                contador++;
            }while(rs.next());
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,"Ocurrió un error!");
        }
    }
}
