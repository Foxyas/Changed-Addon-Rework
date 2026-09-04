package net.foxyas.changedaddon.variant;

import com.google.common.collect.Sets;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.foxyas.changedaddon.variant.TransfurSoundsDetails.TransfurSoundType.getSoundTypes;

public class TransfurSoundsDetails {

    public static void tryPlay(Player player, TransfurSoundAction action, boolean warnPlayer) {

        if (!action.canUse(player)) {
            if (warnPlayer) {
                player.sendSystemMessage(
                        Component.literal("Your current form can't do that.")
                );
            }
            return;
        }

        if (ChangedAddonVariables.nonNullOf(player).isActInCooldown()) {
            if (warnPlayer) {
                player.sendSystemMessage(
                        Component.literal("You're too exhausted to do that.")
                );
            }
            return;
        }

        action.playAndApplyCooldown(player);
    }

    private static float getPitch(Player player, TransfurSoundAction action) {
        return switch (action) {
            case YIP, CHATTER -> Mth.nextFloat(player.getRandom(), 1.2f, 1.6f);
            case FOX_SCREAM -> 0.8f;
            default -> 1.0f;
        };
    }

    public enum TransfurSoundType {

        CAT(PlayerUtil::isCatTransfur, TransfurSoundAction.MEOW, TransfurSoundAction.PURREOW, TransfurSoundAction.PURR, TransfurSoundAction.HISS),
        DOG(PlayerUtil::isWolfTransfur, TransfurSoundAction.BARK, TransfurSoundAction.GROWL, TransfurSoundAction.HOWL, TransfurSoundAction.WHINE),
        //WOLF(PlayerUtil::isWolfTransfur, TransfurSoundAction.BARK, TransfurSoundAction.GROWL, TransfurSoundAction.HOWL),//same tfs as dog
        FOX(PlayerUtil::isFoxTransfur, TransfurSoundAction.YIP, TransfurSoundAction.CHATTER, TransfurSoundAction.FOX_SCREAM),
        BIG_CAT(PlayerUtil::canRoar, TransfurSoundAction.ROAR),
        DRAGON(PlayerUtil::isDragonTransfur, TransfurSoundAction.DRAGON_ROAR, TransfurSoundAction.DRAGON_GROWL),
        AQUATIC(PlayerUtil::isAquaticTransfur, TransfurSoundAction.SWIM, TransfurSoundAction.FLOP),
        SPIDER(PlayerUtil::isSpiderTransfur, TransfurSoundAction.SPIDER_AMBIENT, TransfurSoundAction.SPIDER_CRAWL);

        public final Predicate<Player> predicate;
        public final Set<TransfurSoundAction> actions;

        TransfurSoundType(Predicate<Player> predicate, TransfurSoundAction... actions) {
            this.predicate = predicate;
            this.actions = Sets.immutableEnumSet(List.of(actions));
        }

        public static Set<TransfurSoundType> getSoundTypes(Player player) {
            Set<TransfurSoundType> types = EnumSet.noneOf(TransfurSoundType.class);

            for (TransfurSoundType type : values()) {
                if (type.predicate.test(player)) types.add(type);
            }

            return types;
        }
    }

    public enum TransfurSoundAction {

        MEOW(20, SoundEvents.CAT_AMBIENT, 1, "meow", "miau"),
        PURREOW(30, SoundEvents.CAT_PURREOW, 1, "purreow"),
        PURR(40, SoundEvents.CAT_PURR, 1, "purr"),
        HISS(40, SoundEvents.CAT_HISS, 1, "hiss"),

        BARK(10, SoundEvents.WOLF_AMBIENT, 1, "bark"),
        GROWL(60, SoundEvents.WOLF_GROWL, 1, "growl", "grr"),
        HOWL(60, SoundEvents.WOLF_HOWL, 1, "howl", "awoo"),
        WHINE(30, SoundEvents.WOLF_WHINE, 1, "whine"),

        YIP(15, SoundEvents.FOX_AMBIENT, 1, "yip"),
        CHATTER(25, SoundEvents.FOX_AGGRO, 1, "chatter"),
        FOX_SCREAM("Scream", 40, SoundEvents.FOX_SCREECH, 1, "scream"),

        DRAGON_ROAR("Roar", 60, SoundEvents.ENDER_DRAGON_AMBIENT, 1.5f, "roar", "rawr"),//might conflict
        DRAGON_GROWL("Growl", 60, SoundEvents.ENDER_DRAGON_GROWL, 1.5f, "growl", "grr"),

        SWIM(20, SoundEvents.FISH_SWIM, 1, "swim"),
        FLOP(15, SoundEvents.TROPICAL_FISH_FLOP, 1, "flop"),

        SPIDER_AMBIENT("Ambient", 20, SoundEvents.SPIDER_AMBIENT, 1),//idk how to name this better / chat keyword
        SPIDER_CRAWL("Crawl", 20, SoundEvents.SPIDER_STEP, 1, "crawl"),

        ROAR(80, ChangedSounds.TIGER_SHARK_ROAR, 2, "roar", "rawr");

        private final String formattedName;
        private final int cooldown;
        private final Supplier<SoundEvent> sound;
        private final float volume;
        private final Set<String> chatMatches;

        TransfurSoundAction(
                String name,
                int cooldown, Supplier<SoundEvent> sound, float volume,
                String... chatMatches
        ) {
            this.formattedName = name;
            this.cooldown = cooldown;
            this.sound = sound;
            this.volume = volume;
            this.chatMatches = Set.of(chatMatches);
        }

        TransfurSoundAction(
                String name,
                int cooldown, SoundEvent sound, float volume,
                String... chatMatches
        ) {
            this(name, cooldown, () -> sound, volume, chatMatches);
        }

        TransfurSoundAction(
                int cooldown, Supplier<SoundEvent> sound, float volume,
                String... chatMatches
        ) {
            formattedName = null;
            this.cooldown = cooldown;
            this.sound = sound;
            this.volume = volume;
            this.chatMatches = Set.of(chatMatches);
        }

        TransfurSoundAction(
                int cooldown, SoundEvent sound, float volume,
                String... chatMatches
        ) {
            this(cooldown, () -> sound, volume, chatMatches);
        }

        public SoundEvent sound() {
            return sound.get();
        }

        public boolean canUse(Player player) {
            return getSoundTypes(player)
                    .stream()
                    .anyMatch(type -> type.actions.contains(this));
        }

        public boolean matchesChat(String text) {
            text = text.toLowerCase();
            for (String match : chatMatches) {
                if (text.contains(match)) {
                    return true;
                }
            }
            return false;
        }

        public void playAndApplyCooldown(Player player) {
            player.level().playSound(
                    null,
                    player,
                    sound(),
                    SoundSource.PLAYERS,
                    volume,
                    getPitch(player, this)
            );

            ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.ofOrDefault(player);
            vars.actCooldown = getCooldown();
            vars.syncPlayerVariables(player);
        }

        public int getCooldown() {
            return cooldown;
        }

        public String getFormatedName() {
            if (formattedName != null) return formattedName;

            String[] parts = this.name().toLowerCase().split("_");
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.isEmpty()) continue;

                result.append(Character.toUpperCase(part.charAt(0)))
                        .append(part.substring(1));

                if (i < parts.length - 1) {
                    result.append(" ");
                }
            }
            return result.toString();
        }
    }
}