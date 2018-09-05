package com.crashcz.minerals;

import com.crashcz.minerals.init.ModRecipes;
import com.crashcz.minerals.init.Properties;
import com.crashcz.minerals.proxy.CommonProxy;
import com.crashcz.minerals.util.Reference;
import com.crashcz.minerals.util.handlers.RegistryHandler;
import com.crashcz.minerals.world.ModWorldGen;

import net.minecraft.init.Blocks;
import net.minecraft.world.gen.ChunkGeneratorSettings.Factory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class Main
{
	@Instance
	public static Main instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event)
	{
	}
	@EventHandler
	public static void Init(FMLInitializationEvent event)
	{
		ModRecipes.init();
		Properties.setCustomIronOre();
		MinecraftForge.ORE_GEN_BUS.register(RegistryHandler.class);
		GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
		RegistryHandler.initRegistries();
	}
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event)
	{
	}
}
