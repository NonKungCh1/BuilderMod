package com.nonkungch.buildermod; // 👈 เปลี่ยนแล้ว

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.List;
import java.util.Random;

public class BuilderMode {
    
    public static boolean IS_ENABLED = false; 

    private static final List<Block> BLOCK_PALETTE = List.of(
            Blocks.TUFF,
            Blocks.TUFF_BRICKS,
            Blocks.CHISELED_TUFF_BRICKS,
            Blocks.POLISHED_TUFF,
            Blocks.STONE,
            Blocks.COBBLESTONE,
            Blocks.ANDESITE
    );

    private static final Random random = new Random();

    public static Block getRandomBlock() {
        return BLOCK_PALETTE.get(random.nextInt(BLOCK_PALETTE.size()));
    }
}
