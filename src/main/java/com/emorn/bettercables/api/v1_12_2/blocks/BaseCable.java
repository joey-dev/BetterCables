package com.emorn.bettercables.api.v1_12_2.blocks;

import com.emorn.bettercables.utils.IHasModel;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseCable extends BlockBase implements IHasModel
{
    protected BaseCable(String name)
    {
        super(name, Material.IRON);
        setSoundType(SoundType.METAL);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public AxisAlignedBB getBoundingBox(
        IBlockState state,
        IBlockAccess source,
        BlockPos pos
    )
    {
        EntityPlayer player = source instanceof World ? ((World) source).getClosestPlayer(
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            10,
            false
        ) : null;
        if (player == null) {
            return this.baseAABB();
        }

        Vec3d start = player.getPositionEyes(1.0F);
        Vec3d lookVec = player.getLook(1.0F);
        Vec3d end = start.add(lookVec.scale(5));

        IBlockState actualState = getActualState(state, source, pos);
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        AxisAlignedBB closestBox = this.baseAABB();
        double closestDistance = Double.MAX_VALUE;

        for (AxisAlignedBB box : allBoxes) {
            RayTraceResult result = rayTrace(pos, start, end, box);
            if (result != null) {
                double distance = result.hitVec.distanceTo(start);
                if (distance < closestDistance) {
                    closestBox = box;
                    closestDistance = distance;
                }
            }
        }

        return closestBox;
    }

    protected abstract AxisAlignedBB baseAABB();

    protected abstract List<AxisAlignedBB> retrieveAllBoxes(IBlockState state);

    @Override
    public void addCollisionBoxToList(
        IBlockState state,
        World worldIn,
        BlockPos pos,
        AxisAlignedBB entityBox,
        List<AxisAlignedBB> collidingBoxes,
        @Nullable Entity entityIn,
        boolean isActualState
    )
    {
        IBlockState actualState = getActualState(state, worldIn, pos);
        List<AxisAlignedBB> allBoxes = retrieveAllBoxes(actualState);

        for (AxisAlignedBB box : allBoxes) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, box);
        }
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void onBlockDestroyedByPlayer(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
        if (!worldIn.isRemote) {
            // todo fix
            //NetworkManager.reCalculateNetworksAround(pos, worldIn);
        }
    }

    @Override
    public Item getItemDropped(
        IBlockState state,
        Random rand,
        int fortune
    )
    {
        return Item.getItemFromBlock(this.currentBlock());
    }

    protected abstract Block currentBlock();

    @Override
    public void onBlockDestroyedByExplosion(
        World worldIn,
        BlockPos pos,
        Explosion explosionIn
    )
    {
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
        if (!worldIn.isRemote) {
            // todo fix
            //NetworkManager.reCalculateNetworksAround(pos, worldIn);
        }
    }

    @Override
    public ItemStack getItem(
        World worldIn,
        BlockPos pos,
        IBlockState state
    )
    {
        return new ItemStack(this.currentBlock());
    }
}