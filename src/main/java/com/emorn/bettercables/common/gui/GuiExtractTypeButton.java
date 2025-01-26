package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiExtractTypeButton extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private ExtractType extractType;

    public GuiExtractTypeButton(
        int buttonId,
        int x,
        int y,
        String buttonText,
        ExtractType extractType
    )
    {
        super(buttonId, x, y, 18, 18, buttonText);
        this.extractType = extractType;
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

        if (this.extractType.equals(ExtractType.ROUND_ROBIN)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.PRIORITY)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 2,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.CLOSEST_FIRST)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 3,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.FURTHEST_FIRST)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 4,
                18,
                18
            );
        }
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!this.isButtonPressed(mouseX, mouseY)) {
            return false;
        }

        if (this.extractType.equals(ExtractType.ROUND_ROBIN)) {
            this.extractType = ExtractType.PRIORITY;
        } else if (this.extractType.equals(ExtractType.PRIORITY)) {
            this.extractType = ExtractType.CLOSEST_FIRST;
        } else if (this.extractType.equals(ExtractType.CLOSEST_FIRST)) {
            this.extractType = ExtractType.FURTHEST_FIRST;
        } else if (this.extractType.equals(ExtractType.FURTHEST_FIRST)) {
            this.extractType = ExtractType.ROUND_ROBIN;
        }

        return true;
    }


    private boolean isButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x && mouseX < this.x + 18) &&
                (mouseY > this.y && mouseY < this.y + 18)
            ;
    }

    public ExtractType extractType()
    {
        return extractType;
    }
}
