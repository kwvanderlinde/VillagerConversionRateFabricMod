package com.kwvanderlinde.fabricmc.villagerconversionrate.mixin;

import com.kwvanderlinde.fabricmc.villagerconversionrate.VillagerConversionRateModInitializer;
import com.kwvanderlinde.fabricmc.villagerconversionrate.config.Configuration;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public class ConfigurableVillagerConversionMixin extends HostileEntity {
	// region This is just to satisfy the `extends` keyword.
	protected ConfigurableVillagerConversionMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
	// endregion

	@Inject(at = @At("HEAD"), method = "onKilledOther", cancellable = true)
	public void onKilledOther(ServerWorld serverWorld, LivingEntity other, CallbackInfo callbackInfo) {
		if (!(other instanceof VillagerEntity)) {
			// Not a potential villager conversion event. Fallback to vanilla behaviour for this case.
			return;
		}

		Configuration configuration = VillagerConversionRateModInitializer.getInstance().getConfigurationSource().get();
		if (!configuration.enabled.get()) {
			// Customized conversion rate disabled. Default to vanilla behaviour.
			return;
		}

		// Customized conversion rate is enabled. Suppress vanilla behaviour.
		callbackInfo.cancel();

		double conversionRate = configuration.conversionRate.get();
		if ((conversionRate == 0.0)                              // 0.0 means it is impossible for conversion to occur.
				|| this.random.nextDouble() > conversionRate) {  // N.B.: `this.random.nextDouble() <= 1.0` is always `true`.
			// Possible conversion failed. Do nothing and let the villager die.
			return;
		}

		// Possible conversion succeeded. Perform the conversion by making a new zombie villager with the villager's data.
		// region TODO Find a way of doing this without copying the code from net.minecraft.entity.mob.ZombieEntity#onKilledOther()
		VillagerEntity villagerEntity = (VillagerEntity)other;
		// method_29243 handles converting one entity to another, including copying common attributes like "is baby?" and "ai disabled".
		ZombieVillagerEntity zombieVillagerEntity = (ZombieVillagerEntity)villagerEntity.method_29243(EntityType.ZOMBIE_VILLAGER, false);
		assert zombieVillagerEntity != null;
		zombieVillagerEntity.initialize(serverWorld, serverWorld.getLocalDifficulty(zombieVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true), (CompoundTag)null);
		// method_29243 is not complete, so we do need to copy villager-specific data.
		zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
		zombieVillagerEntity.setGossipData((Tag)villagerEntity.getGossip().serialize(NbtOps.INSTANCE).getValue());
		zombieVillagerEntity.setOfferData(villagerEntity.getOffers().toTag());
		zombieVillagerEntity.setXp(villagerEntity.getExperience());
		if (!this.isSilent()) {
			serverWorld.syncWorldEvent((PlayerEntity)null, 1026, this.getBlockPos(), 0);
		}
		// endregion
	}
}
