package com.mraof.neverminetic;

import java.util.ArrayList;
import java.util.Arrays;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import tconstruct.library.TConstructRegistry;

@Mod(modid = "NevermineTiC", name = "NevermineTiC", version = "0", dependencies="required-after:TConstruct;required-after:nevermine")
public class NevermineTiC
{
	final String[] blockRecipe = {"###", "###", "###"};
	int materialId = 500;
	public static ArrayList<Fluid> fluids = new ArrayList<Fluid>();
	public static ArrayList<String> materialNames = new ArrayList<String>();
	public static Item metalBucket;
	boolean oreDictIngots;
	boolean oreDictOres;

	public CreativeTabs tabNevermineTiC = new CreativeTabs("tabNevermineTiC") 
	{
		@Override
		public Item getTabIconItem()
		{
			return metalBucket;
		}
	};
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		oreDictIngots = config.getBoolean("oreDictIngots", "oreDict", true, "add nevermine ingots to the ore dictionary");
		oreDictOres = config.getBoolean("oreDictOres", "oreDict", true, "add nevermine ores to the ore dictionary");
		config.save();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		try
		{
			Class.forName("net.nevermine.izer.Blockizer");
			Class.forName("net.nevermine.izer.Itemizer");
		}
		catch(ClassNotFoundException e)
		{
		}

		metalBucket = new MetalBucket().setCreativeTab(tabNevermineTiC);
		GameRegistry.registerItem(metalBucket, "metalBucket");
		MinecraftForge.EVENT_BUS.register(new BucketHandler());

