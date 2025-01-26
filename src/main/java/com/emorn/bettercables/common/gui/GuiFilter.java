package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiFilter extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private ItemStack filteredItem = ItemStack.EMPTY;

    public GuiFilter(
        int buttonId,
        int x,
        int y
    )
    {
        super(buttonId, x, y, 18, 18, "");
    }

    @Override
    public void drawButton(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY,
        float partialTicks
    )
    {
        if (!this.visible) {
            return;
        }
        setupRenderState(mc);
    }

    private void setupRenderState(net.minecraft.client.Minecraft mc)
    {
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        drawTexturedModalRect(
            this.x,
            this.y,
            36,
            0,
            this.width,
            this.height
        );

        if (!this.filteredItem.isEmpty()) {
            RenderItem renderItem = mc.getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.x + 1, this.y + 1, 0);

            RenderHelper.enableGUIStandardItemLighting();

            this.filteredItem.setCount(1);
            renderItem.renderItemAndEffectIntoGUI(this.filteredItem, 0, 0);
            renderItem.renderItemOverlayIntoGUI(mc.fontRenderer, this.filteredItem, 0, 0, null);

            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!super.mousePressed(mc, mouseX, mouseY)) {
            return false;
        }

        ItemStack heldItem = mc.player.inventory.getItemStack();

        if (!heldItem.isEmpty()) {
            this.filteredItem = heldItem.copy();
        } else {
            this.filteredItem = ItemStack.EMPTY;
        }
        return true;
    }

    public ItemStack filteredItem()
    {
        return this.filteredItem;
    }
}
