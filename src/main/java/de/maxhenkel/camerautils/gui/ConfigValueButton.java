package de.maxhenkel.camerautils.gui;

import de.maxhenkel.configbuilder.entry.ConfigEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers; // CORREÇÃO: Import necessário
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ConfigValueButton extends AbstractButton {

    private ConfigEntry<Boolean> entry;
    private Function<Boolean, Component> component;

    public ConfigValueButton(int i, int j, int k, int l, ConfigEntry<Boolean> entry, Function<Boolean, Component> component) {
        super(i, j, k, l, Component.literal(""));
        this.entry = entry;
        this.component = component;
        updateText();
    }

    private void updateText() {
        setMessage(component.apply(entry.get()));
    }

    // CORREÇÃO: Assinatura do método corrigida para corresponder à superclasse
    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        entry.set(!entry.get()).save();
        updateText();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }
    
    // CORREÇÃO: Este método não existe mais na superclasse e deve ser removido.
    // O texto é renderizado automaticamente.
}