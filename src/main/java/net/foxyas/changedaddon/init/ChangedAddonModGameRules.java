
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.foxyas.changedaddon.init;

import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.GameRules;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonModGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> FIGHTTOKEEPCONSCIOUSNESS = GameRules.register("fighttokeepconsciousness", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> CHANGED_ADDON_DUCT_MECANIC = GameRules.register("changedAddonDuctMecanic", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> GIVE_BOOK_ON_START = GameRules.register("giveBookOnStart", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> DOLATEXINFECTION = GameRules.register("dolatexinfection", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> PAINITE_GENERATION = GameRules.register("painiteGeneration", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.IntegerValue> CHANGED_ADDON_HARD_MODE_BOSSES = GameRules.register("changedAddonHardModeBosses", GameRules.Category.MOBS, GameRules.IntegerValue.create(0));
	public static final GameRules.Key<GameRules.BooleanValue> DO_DAZED_LATEX_BURN = GameRules.register("doDazedLatexBurn", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> NEED_PERMITION_FOR_009_TRANSFUR = GameRules.register("needPermitionFor009Transfur", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
	public static final GameRules.Key<GameRules.IntegerValue> DO_DARK_LATEX_MASK_TRANSFUR = GameRules.register("doDarkLatexMaskTransfur", GameRules.Category.PLAYER, GameRules.IntegerValue.create(0));
	public static final GameRules.Key<GameRules.BooleanValue> CHANGED_ADDON_CREATURE_DIETS = GameRules.register("changedAddonCreatureDiets", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
}
