import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) throws Exception {
        Connection connection = null;
        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:trees.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists purgatory");
            statement.executeUpdate("create table purgatory (treeid integer not null, " +
            "symbol text not null, status text not null, dbh text not null, primary key(treeid))");

            statement.executeUpdate("drop table if exists life");
            statement.executeUpdate("create table life (treeid integer not null, " +
            "radialgrowth integer default -1, age integer default -1, height integer not null, " +
            "crowncoverage integer not null, primary key(treeid), foreign key(treeid) references purgatory(treeid))");

            statement.executeUpdate("drop table if exists death");
            statement.executeUpdate("create table death (treeid integer not null, " +
            "snagdecay integer not null, primary key(treeid), foreign key(treeid) references purgatory(treeid))");

            statement.executeUpdate("drop table if exists treescientific");
            statement.executeUpdate("create table treescientific (symbol text not null, " +
            "scientificname text not null, primary key(symbol), foreign key(symbol) references purgatory(symbol))");

            statement.executeUpdate("drop table if exists treecommon");
            statement.executeUpdate("create table treecommon (symbol text not null, " +
            "commonname text not null, primary key(symbol), foreign key(symbol) references purgatory(symbol))");

            insertRows(statement, "purgatory_inserts.txt");
            insertRows(statement, "life_inserts.txt");
            insertRows(statement, "death_inserts.txt");
            insertRows(statement, "tree_common_inserts.txt");
            insertRows(statement, "tree_scientific_inserts.txt");
            
            ResultSet rs = statement.executeQuery("select * from purgatory");
            while(rs.next()) // read the result set
            {
                System.out.println("name = " + rs.getString("treeid"));
            }
        }
        catch(SQLException e)
        {
            // Error message "out of memory", probably means no database file found
            System.err.println(e.getMessage());
        } finally {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e.getMessage()); // connection close failed.
            }
        }

    }

    public static void insertRows(Statement statement, String file) throws Exception{
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while(br.ready()){
                String line = br.readLine();

                statement.executeUpdate(line);
            }

            br.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
