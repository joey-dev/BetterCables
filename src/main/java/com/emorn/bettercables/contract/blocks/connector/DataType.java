package com.emorn.bettercables.contract.blocks.connector;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public enum DataType
{
    BOOLEAN, // = TAG_BYTE
    STRING,
    COMPOUND,
    LIST,
    INTEGER;
}