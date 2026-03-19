package net.foxyas.changedaddon.ability;

import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class PsychicGrab extends AbstractAbility<PsychicGrabInstance> {

    public static final Set<Integer> Keys = Set.of(
            GLFW.GLFW_KEY_UP,
            GLFW.GLFW_KEY_DOWN,
            GLFW.GLFW_KEY_LEFT,
            GLFW.GLFW_KEY_RIGHT
    );

    public PsychicGrab() {
        super(PsychicGrabInstance::new);
    }

    @Nullable
    @Override
    public Component getSelectedDisplayText(IAbstractChangedEntity entity) {
        return super.getSelectedDisplayText(entity);
    }

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed_addon.psychic_grab");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed_addon:textures/screens/psychic_hold.png"); // Placeholder
    }

    @Override
    public Collection<Component> getAbilityDescription(IAbstractChangedEntity entity) {
        Collection<Component> descriptions = new ArrayList<>(super.getAbilityDescription(entity));
        descriptions.add(Component.translatable("ability.changed_addon.psychic_grab.description"));
        return descriptions;
    }

    @Override
    public int getCoolDown(IAbstractChangedEntity entity) {
        PsychicGrabInstance abilityInstance = entity.getAbilityInstance(this);
        if (abilityInstance == null) return 0;
        UseType useType = abilityInstance.getUseType();
        return useType == UseType.INSTANT ? 15 : 0;
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        super.startUsing(entity);
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        super.tick(entity);
    }

    @Override
    public void stopUsing(IAbstractChangedEntity entity) {
        super.stopUsing(entity);
    }

    public void addOffset(int keyCode, Player player) {
        double dx = 0, dy = 0, dz = 0;
        boolean shift = player.isShiftKeyDown();

        switch (keyCode) {
            case GLFW.GLFW_KEY_UP -> {
                dz = shift ? 0.5 : 0;
                dy = shift ? 0 : 0.5;
            }
            case GLFW.GLFW_KEY_DOWN -> {
                dz = shift ? -0.5 : 0;
                dy = shift ? 0 : -0.5;
            }
            case GLFW.GLFW_KEY_LEFT -> dx = 0.5;
            case GLFW.GLFW_KEY_RIGHT -> dx = -0.5;
            default -> {
                return;
            }
        }

        IAbstractChangedEntity abstractChangedEntity = IAbstractChangedEntity.forEither(player);
        if (abstractChangedEntity == null) return;
        PsychicGrabInstance abilityInstance = abstractChangedEntity.getAbilityInstance(this);
        if (abilityInstance == null) return;
        Vec3 offset = abilityInstance.offset;

        Vec3 newOffset = offset.add(dx, dy, dz);
        abilityInstance.offset = new Vec3(
                Mth.clamp(newOffset.x, -3, 3),
                Mth.clamp(newOffset.y, -3, 3),
                Mth.clamp(newOffset.z, 0, 4)
        );
    }
}