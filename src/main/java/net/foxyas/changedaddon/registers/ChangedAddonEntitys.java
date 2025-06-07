package net.foxyas.changedaddon.registers;

import net.foxyas.changedaddon.entity.SnowLeopardPartialEntity;
import net.foxyas.changedaddon.entity.projectile.ParticleProjectile;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonEntitys extends ChangedAddonModEntities {
    public static final RegistryObject<EntityType<SnowLeopardPartialEntity>> SNOW_LEOPARD_PARTIAL = register("latex_snow_leopard_partial",
            EntityType.Builder.<SnowLeopardPartialEntity>of(SnowLeopardPartialEntity::new, MobCategory.MONSTER)
					.setShouldReceiveVelocityUpdates(true)
					.setTrackingRange(64).setUpdateInterval(3)
					.setCustomClientFactory(SnowLeopardPartialEntity::new)

                    .sized(0.7f, 1.93f));

	public static final RegistryObject<EntityType<ParticleProjectile>> PARTICLE_PROJECTILE = register("particle_projectile",
			EntityType.Builder.<ParticleProjectile>of(ParticleProjectile::new, MobCategory.MONSTER)
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
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(SNOW_LEOPARD_PARTIAL.get(), SnowLeopardPartialEntity.createAttributes().build());
	}

}
