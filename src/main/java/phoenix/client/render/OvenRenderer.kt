package phoenix.client.render

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.block.AbstractFurnaceBlock
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.model.ItemCameraTransforms
import net.minecraft.client.renderer.tileentity.TileEntityRenderer
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.IContainerListener
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SSetSlotPacket
import net.minecraft.util.Direction
import net.minecraft.util.NonNullList
import phoenix.init.PhoenixItems
import phoenix.tile.ash.OvenTile

class OvenRenderer(rendererDispatcherIn: TileEntityRendererDispatcher) : TileEntityRenderer<OvenTile>(rendererDispatcherIn), IContainerListener
{
    override fun render(te: OvenTile, partialTicks: Float, matrixStackIn: MatrixStack, bufferIn: IRenderTypeBuffer, combinedLightIn: Int, combinedOverlayIn: Int)
    {
        val direction: Direction = te.blockState[AbstractFurnaceBlock.FACING]
        matrixStackIn.translate(0.0, 1.0, 0.0)
        for (i in 0 until te.inventory.size)
        {
            val stack: ItemStack = te.inventory[i]
            if (stack != ItemStack.EMPTY)
            {
                matrixStackIn.push()
                matrixStackIn.translate(0.5, 0.44921875, 0.5)
                val direction1 = Direction.byHorizontalIndex((i + direction.horizontalIndex) % 4)
                val dirAngle = -direction1.horizontalAngle
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(dirAngle))
                matrixStackIn.translate(-0.15, -0.3125, 0.15)
                if (stack.item.containerItem == PhoenixItems.CRUCIBLE.get())
                {
                    matrixStackIn.scale(0.7f, 0.7f, 0.7f)
                }
                else
                {
                    matrixStackIn.translate(0.0, -0.1, 0.0)
                    matrixStackIn.scale(0.3f, 0.3f, 0.3f)
                    matrixStackIn.rotate(Quaternion(90f, 0f, 90f, true))
                }
                Minecraft.getInstance().getItemRenderer().renderItem(
                    stack,
                    ItemCameraTransforms.TransformType.FIXED,
                    combinedLightIn,
                    combinedOverlayIn,
                    matrixStackIn,
                    bufferIn
                )
                matrixStackIn.pop()
            }
        }
    }


    /**
     * update the crafting window inventory with the items in the list
     */
    override fun sendAllContents(containerToSend: Container, itemsList: NonNullList<ItemStack>)
    {
        for(i in itemsList.indices)
         sendSlotContents(containerToSend, i, containerToSend.getSlot(i).stack)
    }

    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot.
     */
    override fun sendSlotContents(containerToSend: Container, slotInd: Int, stack: ItemStack)
    {
        if (slotInd == 0)
        {
            for (i in containerToSend.playerList)
                (i as ServerPlayerEntity).connection.sendPacket(SSetSlotPacket(containerToSend.windowId, slotInd, stack))
        }
    }

    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     */
    override fun sendWindowProperty(containerIn: Container?, varToUpdate: Int, newValue: Int)
    {
    }
}