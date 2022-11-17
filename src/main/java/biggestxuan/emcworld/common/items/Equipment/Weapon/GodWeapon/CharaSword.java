package biggestxuan.emcworld.common.items.Equipment.Weapon.GodWeapon;

/**
 *  EMC WORLD MOD
 *  @Author Biggest_Xuan
 *  2022/09/28
 */

import biggestxuan.emcworld.api.item.ISponsorItem;
import biggestxuan.emcworld.api.item.equipment.weapon.BaseEMCGodWeapon;
import biggestxuan.emcworld.common.utils.Sponsors.Sponsors;
import net.minecraft.item.ItemStack;

public class CharaSword extends BaseEMCGodWeapon implements ISponsorItem {
    public CharaSword() {
        super(11,"chara_sword",0x44ccaa);
    }

    @Override
    public long EMCModifySecond(ItemStack stack) {
        return (long) (555.5 * Math.pow(1.14514 * getLevel(stack), Math.pow(1.14514, 1.14514)));
    }

    @Override
    public float getAdditionsDamage(ItemStack stack) {
        return (float) (Math.sqrt(getLevel(stack))*11.4514);
    }

    @Override
    public double getAttackRange(ItemStack stack) {
        return 0.06*getLevel(stack);
    }

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    public double costEMCWhenAttack(ItemStack stack) {
        return Math.sqrt(getLevel(stack));
    }

    @Override
    public Sponsors getSponsor(){
        return new Sponsors("cx02","1738cb1b-ea69-4e0f-8678-688aea7e8d1b",2);
    }
}