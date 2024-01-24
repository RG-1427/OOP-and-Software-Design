//Player Class
package com.company;
import static com.company.Main.database;

public class Player
{
    //Creating variables needed for a player
    private String name;
    private String password;
    private int curPos;
    private int totalGamesPlayed;
    private int gamesWon;
    private double winRate;

    public Player(String name, String password, boolean isLoggingIn)
    {
        //If the player is logging in, gather their data, else add them to the database
        this.name = name;
        this.password = password;
        if(isLoggingIn == false)
        {
            database.CreateNewPlayer(name, password);
        }
        else
        {
            double[] playerData = database.LoadPlayerData(name, password);
            this.totalGamesPlayed = (int)playerData[0];
            this.gamesWon = (int)playerData[1];
            this.winRate = playerData[2];
        }
    }

    //Player constructor for AI
    public Player()
    {
        name = "AI";
    }

    //Get a player's name
    public String GetName()
    {
        return name;
    }

    //Set a player's name
    public void SetName(String name)
    {
        this.name = name;
    }

    //Get a player's password
    public String GetPassword()
    {
        return password;
    }

    //Set a player's password
    public void SetPassword(String password)
    {
        this.password = password;
    }

    //Get a player's position
    public int GetCurPos()
    {
        return curPos;
    }

    //Set a player's position
    public void SetCurPos(int newPos)
    {
        curPos = newPos;
    }

    //Get the amount of games a player has played
    public int GetTotalGamesPlayed()
    {
        return totalGamesPlayed;
    }

    //Add a game to the player's total game count
    public void FinishedGame()
    {
        this.totalGamesPlayed = totalGamesPlayed + 1;
    }

    //Get the amount of games a player won
    public int GetGamesWon()
    {
        return gamesWon;
    }

    //Add a win to the player's total
    public void AddGamesWon()
    {
        this.gamesWon = gamesWon + 1;
    }

    //Get the player's win rate
    public double GetWinRate()
    {
        return winRate;
    }

    //Calculate the player's win rate
    public void SetWinRate()
    {
        this.winRate = (double)gamesWon / (double)totalGamesPlayed * 100.0;
    }



}
