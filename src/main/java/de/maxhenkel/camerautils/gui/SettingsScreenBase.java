package de.maxhenkel.camerautils.gui;

import de.maxhenkel.camerautils.CameraUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Identifier; // CORREÇÃO: Import alterado

public class SettingsScreenBase extends CameraScreenBase {

    // CORREÇÃO: Tipos alterados para Identifier
    private static final Identifier VISIBILITY = Identifier.of(CameraUtils.MODID, "textures/visibility.png");
    protected Identifier texture;

    private HoverArea visibilityArea;
    private float opacity;

    // CORREÇÃO: Tipo do parâmetro alterado para Identifier
    public SettingsScreenBase(Component title, Identifier texture, int xSize, int ySize) {
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
        // CORREÇÃO: O método renderBackground agora lida com o fundo escurecido
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        
        // CORREÇÃO: Atualizado para o novo sistema de renderização com opacidade
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
        guiGraphics.blit(this.texture, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        int titleWidth = font.width(getTitle());
        guiGraphics.drawString(font, getTitle(), guiLeft + (xSize - titleWidth) / 2, guiTop + 7, FONT_COLOR, false);

        // CORREÇÃO: Chamadas de blit atualizadas
        if (visibilityArea.isHovered(guiLeft, guiTop, mouseX, mouseY)) {
            guiGraphics.blit(VISIBILITY, guiLeft + xSize - 7 - 16, guiTop + 4, 16, 0, 16, 16, 32, 32);
        } else {
            guiGraphics.blit(VISIBILITY, guiLeft + xSize - 7 - 16, guiTop + 4, 0, 0, 16, 16, 32, 32);
        }
    }

    // CORREÇÃO: Assinatura do método mouseClicked atualizada para o padrão
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (visibilityArea.isHovered(guiLeft, guiTop, (int) mouseX, (int) mouseY)) {
            opacity -= 0.25F;
            if (opacity < 0.25F) { // Evita ficar totalmente invisível
                opacity = 1F;
            }
            CameraUtils.CLIENT_CONFIG.guiOpacity.set((double) opacity);
            CameraUtils.CLIENT_CONFIG.guiOpacity.save();
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1F));
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}