package net.foxyas.changedaddon.mixins.compatibility;

import com.google.common.collect.Multimap;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ChangedAddonMixinPlugin implements IMixinConfigPlugin {
    private static boolean isModPresent(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    private static boolean conditionSatisfied(String modIdCondition) {
        if (modIdCondition.startsWith("!"))
            return !isModPresent(modIdCondition.substring(1));
        else
            return isModPresent(modIdCondition);
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // This map is generated at compile-time, and doesn't exist until then
        final Multimap<String, String> dependencies = net.foxyas.changedaddon.extension.MixinDependencies.MULTIMAP;
        return dependencies.get(mixinClassName).stream().allMatch(ChangedAddonMixinPlugin::conditionSatisfied);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
