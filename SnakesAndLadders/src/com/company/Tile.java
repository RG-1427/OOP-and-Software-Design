//Tile class
package com.company;

public class Tile
{
    //Creating variables needed for a tile
    private int num;
    private String type;

    public Tile (int num, String type)
    {
        //Creating a tile
        this.num = num;
        this.type = type;
    }

    //Set a tile's type
    public void SetType(String type)
    {
        this.type = type;
    }

    //Get a tile's type
    public String GetType()
    {
        return type;
    }

    //Set a tile's number
    public void SetNum(int num)
    {
        this.num = num;
    }

    //Get a tile's number
    public int GetNum()
    {
        return num;
    }

    //Checking tile, deciding if the user lands on the start of a ladder or a snake
    public boolean CheckTile()
    {
        if(type == "snake start" || type == "ladder start")
        {
            return true;
        }
        return false;
    }
}

