package buildcraft.compat.forestry.pipe;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import buildcraft.api.core.EnumPipePart;
import buildcraft.api.transport.pipe.IPipe;
import buildcraft.api.transport.pipe.PipeBehaviour;
import buildcraft.api.transport.pipe.PipeEventHandler;
import buildcraft.api.transport.pipe.PipeEventItem;

import buildcraft.lib.cap.CapabilityHelper;

import buildcraft.compat.forestry.ForestryPipes;

import forestry.api.core.ILocatable;
import forestry.api.genetics.GeneticCapabilities;
import forestry.api.genetics.IFilterLogic;
import forestry.core.gui.GuiHandler;
import forestry.core.gui.IGuiHandlerTile;
import forestry.core.network.IStreamableGui;
import forestry.core.network.PacketBufferForestry;
import forestry.sorting.FilterLogic;
import forestry.sorting.gui.ContainerGeneticFilter;
import forestry.sorting.gui.GuiGeneticFilter;
import forestry.sorting.tiles.TileGeneticFilter;

public class PipeBehaviourPropolis extends PipeBehaviour
    implements ILocatable, IFilterLogic.INetworkHandler, IStreamableGui, IGuiHandlerTile, IFilterContainer {

    private final FilterLogic filter = new FilterLogic(this, this);
    private final CapabilityHelper caps = new CapabilityHelper();

    {
        caps.addCapabilityInstance(GeneticCapabilities.FILTER_LOGIC, filter, EnumPipePart.VALUES);
    }

    public PipeBehaviourPropolis(IPipe pipe) {
        super(pipe);
    }

    public PipeBehaviourPropolis(IPipe pipe, NBTTagCompound nbt) {
        super(pipe, nbt);
        filter.readFromNBT(nbt.getCompoundTag("filter"));
    }

    @Override
    public NBTTagCompound writeToNbt() {
        NBTTagCompound nbt = super.writeToNbt();
        nbt.setTag("filter", filter.writeToNBT(new NBTTagCompound()));
        return nbt;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        T cap = caps.getCapability(capability, facing);
        if (cap != null) {
            return cap;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public int getTextureIndex(EnumFacing face) {
        return face == null ? 0 : face.ordinal() + 1;
    }

    @Override
    public boolean onPipeActivate(EntityPlayer player, RayTraceResult trace, float hitX, float hitY, float hitZ,
        EnumPipePart part) {
        if (!getWorldObj().isRemote) {
            // Yay hacks!
            TileGeneticFilter tile = new TileGeneticFilter();
            tile.setPos(getCoordinates());
            // FXIME: This doesn't work. Unfortunately.
            GuiHandler.openGui(player, tile);
        }
        return true;
    }

    // ILocatable

    @Override
    public BlockPos getCoordinates() {
        return pipe.getHolder().getPipePos();
    }

    @Override
    public World getWorldObj() {
        return pipe.getHolder().getPipeWorld();
    }

    // IFilterLogic.INetworkHandler

    @Override
    public void sendToPlayers(IFilterLogic logic, WorldServer server, EntityPlayer currentPlayer) {
        // Copied from TileGeneticFilter
        for (EntityPlayer player : server.playerEntities) {
            if (player != currentPlayer && player.openContainer instanceof ContainerGeneticFilter) {
                ContainerGeneticFilter currentContainer = (ContainerGeneticFilter) currentPlayer.openContainer;
                ContainerGeneticFilter otherContainer = (ContainerGeneticFilter) player.openContainer;
                if (currentContainer.hasSameTile(otherContainer)) {
                    otherContainer.setGuiNeedsUpdate(true);
                }
            }
        }
    }

    // IStreamableGui
    // NOTE: Currently this doesn't work -- forestry mandates (atm) that tile entities implement this.

    @Override
    @SideOnly(Side.CLIENT)
    public void readGuiData(PacketBufferForestry data) throws IOException {
        filter.readGuiData(data);
    }

    @Override
    public void writeGuiData(PacketBufferForestry data) {
        filter.writeGuiData(data);
    }

    // IFilterContainer

    @Override
    public FilterLogic getFilter() {
        return filter;
    }

    @Override
    public TileEntity getTileEntity() {
        return pipe.getHolder().getPipeTile();
    }

    // ITitled

    @Override
    public String getUnlocalizedTitle() {
        return ForestryPipes.pipeItemPropolis.getUnlocalizedName();
    }

    // IGuiHandlerTile

    @Override
    @Nullable
    public Container getContainer(EntityPlayer player, int data) {
        return new ContainerGeneticFilter(this, player.inventory);
    }

    @Override
    @Nullable
    public GuiContainer getGui(EntityPlayer player, int data) {
        return new GuiGeneticFilter(this, player.inventory);
    }

    // Pipe Events

    @PipeEventHandler
    public void onFindDest(PipeEventItem.SideCheck event) {
        for (EnumFacing face : EnumFacing.VALUES) {
            if (!filter.isValid(event.stack, face)) {
                event.disallow(face);
            }
        }
    }
}
