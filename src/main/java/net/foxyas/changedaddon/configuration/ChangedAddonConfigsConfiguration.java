package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonConfigsConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ACCEPT_ALL_VARIANTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUFFS;
	public static final ForgeConfigSpec.ConfigValue<Double> AGE_NEED;
	public static final ForgeConfigSpec.ConfigValue<Boolean> CUSTOMRECIPES;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ALWAYS_INFECT;
	static {
		BUILDER.push("Latex Totem");
		ACCEPT_ALL_VARIANTS = BUILDER.comment("allow latex totem to have all Latex Variants").define("No Latex Totem Limitations", true);
		BUILDER.pop();
		BUILDER.push("Creatures Diets");
		DEBUFFS = BUILDER.comment("Add Debuffs when eat a non good food for your kind").define("When Eat Food Debuffs", false);
		AGE_NEED = BUILDER.comment("Set Amount of Transfur Age is need to not get debuffs when eat a food that is not of your diet").define("Age Need", (double) 15000);
		BUILDER.pop();
		BUILDER.push("BlocksRecipes");
		CUSTOMRECIPES = BUILDER.comment("Allow Catalyzer and Unifuser Use New Recipe System ").define("Custom Recipes", true);
		BUILDER.pop();
		BUILDER.push("Latex Infection");
		ALWAYS_INFECT = BUILDER.comment("Always Add Latex Infection").define("Always Cause Infect", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
