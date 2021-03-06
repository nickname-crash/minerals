package com.crashcz.minerals.init;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FoundryRecipes
{
	private static final FoundryRecipes INSTANCE = new FoundryRecipes();
	private final Table<ItemStack, ItemStack, ItemStack> smeltingList = HashBasedTable.<ItemStack, ItemStack, ItemStack>create();
	private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();
	
	public static FoundryRecipes getInstance()
	{
		return INSTANCE;
	}
	
	public FoundryRecipes() 
	{
		addSinteringRecipe(new ItemStack(ModBlocks.COPPER_BLOCK), new ItemStack(ModBlocks.TIN_BLOCK), new ItemStack(ModBlocks.RUBY_BLOCK), 5.0F);
	}

	
	public void addSinteringRecipe(ItemStack input1, ItemStack input2, ItemStack result, float experience) 
	{
		if(getFoundryResult(input1, input2) != ItemStack.EMPTY) return;
		smeltingList.put(input1, input2, result);
		experienceList.put(result, Float.valueOf(experience));
	}
	
	public ItemStack getFoundryResult(ItemStack input1, ItemStack input2) 
	{
		for(Entry<ItemStack, Map<ItemStack, ItemStack>> entry : smeltingList.columnMap().entrySet()) 
		{
			if(compareItemStacks(input1, (ItemStack)entry.getKey())) 
			{
				System.out.println("found something");
				for(Entry<ItemStack, ItemStack> ent : entry.getValue().entrySet()) 
				{
					if(compareItemStacks(input2, (ItemStack)ent.getKey())) 
					{
						System.out.println("found result " + ent.getValue());
						return (ItemStack)ent.getValue();
					}
				}
			}
		}
		System.out.println("no result found");
		return ItemStack.EMPTY;
	}
	
	private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
	{
		return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
	}
	
	public Table<ItemStack, ItemStack, ItemStack> getDualSmeltingList() 
	{
		return this.smeltingList;
	}
	
	public float getFoundryExperience(ItemStack stack)
	{
		for (Entry<ItemStack, Float> entry : this.experienceList.entrySet()) 
		{
			if(this.compareItemStacks(stack, (ItemStack)entry.getKey())) 
			{
				return ((Float)entry.getValue()).floatValue();
			}
		}
		return 0.0F;
	}
}
