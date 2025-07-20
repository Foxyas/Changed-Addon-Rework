package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.GlowItemFrame;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownExperienceBottle;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DarkLatexPuddleFeatureProcedure {

    private static boolean isIgnoredEntity(Entity e) {
        return e instanceof ItemEntity || e instanceof ArmorStand || e instanceof ItemFrame ||
                e instanceof GlowItemFrame || e instanceof ThrownExperienceBottle || e instanceof ExperienceOrb;
    }

    private static boolean isDarkLatexOrPuroKind(Player entity) {
        var variant = ProcessTransfur.getPlayerTransfurVariant(entity);
        if (variant == null) return false;

        String id = variant.getFormId().toString();
        return id.contains("dark_latex") || id.contains("puro_kind");
    }

    private static boolean isEntityDarkLatex(Entity entity) {
        ResourceLocation key = ForgeRegistries.ENTITIES.getKey(entity.getType());
        return key != null && key.toString().contains("dark_latex");
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null || isIgnoredEntity(entity)) return;
        if (!(entity instanceof Player player)) return;

        if (!isEntityDarkLatex(entity) && !isDarkLatexOrPuroKind(player)) {
            BlockPos pos = new BlockPos(x, y, z);

            if (getTileCooldown(world, pos) <= 0) {
                // Reproduz som para dark latex próximos
                if (world instanceof ServerLevel serverLevel) {
                    String command = "playsound changed_addon:warn block @a[nbt={ForgeCaps:{\"changed_addon:player_variables\":{aredarklatex:1b}}}] ~ ~ ~ 20 0 1";
                    CommandSourceStack source = new CommandSourceStack(CommandSource.NULL, new Vec3(x, y, z), Vec2.ZERO, serverLevel, 4, "", new TextComponent(""), serverLevel.getServer(), null).withSuppressedOutput();
                    serverLevel.getServer().getCommands().performCommand(source, command);
                }

                // Aplica cooldown no bloco
                setTileCooldown(world, pos, 10);
            }
        }

        // Atração de dark latex
        Vec3 center = new Vec3(x, y, z);
        AABB area = new AABB(center, center).inflate(10); // raio de 10 blocos
        List<Entity> nearbyEntities = world.getEntitiesOfClass(Entity.class, area, e -> true)
                .stream()
                .filter(e -> e != entity)
                .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center)))
                .collect(Collectors.toList());

        for (Entity nearby : nearbyEntities) {
            if (!isIgnoredEntity(nearby) && !isEntityDarkLatex(entity) && !isDarkLatexOrPuroKind(player)) {
                if (isChangedCreature(nearby) && isEntityDarkLatex(nearby)) {
                    if (nearby instanceof Mob mob)
                        mob.getNavigation().moveTo(x, y, z, 0.3);
                }
            }
        }
    }

    private static boolean isChangedCreature(Entity entity) {
        return entity.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:changed_creature"))) ||
                entity instanceof net.ltxprogrammer.changed.entity.ChangedEntity;
    }

    private static double getTileCooldown(LevelAccessor world, BlockPos pos) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null) return be.getTileData().getDouble("cooldown");
        return -1;
    }

    private static void setTileCooldown(LevelAccessor world, BlockPos pos, double cooldown) {
        BlockEntity be = world.getBlockEntity(pos);
        if (be != null) {
            be.getTileData().putDouble("cooldown", cooldown);
            if (world instanceof Level level) {
                BlockState state = world.getBlockState(pos);
                level.sendBlockUpdated(pos, state, state, 3);
            }
        }
    }
}
