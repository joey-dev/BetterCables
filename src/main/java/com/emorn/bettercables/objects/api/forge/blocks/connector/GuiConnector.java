package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.common.gui.*;
import com.emorn.bettercables.common.gui.toggle.GuiCheckbox;
import com.emorn.bettercables.common.gui.toggle.GuiNbtDataBox;
import com.emorn.bettercables.common.gui.toggle.GuiOreDictionaryBox;
import com.emorn.bettercables.common.gui.toggle.GuiToggle;
import com.emorn.bettercables.objects.api.forge.common.Direction;
import com.emorn.bettercables.proxy.ModNetworkHandler;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
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

        if (this.isInsertSettingsOpen || this.isExtractSettingsOpen) {
            this.settingsMenu();
            return;
        }

        GuiCheckbox insertCheckbox = new GuiCheckbox(
            0,
            checkboxX,
            checkboxInsertY,
            "Insert",
            tileEntity.isInsertEnabled(this.direction)
        );

        GuiGear insertSettings = new GuiGear(
            2,
            gearX,
            checkboxInsertY,
            onSettingsClicked
        );
        GuiCheckbox extractCheckbox = new GuiCheckbox(
            1,
            checkboxX,
            checkboxExtractY,
            "Extract",
            tileEntity.isExtractEnabled(this.direction)
        );
        GuiGear extractSettings = new GuiGear(
            4,
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
        if (buttonId == 2) {
            this.isInsertSettingsOpen = true;
        } else if (buttonId == 4) {
            this.isExtractSettingsOpen = true;
        }

        this.settingsMenu();
    }

    private void settingsMenu()
    {
        this.buttonList.clear();
        drawSettings();
        if (this.isInsertSettingsOpen) {
            drawInsertSettings();
            return;
        }

        if (this.isExtractSettingsOpen) {
            drawExtractSettings();
        }

        drawFilters();
    }

    private void drawFilters()
    {
        int filtersPerRow = 9;
        int rows = 3;
        int iteration = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < filtersPerRow; j++) {
                int x = j * 18;
                int y = i * 18;

                GuiFilter filter = new GuiFilter(
                    20 + iteration,
                    this.guiLeft + 7 + x,
                    this.guiTop + 17 + y
                );

                this.buttonList.add(filter);
                iteration++;
            }
        }


    }

    private void drawSettings()
    {
        GuiNumberInput channelInput = new GuiNumberInput(
            0,
            this.guiLeft + 180,
            this.guiTop + 3,
            0,
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
        GuiNumberRangeInput slotInput = new GuiNumberRangeInput(
            1,
            2,
            3,
            this.guiLeft - 80,
            this.guiTop + 5,
            -1,
            -1,
            "Slot range",
            -1,
            false
        );

        this.buttonList.add(slotInput);
        this.buttonList.add(slotInput.minInput());
        this.buttonList.add(slotInput.maxInput());
        this.numberInputs.add(slotInput.minInput());
        this.numberInputs.add(slotInput.maxInput());

        GuiOreDictionaryBox oreDictionaryBox = new GuiOreDictionaryBox(
            4,
            this.guiLeft - 80,
            this.guiTop + 100,
            false
        );

        this.buttonList.add(oreDictionaryBox);

        GuiNbtDataBox nbtDataBox = new GuiNbtDataBox(
            5,
            this.guiLeft - (80 - 18),
            this.guiTop + 100,
            false
        );

        this.buttonList.add(nbtDataBox);

        GuiNumberInput itemCount = new GuiNumberInput(
            6,
            this.guiLeft - 80,
            this.guiTop + 45,
            0,
            TextPosition.TOP,
            "Min item count",
            0,
            false
        );

        this.buttonList.add(itemCount);
        this.numberInputs.add(itemCount);

        GuiDurability durability = new GuiDurability(
            7,
            8,
            this.guiLeft - 80,
            this.guiTop + 65,
            ComparisonOperator.EQUALS,
            -1
        );

        this.buttonList.add(durability);
        this.buttonList.add(durability.numberInput());
        this.buttonList.add(durability.operatorInput());
        this.numberInputs.add(durability.numberInput());
    }

    private void drawInsertSettings()
    {

    }

    private void drawExtractSettings()
    {
        GuiNumberInput tickRate = new GuiNumberInput(
            9,
            this.guiLeft + 180,
            this.guiTop + 20,
            1,
            TextPosition.RIGHT,
            "Tick rate",
            1,
            false
        );

        this.buttonList.add(tickRate);
        this.numberInputs.add(tickRate);

        GuiCheckbox dynamic = new GuiCheckbox(
            10,
            this.guiLeft + 180,
            this.guiTop + 35,
            "Dynamic",
            false
        );

        this.buttonList.add(dynamic);
        this.dynamicDisableOnChecked.put(tickRate, dynamic);

        GuiNumberRangeInput dynamicRange = new GuiNumberRangeInput(
            11,
            12,
            13,
            this.guiLeft + 180,
            this.guiTop + 55,
            1,
            999,
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
            14,
            this.guiLeft + 180,
            this.guiTop + 95,
            "Dynamic",
            ExtractType.ROUND_ROBIN
        );

        this.buttonList.add(extractType);

        GuiNumberInput itemsPerExtract = new GuiNumberInput(
            15,
            this.guiLeft + 180,
            this.guiTop + 115,
            1,
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
        super.drawGuiContainerForegroundLayer(mouseX, mouseY); // Ensure default behavior

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
            if (!(entry.getKey() instanceof GuiNumberRangeInput)) {
                continue;
            }
            if (!(entry.getValue() instanceof GuiToggle)) {
                continue;
            }
            GuiToggle toggle = (GuiToggle) entry.getValue();
            ((GuiNumberRangeInput) entry.getKey()).changeDisabledState(!toggle.isChecked());
        }

        for (Map.Entry<GuiButton, GuiButton> entry : this.dynamicDisableOnChecked.entrySet()) {
            if (!(entry.getKey() instanceof GuiNumberInput)) {
                continue;
            }
            if (!(entry.getValue() instanceof GuiToggle)) {
                continue;
            }
            GuiToggle toggle = (GuiToggle) entry.getValue();
            ((GuiNumberInput) entry.getKey()).changeDisabledState(toggle.isChecked());
        }

        if (button instanceof GuiCheckbox) {
            updateCheckboxes((GuiCheckbox) button);
        }
    }

    private void updateCheckboxes(GuiCheckbox checkbox)
    {
        switch (checkbox.id) {
            case 0:
                tileEntity.setInsertEnabled(checkbox.isChecked(), this.direction);
                break;
            case 1:
                tileEntity.setExtractEnabled(checkbox.isChecked(), this.direction);
                break;
        }

        ModNetworkHandler.INSTANCE.sendToServer(new PacketUpdateConnector(
            tileEntity.getPos(),
            tileEntity.isInsertEnabled(this.direction),
            tileEntity.isExtractEnabled(this.direction),
            this.direction
        ));
    }
}