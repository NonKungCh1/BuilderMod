package com.nonkungch.buildermod; // 👈 เปลี่ยนแล้ว

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class ClientBuilderMod implements ClientModInitializer {

    public static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.buildermod.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.buildermod.general"
        ));

        registerClientTickEvent();
        registerRightClickEvent(); 
    }

    public void registerClientTickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            
            while (keyBinding.wasPressed()) {
                BuilderMode.IS_ENABLED = !BuilderMode.IS_ENABLED;
                String message = "§aBuilder Mode: " + (BuilderMode.IS_ENABLED ? "§2ON" : "§cOFF");
                if (client.player != null) {
                    client.player.sendMessage(Text.literal(message), true);
                }
            }
        });
    }

    public void registerRightClickEvent() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            
            if (!BuilderMode.IS_ENABLED || !player.isCreative()) {
                return ActionResult.PASS;
            }

            MinecraftClient client = MinecraftClient.getInstance();
            HitResult hit = client.crosshairTarget;

            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                
                BlockHitResult blockHit = (BlockHitResult) hit;
                BlockPos placePos = blockHit.getBlockPos().offset(blockHit.getSide());
                Block blockToPlace = BuilderMode.getRandomBlock();

                client.interactionManager.placeBlock(
                        player,
                        world,
                        hand,
                        new BlockHitResult(
                                blockHit.getPos(),
                                blockHit.getSide(),
                                placePos,
                                false
                        )
                );
                return ActionResult.CONSUME;
            }
            return ActionResult.PASS;
        });
    }
}
