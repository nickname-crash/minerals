package com.crashcz.minerals.init;

import java.util.ArrayList;
import java.util.List;

import com.crashcz.minerals.blocks.BlockBase;
import com.crashcz.minerals.blocks.CopperBlock;
import com.crashcz.minerals.blocks.CopperOre;
import com.crashcz.minerals.blocks.Foundry;
import com.crashcz.minerals.blocks.RubyBlock;
import com.crashcz.minerals.blocks.RubyOre;
import com.crashcz.minerals.blocks.TinBlock;
import com.crashcz.minerals.blocks.TinOre;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<Block>();
	
	public static final Block RUBY_BLOCK = new RubyBlock("ruby_block", Material.IRON);
	public static final Block RUBY_ORE = new RubyOre("ruby_ore", Material.ROCK);
	public static final Block COPPER_BLOCK = new CopperBlock("copper_block", Material.IRON);
	public static final Block COPPER_ORE = new CopperOre("copper_ore", Material.ROCK);
	public static final Block TIN_BLOCK = new TinBlock("tin_block", Material.IRON);
	public static final Block TIN_ORE = new TinOre("tin_ore", Material.ROCK);
	public static final Block FOUNDRY = new Foundry("foundry", Material.IRON);
}
