package buildcraft.compat;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import buildcraft.api.core.BCLog;

import buildcraft.compat.theoneprobe.BCPluginTOP;
import buildcraft.core.BCCoreConfig;

//@formatter:off
@Mod(
        modid = BCCompat.MODID,
        name = "BuildCraft Compat",
        version = BCCompat.VERSION,
        updateJSON = "https://mod-buildcraft.com/version/versions-compat.json",
        acceptedMinecraftVersions = "(gradle_replace_mcversion,)",
        dependencies = BCCompat.DEPENDENCIES
)
//@formatter:on
public class BCCompat {

    static final String DEPENDENCIES = "required-after:forge@(gradle_replace_forgeversion,)"//
        + ";required-after:buildcraftcore"//
        + ";after:buildcrafttransport"//
        + ";after:buildcraftbuilders"//
        + ";after:buildcraftsilicon"//
        + ";after:theoneprobe"//
        + ";after:forestry"//
    ;

    public static final String MODID = "buildcraftcompat";
    public static final String VERSION = "${version}";
    public static final String GIT_BRANCH = "${git_branch}";
    public static final String GIT_COMMIT_HASH = "${git_commit_hash}";
    public static final String GIT_COMMIT_MSG = "${git_commit_msg}";
    public static final String GIT_COMMIT_AUTHOR = "${git_commit_author}";

    @Mod.Instance(MODID)
    public static BCCompat instance;

    private static final Map<String, CompatModuleBase> modules = new HashMap<>();

    private void offerModule(final CompatModuleBase module) {
        if (module.canLoad()) {
            Property prop = BCCoreConfig.config.get("modules", module.compatModId(), true);
            if (prop.getBoolean(true)) {
                modules.put(module.compatModId(), module);
            }
        }
    }

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent evt) {

        BCLog.logger.info("");
        BCLog.logger.info("Starting BuildCraftCompat " + VERSION);
        BCLog.logger.info("Copyright (c) the BuildCraft team, 2011-2017");
        BCLog.logger.info("https://www.mod-buildcraft.com");
        if (!GIT_COMMIT_HASH.startsWith("${")) {
            BCLog.logger.info("Detailed Build Information:");
            BCLog.logger.info("  Branch " + GIT_BRANCH);
            BCLog.logger.info("  Commit " + GIT_COMMIT_HASH);
            BCLog.logger.info("    " + GIT_COMMIT_MSG);
            BCLog.logger.info("    committed by " + GIT_COMMIT_AUTHOR);
        }
        BCLog.logger.info("");

        // TODO: put this in a compat module or something :S
        BCPluginTOP.init();

        // List of all modules
        offerModule(new CompatModuleForestry());
        // End of module list

        for (CompatModuleBase m : modules.values()) {
            m.preInit();
        }
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent evt) {
        // NetworkRegistry.INSTANCE.registerGuiHandler(instance, new CompatGuiHandler());
        //
        // compatChannelHandler = new ChannelHandler();
        // MinecraftForge.EVENT_BUS.register(this);
        //
        // compatChannelHandler.registerPacketType(PacketGenomeFilterChange.class);
        // compatChannelHandler.registerPacketType(PacketTypeFilterChange.class);
        // compatChannelHandler.registerPacketType(PacketRequestFilterSet.class);
        //
        // channels = NetworkRegistry.INSTANCE.newChannel
        // (DefaultProps.NET_CHANNEL_NAME + "-COMPAT", compatChannelHandler, new PacketHandlerCompat());

        for (final CompatModuleBase m : modules.values()) {
            BCLog.logger.info("[compat] Loading module " + m.compatModId());
            m.init();
        }
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent evt) {
        for (final CompatModuleBase m : modules.values()) {
            m.postInit();
        }
    }

    // @Mod.EventHandler
    // public void missingMapping(FMLMissingMappingsEvent event) {
    // CompatModuleForestry.missingMapping(event);
    // }

    // @SubscribeEvent
    // @SideOnly(Side.CLIENT)
    // public void handleTextureRemap(TextureStitchEvent.Pre event) {
    // if (event.map.getTextureType() == 1) {
    // TextureManager.getInstance().initIcons(event.map);
    // }
    // }

    // public static boolean isLoaded(String module) {
    // return moduleNames.contains(module);
    // }

    // public static boolean hasModule(final String module) {
    // return BuildCraftCompat.moduleNames.contains(module);
    // }
}
