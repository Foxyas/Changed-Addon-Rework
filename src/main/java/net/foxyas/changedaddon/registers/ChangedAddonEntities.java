package net.foxyas.changedaddon.registers;

import net.foxyas.changedaddon.entity.*;
import net.foxyas.changedaddon.entity.advanced.*;
import net.foxyas.changedaddon.entity.projectile.*;
import net.foxyas.changedaddon.entity.simple.*;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.init.ChangedMobCategories;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonEntities extends ChangedAddonModEntities {
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

                    .sized(0.25F, 0.25F)); // pequeno bounding box

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
        return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
    }


    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
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
        event.put(SNOW_LEOPARD_PARTIAL.get(), SnowLeopardPartialEntity.createAttributes().build());
		event.put(BLUE_LIZARD.get(), BlueLizard.createAttributes().build());
        event.put(AVALI.get(), AvaliEntity.createAttributes().build());
        event.put(LATEX_KITSUNE_MALE.get(), LatexKitsuneMaleEntity.createAttributes().build());
        event.put(LATEX_KITSUNE_FEMALE.get(), LatexKitsuneFemaleEntity.createAttributes().build());
        event.put(LATEX_CALICO_CAT.get(), LatexCalicoCatEntity.createAttributes().build());
        event.put(PROTOGEN.get(), ProtogenEntity.createAttributes().build());
    }

}
