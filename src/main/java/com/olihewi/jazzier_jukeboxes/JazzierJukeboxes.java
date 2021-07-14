package com.olihewi.jazzier_jukeboxes;

import com.olihewi.jazzier_jukeboxes.blocks.DiscRackEntity;
import com.olihewi.jazzier_jukeboxes.blocks.DiscRackEntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("jazzier_jukeboxes")
public class JazzierJukeboxes
{
  public static final String MOD_ID = "jazzier_jukeboxes";
  public JazzierJukeboxes()
  {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

    Registry.init();

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void commonSetup(final FMLCommonSetupEvent event)
  {
  }

  private void clientSetup(final FMLClientSetupEvent event)
  {
    ClientRegistry.bindTileEntityRenderer(Registry.DISC_RACK_TILE.get(), DiscRackEntityRenderer::new);
  }

}
