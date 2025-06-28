package net.foxyas.changedaddon.process.variantsExtraStats;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber
public class FormsDialogues {

	private static final List<UUID> YTNames = List.of(
			UUID.fromString("e61e6f3e-4820-4bca-816f-ba5fd7fdf529"),
			UUID.fromString("c1136b82-915d-49b9-b468-e717d371dc1e"),
			UUID.fromString("520f7606-1276-46b1-be2d-b307cc6eddd7"),
			UUID.fromString("f0358d36-d4b5-4aa9-aac6-e9b62bf55a03"),
			UUID.fromString("145f75e9-2636-4c96-99cd-0dbd0973a1d0"));

	private static final List<UUID> FBNames = List.of(
			UUID.fromString("66220457-29ea-4093-9389-5c4b571d4bda"));

	
	@SubscribeEvent
	public static void SendDeathTexts(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof Player player){
			Entity attacker = event.getSource().getEntity();
			TransfurVariantInstance<?> PlayerVariant = ProcessTransfur.getPlayerTransfurVariant(player);
			
			final List<TransfurVariant<?>> TRANSFUR_VARIANT_LIST = List.of(
			ChangedAddonTransfurVariants.EXP1_FEMALE.get(),
			ChangedAddonTransfurVariants.EXP1_MALE.get(),
			ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get(),
			ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get(),
			ChangedAddonTransfurVariants.EXP2_MALE.get(),
			ChangedAddonTransfurVariants.EXP2_FEMALE.get());
			
			if (PlayerVariant != null && TRANSFUR_VARIANT_LIST.contains(PlayerVariant.getParent())){
				//ChangedAddonMod.LOGGER.log(level().INFO,"THE EVENT HAPPEN");
				if (player.level().getLevelData().isHardcore()){
					if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get())){
						var randomSelector = player.level().getRandom().nextInt(10);
						if (randomSelector <= 4){ // 4 in 10 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp10.death.text1"),false);
						} else{
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp10.death.text2"),false);
						}
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get())) {
						var randomSelector = player.level().getRandom().nextInt(10);
						if (randomSelector <= 4){ // 4 in 10 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp9.death.text1"),false);
						} else{
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp9.death.text2"),false);
						}
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_MALE.get())
							|| PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())) {
						var randomSelector = player.level().getRandom().nextInt(15);
						if (randomSelector <= 3){ // 3 in 11 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text1"),false);
						} else if (randomSelector <= 6) { //3 in 11 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text2"),false);
						} else if (randomSelector <= 9) { //3 in 11 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text3"),false);
						} else if (randomSelector <= 12) { //3 in 11 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text4",player.getDisplayName().getString()),false);
						} // 2 in 11 for not talk [numbers 10 or 11]
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_MALE.get())
							|| PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_FEMALE.get())) {
						var randomSelector = player.level().getRandom().nextInt(10);
						if (randomSelector <= 4){ // 4 in 10 of chance
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp2.death.text1"),false);
						} else if (randomSelector >= 7) { //3 in 10 of chance [7,6 or 5]
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp2.death.text2"),false);
						} // 3 in 10 for not talk
					}
				} else {
					if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXPERIMENT_10_BOSS.get())){
						var randomSelector = player.level().getRandom().nextInt(100);
						if (randomSelector <= 4){ // 4%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp10.death.text1"),false);
						} else{
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp10.death.text2"),false);
						}
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get())) {
						var randomSelector = player.level().getRandom().nextInt(100);
						if (randomSelector <= 4){ // 4%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp9.death.text1"),false);
						} else{
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp9.death.text2"),false);
						}
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_MALE.get())
							|| PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())) {
						var randomSelector = player.level().getRandom().nextInt(100);
						if (randomSelector <= 3){ // 3%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text1"),false);
						} else if (randomSelector <= 6) { //6%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text2"),false);
						} else if (randomSelector <= 9) { //9%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text3"),false);
						} else if (randomSelector <= 12) { //12%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.death.text4",player.getDisplayName().getString()),false);
						}
					} else if (PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_MALE.get())
							|| PlayerVariant.getParent().is(ChangedAddonTransfurVariants.EXP2_FEMALE.get())) {
						var randomSelector = player.level().getRandom().nextInt(100);
						if (randomSelector <= 4){ //4%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp2.death.text1"),false);
						} else if (randomSelector <= 7) { //7%
							player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp2.death.text2"),false);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void SendTransfurTexts(ProcessTransfur.EntityVariantAssigned.ChangedVariant changedVariantEvent){
		if (changedVariantEvent.newVariant == null){
			return;
		}
		if (changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.KET_EXPERIMENT_009_BOSS_LATEX_VARIANT.get())){
			if (changedVariantEvent.livingEntity instanceof Player player){
				//ChangedAddonMod.LOGGER.log(level().INFO,"THE EVENT HAPPEN");
				if (YTNames.contains(player.getUUID())){
					player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp9.transfur.text.secret"), false);
				}
			}
		}
		if (changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.EXP1_MALE.get()) || changedVariantEvent.newVariant.is(ChangedAddonTransfurVariants.EXP1_FEMALE.get())){
			if (changedVariantEvent.livingEntity instanceof Player player){
				//ChangedAddonMod.LOGGER.log(level().INFO,"THE EVENT HAPPEN");
				if (FBNames.contains(player.getUUID())){
					player.displayClientMessage(Component.translatable("changed_addon.form_dialogues.exp1.transfur.text.secret"), false);
				}
			}
		}
	}
}
