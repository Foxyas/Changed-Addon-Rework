package net.foxyas.changedaddon.effect.particles;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ThunderParticleOptionsBuilder {
    private Vec3 startPos = Vec3.ZERO;
    private Vec3 endPos = Vec3.ZERO;
    private float speed = 1.0f;
    private boolean rooted = true;
    private Vector3f shake = new Vector3f(1.0f, 1.0f, 1.0f);
    private Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);
    private float size = 1.0f;

    public static ThunderParticleOptionsBuilder create() {
        return new ThunderParticleOptionsBuilder();
    }

    public ThunderParticleOptionsBuilder startPos(Vec3 startPos) {
        this.startPos = startPos;
        return this;
    }

    public ThunderParticleOptionsBuilder startPos(double x, double y, double z) {
        this.startPos = new Vec3(x, y, z);
        return this;
    }

    public ThunderParticleOptionsBuilder endPos(Vec3 endPos) {
        this.endPos = endPos;
        return this;
    }

    public ThunderParticleOptionsBuilder endPos(double x, double y, double z) {
        this.endPos = new Vec3(x, y, z);
        return this;
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

    public ThunderParticleOptions build() {
        return new ThunderParticleOptions(startPos, endPos, speed, rooted, shake, color, size);
    }
}