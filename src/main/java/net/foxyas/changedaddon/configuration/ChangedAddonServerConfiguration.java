package net.foxyas.changedaddon.configuration;

import net.foxyas.changedaddon.world.gamerules.ChangedEntitySpawnDressedType;
import net.ltxprogrammer.changed.data.RegistryElementPredicate;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ChangedAddonServerConfiguration {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ACCEPT_ALL_VARIANTS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUFFS;
    public static final ForgeConfigSpec.ConfigValue<Double> AGE_NEED;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALWAYS_INFECT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DL_COAT_AFFECT_ALL;
    public static final ForgeConfigSpec.ConfigValue<ChangedEntitySpawnDressedType> CHANGED_SPAWN_DRESS_MODE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALLOW_SECOND_ABILITY_USE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALLOW_RESPAWN_AS_TRANSFUR;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ALLOWED_RESPAWN_TRANSFURS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALLOW_PLAYERS_TO_SELECT_RESPAWN_TRANSFUR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> APPLY_UNTRANSFUR_IMMUNITY_AFTER_RESPAWN_AS_TRANSFUR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALLOW_TRANSFURED_PLAYERS_TO_RESPAWN_WAS_TRANSFUR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE;
    public static final ForgeConfigSpec.ConfigValue<Double> ALPHA_SPAWN_PEACEFUL;
    public static final ForgeConfigSpec.ConfigValue<Double> ALPHA_SPAWN_EASY;
    public static final ForgeConfigSpec.ConfigValue<Double> ALPHA_SPAWN_NORMAL;
    public static final ForgeConfigSpec.ConfigValue<Double> ALPHA_SPAWN_HARD;
    public static final ForgeConfigSpec.ConfigValue<Double> ALPHA_SPAWN_HARDCORE;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("Latex Totem");
        ACCEPT_ALL_VARIANTS = BUILDER.comment("allow latex totem to have all Latex Variants").define("No Latex Totem Limitations", true);
        BUILDER.pop();
        BUILDER.push("Creatures Diets");
        DEBUFFS = BUILDER.comment("Add Debuffs when eat a non good food for your kind").define("When Eat Food Debuffs", false);
        AGE_NEED = BUILDER.comment("Set Amount of Transfur Age is need to not get debuffs when eat a food that is not of your diet").define("Age Need", (double) 15000);
        BUILDER.pop();
        BUILDER.push("Latex Infection");
        ALWAYS_INFECT = BUILDER.comment("Always Add Latex Infection").define("Always Cause Infect", false);
        BUILDER.pop();
        BUILDER.push("Beasts Behavior");
        DL_COAT_AFFECT_ALL = BUILDER.comment("When active, the Dark Latex Coat will affect all beasts").define("DL Coat Confuse All Creatures", true);
        BUILDER.push("Changed Entities");

        BUILDER.push("Alpha Spawn Difficulty Scaling");
        ALPHA_SPAWN_PEACEFUL = BUILDER
                .comment("Chance for an Alpha to spawn on Peaceful difficulty (0.0 - 1.0)")
                .defineInRange("Alpha Spawn Chance Peaceful", 0.0D, 0.0D, 1.0D);
        ALPHA_SPAWN_EASY = BUILDER
                .comment("Chance for an Alpha to spawn on Easy difficulty (0.0 - 1.0)")
                .defineInRange("Alpha Spawn Chance Easy", 0.000025f, 0.0D, 1.0D);
        ALPHA_SPAWN_NORMAL = BUILDER
                .comment("Chance for an Alpha to spawn on Normal difficulty (0.0 - 1.0)")
                .defineInRange("Alpha Spawn Chance Normal", 0.00025f, 0.0D, 1.0D);
        ALPHA_SPAWN_HARD = BUILDER
                .comment("Chance for an Alpha to spawn on Hard difficulty (0.0 - 1.0)")
                .defineInRange("Alpha Spawn Chance Hard", 0.0025f, 0.0D, 1.0D);
        ALPHA_SPAWN_HARDCORE = BUILDER
                .comment("Chance for an Alpha to spawn on HardCore difficulty (0.0 - 1.0)")
                .defineInRange("Alpha Spawn Chance HardCore", 0.025f, 0.0D, 1.0D);
        BUILDER.pop();

        CHANGED_SPAWN_DRESS_MODE = BUILDER
                .comment("Defines how Changed entities spawn with clothing.")
                .worldRestart()
                .defineEnum("Changed Entities Dress Mode", ChangedEntitySpawnDressedType.ANY);
        BUILDER.pop();
        BUILDER.pop();
        BUILDER.push("Player Handle");
        ALLOW_SECOND_ABILITY_USE = BUILDER.comment("Allow the Player to use the second selected ability (similar to offhand and main hand)").define("Allow Second Ability use", false);

        BUILDER.push("Chat"); {
            TRANSFURED_PLAYERS_CHAT_IN_LATEX_LANGUAGE = BUILDER.comment("The Chat of Transfured Players Should be affect by Latex Language?").define("Transfured Players Chat in Latex Language", false);
        } BUILDER.pop();

        BUILDER.push("Respawn As Transfur");{
            ALLOW_RESPAWN_AS_TRANSFUR = BUILDER.comment("Allow the Player to respawn as a transfured entity").define("Allow Respawn as Transfur", false);
            ALLOWED_RESPAWN_TRANSFURS = BUILDER.comment("List of form ids, transfur variant tags or mod ids.\n(@modid, #tag:id, formId)").defineList("allowed Respawn Transfur Variants", List.of("changed:random"), RegistryElementPredicate::isValidSyntax);
            ALLOW_PLAYERS_TO_SELECT_RESPAWN_TRANSFUR = BUILDER.comment("Allow the non admins Players to select a transfur to be transfured when spawning").define("Allow Players to Select Respawn Transfur", false);
            APPLY_UNTRANSFUR_IMMUNITY_AFTER_RESPAWN_AS_TRANSFUR = BUILDER.comment("Apply Untransfur Immunity to the player after they respawn as a transfured player").define("Apply Untransfur Immunity After Respawn as a Transfur", false);
            ALLOW_TRANSFURED_PLAYERS_TO_RESPAWN_WAS_TRANSFUR = BUILDER.comment("Allow a already transfured player to respawn as another transfur").define("Allow transfured players to respawn as another transfur", false);
        }BUILDER.pop();


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
