package net.foxyas.changedaddon;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class ChangedAddonModConfig {

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> PotionSynthesis;
        public Common(ForgeConfigSpec.Builder builder){
            builder.comment("Turning this on will disable registration of potions for this mod. If you are not the modpack author, please turn this off.");
            PotionSynthesis= builder.define("PotionSynthesis",false);
        }

    }
    public static class Client {
        public Client(ForgeConfigSpec.Builder builder){

        }
    }
    public static class Server {
        public Server(ForgeConfigSpec.Builder builder){

        }
    }
    private final Pair<Common, ForgeConfigSpec> commonPair;
    private final Pair<Client, ForgeConfigSpec> clientPair;
    private final Pair<Server, ForgeConfigSpec> serverPair;
    public final Common common;
    public final Client client;
    public final Server server;

    public ChangedAddonModConfig(ModLoadingContext context) {
        commonPair = new ForgeConfigSpec.Builder()
                .configure(Common::new);
        clientPair = new ForgeConfigSpec.Builder()
                .configure(Client::new);
        serverPair = new ForgeConfigSpec.Builder()
                .configure(Server::new);

        context.registerConfig(ModConfig.Type.COMMON, commonPair.getRight());
        context.registerConfig(ModConfig.Type.CLIENT, clientPair.getRight());
        context.registerConfig(ModConfig.Type.SERVER, serverPair.getRight());
        common = commonPair.getLeft();
        client = clientPair.getLeft();
        server = serverPair.getLeft();
    }
}
