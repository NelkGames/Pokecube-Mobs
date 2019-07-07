/**
 * 
 */
package pokecube.core.moves.implementations.attacks.special;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IWorldEventListener;
import pokecube.core.interfaces.IMoveAnimation;
import pokecube.core.interfaces.IMoveConstants;
import pokecube.core.interfaces.IMoveNames;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.interfaces.capabilities.CapabilityPokemob;
import pokecube.core.interfaces.pokemob.moves.MovePacket;
import pokecube.core.moves.MovesUtils;
import pokecube.core.moves.templates.Move_Basic;
import thut.api.entity.IBreedingMob;

/** @author Manchou */
public class Move_Transform extends Move_Basic
{

    public static class Animation implements IMoveAnimation
    {
        @Override
        public void clientAnimation(MovePacketInfo info, IWorldEventListener world, float partialTick)
        {
        }

        @Override
        public int getDuration()
        {
            return 0;
        }

        @Override
        public int getApplicationTick()
        {
            return 0;
        }

        @Override
        public void setDuration(int arg0)
        {
        }

        @Override
        public void spawnClientEntities(MovePacketInfo info)
        {
        }

        @Override
        public void reallyInitRGBA()
        {
        }

    }

    /** @param name
     * @param type
     * @param PWR
     * @param PRE
     * @param PP
     * @param attackCategory */
    public Move_Transform()
    {
        super("transform");
        setAnimation(new Animation());
        this.setSelf();
        this.setNotInterceptable();

    }

    @Override
    public void attack(IPokemob attacker, Entity attacked)
    {
        IPokemob attackedMob = CapabilityPokemob.getPokemobFor(attacked);
        if (attacker.getTransformedTo() == null && attacked instanceof EntityLivingBase)
        {
            if (MovesUtils.contactAttack(attacker, attacked))
            {
                if (attackedMob != null)
                {
                    if (!(attacked instanceof IBreedingMob) || attacked != ((IBreedingMob) attacker).getLover())
                        ((EntityCreature) attacked).setAttackTarget(attacker.getEntity());
                }
                attacker.setTransformedTo(attacked);
            }
        }
        else
        {
            if (attackedMob != null)
            {
                String move = attackedMob.getMove(0);
                if (move != null && !IMoveNames.MOVE_TRANSFORM.equals(move))
                    MovesUtils.doAttack(move, attacker, attacked);
                else if (MovesUtils.contactAttack(attacker, attacked))
                {
                    MovesUtils.displayEfficiencyMessages(attacker, attacked, 0F, 1F);
                }
            }
            else if (attacked instanceof EntityPlayer)
            {
                if (MovesUtils.contactAttack(attacker, attacked))
                {
                    MovePacket packet = new MovePacket(attacker, attacked, name, move.type, 25, 1,
                            IMoveConstants.STATUS_NON, IMoveConstants.CHANGE_NONE);
                    onAttack(packet);
                }
            }
        }
    }
}
