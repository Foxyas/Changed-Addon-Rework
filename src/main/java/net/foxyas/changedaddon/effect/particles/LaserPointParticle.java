package net.foxyas.changedaddon.effect.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.item.LaserPointer;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.foxyas.changedaddon.process.util.FoxyasUtils.manualRaycastIgnoringBlocks;

public class LaserPointParticle extends TextureSheetParticle {
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

        public static Codec<LaserPointParticle.Option> codec(ParticleType<LaserPointParticle.Option> type) {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Codec.INT.fieldOf("entity").forGetter(Option::getEntityId),
                    Codec.INT.fieldOf("color").forGetter(Option::getColorAsInt),
                    Codec.FLOAT.fieldOf("alpha").forGetter(Option::getColorAlpha)
            ).apply(builder, Option::new));
        }
        private final int entityId, color;
        private float alpha;
        private final Entity entity;

        public Option(int entityId , int color, float alpha) {
            this.entityId = entityId;
            this.entity = null;
            this.color = color;
            this.alpha = alpha;
        }
        public Option(Entity entity, int color,float alpha) {
            this.entity = entity;
            this.entityId = entity.getId();
            this.color = color;
            this.alpha = alpha;
        }

        public int getEntityId() {
            return entityId;
        }

        public int getColorAsInt() {
            return color;
        }

        public Color3 getColorAsColor3(){
            return Color3.fromInt(color);
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
        public Particle createParticle(LaserPointParticle.Option type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new LaserPointParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type, sprite);
        }
    }



    private final Entity entity;

    public final SpriteSet spriteSet;
    public LaserPointParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz,
                              LaserPointParticle.Option data, SpriteSet sprites) {
        super(level, x, y, z, dx, dy, dz);
        this.spriteSet = sprites;
        this.setSize(0.1f, 0.1f);
        this.setAlpha(data.getColorAlpha());
        this.lifetime = 100; // você pode ajustar isso
        this.entity = level.getEntity(data.getEntityId());
        this.rCol = data.getColorAsColor3().red();
        this.bCol = data.getColorAsColor3().blue();
        this.gCol = data.getColorAsColor3().green();
        this.pickSprite(sprites);
    }

   	@Override
	public void tick() {
	    super.tick();
	
    	if (entity instanceof LivingEntity owner && owner.isAlive()) {
        	ItemStack heldItem = owner.getUseItem();
        	if (!heldItem.isEmpty() && heldItem.getItem() instanceof LaserPointer && owner.isUsingItem()) {

            	HitResult result = owner.pick(64.0D, 0.0F, true);
                EntityHitResult entityHitResult = PlayerUtilProcedure.getEntityHitLookingAt(owner,64);
            	if (result instanceof BlockHitResult blockResult) {
                    if (level.getBlockState(blockResult.getBlockPos()).is(ChangedTags.Blocks.LASER_TRANSLUCENT)){
                        Set<Block> blockSet = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(ChangedTags.Blocks.LASER_TRANSLUCENT).stream().collect(Collectors.toSet());
                        blockResult = manualRaycastIgnoringBlocks(level, owner, 64, blockSet);
                    }
                	Vec3 hitPos = blockResult.getLocation();
                	Direction face = blockResult.getDirection(); // ← face colidida
	
                	// Aplica offset para trás da face atingida
                	double offset = -0.05D;
                	hitPos = hitPos.subtract(
                    	face.getStepX() * offset,
                    	face.getStepY() * offset,
	                    face.getStepZ() * offset
                	);
	
                	double dx = hitPos.x - this.x;
                	double dy = hitPos.y - this.y;
                	double dz = hitPos.z - this.z;
	
                	double distanceSquared = dx * dx + dy * dy + dz * dz;
                	double distance = Math.sqrt(distanceSquared);
	
                	if (distance >= 0.001) {
                    	// Normaliza o vetor de direção
                    	double ndx = dx * 1; //Math.max(1, distance);
                    	double ndy = dy * 1; //Math.max(1, distance);
                    	double ndz = dz * 1; //Math.max(1, distance);
	
                    	// Define a velocidade base
                    	double speed = 0.5;
	
                    	// Aplica a velocidade ajustada
                    	this.xd = ndx * speed;
                    	this.yd = ndy * speed;
                    	this.zd = ndz * speed;
	
                    	// Move a partícula
                    	this.x += this.xd;
                    	this.y += this.yd;
                    	this.z += this.zd;
	
                    	// Reset da idade para não desaparecer
                    	this.age = 0;
                	}
	            } else if (entityHitResult != null) {
                    Vec3 hitPos = entityHitResult.getLocation();
                    Direction face = entityHitResult.getEntity().getDirection();// ← face colidida

                    // Aplica offset para trás da face atingida
                    double offset = -0.05D;
                    hitPos = hitPos.subtract(
                            face.getStepX() * offset,
                            face.getStepY() * offset,
                            face.getStepZ() * offset
                    );

                    double dx = hitPos.x - this.x;
                    double dy = hitPos.y - this.y;
                    double dz = hitPos.z - this.z;

                    double distanceSquared = dx * dx + dy * dy + dz * dz;
                    double distance = Math.sqrt(distanceSquared);

                    if (distance >= 0.001) {
                        // Normaliza o vetor de direção
                        double ndx = dx * 1; //Math.max(1, distance);
                        double ndy = dy * 1; //Math.max(1, distance);
                        double ndz = dz * 1; //Math.max(1, distance);

                        // Define a velocidade base
                        double speed = 0.5;

                        // Aplica a velocidade ajustada
                        this.xd = ndx * speed;
                        this.yd = ndy * speed;
                        this.zd = ndz * speed;

                        // Move a partícula
                        this.x += this.xd;
                        this.y += this.yd;
                        this.z += this.zd;

                        // Reset da idade para não desaparecer
                        this.age = 0;
                    }
                } else {
                    Vec3 hitPos = result.getLocation();
                    double dx = hitPos.x - this.x;
                    double dy = hitPos.y - this.y;
                    double dz = hitPos.z - this.z;

                    double distanceSquared = dx * dx + dy * dy + dz * dz;
                    double distance = Math.sqrt(distanceSquared);

                    if (distance >= 0.001) {
                        // Normaliza o vetor de direção
                        double ndx = dx * 1; //Math.max(1, distance);
                        double ndy = dy * 1; //Math.max(1, distance);
                        double ndz = dz * 1; //Math.max(1, distance);

                        // Define a velocidade base
                        double speed = 0.5;

                        // Aplica a velocidade ajustada
                        this.xd = ndx * speed;
                        this.yd = ndy * speed;
                        this.zd = ndz * speed;

                        // Move a partícula
                        this.x += this.xd;
                        this.y += this.yd;
                        this.z += this.zd;

                        // Reset da idade para não desaparecer
                        this.age = 0;
                    }
                }
        	} else {
            	this.remove(); // Jogador parou de usar
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
