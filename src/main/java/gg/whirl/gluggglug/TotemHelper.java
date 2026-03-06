package gg.whirl.gluggglug;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class TotemHelper {
    private TotemHelper() {}

    public static boolean isTotem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}