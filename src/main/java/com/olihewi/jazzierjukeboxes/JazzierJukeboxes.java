package com.olihewi.jazzierjukeboxes;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("jazzierjukeboxes")
public class JazzierJukeboxes
{
  public static final String MOD_ID = "jazzierjukeboxes";
  public JazzierJukeboxes()
  {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void commonSetup(final FMLCommonSetupEvent event)
  {
  }

  private void clientSetup(final FMLClientSetupEvent event)
  {
  }

}
