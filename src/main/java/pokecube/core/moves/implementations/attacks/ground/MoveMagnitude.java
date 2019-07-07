package pokecube.core.moves.implementations.attacks.ground;

import java.util.Random;

import net.minecraft.entity.Entity;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.moves.templates.Move_AOE;

public class MoveMagnitude extends Move_AOE
{

    public MoveMagnitude()
    {
        super("magnitude");
    }

    @Override
    public int getPWR(IPokemob attacker, Entity attacked)
    {
        int PWR = 0;
        int rand = (new Random()).nextInt(20);
        if (rand == 0) PWR = 10;
        else if (rand <= 2) PWR = 30;
        else if (rand <= 6) PWR = 50;
        else if (rand <= 12) PWR = 70;
        else if (rand <= 16) PWR = 90;
        else if (rand <= 18) PWR = 110;
        else PWR = 150;
        return PWR;
    }

}
