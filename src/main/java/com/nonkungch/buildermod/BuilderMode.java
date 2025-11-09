package com.nonkungch.buildermod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class BuilderMode {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Random RANDOM = new Random();

    // ช่องที่อนุญาตให้สลับ (1–9)
    private static int minSlot = 1;
    private static int maxSlot = 9;

    // ตั้งค่าช่วงช่องที่จะสุ่ม
    public static void setSlotRange(int min, int max) {
        minSlot = Math.max(1, Math.min(min, 9));
        maxSlot = Math.max(minSlot, Math.min(max, 9));
    }

    // คืนค่าช่วงปัจจุบัน
    public static String getSlotRangeString() {
        return minSlot + "-" + maxSlot;
    }

    // ฟังก์ชันสุ่มสลับบล็อกในมือ
    public static void randomizeHeldBlock() {
        if (client.player == null || client.player.getInventory() == null) return;

        int randomSlot = RANDOM.nextInt(maxSlot - minSlot + 1) + minSlot - 1; // 0-index
        ItemStack newStack = client.player.getInventory().getStack(randomSlot);

        // สลับบล็อกในมือกับบล็อกที่สุ่มได้
        int selectedSlot = client.player.getInventory().selectedSlot;
        client.player.getInventory().setStack(selectedSlot, newStack.copy());
    }
}
