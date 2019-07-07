package pokecube.core.moves.implementations.attacks.psychic;

import net.minecraft.entity.Entity;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.moves.templates.Move_Basic;

public class Move_Psywave extends Move_Basic
{

    public Move_Psywave()
    {
        super("psywave");
    }

    @Override
    public int getPWR(IPokemob user, Entity target)
    {
        int lvl = user.getLevel();
        int pwr = (int) Math.max(1, lvl * (Math.random() + 0.5));

        return pwr;
    }

}
