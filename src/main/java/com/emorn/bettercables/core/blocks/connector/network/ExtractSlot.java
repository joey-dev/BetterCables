package com.emorn.bettercables.core.blocks.connector.network;

import com.emorn.bettercables.core.blocks.connector.settings.ConnectorSettings;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtractSlot
{
    private final int extractIndex;
    private int enabledAt;
    private final Map<ConnectorSettings, InsertInventory> slotsPerInsertConnector = new HashMap<>();

    public ExtractSlot(int extractIndex)
    {
        this.extractIndex = extractIndex;
    }

    public int extractIndex()
    {
        return extractIndex;
    }

    public int enabledAt()
    {
        return enabledAt;
    }

    @Nullable
    public InsertInventory find(ConnectorSettings insertSettings)
    {
        return slotsPerInsertConnector.get(insertSettings);
    }

    public void addInsert(
        ConnectorSettings insertSettings,
        List<InsertSlot> insertSlots
    )
    {
        this.slotsPerInsertConnector.remove(insertSettings);
        this.slotsPerInsertConnector.put(insertSettings, new InsertInventory(insertSlots));
    }

    public void removeInsert(ConnectorSettings insertSettings)
    {
        this.slotsPerInsertConnector.remove(insertSettings);
    }
}
