package com.emorn.bettercables.objects.blocks.machines.sintering;

import com.emorn.bettercables.utils.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSinteringFurnace extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MODID + ":textures/gui/sintering_furnace.png");
    private final InventoryPlayer player;
    private final TileEntitySinteringFurnace tileEntity;

    public GuiSinteringFurnace(InventoryPlayer player, TileEntitySinteringFurnace tileEntity)
    {
        super(new ContainerSinteringFurnace(player, tileEntity));
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String tileName = this.tileEntity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(tileName, (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3, 8, Reference.TEXT_COLOR);
        this.fontRenderer.drawString(this.player.getDisplayName().getUnformattedText(), 122, this.ySize - 96 + 2, Reference.TEXT_COLOR);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if(TileEntitySinteringFurnace.isBurning(tileEntity))
        {
            int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 54 + 12 - k, 176, 12 - k, 14, k + 1);
        }

        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(this.guiLeft + 44, this.guiTop + 36, 176, 14, l + 1, 16);
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileEntity.getField(1);
        if(i == 0) i = 200;
        return this.tileEntity.getField(0) * pixels / i;
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileEntity.getField(2);
        int j = this.tileEntity.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}