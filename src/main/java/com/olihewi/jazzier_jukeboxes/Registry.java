package com.olihewi.jazzier_jukeboxes;

import com.olihewi.jazzier_jukeboxes.blocks.DiscRack;
import com.olihewi.jazzier_jukeboxes.blocks.DiscRackEntity;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registry
{
  public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JazzierJukeboxes.MOD_ID);
  public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JazzierJukeboxes.MOD_ID);
  public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JazzierJukeboxes.MOD_ID);

  public static void init()
  {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  // Disc Rack
  public static final RegistryObject<Block> DISC_RACK_BLOCK = BLOCKS.register("disc_rack", DiscRack::new);
  public static final RegistryObject<Item> DISC_RACK_ITEM = ITEMS.register("disc_rack", () -> new BlockItem(DISC_RACK_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
  public static final RegistryObject<TileEntityType<DiscRackEntity>> DISC_RACK_TILE = TILE_ENTITIES.register("disc_rack", () -> TileEntityType.Builder.of(DiscRackEntity::new,DISC_RACK_BLOCK.get()).build(null));
}
