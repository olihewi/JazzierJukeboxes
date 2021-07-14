package com.olihewi.jazzier_jukeboxes.blocks;

import com.olihewi.jazzier_jukeboxes.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DiscRackEntity extends TileEntity
{
  public final ItemStackHandler records = new ItemStackHandler(7)
  {
    @Override
    protected void onContentsChanged(int slot)
    {
      super.onContentsChanged(slot);
      DiscRackEntity.this.setChanged();
      DiscRackEntity.this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 2);
    }
  };

  public DiscRackEntity()
  {
    super(Registry.DISC_RACK_TILE.get());
  }

  @Override
  public void load(BlockState p_230337_1_, CompoundNBT nbt)
  {
    records.deserializeNBT(nbt.getCompound("inventory"));
    super.load(p_230337_1_, nbt);
  }

  @Override
  public CompoundNBT save(CompoundNBT p_189515_1_)
  {
    p_189515_1_.put("inventory", records.serializeNBT());
    return super.save(p_189515_1_);
  }

  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket()
  {
    return new SUpdateTileEntityPacket(getBlockPos(),1, getUpdateTag());
  }

  @Nonnull
  @Override
  public CompoundNBT getUpdateTag()
  {
    return serializeNBT();
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
  {
    this.deserializeNBT(pkt.getTag());
  }
}
