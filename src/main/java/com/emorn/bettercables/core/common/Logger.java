package com.emorn.bettercables.core.common;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Logger
{
    public static final boolean DEBUG = Boolean.parseBoolean(System.getProperty("DEBUG_MODE", "false"));

    private Logger()
    {

    }

    public static void error(String message)
    {
        System.err.println(message);
    }

    public static void debug(String message)
    {
        if (DEBUG) {
            System.out.println(message);
        }
    }
}