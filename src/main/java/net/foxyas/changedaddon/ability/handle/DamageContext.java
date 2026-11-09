package net.foxyas.changedaddon.ability.handle;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record DamageContext(@Nullable DamageSourceInstance damageSourceInstance, @Nullable Projectile projectile) {

    public DamageContext(@Nullable DamageSourceInstance damageSourceInstance) {
        this(damageSourceInstance, null);
    }

    public DamageContext(@Nullable Projectile projectile) {
        this(null, projectile);
    }

    public DamageContext ifDamageSource(Consumer<DamageSourceInstance> damageSourceInstanceConsumer) {
        if (damageSourceInstance != null) damageSourceInstanceConsumer.accept(damageSourceInstance);
        return this;
    }

    public DamageContext ifProjectile(Consumer<Projectile> projectileConsumer) {
        if (projectile != null) projectileConsumer.accept(projectile);
        return this;
    }

    public DamageContext ifBoth(BiConsumer<DamageSourceInstance, Projectile> biConsumer) {
        if (projectile != null && damageSourceInstance != null) biConsumer.accept(damageSourceInstance, projectile);
        return this;
    }

    public Optional<DamageSourceInstance> getDamage() {
        return Optional.ofNullable(damageSourceInstance);
    }

    public Optional<Projectile> getProjectile() {
        return Optional.ofNullable(projectile);
    }

    public Either<DamageSourceInstance, Projectile> asEither() {
        return damageSourceInstance != null ? Either.left(damageSourceInstance) : Either.right(projectile);
    }

    public boolean isProjectile() {
        return this.getProjectile().isPresent();
    }
    public boolean isDamageSource() {
        return this.getDamage().isPresent();
    }
}
