package com.oredetector.keybind;

import com.oredetector.scanner.BaseDetector;
import com.oredetector.scanner.BlockFinder;
import com.oredetector.scanner.OreScanner;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler implements ClientModInitializer {
    private static KeyBinding oreKey;
    private static KeyBinding findKey;
    private static KeyBinding baseKey;
    
    @Override
    public void onInitializeClient() {
        oreKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oredetector.ore", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F6, "category.oredetector"));
        findKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oredetector.find", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F7, "category.oredetector"));
        baseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.oredetector.base", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F8, "category.oredetector"));
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (oreKey.wasPressed()) {
                sendScanCommand(client, "ore");
            }
            while (findKey.wasPressed()) {
                sendScanCommand(client, "find");
            }
            while (baseKey.wasPressed()) {
                sendScanCommand(client, "base");
            }
        });
    }
    
    private void sendScanCommand(MinecraftClient client, String type) {
        ClientPlayerEntity player = client.player;
        if (player != null && player.hasPermissionLevel(2)) {
            // Execute as a command via chat (server-side)
            player.networkHandler.sendChatCommand("scandebug " + type);
        } else if (player != null && !player.hasPermissionLevel(2)) {
            player.sendMessage(net.minecraft.text.Text.literal("§cYou need OP permissions to use OreDetector."), false);
        }
    }
}
