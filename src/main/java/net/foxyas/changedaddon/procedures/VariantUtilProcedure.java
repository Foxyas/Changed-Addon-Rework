package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class VariantUtilProcedure {

	public static float GetLandSpeed(String stringvariant) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return 0f;
			} else {
				return variant.groundSpeed;
			}
		} else {
			return 0f;
		}

	}

	public static float GetSwimSpeed(String stringvariant) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return 0f;
			} else {
				return variant.swimSpeed;
			}
		} else {
			return 0f;
		}
	}
		public static int GetExtraHp(String stringvariant) {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				if (variant == null) {
					return 0;
				} else {
					return variant.additionalHealth;
				}
			} else {
				return 0;
			}
		}

	public static int GetLegs(String stringvariant) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return 0;
			} else {
				return variant.legCount;
			}
		} else {
			return 0;
		}
	}

	public static boolean CanGlideandFly(String stringvariant) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return false;
			} else {
				return variant.canGlide;
			}
		} else {
			return false;
		}
	}

	public static boolean CanClimb(String stringvariant) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return false;
			} else {
				return variant.canClimb;
			}
		} else {
			return false;
		}
	}

	public static EntityType GetEntity(String stringvariant, Level world) {
		ResourceLocation form = new ResourceLocation(stringvariant);
		if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
			LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
			if (variant == null) {
				return variant.LIGHT_LATEX_WOLF.male().getEntityType();
			} else {
				return variant.getEntityType();
			}
		} else {
			return LatexVariant.LIGHT_LATEX_WOLF.male().getEntityType();
		}
	}
}
