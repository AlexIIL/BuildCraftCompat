package buildcraft.compat.forestry.pipe;

import net.minecraft.entity.player.EntityPlayer;

import buildcraft.lib.gui.ContainerBC_Neptune;

public class ContainerPropolisPipe extends ContainerBC_Neptune {

    public final PipeBehaviourPropolis pipe;

    public ContainerPropolisPipe(EntityPlayer player, PipeBehaviourPropolis pipe) {
        super(player);
        this.pipe = pipe;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;// FIXME!
    }
}
