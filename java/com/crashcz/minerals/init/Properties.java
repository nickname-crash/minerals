package com.crashcz.minerals.init;

import net.minecraft.init.Blocks;

public class Properties
{
	public static void setCustomIronOre()
	{
		Blocks.IRON_ORE.setHarvestLevel("pickaxe", 3);
	}
}
