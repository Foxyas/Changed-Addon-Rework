package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.effect.particles.ChangedAddonParticles;
import net.foxyas.changedaddon.entity.goals.FollowAndLookAtLaser;
import net.foxyas.changedaddon.init.ChangedAddonModTabs;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.foxyas.changedaddon.process.util.FoxyasUtils.manualRaycastIgnoringBlocks;

public class LaserPointer extends Item implements SpecializedAnimations {

    public static final float MAX_LASER_REACH = 32;

    public static enum DefaultColors {
        RED(new Color(255, 0, 0)),
        GREEN(new Color(0, 255, 0)),
        BLUE(new Color(0, 0, 255)),
        YELLOW(new Color(255, 255, 0)),
        CYAN(new Color(0, 255, 255)),
        MAGENTA(new Color(255, 0, 255)),
        ORANGE(new Color(255, 165, 0)),
        PINK(new Color(255, 105, 180)),
        PURPLE(new Color(128, 0, 128)),
        WHITE(new Color(255, 255, 255)),
        GRAY(new Color(128, 128, 128)),
        LIGHT_GRAY(new Color(211, 211, 211)),
        LIME(new Color(50, 205, 50)),
        BROWN(new Color(139, 69, 19)),
        NAVY(new Color(0, 0, 128));

        public final Color color;

        DefaultColors(Color color) {
            this.color = color;
        }

        // Construtor sem argumentos, caso queira usar valores padrão depois
        DefaultColors() {
            this.color = new Color(255, 255, 255); // fallback: branco
        }

        public Color getColor() {
            return color;
        }

        public int getColorToInt() {
            return color.getRGB();
        }
    }

    public LaserPointer() {
        super(new Properties().stacksTo(1).tab(ChangedAddonModTabs.TAB_CHANGED_ADDON));
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("Color", DefaultColors.RED.getColorToInt()); // Cor padrão vermelha
        return stack;
    }

