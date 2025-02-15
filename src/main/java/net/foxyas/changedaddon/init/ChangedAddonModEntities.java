
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

import net.foxyas.changedaddon.entity.WolfyEntity;
import net.foxyas.changedaddon.entity.SnowLeopardMaleOrganicEntity;
import net.foxyas.changedaddon.entity.SnowLeopardFemaleOrganicEntity;
import net.foxyas.changedaddon.entity.ReynEntity;
import net.foxyas.changedaddon.entity.PuroKindFemaleEntity;
import net.foxyas.changedaddon.entity.PuroKindEntity;
import net.foxyas.changedaddon.entity.PrototypeEntity;
import net.foxyas.changedaddon.entity.MirrorWhiteTigerEntity;
import net.foxyas.changedaddon.entity.LuminarcticLeopardEntity;
import net.foxyas.changedaddon.entity.LatexSnowFoxFemaleEntity;
import net.foxyas.changedaddon.entity.LatexSnowFoxEntity;
import net.foxyas.changedaddon.entity.LatexSnepEntity;
import net.foxyas.changedaddon.entity.KetExperiment009Entity;
import net.foxyas.changedaddon.entity.KetExperiment009BossEntity;
import net.foxyas.changedaddon.entity.FoxyasEntity;
import net.foxyas.changedaddon.entity.FemaleLuminarcticLeopardEntity;
import net.foxyas.changedaddon.entity.Experiment10Entity;
import net.foxyas.changedaddon.entity.Experiment10BossEntity;
import net.foxyas.changedaddon.entity.Experiment009phase2Entity;
import net.foxyas.changedaddon.entity.Experiment009Entity;
import net.foxyas.changedaddon.entity.Exp6Entity;
import net.foxyas.changedaddon.entity.Exp2MaleEntity;
import net.foxyas.changedaddon.entity.Exp2FemaleEntity;
import net.foxyas.changedaddon.entity.Exp1MaleEntity;
import net.foxyas.changedaddon.entity.Exp1FemaleEntity;
import net.foxyas.changedaddon.entity.ErikEntity;
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
	public static final RegistryObject<EntityType<SnowLeopardFemaleOrganicEntity>> SNOW_LEOPARD_FEMALE_ORGANIC = register("snow_leopard_female_organic",
			EntityType.Builder.<SnowLeopardFemaleOrganicEntity>of(SnowLeopardFemaleOrganicEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardFemaleOrganicEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<KetExperiment009Entity>> KET_EXPERIMENT_009 = register("ket_experiment_009", EntityType.Builder.<KetExperiment009Entity>of(KetExperiment009Entity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(KetExperiment009Entity::new).fireImmune().sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<MirrorWhiteTigerEntity>> MIRROR_WHITE_TIGER = register("mirror_white_tiger",
			EntityType.Builder.<MirrorWhiteTigerEntity>of(MirrorWhiteTigerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(MirrorWhiteTigerEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<SnowLeopardMaleOrganicEntity>> SNOW_LEOPARD_MALE_ORGANIC = register("snow_leopard_male_organic",
			EntityType.Builder.<SnowLeopardMaleOrganicEntity>of(SnowLeopardMaleOrganicEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardMaleOrganicEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Experiment10Entity>> EXPERIMENT_10 = register("experiment_10",
			EntityType.Builder.<Experiment10Entity>of(Experiment10Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Experiment10Entity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Exp2MaleEntity>> EXP_2_MALE = register("exp_2_male",
			EntityType.Builder.<Exp2MaleEntity>of(Exp2MaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Exp2MaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Exp2FemaleEntity>> EXP_2_FEMALE = register("exp_2_female",
			EntityType.Builder.<Exp2FemaleEntity>of(Exp2FemaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Exp2FemaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<WolfyEntity>> WOLFY = register("wolfy",
			EntityType.Builder.<WolfyEntity>of(WolfyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(WolfyEntity::new).fireImmune().sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<ErikEntity>> ERIK = register("erik",
			EntityType.Builder.<ErikEntity>of(ErikEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ErikEntity::new)

					.sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<Exp6Entity>> EXP_6 = register("exp_6",
			EntityType.Builder.<Exp6Entity>of(Exp6Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Exp6Entity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<ReynEntity>> REYN = register("reyn",
			EntityType.Builder.<ReynEntity>of(ReynEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ReynEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<KetExperiment009BossEntity>> KET_EXPERIMENT_009_BOSS = register("ket_experiment_009_boss", EntityType.Builder.<KetExperiment009BossEntity>of(KetExperiment009BossEntity::new, MobCategory.MONSTER)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(KetExperiment009BossEntity::new).fireImmune().sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Experiment10BossEntity>> EXPERIMENT_10_BOSS = register("experiment_10_boss",
			EntityType.Builder.<Experiment10BossEntity>of(Experiment10BossEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Experiment10BossEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Exp1MaleEntity>> EXP_1_MALE = register("exp_1_male",
			EntityType.Builder.<Exp1MaleEntity>of(Exp1MaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Exp1MaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<Exp1FemaleEntity>> EXP_1_FEMALE = register("exp_1_female",
			EntityType.Builder.<Exp1FemaleEntity>of(Exp1FemaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Exp1FemaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<LatexSnepEntity>> LATEX_SNEP = register("latex_snep",
			EntityType.Builder.<LatexSnepEntity>of(LatexSnepEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LatexSnepEntity::new)

					.sized(0.6f, 0.7f));
	public static final RegistryObject<EntityType<LuminarcticLeopardEntity>> LUMINARCTIC_LEOPARD = register("luminarctic_leopard",
			EntityType.Builder.<LuminarcticLeopardEntity>of(LuminarcticLeopardEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LuminarcticLeopardEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<FemaleLuminarcticLeopardEntity>> FEMALE_LUMINARCTIC_LEOPARD = register("female_luminarctic_leopard",
			EntityType.Builder.<FemaleLuminarcticLeopardEntity>of(FemaleLuminarcticLeopardEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(FemaleLuminarcticLeopardEntity::new)

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
			SnowLeopardFemaleOrganicEntity.init();
			KetExperiment009Entity.init();
			MirrorWhiteTigerEntity.init();
			SnowLeopardMaleOrganicEntity.init();
			Experiment10Entity.init();
			Exp2MaleEntity.init();
			Exp2FemaleEntity.init();
			WolfyEntity.init();
			ErikEntity.init();
			Exp6Entity.init();
			ReynEntity.init();
			KetExperiment009BossEntity.init();
			Experiment10BossEntity.init();
			Exp1MaleEntity.init();
			Exp1FemaleEntity.init();
			LatexSnepEntity.init();
			LuminarcticLeopardEntity.init();
			FemaleLuminarcticLeopardEntity.init();
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
		event.put(SNOW_LEOPARD_FEMALE_ORGANIC.get(), SnowLeopardFemaleOrganicEntity.createAttributes().build());
		event.put(KET_EXPERIMENT_009.get(), KetExperiment009Entity.createAttributes().build());
		event.put(MIRROR_WHITE_TIGER.get(), MirrorWhiteTigerEntity.createAttributes().build());
		event.put(SNOW_LEOPARD_MALE_ORGANIC.get(), SnowLeopardMaleOrganicEntity.createAttributes().build());
		event.put(EXPERIMENT_10.get(), Experiment10Entity.createAttributes().build());
		event.put(EXP_2_MALE.get(), Exp2MaleEntity.createAttributes().build());
		event.put(EXP_2_FEMALE.get(), Exp2FemaleEntity.createAttributes().build());
		event.put(WOLFY.get(), WolfyEntity.createAttributes().build());
		event.put(ERIK.get(), ErikEntity.createAttributes().build());
		event.put(EXP_6.get(), Exp6Entity.createAttributes().build());
		event.put(REYN.get(), ReynEntity.createAttributes().build());
		event.put(KET_EXPERIMENT_009_BOSS.get(), KetExperiment009BossEntity.createAttributes().build());
		event.put(EXPERIMENT_10_BOSS.get(), Experiment10BossEntity.createAttributes().build());
		event.put(EXP_1_MALE.get(), Exp1MaleEntity.createAttributes().build());
		event.put(EXP_1_FEMALE.get(), Exp1FemaleEntity.createAttributes().build());
		event.put(LATEX_SNEP.get(), LatexSnepEntity.createAttributes().build());
		event.put(LUMINARCTIC_LEOPARD.get(), LuminarcticLeopardEntity.createAttributes().build());
		event.put(FEMALE_LUMINARCTIC_LEOPARD.get(), FemaleLuminarcticLeopardEntity.createAttributes().build());
	}
}
