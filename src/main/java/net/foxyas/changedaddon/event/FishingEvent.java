package net.foxyas.changedaddon.event;

import net.foxyas.changedaddon.init.ChangedAddonEnchantments;
import net.ltxprogrammer.changed.entity.beast.*;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class FishingEvent {

    private static final Random RANDOM = new Random();

    public static LivingEntity getRandomEntity(List<LivingEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null; // Retorna null se a lista for vazia ou nula
        }
        int randomIndex = RANDOM.nextInt(entities.size());
        return entities.get(randomIndex);
    }

    public static List<LivingEntity> entityList(ServerLevel serverLevel) {
        return List.of(
                new LatexTigerShark(ChangedEntities.LATEX_TIGER_SHARK.get(), serverLevel),
                new LatexSquidDogMale(ChangedEntities.LATEX_SQUID_DOG_MALE.get(), serverLevel),
                new LatexSquidDogFemale(ChangedEntities.LATEX_SQUID_DOG_FEMALE.get(), serverLevel),
                new BuffLatexSharkMale(ChangedEntities.LATEX_SHARK_MALE.get(), serverLevel),
                new BuffLatexSharkFemale(ChangedEntities.LATEX_SHARK_FEMALE.get(), serverLevel),
                new LatexMantaRayMale(ChangedEntities.LATEX_MANTA_RAY_MALE.get(), serverLevel),
                new LatexMantaRayFemale(ChangedEntities.LATEX_MANTA_RAY_FEMALE.get(), serverLevel),
                new LatexShark(ChangedEntities.LATEX_SHARK.get(), serverLevel),
                new LatexMermaidShark(ChangedEntities.LATEX_MERMAID_SHARK.get(), serverLevel),
                new LatexCrocodile(ChangedEntities.LATEX_CROCODILE.get(), serverLevel),
                new LatexOrca(ChangedEntities.LATEX_ORCA.get(), serverLevel)
        );
    }

    public static void AddAdvancement(Player entity) {
        if (entity == null)
            return;
        if (entity instanceof ServerPlayer serverPlayer) {
            Advancement advancement = serverPlayer.server.getAdvancements().getAdvancement(ResourceLocation.parse("changed_addon:big_one"));
            assert advancement != null;
            AdvancementProgress advancementProgress = serverPlayer.getAdvancements().getOrStartProgress(advancement);
            if (!advancementProgress.isDone()) {
                for (String s : advancementProgress.getRemainingCriteria())
                    serverPlayer.getAdvancements().award(advancement, s);
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerFishItem(ItemFishedEvent event) {
        if (event != null && event.getEntity() != null) {
            Player player = event.getEntity();
            LevelAccessor world = player.level();
            FishingHook hookEntity = event.getHookEntity();

            // Obtem o item em uso do jogador
            ItemStack itemStack = player.getMainHandItem();
            if (!(itemStack.getItem() instanceof FishingRodItem)) {
                itemStack = player.getOffhandItem();
            }

            // Verifica se o item é uma vara de pesca
            if (!(itemStack.getItem() instanceof FishingRodItem)) {
                return; // Nenhuma vara de pesca encontrada, encerra o Method
            }

            // Obtém o nível do encantamento "Changed Lure" na vara
            float itemEnchantment = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonEnchantments.CHANGED_LURE.get(), itemStack);

            // Verifica se o item possui o encantamento "Changed Lure"
            if (itemEnchantment > 0) {
                // Cálculo da chance de spawnar uma criatura "Changed"
                float luck;
                if (player.getAttribute(Attributes.LUCK) != null) {
                    luck = (float) player.getAttributeValue(Attributes.LUCK);
                } else {
                    luck = 0;
                }
                float attributeBonus = Math.min(luck, 35.0F); // Aplica o cap no valor de Luck
                float enchantmentBonus = (2.5F * itemEnchantment) + attributeBonus;
                float chance = 7.5F + enchantmentBonus; // Aumenta a chance de spawn com base no Luck e no encantamento

                // Verifica se a chance de spawnar a entidade é suficiente
                if (player.getRandom().nextFloat() * 100 <= chance) {
                    if (world instanceof ServerLevel _level) {
                        // Cria a entidade
                        Entity entityToSpawn = getRandomEntity(entityList(_level));
                        if (entityToSpawn != null) {
                            entityToSpawn.moveTo(hookEntity.getX(), hookEntity.getY(), hookEntity.getZ(), 0, 0);
                            entityToSpawn.lookAt(EntityAnchorArgument.Anchor.FEET, hookEntity.getEyePosition(0));
                            entityToSpawn.setYBodyRot(entityToSpawn.getYHeadRot());

                            // Define a movimentação da entidade para se aproximar do jogador
                            entityToSpawn.setDeltaMovement(
                                    (player.getX() - hookEntity.getX()) * 0.15,
                                    (player.getY() - hookEntity.getY()) * 0.15,
                                    (player.getZ() - hookEntity.getZ()) * 0.15
                            );

                            // Finaliza o spawn da entidade
                            if (entityToSpawn instanceof Mob _mobToSpawn) {
                                ForgeEventFactory.onFinalizeSpawn(_mobToSpawn, _level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                            }

                            // Adiciona a entidade ao mundo
                            world.addFreshEntity(entityToSpawn);
                            AddAdvancement(player); // Adiciona uma conquista ao jogador
                            event.damageRodBy(1);  // Diminui a durabilidade da vara de pesca

                            // Cancela o evento de pesca para evitar o item normal
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }

}
