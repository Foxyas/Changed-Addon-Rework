package net.foxyas.changedaddon.data;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarredPackResources extends AbstractPackResources {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final File file;
    private final String prefix;
    private final Splitter splitter;
    private final int prefixDirCount;

    @Nullable
    private ZipFile zipFile;

    public JarredPackResources(String packName, File file, String prefix) {
        super(packName, true);
        this.file = file;
        this.prefix = prefix;
        this.prefixDirCount = Path.of(prefix).getNameCount();
        this.splitter = Splitter.on('/').omitEmptyStrings().limit(3 + Path.of(prefix).getNameCount());
    }

    private ZipFile getOrCreateZipFile() throws IOException {
        if (this.zipFile == null) {
            this.zipFile = new ZipFile(this.file);
        }

        return this.zipFile;
    }

    public @Nullable IoSupplier<InputStream> getResource(String resolvedPath) {
        try {
            ZipFile zipfile = this.getOrCreateZipFile();
            ZipEntry zipentry = zipfile.getEntry(prefix + resolvedPath);
            return zipentry != null ? IoSupplier.create(zipfile, zipentry) : null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String @NotNull ... paths) {
        return this.getResource(String.join("/", paths));
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        return this.getResource(packType.getDirectory() + "/" + resourceLocation.getNamespace() + "/" + resourceLocation.getPath());
    }

    @Override
    public void listResources(@NotNull PackType packType, @NotNull String namespace, @NotNull String path, @NotNull ResourceOutput output) {
        ZipFile zipfile;
        try {
            zipfile = this.getOrCreateZipFile();
        } catch (IOException ioexception) {
            return;
        }

        Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        String s = prefix + packType.getDirectory() + "/" + namespace + "/";
        String s1 = s + path + "/";

        while (enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            if (!zipentry.isDirectory()) {
                String s2 = zipentry.getName();
                if (!s2.startsWith(prefix))
                    continue;

                if (!s2.endsWith(".mcmeta") && s2.startsWith(s1)) {
                    String s3 = s2.substring(s.length());
                    output.accept(ResourceLocation.fromNamespaceAndPath(namespace, s3),
                            IoSupplier.create(zipfile, zipentry));
                }
            }
        }
    }

    public @NotNull Set<String> getNamespaces(@NotNull PackType type) {
        ZipFile zipfile;
        try {
            zipfile = this.getOrCreateZipFile();
        } catch (IOException ioexception) {
            return Collections.emptySet();
        }

        Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        Set<String> set = Sets.newHashSet();

        while (enumeration.hasMoreElements()) {
            ZipEntry zipentry = enumeration.nextElement();
            String s = zipentry.getName();
            if (s.startsWith(prefix + type.getDirectory() + "/")) {
                List<String> list = Lists.newArrayList(splitter.split(s));
                if (list.size() > 1 + prefixDirCount) {
                    String s1 = list.get(1 + prefixDirCount);
                    if (s1.equals(s1.toLowerCase(Locale.ROOT))) {
                        set.add(s1);
                    } else {
                        LOGGER.warn("Ignored non-lowercase namespace: {} in {}", s1, this.file);
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
            IOUtils.closeQuietly((Closeable)this.zipFile);
            this.zipFile = null;
        }

    }
}
