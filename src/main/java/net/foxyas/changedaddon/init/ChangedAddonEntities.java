
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.entity.advanced.AvaliEntity;
import net.foxyas.changedaddon.entity.advanced.LatexKitsuneFemaleEntity;
import net.foxyas.changedaddon.entity.advanced.LatexKitsuneMaleEntity;
import net.foxyas.changedaddon.entity.advanced.ProtogenEntity;
import net.foxyas.changedaddon.entity.projectile.LuminarCrystalSpearEntity;
import net.foxyas.changedaddon.entity.projectile.ParticleProjectile;
import net.foxyas.changedaddon.entity.simple.LatexCalicoCatEntity;
import net.ltxprogrammer.changed.init.ChangedMobCategories;
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

import net.foxyas.changedaddon.ChangedAddonMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonEntities {
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
	public static final RegistryObject<EntityType<LuminarCrystalSpearEntity>> LUMINAR_CRYSTAL_SPEAR = register("projectile_luminar_crystal_spear", EntityType.Builder.<LuminarCrystalSpearEntity>of(LuminarCrystalSpearEntity::new, MobCategory.MISC)
			.setCustomClientFactory(LuminarCrystalSpearEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
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
	public static final RegistryObject<EntityType<LatexSquidTigerSharkEntity>> LATEX_SQUID_TIGER_SHARK = register("latex_squid_tiger_shark",
			EntityType.Builder.<LatexSquidTigerSharkEntity>of(LatexSquidTigerSharkEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(LatexSquidTigerSharkEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<LynxEntity>> LYNX = register("lynx",
			EntityType.Builder.<LynxEntity>of(LynxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LynxEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<FoxtaFoxyEntity>> FOXTA_FOXY = register("foxta_foxy",
			EntityType.Builder.<FoxtaFoxyEntity>of(FoxtaFoxyEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FoxtaFoxyEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<SnepsiLeopardEntity>> SNEPSI_LEOPARD = register("snepsi_leopard",
			EntityType.Builder.<SnepsiLeopardEntity>of(SnepsiLeopardEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SnepsiLeopardEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<BagelEntity>> BAGEL = register("bagel",
			EntityType.Builder.<BagelEntity>of(BagelEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BagelEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<LatexDragonSnowLeopardSharkEntity>> LATEX_DRAGON_SNOW_LEOPARD_SHARK = register("latex_dragon_snow_leopard_shark",
			EntityType.Builder.<LatexDragonSnowLeopardSharkEntity>of(LatexDragonSnowLeopardSharkEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(LatexDragonSnowLeopardSharkEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<CrystalGasCatMaleEntity>> CRYSTAL_GAS_CAT_MALE = register("crystal_gas_cat_male",
			EntityType.Builder.<CrystalGasCatMaleEntity>of(CrystalGasCatMaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CrystalGasCatMaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<CrystalGasCatFemaleEntity>> CRYSTAL_GAS_CAT_FEMALE = register("crystal_gas_cat_female",
			EntityType.Builder.<CrystalGasCatFemaleEntity>of(CrystalGasCatFemaleEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CrystalGasCatFemaleEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<VoidFoxEntity>> VOID_FOX = register("void_fox",
			EntityType.Builder.<VoidFoxEntity>of(VoidFoxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(VoidFoxEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<FengQIWolfEntity>> FENGQI_WOLF = register("fengqi_wolf",
			EntityType.Builder.<FengQIWolfEntity>of(FengQIWolfEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FengQIWolfEntity::new)

					.sized(0.7f, 1.93f));
	public static final RegistryObject<EntityType<HaydenFennecFoxEntity>> HAYDEN_FENNEC_FOX = register("hayden_fennec_fox",
			EntityType.Builder.<HaydenFennecFoxEntity>of(HaydenFennecFoxEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(HaydenFennecFoxEntity::new)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<SnowLeopardPartialEntity>> SNOW_LEOPARD_PARTIAL = register("latex_snow_leopard_partial",
			EntityType.Builder.<SnowLeopardPartialEntity>of(SnowLeopardPartialEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardPartialEntity::new)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<BlueLizard>> BLUE_LIZARD = register("blue_lizard",
			EntityType.Builder.<BlueLizard>of(BlueLizard::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(BlueLizard::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<AvaliEntity>> AVALI = register("avali",
			EntityType.Builder.<AvaliEntity>of(AvaliEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(AvaliEntity::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<LatexKitsuneMaleEntity>> LATEX_KITSUNE_MALE = register("latex_kitsune_male",
			EntityType.Builder.<LatexKitsuneMaleEntity>of(LatexKitsuneMaleEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(LatexKitsuneMaleEntity::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<LatexKitsuneFemaleEntity>> LATEX_KITSUNE_FEMALE = register("latex_kitsune_female",
			EntityType.Builder.<LatexKitsuneFemaleEntity>of(LatexKitsuneFemaleEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(LatexKitsuneFemaleEntity::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));


	public static final RegistryObject<EntityType<LatexCalicoCatEntity>> LATEX_CALICO_CAT = register("latex_calico_cat",
			EntityType.Builder.<LatexCalicoCatEntity>of(LatexCalicoCatEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(LatexCalicoCatEntity::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<ProtogenEntity>> PROTOGEN = register("protogen",
			EntityType.Builder.<ProtogenEntity>of(ProtogenEntity::new, ChangedMobCategories.CHANGED)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(ProtogenEntity::new)
					.clientTrackingRange(10)

					.sized(0.7f, 1.93f));


	public static final RegistryObject<EntityType<ParticleProjectile>> PARTICLE_PROJECTILE = register("particle_projectile",
			EntityType.Builder.<ParticleProjectile>of(ParticleProjectile::new, MobCategory.MISC)
					.setShouldReceiveVelocityUpdates(true)
					.clientTrackingRange(64)
					.updateInterval(1)

					.sized(0.25F, 0.25F));

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
			LatexSquidTigerSharkEntity.init();
			LynxEntity.init();
			FoxtaFoxyEntity.init();
			SnepsiLeopardEntity.init();
			BagelEntity.init();
			LatexDragonSnowLeopardSharkEntity.init();
			CrystalGasCatMaleEntity.init();
			CrystalGasCatFemaleEntity.init();
			VoidFoxEntity.init();
			FengQIWolfEntity.init();
			HaydenFennecFoxEntity.init();
			SnowLeopardPartialEntity.init();
			ParticleProjectile.init();
			BlueLizard.init();
			AvaliEntity.init();
			LatexKitsuneMaleEntity.init();
			LatexKitsuneFemaleEntity.init();
			LatexCalicoCatEntity.init();
			ProtogenEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(PROTOTYPE.get(), PrototypeEntity.createAttributes().build());
		event.put(FOXYAS.get(), FoxyasEntity.createAttributes().build());
		event.put(LATEX_SNOW_FOX.get(), LatexSnowFoxEntity.createAttributes().build());
		event.put(LATEX_SNOW_FOX_FEMALE.get(), LatexSnowFoxFemaleEntity.createAttributes().build());
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
		event.put(LATEX_SQUID_TIGER_SHARK.get(), LatexSquidTigerSharkEntity.createAttributes().build());
		event.put(LYNX.get(), LynxEntity.createAttributes().build());
		event.put(FOXTA_FOXY.get(), FoxtaFoxyEntity.createAttributes().build());
		event.put(SNEPSI_LEOPARD.get(), SnepsiLeopardEntity.createAttributes().build());
		event.put(BAGEL.get(), BagelEntity.createAttributes().build());
		event.put(LATEX_DRAGON_SNOW_LEOPARD_SHARK.get(), LatexDragonSnowLeopardSharkEntity.createAttributes().build());
		event.put(CRYSTAL_GAS_CAT_MALE.get(), CrystalGasCatMaleEntity.createAttributes().build());
		event.put(CRYSTAL_GAS_CAT_FEMALE.get(), CrystalGasCatFemaleEntity.createAttributes().build());
		event.put(VOID_FOX.get(), VoidFoxEntity.createAttributes().build());
		event.put(FENGQI_WOLF.get(), FengQIWolfEntity.createAttributes().build());
		event.put(HAYDEN_FENNEC_FOX.get(), HaydenFennecFoxEntity.createAttributes().build());
		event.put(SNOW_LEOPARD_PARTIAL.get(), SnowLeopardPartialEntity.createAttributes().build());
		event.put(BLUE_LIZARD.get(), BlueLizard.createAttributes().build());
		event.put(AVALI.get(), AvaliEntity.createAttributes().build());
		event.put(LATEX_KITSUNE_MALE.get(), LatexKitsuneMaleEntity.createAttributes().build());
		event.put(LATEX_KITSUNE_FEMALE.get(), LatexKitsuneFemaleEntity.createAttributes().build());
		event.put(LATEX_CALICO_CAT.get(), LatexCalicoCatEntity.createAttributes().build());
		event.put(PROTOGEN.get(), ProtogenEntity.createAttributes().build());
	}
}
