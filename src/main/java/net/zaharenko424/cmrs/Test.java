package net.zaharenko424.cmrs;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.RoundedButton;
import net.zaharenko424.cmrs.client.gui.widget.RoundedRectWidget;
import net.zaharenko424.cmrs.client.gui.widget.WidgetContainer;

import java.awt.*;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class Test {

    @SubscribeEvent
    public static void a(InputEvent.Key event) {
        if (event.getKey() != InputConstants.KEY_I) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != null) return;

        minecraft.setScreen(new TestScreen());
    }

    static class TestScreen extends Screen implements MouseMoveListener {

        final WidgetContainer window = new WidgetContainer().setSize(200, 100);
        final RoundedRectWidget background = new RoundedRectWidget().setSize(200, 100).setInsideColorFunc(a -> Color.GREEN.getRGB());
        final RoundedButton button = new RoundedButton().setRoundingRadius(5).setSize(50, 25).setText(Component.literal("Text").withStyle(ChatFormatting.AQUA))
                .setOrigin(0, 0, 10).setRenderTransform(WidgetHelper.hoverAnim(.1f, 0.025f, 0.025f));

        protected TestScreen() {
            super(Component.literal("A"));

            background.rebuildMesh();

            List<String> list = List.of("Hi", "Hello", "Hai", "hi");
            button.setOnClick((b, key) -> {
                Player player = Minecraft.getInstance().player;
                if (player == null) return false;

                player.displayClientMessage(Component.literal(list.get(player.getRandom().nextInt(4))), true);
                return true;
            });
            button.rebuildMesh();

            window.addWidget(background);
            window.addWidget(button);
            window.init();
        }

        @Override
        protected void init() {
            window.setOrigin(width / 2f, height / 2f, 0);
            addRenderableWidget(window);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
