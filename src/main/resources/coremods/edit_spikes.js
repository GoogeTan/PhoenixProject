function initializeCoreMod()
{
    return
    {
        'edit_spikes':
        {
            'target':
            {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.feature.EndSpikeFeature',
                'methodName': 'func_214553_a',
                'methodDesc': '(Lnet/minecraft/world/IWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/EndSpikeFeatureConfig)Z;'
            },
            'transformer': function(method)
            {
                var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                ASMAPI.log('INFO', 'Added \'spikes\' ASM patch!');

                var methodCall = ASMAPI.findFirstMethodCallAfter(method,
                                        ASMAPI.MethodType.VIRTUAL,
                                        'V',
                                        callGetEnchantmentTagList,
                                        '()Lnet/minecraft/nbt/ListNBT;',
                                        0);

                method.instructions.insertBefore(methodCall.getNext(), new VarInsnNode(Opcodes.ALOAD, 1)); //ItemStack
                return method;
            }
        }
    }
}