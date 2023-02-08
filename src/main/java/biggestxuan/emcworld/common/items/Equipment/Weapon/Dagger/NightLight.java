package biggestxuan.emcworld.common.items.Equipment.Weapon.Dagger;

/*
 *  EMC WORLD MOD
 *  @Author Biggest_Xuan
 *  2022/12/09
 */

import biggestxuan.emcworld.api.item.equipment.dagger.BaseEMCGodDagger;
import net.minecraft.item.ItemStack;

public class NightLight extends BaseEMCGodDagger {
    @Override
    protected double AttackSpeed(ItemStack stack) {
        return Math.pow(1.02,getLevel(stack));
    }

    @Override
    protected double EMCCost(ItemStack stack) {
        return Math.pow(1.039f,getLevel(stack)*0.975f);
    }

    @Override
    protected long EMCModify(ItemStack stack) {
        return Math.round(Math.pow(1.7,getLevel(stack)));
    }

    @Override
    protected float AddonDamage(ItemStack stack) {
        return getLevel(stack) == 0 ? 0 : (float) (Math.pow(1.14,getLevel(stack)) * 6);
    }
}
