package com.crashcz.minerals.containers.gui;

import com.crashcz.minerals.containers.FoundryContainer;
import com.crashcz.minerals.entities.tile.TileEntityFoundry;
import com.crashcz.minerals.util.Reference;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class FoundryGUI extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID + ":textures/gui/foundry.png");
	private final InventoryPlayer player;
	private final TileEntityFoundry tileEntity;
	
	public FoundryGUI(InventoryPlayer player, TileEntityFoundry tileEntity)
	{
		super(new FoundryContainer(player, tileEntity));
		this.player = player;
		this.tileEntity = tileEntity;
	}
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		String tileName = this.tileEntity.getDisplayName().getUnformattedComponentText();
		this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3, 8, 4210752);
		this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 96 + 2, 4210752);
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1, 1, 1);
		this.mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		
		if(TileEntityFoundry.isBurning(tileEntity))
		{
			int k = this.getBurnLeftScale(13);
			this.drawTexturedModalRect(this.guiLeft + 58, this.guiTop + 38 + 12 - k, 176, 12 - k, 14, k + 1);
			int l = this.getCookProgressScaleId(24);
			this.drawTexturedModalRect(this.guiLeft + 80, this.guiTop + 35, 176, 14, l + 1, 16);
		}
	}
	private int getBurnLeftScale(int pixels)
	{
		int i = this.tileEntity.getField(1);
		if(i == 0)
			i = 200;
		return this.tileEntity.getField(0) * pixels / i;
	}
	private int getCookProgressScaleId(int pixels)
	{
		int i = this.tileEntity.getField(2);
		int j = this.tileEntity.getField(3);
		return j != 0 && i != 0 ? i * pixels / j : 0;
	}
}