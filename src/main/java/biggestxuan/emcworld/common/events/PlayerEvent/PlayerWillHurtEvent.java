package biggestxuan.emcworld.common.events.PlayerEvent;

/*
 *  EMC WORLD MOD
 *  @Author Biggest_Xuan
 *  2022/12/28
 */

import biggestxuan.emcworld.EMCWorld;
import biggestxuan.emcworld.api.item.equipment.armor.IEMCShieldArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = EMCWorld.MODID)
public class PlayerWillHurtEvent {
    @SubscribeEvent
    public static void LivingAttackEvent(LivingAttackEvent event){
        LivingEntity entity = event.getEntityLiving();
        if(entity.level.isClientSide || !(entity instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) entity;
        if(costEMCShield(player,event.getAmount())){
            event.setCanceled(true);
            player.hurtTime += 5;
        }
    }

    private static boolean costEMCShield(PlayerEntity player,float amount){
        if(player.hurtTime != 0) return true;
        float shield = 0f;
        float maxShield = 0f;
        List<ItemStack> armors = new ArrayList<>();
        for(ItemStack stack :player.inventory.armor){
            if(stack.getItem() instanceof IEMCShieldArmor){
                armors.add(stack);
                IEMCShieldArmor armor = (IEMCShieldArmor) stack.getItem();
                shield += armor.getShield(stack);
                maxShield += armor.getMaxShield(stack);
            }
        }
        if(shield < amount) return false;
        for (ItemStack stack : armors) {
            IEMCShieldArmor armor = (IEMCShieldArmor) stack.getItem();
            armor.modifyShield(stack, negateExact(amount * armor.getMaxShield(stack) / maxShield));
        }
        return true;
    }

    private static float negateExact(float value){
        return 0 - value;
    }
}
