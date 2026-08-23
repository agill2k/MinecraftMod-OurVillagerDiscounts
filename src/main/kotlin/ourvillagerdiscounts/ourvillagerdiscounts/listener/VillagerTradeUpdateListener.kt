package ourvillagerdiscounts.ourvillagerdiscounts.listener

import java.util.Comparator
import java.util.stream.Stream
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.ai.gossip.GossipContainer
import net.minecraft.world.entity.ai.gossip.GossipContainer.GossipEntry
import net.minecraft.world.entity.ai.gossip.GossipType
import net.minecraft.world.entity.npc.villager.Villager
import net.minecraft.world.entity.npc.villager.VillagerData
import net.minecraft.world.entity.npc.villager.VillagerProfession
import net.minecraft.world.entity.player.Player
import org.apache.logging.log4j.LogManager
import ourvillagerdiscounts.ourvillagerdiscounts.callback.VillagerInteractCallback
import ourvillagerdiscounts.ourvillagerdiscounts.mixin.VillagerGossipEntriesInvoker

class VillagerTradeUpdateListener : VillagerInteractCallback {
    override fun interact(player: Player, villager: Villager): InteractionResult {
        val data: VillagerData = villager.villagerData
        val profession = data.profession
        if (!profession.`is`(VillagerProfession.NONE) && !profession.`is`(VillagerProfession.NITWIT)) {
            val gossip: GossipContainer = villager.gossips
            val gossipAccessor: Stream<GossipEntry> = (gossip as VillagerGossipEntriesInvoker).invokeUnpack()
            gossipAccessor
                .filter { a -> a.type() == GossipType.MAJOR_POSITIVE }
                .max(Comparator.comparingInt { a -> a.weightedValue() })
                .ifPresent { maxEntry: GossipEntry ->
                    val majorPositiveGossipWeighted = maxEntry.weightedValue()
                    val currentMajorPositiveGossipWeighted = gossip.getReputation(
                        player.uuid
                    ) { g -> g == GossipType.MAJOR_POSITIVE }
                    if (majorPositiveGossipWeighted > currentMajorPositiveGossipWeighted) {
                        val majorPositiveGossipUnweighted = majorPositiveGossipWeighted / maxEntry.type().weight
                        villager.setGossips(GossipContainer().apply {
                            this.add(player.uuid, GossipType.MAJOR_POSITIVE, majorPositiveGossipUnweighted)
                        })
                    }
                }
        }
        return InteractionResult.PASS
    }

    companion object {
        private val LOG = LogManager.getLogger(
            VillagerTradeUpdateListener::class.java
        )
    }
}
