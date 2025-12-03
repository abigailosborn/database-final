import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
            
            Scanner scan = new Scanner(System.in);
            boolean running = true;

            while(running){
                
                System.out.println("Tree Database Menu");
                System.out.println("1. Find tree by ID");
                System.out.println("2. Find trees by tree symbol");
                System.out.println("3. Find trees by scientific name");
                System.out.println("4. Find trees by common name");
                System.out.println("5. View all 1478 rows of data");
                System.out.println();
            	System.out.println("Enter a number or q to quit:");
                String input = scan.nextLine().toLowerCase();
                
                if(!input.equals("q") && !input.equals("quit")){
                    switch(input) {
                    	case "1":
                    	case "one":
                    		findByID(scan, statement);
                    		break;
                    	case "2":
                    	case "two":
                    		findBySymbol(scan, statement);
                    		break;
                    	case "3":
                    	case "three":
                    		findByScientific(scan, statement);
                    		break;
                    	case "4":
                    	case "four":
                    		findByCommon(scan, statement);
                    		break;
                    	case "5":
                    	case "five":
                    		viewAll(statement);
                    		break;
                    	default:
                    		System.out.println("Input not found. Try again please <3");
                    }
                }
                else {
                    running = false;
                }
            }
            
            System.out.println();
            System.out.println("Quitting... Make like a tree and get out of here");
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
    
    public static void findByID(Scanner scan, Statement statement) throws SQLException {
    	boolean finished = false;
		int id = 1;

    	while(!finished) {
    		System.out.println("Enter an ID number 1 to 1478:");
        	try {
        		String input = scan.nextLine();
        		id = Integer.parseInt(input);
        		
        		if(id < 1 || id > 1478) {
        			throw new Exception("ID out of range :(");
        		}
        		else {
        			finished = true;
        		}
        	}
            catch (NumberFormatException e){
                System.out.println("ID must be an integer </3");
            }
        	catch (Exception f) {
        		System.out.println(f.getMessage());
        	}
    	}
    	
    	ResultSet rs = statement.executeQuery("select * from purgatory where treeid = " + id);
        
    	System.out.println("Tree " + id + " info:");
		System.out.println("Symbol: " + rs.getString("symbol") 
		+ " Status: " + rs.getString("status") + " DBH: " + rs.getString("dbh"));
		
    	String status = rs.getString("status");
    	
    	if(status.equalsIgnoreCase("L")) {
    		ResultSet life = statement.executeQuery("select * from life where treeid = " + id);
    		System.out.println("Radial Growth: " + life.getString("radialgrowth") 
    		+ " Age: " + life.getString("age") + " Height: " + life.getString("height")
    		+ " Crown Coverage: " + life.getString("crowncoverage"));
    	}
    	else {
    		ResultSet death = statement.executeQuery("select * from death where treeid = " + id);
    		
    		System.out.println("Snag Decay: " + death.getString("snagdecay"));
    	}
    	
    	System.out.println();
    }
    
    public static void findBySymbol(Scanner scan, Statement statement) throws SQLException {
    	boolean finished = false;
		String symbol = "";

    	while(!finished) {
    		System.out.println("Enter a symbol: ");
    		symbol = scan.nextLine().toUpperCase();

    		switch(symbol) {
    			case"PIPO":
    			case"PSME":
    			case"LAOC":
    			case"ABGR":
    			case"THPL":
    			case"TSHE":
    			case"PIMO3":
    			case"PICO":
    			case"ABLA":
    				finished = true;
    				break;
    			default:
    				System.out.println("Invalid symbol :(");
    		}
    	}
    	
    	ResultSet sciQuery = statement.executeQuery("select scientificname from treescientific where symbol = '" + symbol + "'");
    	String scientific = sciQuery.getString("scientificname");
    	
    	ResultSet commonQuery = statement.executeQuery("select commonname from treecommon where symbol = '" + symbol + "'");
    	String common = commonQuery.getString("commonname");
    	
    	ResultSet rs = statement.executeQuery("select * from purgatory where symbol = '" + symbol + "'");
    	
    	System.out.println(scientific + " (scientific) " + common + " (common) " + symbol + " Trees:");
    	
    	while(rs.next()) {
            System.out.println("ID: " + rs.getString("treeid") + " Symbol: " 
            	+ rs.getString("symbol") + " Status: " + rs.getString("status") 
            	+ " DBH: " + rs.getString("dbh"));
        }
    	System.out.println();
    }
    
    public static void findByScientific(Scanner scan, Statement statement) throws SQLException {
    	boolean finished = false;
		String scientific = "";

    	while(!finished) {
    		System.out.println("Enter a scientific name: ");
    		scientific = scan.nextLine().toLowerCase();

    		switch(scientific) {
    			case"pinus ponderosa":
    				scientific = "Pinus Ponderosa";
    				finished = true;
    				break;
    			case"pseudotsuga menziesii":
    				scientific = "Pseudotsuga Menziesii";
    				finished = true;
    				break;
    			case"larix occidentalis":
    				scientific = "Larix Occidentalis";
    				finished = true;
    				break;
    			case"abies grandis":
    				scientific = "Abies Grandis";
    				finished = true;
    				break;
    			case"thuja plicata":
    				scientific = "Thuja Plicata";
    				finished = true;
    				break;
    			case"tsuga heterophylla":
    				scientific = "Tsuga Heterophylla";
    				finished = true;
    				break;
    			case"pinus monticola":
    				scientific = "Pinus Monticola";
    				finished = true;
    				break;
    			case"pinus contorta":
    				scientific = "Pinus Contorta";
    				finished = true;
    				break;
    			case"abies lasiocarpa":
    				scientific = "Abies Lasiocarpa";
    				finished = true;
    				break;
    			default:
    				System.out.println("Invalid name :(");
    		}
    	}
    	
    	ResultSet symbolQuery = statement.executeQuery("select symbol from treescientific where scientificname = '" + scientific + "'");
    	String symbol = symbolQuery.getString("symbol");
    	
    	
    	ResultSet commonQuery = statement.executeQuery("select commonname from treecommon where symbol = '" + symbol + "'");
    	String common = commonQuery.getString("commonname");
    	    	
    	ResultSet rs = statement.executeQuery("select * from purgatory where symbol = '" + symbol + "'");
    	
    	System.out.println(scientific + " (scientific) " + common + " (common) " + symbol + " Trees:");
    	
    	while(rs.next()) {
            System.out.println("ID: " + rs.getString("treeid") + " Symbol: " 
            	+ rs.getString("symbol") + " Status: " + rs.getString("status") 
            	+ " DBH: " + rs.getString("dbh"));
        }
    	System.out.println();
    }
    
    public static void findByCommon(Scanner scan, Statement statement) throws SQLException {
    	boolean finished = false;
		String common = "";

    	while(!finished) {
    		System.out.println("Enter a common name: ");
    		common = scan.nextLine().toLowerCase();

    		switch(common) {
    			case"ponderosa pine":
    				common = "Ponderosa Pine";
    				finished = true;
    				break;
    			case"douglas-fir":
    			case"douglas fir":
    				common = "Douglas-Fir";
    				finished = true;
    				break;
    			case"western larch":
    				common = "Western Larch";
    				finished = true;
    				break;
    			case"grand fir":
    				common = "Grand Fir";
    				finished = true;
    				break;
    			case"western redcedar":
    				common = "Western Redcedar";
    				finished = true;
    				break;
    			case"western hemlock":
    				common = "Western Hemlock";
    				finished = true;
    				break;
    			case"western white pine":
    				common = "Western White Pine";
    				finished = true;
    				break;
    			case"lodgepole pine":
    				common = "Lodgepole Pine";
    				finished = true;
    				break;
    			case"subalpine fir":
    				common = "Subalpine Fir";
    				finished = true;
    				break;
    			default:
    				System.out.println("Invalid name :(");
    		}
    	}
    	
    	ResultSet commonQuery = statement.executeQuery("select symbol from treecommon where commonname = '" + common + "'");
    	String symbol = commonQuery.getString("symbol");
    	
    	ResultSet symbolQuery = statement.executeQuery("select scientificname from treescientific where symbol = '" + symbol + "'");
    	String scientific = symbolQuery.getString("scientificname");
    	    	
    	ResultSet rs = statement.executeQuery("select * from purgatory where symbol = '" + symbol + "'");
    	
    	System.out.println(scientific + " (scientific) " + common + " (common) " + symbol + " Trees:");
    	
    	while(rs.next()) {
            System.out.println("ID: " + rs.getString("treeid") + " Symbol: " 
            	+ rs.getString("symbol") + " Status: " + rs.getString("status") 
            	+ " DBH: " + rs.getString("dbh"));
        }
    	System.out.println();
    }
    
    public static void viewAll(Statement statement) throws SQLException {
    	ResultSet rs = statement.executeQuery("select * from purgatory");
        while(rs.next()){
            System.out.println("ID: " + rs.getString("treeid") + " Symbol: " 
            	+ rs.getString("symbol") + " Status: " + rs.getString("status") 
            	+ " DBH: " + rs.getString("dbh"));
        }
        System.out.println();
    }
}
