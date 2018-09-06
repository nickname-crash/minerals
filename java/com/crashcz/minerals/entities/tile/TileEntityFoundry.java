package com.crashcz.minerals.entities.tile;

import com.crashcz.minerals.blocks.Foundry;
import com.crashcz.minerals.init.FoundryRecipes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import scala.NotNull;

public class TileEntityFoundry extends TileEntity implements ITickable
{
	private ItemStackHandler handler = new ItemStackHandler(4);
	private String customName;
	private ItemStack smelting = ItemStack.EMPTY;
	
	private int burnTime;
	private int currentBurnTime;
	private int cookTime;
	private int totalCookTime = 200;
	
	public TileEntityFoundry()
	{
		this.smelting = FoundryRecipes.getInstance().getFoundryResult(this.handler.getStackInSlot(0), this.handler.getStackInSlot(1));
	}
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		else return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) this.handler;
		return super.getCapability(capability, facing);
	}
	
	public boolean hasCustomName() 
	{
		return this.customName != null && !this.customName.isEmpty();
	}
	
	public void setCustomName(String customName) 
	{
		this.customName = customName;
	}
	
	@Override
	public ITextComponent getDisplayName() 
	{
		return this.hasCustomName() ? new TextComponentString(this.customName) : new TextComponentTranslation("container.sintering_furnace");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.handler.deserializeNBT(compound.getCompoundTag("Inventory"));
		this.burnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentBurnTime = getItemBurnTime((ItemStack)this.handler.getStackInSlot(2));
		
		if(compound.hasKey("CustomName", 8)) this.setCustomName(compound.getString("CustomName"));
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) 
	{
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short)this.burnTime);
		compound.setInteger("CookTime", (short)this.cookTime);
		compound.setInteger("CookTimeTotal", (short)this.totalCookTime);
		compound.setTag("Inventory", this.handler.serializeNBT());
		
		if(this.hasCustomName()) compound.setString("CustomName", this.customName);
		return compound;
	}
	
	public boolean isBurning() 
	{
		return this.burnTime > 0;
	}
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public static boolean isBurning(TileEntityFoundry te) 
	{
		return te.getField(0) > 0;
	}
	public void update() 
	{	
		boolean flag = this.isBurning();
		boolean flag1 = false;
		
		if(this.isBurning())
			--this.burnTime;
		
		if(!this.world.isRemote)
		{
			ItemStack stack = this.handler.getStackInSlot(2);
			
			if(this.isBurning() || !stack.isEmpty() && !((((ItemStack)this.handler.getStackInSlot(0)).isEmpty()) || ((ItemStack)this.handler.getStackInSlot(1)).isEmpty()))
			{
				System.out.println("isBurning, slot filled");
				if(!this.isBurning() && this.canSmelt())
				{
					System.out.println("continue1");
					this.burnTime = getItemBurnTime(stack);
					this.currentBurnTime = this.burnTime;
					
					if(this.isBurning())
					{
						System.out.println("continue2");
						flag1 = true;
						
						if(!stack.isEmpty())
						{
							Item item = stack.getItem();
							stack.shrink(1);
							
							if(stack.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(stack);
								this.handler.setStackInSlot(2, item1);
							}
						}
					}
				}
				if(this.isBurning() && this.canSmelt())
				{
					System.out.println("continue1 else");
					++this.cookTime;
					if(this.cookTime == this.totalCookTime)
					{
						System.out.println("cook time = total cook time");
						if(this.handler.getStackInSlot(3).getCount() > 0)
						{
							System.out.println("Slot3 filled, growing");
							this.handler.getStackInSlot(3).grow(1);
						}
						else
						{
							System.out.println("Slot3 empty, inserting smelting item");
							this.smelting = FoundryRecipes.getInstance().getFoundryResult(this.handler.getStackInSlot(0), this.handler.getStackInSlot(1));
							this.handler.insertItem(3, this.smelting, false);
						}
						System.out.println("reset");
						this.cookTime = 0;
						return;
					}
				}
				else
				{
					System.out.println("cantSmelt");
					this.cookTime = 0;
				}
			}
			else if(!this.isBurning() && this.cookTime > 0)
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			if(flag != this.isBurning())
			{
				System.out.println("setting state");
				flag1 = true;
				Foundry.setState(this.isBurning(), this.world, this.pos);
			}
		}
		if(flag1)
			this.markDirty();
	}
	
	public int getCookTime(ItemStack input1, ItemStack input2)
	{
		return 200;
	}
	private boolean canSmelt() 
	{
		if(((ItemStack)this.handler.getStackInSlot(0)).isEmpty() || ((ItemStack)this.handler.getStackInSlot(1)).isEmpty())
		{
			System.out.println("inputs empty");
			return false;
		}
		else 
		{
			ItemStack result = FoundryRecipes.getInstance().getFoundryResult(handler.getStackInSlot(0), handler.getStackInSlot(1));	
			if(result.isEmpty())
			{
				System.out.println("result does'nt match any recipe" + result);
				return false;
			}
			else
			{
				System.out.print(result);
				ItemStack output = (ItemStack)this.handler.getStackInSlot(3);
				if(output.isEmpty()) return true;
				if(!output.isItemEqual(result))
				{
					System.out.println("item in slot3 doesnt match result item");
					return false;
				}
				System.out.println("should be ok");
				int res = output.getCount() + result.getCount();
				return res <= 64 && res <= output.getMaxStackSize();
			}
		}
	}
	
	public static int getItemBurnTime(ItemStack fuel) 
	{
		if(fuel.isEmpty()) return 0;
		else 
		{
			Item item = fuel.getItem();

			if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.AIR) 
			{
				Block block = Block.getBlockFromItem(item);

				if (block == Blocks.WOODEN_SLAB) return 150;
				if (block.getDefaultState().getMaterial() == Material.WOOD) return 300;
				if (block == Blocks.COAL_BLOCK) return 16000;
			}

			if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName())) return 200;
			if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName())) return 200;
			if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName())) return 200;
			if (item == Items.STICK) return 100;
			if (item == Items.COAL) return 1600;
			if (item == Items.LAVA_BUCKET) return 20000;
			if (item == Item.getItemFromBlock(Blocks.SAPLING)) return 100;
			if (item == Items.BLAZE_ROD) return 2400;

			return GameRegistry.getFuelValue(fuel);
		}
	}
		
	public static boolean isItemFuel(ItemStack fuel)
	{
		return getItemBurnTime(fuel) > 0;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
	}

	public int getField(int id) 
	{
		switch(id) 
		{
		case 0:
			return this.burnTime;
		case 1:
			return this.currentBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	public void setField(int id, int value) 
	{
		switch(id) 
		{
		case 0:
			this.burnTime = value;
			break;
		case 1:
			this.currentBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
}
}