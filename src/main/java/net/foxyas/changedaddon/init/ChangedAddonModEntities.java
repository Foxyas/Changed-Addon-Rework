
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

import net.foxyas.changedaddon.entity.SnowLeopardMaleOrganicEntity;
import net.foxyas.changedaddon.entity.SnowLeopardFemaleOrganicEntity;
import net.foxyas.changedaddon.entity.PuroKindFemaleEntity;
import net.foxyas.changedaddon.entity.PuroKindEntity;
import net.foxyas.changedaddon.entity.PrototypeEntity;
import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.foxyas.changedaddon.entity.LatexSnowFoxEntity;
import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.entity.DazedEntity;
import net.foxyas.changedaddon.entity.BunyEntity;
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
	public static final RegistryObject<EntityType<LatexSnowFoxEntity>> LATEX_SNOW_FOX = register("latex_snow_fox",
			EntityType.Builder.<LatexSnowFoxEntity>of(LatexSnowFoxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LatexSnowFoxEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<LatexSnowFoxFemaleEntity>> LATEX_SNOW_FOX_FEMALE = register("latex_snow_fox_female",
			EntityType.Builder.<LatexSnowFoxFemaleEntity>of(LatexSnowFoxFemaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LatexSnowFoxFemaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Experiment009Entity>> EXPERIMENT_009 = register("experiment_009",
			EntityType.Builder.<Experiment009Entity>of(Experiment009Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(Experiment009Entity::new)

					.sized(0.6f, 1.9f));
	public static final RegistryObject<EntityType<Experiment009phase2Entity>> EXPERIMENT_009_PHASE_2 = register("experiment_009_phase_2", EntityType.Builder.<Experiment009phase2Entity>of(Experiment009phase2Entity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(128).setUpdateInterval(3).setCustomClientFactory(Experiment009phase2Entity::new).fireImmune().sized(0.6f, 1.9f));
	public static final RegistryObject<EntityType<DazedEntity>> DAZED = register("dazed",
			EntityType.Builder.<DazedEntity>of(DazedEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(DazedEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<PuroKindEntity>> PURO_KIND = register("puro_kind",
			EntityType.Builder.<PuroKindEntity>of(PuroKindEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PuroKindEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<PuroKindFemaleEntity>> PURO_KIND_FEMALE = register("puro_kind_female",
			EntityType.Builder.<PuroKindFemaleEntity>of(PuroKindFemaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PuroKindFemaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<BunyEntity>> BUNY = register("buny",
			EntityType.Builder.<BunyEntity>of(BunyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BunyEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<SnowLeopardMaleOrganicEntity>> SNOW_LEOPARD_MALE_ORGANIC = register("snow_leopard_male_organic",
			EntityType.Builder.<SnowLeopardMaleOrganicEntity>of(SnowLeopardMaleOrganicEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardMaleOrganicEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<SnowLeopardFemaleOrganicEntity>> SNOW_LEOPARD_FEMALE_ORGANIC = register("snow_leopard_female_organic",
			EntityType.Builder.<SnowLeopardFemaleOrganicEntity>of(SnowLeopardFemaleOrganicEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardFemaleOrganicEntity::new)

					.sized(0.7f, 1.93f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PrototypeEntity.init();
			FoxyasEntity.init();
			LatexSnowFoxEntity.init();
			LatexSnowFoxFemaleEntity.init();
			Experiment009Entity.init();
			Experiment009phase2Entity.init();
			DazedEntity.init();
			PuroKindEntity.init();
			PuroKindFemaleEntity.init();
			BunyEntity.init();
			SnowLeopardMaleOrganicEntity.init();
			SnowLeopardFemaleOrganicEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PROTOTYPE.get(), PrototypeEntity.createAttributes().build());
		event.put(FOXYAS.get(), FoxyasEntity.createAttributes().build());
		event.put(LATEX_SNOW_FOX.get(), LatexSnowFoxEntity.createAttributes().build());
		event.put(LATEX_SNOW_FOX_FEMALE.get(), LatexSnowFoxFemaleEntity.createAttributes().build());
		event.put(EXPERIMENT_009.get(), Experiment009Entity.createAttributes().build());
		event.put(EXPERIMENT_009_PHASE_2.get(), Experiment009phase2Entity.createAttributes().build());
		event.put(DAZED.get(), DazedEntity.createAttributes().build());
		event.put(PURO_KIND.get(), PuroKindEntity.createAttributes().build());
		event.put(PURO_KIND_FEMALE.get(), PuroKindFemaleEntity.createAttributes().build());
		event.put(BUNY.get(), BunyEntity.createAttributes().build());
		event.put(SNOW_LEOPARD_MALE_ORGANIC.get(), SnowLeopardMaleOrganicEntity.createAttributes().build());
		event.put(SNOW_LEOPARD_FEMALE_ORGANIC.get(), SnowLeopardFemaleOrganicEntity.createAttributes().build());
	}
}
