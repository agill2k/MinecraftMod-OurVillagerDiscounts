package ourvillagerdiscounts.ourvillagerdiscounts.callback

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.player.Player

/**
 * Callback for interacting with a villager.
 * Upon return:
 * - SUCCESS cancels further processing and continues with normal interaction behavior.
 * - PASS falls back to further processing and defaults to SUCCESS if no other listeners are available.
 * - FAIL cancels further processing and does not interact with the villager.
 */
interface VillagerInteractCallback {
    fun interact(player: Player, villager: Villager): InteractionResult

    companion object {
        @JvmField
        val EVENT: Event<VillagerInteractCallback> = EventFactory.createArrayBacked(
            VillagerInteractCallback::class.java
        ) { listeners: Array<VillagerInteractCallback> ->
            return@createArrayBacked object : VillagerInteractCallback {
                override fun interact(player: Player, villager: Villager): InteractionResult {
                    for (listener in listeners) {
                        val result: InteractionResult = listener.interact(player, villager)
                        if (result != InteractionResult.PASS) {
                            return result
                        }
                    }
                    return InteractionResult.PASS
                }
            }
        }
    }
}
