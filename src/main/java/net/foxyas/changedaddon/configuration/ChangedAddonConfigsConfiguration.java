package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonConfigsConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> ACCEPT_ALL_VARIANTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUFFS;
	static {
		BUILDER.push("Latex Totem");
		ACCEPT_ALL_VARIANTS = BUILDER.comment("allow latex totem to have all Latex Variants").define("No Latex Totem Limitations", true);
		BUILDER.pop();
		BUILDER.push("Creatures Diets");
		DEBUFFS = BUILDER.comment("Add Debuffs when eat a non good food for your kind").define("When Eat Food Debuffs", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
