package cursedflames.bountifulbaubles.mixin.common;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import cursedflames.bountifulbaubles.common.baubleeffect.StatusEffectNegate.IStatusEffectNegate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.component.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	private MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "canHaveStatusEffect",
			at = @At("HEAD"),
			cancellable = true)
	private void canHaveStatusEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> info) {
		LivingEntity self = (LivingEntity) (Object) this;
		
		ICuriosItemHandler handler = CuriosApi.getCuriosHelper().getCuriosHandler(self).orElse(null);
		if (handler != null) {
			Map<String, ICurioStacksHandler> curioMap = handler.getCurios();
			for (ICurioStacksHandler stacksHandler : curioMap.values()) {
				IDynamicStackHandler stackHandler = stacksHandler.getStacks();
				for (int i = 0; i < stackHandler.size(); i++) {
					ItemStack stack = stackHandler.getStack(i);
					
					if (!stack.isEmpty() && stack.getItem() instanceof IStatusEffectNegate) {
						Item item = stack.getItem();
						if (((IStatusEffectNegate) item).shouldNegate(effect)) {
							info.setReturnValue(false);
							return;
						}
					}
				}
			}
		}
		// TODO also check for items in hands
	}
}
