package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonClientConfigsConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MUSICPLAYER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SLIMMODEL;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FEMALE_SNEPS_HAIR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MALE_SNEPS_HAIR;
	static {
		BUILDER.push("MusicPlayer");
		MUSICPLAYER = BUILDER.comment("allow the music player to play boss themes").define("Music Player", true);
		BUILDER.pop();
		BUILDER.push("Slim Models");
		SLIMMODEL = BUILDER.comment("Turn On the SlimModel Of The Partial Snow Leoapard Transfur (NEED RESTART FOR APPLY)").define("Slim Models", false);
		BUILDER.pop();
		BUILDER.push("Custom Hair Color");
		FEMALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Female Sneps [BioSynth and Exp2]").define("Females Sneps Custom Hair Color", false);
		MALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Male Sneps [BioSynth and Exp2]").define("Males Sneps Custom Hair Color", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
