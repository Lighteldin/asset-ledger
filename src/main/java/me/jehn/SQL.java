package me.jehn;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQL {

    public static void createDB(){
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/NALDB", "sa", "");
            Statement stmt = connection.createStatement();
            //
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS NAL(name VARCHAR(40), asset INT, liability INT);");
        }
        catch(Exception exception){exception.printStackTrace();}
    }


    public static void addToDB(String name, int asset, int liability){
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/NALDB", "sa", "");
            Statement stmt = connection.createStatement();
            //
            PreparedStatement ps = connection.prepareStatement("INSERT INTO NAL VALUES(?, ?, ?)");
            ps.setString(1, name);
            ps.setInt(2, asset);
            ps.setInt(3, liability);
            ps.executeUpdate();
        }
        catch(Exception exception){exception.printStackTrace();}
    }


    public static void editDB(String oldAName, int oldAsset, int oldLiability, String newName, int newAsset, int newLiability){
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/NALDB", "sa", "");
            Statement stmt = connection.createStatement();
            //
            PreparedStatement ps = connection.prepareStatement("UPDATE NAL SET name = ?, asset = ?, liability = ? WHERE name = ? AND asset = ? AND liability = ?;");

            ps.setString(1, newName);
            ps.setInt(2, newAsset);
            ps.setInt(3, newLiability);

            ps.setString(4, oldAName);
            ps.setInt(5, oldAsset);
            ps.setInt(6, oldLiability);

            ps.executeUpdate();
        }
        catch(Exception exception){exception.printStackTrace();}
    }


    public static void deleteFromDB(String name, int asset, int liability){
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/NALDB", "sa", "");
            Statement stmt = connection.createStatement();
            //
            PreparedStatement ps = connection.prepareStatement("DELETE FROM NAL WHERE name = ? AND asset = ? AND liability = ?");
            ps.setString(1, name);
            ps.setInt(2, asset);
            ps.setInt(3, liability);
            ps.executeUpdate();
        }
        catch(Exception exception){exception.printStackTrace();}
    }

    public static ObservableList getFromDB(){
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:~/NALDB", "sa", "");
            Statement stmt = connection.createStatement();
            //
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM NAL;");
            ResultSet resultset = ps.executeQuery();


            while (resultset.next()){
                String name = resultset.getString("name");
                int asset = resultset.getInt("asset");
                int liability = resultset.getInt("liability");
                list.add(new User(name, asset, liability));
            }

        }
        catch(Exception exception){
            exception.printStackTrace();
        }
        return list;
    }
}
