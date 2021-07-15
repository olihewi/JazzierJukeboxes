package com.olihewi.jazzier_jukeboxes.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DiscRack extends Block
{
  public static final DirectionProperty FACING = HorizontalBlock.FACING;
  public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
  protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D,0.0D,0.0D,16.0D, 7.0D, 16.0D);
  protected static final VoxelShape TOP_AABB = Block.box(0.0D,9.0D,0.0D,16.0D, 16.0D, 16.0D);

  @Override
  public boolean hasTileEntity(BlockState state)
  {
    return true;
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  {
    return new DiscRackEntity();
  }

  @Override
  public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType)
  {
    return false;
  }

  public DiscRack()
  {
    super(AbstractBlock.Properties.of(Material.WOOD)
          .strength(2.0F, 6.0F));
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(HALF, Half.BOTTOM));
  }

  @Override
  public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean moving)
  {
    if (state.getBlock() != newState.getBlock())
    {
      TileEntity tileEntity = world.getBlockEntity(blockPos);
      if (tileEntity instanceof DiscRackEntity)
      {
        DiscRackEntity discRackEntity = (DiscRackEntity) tileEntity;
        for (int i = 0; i < discRackEntity.records.getSlots(); i++)
        {
          ItemStack stack = discRackEntity.records.getStackInSlot(i);
          if (!stack.isEmpty())
          {
            world.addFreshEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack));
          }
        }
      }
    }
    super.onRemove(state, world, blockPos, newState, moving);
  }

  @Override
  public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    if (!world.isClientSide && hand == Hand.MAIN_HAND)
    {
      int slot = HitPosToSlot(hit, state);
      ItemStack heldItem = player.getItemInHand(hand);
      TileEntity tile = world.getBlockEntity(pos);
      if (tile instanceof DiscRackEntity)
      {
        DiscRackEntity discRack = (DiscRackEntity) tile;
        int closestEmptySlot = getClosestEmptySlot(discRack, slot, true);
        if (closestEmptySlot != -1 && !heldItem.isEmpty() && (heldItem.getItem() instanceof MusicDiscItem ||
            (heldItem.getTag() != null && heldItem.getTag().contains("Music"))))
        {
          discRack.records.setStackInSlot(closestEmptySlot, heldItem.copy());
          heldItem.shrink(1);
          return ActionResultType.SUCCESS;
        }
        int closestFilledSlot = getClosestEmptySlot(discRack, slot, false);
        if (heldItem.isEmpty() && closestFilledSlot != -1)
        {
          ItemStack record = discRack.records.extractItem(closestFilledSlot, 64, false);
          world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + (state.getValue(HALF) == Half.TOP ? 1.0F : 0.5F), pos.getZ() + 0.5F, record));
          return ActionResultType.SUCCESS;
        }
      }
    }
    return ActionResultType.PASS;
  }

  public static int HitPosToSlot(BlockRayTraceResult hit, BlockState state)
  {
    Vector3d pos = hit.getLocation();
    BlockPos blockPos = hit.getBlockPos();
    Direction dir = state.getValue(HorizontalBlock.FACING);
    double posAlongAxis = 0;
    switch (dir)
    {
      case NORTH:
        posAlongAxis = 1 - (pos.x - blockPos.getX());
        break;
      case EAST:
        posAlongAxis = 1 - (pos.z - blockPos.getZ());
        break;
      case SOUTH:
        posAlongAxis = pos.x - blockPos.getX();
        break;
      case WEST:
        posAlongAxis = pos.z - blockPos.getZ();
        break;
    }
    return (Math.min(Math.max((int) (posAlongAxis * 7),0),6));
  }

  public static int getClosestEmptySlot(DiscRackEntity rack, int slot, boolean empty)
  {
    if (rack.records.getStackInSlot(slot).isEmpty() == empty)
    {
      return slot;
    }
    for (int range = 1; range < 7; range++)
    {
      if (slot + range < 7 && rack.records.getStackInSlot(slot+range).isEmpty() == empty)
      {
        return slot + range;
      }
      if (slot - range >= 0 && rack.records.getStackInSlot(slot-range).isEmpty() == empty)
      {
        return slot - range;
      }
    }
    return -1;
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState p_220074_1_)
  {
    return false;
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState p_149740_1_)
  {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos)
  {
    TileEntity tile = world.getBlockEntity(pos);
    DiscRackEntity rack = (DiscRackEntity) tile;
    int fillAmount = 0;
    for (int i = 0; i < rack.records.getSlots(); i++)
    {
      if (!rack.records.getStackInSlot(i).isEmpty())
      {
        fillAmount++;
      }
    }
    return 15 * fillAmount / rack.records.getSlots();
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
  {
    return p_220053_1_.getValue(HALF) == Half.TOP ? TOP_AABB : BOTTOM_AABB;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext)
  {
    return this.defaultBlockState().setValue(FACING, blockItemUseContext.getHorizontalDirection().getOpposite())
        .setValue(HALF, blockItemUseContext.getClickLocation().y - blockItemUseContext.getClickedPos().getY() > 0.5D ? Half.TOP : Half.BOTTOM);
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> stateBuilder)
  {
    stateBuilder.add(FACING);
    stateBuilder.add(HALF);
  }
}
