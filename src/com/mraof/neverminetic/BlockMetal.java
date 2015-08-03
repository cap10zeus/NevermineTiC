package com.mraof.neverminetic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockMetal extends Block
{
	public BlockMetal()
	{
		super(Material.iron);
		this.setHardness(10F);
		this.setHarvestLevel("pickaxe", 1);
	}
	
	public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
	{
		return true;
	}
}
