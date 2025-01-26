package com.emorn.bettercables.common.gui;

import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiComparisonOperatorButton extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );

    private ComparisonOperator comparisonOperator;
    private final GuiNumberInput numberInput;
    private int stringWidth = 0;
    private boolean disabled;

    public GuiComparisonOperatorButton(
        int buttonId,
        int x,
        int y,
        String buttonText,
        ComparisonOperator comparisonOperator,
        int initialValue,
        boolean disable
    )
    {
        super(buttonId, x, y, 18, 18, buttonText);
        this.comparisonOperator = comparisonOperator;
        this.disabled = disable;

        this.numberInput = new GuiNumberInput(
            buttonId,
            x,
            y + 15,
            initialValue,
            TextPosition.RIGHT,
            "",
            -1,
            disable
        );
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
        stringWidth = mc.fontRenderer.getStringWidth(this.displayString);
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (this.comparisonOperator.equals(ComparisonOperator.EQUALS)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                89,
                2,
                9,
                9
            );
        } else if (this.comparisonOperator.equals(ComparisonOperator.GREATER_THAN)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                97,
                2,
                9,
                9
            );
        } else if (this.comparisonOperator.equals(ComparisonOperator.LESS_THAN)) {
            drawTexturedModalRect(
                this.x + stringWidth,
                this.y + 2,
                104,
                2,
                9,
                9
            );
        }

        mc.fontRenderer.drawString(
            displayString,
            this.x,
            this.y + ((this.height - 3) - mc.fontRenderer.FONT_HEIGHT) / 2,
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
        if (disabled || !this.isButtonPressed(mouseX, mouseY)) {
            return false;
        }

        if (this.comparisonOperator.equals(ComparisonOperator.EQUALS)) {
            this.comparisonOperator = ComparisonOperator.GREATER_THAN;
        } else if (this.comparisonOperator.equals(ComparisonOperator.GREATER_THAN)) {
            this.comparisonOperator = ComparisonOperator.LESS_THAN;
        } else if (this.comparisonOperator.equals(ComparisonOperator.LESS_THAN)) {
            this.comparisonOperator = ComparisonOperator.EQUALS;
        }

        return true;
    }


    private boolean isButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x + this.stringWidth && mouseX < this.x + this.stringWidth + 9) &&
                (mouseY > this.y + 2 && mouseY < this.y + 2 + 9)
            ;
    }

    public ComparisonOperator comparisonOperator()
    {
        return comparisonOperator;
    }

    public GuiNumberInput numberInput()
    {
        return this.numberInput;
    }

    public void changeDisabledState(boolean disabled)
    {
        this.disabled = disabled;
        this.numberInput.changeDisabledState(disabled);
    }
}
