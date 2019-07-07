package pokecube.core.moves.implementations.attacks.special;

import pokecube.core.interfaces.IPokemob;
import pokecube.core.moves.templates.Move_Basic;
import pokecube.core.utils.PokeType;

public class MoveMultiAttack extends Move_Basic
{

    public MoveMultiAttack()
    {
        super("multiattack");
    }

    /** Type getter
     * 
     * @return the type of this move */
    @Override
    public PokeType getType(IPokemob user)
    {
        if (user == null) return move.type;
        return user.getType1();
    }

}
