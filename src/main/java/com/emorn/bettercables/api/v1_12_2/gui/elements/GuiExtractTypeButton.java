package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.contract.gui.ExtractType;
import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiExtractTypeButton extends GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );
    private static final int WIDTH_OF_IMAGE = 18;
    private static final int HEIGHT_OF_IMAGE = 18;

    private ExtractType extractType;
    private final Consumer<GuiTooltipData> callbackTooltip;
    private boolean wasCursorOverInputBox = false;

    public GuiExtractTypeButton(
        int buttonId,
        int x,
        int y,
        ExtractType extractType,
        Consumer<GuiTooltipData> callbackTooltip
    )
    {
        super(buttonId, x, y, WIDTH_OF_IMAGE, HEIGHT_OF_IMAGE, "");
        this.extractType = extractType;
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
            new String[]{
                "Determine where it will extract the items to",
                "Round Robin: Extracts to the first input, then the second, and so on",
                "Priority: Extracts to the input with the highest priority first",
                "Closest First: Extracts to the closest input first",
                "Furthest First: Extracts to the furthest input first"
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
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        if (this.extractType.equals(ExtractType.ROUND_ROBIN)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.PRIORITY)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 2,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.CLOSEST_FIRST)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 3,
                18,
                18
            );
        } else if (this.extractType.equals(ExtractType.FURTHEST_FIRST)) {
            drawTexturedModalRect(
                this.x,
                this.y,
                18,
                18 * 4,
                18,
                18
            );
        }
    }

    @Override
    public boolean mousePressed(
        net.minecraft.client.Minecraft mc,
        int mouseX,
        int mouseY
    )
    {
        if (!this.isButtonPressed(mouseX, mouseY)) {
            return false;
        }

        if (this.extractType.equals(ExtractType.ROUND_ROBIN)) {
            this.extractType = ExtractType.PRIORITY;
        } else if (this.extractType.equals(ExtractType.PRIORITY)) {
            this.extractType = ExtractType.CLOSEST_FIRST;
        } else if (this.extractType.equals(ExtractType.CLOSEST_FIRST)) {
            this.extractType = ExtractType.FURTHEST_FIRST;
        } else if (this.extractType.equals(ExtractType.FURTHEST_FIRST)) {
            this.extractType = ExtractType.ROUND_ROBIN;
        }

        return true;
    }


    private boolean isButtonPressed(
        int mouseX,
        int mouseY
    )
    {
        return
            (mouseX > this.x && mouseX < this.x + 18) &&
                (mouseY > this.y && mouseY < this.y + 18)
            ;
    }

    public ExtractType extractType()
    {
        return extractType;
    }
}
