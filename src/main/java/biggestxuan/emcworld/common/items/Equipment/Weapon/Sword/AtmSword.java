package biggestxuan.emcworld.common.items.Equipment.Weapon.Sword;

/*
 *  EMC WORLD MOD
 *  @Author Biggest_Xuan
 *  2022/10/19
 */

import biggestxuan.emcworld.api.item.INeedLevelItem;
import biggestxuan.emcworld.api.item.ISecondEMCItem;
import biggestxuan.emcworld.api.item.equipment.weapon.BaseWeaponItem;
import biggestxuan.emcworld.api.item.equipment.weapon.IUpgradeableWeapon;
import biggestxuan.emcworld.common.items.Equipment.Weapon.Tier.EWSwordTier;
import biggestxuan.emcworld.api.item.equipment.weapon.IRangeAttackWeapon;
import net.minecraft.item.ItemStack;

public class AtmSword extends BaseWeaponItem implements IUpgradeableWeapon,IRangeAttackWeapon, ISecondEMCItem, INeedLevelItem {
    public AtmSword() {
        super(EWSwordTier.ATM,0,-2.4F);
    }

    @Override
    public int getUseLevel(ItemStack stack) {
        return 80;
    }

    @Override
    public long EMCModifySecond(ItemStack stack) {
        return 300;
    }

    @Override
    public double getAttackRange(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return false;
    }

    @Override
    public double costEMCWhenAttack(ItemStack stack) {
        return 1;
    }

    @Override
    public long getTickCost(ItemStack stack) {
        return 0;
    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public float getAdditionsDamage(ItemStack stack) {
        return 0;
    }
}
