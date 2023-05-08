package biggestxuan.emcworld.common.events;

/**
 *  EMC WORLD MOD
 *  @Author Biggest_Xuan
 *  2023/04/08
 */

import biggestxuan.emcworld.EMCWorld;
import biggestxuan.emcworld.common.data.LotteryData;
import biggestxuan.emcworld.common.utils.EMCLog.EMCWriter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod.EventBusSubscriber(modid = EMCWorld.MODID)
public class serverEvent {
    @SubscribeEvent
    public static void ServerStartEvent(FMLServerStartedEvent event){
        EMCWriter.Init();
    }

    @SubscribeEvent
    public static void ServerTick(TickEvent.ServerTickEvent event){
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerWorld world = server.overworld();
            LotteryData data = LotteryData.getInstance(server);
            if(world.getDayTime() % 240000 == 0 || world.getDayTime() == 0){
                data.refreshNum();
                data.setIndex(data.getIndex()+1);
            }
            if(world.getDayTime() == 0){
                data.setStoredEMC(2000000000L);
            }
        }
    }
}
