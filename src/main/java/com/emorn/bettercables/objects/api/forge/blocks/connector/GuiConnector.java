package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.common.gui.*;
import com.emorn.bettercables.common.gui.toggle.*;
import com.emorn.bettercables.objects.api.forge.common.Direction;
import com.emorn.bettercables.objects.api.forge.common.Logger;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettings;
import com.emorn.bettercables.objects.gateway.blocks.ConnectorSettingsFilter;
import com.emorn.bettercables.proxy.ModNetworkHandler;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiConnector extends GuiContainer
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/empty_gui_inventory.png"
    );
    private final InventoryPlayer player;
    private final TileEntityConnector tileEntity;
    private final Direction direction;
    private final ArrayList<GuiNumberInput> numberInputs = new ArrayList<>();
    private boolean isInsertSettingsOpen = false;
    private boolean isExtractSettingsOpen = false;
    // key contains changeDisabledStateBasedOnChecked, value = GuiToggle
    private final Map<GuiButton, GuiButton> dynamicEnableOnChecked = new HashMap<>();
    private final Map<GuiButton, GuiButton> dynamicDisableOnChecked = new HashMap<>();
    private Integer filterId = null;

    private static final int INSERT_CHECKBOX_ID = 0;
    private static final int EXTRACT_CHECKBOX_ID = 1;
    private static final int INSERT_SETTINGS_ID = 2;
    private static final int EXTRACT_SETTINGS_ID = 3;
    private static final int CHANNEL_INPUT_ID = 4;
    private static final int OVERWRITE_CHECKBOX_ID = 5;
    private static final int BACK_BUTTON_ID = 6;
    private static final int SLOT_RANGE_BUTTON_ID = 7;
    private static final int MIN_SLOT_RANGE_INPUT_ID = 8;
    private static final int MAX_SLOT_RANGE_INPUT_ID = 9;
    private static final int ORE_DICTIONARY_BUTTON_ID = 10;
    private static final int NBT_DATA_BUTTON_ID = 11;
    private static final int BLACK_LIST_BUTTON_ID = 12;
    private static final int ITEM_COUNT_INPUT_ID = 13;
    private static final int DURABILITY_BUTTON_ID = 14;
    private static final int DURABILITY_INPUT_ID = 15;
    private static final int PRIORITY_INPUT_ID = 16;
    private static final int TICK_RATE_INPUT_ID = 17;
    private static final int DYNAMIC_TICK_RATE_BUTTON_ID = 18;
    private static final int DYNAMIC_TICK_RATE_RANGE_BUTTON_ID = 19;
    private static final int MIN_DYNAMIC_TICK_RATE_RANGE_INPUT_ID = 20;
    private static final int MAX_DYNAMIC_TICK_RATE_RANGE_INPUT_ID = 21;
    private static final int EXTRACT_TYPE_BUTTON_ID = 22;
    private static final int ITEMS_PER_EXTRACT_INPUT_ID = 23;

    private static final int FILTER_START_ID = 24;
    private ConnectorSettingsFilter filter;

    public GuiConnector(
        InventoryPlayer player,
        TileEntityConnector tileEntity,
        Direction direction
    )
    {
        super(new ContainerConnector(player, tileEntity));
        this.player = player;
        this.tileEntity = tileEntity;
        this.direction = direction;
    }


    @Override
    public void initGui()
    {
        super.initGui();

        int checkboxX = this.guiLeft + 26;
        int checkboxInsertY = this.guiTop + 20;
        int checkboxExtractY = this.guiTop + 54;
        int gearX = checkboxX - 20;

        Consumer<Integer> onSettingsClicked = this::onSettingsClicked;

        ConnectorSettings tileSettings = this.tileEntity.settings(this.direction);
        if (tileSettings == null) {
            Logger.error("ConnectorSettings is null");
            return;
        }

        if (this.isInsertSettingsOpen || this.isExtractSettingsOpen) {
            this.settingsMenu(tileSettings);
            return;
        }

        GuiCheckbox insertCheckbox = new GuiCheckbox(
            INSERT_CHECKBOX_ID,
            checkboxX,
            checkboxInsertY,
            "Insert",
            tileEntity.isInsertEnabled(this.direction),
            false
        );

        GuiGear insertSettings = new GuiGear(
            INSERT_SETTINGS_ID,
            gearX,
            checkboxInsertY,
            onSettingsClicked
        );
        GuiCheckbox extractCheckbox = new GuiCheckbox(
            EXTRACT_CHECKBOX_ID,
            checkboxX,
            checkboxExtractY,
            "Extract",
            tileEntity.isExtractEnabled(this.direction),
            false
        );
        GuiGear extractSettings = new GuiGear(
            EXTRACT_SETTINGS_ID,
            gearX,
            checkboxExtractY,
            onSettingsClicked
        );

        this.buttonList.add(insertCheckbox);
        this.buttonList.add(insertSettings);
        this.buttonList.add(extractCheckbox);
        this.buttonList.add(extractSettings);
    }

    private void onSettingsClicked(Integer buttonId)
    {
        ConnectorSettings tileSettings = this.tileEntity.settings(this.direction);
        if (tileSettings == null) {
            Logger.error("ConnectorSettings is null");
            return;
        }

        if (buttonId == INSERT_SETTINGS_ID) {
            this.isInsertSettingsOpen = true;
            this.filter = tileSettings.defaultInsertFilter();
        } else if (buttonId == EXTRACT_SETTINGS_ID) {
            this.isExtractSettingsOpen = true;
            this.filter = tileSettings.defaultExtractFilter();
        }

        this.settingsMenu(tileSettings);
    }

    private void settingsMenu(ConnectorSettings tileSettings)
    {
        this.buttonList.clear();
        drawSettings(tileSettings);
        if (this.isInsertSettingsOpen) {
            drawInsertSettings(tileSettings);
        }

        if (this.isExtractSettingsOpen) {
            drawExtractSettings(tileSettings);
        }

        drawFilters(tileSettings);
    }

    private void drawFilters(ConnectorSettings tileSettings)
    {
        int filtersPerRow = 9;
        int rows = 3;
        int iteration = 0;
        Consumer<Integer> onFilterSettingsClicked = this::onFilterSettingsClicked;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < filtersPerRow; j++) {
                int x = j * 18;
                int y = i * 18;

                ItemStack item;
                if (this.isInsertSettingsOpen) {
                    item = tileSettings.insertFilter(iteration).itemStack();
                } else {
                    item = tileSettings.extractFilter(iteration).itemStack();
                }

                GuiFilter guiFilter = new GuiFilter(
                    FILTER_START_ID + iteration,
                    this.guiLeft + 7 + x,
                    this.guiTop + 17 + y,
                    item,
                    onFilterSettingsClicked
                );

                this.buttonList.add(guiFilter);
                iteration++;
            }
        }


    }

    private void onFilterSettingsClicked(Integer id)
    {
        ConnectorSettings tileSettings = this.tileEntity.settings(this.direction);
        if (tileSettings == null) {
            Logger.error("ConnectorSettings is null");
            return;
        }

        this.filterId = id;

        if (this.isExtractSettingsOpen) {
            this.filter = tileSettings.extractFilter(this.filterId - FILTER_START_ID);
        } else {
            this.filter = tileSettings.insertFilter(this.filterId - FILTER_START_ID);
        }
        this.initGui();
    }

    private void drawSettings(ConnectorSettings tileSettings)
    {
        int channelId;
        if (this.isInsertSettingsOpen) {
            channelId = tileSettings.insertChannelId();
        } else {
            channelId = tileSettings.extractChannelId();
        }

        GuiNumberInput channelInput = new GuiNumberInput(
            CHANNEL_INPUT_ID,
            this.guiLeft + 180,
            this.guiTop + 3,
            channelId,
            TextPosition.RIGHT,
            "Channel",
            0,
            false
        );

        this.buttonList.add(channelInput);
        this.numberInputs.add(channelInput);

        drawDynamic();
    }

    private void drawDynamic()
    {
        int extraY = 0;
        boolean disable = false;
        GuiOverwriteDefaultBox overwriteDefaultBox = null;

        if (filterId != null) {
            Consumer<Integer> onBackClicked = this::onBackClicked;

            extraY = 18 + 18;
            disable = true;

            overwriteDefaultBox = new GuiOverwriteDefaultBox(
                OVERWRITE_CHECKBOX_ID,
                this.guiLeft - 80,
                this.guiTop + 5 + 18,
                this.filter.isOverwriteEnabled()
            );

            this.buttonList.add(overwriteDefaultBox);

            GuiBackButton backButton = new GuiBackButton(
                BACK_BUTTON_ID,
                this.guiLeft - 80,
                this.guiTop + 5,
                onBackClicked
            );

            this.buttonList.add(backButton);
        }

        GuiNumberRangeInput slotInput = new GuiNumberRangeInput(
            SLOT_RANGE_BUTTON_ID,
            MIN_SLOT_RANGE_INPUT_ID,
            MAX_SLOT_RANGE_INPUT_ID,
            this.guiLeft - 80,
            this.guiTop + 5 + extraY,
            this.filter.minSlotRange(),
            this.filter.maxSlotRange(),
            "Slot range",
            -1,
            disable
        );

        this.buttonList.add(slotInput);
        this.buttonList.add(slotInput.minInput());
        this.buttonList.add(slotInput.maxInput());
        this.numberInputs.add(slotInput.minInput());
        this.numberInputs.add(slotInput.maxInput());
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(slotInput, overwriteDefaultBox);
        }

        GuiOreDictionaryBox oreDictionaryBox = new GuiOreDictionaryBox(
            ORE_DICTIONARY_BUTTON_ID,
            this.guiLeft - 80,
            this.guiTop + 100 + extraY,
            this.filter.isOreDictEnabled(),
            disable
        );

        this.buttonList.add(oreDictionaryBox);
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(oreDictionaryBox, overwriteDefaultBox);
        }

        GuiNbtDataBox nbtDataBox = new GuiNbtDataBox(
            NBT_DATA_BUTTON_ID,
            this.guiLeft - (80 - 18),
            this.guiTop + 100 + extraY,
            this.filter.isNbtDataEnabled(),
            disable
        );

        this.buttonList.add(nbtDataBox);
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(nbtDataBox, overwriteDefaultBox);
        }

        GuiBlackListBox blackListBox = new GuiBlackListBox(
            BLACK_LIST_BUTTON_ID,
            this.guiLeft - (80 - 18 - 18),
            this.guiTop + 100 + extraY,
            this.filter.isBlackListEnabled(),
            disable
        );

        this.buttonList.add(blackListBox);
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(blackListBox, overwriteDefaultBox);
        }

        GuiNumberInput itemCount = new GuiNumberInput(
            ITEM_COUNT_INPUT_ID,
            this.guiLeft - 80,
            this.guiTop + 45 + extraY,
            this.filter.itemCount(),
            TextPosition.TOP,
            this.isExtractSettingsOpen ? "Min item count" : "Max item count",
            0,
            disable
        );

        this.buttonList.add(itemCount);
        this.numberInputs.add(itemCount);
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(itemCount, overwriteDefaultBox);
        }

        GuiDurability durability = new GuiDurability(
            DURABILITY_BUTTON_ID,
            DURABILITY_INPUT_ID,
            this.guiLeft - 80,
            this.guiTop + 65 + extraY,
            this.filter.durabilityType(),
            this.filter.durabilityPercentage(),
            disable
        );

        this.buttonList.add(durability);
        this.buttonList.add(durability.numberInput());
        this.buttonList.add(durability.operatorInput());
        this.numberInputs.add(durability.numberInput());
        if (overwriteDefaultBox != null) {
            this.dynamicEnableOnChecked.put(durability, overwriteDefaultBox);
        }
    }

    private void onBackClicked(Integer integer)
    {
        this.filterId = null;
        this.initGui();
    }

    private void drawInsertSettings(ConnectorSettings tileSettings)
    {
        GuiNumberInput priority = new GuiNumberInput(
            PRIORITY_INPUT_ID,
            this.guiLeft + 180,
            this.guiTop + 20,
            tileSettings.priority(),
            TextPosition.RIGHT,
            "Priority",
            -99,
            false
        );

        this.buttonList.add(priority);
        this.numberInputs.add(priority);
    }

    private void drawExtractSettings(ConnectorSettings tileSettings)
    {
        GuiNumberInput tickRate = new GuiNumberInput(
            TICK_RATE_INPUT_ID,
            this.guiLeft + 180,
            this.guiTop + 20,
            tileSettings.tickRate(),
            TextPosition.RIGHT,
            "Tick rate",
            1,
            false
        );

        this.buttonList.add(tickRate);
        this.numberInputs.add(tickRate);

        GuiCheckbox dynamic = new GuiCheckbox(
            DYNAMIC_TICK_RATE_BUTTON_ID,
            this.guiLeft + 180,
            this.guiTop + 35,
            "Dynamic",
            tileSettings.isDynamicTickRateEnabled(),
            false
        );

        this.buttonList.add(dynamic);
        this.dynamicDisableOnChecked.put(tickRate, dynamic);

        GuiNumberRangeInput dynamicRange = new GuiNumberRangeInput(
            DYNAMIC_TICK_RATE_RANGE_BUTTON_ID,
            MIN_DYNAMIC_TICK_RATE_RANGE_INPUT_ID,
            MAX_DYNAMIC_TICK_RATE_RANGE_INPUT_ID,
            this.guiLeft + 180,
            this.guiTop + 55,
            tileSettings.dynamicTickRateMinimum(),
            tileSettings.dynamicTickRateMaximum(),
            "Dynamic range",
            1,
            !dynamic.isChecked()
        );

        this.buttonList.add(dynamicRange);
        this.buttonList.add(dynamicRange.minInput());
        this.buttonList.add(dynamicRange.maxInput());
        this.dynamicEnableOnChecked.put(dynamicRange, dynamic);
        this.numberInputs.add(dynamicRange.minInput());
        this.numberInputs.add(dynamicRange.maxInput());

        GuiExtractTypeButton extractType = new GuiExtractTypeButton(
            EXTRACT_TYPE_BUTTON_ID,
            this.guiLeft + 180,
            this.guiTop + 95,
            tileSettings.extractType()
        );

        this.buttonList.add(extractType);

        GuiNumberInput itemsPerExtract = new GuiNumberInput(
            ITEMS_PER_EXTRACT_INPUT_ID,
            this.guiLeft + 180,
            this.guiTop + 115,
            tileSettings.itemsPerExtract(),
            TextPosition.TOP,
            "Items per action",
            1,
            false
        );

        this.buttonList.add(itemsPerExtract);
        this.numberInputs.add(itemsPerExtract);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(
        int mouseX,
        int mouseY
    )
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        String tileName = this.tileEntity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(
            tileName,
            (this.xSize / 2 - this.fontRenderer.getStringWidth(tileName) / 2) + 3,
            8,
            Reference.TEXT_COLOR
        );
        this.fontRenderer.drawString(
            this.player.getDisplayName().getUnformattedText(),
            122,
            this.ySize - 96 + 2,
            Reference.TEXT_COLOR
        );

        this.renderHoveredToolTip(mouseX - this.guiLeft, mouseY - this.guiTop);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(
        float partialTicks,
        int mouseX,
        int mouseY
    )
    {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 176, 166);

        if (this.isInsertSettingsOpen || this.isExtractSettingsOpen) {
            this.drawSettingsBackground();
        }
    }

    private void drawSettingsBackground()
    {
        this.drawTexturedModalRect(this.guiLeft - 88, this.guiTop, 0, 0, 88, 82);
        this.drawTexturedModalRect(this.guiLeft - 88, this.guiTop + 82, 0, 4, 88, 78);
        this.drawTexturedModalRect(this.guiLeft - 88, this.guiTop + 160, 0, 4, 88, 3);
        this.drawTexturedModalRect(this.guiLeft - 88, this.guiTop + 163, 0, 163, 88, 3);

        this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop, 88, 0, 88, 82);
        this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop + 82, 88, 4, 88, 78);
        this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop + 160, 88, 4, 88, 3);
        this.drawTexturedModalRect(this.guiLeft + 176, this.guiTop + 163, 88, 163, 88, 3);
    }

    @Override
    public void keyTyped(
        char typedChar,
        int keyCode
    ) throws IOException
    {
        for (GuiNumberInput numberInput : this.numberInputs) {
            numberInput.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        for (Map.Entry<GuiButton, GuiButton> entry : this.dynamicEnableOnChecked.entrySet()) {
            if (!(entry.getKey() instanceof AbleToChangeDisabledState)) {
                continue;
            }
            if (!(entry.getValue() instanceof GuiToggle)) {
                continue;
            }
            GuiToggle toggle = (GuiToggle) entry.getValue();
            ((AbleToChangeDisabledState) entry.getKey()).changeDisabledState(!toggle.isChecked());
        }

        for (Map.Entry<GuiButton, GuiButton> entry : this.dynamicDisableOnChecked.entrySet()) {
            if (!(entry.getKey() instanceof AbleToChangeDisabledState)) {
                continue;
            }
            if (!(entry.getValue() instanceof GuiToggle)) {
                continue;
            }
            GuiToggle toggle = (GuiToggle) entry.getValue();
            ((AbleToChangeDisabledState) entry.getKey()).changeDisabledState(toggle.isChecked());
        }

        ConnectorSettings tileSettings = this.tileEntity.settings(this.direction);
        if (tileSettings == null) {
            Logger.error("Tile settings is null");
            return;
        }

        if (button instanceof GuiToggle) {
            updateCheckboxes((GuiToggle) button, tileSettings);
        } else if (button instanceof GuiNumberInput) {
            updateNumberInput((GuiNumberInput) button, tileSettings);
        } else if (button instanceof GuiComparisonOperatorButton) {
            updateDurability((GuiComparisonOperatorButton) button);
        } else if (button instanceof GuiExtractTypeButton) {
            updateExtractType((GuiExtractTypeButton) button, tileSettings);
        } else if (button instanceof GuiFilter) {
            updateFilters((GuiFilter) button, tileSettings);
        }
    }

    private void updateFilters(
        GuiFilter guiFilter,
        ConnectorSettings tileSettings
    )
    {
        tileSettings.changeFilterItem(
            guiFilter.id - FILTER_START_ID,
            this.isInsertSettingsOpen,
            guiFilter.filteredItem()
        );

        ModNetworkHandler.INSTANCE.sendToServer(new PacketUpdateConnector(
            tileEntity.getPos(),
            this.direction,
            tileSettings
        ));
    }

    private void updateExtractType(
        GuiExtractTypeButton button,
        ConnectorSettings tileSettings
    )
    {
        tileSettings.changeExtractType(button.extractType());
    }

    private void updateDurability(
        GuiComparisonOperatorButton guiDurability
    )
    {
        this.filter.changeDurabilityType(guiDurability.comparisonOperator());
    }

    private void updateNumberInput(
        GuiNumberInput numberInput,
        ConnectorSettings tileSettings
    )
    {
        switch (numberInput.id) {
            case CHANNEL_INPUT_ID:
                if (this.isExtractSettingsOpen) {
                    tileSettings.changeExtractChannelId(numberInput.value());
                } else {
                    tileSettings.changeInsertChannelId(numberInput.value());
                }
                break;
            case MIN_SLOT_RANGE_INPUT_ID:
                this.filter.changeMinSlotRange(numberInput.value());
                break;
            case MAX_SLOT_RANGE_INPUT_ID:
                this.filter.changeMaxSlotRange(numberInput.value());
                break;
            case PRIORITY_INPUT_ID:
                tileSettings.changePriority(numberInput.value());
                break;
            case TICK_RATE_INPUT_ID:
                tileSettings.changeTickRate(numberInput.value());
                break;
            case MIN_DYNAMIC_TICK_RATE_RANGE_INPUT_ID:
                tileSettings.changeDynamicTickRateMinimum(numberInput.value());
                break;
            case MAX_DYNAMIC_TICK_RATE_RANGE_INPUT_ID:
                tileSettings.changeDynamicTickRateMaximum(numberInput.value());
                break;
            case ITEMS_PER_EXTRACT_INPUT_ID:
                tileSettings.changeItemsPerExtract(numberInput.value());
                break;
            case DURABILITY_INPUT_ID:
            case ITEM_COUNT_INPUT_ID:
                this.changeFilter(numberInput);
                break;
        }

        ModNetworkHandler.INSTANCE.sendToServer(new PacketUpdateConnector(
            tileEntity.getPos(),
            this.direction,
            tileSettings
        ));
    }

    private void changeFilter(
        GuiNumberInput numberInput
    )
    {
        switch (numberInput.id) {
            case DURABILITY_INPUT_ID:
                this.filter.changeDurabilityPercentage(numberInput.value());
                break;
            case ITEM_COUNT_INPUT_ID:
                this.filter.changeItemCount(numberInput.value());
                break;
        }
    }

    private void changeFilter(
        GuiToggle checkbox
    )
    {
        switch (checkbox.id) {
            case OVERWRITE_CHECKBOX_ID:
                this.filter.changeOverwriteEnabled(checkbox.isChecked());
                break;
            case ORE_DICTIONARY_BUTTON_ID:
                this.filter.changeOreDictEnabled(checkbox.isChecked());
                break;
            case NBT_DATA_BUTTON_ID:
                this.filter.changeNbtDataEnabled(checkbox.isChecked());
                break;
            case BLACK_LIST_BUTTON_ID:
                this.filter.changeBlackListEnabled(checkbox.isChecked());
                break;
        }
    }


    private void updateCheckboxes(
        GuiToggle checkbox,
        ConnectorSettings tileSettings
    )
    {
        switch (checkbox.id) {
            case INSERT_CHECKBOX_ID:
                tileEntity.setInsertEnabled(checkbox.isChecked(), this.direction);
                break;
            case EXTRACT_CHECKBOX_ID:
                tileEntity.setExtractEnabled(checkbox.isChecked(), this.direction);
                break;
            case DYNAMIC_TICK_RATE_BUTTON_ID:
                tileSettings.changeDynamicTickRateEnabled(checkbox.isChecked());
                break;
            case OVERWRITE_CHECKBOX_ID:
            case ORE_DICTIONARY_BUTTON_ID:
            case NBT_DATA_BUTTON_ID:
            case BLACK_LIST_BUTTON_ID:
                this.changeFilter(checkbox);
                break;
        }

        ModNetworkHandler.INSTANCE.sendToServer(new PacketUpdateConnector(
            tileEntity.getPos(),
            this.direction,
            tileSettings
        ));
    }
}