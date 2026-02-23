package net.foxyas.changedaddon.procedure;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.advanced.LatexSnowFoxFoxyasEntity;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mod.EventBusSubscriber
public class SmallTickUpdateProcedure {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Player player = event.player;
        Level level = player.level;
        if (player instanceof ServerPlayer sPlayer) {
            Advancement adv = sPlayer.server.getAdvancements().getAdvancement(ChangedAddonMod.resourceLoc("gooey_friend"));
            AdvancementProgress ap = sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(adv));

            if (!ap.isDone()) {
                final Vec3 center = new Vec3(player.getX(), player.getY(), player.getZ());
                List<LatexSnowFoxFoxyasEntity> entityList = level.getEntitiesOfClass(LatexSnowFoxFoxyasEntity.class, new AABB(center, center).inflate(2), e -> true)
                        .stream().sorted(Comparator.comparingDouble(e -> e.distanceToSqr(center))).toList();

                if (!entityList.isEmpty()) {
                    for (String s : ap.getRemainingCriteria()) sPlayer.getAdvancements().award(adv, s);
                }
            }
        }

        TransfurVariantInstance<?> variant = ProcessTransfur.getPlayerTransfurVariant(player);
        if (variant == null) return;

        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.ofOrDefault(player);
        String id = variant.getFormId().toString();
        boolean darkLatex = id.contains("dark_latex") || id.contains("puro_kind");
        if (vars.areDarkLatex == darkLatex) return;

        vars.areDarkLatex = darkLatex;
        vars.syncPlayerVariables(player);
    }
}
