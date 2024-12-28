package net.foxyas.changedaddon;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ResourcePackDownloader {

    // 在FMLCommonSetupEvent中初始化资源包下载
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        System.out.println("Common Setup");
        // 使用线程来异步下载资源包，避免阻塞主线程
        new Thread(ResourcePackDownloader::downloadResourcePackOnSetup).start();
    }

    // 执行资源包下载的操作
    private static void downloadResourcePackOnSetup() {
        try {
            // 资源包下载链接（两个备份链接）
            String[] resourcePackUrls = {
                    "https://raw.githubusercontent.com/gengyoubo/Changed-Addon-Rework/main/resourcepack/translationpack.zip",
                    "https://raw.githubusercontent.com/Foxyas/Changed-Addon-Rework/main/resourcepack/translationpack.zip"
            };

            // 查找相对路径中的 resourcepacks 文件夹
            File resourcePackDir = new File("resourcepacks");

            // 如果没有找到 resourcepacks 文件夹，检查 run/resourcepacks
            if (!resourcePackDir.exists()) {
                resourcePackDir = new File("run/resourcepacks");
            }

            // 如果 run/resourcepacks 也没有，创建 resourcepacks 文件夹
            if (!resourcePackDir.exists()) {
                boolean created = resourcePackDir.mkdirs();
                if (!created) {
                    throw new RuntimeException("Failed to create directory: " + resourcePackDir.getAbsolutePath());
                }
            }

            // 遍历每个下载链接
            for (String url : resourcePackUrls) {
                // 根据链接生成不同的文件名
                String fileName = getFileNameFromUrl(url);
                File output = new File(resourcePackDir, fileName);

                // 如果文件已经存在，进行内容检查
                if (output.exists()) {
                    // 获取文件的哈希值
                    String existingFileHash = getFileHash(output);
                    String newFileHash = getFileHashFromUrl(url);

                    if (!existingFileHash.equals(newFileHash)) {
                        // 如果哈希值不同，下载并覆盖文件
                        downloadResourcePack(url, output);
                        System.out.println("Resource pack updated successfully from: " + url);
                        return;
                    }
                } else {
                    // 如果文件不存在，直接下载
                    downloadResourcePack(url, output);
                    System.out.println("Resource pack downloaded successfully from: " + url);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取文件名根据URL
    private static String getFileNameFromUrl(String url) {
        if (url.contains("gengyoubo")) {
            return "translationpack(gengyoubo).zip";
        } else if (url.contains("Foxyas")) {
            return "translationpack(Foxyas).zip";
        }
        return "translationpack.zip"; // 默认文件名
    }

    // 获取文件的哈希值（SHA-256）
    private static String getFileHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (BufferedInputStream bis = new BufferedInputStream(new java.io.FileInputStream(file))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // 获取远程文件的哈希值（通过URL）
    private static String getFileHashFromUrl(String url) throws IOException, NoSuchAlgorithmException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to download resource pack: HTTP " + connection.getResponseCode());
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }

        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // 下载资源包的方法
    public static void downloadResourcePack(String url, File output) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to download resource pack: HTTP " + connection.getResponseCode());
        }

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(output)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            connection.disconnect();
        }
    }
}
