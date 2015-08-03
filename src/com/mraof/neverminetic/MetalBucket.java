package com.mraof.neverminetic;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class MetalBucket extends ItemBucket
{
	ArrayList<IIcon> textures = new ArrayList<IIcon>();
	public MetalBucket()
	{
		super(Blocks.air);
		this.setUnlocalizedName("metalBucket");
		this.setContainerItem(Items.bucket);
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer player)
	{
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, player, false);

		if (movingobjectposition == null)
		{
			return itemStack;
		}
		else
		{
			/*FillBucketEvent event = new FillBucketEvent(player, itemStack, par2World, movingobjectposition);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return itemStack;
			}

			if (event.getResult() == Event.Result.ALLOW)
			{
				if (player.capabilities.isCreativeMode)
				{
					return itemStack;
				}

				if (--itemStack.stackSize <= 0)
				{
					return event.result;
				}

				if (!player.inventory.addItemStackToInventory(event.result))
				{
					player.func_146097_a(event.result, false, false);
				}

				return itemStack;
			}
*/
			if (movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
			{
				int x = movingobjectposition.blockX;
				int y = movingobjectposition.blockY;
				int z = movingobjectposition.blockZ;

				if (!par2World.canMineBlock(player, x, y, z))
				{
					return itemStack;
				}

				if (itemStack.getMetadata() < 0)
				{
					return new ItemStack(Items.bucket);
				}

				switch(movingobjectposition.sideHit)
				{
					case 0:
						--y; 
						break;
					case 1:
						++y; 
						break;
					case 2:
						--z; 
						break;
					case 3:
						++z; 
						break;
					case 4:
						--x; 
						break;
					case 5:
						++x; 
						break;
				}


				if (!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, itemStack))
				{
					return itemStack;
				}

				if (this.tryPlaceContainedLiquid(par2World, x, y, z, NevermineTiC.fluids.get(itemStack.getMetadata()).getBlock()) && !player.capabilities.isCreativeMode)
				{
					return new ItemStack(Items.bucket);
				}
			}

			return itemStack;
		}
	}

	public boolean tryPlaceContainedLiquid(World par1World, int par2, int par3, int par4, Block block)
	{
		Material material = par1World.getBlock(par2, par3, par4).getMaterial();
		boolean flag = !material.isSolid();

		if (!par1World.isAirBlock(par2, par3, par4) && !flag)
		{
			return false;
		}
		else
		{
			if (!par1World.isRemote && flag && !material.isLiquid())
			{
				par1World.breakBlock(par2, par3, par4, true);
			}

			par1World.setBlock(par2, par3, par4, block, 0, 3);

			return true;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		for(int id = 0; id < NevermineTiC.fluids.size(); id++)
			subItems.add(new ItemStack(this, 1, id));
	}

	@Override
	public String getUnlocalizedName(ItemStack itemStack) 
	{
		return getUnlocalizedName() + "." + NevermineTiC.fluids.get(itemStack.getMetadata()).getUnlocalizedName().replace("tile.", "");
	}

	@Override
	public IIcon getIconFromDamage(int damage) 
	{
		if(this.textures.size() > damage)
		{
			return this.textures.get(damage);
		}
		else
		{
			return this.textures.get(0);
		}
	}
	@Override
	public void registerIcons(IIconRegister par1IconRegister)
	{
		for(int i = 0; i < NevermineTiC.fluids.size(); i++)
		{
			this.textures.add(par1IconRegister.registerIcon("neverminetic:" + NevermineTiC.materialNames.get(i) + "Bucket"));
		}
	}
}
