package buildcraft.compat;

import buildcraft.api.EnumBuildCraftModule;

import buildcraft.compat.forestry.ForestryPipes;

public class CompatModuleForestry extends CompatModuleBase {

    @Override
    public String compatModId() {
        return "forestry";
    }

    @Override
    public void preInit() {
        if (EnumBuildCraftModule.TRANSPORT.isLoaded()) {
            ForestryPipes.preInit();
        }
    }
}
