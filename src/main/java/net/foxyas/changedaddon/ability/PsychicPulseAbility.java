package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.IAbstractLatex;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PsychicPulseAbility extends SimpleAbility {

	@Override
	public TranslatableComponent getDisplayName(IAbstractLatex entity) {
		return new TranslatableComponent("changed_addon.ability.psychic_pulse");
	}

	@Override
	public ResourceLocation getTexture(IAbstractLatex entity) {
		return new ResourceLocation("changed_addon:textures/screens/psychic_pulse.png"); //Place holder
	}

	@Override
	public boolean canUse(IAbstractLatex entity) {
		return true;
	}


	public UseType getUseType(IAbstractLatex entity) {
		return UseType.INSTANT;
	}


	@Override
	public void startUsing(IAbstractLatex entity) {
		super.startUsing(entity);
		execute(entity.getLevel(),entity.getEntity());
	}

	@Override
	public void tick(IAbstractLatex entity) {
		super.tick(entity);
		//execute(entity.getLevel(),entity.getEntity());
	}


	public static void execute(LevelAccessor world, Entity iAbstractLatex) {
		if (iAbstractLatex == null)
			return;
		if (iAbstractLatex instanceof Player entity) {
			{
				final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center)))
						.collect(Collectors.toList());
				for (Entity entityiterator : _entfound) {
					if (entityiterator instanceof FallingBlockEntity || entityiterator.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("minecraft:impact_projectiles")))) {
						Vec3 NegativeMotion = new Vec3((-(entityiterator.getDeltaMovement().x())), (-(entityiterator.getDeltaMovement().y())), (-(entityiterator.getDeltaMovement().z())));
						Vec3 Motion = NegativeMotion.multiply(1.25,1.25,1.25);
						entityiterator.setDeltaMovement(Motion);
					}
				}
			}
		}
	}
}