    public static int getColor(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return stack.getTag().getInt("Color");
        }
        return DefaultColors.RED.getColorToInt(); // Cor padrão se não tiver NBT
    }

    public static Color getColorAsColor(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return new Color(stack.getTag().getInt("Color"));
        }
        return DefaultColors.RED.getColor(); // Cor padrão se não tiver NBT
    }

    public static Color3 getColorAsColor3(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return Color3.fromInt(stack.getTag().getInt("Color"));
        }
        return Color3.fromInt(DefaultColors.RED.getColorToInt()); // Cor padrão se não tiver NBT
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 720000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
        if (this.allowdedIn(tab)) {
            for (DefaultColors color : DefaultColors.values()) {
                ItemStack stack = new ItemStack(this);
                stack.getOrCreateTag().putInt("Color", color.getColorToInt());
                items.add(stack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (flag.isAdvanced()) {
            // Suponha que você tenha salvo os valores RGB no NBT
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("Color")) {
                Color color = new Color(tag.getInt("Color"));
                String hex = getHex(color);
                tooltip.add(new TextComponent("Color: " + hex).withStyle((e) -> e.withColor(TextColor.parseColor(hex))));
            }
        }

    }

    public static String getHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            HitResult result = player.pick(MAX_LASER_REACH, 0.0F, false);
            EntityHitResult entityHitResult = PlayerUtilProcedure.getEntityHitLookingAt(player, LaserPointer.MAX_LASER_REACH);
            Vec3 hitPos = result.getLocation();
            Direction face = Direction.UP; // fallback para quando mirar no ar

            if (entityHitResult != null) {
                face = Direction.getNearest(entityHitResult.getLocation().x, entityHitResult.getLocation().y, entityHitResult.getLocation().z);
                hitPos = applyOffset(entityHitResult.getLocation(), face, -0.05D);
                spawnLaserParticle(level, player, stack, hitPos);
            } else if (result instanceof BlockHitResult blockResult && level.getBlockState(blockResult.getBlockPos()).isAir()) {
                // Mira no ar: define uma posição "alvo" no ar baseada na direção do olhar
                spawnLaserParticle(level, player, stack, hitPos);
            } else if (result instanceof BlockHitResult blockResult &&
                    // Se for translúcido, refazer raycast ignorando blocos
                    level.getBlockState(blockResult.getBlockPos()).is(ChangedTags.Blocks.LASER_TRANSLUCENT)) {

                Set<Block> blockSet = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags())
                        .getTag(ChangedTags.Blocks.LASER_TRANSLUCENT).stream().collect(Collectors.toSet());
                BlockHitResult blockHitResult = manualRaycastIgnoringBlocks(level, player, 64, blockSet);
                hitPos = applyOffset(result.getLocation(), blockHitResult.getDirection(), 0f);
                spawnLaserParticle(level, player, stack, hitPos);

            } else if (result instanceof BlockHitResult blockResult && !level.getBlockState(blockResult.getBlockPos()).is(ChangedTags.Blocks.LASER_TRANSLUCENT)) {
                hitPos = applyOffset(result.getLocation(), blockResult.getDirection(), -0.05D);
                spawnLaserParticle(level, player, stack, hitPos);
            }

            double radius = 16.0; // Raio de busca
            List<LivingEntity> nearbyMobs = level.getEntitiesOfClass(LivingEntity.class, new AABB(hitPos, hitPos).inflate(radius), (e) -> {
                if (e instanceof Mob mob) {
                    return mob.goalSelector.getAvailableGoals().stream().anyMatch(g -> g.getGoal() instanceof FollowAndLookAtLaser);
                }
                return false;
            });

            for (LivingEntity livingEntity : nearbyMobs) {
                for (Goal goal : ((Mob) livingEntity).goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).toList()) {
                    if (goal instanceof FollowAndLookAtLaser followGoal) {
                        followGoal.setLaserTarget(hitPos, player);
                    }
                }
            }

            player.startUsingItem(hand);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }


    @Override
    public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
        super.releaseUsing(itemstack, world, entity, time);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);
        if (!player.getLevel().isClientSide) {
            HitResult result = player.pick(MAX_LASER_REACH, 0.0F, false);
            EntityHitResult entityHitResult = PlayerUtilProcedure.getEntityHitLookingAt(player, LaserPointer.MAX_LASER_REACH);
            Vec3 hitPos = result.getLocation();
            Direction face = Direction.UP; // fallback para quando mirar no ar

            if (entityHitResult != null) {
                face = Direction.getNearest(entityHitResult.getLocation().x, entityHitResult.getLocation().y, entityHitResult.getLocation().z);
                hitPos = applyOffset(entityHitResult.getLocation(), face, -0.05D);
            } else if (result instanceof BlockHitResult blockResult && player.getLevel().getBlockState(blockResult.getBlockPos()).isAir()) {
                // Mira no ar: define uma posição "alvo" no ar baseada na direção do olhar
            } else if (result instanceof BlockHitResult blockResult &&
                    // Se for translúcido, refazer raycast ignorando blocos
                    player.getLevel().getBlockState(blockResult.getBlockPos()).is(ChangedTags.Blocks.LASER_TRANSLUCENT)) {

                Set<Block> blockSet = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags())
                        .getTag(ChangedTags.Blocks.LASER_TRANSLUCENT).stream().collect(Collectors.toSet());
                BlockHitResult blockHitResult = manualRaycastIgnoringBlocks(player.getLevel(), player, 64, blockSet);
                hitPos = applyOffset(result.getLocation(), blockHitResult.getDirection(), -0.05D);

            } else if (result instanceof BlockHitResult blockResult && !player.getLevel().getBlockState(blockResult.getBlockPos()).is(ChangedTags.Blocks.LASER_TRANSLUCENT)) {
                hitPos = applyOffset(result.getLocation(), blockResult.getDirection(), -0.05D);
            }

            double radius = 16.0; // Raio de busca
            List<LivingEntity> nearbyMobs = player.getLevel().getEntitiesOfClass(LivingEntity.class, new AABB(hitPos, hitPos).inflate(radius), (e) -> {
                if (e instanceof Mob mob) {
                    return mob.goalSelector.getAvailableGoals().stream().anyMatch(g -> g.getGoal() instanceof FollowAndLookAtLaser);
                }
                return false;
            });

            for (LivingEntity livingEntity : nearbyMobs) {
                for (Goal goal : ((Mob) livingEntity).goalSelector.getAvailableGoals().stream().map(WrappedGoal::getGoal).toList()) {
                    if (goal instanceof FollowAndLookAtLaser followGoal) {
                        followGoal.setLaserTarget(hitPos, player);
                    }
                }
            }
        }
    }

    // Utilitário para aplicar deslocamento da face atingida
    private Vec3 applyOffset(Vec3 hitPos, Direction face, double offset) {
        return hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );
    }

    // Envia partícula do laser
    private void spawnLaserParticle(Level level, Player player, ItemStack stack, Vec3 pos) {
        PlayerUtilProcedure.ParticlesUtil.sendParticles(
                level,
                ChangedAddonParticles.laserPoint(player, LaserPointer.getColorAsColor3(stack), 1f),
                pos.x, pos.y, pos.z,
                0.0, 0.0, 0.0,
                1, 0
        );
    }


    @Nullable
    @Override
    public AnimationHandler getAnimationHandler() {
        return ANIMATION_CACHE.computeIfAbsent(this, LaserPointer.Animator::new);
    }

    public static class Animator extends AnimationHandler {
        public Animator(Item item) {
            super(item);
        }

        @Override
        public void setupUsingAnimation(ItemStack itemStack, EntityStateContext entity, UpperModelContext model, HumanoidArm arm, float progress) {
            super.setupUsingAnimation(itemStack, entity, model, arm, progress);

            // Sets the correct arm depending on the hand used
            // HumanoidArm ContextArm = entity.livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND ? arm : arm.getOpposite();

            // Sets the arm rotation based on the player's head
            model.getArm(arm).xRot = model.head.xRot - 1.570796f - (entity.livingEntity.isCrouching() ? 0.2617994F : 0.0F);
            model.getArm(arm).yRot = model.head.yRot;

            // Silly animation [Intentionally not smooth due to lack of partial ticks and design]
        }
    }
}
