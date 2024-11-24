package net.foxyas.changedaddon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResourcePackDownloader {
    public static void main(String[] args) {
        try {
            String resourcePackUrl = "https://github.com/<owner>/<repo>/releases/download/v1.0.0/your_resource_pack.zip";
            File output = new File("resourcepacks/your_resource_pack.zip");
            ResourcePackDownloader.downloadResourcePack(resourcePackUrl, output);
            System.out.println("Resource pack downloaded successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void downloadResourcePack(String url, File output) throws Exception {
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
