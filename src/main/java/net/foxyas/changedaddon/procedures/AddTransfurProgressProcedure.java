package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.Random;

public class AddTransfurProgressProcedure {
	public static void add(Entity entity,float amount) {
		if (entity == null) {
			return;
		}
		if (entity instanceof Player player){
			ProcessTransfur.progressPlayerTransfur(player,amount, ChangedTransfurVariants.FALLBACK_VARIANT.get(), TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		}
	}

	public static void set(Entity entity, float Amount){
		if (entity == null){
			return;
		}
		if (entity instanceof Player _entity) {
			ProcessTransfur.setPlayerTransfurProgress((Player) entity, Amount);
		}

	}

	public static void setAdd(Entity entity, float Amount){
		if (entity == null){
			return;
		}

		if (entity instanceof Player _entity) {
			float transfurProgress = ProcessTransfur.getPlayerTransfurProgress(_entity) + Amount;
			ProcessTransfur.setPlayerTransfurProgress((Player) entity, transfurProgress);
		}

	}

	public static void set(Entity entity, double Amount){
		if (entity == null){
			return;
		}
		float FAmount = (float) Amount;
		if (entity instanceof Player _entity) {
			ProcessTransfur.setPlayerTransfurProgress((Player) entity,(float) Amount);
		}

	}

	public static void minus(Entity entity,float amount) {
		if (entity == null) {
			return;
		}
		float negative = -amount;
		if (entity instanceof Player player){
				ProcessTransfur.progressTransfur(player,negative, null,TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		}
	}

	public static void minus(Entity entity,double amount) {
		if (entity == null) {
			return;
		}
		float negative = (float) -amount;
		if (entity instanceof Player player){
			ProcessTransfur.progressTransfur(player,negative, null,TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		}
	}

	public static void setminus(Entity entity, float Amount){
		if (entity == null){
			return;
		}
		float negative = -Amount;
		if (entity instanceof Player _entity) {
			float Progress = ProcessTransfur.getPlayerTransfurProgress(_entity);
			float transfurProgress = Progress + negative;
			ProcessTransfur.setPlayerTransfurProgress((Player) entity, transfurProgress);
		}

	}

	public static void setminus(Entity entity, double Amount){
		if (entity == null){
			return;
		}
		float negative = (float) -Amount;
		if (entity instanceof Player _entity) {
			float Progress = ProcessTransfur.getPlayerTransfurProgress(_entity);
			float transfurProgress = Progress + negative;
			ProcessTransfur.setPlayerTransfurProgress((Player) entity, transfurProgress);
		}

	}

	public static void Exp009LatexDmg(Entity entity,double amountB) {
		if (entity == null) {
			return;
		}
		if (entity instanceof Player player) {
			float amount = (float) amountB + ProcessTransfur.getPlayerTransfurProgress(player);
			if (ProcessTransfur.getPlayerTransfurProgress(player) <= Objects.requireNonNull(player.getAttributes().getInstance(ChangedAttributes.TRANSFUR_TOLERANCE.get())).getBaseValue() * 0.75) {
				ProcessTransfur.setPlayerTransfurProgress((Player) entity, amount);
			} else {
				ProcessTransfur.progressTransfur(player,(float) amountB,ChangedTransfurVariants.WHITE_LATEX_WOLF_MALE.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}
	}

	public static void addRed(Entity entity,float amount) {
		if (entity == null) {
			return;
		}

		if (entity instanceof Player player){
			if(player.getLevel().random.nextInt(10) > 5) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.CRYSTAL_WOLF.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.CRYSTAL_WOLF_HORNED.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

		if (entity instanceof LivingEntity player){
			if(player.getLevel().random.nextInt(10) > 5) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.CRYSTAL_WOLF.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.CRYSTAL_WOLF_HORNED.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

	}

	public static void addBlack(Entity entity,float amount) {
		if (entity == null) {
			return;
		}

		if (entity instanceof Player player){
			if(player.getLevel().random.nextInt(6) <= 1) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getRandomVariant(new Random()),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 2)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_DRAGON.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 3)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PUP.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 4)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_YUFENG.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 5)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

		if (entity instanceof LivingEntity player){
			if(player.getLevel().random.nextInt(6) <= 1) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getRandomVariant(new Random()),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 2)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_DRAGON.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 3)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PUP.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 4)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_YUFENG.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(6) == 5)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

	}

	public static void addDarkLatex(Entity entity,float amount) {
		if (entity == null) {
			return;
		}

		if (entity instanceof Player player){
			if(player.getLevel().random.nextInt(3) == 1) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getRandomVariant(new Random()),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(3) == 2)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

		if (entity instanceof LivingEntity player){
			if(player.getLevel().random.nextInt(3) == 1) {
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.Gendered.DARK_LATEX_WOLVES.getRandomVariant(new Random()),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			} else if ((player.getLevel().random.nextInt(3) == 2)){
				ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.DARK_LATEX_WOLF_PARTIAL.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
			}
		}

	}


	public static void addGreen(Entity entity,float amount) {
		if (entity == null) {
			return;
		}

		if (entity instanceof LivingEntity player){
			ProcessTransfur.progressTransfur(player, amount, ChangedTransfurVariants.BEIFENG.get(),TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));

		}
	}

	public static void SnepsiTransfur(Entity player,boolean keepConscience,int type){
		if (type == 1 ){
        	ProcessTransfur.transfur((LivingEntity) player,player.getLevel(), ChangedAddonTransfurVariants.SNOW_LEOPARD_PARTIAL.get(), keepConscience, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		} else if (type == 2) {
			ProcessTransfur.transfur((LivingEntity) player,player.getLevel(), ChangedAddonTransfurVariants.Gendered.EXP2.getMaleVariant(), keepConscience, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		} else if (type == 3) {
			ProcessTransfur.transfur((LivingEntity) player,player.getLevel(), ChangedAddonTransfurVariants.Gendered.EXP2.getFemaleVariant(), keepConscience, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		} else if (type == 4) {
			ProcessTransfur.transfur((LivingEntity) player,player.getLevel(), ChangedAddonTransfurVariants.EXP6.get(), keepConscience, TransfurContext.hazard(TransfurCause.GRAB_REPLICATE));
		} else if (type == 5) {
			ProcessTransfur.transfur((LivingEntity) player,player.getLevel(), ChangedAddonTransfurVariants.LATEX_SNEP.get(), keepConscience, TransfurContext.hazard(TransfurCause.FACE_HAZARD));
		}


	}
}