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
    @SuppressWarnings("unused")
    public ConfigurableVillagerConversionMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("unused")
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

        final var config = VillagerConversionRate.getInstance().getConfiguration();
        if (!config.enabled()) {
            // Customized conversion rate disabled. Default to vanilla behaviour.
            return;
        }

        // Mod is enabled, so suppress vanilla selection logic.
        callbackInfo.cancel();

        double conversionRate = config.conversionRate();
        if ((conversionRate == 0.0)                              // 0.0 means it is impossible for conversion to occur.
                || this.random.nextDouble() > conversionRate) {  // N.B.: `this.random.nextDouble() <= 1.0` is always `true`.
            // Conversion failed. Just let the villager die.
            callbackInfo.setReturnValue(true);
            return;
        }

        // Conversion succeeded. Can't delegate to vanilla since it will try to apply its own
        // decision about whether to kill the villager. Instead we have to unfortunately duplicate
        // the logic here.
        // TODO Find a way of doing this without copying the code from Zombie#killedEntity()

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
    }
}
