package com.emorn.bettercables.objects.blocks.connector;

import com.emorn.bettercables.common.gui.GuiCheckbox;
import com.emorn.bettercables.proxy.ModNetworkHandler;
import com.emorn.bettercables.utils.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

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

        GuiCheckbox insertCheckbox = new GuiCheckbox(
            0,
            checkboxX,
            checkboxInsertY,
            "Insert",
            tileEntity.isInsertEnabled(this.direction)
        );
        GuiCheckbox extractCheckbox = new GuiCheckbox(
            1,
            checkboxX,
            checkboxExtractY,
            "Extract",
            tileEntity.isExtractEnabled(this.direction)
        );

        this.buttonList.add(insertCheckbox);
        this.buttonList.add(extractCheckbox);
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