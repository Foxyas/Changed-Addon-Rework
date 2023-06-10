
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.foxyas.changedaddon.entity.PrototypeEntity;
import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, ChangedAddonMod.MODID);
	public static final RegistryObject<EntityType<PrototypeEntity>> PROTOTYPE = register("prototype",
			EntityType.Builder.<PrototypeEntity>of(PrototypeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PrototypeEntity::new)

					.sized(0.9f, 1.9f));
	public static final RegistryObject<EntityType<FoxyasEntity>> FOXYAS = register("foxyas",
			EntityType.Builder.<FoxyasEntity>of(FoxyasEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FoxyasEntity::new)

					.sized(0.6f, 1.9f));
	public static final RegistryObject<EntityType<Experiment009Entity>> EXPERIMENT_009 = register("experiment_009",
			EntityType.Builder.<Experiment009Entity>of(Experiment009Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Experiment009Entity::new)

					.sized(0.6f, 1.9f));
	public static final RegistryObject<EntityType<Experiment009phase2Entity>> EXPERIMENT_009_PHASE_2 = register("experiment_009_phase_2", EntityType.Builder.<Experiment009phase2Entity>of(Experiment009phase2Entity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Experiment009phase2Entity::new).fireImmune().sized(0.6f, 1.9f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PrototypeEntity.init();
			FoxyasEntity.init();
			Experiment009Entity.init();
			Experiment009phase2Entity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PROTOTYPE.get(), PrototypeEntity.createAttributes().build());
		event.put(FOXYAS.get(), FoxyasEntity.createAttributes().build());
		event.put(EXPERIMENT_009.get(), Experiment009Entity.createAttributes().build());
		event.put(EXPERIMENT_009_PHASE_2.get(), Experiment009phase2Entity.createAttributes().build());
	}
}
