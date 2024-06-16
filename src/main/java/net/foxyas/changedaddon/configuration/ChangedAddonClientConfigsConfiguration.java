package net.foxyas.changedaddon.configuration;

import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonClientConfigsConfiguration {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Boolean> MUSICPLAYER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> SLIMMODEL;
	static {
		BUILDER.push("MusicPlayer");
		MUSICPLAYER = BUILDER.comment("allow the music player to play boss themes").define("Music Player", true);
		BUILDER.pop();
		BUILDER.push("SlimModels");
		SLIMMODEL = BUILDER.define("Slim Models", false);
		BUILDER.pop();

		SPEC = BUILDER.build();
	}

}
