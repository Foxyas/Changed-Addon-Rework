package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.init.ChangedAddonBlocks;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.util.RenderUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SignalCatcherItem extends Item {

    private static final int LARGE_SEARCH_RADIUS = 121;
    private static final int SMALL_SEARCH_RADIUS = 33;
    private static final int MAX_FOUND_BLOCKS = 10;

    public SignalCatcherItem() {
        super(new Properties()//.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
                .stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 15;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player entity, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        entity.startUsingItem(hand);
        return ar;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (!isSignalCatcherInHand(entity)) return stack;

        Player player = (Player) entity;
        if (!player.getCooldowns().isOnCooldown(ChangedAddonItems.SIGNAL_CATCHER.get())) {
            int radius = player.isShiftKeyDown() ? LARGE_SEARCH_RADIUS : SMALL_SEARCH_RADIUS;
            int cooldown = player.isShiftKeyDown() ? 225 : 75;
            searchSignalBlockUsingChunks(level, player, stack, radius, cooldown);
        }

        return stack;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemstack, @NotNull Level world, @NotNull LivingEntity entity, int time) {
        if (!itemstack.getOrCreateTag().getBoolean("set")) {
            if (entity instanceof Player player && !player.level.isClientSide())
                player.displayClientMessage(Component.literal("§o§bNo Location Found §l[Not Close Enough]"), false);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag pIsAdvanced) {
        @Nullable Player player = Util.make(() -> Minecraft.getInstance().player);
        if (player == null) return;

        CompoundTag tag = stack.getOrCreateTag();
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        double deltaX = x - player.getX();
        double deltaY = y - player.getY();
        double deltaZ = z - player.getZ();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.literal("Hold §6<Shift>§r for Info"));
        } else {
            tooltip.add(Component.literal("Hold §b<Right Click>§r to scan a 32 block area"));
            tooltip.add(Component.literal("Hold §c<Shift + Right Click>§r to perform a Super scan and scan 120 block area"));
        }
        tooltip.add(Component.literal(("§oCoords §l" + x + " " + y + " " + z)));
        if (stack.getOrCreateTag().getBoolean("set")) {
            tooltip.add(Component.literal(("§oDistance §l" + Math.round(distance))));
        }
    }

    private static boolean isSignalCatcherInHand(LivingEntity entity) {
        return entity.getMainHandItem().is(ChangedAddonItems.SIGNAL_CATCHER.get()) || entity.getOffhandItem().is(ChangedAddonItems.SIGNAL_CATCHER.get());
    }

    private static void searchSignalBlockUsingChunks(Level level, Player player, ItemStack itemstack, int radius, int cooldown) {
        List<BlockPos> foundPositions = new ArrayList<>();
        int chunkRadius = (radius >> 4) + 1; // Raio em chunks (16 blocos por chunk)
        int x, y, z;
        x = player.getBlockX();
        y = player.getBlockY();
        z = player.getBlockZ();

        int chunkX = x >> 4;
        int chunkZ = z >> 4;

        for (int cx = chunkX - chunkRadius; cx <= chunkX + chunkRadius; cx++) {
            for (int cz = chunkZ - chunkRadius; cz <= chunkZ + chunkRadius; cz++) {
                if (foundPositions.size() >= MAX_FOUND_BLOCKS) break;

                LevelChunk chunk = level.getChunk(cx, cz);
                chunk.getBlockEntities().forEach((BlockPos, entity) -> {
                    // Verifique se a posição está no raio
                    if (entity.getBlockState().getBlock() == ChangedAddonBlocks.SIGNAL_BLOCK.get() &&
                            BlockPos.distSqr(new Vec3i(x, y, z)) <= radius * radius) {
                        foundPositions.add(BlockPos);
                    }
                });
            }
        }

        // Resultado da busca
        if (!foundPositions.isEmpty()) {
            BlockPos firstFound = foundPositions.get(0);
            updatePlayerState(player, itemstack, firstFound.getX(), firstFound.getY(), firstFound.getZ(), cooldown);
            if (player.level.isClientSide()) {
                displayFoundLocations(player, foundPositions);
            }

            level.playSound(null, firstFound, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 1.5f, 1);
            level.playSound(null, player, SoundEvents.CONDUIT_ACTIVATE, SoundSource.PLAYERS, 1.5f, 1);
        } else if (!player.level.isClientSide()) {
            player.displayClientMessage(Component.literal("No Signal Block Found"), false);
        }
    }

    private static void updatePlayerState(Player player, ItemStack itemstack, int x, int y, int z, int cooldown) {
        itemstack.getOrCreateTag().putDouble("x", x);
        itemstack.getOrCreateTag().putDouble("y", y);
        itemstack.getOrCreateTag().putDouble("z", z);
        itemstack.getOrCreateTag().putBoolean("set", true);
        player.getCooldowns().addCooldown(itemstack.getItem(), cooldown);
    }

    private static void displayFoundLocations(Player player, List<BlockPos> positions) {
        boolean isCreative = player.isCreative();

        player.displayClientMessage(Component.literal("Signal Blocks found at:"), false); // Mensagem inicial

        for (int i = 0; i < positions.size(); i++) {
            BlockPos pos = positions.get(i);

            // Cria o texto básico da posição
            String positionText = String.format("Block %d: [%d, %d, %d]", i + 1, pos.getX(), pos.getY(), pos.getZ());
            MutableComponent message = Component.literal(positionText);

            if (isCreative) {
                // Adiciona eventos ao texto apenas para jogadores criativos
                Style style = Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ())))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Click to copy the teleport command")));

                message.withStyle(style);
            }

            // Envia cada linha individualmente
            player.displayClientMessage(message, false);
        }
    }

    @Nullable
    public static BlockPos getTargetFromItem(Player player) {
        ItemStack stack = player.getMainHandItem().is(ChangedAddonItems.SIGNAL_CATCHER.get())
                ? player.getMainHandItem()
                : player.getOffhandItem();

        if (!stack.is(ChangedAddonItems.SIGNAL_CATCHER.get())) return null;

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.getBoolean("set")) return null;

        return BlockPos.containing(new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")));
    }

    @Nullable
    public static BlockPos getTargetFromItem(ItemStack stack) {
        if (!stack.is(ChangedAddonItems.SIGNAL_CATCHER.get())) return null;

        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.getBoolean("set")) return null;

        return BlockPos.containing(new Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z")));
    }

    /* ========================================================= */
    /* ===================== CLIENT EVENTS ===================== */
    /* ========================================================= */

    @OnlyIn(Dist.CLIENT)
    @Mod.EventBusSubscriber(Dist.CLIENT)
    public static class ClientEvents {

        @SubscribeEvent
        public static void onRenderLevel(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
                return;

            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            LocalPlayer player = mc.player;
            Vec3 camPos = mc.gameRenderer.getMainCamera().getPosition();

            if (level == null || player == null)
                return;

            ItemStack stack = getSignalCatcher(player);
            if (stack == null)
                return;

            BlockPos target = getTargetFromItem(stack);
            if (target == null)
                return;

            // ================= Shadow Entity =================
            PathfinderMob shadowMob = createShadowMob(player, level);
            if (shadowMob == null)
                return;

            shadowMob.setPos(player.getX(), player.getY(), player.getZ());

            PathNavigation navigation = getNavigation(player, shadowMob, level);
            Path path = navigation.createPath(target, 0);

            if (path == null || path.getNodeCount() <= 1)
                return;

            // ================= Render Path =================
            RenderUtil.renderPathAsLine(event.getPoseStack(), camPos, path);
        }

        private static @NotNull PathNavigation getNavigation(LocalPlayer player, PathfinderMob shadowMob, ClientLevel level) {
            if (player.getAbilities().flying || player.isFallFlying() || !player.onGround()) {
                FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(shadowMob, level);
                flyingPathNavigation.setCanOpenDoors(true);
                flyingPathNavigation.setCanPassDoors(true);
                return flyingPathNavigation;
            }

            if (shadowMob instanceof ChangedEntity changedEntity && (player.getAbilities().flying || player.isFallFlying() || !player.onGround())) {
                FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(changedEntity, level);
                flyingPathNavigation.setCanOpenDoors(true);
                flyingPathNavigation.setCanPassDoors(true);
                return flyingPathNavigation;
            } else if (shadowMob instanceof ChangedEntity changedEntity) {
                return changedEntity.getNavigation();
            }

            FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(shadowMob, level);
            flyingPathNavigation.setCanOpenDoors(true);
            flyingPathNavigation.setCanPassDoors(true);
            return flyingPathNavigation;
        }

        /* --------------------------------------------------------- */

        private static ItemStack getSignalCatcher(LocalPlayer player) {
            if (player.getMainHandItem().is(ChangedAddonItems.SIGNAL_CATCHER.get()))
                return player.getMainHandItem();

            if (player.getOffhandItem().is(ChangedAddonItems.SIGNAL_CATCHER.get()))
                return player.getOffhandItem();

            return null;
        }

        private static PathfinderMob createShadowMob(LocalPlayer player, Level level) {
            ChangedEntity changedEntity = ProcessTransfur.getPlayerTransfurVariantSafe(player).map(TransfurVariantInstance::getChangedEntity).orElse(null);
            return changedEntity == null ? ChangedEntities.GAS_WOLF_MALE.get().create(level) : changedEntity;
        }
    }
}