		addMaterial("Amethyst", 4, 1000, 800, 3, 1.3F, 0x5727b2, "AmethystIngot", true);
		addMaterial("Rosite", 4, 2000, 1050, 9, 1.3F, 0xde1600);
		addMaterial("Limonite", 4, 400, 600, 6, 1.3F, 0xf18600);
		addMaterial("Jade", 4, 2000, 1100, 10, 1.3F, 0x46a434);
		addMaterial("Sapphire", 4, 2000, 1200, 13, 1.3F, 0x0057d8);
		addMaterial("Emberstone", 4, 2000, 1400, 10, 1.3F, 0xc71c23);
		addMaterial("Skeletal", 4, 2000, 1600, 27, 1.3F, 0xbeb08d, "IngotSkeletal", false);
		ArrayList<String> oreDictNames = new ArrayList<String>();
		if(oreDictOres)
		{
			oreDictNames.addAll(Arrays.asList(new String[]{
				"oreBloodstone", "oreBloodstone", "oreAmethyst", "oreAmethyst", "oreChestFragments", "oreChestFragments", "oreCrystallite", "oreCrystallite", "oreEmberstone", "oreEmberstone", "oreFootFragments", "oreFootFragments", "oreJade", "oreJade", "oreLegFragments", "oreLegFragments", "oreLimonite", "oreLimonite", "oreRosite", "oreRosite", "oreSapphire", "oreSapphire", "oreSkullFragments", "oreSkullFragments", "oreMystite", "oreMystite", "oreLyon", "oreLyon", "oreGhastly", "oreGhastly", "oreGhoulish", "oreGhoulish", "oreVarsium", "oreVarsium", "oreElecanium", "oreElecanium", "oreBaronyte", "oreBaronyte", "oreBlazium", "oreBlazium", "oreRunium", "oreRunium", "oreChargedRunium", "oreChargedRunium", "oreCrystalPurple", "oreCrystalPurple", "oreCrystalBlue", "oreCrystalBlue", "oreCrystalGreen", "oreCrystalGreen", "oreCrystalYellow", "oreCrystalYellow", "oreCrystalRed", "oreCrystalRed", "oreCrystalWhite", "oreCrystalWhite"
			}));
		}
		if(oreDictIngots)
		{
			oreDictNames.addAll(Arrays.asList(new String[]{
"AmethystIngot", "ingotAmethyst", "IngotEmberstone", "ingotEmberstone", "IngotJade", "ingotJade", "IngotRosite", "ingotRosite", "IngotLimonite", "ingotLimonite", "IngotSapphire", "ingotSapphire", "IngotMystite", "ingotMystite", "IngotSkeletal", "ingotSkeletal", "IngotLyon", "ingotLyon", "IngotVarsium", "ingotVarsium", "IngotElecanium", "ingotElecanium", "IngotBaronyte", "ingotBaronyte", "IngotBlazium", "ingotBlazium", "IngotGhastly", "ingotGhastly", "IngotGhoulish", "ingotGhoulish", "IngotLunar", "ingotLunar", "IngotRustedIron", "ingotRustedIron"
			}));
		}
		for(int i = 0; i < oreDictNames.size(); i += 2)
		{
			ItemStack item = getItem(oreDictNames.get(i));
			if(item != null)
			{
				OreDictionary.registerOre(oreDictNames.get(i + 1), item);
			}
		}
	}

	public void addMaterial(String name, int harvestLevel, int durability, int miningSpeed, int attack, float handleModifier, int color)
	{
		this.addMaterial(name, harvestLevel, durability, miningSpeed, attack, handleModifier, color, "Ingot" + name, true);
	}
	public void addMaterial(String name, int harvestLevel, int durability, int miningSpeed, int attack, float handleModifier, int color, String ingotName, boolean makeBlockRecipe)
	{
		System.out.println("making material and such for " + name);
		materialNames.add(name);
		//add Material
		int id = materialId;
		materialId++;
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Id", id);
		tag.setString("Name", name);
		tag.setInteger("HarvestLevel", harvestLevel);
		tag.setInteger("Durability", durability);
		tag.setInteger("MiningSpeed", miningSpeed);
		tag.setInteger("Attack", attack);
		tag.setFloat("HandleModifier", handleModifier);
		tag.setFloat("Color", color);
		tag.setInteger("Reinforced", 1);
		tag.setInteger("Bow_DrawSpeed", 54);
		tag.setFloat("Bow_ProjectileSpeed", 3 + attack / 3F);
		tag.setFloat("Projectile_Mass", 2.05F);
		tag.setFloat("Projectile_Fragility", 0.8F);
		FMLInterModComms.sendMessage("TConstruct", "addMaterial", tag);

		//register molten form
		Fluid fluid = new Fluid(name + ".molten").setDensity(3000).setViscosity(6000).setTemperature(1300).setLuminosity(12);
		FluidRegistry.registerFluid(fluid);
		Block block = new MoltenFluid(fluid, name).setUnlocalizedName(name + ".molten").setCreativeTab(tabNevermineTiC);
		GameRegistry.registerBlock(block, "Molten" + name);
		fluid.setBlock(block);
		FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, 1000), new ItemStack(metalBucket, 1, fluids.size()));
		fluids.add(fluid);

		//solid form
		Block metalBlock = block = new BlockMetal().setTextureName("neverminetic:Block" + name).setUnlocalizedName("Block" + name).setCreativeTab(tabNevermineTiC);
		GameRegistry.registerBlock(block, "Block" + name);
		OreDictionary.registerOre("block" + name, new ItemStack(block));
		if(makeBlockRecipe)
		{
			GameRegistry.addRecipe(new ItemStack(block), blockRecipe, '#', getItem(ingotName));
		}
		GameRegistry.addRecipe(GameRegistry.findItemStack("nevermine", ingotName, 9), "#", '#', new ItemStack(block));

		//register Smeltery stuff
		tag = new NBTTagCompound();
		(new FluidStack(fluid, 1)).writeToNBT(tag);
		tag.setInteger("MaterialId", id);
		FMLInterModComms.sendMessage("TConstruct", "addPartCastingMaterial", tag);
		addSmelteryMelting(getItem(ingotName), block, fluid, 144);
		addSmelteryMelting(new ItemStack(block), block, fluid, 1296);
		if((block = GameRegistry.findBlock("nevermine", "ore" + name)) != null)
		{
			addSmelteryMelting(new ItemStack(block), block, fluid, 288);
		}

		TConstructRegistry.getTableCasting().addCastingRecipe(getItem(ingotName).copy(), new FluidStack(fluid, 144), GameRegistry.findItemStack("TConstruct", "metalPattern", 1), 20);
		TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack(metalBlock), new FluidStack(fluid, 1296), null, 20);

	}

	public ItemStack getItem(String name)
	{
		return GameRegistry.findItemStack("nevermine", name, 1);
	}

	public void addSmelteryMelting(ItemStack stack, Block block, Fluid fluid, int amount)
	{
		//ingot
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound item = new NBTTagCompound();
		stack.writeToNBT(item);
		tag.setTag("Item", item);
		item = new NBTTagCompound();
		(new ItemStack(block)).writeToNBT(item);
		tag.setTag("Block", item);
		(new FluidStack(fluid, 144)).writeToNBT(tag);
		tag.setInteger("Temperature", 650);
		System.out.println(tag);
		FMLInterModComms.sendMessage("TConstruct", "addSmelteryMelting", tag);
	}
}
