package net.foxyas.changedaddon.configuration;

import net.foxyas.changedaddon.process.sounds.BossMusicHandler;
import net.minecraftforge.common.ForgeConfigSpec;

public class ChangedAddonClientConfiguration {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> MUSIC_PLAYER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> FEMALE_SNEPS_HAIR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> MALE_SNEPS_HAIR;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PAT_OVERLAY;
    public static final ForgeConfigSpec.ConfigValue<Double> PAT_OVERLAY_X;
    public static final ForgeConfigSpec.ConfigValue<Double> PAT_OVERLAY_Y;
    public static final ForgeConfigSpec.ConfigValue<Boolean> WING_FLAP_INFO;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GRAB_ABILITY_KEY_INFO;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PAW_STYLE_PAT_OVERLAY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_LATEX_INFECTION_ICONS_OUTSIDE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_EXTRA_HAND;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SMOOTH_LASER_MOVEMENT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> PLANTOIDS_VISIBILITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ALPHA_COMPATIBILITY_MODE_RENDER;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DYNAMIC_ALPHA_CHECKER;
    public static final ForgeConfigSpec.ConfigValue<BossMusicHandler.FollowType> BOSS_MUSIC_LOCATION_TYPE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SUIT_ANIM;

    static {
        ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        BUILDER.push("MusicPlayer");
        MUSIC_PLAYER = BUILDER.comment("allow the music player to play boss themes").define("Music Player", true);
        BOSS_MUSIC_LOCATION_TYPE = BUILDER.comment("define which entity the boss music will follow.").defineEnum("Boss Music Location Type", BossMusicHandler.FollowType.BOSS);
        BUILDER.pop();

        BUILDER.push("Alpha textures");
        DYNAMIC_ALPHA_CHECKER = BUILDER.comment("Enables the dynamic alpha texture detector. This allows the mod to automatically identify generic alpha textures, but may cause performance drops (lag) during the render of alpha entities.").define("Enable dynamic alpha texture detector", true);
        BUILDER.pop();

        BUILDER.push("Custom Hair Color");
        FEMALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Female Sneps [BioSynth and Exp2]").define("Females Sneps Custom Hair Color", false);
        MALE_SNEPS_HAIR = BUILDER.comment("Set The Custom Hair Color For the Male Sneps [BioSynth and Exp2]").define("Males Sneps Custom Hair Color", false);
        BUILDER.pop();

        BUILDER.push("Overlays");
        PAT_OVERLAY = BUILDER.comment("Set the Pat Overlay On or Off").define("Pat Overlay", true);
        PAT_OVERLAY_X = BUILDER.comment("Set the X pos of the pat overlay.  \n[Default 12]").define("Pat Overlay X pos", (double) 12);
        PAT_OVERLAY_Y = BUILDER.comment("Set the Y pos of the pat overlay.  \n[Default 72]").define("Pat Overlay Y pos", (double) 72);
        WING_FLAP_INFO = BUILDER.comment("Display How much Ticks You have Hold the Wing Flap Ability").define("Wing Flap Ability Ticks Info", false);
        GRAB_ABILITY_KEY_INFO = BUILDER.comment("Display which key is the correct to be pressed in the moment").define("Grab ability key info", false);
        PAW_STYLE_PAT_OVERLAY = BUILDER.comment("Make the pat overlay use a paw icon instead of text").define("Paw Style Pat Overlay", true);
        RENDER_LATEX_INFECTION_ICONS_OUTSIDE = BUILDER.comment("Should the render of latex infection icons be outside the main transfur progress").define("Render Latex Infection Icons Outside", true);
        BUILDER.pop();

        BUILDER.push("Extra Animations");
        SHOW_EXTRA_HAND = BUILDER.comment("allow the show of the extra hand in some contexts like fall fly").define("Show Extra Hand", false);
        SMOOTH_LASER_MOVEMENT = BUILDER.comment("Make the Laser Moviment be smooth, it may cause the particule to be slower").define("Laser Smooth Moviment", false);
        BUILDER.pop();

        BUILDER.push("ModelsHandle");
        PLANTOIDS_VISIBILITY = BUILDER.comment("Turn off the Plantoids [Female Chest Features]").define("Turn Off the Plantoids", false);
        ALPHA_COMPATIBILITY_MODE_RENDER = BUILDER.comment("Turn the Compatibility Mode Render For Alpha Scales, turning this off may add some performance").define("Alpha Compatibility Render Mode", true);
        BUILDER.pop();

        SUIT_ANIM = BUILDER.comment("Switches on/off the animation where held entity is rendered closer to grabber based on suit progress.")
                .define("Suit Anim", true);

        SPEC = BUILDER.build();
    }
}
