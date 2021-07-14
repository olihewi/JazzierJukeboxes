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
import net.minecraft.state.StateContainer;
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
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        if (discRack.records.getStackInSlot(slot).isEmpty() && (heldItem.getItem() instanceof MusicDiscItem ||
            heldItem.getTag().contains("Music")))
        {
          discRack.records.setStackInSlot(slot, heldItem.copy());
          heldItem.shrink(1);
          return ActionResultType.SUCCESS;
        }
        if (heldItem.isEmpty() && !discRack.records.getStackInSlot(slot).isEmpty())
        {
          ItemStack record = discRack.records.extractItem(slot, 64, false);
          world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.6F, pos.getZ() + 0.5F, record));
          return ActionResultType.SUCCESS;
        }
      }
    }
    return ActionResultType.FAIL;
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

  @Override
  public boolean useShapeForLightOcclusion(BlockState p_220074_1_)
  {
    return false;
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_)
  {
    return Block.box(0.0D,0.0D,0.0D,16.0D, 8.0D, 16.0D);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_)
  {
    return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_)
  {
    p_206840_1_.add(FACING);
  }
}
