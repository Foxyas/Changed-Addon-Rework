package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedaddoncommonConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SOFTEN_ABILITY;
	static {
		BUILDER.push("Latexs Forms Extra Abilities");
		SOFTEN_ABILITY = BUILDER.comment("Add the Soften Ability for all the goeys latexes ").define("Soften Ability", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
