package com.emorn.bettercables.api.v1_12_2.gui.elements;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;
import static net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiTooltip extends GuiButton implements AbleToChangeDisabledState
{
    private static final int WIDTH_OF_IMAGE = 27;
    private static final int HEIGHT_OF_IMAGE = 13;
    private String[] description;
    private int minimumValue;
    private int maximumValue;
    private boolean isDisabled;
    private boolean hasMinimumValue;
    private boolean hasMaximumValue;

    public GuiTooltip(
        int buttonId,
        GuiTooltipData tooltipData
    )
    {
        super(buttonId, tooltipData.positionX(), tooltipData.positionY(), WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "");

        this.update(tooltipData);
    }

    public void update(
        GuiTooltipData tooltipData
    ) {
        this.description = tooltipData.description();
        this.minimumValue = tooltipData.minimumValue();
        this.hasMinimumValue = tooltipData.hasMinimumValue();
        this.maximumValue = tooltipData.maximumValue();
        this.hasMaximumValue = tooltipData.hasMaximumValue();
        this.isDisabled = tooltipData.isDisabled();
    }

    @Override
    public void drawButton(
        Minecraft mc,
        int mouseX,
        int mouseY,
        float partialTicks
    )
    {
        if (partialTicks < 0.1) {
            this.isDisabled = true;
        }
        if (!this.visible) {
            return;
        }

        if (this.isDisabled) {
            return;
        }

        List<String> tooltip = new ArrayList<>(Arrays.asList(this.description));

        if (this.hasMinimumValue) {
            tooltip.add("Minimum value: " + this.minimumValue);
        }

        if (this.hasMaximumValue) {
            tooltip.add("Maximum value: " + this.maximumValue);
        }

        int tooltipWidth = 0;
        for (String line : tooltip) {
            int lineWidth = mc.fontRenderer.getStringWidth(line);
            if (lineWidth > tooltipWidth) {
                tooltipWidth = lineWidth;
            }
        }
        int tooltipX = mouseX - tooltipWidth - 16;
        if (tooltipX < 0) tooltipX = 0;

        if (mouseX > mc.displayWidth / 5) {
            tooltipX = mouseX;
        } else {
            tooltipWidth = min(tooltipWidth, mouseX - 16);
        }

        if (tooltipX + tooltipWidth + 16 > mc.displayWidth / 3) {
            tooltipWidth = mc.displayWidth / 6;
        }

        drawHoveringText(tooltip, tooltipX, mouseY, mc.displayWidth, mc.displayHeight, tooltipWidth, mc.fontRenderer);
    }

    @Override
    public void changeDisabledState(boolean disabled)
    {
        this.isDisabled = disabled;
    }
}