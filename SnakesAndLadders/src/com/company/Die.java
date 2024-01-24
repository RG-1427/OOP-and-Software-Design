//Die class
package com.company;
import java.util.Random;

public class Die
{
    //Creating variables used for a die
    private Integer curRoll;
    private Random rollGenerator = new Random();

    //Roll a die and return the value of it
    public int RollDie()
    {
        int curRoll = rollGenerator.nextInt(1,7);
        return curRoll;
    }

}
