package net.foxyas.changedaddon.entity.bosses;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Arrays;

public enum AngerLevel {
   CALM(0, SoundEvents.WARDEN_AMBIENT, SoundEvents.WARDEN_LISTENING),
   AGITATED(40, SoundEvents.WARDEN_AGITATED, SoundEvents.WARDEN_LISTENING_ANGRY),
   ANGRY(80, SoundEvents.WARDEN_ANGRY, SoundEvents.WARDEN_LISTENING_ANGRY);

   private static final AngerLevel[] SORTED_LEVELS = Util.make(values(), (p_219233_) -> {
      Arrays.sort(p_219233_, (p_219230_, p_219231_) -> {
         return Integer.compare(p_219231_.minimumAnger, p_219230_.minimumAnger);
      });
   });
   private final int minimumAnger;
   private final SoundEvent ambientSound;
   private final SoundEvent listeningSound;

   private AngerLevel(int pMinimumAnger, SoundEvent pAmbientSound, SoundEvent pListeningSound) {
      this.minimumAnger = pMinimumAnger;
      this.ambientSound = pAmbientSound;
      this.listeningSound = pListeningSound;
   }

   public int getMinimumAnger() {
      return this.minimumAnger;
   }

   public SoundEvent getAmbientSound() {
      return this.ambientSound;
   }

   public SoundEvent getListeningSound() {
      return this.listeningSound;
   }

   public static AngerLevel byAnger(int pAnger) {
      for(AngerLevel angerlevel : SORTED_LEVELS) {
         if (pAnger >= angerlevel.minimumAnger) {
            return angerlevel;
         }
      }

      return CALM;
   }

   public boolean isAngry() {
      return this == ANGRY;
   }
}