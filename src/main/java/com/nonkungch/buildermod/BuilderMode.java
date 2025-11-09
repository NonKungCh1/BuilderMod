package com.nonkungch.buildermod;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuilderMode {
    private static final Random RANDOM = new Random();
    private static final List<Block> ALL_BLOCKS = new ArrayList<>();

    static {
        for (Block block : Registries.BLOCK) {
            // ตัดบล็อกที่ไม่น่าจะวางได้ (เช่น air, water, lava)
            if (!block.getDefaultState().isAir()) {
                ALL_BLOCKS.add(block);
            }
        }
    }

    public static Block getRandomBlock() {
        if (ALL_BLOCKS.isEmpty()) {
            throw new IllegalStateException("No blocks found!");
        }
        return ALL_BLOCKS.get(RANDOM.nextInt(ALL_BLOCKS.size()));
    }
}
