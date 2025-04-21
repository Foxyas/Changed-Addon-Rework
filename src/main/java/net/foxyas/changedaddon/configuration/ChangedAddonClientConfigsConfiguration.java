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
	public static final ForgeConfigSpec.ConfigValue<Boolean> WING_FLAP_INFO;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PAW_STYLE_PAT_OVERLAY;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_EXTRA_HAND;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SMOOTH_LASER_MOVIMENT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PLANTOIDS_VARIABLE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> DIETS_DISPLAY_INFO;
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
		PAT_OVERLAY_X = BUILDER.comment("Set the X pos of the pat overlay.  \n[Default 12]").define("Pat Overlay X pos", (double) 12);
		DYNAMIC_PAT_OVERLAY = BUILDER.comment("The overlay's position will change depending on the entity's name. \n[Default true]").define("Dynamic Pos Pat Overlay", true);
		PAT_OVERLAY_Y = BUILDER.comment("Set the Y pos of the pat overlay.  \n[Default 72]").define("Pat Overlay Y pos", (double) 72);
		WING_FLAP_INFO = BUILDER.comment("Display How much Ticks You have Hold the Wing Flap Ability").define("Wing Flap Ability Ticks Info", false);
		PAW_STYLE_PAT_OVERLAY = BUILDER.comment("Make the pat overlay use a paw icon instead of text").define("Paw Style Pat Overlay", true);
		BUILDER.pop();
		BUILDER.push("Extra Animations");
		SHOW_EXTRA_HAND = BUILDER.comment("allow the show of the extra hand in some contexts like fall fly").define("Show Extra Hand", false);
		SMOOTH_LASER_MOVIMENT = BUILDER.comment("Make the Laser Moviment be smooth, it may cause the particule to be slower").define("Laser Smooth Moviment", false);
		BUILDER.pop();
		BUILDER.push("ModelsHandle");
		PLANTOIDS_VARIABLE = BUILDER.comment("Turn off the Plantoids [Female Chest Features]").define("Turn Off the Plantoids", false);
		BUILDER.pop();
		BUILDER.push("Textual Info ");
		DIETS_DISPLAY_INFO = BUILDER.comment("Displays a text when the player consumes food favorable to their transformation species").define("Diets Display Info", true);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
