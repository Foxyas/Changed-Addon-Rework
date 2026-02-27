package net.foxyas.changedaddon.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.ltxprogrammer.changed.data.BuiltinPackResources;
import net.ltxprogrammer.changed.data.PackExtender;
import net.minecraft.Util;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

public class ChangedAddonBuiltinRepositorySource implements RepositorySource {
    private final String modId;
    private final Path modFile;
    private final boolean isJar;
    private final Set<String> packIds = new HashSet();
    private final String packsFolder;
    private static final EnumMap<PackType, String> NAMED_FOLDERS = Util.make(new EnumMap<>(PackType.class), (map) -> {
        map.put(PackType.CLIENT_RESOURCES, "resourcepacks");
        map.put(PackType.SERVER_DATA, "datapacks");
    });
    private static final String MCMETA = "pack.mcmeta";

    public ChangedAddonBuiltinRepositorySource(PackType type, String modId) throws IOException, NullPointerException {
        this.modId = modId;
        this.modFile = FMLLoader.getLoadingModList().getModFileById(modId).getFile().getFilePath();
        this.packsFolder = NAMED_FOLDERS.getOrDefault(type, type.getDirectory());
        File file = this.modFile.toFile();
        if (file.isDirectory()) {
            this.isJar = false;
            Files.walk(this.modFile.resolve(this.packsFolder), 1, new FileVisitOption[0]).filter((path) -> path.resolve("pack.mcmeta").toFile().isFile()).forEach((path) -> this.packIds.add(path.getFileName().toString()));
        } else {
            if (!file.isFile()) {
                throw new IOException("Invalid mod format");
            }

            this.isJar = true;
            ZipFile jar = new ZipFile(this.modFile.toFile());
            jar.stream().filter(ZipEntry::isDirectory).filter((entry) -> entry.getName().startsWith(this.packsFolder + "/") && jar.getEntry(entry.getName() + "pack.mcmeta") != null).forEach((entry) -> this.packIds.add((new File(entry.getName())).getName()));
            jar.close();
        }

    }

    @Override
    public void loadPacks(@NotNull Consumer<Pack> out, Pack.@NotNull PackConstructor constructor) {
        for(String id : this.packIds) {
            Pack pack = Pack.create(this.modId + ":" + id, false, this.createSupplier(this.modFile.toFile(), id), constructor, Position.TOP, PackSource.BUILT_IN);
            if (pack instanceof PackExtender ext) {
                ext.setIncludeByDefault(false);
            }

            if (pack != null) {
                out.accept(pack);
            }
        }

    }

    private Supplier<PackResources> createSupplier(File file, String packName) {
        return this.isJar ? () -> new BuiltinPackResources(file, this.packsFolder + "/" + packName + "/") : () -> new FolderPackResources(new File(file, this.packsFolder + "/" + packName));
    }
}
