package com.emorn.bettercables.api.v1_12_2.gui.elements;

import com.emorn.bettercables.core.common.Reference;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GuiGear extends net.minecraft.client.gui.GuiButton
{
    private static final ResourceLocation TEXTURES = new ResourceLocation(
        Reference.MODID + ":textures/gui/gui_elements.png"
    );

    private final Consumer<Integer> callback;

    public GuiGear(
        int buttonId,
        int x,
        int y,
        Consumer<Integer> callback
    )
    {
        super(buttonId, x, y, 18, 18, "settings");
        this.callback = callback;
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
    }

    private void setupRenderState(net.minecraft.client.Minecraft mc)
    {
        mc.getTextureManager().bindTexture(TEXTURES);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawTexturedModalRect(this.x, this.y, 0, 18, 18, 18);
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
