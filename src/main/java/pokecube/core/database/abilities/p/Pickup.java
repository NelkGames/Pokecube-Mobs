package pokecube.core.database.abilities.p;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import pokecube.core.PokecubeItems;
import pokecube.core.database.abilities.Ability;
import pokecube.core.interfaces.IPokemob;
import pokecube.core.interfaces.pokemob.ai.GeneralStates;
import pokecube.core.interfaces.pokemob.ai.LogicStates;

public class Pickup extends Ability
{
    public static ResourceLocation lootTable    = new ResourceLocation("pokecube", "abilities/pickup");
    public static boolean          useLootTable = true;

    @Override
    public void onUpdate(IPokemob mob)
    {
        final LivingEntity poke = mob.getEntity();
        // Staying in one place, nothing to find.
        if (mob.getGeneralState(GeneralStates.STAYING)) return;
        // Only works if your pokemob is following you.
        if (mob.getLogicState(LogicStates.SITTING)) return;

        if (poke.ticksExisted % 200 != 0 || Math.random() > 0.05) return;
        if (!mob.getHeldItem().isEmpty()) return;

        if (Pickup.lootTable != null && Pickup.useLootTable)
        {
            // Load in the loot table.
            final LootTable loottable = mob.getEntity().getEntityWorld().getServer().getLootTableManager()
                    .getLootTableFromLocation(Pickup.lootTable);
            final LootContext.Builder lootcontext$builder = new LootContext.Builder((ServerWorld) mob.getEntity()
                    .getEntityWorld()).withParameter(LootParameters.THIS_ENTITY, mob.getEntity());
            // Apply bonuses from the player
            if (mob.getOwner() instanceof ServerPlayerEntity) lootcontext$builder.withParameter(
                    LootParameters.KILLER_ENTITY, mob.getOwner());
            // Generate the loot list.
            final List<ItemStack> list = loottable.generate(lootcontext$builder.build(loottable.func_216122_a()));
            // Shuffle the list.
            if (!list.isEmpty()) Collections.shuffle(list);
            for (final ItemStack itemstack : list)
                // Pick first valid item in it.
                if (!itemstack.isEmpty())
                {
                    final ItemStack stack = itemstack.copy();
                    if (stack.getItem().getRegistryName().equals(new ResourceLocation("pokecube", "candy")))
                        PokecubeItems.makeStackValid(stack);
                    mob.setHeldItem(stack);
                    return;
                }
        }
    }

}
