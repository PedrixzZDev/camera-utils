package de.maxhenkel.camerautils.gui;

import com.mojang.blaze3d.systems.RenderSystem; // CORREÇÃO: Import necessário para renderização
import de.maxhenkel.camerautils.CameraUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent; // CORREÇÃO: Import para o evento de mouse
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation; // Usando o nome correto para versões modernas
import net.minecraft.sounds.SoundEvents;

public class SettingsScreenBase extends CameraScreenBase {

    private static final ResourceLocation VISIBILITY = ResourceLocation.fromNamespaceAndPath(CameraUtils.MODID, "textures/visibility.png");
    protected ResourceLocation texture;

    private HoverArea visibilityArea;
    private float opacity;

    public SettingsScreenBase(Component title, ResourceLocation texture, int xSize, int ySize) {
        super(title, xSize, ySize);
        this.texture = texture;
        this.opacity = CameraUtils.CLIENT_CONFIG.guiOpacity.get().floatValue();
    }

    @Override
    protected void init() {
        super.init();
        visibilityArea = new HoverArea(xSize - 7 - 16, 4, 16, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        
        // CORREÇÃO: O método setColor foi removido. Use RenderSystem.
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.opacity);
        guiGraphics.blit(this.texture, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F); // Reseta a cor para não afetar outros elementos
        RenderSystem.disableBlend();

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        int titleWidth = font.width(getTitle());
        guiGraphics.drawString(font, getTitle(), guiLeft + (xSize - titleWidth) / 2, guiTop + 7, FONT_COLOR, false);

        if (visibilityArea.isHovered(guiLeft, guiTop, mouseX, mouseY)) {
            guiGraphics.blit(VISIBILITY, guiLeft + xSize - 7 - 16, guiTop + 4, 16, 0, 16, 16, 32, 32);
        } else {
            guiGraphics.blit(VISIBILITY, guiLeft + xSize - 7 - 16, guiTop + 4, 0, 0, 16, 16, 32, 32);
        }
    }

    // CORREÇÃO: A assinatura do método mudou.
    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean bl) {
        if (visibilityArea.isHovered(guiLeft, guiTop, (int) mouseButtonEvent.x(), (int) mouseButtonEvent.y())) {
            opacity -= 0.25F;
            if (opacity < 0.25F) {
                opacity = 1.0F;
            }
            CameraUtils.CLIENT_CONFIG.guiOpacity.set((double) opacity);
            CameraUtils.CLIENT_CONFIG.guiOpacity.save();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            return true;
        }
        return super.mouseClicked(mouseButtonEvent, bl);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

}