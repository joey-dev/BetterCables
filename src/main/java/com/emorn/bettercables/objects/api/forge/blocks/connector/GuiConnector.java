package com.emorn.bettercables.objects.api.forge.blocks.connector;

import com.emorn.bettercables.common.gui.toggle.GuiCheckbox;
import com.emorn.bettercables.common.gui.GuiGear;
import com.emorn.bettercables.common.gui.GuiNumberInput;
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
        this.buttonList.clear();
        drawSettings();
        if (buttonId == 2) {
            drawInsertSettings();
        } else if (buttonId == 4) {
            drawExtractSettings();
        }
    }

    private void drawSettings()
    {
        GuiNumberInput channelInput = new GuiNumberInput(
            0,
            this.guiLeft + 10,
            this.guiTop + 15,
            0,
            "Channel Id"
        );

        this.buttonList.add(channelInput);
        this.numberInputs.add(channelInput);
    }

    private void drawInsertSettings()
    {

    }

    private void drawExtractSettings()
    {

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