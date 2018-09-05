package com.crashcz.minerals.containers;

import com.crashcz.minerals.entities.tile.TileEntityFoundry;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class FoundryFuelSlot extends Slot
{
	public FoundryFuelSlot(IInventory inventory, int index, int x, int y)
	{
		super(inventory, index, x, y);
	}
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return TileEntityFoundry.isItemFuel(stack);
	}
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return super.getItemStackLimit(stack);
	}
}
