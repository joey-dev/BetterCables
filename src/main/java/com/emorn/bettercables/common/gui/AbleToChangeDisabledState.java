package com.emorn.bettercables.common.gui;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface AbleToChangeDisabledState
{
    public void changeDisabledState(boolean disabled);
}
