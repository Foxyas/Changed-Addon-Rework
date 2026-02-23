package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.entity.ai.goals.simple.FollowAndLookAtLaser;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.item.api.ColorHolder;
import net.foxyas.changedaddon.item.api.DynamicCreativeTab;
import net.foxyas.changedaddon.util.DynamicClipContext;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.item.SpecializedAnimations;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class LaserPointerItem extends Item implements SpecializedAnimations, ColorHolder, DynamicCreativeTab {

    public static final float MAX_LASER_REACH = 32;
    public static final int FOLLOW_LASER_RADIUS = 16;
    public static final int FOLLOW_BB_SIZE = FOLLOW_LASER_RADIUS * 2;
    public static final ClipContext.ShapeGetter IGNORE_TRANSLUCENT = (state, b, pos, context) -> {
        if(state.is(ChangedTags.Blocks.LASER_TRANSLUCENT)) return Shapes.empty();
        return ClipContext.Block.COLLIDER.get(state, b, pos, context);
    };

    public LaserPointerItem() {
        super(new Properties().stacksTo(1)//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                );
    }

    public static int getColor(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("Color")) {
            return stack.getTag().getInt("Color");
        }
        return DefaultColors.RED.getRGB(); // Cor padrão se não tiver NBT
    }

    public static Color getAWTColor(ItemStack stack) {
        int color = getColor(stack);
        return color == DefaultColors.RED.getRGB() ? DefaultColors.RED.color : new Color(color);
    }

    public static Color3 getColorAsColor3(ItemStack stack) {
        return Color3.fromInt(getColor(stack)); // Cor padrão se não tiver NBT
    }

    public static String getHex(Color color) {
        return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static void setLaserColor(ItemStack stack, Color color) {
        setLaserColor(stack, color.getRGB());
    }

    public static void setLaserColor(ItemStack stack, int color) {
        if(!(stack.getItem() instanceof LaserPointerItem)) return;

        stack.getOrCreateTag().putInt("Color", color);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("Color", DefaultColors.RED.getRGB()); // Cor padrão vermelha
        return stack;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 720000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.NONE;
    }


    @Override
    public void fillItemCategory(@NotNull CreativeModeTab.Output tab) {
        for (DefaultColors color : DefaultColors.values()) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("Color", color.getRGB());
            tab.accept(stack);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if(!flag.isAdvanced()) return;

        // Suponha que você tenha salvo os valores RGB no NBT
        CompoundTag tag = stack.getOrCreateTag();
        if(!tag.contains("Color")) return;

        Color color = new Color(tag.getInt("Color"));
        String hex = getHex(color);
        tooltip.add(Component.translatable("item.changed_addon.laser_pointer.tooltip",hex).withStyle((e) -> e.withColor(TextColor.parseColor(hex))));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if(level.isClientSide) return InteractionResultHolder.pass(stack);

        Vec3 eyePos = player.getEyePosition();
        HitResult result = level.clip(new DynamicClipContext(eyePos, eyePos.add(player.getViewVector(1).scale(MAX_LASER_REACH)),
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(player))
        );

        EntityHitResult entityHitResult = PlayerUtil.getEntityHitLookingAt(player, result.getType() != HitResult.Type.MISS
                ? (float) result.distanceTo(player)
                : LaserPointerItem.MAX_LASER_REACH, null);
        Vec3 hitPos;

        if (entityHitResult != null) {
            Direction face = Direction.getNearest(entityHitResult.getLocation().x, entityHitResult.getLocation().y, entityHitResult.getLocation().z);
            hitPos = applyOffset(entityHitResult.getLocation(), face, -0.05f);
            spawnLaserParticle(level, player, stack, hitPos);
        } else if (result instanceof BlockHitResult blockResult) {
            if(level.getBlockState(blockResult.getBlockPos()).isAir()) {
                spawnLaserParticle(level, player, stack, blockResult.getLocation());
                return InteractionResultHolder.pass(stack);
            }

            hitPos = applyOffset(result.getLocation(), blockResult.getDirection(), -0.05f);
            spawnLaserParticle(level, player, stack, hitPos);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity player, ItemStack pStack, int count) {
        super.onUseTick(pLevel, player, pStack, count);


        Level level = player.level;
        if(player.level.isClientSide || count % 20 != 0) return;

        Vec3 eyePos = player.getEyePosition();
        HitResult result = level.clip(new DynamicClipContext(eyePos, eyePos.add(player.getViewVector(1).scale(MAX_LASER_REACH)),
                IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(player))
        );

        EntityHitResult entityHitResult = PlayerUtil.getEntityHitLookingAt(player, result.getType() != HitResult.Type.MISS
                ? (float) result.distanceTo(player)
                : LaserPointerItem.MAX_LASER_REACH, null);

        Vec3 hitPos = result.getLocation();

        if (entityHitResult != null) {
            Direction face = Direction.getNearest(entityHitResult.getLocation().x, entityHitResult.getLocation().y, entityHitResult.getLocation().z);
            hitPos = applyOffset(entityHitResult.getLocation(), face, -0.05f);
        } else if (result instanceof BlockHitResult blockResult) {
            if(level.getBlockState(blockResult.getBlockPos()).isAir()) return;

            hitPos = applyOffset(result.getLocation(), blockResult.getDirection(), -0.05f);
        }

        List<Mob> nearbyMobs = level.getEntitiesOfClass(Mob.class, AABB.ofSize(hitPos, FOLLOW_BB_SIZE, FOLLOW_BB_SIZE, FOLLOW_BB_SIZE));

        for (Mob mob : nearbyMobs) {
            for (WrappedGoal wrapped : mob.goalSelector.getAvailableGoals()) {
                if (!(wrapped.getGoal() instanceof FollowAndLookAtLaser followGoal)) continue;

                followGoal.setLaserTarget(hitPos, player);
                break;
            }
        }
    }

    // Utilitário para aplicar deslocamento da face atingida
    private Vec3 applyOffset(Vec3 hitPos, Direction face, float offset) {
        return hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );
    }

    // Envia partícula do laser
    private void spawnLaserParticle(Level level, Player player, ItemStack stack, Vec3 pos) {
        ParticlesUtil.sendParticles(
                level,
                ChangedAddonParticleTypes.laserPoint(player, LaserPointerItem.getAWTColor(stack)),
                pos.x, pos.y, pos.z,
                0.0, 0.0, 0.0,
                1, 0
        );
    }

    @Nullable
    @Override
    public AnimationHandler getAnimationHandler() {
        return ANIMATION_CACHE.computeIfAbsent(this, LaserPointerItem.Animator::new);
    }

    @Override
    public void registerCustomColors(RegisterColorHandlersEvent.Item event, RegistryObject<Item> item) {
        if (item.get() instanceof LaserPointerItem) {
            event.register((stack, tintIndex) -> {
                if (tintIndex == 0) { // Só aplica a cor no layer certo
                    if (LaserPointerItem.getAWTColor(stack) == null) {
                        return -1; // Cor padrão (branco)
                    }
                    return LaserPointerItem.getAWTColor(stack).getRGB();
                }
                return -1; // Cor padrão (branco)
            }, item.get());
        }
    }


    public enum DefaultColors {
        RED(Color.RED),
        GREEN(Color.GREEN),
        BLUE(Color.BLUE),
        YELLOW(Color.YELLOW),
        CYAN(Color.CYAN),
        MAGENTA(Color.MAGENTA),
        ORANGE(new Color(255, 165, 0)),
        PINK(new Color(255, 105, 180)),
        PURPLE(new Color(128, 0, 128)),
        WHITE(Color.WHITE),
        GRAY(Color.GRAY),
        LIGHT_GRAY(new Color(211, 211, 211)),
        LIME(new Color(50, 205, 50)),
        BROWN(new Color(139, 69, 19)),
        NAVY(new Color(0, 0, 128));

        public final Color color;

        DefaultColors(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public int getRGB() {
            return color.getRGB();
        }
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
            model.getArm(arm).xRot = model.head.xRot - Mth.HALF_PI - (entity.livingEntity.isCrouching() ? 15 * Mth.DEG_TO_RAD : 0.0F);
            model.getArm(arm).yRot = model.head.yRot;

            // Silly animation [Intentionally not smooth due to lack of partial ticks and design]
        }
    }
}
