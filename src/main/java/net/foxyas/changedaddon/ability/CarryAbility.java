package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.beast.WhiteLatexCentaur;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class CarryAbility extends SimpleAbility {

    public CarryAbility() {
        super();
    }

    @Override
    public TranslatableComponent getDisplayName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("changed_addon.ability.carry");
    }

    @Override
    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed_addon:textures/screens/carry_ability.png");
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.INSTANT;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        return 5;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return (Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.EXP2.getFemaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.EXP2.getMaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getFemaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.ORGANIC_SNOW_LEOPARD.getMaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getFemaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(ChangedAddonTransfurVariants.Gendered.ADDON_PURO_KIND.getMaleVariant())
                || Objects.requireNonNull(entity.getTransfurVariantInstance()).getParent().is(TagKey.create(ChangedRegistry.TRANSFUR_VARIANT.get().getRegistryKey(), new ResourceLocation("changed_addon:able_to_carry"))))
                && !Spectator(entity.getEntity());
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
        Run(entity.getEntity());
    }

    @Override
    public void onRemove(IAbstractChangedEntity entity) {
        super.onRemove(entity);
        SafeRemove(entity.getEntity());
    }

    public static boolean Spectator(Entity entity) {
        if (entity instanceof Player player1) {
            return player1.isSpectator();
        }
        return true;
    }

    public Entity CarryTarget(Player player){
        return PlayerUtilProcedure.getEntityPlayerLookingAt(player, 4);
    }

    private void Run(Entity mainEntity) {
        if (mainEntity instanceof Player player) {
            // If the player is already carrying someone, make them dismount
            if (player.getFirstPassenger() != null && player.isShiftKeyDown()) {
                Entity localEntity = player.getFirstPassenger();
                if (localEntity instanceof Player playerEntity){
                    playerEntity.stopRiding();
                    syncMount(player);
                    syncMount(playerEntity);
                } else {
                    player.getFirstPassenger().stopRiding();
                    syncMount(player);
                }
                return;
            } else if (player.getFirstPassenger() != null && !player.isShiftKeyDown()) {
                Entity localEntity = player.getFirstPassenger();
                if (localEntity instanceof Player playerEntity){
                    playerEntity.stopRiding();
                    syncMount(player);
                    syncMount(playerEntity);
                    if (!player.level.isClientSide()){
                        playerEntity.setDeltaMovement(player.getLookAngle().scale(1.05));
                    }
                } else {
                localEntity.stopRiding();
                syncMount(player);
                localEntity.setDeltaMovement(player.getLookAngle().scale(1.05));
                localEntity.hasImpulse = true;
                    if (!player.level.isClientSide()) {
                        ((ServerLevel) player.level).getChunkSource().broadcast(localEntity, new ClientboundSetEntityMotionPacket(localEntity));
                    }
                }
                return;
            }

            // If the player is a spectator, do nothing
            if (player.isSpectator()) {
                return;
            }

            // Get the entity the player is looking at
            Entity carryTarget = this.CarryTarget(player);

            // If no entity is being looked at, exit
            if (carryTarget == null) {
                return;
            }

            // If the looked at entity is a player
            if (carryTarget instanceof Player carryPlayer) {
                // Creative players cannot be carried by non-creative players
                if (carryPlayer.isCreative() && !player.isCreative()) {
                    return;
                }

                // Check if the player has the light latex centaur variant and prevent them from being carried
                if (ProcessTransfur.getPlayerTransfurVariant(carryPlayer) != null && ProcessTransfur.getPlayerTransfurVariant(carryPlayer).is(ChangedTransfurVariants.WHITE_LATEX_CENTAUR.get())) {
                    player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry", carryPlayer.getDisplayName()), true);
                    return;
                }

                // Start carrying the player
                if (carryPlayer.startRiding(player, true)) {
                    // Synchronize mount with clients
                    syncMount(player);
                    syncMount(carryPlayer);
                }

            } else {
                // If the looked at entity is a light latex centaur, prevent them from being carried
                if (carryTarget instanceof WhiteLatexCentaur) {
                    player.displayClientMessage(new TranslatableComponent("changedaddon.warn.cant_carry", carryTarget.getDisplayName()), true);
                    return;
                }

                // If the looked at entity is in the humanoid tag or can be carried
                if (carryTarget.getType().is(ChangedTags.EntityTypes.HUMANOIDS) || carryTarget.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("changed_addon:can_carry")))) {
                    if (carryTarget.startRiding(player, true)) {
                        // Synchronize mount with clients
                        syncMount(player);
                    }
                }
            }
        }
    }

    public static void SoundPlay(Player player) {
        // Play the sound on the server side to ensure it is properly synchronized
        player.level.playSound(null, player.blockPosition(), ChangedSounds.BOW2, SoundSource.PLAYERS, 2.5f, 1.0f);
    }

    private static void syncMount(Player player) {
        // Send the mount synchronization packet to all tracking players
        if (!player.level.isClientSide) {
            ((ServerLevel) player.level).getChunkSource().broadcast(player, new ClientboundSetPassengersPacket(player));
            SoundPlay(player); // Play the sound on the server for correct synchronization
        }
    }


    public static void SafeRemove(Entity MainEntity) {
        if (MainEntity.getFirstPassenger() != null) {
            MainEntity.getFirstPassenger().stopRiding();
        }
    }
}
