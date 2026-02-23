package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.item.api.ColorHolder;
import net.ltxprogrammer.changed.block.Computer;
import net.ltxprogrammer.changed.block.KeypadBlock;
import net.ltxprogrammer.changed.block.entity.KeypadBlockEntity;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeycardItem extends Item implements ColorHolder {

    private static final String TOP_COLOR = "TopColor";
    private static final int DEF_TOP = 0xFFFFFF;
    private static final String BOTTOM_COLOR = "BottomColor";
    private static final int DEF_BOTTOM = 0xFF0000;

    public KeycardItem(Properties pProperties) {
        super(pProperties);
    }

    public KeycardItem() {
        super(new Properties().stacksTo(1));  //.tab(ChangedAddonTabs.CHANGED_ADDON_MAIN_TAB)
    }

    public static void setCode(ItemStack stack, byte @Nullable [] code) {
        if (code == null) {
            removeCode(stack);
            return;
        }

        CompoundTag tag = stack.getOrCreateTag();
        tag.putByteArray("StoredCode", code);
    }

    public static void removeCode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("StoredCode")) tag.remove("StoredCode");
    }

    public static byte @Nullable [] getCode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("StoredCode") ? tag.getByteArray("StoredCode") : null;
    }

    public static boolean hasTopColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TOP_COLOR) && tag.getInt(TOP_COLOR) != DEF_TOP;
    }

    public static int getTopColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(TOP_COLOR) ? tag.getInt(TOP_COLOR) : DEF_TOP; // vermelho
    }

    public static void setTopColor(ItemStack stack, int color) {
        stack.getOrCreateTag().putInt(TOP_COLOR, color);
    }

    public static boolean hasBottomColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(BOTTOM_COLOR) && tag.getInt(BOTTOM_COLOR) != DEF_BOTTOM;
    }

    public static int getBottomColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(BOTTOM_COLOR) ? tag.getInt(BOTTOM_COLOR) : DEF_BOTTOM; // branco
    }

    public static void setBottomColor(ItemStack stack, int color) {
        stack.getOrCreateTag().putInt(BOTTOM_COLOR, color);
    }

    private static void playLock(Level level, BlockPos pos) {
        playSound(level, pos, ChangedSounds.KEYPAD_LOCK, 1.0F, 1.0F);
    }

    private static void playWrite(Level level, BlockPos pos) {
        playSound(level, pos, ChangedSounds.KEYPAD_UNLOCK_SUCCESS, 1.0F, 1.0F);
    }

    private static void playUnlock(Level level, BlockPos pos) {
        playSound(level, pos, ChangedSounds.RETINAL_SCAN, 1.0F, 1.0F);
    }

    private static void playSound(Level level, BlockPos worldPosition, RegistryObject<SoundEvent> event, float volume, float pitch) {
        if (level.getServer() != null) {
            if (level instanceof ServerLevel serverLevel) {
                ChangedSounds.broadcastSound(serverLevel, event, worldPosition, volume, pitch);
            }
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level world, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        byte[] code = getCode(stack);
        if (code != null) {
            tooltip.add(Component.translatable("item.changed_addon.keycard.desc.code", Arrays.toString(code))
                    .withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("item.changed_addon.keycard.desc.nocode")
                    .withStyle(ChatFormatting.RED));
        }

        if (flag.isAdvanced()) {
            if (getTopColor(stack) != DEF_TOP) {
                tooltip.add((Component.translatable("item.changed_addon.keycard.desc.color_top.data", String.format("#%06X", getTopColor(stack)))).withStyle(ChatFormatting.GRAY));
            }
            if (getBottomColor(stack) != DEF_BOTTOM) {
                tooltip.add((Component.translatable("item.changed_addon.keycard.desc.color_bottom.data", String.format("#%06X", getBottomColor(stack)))).withStyle(ChatFormatting.GRAY));
            }
        } else {
            if (getTopColor(stack) != DEF_TOP) {
                tooltip.add((Component.translatable("item.changed_addon.keycard.desc.color_top")).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            if (getBottomColor(stack) != DEF_BOTTOM) {
                tooltip.add((Component.translatable("item.changed_addon.keycard.desc.color_bottom")).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        InteractionHand hand = context.getHand();

        // Try to clean the stored Keypad code from the keycard
        if (blockState.getBlock() instanceof Computer) {
            return handleComputerFeature(stack, level, player, pos, hand);
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!(blockState.getBlock() instanceof KeypadBlock) ||
                !(blockEntity instanceof KeypadBlockEntity keypadBlockEntity))
            return InteractionResult.PASS;

        // Try to store the code from the keypad to the keycard
        byte[] itemCode = getCode(stack);
        boolean clientSide = level.isClientSide();
        if (itemCode == null) {
            if (!blockState.getValue(KeypadBlock.POWERED)) {
                return InteractionResult.PASS;
            }

            if (clientSide) return player.isShiftKeyDown() ? InteractionResult.SUCCESS : InteractionResult.PASS;

            if (player.isShiftKeyDown() && keypadBlockEntity.code != null) {
                setCode(stack, keypadBlockEntity.code);
                playWrite(level, pos);
                player.swing(hand, true);
                return InteractionResult.sidedSuccess(false);
            }
            return InteractionResult.PASS;
        }

        // Agora insere o código automaticamente no keypad
        if (blockState.getValue(KeypadBlock.POWERED)) {
            return InteractionResult.PASS;
        }

        if (clientSide) return InteractionResult.SUCCESS;

        // Converte o código para lista de bytes (se o keypad usar isso)
        List<Byte> codeList = new ArrayList<>();
        for (byte b : itemCode)
            codeList.add(b);

        boolean fail = false;
        keypadBlockEntity.useCode(codeList);
        if (codeList.size() != keypadBlockEntity.code.length) {
            fail = true;
        }

        for (int idx = 0; idx < keypadBlockEntity.code.length; ++idx) {
            if (codeList.get(idx) != keypadBlockEntity.code[idx]) {
                fail = true;
                break;
            }
        }

        MutableComponent chatComponent = !fail ?
                Component.translatable("item.changed_addon.keycard.message.used.success").withStyle(ChatFormatting.GREEN) :
                Component.translatable("item.changed_addon.keycard.message.used.fail").withStyle(ChatFormatting.RED);

        if (!fail) playUnlock(level, pos);
        player.displayClientMessage(chatComponent, true);
        player.swing(hand);

        return InteractionResult.sidedSuccess(false);
    }

    public @NotNull InteractionResult handleComputerFeature(ItemStack stack, Level level, Player player, BlockPos pos, InteractionHand hand) {
        byte[] itemCode = getCode(stack);
        boolean clientSide = level.isClientSide();

        // Determine the opposite hand
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack otherStack = player.getItemInHand(otherHand);

        // --- This keycard has no code ---
        if (itemCode == null) return InteractionResult.PASS;

        // --- If this keycard already has a code ---
        // SHIFT action: clear the code
        if (clientSide) return player.isShiftKeyDown()
                || (otherStack.isEmpty() && otherStack.getItem() instanceof KeycardItem && getCode(otherStack) != null) ?
                InteractionResult.SUCCESS : InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            setCode(stack, null);
            playWrite(level, pos);
            player.swing(hand, true);
            return InteractionResult.sidedSuccess(false);
        }

        // --- OTHER LOGIC: copy the code to the offhand keycard ---
        if (!otherStack.isEmpty() && otherStack.getItem() instanceof KeycardItem) {

            byte[] otherCode = getCode(otherStack);

            // Only copy if the offhand keycard has no code
            if (otherCode == null) {
                setCode(otherStack, itemCode.clone()); // Cloning to avoid sharing the same array
                playWrite(level, pos);
                player.swing(hand, true);
                return InteractionResult.sidedSuccess(false);
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void registerCustomColors(RegisterColorHandlersEvent.Item itemColors, RegistryObject<Item> item) {
        itemColors.register(
                (stack, tintIndex) -> {
                    /*
                     * tintIndex 0 = layer0 (base/letters part)
                     * tintIndex 1 = layer1 (top)
                     * tintIndex 2 = layer2 (bottom)
                     */

                    if (tintIndex == 2) {
                        // Cor da parte de baixo
                        return getBottomColor(stack);
                    } else if (tintIndex == 1) {
                        // Cor da parte de cima
                        return getTopColor(stack);
                    }

                    return -1; // fallback default
                },
                item.get()
        );
    }
}
