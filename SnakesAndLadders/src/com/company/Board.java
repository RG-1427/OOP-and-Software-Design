//Board class
package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import static com.company.Main.database;

public class Board
{
    //Creating variables needed for the board
    private List<Player> players = new ArrayList<Player>();
    private List<Tile> tiles = new ArrayList<Tile>();
    private List<Obstacle> snakes = new ArrayList<Obstacle>();
    private List<Obstacle> ladders = new ArrayList<Obstacle>();
    private List<Die> dice = new ArrayList<Die>(2);
    private Random obstacleLocationGenerator = new Random();
    static Scanner playerInput = new Scanner(System.in);
    private boolean hasGameStarted;
    private int curTurn;

    public Board()
    {
        //Creating elements required for the game
        for (int i = 0; i < 2; i++)
        {
            Die die = new Die();
            dice.add(die);
        }
        this.hasGameStarted = false;
        this.curTurn = 0;
    }

    //Adding player function
    public void AddPlayer(String name, String password, int playerNum, boolean isLoggingIn)
    {
        //Add players to the players list
        if (playerNum == 1 && name == "AI" && password == "") {
            Player newAi = new Player();
            players.add(newAi);
        } else {
            Player newPlayer = new Player(name, password, isLoggingIn);
            players.add(newPlayer);
        }

    }

    //Update player info function
    public void UpdatePlayer(String name, String password, String newName, String newPassword)
    {
        //Update player on the player list and on the database
        for(int i = 0; i < players.size(); i++)
        {
            if(players.get(i).GetName().equals(name) && players.get(i).GetPassword().equals(password))
            {
                players.get(i).SetName(newName);
                players.get(i).SetPassword(newPassword);
            }
        }
        database.UpdatePlayerInfo(name, password, newName, newPassword);
    }

    //Generate board function
    public void GenerateBoard()
    {
        //Creating variables needed for board generation
        int i;
        int snakeStartPos;
        int snakeEndPos;
        int ladderStartPos;
        int ladderEndPos;
        List<Integer> takenTiles = new ArrayList<>();

        //Setting all players current position to the start
        for(i = 0; i < players.size(); i++)
        {
            players.get(i).SetCurPos(1);
        }

        //Generating snakes and ladders
        for (i = 1; i <= 5; i++)
        {
            //Generate a snake starting position if not taken
            snakeStartPos = obstacleLocationGenerator.nextInt((i - 1) * 20 + 2, i * 20 - 1);
            while (takenTiles.contains(snakeStartPos) == true) {
                snakeStartPos = obstacleLocationGenerator.nextInt((i - 1) * 20 + 2, i * 20 - 1);
            }
            takenTiles.add(snakeStartPos);

            //Generate a snake endinging position if not taken
            snakeEndPos = obstacleLocationGenerator.nextInt(1, i * 20 - 1);
            while (snakeEndPos >= snakeStartPos || takenTiles.contains(snakeEndPos) == true)
            {
                snakeEndPos = obstacleLocationGenerator.nextInt(1, i * 20 - 1);
            }
            takenTiles.add(snakeEndPos);

            //Add snake to the snakes list
            Snake snake = new Snake("snake", snakeStartPos, snakeEndPos);
            snakes.add(snake);

            //Generate a ladder starting position if not taken
            ladderStartPos = obstacleLocationGenerator.nextInt((i - 1) * 20 + 2, i * 20 - 1);
            while (ladderStartPos == snakeStartPos || ladderStartPos == snakeEndPos || takenTiles.contains(ladderStartPos) == true)
            {
                ladderStartPos = obstacleLocationGenerator.nextInt(2,i * 20 - 1);
            }
            takenTiles.add(ladderStartPos);

            //Generate a snake ending position if not taken
            ladderEndPos = obstacleLocationGenerator.nextInt(99);
            while (ladderEndPos == snakeStartPos || ladderEndPos == snakeEndPos || ladderEndPos <= ladderStartPos ||
                    takenTiles.contains(ladderEndPos) == true)
            {
                ladderEndPos = obstacleLocationGenerator.nextInt(99);
            }
            takenTiles.add(ladderEndPos);

            //Adding ladder to the ladders list
            Ladder ladder = new Ladder("ladder", ladderStartPos, ladderEndPos);
            ladders.add(ladder);

        }

        //Tile generation
        for(i = 1; i <= 100; i++)
        {
            Tile tile = null;
            //If tile position is part of a snake or ladder update it
            for(int j = 0; j < 5; j++)
            {
                if(i == snakes.get(j).GetStartPos())
                {
                    tile = new Tile(i, "snake start");
                }
                else if(i == snakes.get(j).GetEndPos())
                {
                    tile = new Tile(i, "snake end");
                }
                else if(i == ladders.get(j).GetStartPos())
                {
                    tile = new Tile(i, "ladder start");
                }
                else if(i == ladders.get(j).GetEndPos())
                {
                    tile = new Tile(i, "ladder end");
                }
            }
            if(tile == null)
            {
                //If the tile is the start or finish positions, update it, otherwise it is a normal tile
                if(i == 1)
                {
                    tile = new Tile(i, "start");
                }
                else if(i == 100)
                {
                    tile = new Tile(i, "Finish");
                }
                else
                {
                    tile = new Tile(i, "normal");
                }
            }

            tiles.add(tile);
        }

    }

