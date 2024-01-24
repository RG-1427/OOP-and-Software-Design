//Ladder class
package com.company;

public class Ladder extends Obstacle
{
    public Ladder(String type, Integer startPos, Integer endPos)
    {
        //Creating a ladder obstacle
        super(type, startPos, endPos);
        type = "ladder";
    }

    //Moving player and informing the player they have landed on a ladder
    @Override
    public void MovePlayer(Player player)
    {
        System.out.println("You have landed on a ladder :)");
        super.MovePlayer(player);
    }
}
