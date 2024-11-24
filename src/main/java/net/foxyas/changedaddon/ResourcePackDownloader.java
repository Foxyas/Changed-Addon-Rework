package net.foxyas.changedaddon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResourcePackDownloader {
    public static void main(String[] args) {
        try {
            // 资源包下载链接
            String resourcePackUrl = "https://raw.githubusercontent.com/gengyoubo/Changed-Addon-Rework/main/resourcepack/translationpack.zip";

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

            // 最终的文件保存路径
            File output = new File(resourcePackDir, "translationpack.zip");

            // 调用下载方法
            downloadResourcePack(resourcePackUrl, output);

            System.out.println("Resource pack downloaded successfully to: " + output.getCanonicalPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadResourcePack(String url, File output) throws Exception {
        // 创建保存路径的父文件夹（如果不存在）
        File parentDir = output.getParentFile();
        if (!parentDir.exists() && !parentDir.mkdirs()) {
            throw new RuntimeException("Failed to create directories: " + parentDir.getAbsolutePath());
        }

        // 下载文件
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
        }
        connection.disconnect();
    }
}
