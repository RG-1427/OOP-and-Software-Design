//Snake class
package com.company;

public class Snake extends Obstacle
{
    public Snake(String type, Integer startPos, Integer endPos)
    {
        //Creating a snake obstacle
        super(type, startPos, endPos);
        type = "snake";
    }

    //Moving player and informing the player they have landed on a snake
    @Override
    public void MovePlayer(Player player)
    {
        System.out.println("You have landed on a snake :(");
        super.MovePlayer(player);
    }
}