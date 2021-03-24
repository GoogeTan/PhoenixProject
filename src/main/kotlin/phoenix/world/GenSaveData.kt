package phoenix.world

import net.minecraft.nbt.CompoundNBT
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.WorldSavedData


class GenSaveData : WorldSavedData
{
    private var data: CompoundNBT

    constructor() : super(DATA_NAME)
    {
        data = CompoundNBT()
        data.putBoolean("iscorngened", false)
    }

    //Это второй конструктор, на всякий. А первый нужен для того, чтобы ничего не упало)
    constructor(s: String) : super(s)
    {
        data = CompoundNBT()
        data.putBoolean("iscorngened", false)
    }

    override fun read(nbt: CompoundNBT)
    {
        if (nbt.contains("gen_nbt"))
        {
            data = nbt.getCompound("gen_nbt")
        }
        else
        {
            val compound = CompoundNBT()
            data.putBoolean("iscorngened", false)
            data = compound
            nbt.put("gen_nbt", compound)
        }
    }

    override fun write(compound: CompoundNBT): CompoundNBT
    {
        compound.put("gen_nbt", data)
        return compound
    }

    var isCornGenned: Boolean
        get() =  data.getBoolean("iscorngened")
        private set(value) = data.putBoolean("iscorngened", value)

    fun setCornGenned()
    {
        isCornGenned = true
        markDirty()
    }

    companion object
    {
        private const val DATA_NAME = "phoenix_gen"

        //Этим мы получаем экземпляр данных для мира
        operator fun get(world: ServerWorld): GenSaveData
        {
            return world.savedData.getOrCreate({ GenSaveData() }, DATA_NAME)
        }
    }
}
