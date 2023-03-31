package fr.weefle.constructor.NMS;

import fr.weefle.constructor.block.DataBuildBlock;
import fr.weefle.constructor.block.EmptyBuildBlock;
import fr.weefle.constructor.essentials.BuilderSchematic;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class MCEditSchematicFormat {

	private final Map<Integer,BlockData> blocks = new HashMap<>();
	public int dataVersion;
	//private ArrayList<EntityMap> entitieslist = new ArrayList<>();


	/*public ArrayList<EntityMap> getEntitieslist() {
		return entitieslist;
	}


	public void setEntitieslist(ArrayList<EntityMap> entitieslist) {
		this.entitieslist = entitieslist;
	}*/


	public BuilderSchematic load(File path, String filename) throws IOException {

		File file = new File(path, filename+".schem");

		if (!file.exists()) { throw (new java.io.FileNotFoundException("File not found")); }

		FileInputStream fis = new FileInputStream(file);
		Object nbt = NMS.getInstance().getNMSProvider().loadNBTFromInputStream(fis);

		//Bukkit.getLogger().warning(nbt.toString());

		dataVersion = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(nbt, "DataVersion");
		//Bukkit.getLogger().warning(dataVersion.toString());

		Vector origin = new Vector();
		Vector offsetvec = new Vector();
		Vector offsetWE = new Vector();

		short width = NMS.getInstance().getNMSProvider().nbtTagCompound_getShort(nbt, "Width");
		short height = NMS.getInstance().getNMSProvider().nbtTagCompound_getShort(nbt, "Height");
		short length = NMS.getInstance().getNMSProvider().nbtTagCompound_getShort(nbt, "Length");

		int xoff, yoff, zoff;

		int[] offset = NMS.getInstance().getNMSProvider().nbtTagCompound_getIntArray(nbt, "Offset");
		if (offset.length == 3) {
			xoff = offset[0];
			yoff = offset[1];
			zoff = offset[2];
			offsetvec = new Vector(xoff, yoff, zoff);
		}

		Object meta = NMS.getInstance().getNMSProvider().nbtTagCompound_getCompound(nbt, "Metadata");

		if (meta != null) {

			int offsetX = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(meta, "WEOffsetX");
			int offsetY = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(meta, "WEOffsetY");
			int offsetZ = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(meta, "WEOffsetZ");
			offsetWE = new Vector(offsetX, offsetY, offsetZ);
			//Bukkit.getLogger().warning(originX + "," + originY + "," + originZ);
			origin = offsetvec.subtract(offsetWE);
		}

		byte[] blockData = NMS.getInstance().getNMSProvider().nbtTagCompound_getByteArray(nbt, "BlockData");
		Object palette = NMS.getInstance().getNMSProvider().nbtTagCompound_getCompound(nbt, "Palette");
		//Bukkit.getLogger().info(palette.getKeys().toString());
		//try {
		for (String rawState : NMS.getInstance().getNMSProvider().nbtTagCompound_getAllKeys(palette)) {
			int id = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(palette, rawState);
			if (getRawState(rawState) != null) {
				blocks.put(id, getRawState(rawState));
			} else {
				String material;
				if (rawState.contains("[")) {
					material = rawState.substring(0, rawState.indexOf("[")).replace("minecraft:", "").toUpperCase();
				} else {
					material = rawState.replace("minecraft:", "").toUpperCase();
				}
				//Bukkit.getLogger().warning(material);
					Material mat;
					if (EnumSet.allOf(Material.class).contains(Material.getMaterial(material)))
					{
						mat = Material.getMaterial(material);
					}else{
						mat = Material.getMaterial(material, true);
					}
					blocks.put(id, Bukkit.createBlockData(Objects.requireNonNull(mat)));
				}

				}

			/*	} catch (Exception e) {

			//TODO get rawState minecraft:id and remove [***] to convert into Material and create empty BlockData
					//Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Schematic was made in an other Minecraft version (" + dataVersion + ") Data is incompatible!");
					//Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "You need to convert your schematic in the right Data Version: https://minecraft.gamepedia.com/Data_version#List_of_data_versions");
			for(String rawState2 : palette.getKeys()) {

				int id2 = palette.getInt(rawState2);
				String material;
				if(rawState2.contains("[")) {
					material = rawState2.substring(0, rawState2.indexOf("[")).replace("minecraft:", "").toUpperCase();
				}else{
					material = rawState2.replace("minecraft:", "").toUpperCase();
				}
				//Bukkit.getLogger().warning(material);
				Material mat;
				if (EnumSet.allOf(Material.class).contains(Material.getMaterial(material)))
				{
					mat = Material.getMaterial(material);
				}else{
					mat = Material.getMaterial(material, true);
				}

				BlockData blockData3 = Bukkit.createBlockData(mat);
				blocks.put(id2, blockData3);

			}
					//return null;
				}*/

		int version = NMS.getInstance().getNMSProvider().nbtTagCompound_getInt(nbt, "Version");

		AbstractList<Object> tileEntities;
		//NBTTagList entities = null;
		//Bukkit.getLogger().warning(nbt+"");
		if(version>1) {
			//ArrayList<NBTTagDouble> listd = new ArrayList<NBTTagDouble>();
			tileEntities = NMS.getInstance().getNMSProvider().nbtTagCompound_getList(nbt, "BlockEntities", 10);
		/*entities = nbt.getList("Entities", 10);
		for (NBTBase tag : entities) {
			if (!(tag instanceof NBTTagCompound)) continue;
			NBTTagCompound t = (NBTTagCompound) tag;
			
			//Bukkit.getLogger().warning(t+"");

			double x = 0;
			double y = 0;
			double z = 0;
			
			String id = t.getString("Id");
			
			NBTTagList pos = t.getList("Pos", 6);
			//Bukkit.getLogger().warning(pos+"");

			for(NBTBase posi: pos) {
				if (!(posi instanceof NBTTagDouble)) continue;
				 NBTTagDouble tdouble = (NBTTagDouble) posi;
				 listd.add(tdouble);
			}
			//Bukkit.getLogger().warning(listd.size()+" avant");
			x = listd.get(0).asDouble();
			y = listd.get(1).asDouble();
			z = listd.get(2).asDouble();
			listd.removeAll(listd);
			//Bukkit.getLogger().warning(listd.size()+" apres");
				
				//Bukkit.getLogger().warning(t+"");
			id = id.replace("minecraft:", "");
			x = xoff - x;
			y = yoff - y;
			z = zoff - z;
			//Bukkit.getLogger().warning("Position: " + x + "," + y + "," + z);
			EntityMap ent = new EntityMap(x,y,z,t,id);
			entitieslist.add(ent);
				
	            
		}*/
		} else {
			tileEntities = NMS.getInstance().getNMSProvider().nbtTagCompound_getList(nbt, "TileEntities", 10);
		}
		//Bukkit.getLogger().warning(tileEntities+"");
		Map<Vector,Object> tileEntitiesMap = new HashMap<>();

		Class<?> nbtTagCompound = NMS.getInstance().getNMSProvider().getNBTTagCompoundClass();
		for (Object tag : tileEntities) {
			if (!nbtTagCompound.isAssignableFrom(tag.getClass())) { continue; }

			int x, y, z;

			int[] pos = NMS.getInstance().getNMSProvider().nbtTagCompound_getIntArray(tag, "Pos");
			if (pos.length == 3) {
				x = pos[0];
				y = pos[1];
				z = pos[2];


				Vector vec = new Vector(x, y, z);
				tileEntitiesMap.put(vec, tag);
			}

		}


		BuilderSchematic out = new BuilderSchematic(width, height, length);

		int index = 0;
		int i = 0;
		int value;
		int varint_length;
		while (i < blockData.length) {
			value = 0;
			varint_length = 0;

			while (true) {
				value |= (blockData[i] & 127) << (varint_length++*7);
				if (varint_length > 5) {
					throw new RuntimeException("VarInt too big (probably corrupted data)");
				}
				if ((blockData[i] & 128) != 128) {
					i++;
					break;
				}
				i++;
			}
			int y = index/(width*length);
			int z = (index%(width*length))/width;
			int x = (index%(width*length))%width;
			BlockData data = blocks.get(value);

			EmptyBuildBlock M;

			Vector v = null;
			for (Vector victor : tileEntitiesMap.keySet()) {
				if (victor.getBlockX() == x && victor.getBlockY() == y && victor.getBlockZ() == z) {
					v = victor;
					break;
				}
				}

				if(v!=null) {

					M = new TileBuildBlock(x, y, z, data);
					((TileBuildBlock) M).nbt = tileEntitiesMap.get(v);
					//Bukkit.getLogger().warning(tileEntitiesMap.get(v)+"");
					//((TileBuildBlock_1_15_R1)M).id = ((NBTTagCompound) tileEntitiesMap.get(v)).getString("Id");
					tileEntitiesMap.remove(v);

				}else if(data==null) {
					

					
					M = new EmptyBuildBlock(x,y,z);
					
				}else {
					M = new DataBuildBlock(x,y,z, data);
				}

				out.Blocks[x][y][z] = M;
				//Bukkit.getLogger().warning(x+","+y+","+z);

	            index++;
	        }
	        //Bukkit.getLogger().warning(out.Blocks[10][11][6].getMat().getAsString()); 
		

		out.Name = filename;
		out.SchematicOrigin = origin;
		out.offset = offsetWE;
		fis.close();
		return out;
		
	}

	public BlockData getRawState(String rawState){

		BlockData bData;
		try {
			bData = Bukkit.createBlockData(rawState);
		}catch (Exception e){
			return null;
		}

		return bData;

	}

}
	