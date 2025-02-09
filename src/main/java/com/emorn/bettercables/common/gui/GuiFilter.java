package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiFilter extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private ItemStack filteredItem = ItemStack.EMPTY;
    private final Consumer<Integer> callback;

    public GuiFilter(
        int buttonId,
        int x,
        int y,
        ItemStack filteredItem,
        Consumer<Integer> callback
    )
    {
        super(buttonId, x, y, 18, 18, "");
        this.filteredItem = filteredItem;
        this.callback = callback;
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
            drawTexturedModalRect(
                this.x + 14,
                this.y + 1,
                36,
                18,
                3,
                3
            );

            RenderItem renderItem = mc.getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.x + 1f, this.y + 1f, 0);

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

        if (this.clickedOnSettings(mouseX, mouseY)) {
            this.callback.accept(this.id);
            return true;
        }

        ItemStack heldItem = mc.player.inventory.getItemStack();

        if (!heldItem.isEmpty()) {
            this.filteredItem = heldItem.copy();
        } else {
            this.filteredItem = ItemStack.EMPTY;
        }
        return true;
    }

    private boolean clickedOnSettings(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x + 14 && mouseX < this.x + 17) &&
                (mouseY > this.y + 1 && mouseY < this.y + 4)
            ;
    }

    public ItemStack filteredItem()
    {
        return this.filteredItem;
    }
}
