package net.foxyas.changedaddon.tools.java_code_generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class AutoWriter {

    public static final String ADVANCED_HUMANOID_RENDERER_TEMPLATE = "AdvancedHumanoidRendererTemplate.txt";
    public static final String CHANGED_ENTITY_TEMPLATE_FILE = "ChangedEntityTemplate.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("   Terminal Fox - Code Generation Script    ");
        System.out.println("=================================================");
        System.out.println("1 [ Renderer Template ]");
        System.out.println("2 [ Entity Class Template ]");
        System.out.print("Choose an option: ");
        
        String option = scanner.nextLine();

        // Common questions for all templates
        System.out.print("\n-> Enter Base Package (e.g., com.foxyas.changedaddon): ");
        String packageName = scanner.nextLine();
        System.out.print("-> Enter Base Entity Name (e.g., SnowLeopard): ");
        String entityName = scanner.nextLine();

        switch (option) {
            case "1" -> generateRenderer(scanner, packageName, entityName);
            case "2" -> generateEntity(scanner, packageName, entityName);
            default -> System.out.println("Invalid option selected!");
        }
        scanner.close();
    }

    // ================= ADVANCED HUMANOID RENDERER GENERATOR =================
    private static void generateRenderer(Scanner scanner, String packageName, String entityName) {
        System.out.print("-> Enter Main Mod Class path (e.g., net.foxyas.changedaddon.ChangedAddonMod): ");
        String modMainClass = scanner.nextLine();

        // Extracting just the class name from the full path (e.g., ChangedAddonMod)
        String modMainClassName = modMainClass.substring(modMainClass.lastIndexOf('.') + 1);

        System.out.print("-> Enter Model Class Name (e.g., LatexCalicoCatModel): ");
        String modelName = scanner.nextLine();

        System.out.print("-> Enter Armor Model Class Name (e.g., ArmorLatexMaleCatModel): ");
        String armorModelClass = scanner.nextLine();

        System.out.print("-> Enter Texture Path inside textures/entities/ (e.g., latex_calico_cat/latex_calico_cat): ");
        String texturePath = scanner.nextLine();

        try {
            String content = Files.readString(Paths.get("RendererTemplate.txt"));
            content = content.replace("${PackageName}", packageName)
                    .replace("${EntityName}", entityName)
                    .replace("${ModMainClass}", modMainClass)
                    .replace("${ModMainClassName}", modMainClassName)
                    .replace("${ModelName}", modelName)
                    .replace("${ArmorModelClass}", armorModelClass)
                    .replace("${TexturePath}", texturePath);

            // Saves directly into the client/renderer/basic folder tree structure
            saveFile(packageName, "client/renderer/basic", entityName + "Renderer.java", content);
        } catch (IOException e) {
            System.out.println("Error reading Renderer template: " + e.getMessage());
        }
    }

    // ================= ENTITY GENERATOR =================
    private static void generateEntity(Scanner scanner, String packageName, String entityName) {
        // Questions to code mapping for attributes
        System.out.print("-> Max Health (e.g., 20): ");
        String maxHealth = scanner.nextLine();
        System.out.print("-> Movement Speed (e.g., 0.3): ");
        String speed = scanner.nextLine();
        System.out.print("-> Attack Damage (e.g., 4): ");
        String attackDamage = scanner.nextLine();
        System.out.print("-> Armor Points (e.g., 2): ");
        String armor = scanner.nextLine();

        try {
            String content = Files.readString(Paths.get(CHANGED_ENTITY_TEMPLATE_FILE));
            content = content.replace("${PackageName}", packageName)
                             .replace("${EntityName}", entityName)
                             .replace("${MaxHealth}", maxHealth)
                             .replace("${MovementSpeed}", speed)
                             .replace("${AttackDamage}", attackDamage)
                             .replace("${Armor}", armor);

            saveFile(packageName, "entity", entityName + "Entity.java", content);
        } catch (IOException e) {
            System.out.println("Error reading Entity template: " + e.getMessage());
        }
    }

    // Helper method to write files into the proper directory tree
    private static void saveFile(String packageName, String subFolder, String fileName, String content) throws IOException {
        String packageFolder = packageName.replace('.', '/');
        Path outputPath = Paths.get("src/main/java/" + packageFolder + "/" + subFolder + "/" + fileName);

        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, content);

        System.out.println("\n[✓] Success! File generated at: " + outputPath.toAbsolutePath());
    }
}