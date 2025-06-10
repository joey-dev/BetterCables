package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiCopyButton extends net.minecraft.client.gui.GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private static final int WIDTH_OF_IMAGE = 18;
    private static final int HEIGHT_OF_IMAGE = 18;

    private final Consumer<Integer> callback;
    private final Consumer<GuiTooltipData> callbackTooltip;
    private final boolean isOpen;
    private boolean wasCursorOverInputBox = false;

    public GuiCopyButton(
        int buttonId,
        int x,
        int y,
        Consumer<Integer> callback,
        Consumer<GuiTooltipData> callbackTooltip,
        boolean isOpen
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "copy");
        this.callback = callback;
        this.callbackTooltip = callbackTooltip;
        this.isOpen = isOpen;
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
            new String[]{
                "Copy the values from the connected input, to these settings"
            },
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
        int textureY = this.isOpen ? 39 + 13 : 39;
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawTexturedModalRect(this.x, this.y, 36, textureY, 13, 13);
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.callback.accept(this.id);
            return true;
        }
        return false;
    }
}
