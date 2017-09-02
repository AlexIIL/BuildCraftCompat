//package buildcraft.compat;
//
//import net.minecraftforge.fml.common.Loader;
//
//import buildcraft.compat.minetweaker.AssemblyTable;
//import buildcraft.compat.minetweaker.Fuels;
//import buildcraft.compat.minetweaker.ProgrammingTable;
//import buildcraft.compat.minetweaker.Refinery;
//
//import minetweaker.MineTweakerAPI;
//
//public class CompatModuleMineTweaker3 extends CompatModuleBase {
//	@Override
//	public boolean canLoad() {
//		return Loader.isModLoaded("crafttweaker") || Loader.isModLoaded(name());
//	}
//
//    @Override
//    public String name() {
//        return "MineTweaker3";
//    }
//
//    @Override
//    public void postInit() {
//        MineTweakerAPI.registerClass(AssemblyTable.class);
//        MineTweakerAPI.registerClass(Fuels.class);
//		MineTweakerAPI.registerClass(ProgrammingTable.class);
//        MineTweakerAPI.registerClass(Refinery.class);
//    }
//}
