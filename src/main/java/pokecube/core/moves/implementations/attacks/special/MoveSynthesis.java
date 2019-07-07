package pokecube.core.moves.implementations.attacks.special;

import pokecube.core.interfaces.IPokemob;
import pokecube.core.moves.PokemobTerrainEffects;
import pokecube.core.moves.templates.Move_Basic;
import thut.api.terrain.TerrainManager;
import thut.api.terrain.TerrainSegment;

public class MoveSynthesis extends Move_Basic
{

    public MoveSynthesis()
    {
        super("synthesis");
    }

    @Override
    public float getSelfHealRatio(IPokemob user)
    {
        TerrainSegment terrain = TerrainManager.getInstance().getTerrainForEntity(user.getEntity());
        PokemobTerrainEffects effects = (PokemobTerrainEffects) terrain.geTerrainEffect("pokemobEffects");
        if (effects.getEffect(PokemobTerrainEffects.EFFECT_WEATHER_RAIN) > 0
                || effects.getEffect(PokemobTerrainEffects.EFFECT_WEATHER_HAIL) > 0
                || effects.getEffect(PokemobTerrainEffects.EFFECT_WEATHER_SAND) > 0) { return 25f; }
        if (effects.getEffect(PokemobTerrainEffects.EFFECT_WEATHER_SUN) > 0) { return 200 / 3f; }
        return 50f;
    }

}
