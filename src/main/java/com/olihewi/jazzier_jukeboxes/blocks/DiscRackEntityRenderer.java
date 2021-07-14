package com.olihewi.jazzier_jukeboxes.blocks;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class DiscRackEntityRenderer extends TileEntityRenderer<DiscRackEntity>
{
  public DiscRackEntityRenderer(TileEntityRendererDispatcher rendererDispatcher)
  {
    super(rendererDispatcher);
  }

  @Override
  public void render(DiscRackEntity rack, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
  {
    matrix.pushPose();
    Direction facing = rack.getBlockState().getValue(HorizontalBlock.FACING);
    switch (facing)
    {
      case NORTH:
        matrix.translate(1.03125D,0.4375D,0.5D);
        matrix.mulPose(Vector3f.YP.rotationDegrees(270));
        break;
      case EAST:
        matrix.translate(0.5D,0.4375D,1.03125D);
        matrix.mulPose(Vector3f.YP.rotationDegrees(180));
        break;
      case SOUTH:
        matrix.translate(-0.03125D,0.4375D,0.5D);
        matrix.mulPose(Vector3f.YP.rotationDegrees(90));
        break;
      case WEST:
        matrix.translate(0.5D,0.4375D,-0.03125D);
        break;
    }
    for (int i = 0; i < 7; i++)
    {
      ItemStack item = rack.records.getStackInSlot(i);
      matrix.translate(0.0D, 0.0D, 0.125D);
      if (item.isEmpty()) { continue; }
      Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemCameraTransforms.TransformType.FIXED, light, overlay, matrix, buffer);
    }
    matrix.popPose();
    // Nameplate
    RayTraceResult rayTraceResult = Minecraft.getInstance().hitResult;
    if (rayTraceResult instanceof BlockRayTraceResult && rayTraceResult.getType() == RayTraceResult.Type.BLOCK)
    {
      BlockRayTraceResult rayTraceResult1 = (BlockRayTraceResult) rayTraceResult;
      World level = rack.getLevel();
      if (level != null && level.getBlockEntity(rayTraceResult1.getBlockPos()) == rack)
      {
        int slot = DiscRack.HitPosToSlot(rayTraceResult1, rack.getBlockState());
        ItemStack stack = rack.records.getStackInSlot(slot);
        if (!stack.isEmpty())
        {
          ITextComponent text;
          if (stack.getItem() instanceof MusicDiscItem)
          {
            MusicDiscItem musicDiscItem = (MusicDiscItem) stack.getItem();
            text = musicDiscItem.getDisplayName();
          }
          else // Etched Support
          {
            CompoundNBT tag = stack.getTag().getCompound("Music");
            String str = tag.getString("Author") + " - " + tag.getString("Title");
            text = new StringTextComponent(str);
          }
          matrix.pushPose();
          switch (facing)
          {
            case NORTH:
              matrix.translate(1 - slot / 7D,1.2D,0.5D);
              break;
            case EAST:
              matrix.translate(0.5D,1.2D,1 - slot / 7D);
              break;
            case SOUTH:
              matrix.translate(slot / 7D,1.2D,0.5D);
              break;
            case WEST:
              matrix.translate(0.5D,1.2D,slot / 7D);
              break;
          }
          matrix.mulPose(renderer.camera.rotation());
          matrix.scale(-0.025F, -0.025F, -0.025F);
          FontRenderer fontRenderer = renderer.getFont();
          float f2 = (float)(-fontRenderer.width(text) / 2);
          float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
          Matrix4f matrix4f = matrix.last().pose();
          int j = (int)(f1 * 255.0F) << 24;
          fontRenderer.drawInBatch(text, f2, 0, 553648127, false, matrix4f, buffer, true, j, light);
          fontRenderer.drawInBatch(text, f2, 0, -1, false, matrix4f, buffer, false, 0, light);
          matrix.popPose();
        }
      }
    }
  }
}
