package net.foxyas.changedaddon.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import net.foxyas.changedaddon.network.ChangedAddonModVariables;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;

public class DarklatexpuddleFeatureProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!(entity instanceof ItemEntity || entity instanceof ArmorStand || entity instanceof ItemFrame || entity instanceof GlowItemFrame || entity instanceof ThrownExperienceBottle || entity instanceof ExperienceOrb)) {
			if (!(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).contains("dark_latex")) {
				if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).contains("dark_latex")
						&& !((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).contains("puro_kind")) {
					if (new Object() {
						public double getValue(LevelAccessor world, BlockPos pos, String tag) {
							BlockEntity blockEntity = world.getBlockEntity(pos);
							if (blockEntity != null)
								return blockEntity.getTileData().getDouble(tag);
							return -1;
						}
					}.getValue(world, new BlockPos(x, y, z), "cooldown") <= 0) {
						if (world instanceof ServerLevel _level)
							_level.getServer().getCommands().performCommand(new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, _level, 4, "", new TextComponent(""), _level.getServer(), null).withSuppressedOutput(),
									("playsound " + "changed_addon:warn " + "block " + "@a[nbt={ForgeCaps:{\"changed_addon:player_variables\":{aredarklatex:1b}}}] " + "~ ~ ~ " + 20 + 0 + 1));
						if (!world.isClientSide()) {
							BlockPos _bp = new BlockPos(x, y, z);
							BlockEntity _blockEntity = world.getBlockEntity(_bp);
							BlockState _bs = world.getBlockState(_bp);
							if (_blockEntity != null)
								_blockEntity.getTileData().putDouble("cooldown", 10);
							if (world instanceof Level _level)
								_level.sendBlockUpdated(_bp, _bs, _bs, 3);
						}
					}
				}
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(20 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).collect(Collectors.toList());
			for (Entity entityiterator : _entfound) {
				if (!(entityiterator == entity)) {
					if (!(entity instanceof ItemEntity || entity instanceof ArmorStand || entity instanceof ItemFrame || entity instanceof GlowItemFrame || entity instanceof ThrownExperienceBottle || entity instanceof ExperienceOrb)) {
						if (!((entity.getCapability(ChangedAddonModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new ChangedAddonModVariables.PlayerVariables())).LatexForm).contains("dark_latex")) {
							if (!(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()).contains("dark_latex")) {
								if (entityiterator.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:changed_creature"))) || entityiterator instanceof net.ltxprogrammer.changed.entity.ChangedEntity) {
									if ((ForgeRegistries.ENTITIES.getKey(entityiterator.getType()).toString()).contains("dark_latex")) {
										if (entityiterator instanceof Mob _entity)
											_entity.getNavigation().moveTo(x, y, z, 0.3);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
