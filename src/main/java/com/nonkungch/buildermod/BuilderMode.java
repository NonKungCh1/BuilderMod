package com.nonkungch.buildermod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClientBuilderMod implements ClientModInitializer {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random random = new Random();

    private static KeyBinding toggleKey;
    private static boolean randomizerEnabled = false;
    private static int selectedSlot = 0; // 0 = ช่องที่ 1

    // ---------------- BuilderMode ----------------
    private static final List<Block> ALL_BLOCKS = new ArrayList<>();
    static {
        for (Block block : Registries.BLOCK) {
            // ตัดบล็อกที่ไม่น่าจะวางได้ (air, water, lava)
            if (!block.getDefaultState().isAir()) {
                ALL_BLOCKS.add(block);
            }
        }
    }

    private static Block getRandomBlock() {
        if (ALL_BLOCKS.isEmpty()) {
            throw new IllegalStateException("No blocks found!");
        }
        return ALL_BLOCKS.get(random.nextInt(ALL_BLOCKS.size()));
    }

    // ---------------- Client mod initializer ----------------
    @Override
    public void onInitializeClient() {
        // คีย์ลัดเปิด/ปิดระบบสุ่ม
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.buildermod.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "key.categories.buildermod"
        ));

        // อีเวนต์กดปุ่ม
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                randomizerEnabled = !randomizerEnabled;
                sendMsg("Randomizer " + (randomizerEnabled ? "§aEnabled" : "§cDisabled"));
            }

            // กดปุ่มตัวเลข 1-9 เพื่อเลือกช่อง
            for (int i = 0; i < 9; i++) {
                if (InputUtil.isKeyPressed(client.getWindow().getHandle(), GLFW.GLFW_KEY_1 + i)) {
                    selectedSlot = i;
                    sendMsg("Selected slot: " + (selectedSlot + 1));
                }
            }
        });

        // อีเวนต์วางบล็อก
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (randomizerEnabled && client.player != null && client.interactionManager != null) {
                if (client.player.isUsingItem()) {
                    ItemStack slotStack = client.player.getInventory().getStack(selectedSlot);
                    if (slotStack.getItem() instanceof BlockItem) {
                        Block randomBlock = getRandomBlock();
                        ItemStack newStack = new ItemStack(randomBlock.asItem());
                        client.player.getInventory().setStack(selectedSlot, newStack);
                    }
                }
            }
        });
    }

    private void sendMsg(String msg) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal(msg), false);
        }
    }
}
