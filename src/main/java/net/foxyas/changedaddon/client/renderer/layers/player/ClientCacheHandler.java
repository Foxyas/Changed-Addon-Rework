package net.foxyas.changedaddon.client.renderer.layers.player;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.LatexInfection;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientCacheHandler {

    // Cache centralizado acessível pela Render Layer
    public static final Map<Player, ChangedEntity> ENTITY_CACHE = new WeakHashMap<>();

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        // Executa apenas na fase END e quando houver um mundo carregado
        if (event.phase != TickEvent.Phase.END) return;
        
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.isPaused()) return;

        // Itera sobre todos os jogadores carregados no cliente
        for (Player player : mc.level.players()) {
            ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(player);
            LatexInfection latexInfection = playerVariables.latexInfection;

            // Se o jogador não tiver infecção ativa, remove do cache se existir
            if (!latexInfection.isActive()) {
                ENTITY_CACHE.remove(player);
                continue;
            }

            TransfurVariant<?> variant = latexInfection.getInfectionVariant();
            if (variant == null) {
                ENTITY_CACHE.remove(player);
                continue;
            }

            // Atualiza ou cria a entidade no cache
            ChangedEntity entity = ENTITY_CACHE.compute(player, (p, existing) -> {
                if (existing == null || existing.getSelfVariant() != variant) {
                    ChangedEntity newEntity = variant.getEntityType().create(mc.level);
                    if (newEntity != null) {
                        newEntity.setNoAi(true);
                    }
                    return newEntity;
                }
                return existing;
            });

            if (entity != null) {
                // Sincroniza e avança o tick da variante/entidade no Client Tick
                TransfurVariantInstance.syncEntityAndPlayer(entity, player);
                entity.variantTick(mc.level);
            }
        }
    }
}