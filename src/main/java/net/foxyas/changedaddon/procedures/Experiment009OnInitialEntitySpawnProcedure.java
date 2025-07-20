package net.foxyas.changedaddon.procedures;

import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class Experiment009OnInitialEntitySpawnProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        AttributeModifier HardModeBuff = null;
        if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
                _level.playSound(null, new BlockPos(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select")), SoundSource.HOSTILE, 100000, 0);
            } else {
                _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.power_select")), SoundSource.HOSTILE, 100000, 0, false);
            }
        }
        HardModeBuff = new AttributeModifier(UUID.fromString("d6aa1721-3594-4b7b-ad84-977d7b14a94d"), "gameruleBuff", ((world.getLevelData().getGameRules().getInt(ChangedAddonModGameRules.CHANGED_ADDON_HARD_MODE_BOSSES)) / 100),
                AttributeModifier.Operation.MULTIPLY_BASE);
        if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).hasModifier(HardModeBuff)))
            ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).addTransientModifier(HardModeBuff);
        if (!(((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).hasModifier(HardModeBuff)))
            ((LivingEntity) entity).getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).addTransientModifier(HardModeBuff);
        if (entity instanceof LivingEntity _entity)
            _entity.setHealth(entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1);
    }
}
