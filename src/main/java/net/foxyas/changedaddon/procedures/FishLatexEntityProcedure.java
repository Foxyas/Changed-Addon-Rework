package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.ltxprogrammer.changed.entity.beast.*;
import net.ltxprogrammer.changed.init.ChangedEntities;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class FishLatexEntityProcedure {

	private static final Random RANDOM = new Random();

	public static Entity getRandomEntity(List<Entity> entities) {
		if (entities == null || entities.isEmpty()) {
			return null; // Retorna null se a lista for vazia ou nula
		}
		int randomIndex = RANDOM.nextInt(entities.size());
		return entities.get(randomIndex);
	}

	public static List<Entity> entityList(ServerLevel _level) {
		return List.of(
				new LatexTigerShark(ChangedEntities.LATEX_TIGER_SHARK.get(), _level),
				new LatexSquidDogMale(ChangedEntities.LATEX_SQUID_DOG_MALE.get(), _level),
				new LatexSquidDogFemale(ChangedEntities.LATEX_SQUID_DOG_FEMALE.get(), _level),
				new BuffLatexSharkMale(ChangedEntities.LATEX_SHARK_MALE.get(), _level),
				new BuffLatexSharkFemale(ChangedEntities.LATEX_SHARK_FEMALE.get(), _level),
				new LatexMantaRayMale(ChangedEntities.LATEX_MANTA_RAY_MALE.get(), _level),
				new LatexMantaRayFemale(ChangedEntities.LATEX_MANTA_RAY_FEMALE.get(), _level),
				new LatexShark(ChangedEntities.LATEX_SHARK.get(), _level),
				new LatexMermaidShark(ChangedEntities.LATEX_MERMAID_SHARK.get(), _level),
				new LatexCrocodile(ChangedEntities.LATEX_CROCODILE.get(), _level),
				new LatexOrca(ChangedEntities.LATEX_ORCA.get(), _level),
				new Shark(ChangedEntities.SHARK.get(), _level)
		);
	}

	public static void AddAdvancement(Player entity) {
		if (entity == null)
			return;
		if (entity instanceof ServerPlayer _player) {
			Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:bigone"));
			AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
			if (!_ap.isDone()) {
				Iterator _iterator = _ap.getRemainingCriteria().iterator();
				while (_iterator.hasNext())
					_player.getAdvancements().award(_adv, (String) _iterator.next());
			}
		}
	}


	@SubscribeEvent
	public static void onPlayerFishItem(ItemFishedEvent event) {
		if (event != null && event.getPlayer() != null) {
			FishingHook hookEntity = event.getHookEntity();
			Player player = event.getPlayer();
			LevelAccessor world = player.getLevel();
			ItemStack itemStack = event.getPlayer().getUseItem();
			float ItemEnchantment = EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.CHANGED_LURE.get(),itemStack);

			// Verifica se o item de pesca tem o encantamento "Changed Lure"
			if (ItemEnchantment > 0) {

				// Cálculo da chance de spawnar uma criatura "Changed"
				float luck = (float) player.getAttributeValue(Attributes.LUCK);
				float attributeBonus = Math.min(luck, 35.0F); // Aplica o cap no valor de Luck
				float enchantmentBonus = ItemEnchantment + attributeBonus;
				float chance = 5.0F + enchantmentBonus;  // Aumenta a chance de spawn com base no Luck e no encantamento

				// Verifique se a chance de spawnar a entidade é suficiente
				if (player.level.random.nextFloat() * 100 <= chance) {
					if (world instanceof ServerLevel _level) {

						// Crie a entidade
						Entity entityToSpawn = getRandomEntity(entityList(_level));
						if (entityToSpawn != null) {
							entityToSpawn.moveTo(hookEntity.getX(), hookEntity.getY(), hookEntity.getZ(), 0, 0);
							entityToSpawn.setYBodyRot(0);
							entityToSpawn.setYHeadRot(0);

							// Define a movimentação da entidade para se aproximar do jogador
							entityToSpawn.setDeltaMovement(
									(player.getX() - hookEntity.getX()) * 0.15,
									(player.getY() - hookEntity.getY()) * 0.15,
									(player.getZ() - hookEntity.getZ()) * 0.15
							);

							// Finaliza o spawn da entidade
							if (entityToSpawn instanceof Mob _mobToSpawn) {
								_mobToSpawn.finalizeSpawn(_level, world.getCurrentDifficultyAt(entityToSpawn.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
							}

							// Adiciona a entidade ao mundo
							world.addFreshEntity(entityToSpawn);
							AddAdvancement(player);  // Suponho que isso adicione uma conquista ao jogador
							event.damageRodBy(1);  // Diminui a durabilidade da vara de pesca

							// Cancele o evento de pesca para evitar o item normal
							event.setCanceled(true);
						}
					}
				}
			}
		}
	}
}
