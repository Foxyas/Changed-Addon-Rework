
package net.foxyas.changedaddon.potion;

import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonModVariables;
import net.foxyas.changedaddon.procedures.SummonDripParticlesProcedure;
import net.foxyas.changedaddon.process.util.DelayedTask;
import net.foxyas.changedaddon.process.util.PlayerUtil;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class UntransfurMobEffect extends MobEffect {

	public UntransfurMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -1);
	}

	@Override
	public @NotNull String getDescriptionId() {
		return "effect.changed_addon.untransfur";
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		Level level = entity.level;

		if (entity instanceof ChangedEntity livEnt) {
			if (entity.getType().is(ChangedTags.EntityTypes.LATEX)) {
				entity.hurt(new DamageSource("latex_solvent"),
						(float) ((livEnt.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get()) ? livEnt.getEffect(ChangedAddonMobEffects.UNTRANSFUR.get()).getAmplifier() : 0) + 1));
			}
			return;
		}

		if(!(entity instanceof Player player)) return;
		ChangedAddonModVariables.PlayerVariables vars = ChangedAddonModVariables.PlayerVariables.of(player);
		if(vars == null || vars.untransfurProgress < 100) return;

		if(!ProcessTransfur.isPlayerTransfurred(player)){
			if (vars.showwarns) {
				if (!player.level.isClientSide())
					player.displayClientMessage(new TextComponent((new TranslatableComponent("changedaddon.untransfur.no_effect").getString())), true);
			}
			return;
		}

		SummonDripParticlesProcedure.execute(entity);
		PlayerUtil.UnTransfurPlayer(entity);

		vars.untransfurProgress = 0;
		vars.syncPlayerVariables(entity);

		player.removeEffect(ChangedAddonMobEffects.UNTRANSFUR.get());

		if(vars.reset_transfur_advancements) new DelayedTask(10, () -> {
			MinecraftServer server = player.getServer();
			if(server != null) server.getCommands().performCommand(player.createCommandSourceStack().withSuppressedOutput().withPermission(4), "advancement revoke @s from minecraft:changed/transfur");
		});

		if (!(entity instanceof ServerPlayer sPlayer && sPlayer.level instanceof ServerLevel
				&& sPlayer.getAdvancements().getOrStartProgress(Objects.requireNonNull(sPlayer.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:untransfuradvancement")))).isDone())) {
			if (entity instanceof ServerPlayer _player) {
				Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("changed_addon:untransfuradvancement"));
				assert _adv != null;
				AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
				if (!_ap.isDone()) {
					for (String s : _ap.getRemainingCriteria()) _player.getAdvancements().award(_adv, s);
				}
			}
		}

		if(!level.isClientSide && !player.isCreative() && !player.isSpectator()){
			player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 60, 0, false, false));
			player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 0, false, false));
		}

		level.playSound(null, player.getX(), player.getY(), player.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("changed_addon:untransfursound")), SoundSource.NEUTRAL, 1, 1);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
