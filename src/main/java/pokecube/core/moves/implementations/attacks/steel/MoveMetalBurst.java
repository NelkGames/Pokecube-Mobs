package pokecube.core.moves.implementations.attacks.steel;

import net.minecraft.entity.EntityLivingBase;
import pokecube.core.interfaces.PokecubeMod;
import pokecube.core.interfaces.pokemob.moves.MovePacket;
import pokecube.core.moves.PokemobDamageSource;
import pokecube.core.moves.templates.Move_Basic;

public class MoveMetalBurst extends Move_Basic
{

    public MoveMetalBurst()
    {
        super("metalburst");
    }

    @Override
    public void postAttack(MovePacket packet)
    {
        super.postAttack(packet);
        if (packet.canceled || packet.failed) return;
        EntityLivingBase attacker = packet.attacker.getEntity();
        if (!packet.attacker.getMoveStats().biding)
        {
            attacker.getEntityData().setLong("bideTime",
                    attacker.getEntityWorld().getTotalWorldTime() + PokecubeMod.core.getConfig().attackCooldown);
            packet.attacker.getMoveStats().biding = true;
            packet.attacker.getMoveStats().SPECIALDAMAGETAKENCOUNTER = 0;
            packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER = 0;
        }
        else
        {
            if (attacker.getEntityData().getLong("bideTime") < attacker.getEntityWorld().getTotalWorldTime())
            {
                attacker.getEntityData().removeTag("bideTime");
                int damage = (int) (1.5 * (packet.attacker.getMoveStats().SPECIALDAMAGETAKENCOUNTER
                        + packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER));
                packet.attacker.getMoveStats().SPECIALDAMAGETAKENCOUNTER = 0;
                packet.attacker.getMoveStats().PHYSICALDAMAGETAKENCOUNTER = 0;
                if (packet.attacked != null)
                    packet.attacked.attackEntityFrom(new PokemobDamageSource("mob", attacker, this), damage);
                packet.attacker.getMoveStats().biding = false;
            }
        }
    }
}