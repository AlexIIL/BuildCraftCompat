package buildcraft.compat.forestry.pipe;

import net.minecraft.entity.player.EntityPlayer;

import buildcraft.lib.gui.GuiBC8;

public class GuiPropolisPipe extends GuiBC8<ContainerPropolisPipe> {

    public GuiPropolisPipe(EntityPlayer player, PipeBehaviourPropolis pipe) {
        super(new ContainerPropolisPipe(player, pipe));
    }

}
