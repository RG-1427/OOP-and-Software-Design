//Obstacle class
package com.company;

public class Obstacle
{
    //Creating variables needed for an obstacle
    protected String type;
    protected Integer startPos;
    protected Integer endPos;

    public Obstacle(String type, Integer startPos, Integer endPos)
    {
        //Generating an obstacle based on the information provided
        this.type = type;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    //Set an obstacle's starting position
    public void SetStartPos(int startPos)
    {
        this.startPos = startPos;
    }

    //Set an obstacle's ending position
    public void SetEndPos(int endPos)
    {
        this.endPos = endPos;
    }

    //Get an obstacle's starting position
    public int GetStartPos()
    {
        return startPos;
    }

    //Get an obstacle's ending position
    public int GetEndPos()
    {
        return endPos;
    }

    //Move a player when they land on an obstacle
    public void MovePlayer(Player player)
    {
        player.SetCurPos(endPos);
    }

}

