package buildcraft.compat.forestry;

import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import buildcraft.api.transport.pipe.PipeApi;
import buildcraft.api.transport.pipe.PipeDefinition;
import buildcraft.api.transport.pipe.PipeDefinition.PipeDefinitionBuilder;

import buildcraft.lib.registry.CreativeTabManager;

import buildcraft.compat.forestry.pipe.PipeBehaviourPropolis;

public class ForestryPipes {
    public static Item pipeItemPropolis;
    public static PipeDefinition pipeDefinitionPropolis;

    public static void preInit() {

        String[] textureSuffixes = new String[8];
        textureSuffixes[0] = "";
        textureSuffixes[7] = "_itemstack";
        for (EnumFacing face : EnumFacing.VALUES) {
            textureSuffixes[face.ordinal() + 1] = "_" + face.getName();
        }

        pipeDefinitionPropolis = new PipeDefinitionBuilder()//
            .texPrefix("propolis")//
            .id("forestry_propolis")// Note: id() automatically sets the namespace to "buildcraftcompat"
            .texSuffixes(textureSuffixes)//
            .logic(PipeBehaviourPropolis::new, PipeBehaviourPropolis::new)//
            .flowItem()//
            .define();

        PipeApi.pipeRegistry.createUnnamedItemForPipe(pipeDefinitionPropolis, item -> {
            pipeItemPropolis = item;
            item.setRegistryName("pipe_item_propolis");
            item.setUnlocalizedName("buildcraftPipe.pipeitemspropolis");
            item.setCreativeTab(CreativeTabManager.getTab("buildcraft.pipes"));
        });
    }
}
