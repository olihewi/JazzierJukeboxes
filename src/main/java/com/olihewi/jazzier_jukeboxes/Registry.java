package com.olihewi.jazzier_jukeboxes;

import com.olihewi.jazzier_jukeboxes.blocks.DiscRack;
import com.olihewi.jazzier_jukeboxes.blocks.DiscRackEntity;
import com.olihewi.jazzier_jukeboxes.events.CommonEvents;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registry
{
  public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JazzierJukeboxes.MOD_ID);
  public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, JazzierJukeboxes.MOD_ID);
  public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, JazzierJukeboxes.MOD_ID);
  public static DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, JazzierJukeboxes.MOD_ID);

  public static void init()
  {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  // Disc Rack
  public static final RegistryObject<Block> DISC_RACK_BLOCK = BLOCKS.register("disc_rack", DiscRack::new);
  public static final RegistryObject<Item> DISC_RACK_ITEM = ITEMS.register("disc_rack", () -> new BlockItem(DISC_RACK_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_DECORATIONS)));
  public static final RegistryObject<TileEntityType<DiscRackEntity>> DISC_RACK_TILE = TILE_ENTITIES.register("disc_rack", () -> TileEntityType.Builder.of(DiscRackEntity::new,DISC_RACK_BLOCK.get()).build(null));

  // Banner Pattern
  public static final BannerPattern MUSIC_NOTE_BANNER = BannerPattern.create("MUSIC_NOTE","music_note", "nte");
  public static final RegistryObject<Item> MUSIC_NOTE_BANNER_PATTERN = ITEMS.register("music_note_banner_pattern", () -> new BannerPatternItem(MUSIC_NOTE_BANNER, new Item.Properties().tab(ItemGroup.TAB_MISC)));

  // Music Disc
  public static final Lazy<SoundEvent> RAVAGED_LAZY = Lazy.of(() -> new SoundEvent(new ResourceLocation(JazzierJukeboxes.MOD_ID,"music.record.ravaged")));
  public static final RegistryObject<SoundEvent> RAVAGED = SOUNDS.register("music.record.ravaged.disc", RAVAGED_LAZY);
  public static final RegistryObject<Item> MUSIC_DISC_RAVAGED = ITEMS.register("music_disc_ravaged", () -> new MusicDiscItem(14, RAVAGED_LAZY.get(), new Item.Properties().tab(ItemGroup.TAB_MISC).rarity(Rarity.RARE).stacksTo(1)));
}
