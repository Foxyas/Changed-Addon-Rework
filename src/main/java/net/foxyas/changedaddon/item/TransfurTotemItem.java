
package net.foxyas.changedaddon.item;

import net.foxyas.changedaddon.configuration.ChangedAddonServerConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonTabs;
import net.foxyas.changedaddon.procedures.SummonDripParticlesProcedure;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class TransfurTotemItem extends Item {

    public TransfurTotemItem() {
        super(new Item.Properties().tab(ChangedAddonTabs.TAB_CHANGED_ADDON).stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BLOCK;
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack) {
        return new ItemStack(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        String form = itemstack.getOrCreateTag().getString("form");
        if (form.isEmpty()) {
            return false;
        } else if (form.startsWith("changed:form")) {
            return true;
        }

        return form.startsWith("changed_addon:form") && ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() == true;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(level, player, hand);
        ItemStack stack = ar.getObject();

        boolean isValidUse = (player.getOffhandItem().is(stack.getItem()) && (player.getMainHandItem().is(stack.getItem())))
                || (player.getOffhandItem().is(stack.getItem()) && player.getMainHandItem().isEmpty())
                || player.getMainHandItem().is(stack.getItem());

        if (!isValidUse) return ar;

        String form = stack.getOrCreateTag().getString("form");
        boolean isTransfurred = ProcessTransfur.isPlayerTransfurred(player);

        if (player.isShiftKeyDown()) {
            if (!form.isEmpty()) {
                stack.getOrCreateTag().putString("form", "");
                activateVisuals(level, player, stack, null, 50, "block.beacon.deactivate");
                return ar;
            }

            if (isTransfurred) tryLinkForm(level, player, stack);
            return ar;
        }

        if (form.isEmpty()) {
            player.displayClientMessage(new TextComponent("No form linked, please link one with §e<Shift+Click>"), true);
            return ar;
        }

        if (isTransfurred) {
            SummonDripParticlesProcedure.execute(player);
            PlayerUtil.UnTransfurPlayer(player);
            cooldown(player, stack, 100);
            visualActivate(level, player, "changed_addon:untransfursound");
            grantAdvancement(player, "changed_addon:transfur_totem_advancement_1");
            return ar;
        }

        if (form.equals("changed_addon:form_puro_kind/female")) {
            form = "changed_addon:form_latex_puro_kind/female";
            stack.getOrCreateTag().putString("form", form);
        }

        if (stack.getOrCreateTag().contains("TransfurVariantData")) {
            CompoundTag data = stack.getOrCreateTag().getCompound("TransfurVariantData");
            PlayerUtil.TransfurPlayerAndLoadData(player, form, data, 0.85f);
            // 0.85f to avoid issues with the transfur animation and because is design choice
        } else {
            PlayerUtil.TransfurPlayer(player, form, 0.85f);
        }

        activateVisuals(level, player, stack, "changed_addon:transfur_totem_advancement_1", 100, null);


        return ar;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemstack, @NotNull Level level, @NotNull Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, level, entity, slot, selected);

        if (!(entity instanceof Player player)) return;

        /*if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_puro_kind/female")) {
            itemstack.getOrCreateTag().putString("form", "changed_addon:form_latex_puro_kind/female");
        }
        if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_snow_leopard/male_organic")) {
            itemstack.getOrCreateTag().putString("form", "changed_addon:form_biosynth_snow_leopard/male");
        }
        if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_snow_leopard/female_organic")) {
            itemstack.getOrCreateTag().putString("form", "changed_addon:form_biosynth_snow_leopard/female");
        }
        if ((itemstack.getOrCreateTag().getString("form")).equals("changed_addon:form_exp_6")) {
            itemstack.getOrCreateTag().putString("form", "changed_addon:form_exp6");
        }*/

        if (!player.getMainHandItem().is(itemstack.getItem()) && !player.getOffhandItem().is(itemstack.getItem()))
            return;

        if (player.getCooldowns().isOnCooldown(itemstack.getItem()) || !ProcessTransfur.isPlayerTransfurred(player)
                || !ProcessTransfur.getPlayerTransfurVariant(player).is(ChangedTransfurVariants.LATEX_BENIGN_WOLF.get()))
            return;

        SummonDripParticlesProcedure.execute(entity);
        PlayerUtil.UnTransfurPlayer(entity);

        level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.totem.use")), SoundSource.NEUTRAL, 1, 1);

        player.getCooldowns().addCooldown(itemstack.getItem(), 100);
        if (level.isClientSide()) Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);

        if (entity instanceof ServerPlayer _player) {
            player.displayClientMessage(new TextComponent("The totem you were carrying has been activated"), true);

            Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:transfur_totem_advancement_2"));
            AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
            if (!_ap.isDone()) {
                for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
            }
        }
    }

    private static void tryLinkForm(Level level, Player player, ItemStack itemstack) {
        TransfurVariantInstance<?> tf = ProcessTransfur.getPlayerTransfurVariant(player);
        ResourceLocation latexFormRes = tf == null ? null : tf.getFormId();
        if (latexFormRes == null) return;

        String latexForm = latexFormRes.toString();

        if (ChangedAddonServerConfiguration.ACCEPT_ALL_VARIANTS.get() || latexForm.startsWith("changed:form")) {
            linkForm(level, player, itemstack, tf, latexForm);
        } else if (latexForm.startsWith("changed_addon:form")) {
            cooldown(player, itemstack, 50);
            visualActivate(level, player, "entity.zombie.attack_iron_door");
            player.displayClientMessage(new TranslatableComponent("changed_addon.latex_totem.notvalid"), true);
        } else if (latexForm.startsWith("changed:special")) {
            linkForm(level, player, itemstack, tf, "changed:form_light_latex_wolf");
        }
    }

    private static void linkForm(Level level, Player player, ItemStack stack, TransfurVariantInstance<?> tf, String form) {
        stack.getOrCreateTag().putString("form", form);
        stack.getOrCreateTag().put("TransfurVariantData", tf.save());
        activateVisuals(level, player, stack, null, 100, "block.beacon.activate");
    }

    private static void cooldown(Player entity, ItemStack itemstack, int ticks) {
        if (!entity.getAbilities().instabuild) {
            entity.getCooldowns().addCooldown(itemstack.getItem(), ticks);
        }
    }

    private static void activateVisuals(Level level, Player entity, ItemStack itemstack, String advancement, int cooldown, String soundId) {
        if (level.isClientSide())
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);

        cooldown(entity, itemstack, cooldown);
        if (soundId != null) visualActivate(level, entity, soundId);

        if (advancement != null)
            grantAdvancement(entity, advancement);
    }

    private static void visualActivate(Level level, Player player, String soundId) {
        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundId));
        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundSource.NEUTRAL, 1, 1);
    }

    private static void grantAdvancement(Entity entity, String id) {
        if (!(entity instanceof ServerPlayer player)) return;

        Advancement adv = player.server.getAdvancements().getAdvancement(new ResourceLocation(id));
        if (adv == null) return;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(adv);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(adv, criterion);
            }
        }
    }
}
