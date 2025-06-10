package com.emorn.bettercables.api.v1_12_2.gui.elements;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiTooltipData
{
    private final int positionX;
    private final int positionY;
    private final String[] description;
    private final int minimumValue;
    private final boolean hasMinimumValue;
    private final int maximumValue;
    private final boolean hasMaximumValue;
    private boolean isDisabled;

    public GuiTooltipData(
        int x,
        int y,
        String[] description,
        int minimumValue,
        boolean hasMinimumValue,
        int maximumValue,
        boolean hasMaximumValue,
        boolean isDisabled
    )
    {
        this.positionX = x;
        this.positionY = y;
        this.description = description;
        this.minimumValue = minimumValue;
        this.hasMinimumValue = hasMinimumValue;
        this.maximumValue = maximumValue;
        this.hasMaximumValue = hasMaximumValue;
        this.isDisabled = isDisabled;
    }

    public int positionX()
    {
        return this.positionX;
    }

    public int positionY()
    {
        return this.positionY;
    }

    public String[] description()
    {
        return this.description;
    }

    public int minimumValue()
    {
        return this.minimumValue;
    }

    public boolean hasMinimumValue()
    {
        return this.hasMinimumValue;
    }

    public int maximumValue()
    {
        return this.maximumValue;
    }

    public boolean hasMaximumValue()
    {
        return this.hasMaximumValue;
    }

    public boolean isDisabled()
    {
        return this.isDisabled;
    }

    public void setDisabled()
    {
        this.isDisabled = true;
    }
}