package cursedflames.bountifulbaubles.common.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IProxy {
	public default void init(FMLCommonSetupEvent event) {
	}
	
	public default World getClientWorld() {
		return null;
	}
	
	public default PlayerEntity getClientPlayer() {
		return null;
	}
}
