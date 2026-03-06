package gg.whirl.gluggglug.mixin;

import gg.whirl.gluggglug.GlugGlugConfig;
import gg.whirl.gluggglug.TotemHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Shadow
    @Final
    protected ScreenHandler handler;

    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;IILnet/minecraft/screen/slot/SlotActionType;)V",
            at = @At("HEAD"), cancellable = true)
    private void gluggglug$onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        GlugGlugConfig config = GlugGlugConfig.get();
        PlayerInventory playerInv = client.player.getInventory();
        ItemStack offhandStack = playerInv.getStack(PlayerInventory.OFF_HAND_SLOT);

        if (actionType == SlotActionType.QUICK_MOVE && slot != null) {
            ItemStack slotStack = slot.getStack();
            if (TotemHelper.isTotem(slotStack)
                    && slot.inventory instanceof PlayerInventory
                    && slot.getIndex() >= 9 && slot.getIndex() <= 35) {

                boolean offhandEnabled = config.clickTotemToOffhand;
                boolean doubleEnabled = config.clickTotemToDoubleHand;
                boolean offhandHasTotem = TotemHelper.isTotem(offhandStack);
                boolean doubleHandHasTotem = TotemHelper.isTotem(playerInv.getStack(config.doubleHandSlot));

                if (offhandEnabled && doubleEnabled) {
                    if (!offhandHasTotem) {
                        ci.cancel();
                        client.interactionManager.clickSlot(
                                handler.syncId, slot.id, 40,
                                SlotActionType.SWAP, client.player);
                    } else if (!doubleHandHasTotem) {
                        ci.cancel();
                        client.interactionManager.clickSlot(
                                handler.syncId, slot.id, config.doubleHandSlot,
                                SlotActionType.SWAP, client.player);
                    }
                    // both slots already have totems — fall through to default behaviour
                } else if (offhandEnabled && !offhandHasTotem) {
                    ci.cancel();
                    client.interactionManager.clickSlot(
                            handler.syncId, slot.id, 40,
                            SlotActionType.SWAP, client.player);
                } else if (doubleEnabled && !doubleHandHasTotem) {
                    ci.cancel();
                    client.interactionManager.clickSlot(
                            handler.syncId, slot.id, config.doubleHandSlot,
                            SlotActionType.SWAP, client.player);
                }
            }
        }

        if (config.blockInvUntotem && actionType == SlotActionType.SWAP && button == 40) {
            if (TotemHelper.isTotem(offhandStack)) {
                if (slot == null || !TotemHelper.isTotem(slot.getStack())) {
                    ci.cancel();
                }
            }
        }
    }
}