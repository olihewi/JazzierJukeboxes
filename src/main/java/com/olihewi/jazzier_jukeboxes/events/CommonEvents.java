package com.olihewi.jazzier_jukeboxes.events;

import com.olihewi.jazzier_jukeboxes.JazzierJukeboxes;
import com.olihewi.jazzier_jukeboxes.Registry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = JazzierJukeboxes.MOD_ID)
public class CommonEvents
{
  @SubscribeEvent
  public static void onMobDeath(LivingDeathEvent event)
  {
    if (event.getEntity() instanceof PillagerEntity && event.getSource().getEntity() instanceof SkeletonEntity)
    {
      Entity entity = event.getEntity();
      entity.level.addFreshEntity(new ItemEntity(entity.level, entity.getX(),entity.getY(),entity.getZ(),new ItemStack(Registry.MUSIC_DISC_RAVAGED.get())));
    }
  }
}
