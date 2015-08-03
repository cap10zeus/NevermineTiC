package com.mraof.neverminetic;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class MoltenFluid extends BlockFluidClassic
{
	public String name;
	public IIcon stillIcon;
	public IIcon flowingIcon;

	public MoltenFluid(Fluid fluid, String name)
	{
		super(fluid, Material.lava);
		this.name = name;
	}

	@Override
	public int getRenderBlockPass()
	{
		return 0;
	}

	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		stillIcon = iconRegister.registerIcon("neverminetic:" + name + "Still");
		flowingIcon = iconRegister.registerIcon("neverminetic:" + name + "Flowing");
		stack.getFluid().setStillIcon(this.stillIcon);
		stack.getFluid().setFlowingIcon(this.flowingIcon);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return (side == 0 || side == 1) ? stillIcon : flowingIcon;
	}
}
