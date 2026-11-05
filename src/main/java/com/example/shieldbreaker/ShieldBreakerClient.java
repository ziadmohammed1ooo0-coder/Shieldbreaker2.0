package com.example.shieldbreaker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ShieldBreakerClient implements ClientModInitializer {

    private static KeyBinding shieldBreakKey;
    private static boolean breaking = false;
    private static int returnSlot = 0;
    private static int axeSlot = 1;
    private static int clickDelay = 2;
    private static int clickTimer = 0;

    @Override
    public void onInitializeClient() {
        shieldBreakKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shieldbreaker.break",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "Shield Breaker"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) {
            breaking = false;
            return;
        }

        if (shieldBreakKey.isPressed()) {
            if (!breaking) {
                breaking = true;
                returnSlot = client.player.getInventory().selectedSlot;
                client.player.getInventory().selectedSlot = axeSlot;
                clickTimer = 0;
            }

            if (clickTimer <= 0) {
                client.doAttack();
                clickTimer = clickDelay;
            } else {
                clickTimer--;
            }
        } else if (breaking) {
            breaking = false;
            client.player.getInventory().selectedSlot = returnSlot;
        }
    }
}
