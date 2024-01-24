//Database class
package com.company;
import java.nio.file.*;
import java.sql.*;

public class Database
{
    //Creating variables needed for a database
    private Connection conn = null;
    private String url;
    private String path;

    public Database()
    {

        //Finding the path to the database and creating a connection
        try
        {
            Path currentRelativePath = Paths.get("");
            path = currentRelativePath.toAbsolutePath().toString();
            url = "jdbc:sqlite:" + path + "\\sqliteDB\\SnakesAndLadders.db";
            conn = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //Closing the connection after it is opened
        finally
        {
            try
            {
                conn.close();
            }
            catch (SQLException ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

    //Creating a new player function
    public void CreateNewPlayer(String name, String password)
    {
        //Establishing a database connection
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Adding the player information into the database
                PreparedStatement sqlCommand = conn.prepareStatement("INSERT INTO player_tbl " +
                        "(id, name, password, games_played, games_won, win_rate) VALUES (?, ?, ?, ?, ?, ?);");
                sqlCommand.setString(2, name);
                sqlCommand.setString(3, password);
                sqlCommand.setInt(4, 0);
                sqlCommand.setInt(5, 0);
                sqlCommand.setDouble(6, 0.0);
                sqlCommand.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Closing connection after adding the player
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    //Updating player information function
    public void UpdatePlayerInfo(String name, String password, String newName, String newPassword)
    {
        //Establishing connection to the database
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Updating the player's information based on the data given
                PreparedStatement sqlCommand = conn.prepareStatement("UPDATE player_tbl SET name = ?, password = ?" +
                        " WHERE name = ? AND password = ?;");
                sqlCommand.setString(1, newName);
                sqlCommand.setString(2, newPassword);
                sqlCommand.setString(3, name);
                sqlCommand.setString(4, password);
                sqlCommand.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Closing database connection
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    //Updating player statistics function
    public void UpdatePlayerStats(String name, int totalGames, int gamesWon, double winRate)
    {
        //Establishing connection to the database
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Updating the player statistics based on the data given
                PreparedStatement sqlCommand = conn.prepareStatement("UPDATE player_tbl SET games_played = ?, " +
                                                    "games_won = ?, win_rate = ? WHERE name = ?;");
                sqlCommand.setInt(1, totalGames);
                sqlCommand.setInt(2, gamesWon);
                sqlCommand.setDouble(3, winRate);
                sqlCommand.setString(4, name);
                sqlCommand.executeUpdate();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Closing connection
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    //Searching database function
    public boolean SearchDatabase(String name, String password) {
        //Establishing database connection
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Sort rows using the information given
                PreparedStatement sqlCommand = conn.prepareStatement("SELECT * FROM player_tbl WHERE " +
                        "name = ? and password = ?;");
                sqlCommand.setString(1, name);
                sqlCommand.setString(2, password);
                ResultSet result = sqlCommand.executeQuery();

                //If there is something there, the player is found
                while (result.next() == true)
                {
                    return true;
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Closing connection
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    //Loading player data function
    public double[] LoadPlayerData(String name, String password)
    {
        //Storing player data to return
        double[] data = new double[3];

        //Establishing datbase connection
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Search for a player based on their username and password
                PreparedStatement sqlCommand = conn.prepareStatement("SELECT * FROM player_tbl WHERE " +
                        "name = ? and password = ?;");
                sqlCommand.setString(1, name);
                sqlCommand.setString(2, password);
                ResultSet result = sqlCommand.executeQuery();

                //Collect their data
                data[0] = ((double)result.getInt(result.findColumn("games_played")));
                data[1] = (double)result.getInt(result.findColumn("games_won"));
                data[2] = (double)result.getDouble(result.findColumn("win_rate"));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Close database connection
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        //Return player data
        return data;
    }

    //Update leaderboard function
    public void UpdateLeaderboard()
    {
        //Establishing database connection
        try
        {
            conn = DriverManager.getConnection(url);

            try
            {
                //Order the rows by win rate
                PreparedStatement sqlCommand = conn.prepareStatement("SELECT name, win_rate FROM player_tbl " +
                        "ORDER BY win_rate DESC;");
                ResultSet result = sqlCommand.executeQuery();

                //If there are no players display that otherwise start the leaderboard
                if(result.next() == false)
                {
                    System.out.println("There is no current Leaderboard");
                }
                else
                {
                    System.out.println("\nLeaderboard");
                    System.out.print("name: " + result.getString("name"));
                    System.out.print(", win rate: " + result.getDouble("win_rate") + "%\n");
                }

                //Continue displaying the leaderboard
                while(result.next() == true)
                {
                        System.out.print("Name: " + result.getString("name"));
                        System.out.print(", Win Rate: " + result.getDouble("win_rate") + "%\n");
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            //Closing connection
            finally
            {
                try
                {
                    conn.close();
                }
                catch (SQLException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}

