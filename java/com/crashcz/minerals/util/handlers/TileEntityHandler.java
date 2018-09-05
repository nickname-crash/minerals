package com.crashcz.minerals.util.handlers;

import com.crashcz.minerals.entities.tile.TileEntityFoundry;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntityHandler
{
	public static void registerTileEntities()
	{
		GameRegistry.registerTileEntity(TileEntityFoundry.class, "foundry");
	}
}
