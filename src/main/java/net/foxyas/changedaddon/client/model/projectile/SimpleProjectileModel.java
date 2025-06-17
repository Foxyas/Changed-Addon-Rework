package net.foxyas.changedaddon.client.model.projectile;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class SimpleProjectileModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("changed_addon", "simple_projectile_model"), "main");
	private final ModelPart main;

	public SimpleProjectileModel(ModelPart root) {
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(-0.75F))
		.texOffs(0, 6).addBox(-1.0F, -4.8F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(8, 10).addBox(-1.0F, -6.2F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(8, 6).addBox(-0.3F, -5.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(0, 10).addBox(-1.0F, -5.5F, -0.3F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(0, 14).addBox(-1.0F, -5.5F, -1.7F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F))
		.texOffs(12, 0).addBox(-1.7F, -5.5F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.75F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.main.yRot = netHeadYaw * ((float)Math.PI / 180F);
		this.main.xRot = netHeadYaw * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}