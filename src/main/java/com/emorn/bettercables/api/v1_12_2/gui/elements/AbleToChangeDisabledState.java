package com.emorn.bettercables.api.v1_12_2.gui.elements;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface AbleToChangeDisabledState
{
    public void changeDisabledState(boolean disabled);
}
