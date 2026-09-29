package net.foxyas.changedaddon.entity.simple;

import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Gender;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class FemaleLuminaraCrystalBeing extends LuminaraCrystalBeing {

    public FemaleLuminaraCrystalBeing(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }
}
