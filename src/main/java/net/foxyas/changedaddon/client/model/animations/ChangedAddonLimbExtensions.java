package net.foxyas.changedaddon.client.model.animations;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.ltxprogrammer.changed.client.animations.Limb;
import net.ltxprogrammer.changed.client.animations.LimbExtension;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChangedAddonLimbExtensions {

    public static final Map<ResourceLocation, LimbExtension> EXTENSIONS = new HashMap<>();

    public static final LimbExtension LEFT_LOWER_LEG = register("left_lower_foot", LimbExtension.forFetcher(Set.of(Limb.LEFT_LEG), getModelPartFetcherFor("LeftLowerLeg")));
    public static final LimbExtension RIGHT_LOWER_LEG = register("right_lower_foot", LimbExtension.forFetcher(Set.of(Limb.RIGHT_LEG), getModelPartFetcherFor("RightLowerLeg")));
    public static final LimbExtension LEFT_FOOT = register("left_foot", LimbExtension.forFetcher(Set.of(Limb.LEFT_LEG), getModelPartFetcherFor("LeftLowerLeg", "LeftFoot", "LeftPad")));
    public static final LimbExtension RIGHT_FOOT = register("right_foot", LimbExtension.forFetcher(Set.of(Limb.RIGHT_LEG), getModelPartFetcherFor("RightLowerLeg", "RightFoot", "RightPad")));
    public static final LimbExtension LEFT_PAD = register("left_pad", LimbExtension.forFetcher(Set.of(Limb.LEFT_LEG), getModelPartFetcherFor("LeftLowerLeg", "LeftFoot", "LeftPad")));
    public static final LimbExtension RIGHT_PAD = register("right_pad", LimbExtension.forFetcher(Set.of(Limb.RIGHT_LEG), getModelPartFetcherFor("RightLowerLeg", "RightFoot", "RightPad")));

    /**
     * Retorna um fetcher que navega a partir do 'limbRoot'.
     * Se os 'paths' forem informados, ele tenta segui-los.
     * Depois (ou se não houver paths), continua descendo nos filhos até achar um nó que não tem mais nenhum filho.
     */
    private static LimbExtension.@NotNull ModelPartFetcher getModelPartFetcherFor(String... path) {
        return (animator, limb, limbRoot, param) -> {
            if (limbRoot == null) {
                return null;
            }

            ModelPart current = limbRoot;

            // 1. Opcional: Navega pelo caminho (path) especificado primeiro
            for (String childName : path) {
                if (current.children.containsKey(childName)) {
                    current = current.getChild(childName);
                } else {
                    break;
                }
            }

            // 2. Continua descendo até encontrar um nó sem filhos (!hasChild / children.isEmpty())
            while (!current.children.isEmpty()) {
                // Pega o primeiro filho disponível da coleção
                current = current.children.values().iterator().next();
            }

            return current; // Retorna o último filho da ponta
        };
    }

    private static LimbExtension register(String name, LimbExtension extension) {
        EXTENSIONS.put(ChangedAddonMod.resourceLoc(name), extension);
        return extension;
    }
}