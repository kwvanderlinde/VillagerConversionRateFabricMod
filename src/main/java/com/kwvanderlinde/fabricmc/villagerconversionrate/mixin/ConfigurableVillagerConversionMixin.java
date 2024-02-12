package com.kwvanderlinde.fabricmc.villagerconversionrate.mixin;

import com.kwvanderlinde.fabricmc.villagerconversionrate.common.VillagerConversionRate;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Zombie.class)
public class ConfigurableVillagerConversionMixin extends Monster {
    // region Just satisfy the compiler with these constructors
    public ConfigurableVillagerConversionMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    public ConfigurableVillagerConversionMixin(Level level) {
        this(EntityType.ZOMBIE, level);
    }
    // endregion

    @Inject(at = @At("HEAD"), method = "killedEntity", cancellable = true)
    public void killedEntity(ServerLevel serverLevel, LivingEntity livingEntity, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (!(livingEntity instanceof Villager villager)) {
            // Not a potential villager conversion event. Fallback to vanilla behaviour for this case.
            return;
        }

        switch (VillagerConversionRate.getInstance().getConversionPredicate().test()) {
            case USE_VANILLA_BEHAVIOUR:
                // Customized conversion rate disabled. Default to vanilla behaviour.
                return;

            case KILL: {
                // Customized conversion rate is enabled, but conversion failed. Suppress vanilla behaviour and do not apply conversion logic.
                callbackInfo.cancel();
                return;
            }

            case CONVERT: {
                // Customized conversion rate is enabled and conversion succeeded. Suppress vanilla behaviour and apply conversion logic.
                callbackInfo.cancel();

                // Perform the conversion by making a new zombie villager with the villager's data.
                // region TODO Find a way of doing this without copying the code from net.minecraft.entity.mob.ZombieEntity#onKilledOther()
                ZombieVillager zombieVillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
                if (zombieVillager != null) {
                    zombieVillager.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(zombieVillager.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), null);
                    zombieVillager.setVillagerData(villager.getVillagerData());
                    zombieVillager.setGossips(villager.getGossips().store(NbtOps.INSTANCE));
                    zombieVillager.setTradeOffers(villager.getOffers().createTag());
                    zombieVillager.setVillagerXp(villager.getVillagerXp());
                    if (!this.isSilent()) {
                        serverLevel.levelEvent(null, 1026, this.blockPosition(), 0);
                    }

                    callbackInfo.setReturnValue(false);
                }
                // endregion
            }
        }
    }
}
