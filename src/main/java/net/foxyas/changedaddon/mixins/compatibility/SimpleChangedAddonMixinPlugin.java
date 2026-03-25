package net.foxyas.changedaddon.mixins.compatibility;

import net.foxyas.changedaddon.annotations.ConditionalMixin;
import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleChangedAddonMixinPlugin implements IMixinConfigPlugin {

    public static boolean hasAnnotation(ClassNode classNode, String annotationDescriptor) {
        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode annotation : classNode.visibleAnnotations) {
                if (annotation.desc.equals(annotationDescriptor)) {
                    return true;
                }
            }
        }

        if (classNode.invisibleAnnotations != null) {
            for (AnnotationNode annotation : classNode.invisibleAnnotations) {
                if (annotation.desc.equals(annotationDescriptor)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static String getAnnotationValue(AnnotationNode annotation, String key) {
        if (annotation.values == null) return null;

        for (int i = 0; i < annotation.values.size(); i += 2) {
            String name = (String) annotation.values.get(i);
            Object value = annotation.values.get(i + 1);

            if (name.equals(key)) {
                return (String) value;
            }
        }

        return null;
    }

    private static boolean isModPresent(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    private static boolean conditionSatisfied(String modIdCondition) {
        if (modIdCondition.startsWith("!"))
            return !isModPresent(modIdCondition.substring(1));
        else
            return isModPresent(modIdCondition);
    }

    private static final Map<String, ClassNode> CACHE = new HashMap<>();

    private static ClassNode getClassNode(String className) {
        return CACHE.computeIfAbsent(className, name -> {
            try {
                String path = name.replace('.', '/') + ".class";
                InputStream stream = SimpleChangedAddonMixinPlugin.class.getClassLoader().getResourceAsStream(path);

                if (stream == null) {
                    throw new RuntimeException("Class not found: " + name);
                }

                ClassReader reader = new ClassReader(stream);
                ClassNode node = new ClassNode();
                reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

                return node;
            } catch (Exception e) {
                throw new RuntimeException("Failed to read class: " + name, e);
            }
        });
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
        ClassNode node = getClassNode(mixinClassName);

        String desc = ConditionalMixin.class.descriptorString();
        AnnotationNode found = null;

        if (node.visibleAnnotations != null) {
            for (AnnotationNode ann : node.visibleAnnotations) {
                if (ann.desc.equals(desc)) {
                    found = ann;
                    break;
                }
            }
        }

        if (found == null && node.invisibleAnnotations != null) {
            for (AnnotationNode ann : node.invisibleAnnotations) {
                if (ann.desc.equals(desc)) {
                    found = ann;
                    break;
                }
            }
        }

        if (found != null) {
            List<String> conditions = null;

            if (found.values != null) {
                for (int i = 0; i < found.values.size(); i += 2) {
                    String key = (String) found.values.get(i);
                    Object value = found.values.get(i + 1);

                    if (key.equals("value")) {
                        conditions = (List<String>) value;
                        break;
                    }
                }
            }

            if (conditions != null) {
                boolean apply = conditions.stream()
                        .allMatch(SimpleChangedAddonMixinPlugin::conditionSatisfied);

                if (!apply) {
                    System.out.println("[MixinPlugin] Skipping " + mixinClassName +
                            " (conditions: " + conditions + ")");
                    return false;
                }
            }
        }

        return true;
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
