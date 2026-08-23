package ourvillagerdiscounts.ourvillagerdiscounts.mixin;

import net.minecraft.world.entity.ai.gossip.GossipContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.stream.Stream;

@Mixin(GossipContainer.class)
public interface VillagerGossipEntriesInvoker {
    @Invoker("unpack")
    Stream<GossipContainer.GossipEntry> invokeUnpack();
}
