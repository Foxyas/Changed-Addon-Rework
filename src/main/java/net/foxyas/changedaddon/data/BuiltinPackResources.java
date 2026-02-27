package net.foxyas.changedaddon.data;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.ResourcePackFileNotFoundException;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

public class BuiltinPackResources extends AbstractPackResources {
    private final File file;
    private final String prefix;
    private final Splitter splitter;
    private final int prefixDirCount;
    @Nullable
    private ZipFile zipFile;

    public BuiltinPackResources(File file, String prefix) {
        super(new File(file, prefix));
        this.file = file;
        this.prefix = prefix;
        Path path = Path.of(prefix);
        this.prefixDirCount = path.getNameCount();
        this.splitter = Splitter.on('/').omitEmptyStrings().limit(3 + path.getNameCount());
    }

    private ZipFile getOrCreateZipFile() throws IOException {
        if (this.zipFile == null) {
            this.zipFile = new ZipFile(this.file);
        }

        return this.zipFile;
    }

    protected @NotNull InputStream getResource(@NotNull String name) throws IOException {
        ZipFile zipfile = this.getOrCreateZipFile();
        ZipEntry zipentry = zipfile.getEntry(this.prefix + name);
        if (zipentry == null) {
            throw new ResourcePackFileNotFoundException(this.file, name);
        } else {
            return zipfile.getInputStream(zipentry);
        }
    }

    public boolean hasResource(@NotNull String name) {
        try {
            ZipFile orCreateZipFile = this.getOrCreateZipFile();
            return orCreateZipFile.getEntry(this.prefix + name) != null;
        } catch (IOException var3) {
            return false;
        }
    }

    public @NotNull Set<String> getNamespaces(@NotNull PackType type) {
        ZipFile zipfile;
        try {
            zipfile = this.getOrCreateZipFile();
        } catch (IOException var9) {
            return Collections.emptySet();
        }

        Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        Set<String> set = Sets.newHashSet();

        while(enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            String name = zipentry.getName();
            if (name.startsWith(this.prefix + type.getDirectory() + "/")) {
                List<String> list = Lists.newArrayList(this.splitter.split(name));
                if (list.size() > 1 + this.prefixDirCount) {
                    String s1 = list.get(1 + this.prefixDirCount);
                    if (s1.equals(s1.toLowerCase(Locale.ROOT))) {
                        set.add(s1);
                    } else {
                        this.logWarning(s1);
                    }
                }
            }
        }

        return set;
    }

    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public void close() {
        if (this.zipFile != null) {
            IOUtils.closeQuietly(this.zipFile);
            this.zipFile = null;
        }

    }

    public @NotNull Collection<ResourceLocation> getResources(@NotNull PackType type, @NotNull String namespace, @NotNull String path, int maxDepth, @NotNull Predicate<String> filter) {
        ZipFile zipfile;
        try {
            zipfile = this.getOrCreateZipFile();
        } catch (IOException var15) {
            return Collections.emptySet();
        }

        Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        List<ResourceLocation> list = Lists.newArrayList();
        String s = this.prefix + type.getDirectory() + "/" + namespace + "/";
        String s1 = s + path + "/";

        while(enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            if (!zipentry.isDirectory()) {
                String s2 = zipentry.getName();
                if (s2.startsWith(this.prefix) && !s2.endsWith(".mcmeta") && s2.startsWith(s1)) {
                    String s3 = s2.substring(s.length());
                    String[] astring = s3.split("/");
                    if (astring.length >= maxDepth + 1 && filter.test(astring[astring.length - 1])) {
                        list.add(new ResourceLocation(namespace, s3));
                    }
                }
            }
        }

        return list;
    }
}
