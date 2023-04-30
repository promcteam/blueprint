package fr.weefle.constructor.nms;

import fr.weefle.constructor.schematic.blocks.EmptyBuildBlock;
import fr.weefle.constructor.schematic.blocks.EntityMap;
import fr.weefle.constructor.schematic.blocks.TileBuildBlock;
import org.bukkit.block.Block;

public class TileChecker implements fr.weefle.constructor.api.TileChecker {


    public void check(EmptyBuildBlock next, Block pending) {
        if (next instanceof TileBuildBlock) {
            Object nbt = NMS.getInstance().getNMSProvider().newNBTTagCompound();
            //Bukkit.getLogger().warning(((TileBuildBlock) next).nbt + "");
            NMS.getInstance().getNMSProvider().nbtTagCompound_merge(nbt, ((TileBuildBlock) next).getNBT());
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nbt, "x", pending.getX());
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nbt, "y", pending.getY());
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nbt, "z", pending.getZ());
            //nbt.set("Items", ((TileBuildBlock) next).items);
            //nbt.setString("id", ((TileBuildBlock_1_15_R1) next).id);
            Object bp = NMS.getInstance().getNMSProvider().newBlockPosition(pending.getX(), pending.getY(), pending.getZ());
            //Bukkit.getLogger().warning(pending.getX()+","+ pending.getY()+"," +pending.getZ());
            Object te = NMS.getInstance().getNMSProvider().world_getTileEntity(pending.getWorld(), bp);
            //Bukkit.getLogger().warning(te.getBlock().toString() + " : " + bp);
            if (te != null) {
                //te.a(nbt);
                NMS.getInstance().getNMSProvider().blockEntity_load(te, nbt);
                //te.update();
                //pending.getState().update(true, true);
            }
        } else if (next instanceof EntityMap) {
            //NBTTagCompound nmsnbt = new NBTTagCompound();
            Object nmsnbt = NMS.fromNative(((EntityMap) next).getNBT());
            //nmsnbt.a((NBTTagCompound) NMSUtil.fromNative(nbt));
            //Bukkit.getLogger().warning(nbt.asString());
            //nmsnbt.set("Items", items);
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nmsnbt, "x", pending.getX());
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nmsnbt, "y", pending.getY());
            NMS.getInstance().getNMSProvider().nbtTagCompound_putInt(nmsnbt, "z", pending.getZ());
            //Bukkit.getLogger().warning(nmsnbt.asString());
            Object bp = NMS.getInstance().getNMSProvider().newBlockPosition(pending.getX(), pending.getY(), pending.getZ());
            Object te = NMS.getInstance().getNMSProvider().world_getTileEntity(pending.getWorld(), bp);

            if (te != null) {
                //te.a(nmsnbt);
                NMS.getInstance().getNMSProvider().blockEntity_load(te, nmsnbt);
            }
        }

    }


}
