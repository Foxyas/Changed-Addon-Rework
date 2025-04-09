package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.item.LaserPointer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LaserPointParticle extends TextureSheetParticle {

    public static class Option implements ParticleOptions {
        public static final Deserializer<LaserPointParticle.Option> DESERIALIZER = new Deserializer<>() {
            @Override
            public @NotNull LaserPointParticle.Option fromNetwork(@NotNull ParticleType<LaserPointParticle.Option> type, FriendlyByteBuf buffer) {
                int entityId = buffer.readInt();
                return new LaserPointParticle.Option(entityId);
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
                return new LaserPointParticle.Option(entityId);
            }
        };

        private final int entityId;

        private final Entity entity;

        public Option(int entityId) {
            this.entityId = entityId;
            this.entity = null;
        }

        public Option(Entity entity) {
            this.entity = entity;
            this.entityId = entity.getId();
        }

        public int getEntityId() {
            return entityId;
        }

        @Override
        public @NotNull ParticleType<?> getType() {
            return ChangedAddonParticles.LAZER_POINT; // Substitua pelo seu ParticleType real
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeInt(entityId);
        }

        @Override
        public @NotNull String writeToString() {
            return entityId + "";
        }

        public static Codec<LaserPointParticle.Option> codec(ParticleType<LaserPointParticle.Option> type) {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("entity").forGetter(Option::getEntityId)
            ).apply(builder, Option::new));
        }
    }


    private final Entity entity;

    public LaserPointParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz,
                              LaserPointParticle.Option data, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.setSize(0.3f, 0.3f);
        this.setAlpha(0.8f);
        this.lifetime = 100; // você pode ajustar isso
        this.entity = level.getEntity(data.getEntityId());
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();

        if (entity instanceof LivingEntity owner && owner.isAlive()) {
            ItemStack heldItem = owner.getMainHandItem();
            if (!heldItem.isEmpty() && heldItem.getItem() instanceof LaserPointer && owner.isUsingItem()) {

                var result = owner.pick(64.0D, 0.0F, false);
                if (result.getType() == HitResult.Type.BLOCK) {
                    var hitPos = result.getLocation();

                    // Calcula a direção da partícula até o bloco atingido
                    double dx = hitPos.x - this.x;
                    double dy = hitPos.y - this.y;
                    double dz = hitPos.z - this.z;

                    double length = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    if (length != 0) {
                        this.xd = dx / length * 0.5;
                        this.yd = dy / length * 0.5;
                        this.zd = dz / length * 0.5;
                    }

                    // Move a partícula
                    this.x += this.xd;
                    this.y += this.yd;
                    this.z += this.zd;
                }

            } else {
                this.remove(); // Jogador parou de usar o item
            }
        } else {
            this.remove(); // Dono sumiu
        }
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
}
