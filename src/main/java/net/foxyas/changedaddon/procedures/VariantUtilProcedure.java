package net.foxyas.changedaddon.procedures;

import net.ltxprogrammer.changed.entity.variant.GenderedVariant;
import net.ltxprogrammer.changed.entity.variant.LatexVariant;
import net.ltxprogrammer.changed.init.ChangedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class VariantUtilProcedure {

	public static float GetLandSpeed(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? 0f : variant.groundSpeed;
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetLandSpeed: " + e.getMessage());
			return 0f;
		}
	}

	public static float GetSwimSpeed(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? 0f : variant.swimSpeed;
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetSwimSpeed: " + e.getMessage());
			return 0f;
		}
	}

	public static int GetExtraHp(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? 0 : variant.additionalHealth;
			} else {
				return 0;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetExtraHp: " + e.getMessage());
			return 0;
		}
	}

	public static int GetLegs(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? 0 : variant.legCount;
			} else {
				return 0;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetLegs: " + e.getMessage());
			return 0;
		}
	}

	public static boolean CanGlideandFly(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant != null && variant.canGlide;
			} else {
				return false;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing CanGlideandFly: " + e.getMessage());
			return false;
		}
	}

	public static boolean CanClimb(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant != null && variant.canClimb;
			} else {
				return false;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing CanClimb: " + e.getMessage());
			return false;
		}
	}

	public static float GetJumpStrength(String stringvariant) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? 0f : variant.jumpStrength;
			} else {
				return 0f;
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetJumpStrength: " + e.getMessage());
			return 0f;
		}
	}

	public static EntityType<?> GetEntity(String stringvariant, Level world) {
		try {
			ResourceLocation form = new ResourceLocation(stringvariant);
			if (LatexVariant.PUBLIC_LATEX_FORMS.contains(form)) {
				LatexVariant variant = ChangedRegistry.LATEX_VARIANT.get().getValue(form);
				return variant == null ? LatexVariant.LIGHT_LATEX_WOLF.male().getEntityType() : variant.getEntityType();
			} else {
				return LatexVariant.LIGHT_LATEX_WOLF.male().getEntityType();
			}
		} catch (Exception e) {
			//System.err.println("Erro when processing GetEntity: " + e.getMessage());
			return LatexVariant.LIGHT_LATEX_WOLF.male().getEntityType();
		}
	}
}
