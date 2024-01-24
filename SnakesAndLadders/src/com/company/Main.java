//Main class
package com.company;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {


    //Creating variables necessary for navigation between the game stages
    static Scanner input = new Scanner(System.in);
    static String selection;
    static boolean isSelectionValid = false;
    static int displayMsg;
    static int playerNum;
    static boolean isLoggingIn;
    static List<String> usedNames = new ArrayList<String>();
    static List<String> usedPasswords = new ArrayList<String>();
    static boolean isInstructionScreen = false;
    static boolean isInMenuSection = true;
    static boolean isInPlayerSection = false;
    static boolean isInGameEndSection = false;
    static boolean isGameOver = false;

    //Creating a game board and database to store player information
    static Board gameBoard = new Board();
    static Database database = new Database();

    public static void main(String[] args)
    {

        //Calling the menu function
        Menu();
    }

    //Menu function
    public static void Menu() {
        //Setting the application to be in the menu
        isInMenuSection = true;
        isInPlayerSection = false;
        isInGameEndSection = false;
        isInstructionScreen = false;

        //printing menu instructions
        System.out.println("Welcome to snakes and ladders!\n\nMenu options(input the first letter of the option to continue, " +
                "for example enter p for play):\n1. Play\n2. Leaderboard");
        ScreenSelection();
    }

    //Instructions function
    public static void Instructions() {

        //Setting the application to be in the instructions screen. Displaying the instructions
        isInstructionScreen = true;
        System.out.println("\n\nSnakes and ladders instructions:");
        System.out.println("\nYour goal is to reach tile 100 before the other players, but you must land exactly on the " +
                "100th tile! Otherwise you will move back :(");
        System.out.println("You will move tiles based on your roll");
        System.out.println("If you land on a snake you will fall down that snake towards the beginning :(");
        System.out.println("If you land on a ladder you will climb up the ladder and advance towards the end goal :)");
        System.out.println("Good luck and have fun!");
        System.out.println("Enter p to continue to set up the game, m for the menu or l for the leaderboard");
        ScreenSelection();
    }

    //Screen selection function
    public static void ScreenSelection() {
        isSelectionValid = false;
        displayMsg = 0;

        //while the user has not chosen the next screen
        while(isSelectionValid == false)
        {
            selection = input.next().toLowerCase();

            //If the user is in the menu screen navigate based on their input
            if(isInMenuSection == true)
            {
                switch(selection)
                {
                    case "p":
                        //If the user is in the menu move them to the instructions screen else move them to the player setup
                        if(isInstructionScreen == false)
                        {
                            Instructions();
                        }
                        else
                        {
                            PlayerSetup();
                        }
                        isSelectionValid = true;
                        break;
                    case "m":
                        Menu();
                        isSelectionValid = true;
                        break;
                    case "l":
                        Leaderboard();
                        isSelectionValid = true;
                        break;
                    default:
                        //If the user enters an invalid entry let them know
                        displayMsg = 1;
                        if(displayMsg == 1)
                        {
                            System.out.println("Please enter a valid value, as stated above.");
                        }
                        break;

                }
            }
            //If the user is in the player set up section navigate through the screens based on their selection
            else if(isInPlayerSection == true)
            {
                switch(selection)
                {
                    case "r":
                        PlayerRegistration();
                        isSelectionValid = true;
                        break;
                    case "l":
                        PlayerLogIn();
                        isSelectionValid = true;
                        break;
                    case "u":
                        PlayerUpdate();
                        isSelectionValid = true;
                        break;
                    case "p":
                        Play();
                        isSelectionValid = true;
                        break;
                    default:
                        //If the user enters an invalid entry let them know
                        displayMsg = 1;
                        if(displayMsg == 1)
                        {
                            System.out.println("Please enter a valid value, as stated above.");
                        }
                        break;
                }
            }
            //If the user has finished the game navigate them into their next screen based on their input
            else if(isInGameEndSection == true)
            {
                switch (selection)
                {
                    case "e":
                        gameBoard.ClearPlayers();
                        Menu();
                        break;
                    case "r":
                        Play();
                        break;
                    default:
                        //If the user enters an invalid entry let them know
                        displayMsg = 1;
                        if(displayMsg == 1)
                        {
                            System.out.println("Please enter a valid value, as stated above.");
                        }
                        break;
                }


            }
        }

    }

    //Player set up function
    public static void PlayerSetup() {
        //Set the application to be in the player set up screen
        isInMenuSection = false;
        isInPlayerSection = true;
        isInGameEndSection = false;
        isInstructionScreen = false;

        //Retrieve number of players from the user
        System.out.println("\nEnter the number of players playing (between 1-4 players," +
                " if you are playing single player you will play an AI):");
        PlayerNumInput();

        //Register or log in for players participating
        for(int i = 0; i < playerNum; ++i) {
            System.out.println("\nPlayer " + (i + 1) + "\nEnter r or l to register or log in:");
            ScreenSelection();
        }

        //Add AI if there is only one player
        if (playerNum == 1)
        {
            isLoggingIn = false;
            gameBoard.AddPlayer("AI", "", playerNum, isLoggingIn);
        }

        //Update players information if they would like or enter the game based on user input
        boolean isUpdateRequired = false;
        while(isUpdateRequired == false) {
            System.out.println("\nIf any player would like to update their information, enter u, else enter p to play");
            ScreenSelection();
        }

    }

    //Player number input function
    public static void PlayerNumInput() {
        isSelectionValid = false;
        displayMsg = 0;

        while(isSelectionValid == false) {
            //Try to convert the user input to a number
            try {
                playerNum = Integer.parseInt(input.next());
            }
            catch (NumberFormatException var1) {
                playerNum = 0;
            }

            //Based on the player number determine if it is a valid input or if the user has to try again
            switch(playerNum) {
                case 1:
                case 2:
                case 3:
                case 4:
                    isSelectionValid = true;
                    break;
                default:
                    displayMsg = 1;
                    if (displayMsg == 1) {
                        System.out.println("Please enter a valid value (1, 2, 3 or 4)");
                    }
                    break;
            }
        }

    }

    //Player registration function
    public static void PlayerRegistration() {
        //Ask the user for the information they would like to enter
        System.out.println("\nRegistration");
        System.out.println("Enter your name: ");
        String name = input.next();
        System.out.println("Enter your password: ");
        String password = input.next();

        //If it is already used tell them to choose to log in or register again, otherwise log them in and add them to the database
        if (database.SearchDatabase(name, password) == false) {
            isLoggingIn = false;
            gameBoard.AddPlayer(name, password, playerNum, isLoggingIn);
            usedNames.add(name);
            usedPasswords.add(password);
            System.out.println("You have joined the game");
        } else {
            System.out.println("\nThis data is already saved in the database. Press l to log in or r to register again");
            ScreenSelection();
        }

    }

    //Player login function
    public static void PlayerLogIn() {
        //Ask the user for their information to log in
        System.out.println("\nLogin");
        System.out.println("Enter your name: ");
        String name = input.next();
        System.out.println("Enter your password: ");
        String password = input.next();

        //If they are in the database, and they did not just register log them in and load their data,
        //otherwise have them choose to register or log in
        if ((database.SearchDatabase(name, password) == false || (usedNames.contains(name) == true &&
                usedPasswords.contains(password)) == true))
        {
            System.out.println("\nThis data is not saved in the database or someone have already joined the game with " +
                    "this account. Press l to try to log in again or r to register");
            ScreenSelection();
        }
        else
        {
            isLoggingIn = true;
            gameBoard.AddPlayer(name, password, playerNum, isLoggingIn);
            usedNames.add(name);
            usedPasswords.add(password);
            System.out.println("You have joined the game");
        }

    }

    //Player update function
    public static void PlayerUpdate() {
        //Ask the player what information they currently have
        System.out.println("\nUpdate");
        System.out.println("Enter your current name in the system: ");
        String name = input.next();
        System.out.println("Enter your current password in the system: ");
        String password = input.next();

        //If they are in the database, ask them for new information, otherwise ask if they want to update a player's information
        //or play
        if (database.SearchDatabase(name, password) == true) {
            System.out.println("\nEnter your new name: ");
            String newName = input.next();
            System.out.println("Enter your new password: ");
            String newPassword = input.next();

            //If the new information is already in the database, ask the user if they want to update their information or play,
            //otherwise update the database
            if((database.SearchDatabase(newName, newPassword) == false))
            {
                gameBoard.UpdatePlayer(name, password, newName, newPassword);
            }
            else
            {
                System.out.println("That data is already in the database.");
                System.out.println("\nIf any player would like to update their information, enter u, else enter p to play");
                ScreenSelection();
            }
        }
        else
        {
            System.out.println("\nThis data is not saved in the database. Press u to try and update again or p to play");
            ScreenSelection();
        }

    }

    //Play function
    public static void Play() {
        //Set application to gameplay screen
        isInMenuSection = false;
        isInPlayerSection = false;
        isInGameEndSection = true;
        isGameOver = false;

        //Clear the users logged in for the next registration or log in
        usedNames.clear();
        usedPasswords.clear();

        //Generate game board
        gameBoard.GenerateBoard();

        //While the game is not done, keep playing
        while (isGameOver == false)
        {
            isGameOver = gameBoard.Gameplay();
        }

        System.out.println("\nEnter e to exit game or r to reset the game");
        ScreenSelection();
    }

    //Leaderboard function
    public static void Leaderboard()
    {
        //Print a leaderboard based on the current data available in the database. Ask the user what they would like to do next
        database.UpdateLeaderboard();
        System.out.println("Enter m to return to the menu, or p to play");
        ScreenSelection();
    }

}

