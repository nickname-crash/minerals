package com.crashcz.minerals.util.handlers;

import com.crashcz.minerals.containers.FoundryContainer;
import com.crashcz.minerals.containers.gui.FoundryGUI;
import com.crashcz.minerals.entities.tile.TileEntityFoundry;
import com.crashcz.minerals.util.Reference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == Reference.GUI_FOUNDRY)
			return new FoundryContainer(player.inventory, (TileEntityFoundry)world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == Reference.GUI_FOUNDRY)
			return new FoundryGUI(player.inventory, (TileEntityFoundry)world.getTileEntity(new BlockPos(x, y, z)));
		return null;
	}
}
