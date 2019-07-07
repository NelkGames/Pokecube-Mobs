package pokecube.core.moves.implementations.attacks.fixedorcustom;

import net.minecraft.entity.Entity;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.interfaces.capabilities.CapabilityPokemob;
import pokecube.core.moves.templates.Move_Basic;

public class MoveGrassknot extends Move_Basic
{

    public MoveGrassknot()
    {
        super("grassknot");
    }

    @Override
    public int getPWR(IPokemob user, Entity target)
    {
        int pwr = 120;
        IPokemob targetMob = CapabilityPokemob.getPokemobFor(target);
        if (targetMob == null) return pwr;
        double mass = targetMob.getWeight();
        if (mass < 10) return 20;
        if (mass < 25) return 40;
        if (mass < 50) return 60;
        if (mass < 100) return 80;
        if (mass < 200) return 100;

        return pwr;
    }
}
