package biggestxuan.emcworld.client.event;

/**
 * EMC WORLD MOD
 * @Author Biggest_Xuan
 * 2022/10/13
 */

import biggestxuan.emcworld.EMCWorld;
import biggestxuan.emcworld.api.EMCWorldAPI;
import biggestxuan.emcworld.api.item.*;
import biggestxuan.emcworld.api.item.equipment.*;
import biggestxuan.emcworld.api.item.equipment.armor.*;
import biggestxuan.emcworld.api.item.equipment.bow.*;
import biggestxuan.emcworld.api.item.equipment.weapon.*;
import biggestxuan.emcworld.common.capability.EMCWorldCapability;
import biggestxuan.emcworld.common.compact.GameStage.GameStageManager;
import biggestxuan.emcworld.common.config.ConfigManager;
import biggestxuan.emcworld.common.items.Curios.StoredTotem;
import biggestxuan.emcworld.common.items.Equipment.BaseWeaponGemItem;
import biggestxuan.emcworld.common.items.Equipment.Weapon.Gun.GunItem;
import biggestxuan.emcworld.common.items.Equipment.Weapon.Staff.StaffItem;
import biggestxuan.emcworld.common.recipes.EMCStageLimit;
import biggestxuan.emcworld.common.utils.MathUtils;
import biggestxuan.emcworld.common.utils.Sponsors.Sponsors;
import cursedflames.bountifulbaubles.common.item.items.ItemGlovesDexterity;
import mekanism.common.item.gear.ItemHazmatSuitArmor;
import mekanism.common.registries.MekanismItems;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(
        modid = EMCWorld.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = {Dist.CLIENT}
)
public class ItemToolTipEvent {
    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void tooltipEvent(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        if(event.getPlayer() == null) return;
        ClientPlayerEntity player = Minecraft.getInstance().player;
        final Item[] radiationItem = new Item[]{
                MekanismItems.PLUTONIUM_PELLET.getItem(),MekanismItems.ANTIMATTER_PELLET.getItem(),MekanismItems.POLONIUM_PELLET.getItem()
        };
        if(stack.equals(ItemStack.EMPTY)) return;
        if(stack.getItem() instanceof ItemGlovesDexterity){
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.gloves_dexterity"));
        }
        if(stack.getItem() instanceof IPlayerDifficultyItem){
            IPlayerDifficultyItem item_3 = (IPlayerDifficultyItem) stack.getItem();
            double diff = item_3.requireDifficulty();
            if(Minecraft.getInstance().player == null) return;
            double player_diff = Minecraft.getInstance().player.getCapability(EMCWorldCapability.UTIL).orElseThrow(NullPointerException::new).getDifficulty();
            if(player_diff < diff){
                event.getToolTip().add(EMCWorld.tc("message.all_diff",diff));
                return;
            }
        }
        if(stack.getItem() instanceof IDifficultyItem){
            IDifficultyItem item_2 = (IDifficultyItem) stack.getItem();
            if(ConfigManager.DIFFICULTY.get() < item_2.getDifficulty()){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.difficulty_cant_use",item_2.getDifficulty()));
                return;
            }
        }
        if(stack.getItem() instanceof IGemInlaidItem){
            IGemInlaidItem i_q = (IGemInlaidItem) stack.getItem();
            if(i_q.getGemType(stack) == 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.gem_null"));
            }else{
                event.getToolTip().add(getGemName(i_q.getGemType(stack)));
            }
        }
        if(stack.getItem() instanceof INeedLevelItem){
            INeedLevelItem item_1 = (INeedLevelItem) stack.getItem();
            int playerLevel = player.getCapability(EMCWorldCapability.PLAYER_LEVEL).orElseThrow(NullPointerException::new).getLevel();
            int itemLevel = item_1.getUseLevel(stack);
            if(playerLevel < itemLevel && !player.isCreative() && itemLevel >= 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.need_level_to_use",itemLevel));
                return;
            }
        }
        long value;
        String stage = "";
        value = EMCHelper.getEmcValue(stack);
        boolean isTrans = true;
        boolean free = ConfigManager.FREE_MODE.get();
        if(value > 0L){
            RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
            for(EMCStageLimit recipe:manager.getAllRecipesFor(EMCStageLimit.EMCStageLimitType.INSTANCE)){
                if(recipe.getItem().equals(stack.getItem())){
                    if(GameStageManager.hasStage(event.getPlayer(),recipe.getStage())){
                        break;
                    }
                    isTrans = false;
                    stage = recipe.getStage();
                }
            }
            IFormattableTextComponent normal = EMCWorld.tc("tooltip.emcworld.emc",MathUtils.format(value));
            IFormattableTextComponent stack_tip = EMCWorld.tc("tooltip.emcworld.emc_stack",MathUtils.format(value * stack.getCount()));
            if(Screen.hasShiftDown()){
                normal = EMCWorld.tc("tooltip.emcworld.emc",MathUtils.thousandSign(value));
                stack_tip = EMCWorld.tc("tooltip.emcworld.emc_stack",MathUtils.thousandSign(value * stack.getCount()));
            }
            if(!(isTrans || free)){
                normal.setStyle(Style.EMPTY.setStrikethrough(true));
                //stack_tip.setStyle(Style.EMPTY.setStrikethrough(true));
            }
            event.getToolTip().add(normal);
            if(stack.getCount() > 1){
                event.getToolTip().add(stack_tip);
            }
        }
        if(Screen.hasShiftDown()){
            if(stack.getItem() instanceof IPrefixItem){
                IPrefixItem item = (IPrefixItem) stack.getItem();
                IPrefixItem.Prefix prefix = item.getPrefix(stack);
                IFormattableTextComponent text = prefix.getName().setStyle(Style.EMPTY.withColor(Color.fromRgb(prefix.getColor())));
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.prefix").append(text));
            }
            if(stack.getItem() instanceof IUpgradeableItem){
                IUpgradeableItem item = (IUpgradeableItem) stack.getItem();
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_level",item.getLevel(stack),item.getMaxLevel()));
            }
            if(stack.getItem() instanceof IStarItem){
                IStarItem item = (IStarItem) stack.getItem();
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.star",item.getStar(stack),item.getMaxStar(stack)));
            }
        }
        if(stack.getItem() instanceof IUpgradeBow){
            IUpgradeBow bow = (IUpgradeBow) stack.getItem();
            float damage = bow.getAdditionDamage(stack);
            float speed = bow.lossBowTime(stack);
            if(damage > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.bow_damage_add",String.format("%.2f",damage)));
            }
            if(damage < 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.bow_damage_loss",String.format("%.2f",neg(damage))));
            }
            if(speed > 1){
                //event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.bow_speed_add",String.format("%.2f",(speed-1)*100)+"%"));
            }
            if(speed < 1){
                //event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.bow_speed_loss",String.format("%.2f",(1-speed)*100)+"%"));
            }
        }
        if(stack.getItem() instanceof StaffItem){
            StaffItem i_s = (StaffItem) stack.getItem();
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.staff_damage",String.format("%.2f",i_s.getBaseDamage(stack))));
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.attack_range").append(EMCWorld.tc(StaffItem.getMode(stack).getName())));
        }
        if(stack.getItem() instanceof IAdditionsDamageWeapon){
            IAdditionsDamageWeapon item = (IAdditionsDamageWeapon) stack.getItem();
            double damage = item.getAdditionsDamage(stack);
            if(damage > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_addition_damage",String.format("%.2f",damage)));
            }else if (damage < 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_addition_damage_loss",String.format("%.2f",Math.abs(damage))));
            }
        }
        if(stack.getItem() instanceof IRangeAttackWeapon){
            IRangeAttackWeapon item1 = (IRangeAttackWeapon) stack.getItem();
            double range = item1.getAttackRange(stack);
            if(range >0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_attack_range",String.format("%.1f",range)));
            }
        }
        if(stack.getItem() instanceof IUpgradeableArmor){
            IUpgradeableArmor item_1_1 = (IUpgradeableArmor) stack.getItem();
            if(item_1_1.hurtRate(stack) < 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_hurt",String.format("%.2f",(1-item_1_1.hurtRate(stack))*100)).append("%"));
            } else if (item_1_1.hurtRate(stack) > 1) {
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_hurt_add",String.format("%.2f",(item_1_1.hurtRate(stack)-1)*100)).append("%"));
            }
            if(item_1_1.extraHealth(stack) > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_health",String.format("%.2f",item_1_1.extraHealth(stack))));
            } else if(item_1_1.extraHealth(stack) < 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_health_loss",String.format("%.2f",Math.abs(item_1_1.extraHealth(stack)))));
            }
        }
        if(stack.getItem() instanceof IHealBoostArmor){
            IHealBoostArmor item1_2 = (IHealBoostArmor) stack.getItem();
            if(item1_2.getHealBoostRate(stack) >1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.heal_boost",String.format("%.2f",(item1_2.getHealBoostRate(stack)-1)*100)+"%"));
            } else if(item1_2.getHealBoostRate(stack) <1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.heal_boost_lost",String.format("%.2f",(1-item1_2.getHealBoostRate(stack))*100)+"%"));
            }
        }
        if(stack.getItem() instanceof ICostEMCItem){
            ICostEMCItem item2 = (ICostEMCItem) stack.getItem();
            double cost = item2.costEMCWhenAttack(stack);
            if(cost > 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_emc_cost_addon",String.format("%.2f",(cost-1)*100)+"%"));
            }
            if(cost < 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_emc_cost",String.format("%.2f",(1-cost)*100)+"%"));
            }
        }
        if(stack.getItem() instanceof GunItem){
            GunItem gun = (GunItem) stack.getItem();
            float damage = gun.damage(stack);
            if(damage > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_addition_damage",String.format("%.2f",damage)));
            }
            if(damage < 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_addition_damage_loss",String.format("%.2f",-damage)));
            }
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.gun_cd",String.format("%.2f",gun.cd(stack) / 20f)));
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.gun_acc",String.format("%.2f",gun.accuracy(stack,player)*100)+"%"));
        }
        if(stack.getItem() instanceof ISpeedArmor){
            ISpeedArmor item_2_5 = (ISpeedArmor) stack.getItem();
            double speed = item_2_5.getSpeed(stack);
            if(speed > 0) {
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_speed",String.format("%.2f",speed)));
            }else if(speed < 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_god_speed_loss",String.format("%.2f",speed)));
            }
        }
        if(stack.getItem() instanceof IReachArmor){
            IReachArmor item_2_6 = (IReachArmor) stack.getItem();
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_reach_distance",String.format("%.2f",item_2_6.getReachDistance(stack))));
        }
        if(stack.getItem() instanceof IEMCShieldArmor){
            IEMCShieldArmor item_2_7_3 = (IEMCShieldArmor) stack.getItem();
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_emc_max_shield",String.format("%.2f",item_2_7_3.getMaxShield(stack))));
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.armor_emc_speed",String.format("%.2f",item_2_7_3.getShieldSpeed(stack))));
        }
        if(stack.getItem() instanceof ICriticalWeapon){
            ICriticalWeapon ww = (ICriticalWeapon) stack.getItem();
            if(ww.getActCriticalChance(stack) > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.critical_chance",String.format("%.2f",ww.getActCriticalChance(stack)*100)).append("%"));
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.critical_rate",String.format("%.2f",ww.getActCriticalRate(stack)*100)).append("%"));
            }
        }
        if(stack.getItem() instanceof IAttackSpeedItem){
            IAttackSpeedItem si = (IAttackSpeedItem) stack.getItem();
            double rate = si.getAttackSpeed(stack);
            if(rate > 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.attack_speed_add",String.format("%.2f",(rate-1)*100)).append("%"));
            }
            if(rate < 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.attack_speed_loss",String.format("%.2f",(1-rate)*100)).append("%"));
            }
        }
        if(stack.getItem() instanceof IUpgradeableTool){
            IUpgradeableTool tool = (IUpgradeableTool) stack.getItem();
            if(tool.getAdditionSpeed(stack) > 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.tool_speed",String.format("%.2f",(tool.getAdditionSpeed(stack)-1)*100)+"%"));
            }else if(tool.getAdditionSpeed(stack) < 1){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.tool_speed_loss",String.format("%.2f",(1-tool.getAdditionSpeed(stack))*100)+"%"));
            }
        }
        if(stack.getItem() instanceof ISecondEMCItem){
            ISecondEMCItem item3 = (ISecondEMCItem) stack.getItem();
            long emc = item3.EMCModifySecond(stack);
            if(emc != 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.weapon_god_emc_second", MathUtils.format(emc)));
            }
        }
        if(stack.getItem() instanceof IRangeAttackWeapon){
            IRangeAttackWeapon a_w = (IRangeAttackWeapon) stack.getItem();
            if(a_w.getAttackRange(stack) > 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.attack_range").append(EMCWorld.tc(a_w.getAttackMode(stack).getName())));
            }
        }
        if(stack.getItem() instanceof IEMCInfuserItem){
            IEMCInfuserItem ii_i = (IEMCInfuserItem) stack.getItem();
            if(Screen.hasShiftDown()){
                if(Screen.hasControlDown()){
                    String per = MathUtils.thousandSign(ii_i.getInfuser(stack)) +" / " + MathUtils.thousandSign(ii_i.getMaxInfuser(stack));
                    event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_power",per));
                }else{
                    String per = ""+MathUtils.doubleFormat(ii_i.getInfuser(stack))+" / "+MathUtils.doubleFormat(ii_i.getMaxInfuser(stack));
                    event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_power",per));
                }
            }else{
                double v = ii_i.getInfuserRate(stack) >= 0 ? ii_i.getInfuserRate(stack) * 100 : 0;
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_power",String.format("%.1f",v)).append("%"));
            }
        }
        for(Item item: radiationItem){
            if(stack.getItem().getItem().equals(item)){
                //event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.radiation_item"));
            }
        }
        if(stack.getItem() instanceof ItemHazmatSuitArmor || stack.getItem().equals(MekanismItems.MODULE_RADIATION_SHIELDING.getItem().getItem())){
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.unclear_limit"));
        }
        if(stack.getItem() instanceof IKillCountItem){
            IKillCountItem w = (IKillCountItem) stack.getItem();
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.killCount",MathUtils.format(w.getKillCount(stack))));
        }
        if(!(isTrans || free) && stage.equals("disabled") && EMCHelper.getEmcValue(stack) > 0){
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_disable"));
            return;
        }
        if(!(isTrans || free) && !stage.equals("") && EMCHelper.getEmcValue(stack) > 0){
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_locked",stage));
        }
        if(stack.isDamageableItem() && !(stack.getItem() instanceof StoredTotem)){
            if(stack.getItem() instanceof IEMCRepairableItem){
                IEMCRepairableItem item = (IEMCRepairableItem) stack.getItem();
                if(item.getTickCost(stack) > 0){
                    event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.emc_repair",MathUtils.format(item.getTickCost(stack))));
                    return;
                }
            }
            if(stack.getMaxDamage() >= 0){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.durability",stack.getMaxDamage()-stack.getDamageValue(),stack.getMaxDamage()));
            }
        }
        if(stack.getItem() instanceof ISponsorItem && !EMCWorld.isOffline){
            ISponsorItem item4 = (ISponsorItem) stack.getItem();
            Sponsors sp = item4.getSponsor();
            int level = 0;
            String name;
            if(sp == null){
                name = "";
            }else{
                level = sp.getIndex();
                name = sp.getPlayerName();
            }
            event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.sponsoritem",name).setStyle(Style.EMPTY.withItalic(true).withColor(getColor(level))));
            if(new Sponsors(player.getScoreboardName(),player.getUUID(), EMCWorldAPI.getInstance().getUtilCapability(player).getLevel()).equals(sp)){
                event.getToolTip().add(EMCWorld.tc("tooltip.emcworld.sponsoract"));
            }
        }
    }

    @Nonnull
    private static Color getColor(int level){
        switch (level){
            case 2:
                return Color.fromRgb(0x00aaaa);
            case 3:
                return Color.fromRgb(0xffaa00);
            default:
                return Color.fromRgb(0x00aa00);
        }
    }

    private static double neg(double a){
        return 0 - a;
    }

    @Nonnull
    private static IFormattableTextComponent getGemName(int type){
        String pre = "item."+EMCWorld.MODID+".";
        String n = "";
        String a = "_gemstone";
        switch (type){
            case 1:
                n = "blood";
                break;
            case 2:
                n = "nature";
                break;
            case 3:
                n = "lake";
                break;
            case 4:
                n = "abyss";
        }
        return EMCWorld.tc(pre+n+a).setStyle(Style.EMPTY.withColor(Color.fromRgb(BaseWeaponGemItem.gem.valueOf(n.toUpperCase()).getColor())));
    }
}
