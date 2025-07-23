package net.foxyas.changedaddon.init;

import net.minecraft.world.level.GameRules;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ChangedAddonGameRules {
    public static final GameRules.Key<GameRules.BooleanValue> FIGHT_TO_KEEP_CONSCIOUSNESS = GameRules.register("changed_addon:fightToKeepConsciousness", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.BooleanValue> DO_LATEX_INFECTION = GameRules.register("doLatexInfection", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.BooleanValue> PAINITE_GENERATION = GameRules.register("painiteGeneration", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.IntegerValue> CHANGED_ADDON_HARD_MODE_BOSSES = GameRules.register("changed_addon:bossesDifficultScale", GameRules.Category.MOBS, GameRules.IntegerValue.create(0));
    public static final GameRules.Key<GameRules.BooleanValue> DO_DAZED_LATEX_BURN = GameRules.register("doDazedLatexBurn", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
    public static final GameRules.Key<GameRules.IntegerValue> DO_DARK_LATEX_MASK_TRANSFUR = GameRules.register("doDarkLatexMaskTransfur", GameRules.Category.PLAYER, GameRules.IntegerValue.create(0));
    public static final GameRules.Key<GameRules.BooleanValue> CHANGED_ADDON_CREATURE_DIETS = GameRules.register("changed_addon:doCreatureDiets", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> NEED_PERMISSION_FOR_BOSS_TRANSFUR = GameRules.register("needPermissionForBossTransfur", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
    public static final GameRules.Key<GameRules.BooleanValue> NEED_FULL_SOURCE_TO_SPREAD = GameRules.register("blocksNeedFullSourceToSpread", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
}
