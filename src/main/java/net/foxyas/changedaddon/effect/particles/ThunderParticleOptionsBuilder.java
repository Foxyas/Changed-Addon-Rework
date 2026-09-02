package net.foxyas.changedaddon.effect.particles;

import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ThunderParticleOptionsBuilder {

    public static final Logger LOGGER = LogManager.getLogger(ThunderParticleOptionsBuilder.class);

    private String debugName = "generic";

    private int lifeTime;
    private int bodyShakeFrequency = 2;

    boolean shouldUseTargetPosAsBaseForDeltas = false;

    private float speed = 1.0f;
    private float size = 1.0f;

    private boolean rooted = true;
    private boolean staticBody = false;

    private Vector3f shake = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f entityPosOffset = new Vector3f(0f, 0f, 0f);

    @Nullable
    private Entity target = null;

    public static ThunderParticleOptionsBuilder create() {
        return new ThunderParticleOptionsBuilder();
    }

    public ThunderParticleOptionsBuilder speed(float speed) {
        this.speed = speed;
        return this;
    }

    public ThunderParticleOptionsBuilder rooted(boolean rooted) {
        this.rooted = rooted;
        return this;
    }

    public ThunderParticleOptionsBuilder shake(Vector3f shake) {
        this.shake = shake;
        return this;
    }

    public ThunderParticleOptionsBuilder shake(float x, float y, float z) {
        this.shake = new Vector3f(x, y, z);
        return this;
    }

    public ThunderParticleOptionsBuilder color(Vector3f color) {
        this.color = color;
        return this;
    }

    public ThunderParticleOptionsBuilder color(float r, float g, float b) {
        this.color = new Vector3f(r, g, b);
        return this;
    }

    public ThunderParticleOptionsBuilder size(float size) {
        this.size = size;
        return this;
    }

    public ThunderParticleOptionsBuilder lifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
        return this;
    }

    public ThunderParticleOptionsBuilder staticBody() {
        this.staticBody = true;
        return this;
    }

    public ThunderParticleOptionsBuilder shakingBody() {
        this.staticBody = false;
        return this;
    }

    public ThunderParticleOptionsBuilder bodyShakeFrequency(int bodyShakeFrequency) {
        this.bodyShakeFrequency = bodyShakeFrequency;
        return this;
    }

    public ThunderParticleOptionsBuilder shouldUseTargetPosAsBaseForDeltas(boolean value) {
        this.shouldUseTargetPosAsBaseForDeltas = value;
        return this;
    }

    public ThunderParticleOptionsBuilder withTarget(@Nullable Entity entity) {
        this.target = entity;
        return this;
    }

    public ThunderParticleOptionsBuilder targetLess() {
        this.target = null;
        return this;
    }

    public ThunderParticleOptionsBuilder withDebugName(String s) {
        this.debugName = s;
        return this;
    }

    public ThunderParticleOptionsBuilder withEntityPosOffset(Vector3f entityPosOffset) {
        this.entityPosOffset = entityPosOffset;
        return this;
    }


    public ThunderParticleOptions build() {
        if (this.target != null) {
            LOGGER.info("Target of builder: \"{}\" is not null, didn't you mean to call ThunderParticleOptionsBuilder.buildLinked instead?", this.debugName);
        }

        return new ThunderParticleOptions(lifeTime, speed, rooted, staticBody, bodyShakeFrequency, shake, color, size);
    }

    public EntityLinkedThunderParticleOptions buildLinked() {
        if (this.target == null) {
            LOGGER.info("Target of builder: \"{}\" is null, didn't you mean to call ThunderParticleOptionsBuilder.build instead?", this.debugName);
        }
        return new EntityLinkedThunderParticleOptions(target.getId(), shouldUseTargetPosAsBaseForDeltas, entityPosOffset, lifeTime, speed, rooted, staticBody, bodyShakeFrequency, shake, color, size);
    }
}