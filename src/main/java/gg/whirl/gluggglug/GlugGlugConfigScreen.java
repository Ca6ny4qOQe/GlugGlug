package gg.whirl.gluggglug;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class GlugGlugConfigScreen extends Screen {
    private final Screen parent;
    private final GlugGlugConfig config;

    public GlugGlugConfigScreen(Screen parent) {
        super(Text.literal("GlugGlug Config"));
        this.parent = parent;
        this.config = GlugGlugConfig.get();
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height / 4;

        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.clickTotemToOffhand)
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Shift Click Totem to Offhand"),
                                (button, value) -> config.clickTotemToOffhand = value)
        );

        y += 28;
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.clickTotemToDoubleHand)
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Shift Click Totem to Double Hand"),
                                (button, value) -> config.clickTotemToDoubleHand = value)
        );

        y += 28;
        this.addDrawableChild(
                new SliderWidget(centerX - 100, y, 200, 20,
                        Text.literal("Double Hand Slot: " + (config.doubleHandSlot + 1)),
                        config.doubleHandSlot / 8.0) {
                    @Override
                    protected void updateMessage() {
                        int slot = MathHelper.clamp((int) Math.round(this.value * 8), 0, 8);
                        this.setMessage(Text.literal("Double Hand Slot: " + (slot + 1)));
                    }

                    @Override
                    protected void applyValue() {
                        config.doubleHandSlot = MathHelper.clamp((int) Math.round(this.value * 8), 0, 8);
                    }
                }
        );

        y += 28;
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.blockInvUntotem)
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Block Inventory Untotem"),
                                (button, value) -> config.blockInvUntotem = value)
        );

        y += 28;
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.blockSwapUntotem)
                        .build(centerX - 100, y, 200, 20,
                                Text.literal("Block Swap Hands Untotem"),
                                (button, value) -> config.blockSwapUntotem = value)
        );

        y += 40;
        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Done"), button -> close())
                        .dimensions(centerX - 100, y, 200, 20)
                        .build()
        );
    }

    @Override
    public void close() {
        config.save();
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}
