package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCheckbox extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/checkbox_gui.png"
    );
    private boolean isChecked;

    public GuiCheckbox(
        int buttonId,
        int x,
        int y,
        String buttonText,
        boolean isChecked
    )
    {
        super(buttonId, x, y, 18, 18, buttonText);
        this.isChecked = isChecked;
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
    
    private void setupRenderState(net.minecraft.client.Minecraft mc) {
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

        drawTexturedModalRect(this.x, this.y, 0, 0, 18, 18);

        if (isChecked) {
            drawTexturedModalRect(this.x + 2, this.y + 2, 18, 0, 14, 14);
        }

        mc.fontRenderer.drawString(
            displayString,
            this.x + this.width + 5,
            this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2,
            Reference.TEXT_COLOR
        );
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.isChecked = !this.isChecked;
            return true;
        }
        return false;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        this.isChecked = checked;
    }
}
