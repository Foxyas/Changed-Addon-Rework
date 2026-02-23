package net.foxyas.changedaddon.effect.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SignalParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final int delay;
    private final int totalSprites = 6;
    private final ItemStack signalCatherItem;
    private final float baseAlpha;

    public SignalParticle(ClientLevel level, double x, double y, double z,
                          double dx, double dy, double dz,
                          SignalParticleOption data, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);

        this.spriteSet = sprites;
        this.age = 0;
        this.setSize(0.3f, 0.3f);

        this.lifetime = Math.max(1, (15 * data.getSignalStrength()) + (this.random.nextInt(6) - 3));

        this.gravity = 0f;
        this.hasPhysics = true;

        this.xd = dx;
        this.yd = dy;
        this.zd = dz;

        this.delay = Math.max(1, (6 - (int) (data.getSignalStrength() * 0.25f)));
        this.baseAlpha = Math.min(1f, 0.2f + (0.15f * data.getSignalStrength()));
        this.signalCatherItem = data.getSignalCatcherItem();

        this.setSpriteFromAge(spriteSet);
    }

    private boolean playerHoldingBlockingItem() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;

        ItemStack main = mc.player.getMainHandItem();
        ItemStack off = mc.player.getOffhandItem();

        return main.is(signalCatherItem.getItem()) || off.is(signalCatherItem.getItem());
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age >= this.lifetime) {
            this.remove();
            return;
        }

        if (!playerHoldingBlockingItem()) {
            this.age++;
        } else {
            if (age + 1 > lifetime) {
                this.age = 0;
            } else {
                this.age++;
            }
        }

        int index = (this.age / delay) % totalSprites;
        this.setSprite(spriteSet.get(index, totalSprites));

        if (!playerHoldingBlockingItem()) {
            this.alpha = this.baseAlpha * (1f - ((float) age / lifetime));
        } else {
            this.alpha = this.baseAlpha;
        }


        this.move(this.xd, this.yd, this.zd);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 15728880;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ChangedAddonRenderTypes.ParticleRenderTypes.OVERLAY;
    }

    /**
     * Provider corrigido
     **/
    public static class Provider implements ParticleProvider<SignalParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull SignalParticleOption type,
                                       @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {

            return new SignalParticle(level, x, y, z, dx, dy, dz, type, sprites);
        }
    }
}