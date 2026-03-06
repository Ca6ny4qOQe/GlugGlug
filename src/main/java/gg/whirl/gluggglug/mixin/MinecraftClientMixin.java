package gg.whirl.gluggglug.mixin;

import gg.whirl.gluggglug.GlugGlugConfig;
import gg.whirl.gluggglug.TotemHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    public ClientPlayerEntity player;

    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void gluggglug$preHandleInput(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;
        if (player == null) return;
        if (client.currentScreen != null) return;
        if (!GlugGlugConfig.get().blockSwapUntotem) return;

        PlayerInventory inv = player.getInventory();
        ItemStack offhand = inv.getStack(PlayerInventory.OFF_HAND_SLOT);

        if (TotemHelper.isTotem(offhand)) {
            ItemStack mainhand = inv.getSelectedStack();
            if (!TotemHelper.isTotem(mainhand)) {
                while (client.options.swapHandsKey.wasPressed()) {
                }
            }
        }
    }
}