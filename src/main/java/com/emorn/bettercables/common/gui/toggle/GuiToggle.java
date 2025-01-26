package com.emorn.bettercables.common.gui.toggle;

import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiToggle extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private final ToggleImagePosition inactive;
    private final ToggleImagePosition active;
    private boolean isChecked;

    public GuiToggle(
        int buttonId,
        int x,
        int y,
        String buttonText,
        boolean isChecked,
        ToggleImagePosition inactiveTogglePosition,
        ToggleImagePosition activeTogglePosition
    )
    {
        super(buttonId, x, y, 18, 18, buttonText);
        this.isChecked = isChecked;
        this.inactive = inactiveTogglePosition;
        this.active = activeTogglePosition;
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

        if (isChecked) {
            drawTexturedModalRect(
                this.active.x,
                this.active.y,
                this.active.textureX,
                this.active.textureY,
                this.active.width,
                this.active.height
            );
        } else {
            drawTexturedModalRect(
                this.inactive.x,
                this.inactive.y,
                this.inactive.textureX,
                this.inactive.textureY,
                this.inactive.width,
                this.inactive.height
            );
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
}
