package com.mraof.neverminetic;

import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketHandler
{
	@SubscribeEvent
	public void fillBucket(FillBucketEvent event)
	{
		if(event.current.getItem() == Items.bucket && event.target.typeOfHit == MovingObjectType.BLOCK)
		{
			int x = event.target.blockX;
			int y = event.target.blockY;
			int z = event.target.blockZ;
			if(event.entityPlayer != null && !event.entityPlayer.canPlayerEdit(x, y, z, event.target.sideHit, event.current))
			{
				return;
			}

			for(int i = 0; i < NevermineTiC.fluids.size(); i++)
			{
				System.out.println(NevermineTiC.fluids.get(i).getBlock());
				if(event.world.getBlock(x, y, z) == NevermineTiC.fluids.get(i).getBlock())
				{
					event.world.setBlockToAir(x, y, z);
					event.result = new ItemStack(NevermineTiC.metalBucket, 1, i);
					event.setResult(Result.ALLOW);
				}
			}
		}
	}
}
