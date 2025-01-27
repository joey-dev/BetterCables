package com.emorn.bettercables.objects.gateway.blocks;

import com.emorn.bettercables.common.gui.ComparisonOperator;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ConnectorSettingsFilter
{
    private boolean isOverwriteEnabled = false;
    private int slotMinimumRange = -1;
    private int slotMaximumRange = -1;
    private boolean isOreDictEnabled = false;
    private boolean isNbtDataEnabled = false;
    private boolean isBlackListEnabled = false;
    private int itemCount = -1;
    private ComparisonOperator durabilityType = ComparisonOperator.EQUALS;
    private int durabilityPercentage = -1;

    public boolean isOverwriteEnabled()
    {
        return isOverwriteEnabled;
    }

    public void changeOverwriteEnabled(boolean overwriteEnabled)
    {
        isOverwriteEnabled = overwriteEnabled;
    }

    public int slotMinimumRange()
    {
        return slotMinimumRange;
    }

    public void changeSlotMinimumRange(int slotMinimumRange)
    {
        this.slotMinimumRange = slotMinimumRange;
    }

    public int slotMaximumRange()
    {
        return slotMaximumRange;
    }

    public void changeSlotMaximumRange(int slotMaximumRange)
    {
        this.slotMaximumRange = slotMaximumRange;
    }

    public boolean isOreDictEnabled()
    {
        return isOreDictEnabled;
    }

    public void changeOreDictEnabled(boolean oreDictEnabled)
    {
        isOreDictEnabled = oreDictEnabled;
    }

    public boolean isNbtDataEnabled()
    {
        return isNbtDataEnabled;
    }

    public void changeNbtDataEnabled(boolean nbtDataEnabled)
    {
        isNbtDataEnabled = nbtDataEnabled;
    }

    public boolean isBlackListEnabled()
    {
        return isBlackListEnabled;
    }

    public void changeBlackListEnabled(boolean blackListEnabled)
    {
        isBlackListEnabled = blackListEnabled;
    }

    public int itemCount()
    {
        return itemCount;
    }

    public void changeItemCount(int itemCount)
    {
        this.itemCount = itemCount;
    }

    public ComparisonOperator durabilityType()
    {
        return durabilityType;
    }

    public void changeDurabilityType(ComparisonOperator durabilityType)
    {
        this.durabilityType = durabilityType;
    }

    public int durabilityPercentage()
    {
        return durabilityPercentage;
    }

    public void changeDurabilityPercentage(int durabilityPercentage)
    {
        this.durabilityPercentage = durabilityPercentage;
    }
}