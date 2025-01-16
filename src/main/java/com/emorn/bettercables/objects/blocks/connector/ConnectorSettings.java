package com.emorn.bettercables.objects.blocks.connector;

public class ConnectorSettings
{
    private boolean isInsertEnabled;
    private boolean isExtractEnabled;

    public ConnectorSettings(
        final boolean isInsertEnabled,
        final boolean isExtractEnabled
    )
    {
        this.isInsertEnabled = isInsertEnabled;
        this.isExtractEnabled = isExtractEnabled;
    }

    public boolean isInsertEnabled()
    {
        return this.isInsertEnabled;
    }

    public boolean isExtractEnabled()
    {
        return this.isExtractEnabled;
    }

    public void enableExtract()
    {
        this.isExtractEnabled = true;
    }

    public void disableExtract()
    {
        this.isExtractEnabled = false;
    }

    public ConnectorSettings enableInsert()
    {
        this.isInsertEnabled = true;
        return this;
    }

    public ConnectorSettings disableInsert()
    {
        this.isInsertEnabled = false;
        return this;
    }
}