    //Gameplay function
    public boolean Gameplay()
    {
        //Creating variables used in gameplay
        int roll;
        int overBoard;

        //If the game has not started, set the current turn to 0 and start the game
        if(hasGameStarted == false)
        {
            curTurn = 0;
            hasGameStarted = true;
        }

        //Display all players position and whose turn it is
        System.out.println("\nCurrent standings:");
        for(int i = 0; i < players.size(); i++)
        {
            System.out.println(players.get(i).GetName() + "'s position is: " + players.get(i).GetCurPos());
        }
        System.out.println(players.get(curTurn).GetName() + "'s turn");

        //If the player is an AI play their turn, else wait for the user to press the enter key
        if(players.get(curTurn).GetName() != "AI")
        {
            System.out.println("Press the ENTER key to play your turn");
            playerInput.nextLine();
        }

        //Generate dice roll for this turn, display it, and move the player
        roll = 0;
        for(int i = 0; i < 2; i++)
        {
            roll += dice.get(i).RollDie();
        }
        System.out.println("Your roll is: " + roll);
        players.get(curTurn).SetCurPos(players.get(curTurn).GetCurPos() + roll);

        //If the player lands on a special tile perform an action
        if(players.get(curTurn).GetCurPos() == 100)
        {
            //If a player wins the game, display it, and update all players statistics
            System.out.println("You have reached 100 and have won the game!");
            for(int i = 0; i < players.size(); i++)
            {
                if(i == curTurn)
                {
                    players.get(i).AddGamesWon();
                }
                players.get(i).FinishedGame();
                players.get(i).SetWinRate();
                database.UpdatePlayerStats(players.get(i).GetName(), players.get(i).GetTotalGamesPlayed(),
                        players.get(i).GetGamesWon(), players.get(i).GetWinRate());
            }

            //Clear the game
            curTurn = 0;
            hasGameStarted = false;
            tiles.clear();
            snakes.clear();
            ladders.clear();
            return true;
        }
        else if(players.get(curTurn).GetCurPos() > 100)
        {
            //If the player has gone over 100, make sure they go that amount backwards
            overBoard = players.get(curTurn).GetCurPos() - 100;
            players.get(curTurn).SetCurPos(100 - overBoard);
        }

        //If a player lands on a snake or ladder move them accordingly
        if(tiles.get(players.get(curTurn).GetCurPos() - 1).CheckTile() == true)
        {
            for(int i = 0; i < 5; i++)
            {
                //If a player lands on the start of a snake, move them and exit the loop
                if(snakes.get(i).GetStartPos() == players.get(curTurn).GetCurPos())
                {
                    snakes.get(i).MovePlayer(players.get(curTurn));
                    i = 5;
                }
                //If a player lands on the start of a ladder, move them and exit the loop
                else if(ladders.get(i).GetStartPos() == players.get(curTurn).GetCurPos())
                {
                    ladders.get(i).MovePlayer(players.get(curTurn));
                    i = 5;
                }
            }
            System.out.println("You have ended your turn in " + players.get(curTurn).GetCurPos());
        }

        //Change the current turn to the next players turn
        if(curTurn < (players.size() - 1))
        {
            curTurn += 1;
        }
        else
        {
            curTurn = 0;
        }

        //Return that the game is not over
        return false;

    }

    //Clear the current list of players
    public void ClearPlayers()
    {
        players.clear();
    }

}
