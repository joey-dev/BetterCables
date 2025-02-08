package com.emorn.bettercables.objects.api.forge.common;

import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Logger
{
    private Logger()
    {

    }

    public static void error(String message)
    {
        System.err.println(message);
    }

    public static void info(String message)
    {
        System.out.println(message);
    }
}