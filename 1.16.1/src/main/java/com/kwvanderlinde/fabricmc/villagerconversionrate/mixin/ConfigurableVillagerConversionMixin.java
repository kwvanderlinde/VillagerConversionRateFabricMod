package com.kwvanderlinde.fabricmc.villagerconversionrate.mixin;

import com.kwvanderlinde.fabricmc.villagerconversionrate.common.VillagerConversionRate;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.passive.VillagerEntity;
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

	@Inject(at = @At("HEAD"), method = "onKilledOther(Lnet/minecraft/entity/LivingEntity;)V", cancellable = true)
	public void onKilledOther(LivingEntity other, CallbackInfo callbackInfo) {
		if (!(other instanceof VillagerEntity)) {
			// Not a potential villager conversion event. Fallback to vanilla behaviour for this case.
			return;
		}

		switch (VillagerConversionRate.getInstance().getConversionPredicate().test()) {
			case USE_VANILLA_BEHAVIOUR:
				// Customized conversion rate disabled. Default to vanilla behaviour.
				return;

			case KILL:
				// Customized conversion rate is enabled, but conversion failed. Suppress vanilla behaviour and do not apply conversion logic.
				callbackInfo.cancel();
				return;

			case CONVERT:
				// Customized conversion rate is enabled and conversion succeeded. Suppress vanilla behaviour and apply conversion logic.
				callbackInfo.cancel();

				// Perform the conversion by making a new zombie villager with the villager's data.
				// region TODO Find a way of doing this without copying the code from net.minecraft.entity.mob.ZombieEntity#onKilledOther()
				VillagerEntity villagerEntity = (VillagerEntity)other;
				ZombieVillagerEntity zombieVillagerEntity = EntityType.ZOMBIE_VILLAGER.create(this.world);
				assert zombieVillagerEntity != null;
				zombieVillagerEntity.copyPositionAndRotation(villagerEntity);
				villagerEntity.remove();
				zombieVillagerEntity.initialize(this.world, this.world.getLocalDifficulty(zombieVillagerEntity.getBlockPos()), SpawnReason.CONVERSION, new ZombieEntity.ZombieData(false, true), null);
				zombieVillagerEntity.setVillagerData(villagerEntity.getVillagerData());
				zombieVillagerEntity.setGossipData(villagerEntity.getGossip().serialize(NbtOps.INSTANCE).getValue());
				zombieVillagerEntity.setOfferData(villagerEntity.getOffers().toTag());
				zombieVillagerEntity.setXp(villagerEntity.getExperience());
				zombieVillagerEntity.setBaby(villagerEntity.isBaby());
				zombieVillagerEntity.setAiDisabled(villagerEntity.isAiDisabled());
				if (villagerEntity.hasCustomName()) {
					zombieVillagerEntity.setCustomName(villagerEntity.getCustomName());
					zombieVillagerEntity.setCustomNameVisible(villagerEntity.isCustomNameVisible());
				}

				if (villagerEntity.isPersistent()) {
					zombieVillagerEntity.setPersistent();
				}

				zombieVillagerEntity.setInvulnerable(this.isInvulnerable());
				this.world.spawnEntity(zombieVillagerEntity);
				if (!this.isSilent()) {
					this.world.syncWorldEvent(null, 1026, this.getBlockPos(), 0);
				}
				// endregion
				break;
		}
	}
}
