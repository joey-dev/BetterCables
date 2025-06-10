package com.emorn.bettercables.api.v1_12_2.gui.elements.toggle;

import com.emorn.bettercables.api.v1_12_2.gui.elements.AbleToChangeDisabledState;
import com.emorn.bettercables.api.v1_12_2.gui.elements.GuiTooltipData;
import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiToggle extends GuiButton implements AbleToChangeDisabledState
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private static final int WIDTH_OF_IMAGE = 18;
    private static final int HEIGHT_OF_IMAGE = 18;

    private final ToggleImagePosition inactive;
    private final ToggleImagePosition active;
    private final ToggleImagePosition disabled;
    private final Consumer<GuiTooltipData> callbackTooltip;
    private boolean isChecked;
    private final String[] description;
    private boolean isDisabled;
    private boolean wasCursorOverInputBox = false;

    public GuiToggle(
        int buttonId,
        int x,
        int y,
        String buttonText,
        String[] description,
        boolean isChecked,
        ToggleImagePosition inactiveTogglePosition,
        ToggleImagePosition activeTogglePosition,
        ToggleImagePosition disabledTogglePosition,
        boolean disabled,
        Consumer<GuiTooltipData> callbackTooltip
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, buttonText);
        this.description = description;
        this.isDisabled = disabled;
        this.isChecked = isChecked;
        this.inactive = inactiveTogglePosition;
        this.active = activeTogglePosition;
        this.disabled = disabledTogglePosition;
        this.callbackTooltip = callbackTooltip;
    }

    @Override
    public void drawButton(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY,
        float partialTicks
    )
    {
        if (!this.visible) {
            return;
        }
        setupRenderState(mc);

        boolean isCursorOverInputBox = this.isMouseOverInputBox(mouseX, mouseY);

        GuiTooltipData guiTooltipData = new GuiTooltipData(
            this.x,
            this.y + 1000,
            this.description,
            0,
            false,
            0,
            false,
            false
        );

        if (isCursorOverInputBox) {
            this.callbackTooltip.accept(
                guiTooltipData
            );
        } else if (this.wasCursorOverInputBox) {
            guiTooltipData.setDisabled();
            this.callbackTooltip.accept(
                guiTooltipData
            );
        }

        this.wasCursorOverInputBox = isCursorOverInputBox;
    }

    private boolean isMouseOverInputBox(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x && mouseX < this.x + WIDTH_OF_IMAGE) &&
                (mouseY > this.y && mouseY < this.y + HEIGHT_OF_IMAGE)
            ;
    }

    private void setupRenderState(net.minecraft.client.Minecraft mc)
    {
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (isDisabled) {
            drawTexturedModalRect(
                this.disabled.x,
                this.disabled.y,
                this.disabled.textureX,
                this.disabled.textureY,
                this.disabled.width,
                this.disabled.height
            );
        } else if (isChecked) {
            drawTexturedModalRect(
                this.active.x,
                this.active.y,
                this.active.textureX,
                this.active.textureY,
                this.active.width,
                this.active.height
            );
        } else {
            drawTexturedModalRect(
                this.inactive.x,
                this.inactive.y,
                this.inactive.textureX,
                this.inactive.textureY,
                this.inactive.width,
                this.inactive.height
            );
        }

        mc.fontRenderer.drawString(
            displayString,
            this.x + this.width + 5,
            this.y + (this.height - mc.fontRenderer.FONT_HEIGHT) / 2,
            Reference.TEXT_COLOR
        );
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!isDisabled && super.mousePressed(mc, mouseX, mouseY)) {
            this.isChecked = !this.isChecked;
            return true;
        }
        return false;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    @Override
    public void changeDisabledState(boolean disabled)
    {
        this.isDisabled = disabled;
    }
}
