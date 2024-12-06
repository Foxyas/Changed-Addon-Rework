package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonClientConfigsConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MUSICPLAYER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SLIMMODEL;
	public static final ForgeConfigSpec.ConfigValue<Boolean> FEMALE_SNEPS_HAIR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MALE_SNEPS_HAIR;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PAT_OVERLAY;
	public static final ForgeConfigSpec.ConfigValue<Double> PAT_OVERLAY_X;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DYNAMIC_PAT_OVERLAY;
	public static final ForgeConfigSpec.ConfigValue<Double> PAT_OVERLAY_Y;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_EXTRA_HAND_FALL_FLY;
	static {
		BUILDER.push("MusicPlayer");
		MUSICPLAYER = BUILDER.comment("allow the music player to play boss themes").define("Music Player", true);
		BUILDER.pop();
		BUILDER.push("Slim Models");
		SLIMMODEL = BUILDER.comment("Turn On the SlimModel Of The Partial Snow Leopard Transfur (NEED RESTART FOR APPLY AND IS DEPRECATED)").define("Slim Models", false);
		BUILDER.pop();
		BUILDER.push("Custom Hair Color");
		FEMALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Female Sneps [BioSynth and Exp2]").define("Females Sneps Custom Hair Color", false);
		MALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Male Sneps [BioSynth and Exp2]").define("Males Sneps Custom Hair Color", false);
		BUILDER.pop();
		BUILDER.push("Overlays");
		PAT_OVERLAY = BUILDER.comment("Set the Pat Overlay On or Off").define("Pat Overlay", true);
		PAT_OVERLAY_X = BUILDER.comment("Set the X pos of the pat overlay.  [ Default  237 ]").define("Pat Overlay X pos", (double) 237);
		DYNAMIC_PAT_OVERLAY = BUILDER.comment("The overlay's position will change depending on the entity's name. [ Default true ]").define("Dynamic Pos Pat Overlay", true);
		PAT_OVERLAY_Y = BUILDER.comment("Set the Y pos of the pat overlay.  [ Default  251 ]").define("Pat Overlay Y pos", (double) 251);
		BUILDER.pop();
		BUILDER.push("Extra Animations");
		SHOW_EXTRA_HAND_FALL_FLY = BUILDER.comment("allow the show of the extra hand while fall fly").define("Show Extra Hand [Fall fly]", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
