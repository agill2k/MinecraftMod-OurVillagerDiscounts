package ourvillagerdiscounts.ourvillagerdiscounts.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ourvillagerdiscounts.ourvillagerdiscounts.callback.VillagerInteractCallback;

@Mixin(Villager.class)
public class VillagerInteractMixin {
    @Inject(at = @At(value = "INVOKE"), method="mobInteract", cancellable = true)
    private void onVillagerInteract(final Player player, final InteractionHand hand, final CallbackInfoReturnable<InteractionResult> info) {
        InteractionResult result = VillagerInteractCallback.EVENT.invoker().interact(player, (Villager)(Object)this);

        if (result == InteractionResult.FAIL) {
            info.cancel();
        }
    }
}
