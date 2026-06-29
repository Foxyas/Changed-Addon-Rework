package net.foxyas.changedaddon.effect.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.configuration.ChangedAddonClientConfiguration;
import net.foxyas.changedaddon.init.ChangedAddonParticleTypes;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.item.LaserPointerItem;
import net.foxyas.changedaddon.util.DynamicClipContext;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaserPointParticle extends TextureSheetParticle {
    public final SpriteSet spriteSet;
    private final Entity entity;

    public LaserPointParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz,
                              LaserPointParticle.Option data, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.spriteSet = sprites;
        this.setSize(0.1f, 0.1f);
        this.quadSize = 0.1f;
        this.setAlpha(data.getColorAlpha());
        this.lifetime = 100; // você pode ajustar isso
        this.entity = level.getEntity(data.getEntityId());
        this.rCol = data.getColorAsColor3().red();
        this.bCol = data.getColorAsColor3().blue();
        this.gCol = data.getColorAsColor3().green();
        this.pickSprite(sprites);
    }

    @Override
    public int getLightColor(float p_105562_) {
        return 15728880;
    }

    @Override
    public void render(@NotNull VertexConsumer pBuffer, @NotNull Camera pRenderInfo, float pPartialTicks) {

        if (level.isClientSide() && Minecraft.getInstance().player != null && ProcessTransfur.getPlayerTransfurVariantSafe(Minecraft.getInstance().player).map(
                transfurVariantInstance -> transfurVariantInstance.getParent().is(ChangedAddonTags.TransfurVariants.CAT_LIKE) || transfurVariantInstance.getParent().is(ChangedAddonTags.TransfurVariants.LEOPARD_LIKE)
        ).orElse(false) && bbWidth != .35) {
            this.setSize(0.35f, 0.35f);
            this.quadSize = 0.35f;
        } else if (bbWidth == .35) {
            this.setSize(0.1f, 0.1f);
            this.quadSize = 0.1f;
        }

        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    @Override
    public void tick() {
        super.tick();

        if (!(entity instanceof LivingEntity owner) || !owner.isAlive()) {
            this.remove(); // Dono sumiu
            return;
        }

        ItemStack heldItem = owner.getUseItem();
        if (heldItem.isEmpty() || !(heldItem.getItem() instanceof LaserPointerItem) || !owner.isUsingItem()) {
            this.remove(); // Jogador parou de usar
            return;
        }

        Vec3 eyePos = owner.getEyePosition();
        HitResult result = level.clip(new DynamicClipContext(eyePos, eyePos.add(owner.getViewVector(1).scale(LaserPointerItem.MAX_LASER_REACH)),
                LaserPointerItem.IGNORE_TRANSLUCENT, ClipContext.Fluid.NONE::canPick, CollisionContext.of(owner))
        );

        EntityHitResult entityHitResult = PlayerUtil.getEntityHitLookingAt(owner, result.getType() != HitResult.Type.MISS
                ? (float) result.distanceTo(owner)
                : LaserPointerItem.MAX_LASER_REACH, null);

        Vec3 hitPos = result.getLocation();
        Direction face = null;

        boolean Subtract = false;
        if (entityHitResult != null) {
            hitPos = entityHitResult.getLocation();
            face = entityHitResult.getEntity().getDirection();
        } else if (result instanceof BlockHitResult blockResult) {
            hitPos = blockResult.getLocation();
            face = blockResult.getDirection();
            hitPos = FoxyasUtil.applyOffset(hitPos, face, !Subtract ? -0.01f : 0.05f);
        }

        // Aplica offset dinâmico baseado na direção
        double offset = !Subtract ? -0.05D : 0.05D;
        hitPos = hitPos.subtract(
                face.getStepX() * offset,
                face.getStepY() * offset,
                face.getStepZ() * offset
        );

        if (ChangedAddonClientConfiguration.SMOOTH_LASER_MOVEMENT.get()) {
            moveToward(hitPos);
        } else {
            SetToward(hitPos);
        }
    }

    private void moveToward(Vec3 target) {
        double dx = target.x - this.x;
        double dy = target.y - this.y;
        double dz = target.z - this.z;

        double distanceSquared = dx * dx + dy * dy + dz * dz;

        if (distanceSquared >= 0.001) {
            double speed = 0.5;
            this.xd = dx * speed;
            this.yd = dy * speed;
            this.zd = dz * speed;

            this.x += this.xd;
            this.y += this.yd;
            this.z += this.zd;

            setBoundingBox(getBoundingBox().move(xd, yd, zd));
        }
        this.age = 0; // Reset idade para manter a partícula viva

    }

    private void SetToward(Vec3 target) {
        setBoundingBox(getBoundingBox().move(target.x - this.x, target.y - this.y, target.z - this.z));

        this.x = target.x;
        this.y = target.y;
        this.z = target.z;

        this.age = 0; // Reset idade para manter a partícula viva
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Option implements ParticleOptions {
        public static final Deserializer<LaserPointParticle.Option> DESERIALIZER = new Deserializer<>() {
            @Override
            public @NotNull LaserPointParticle.Option fromNetwork(@NotNull ParticleType<LaserPointParticle.Option> type, FriendlyByteBuf buffer) {
                int entityId = buffer.readInt();
                int color = buffer.readInt(); // <- nova cor
                float alpha = buffer.readFloat();
                return new LaserPointParticle.Option(entityId, color, alpha);
            }

            @Override
            public @NotNull LaserPointParticle.Option fromCommand(@NotNull ParticleType<LaserPointParticle.Option> type, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                if (!reader.canRead()) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
                }

                int entityId;
                try {
                    entityId = reader.readInt();
                } catch (Exception e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
                }

                reader.expect(' '); // <- espera mais um espaço
                if (!reader.canRead()) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
                }

                int color;
                try {
                    color = reader.readInt();
                } catch (Exception e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
                }

                reader.expect(' '); // <- espera mais um espaço
                if (!reader.canRead()) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerExpectedInt().create();
                }

                float alpha;
                try {
                    alpha = reader.readFloat();
                } catch (Exception e) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().create(reader);
                }

                return new LaserPointParticle.Option(entityId, color, alpha);
            }
        };
        private final int entityId, color;
        private final Entity entity;
        private float alpha;

        public Option(int entityId, int color, float alpha) {
            this.entityId = entityId;
            this.entity = null;
            this.color = color;
            this.alpha = alpha;
        }

        public Option(Entity entity, int color, float alpha) {
            this.entity = entity;
            this.entityId = entity.getId();
            this.color = color;
            this.alpha = alpha;
        }

        public static Codec<LaserPointParticle.Option> codec(ParticleType<LaserPointParticle.Option> type) {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("entity").forGetter(Option::getEntityId),
                    Codec.INT.fieldOf("color").forGetter(Option::getColorAsInt),
                    Codec.FLOAT.fieldOf("alpha").forGetter(Option::getColorAlpha)
            ).apply(builder, Option::new));
        }

        public int getEntityId() {
            return entityId;
        }

        public int getColorAsInt() {
            return color;
        }

        public Color3 getColorAsColor3() {
            return Color3.fromInt(color);
        }

        @Override
        public @NotNull ParticleType<?> getType() {
            return ChangedAddonParticleTypes.LASER_POINT.get();
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
            buffer.writeInt(getColorAsInt());
            buffer.writeFloat(getColorAlpha());
        }

        @Override
        public @NotNull String writeToString() {
            return "EntityId:" + entityId + " ,Color:" + color;
        }

        public float getColorAlpha() {
            return alpha;
        }

        public void setColorAlpha(float alpha) {
            this.alpha = alpha;
        }
    }

    public static class Provider implements ParticleProvider<LaserPointParticle.Option> {
        protected final SpriteSet sprite;

        public Provider(SpriteSet p_106394_) {
            this.sprite = p_106394_;
        }

        @Nullable
        @Override
        public Particle createParticle(LaserPointParticle.@NotNull Option type, @NotNull ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new LaserPointParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprite);
        }
    }
}
