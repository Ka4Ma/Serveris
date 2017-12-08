package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Main {
    public static final String HOST = "192.168.0.62";
    public static final int PORT = 7000;

    public static void main(String[] args) {
// write your code here
        try {
            ServerSocket jungtis = new ServerSocket(PORT);
            boolean testi = true;//
            Socket kanalas = null;
            ObjectInputStream ivedimas = null;
            ObjectOutputStream isvedimas = null;
            kanalas = jungtis.accept(); // cia kodas sustos ir lauks prisijungimo
            System.out.println("Prisijunge: " + kanalas.getInetAddress());
            isvedimas = new ObjectOutputStream(kanalas.getOutputStream());// output stream visada kurti pirma
            ivedimas = new ObjectInputStream(kanalas.getInputStream());
            while (testi) {
                String veiksmas = "";
                try {
                    if (ivedimas.available() > 0) {
                        veiksmas = ivedimas.readUTF();
                    }
                    if (veiksmas == null) {
                        kanalas.close();
                        veiksmas = "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                switch (veiksmas) {
                    case "pabaiga":
                        System.out.println("Gautas nurodymas baigti darba");
                        testi = false;
//isvedimas.writeUTF("pabaiga");
//isvedimas.flush();
                        kanalas.close();
                        jungtis.close();
                        break;
                    case "":
                        break;
                    case "studentai":
                        parodykStudentus(isvedimas);
                        break;
                    case "adresas":
                        StudentoAdresas(isvedimas);
                    default:
                        System.out.println(veiksmas);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void parodykStudentus(ObjectOutputStream isvedimas) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String HOST = "jdbc:mysql://localhost:3306/mts";
            final String USER = "root";
            final String PASSWORD = "";
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `students`;";
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String eilute = "";
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    eilute += resultSet.getString(i) + "\t";
                }
                eilute += "\n";
            }
            isvedimas.writeUTF(eilute);
            isvedimas.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void StudentoAdresas(ObjectOutputStream isvedimas){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String HOST = "jdbc:mysql://localhost:3306/mts";
            final String USER = "root";
            final String PASSWORD = "";
            Connection connection = DriverManager.getConnection(HOST, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM `student_address`;";
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            String eilute = "";
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    eilute += resultSet.getString(i) + "\t";
                }
                eilute += "\n";
            }
            isvedimas.writeUTF(eilute);
            isvedimas.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}