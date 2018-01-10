package buildcraft.compat.forestry.pipe;

import net.minecraft.tileentity.TileEntity;

import forestry.core.tiles.ITitled;
import forestry.sorting.FilterLogic;

public interface IFilterContainer extends ITitled {
    FilterLogic getFilter();

    TileEntity getTileEntity();
}
