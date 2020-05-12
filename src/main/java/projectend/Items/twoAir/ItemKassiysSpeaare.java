package projectend.Items.twoAir;

import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import projectend.Projectend;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKassiysSpeaare extends ItemSword
{
    public ItemKassiysSpeaare()
    {
        super(Projectend.siliconlife);
        setRegistryName("kassiesspeare");
        setCreativeTab(Projectend.TheEndOfCreativeTabs);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("It uses 1.8 PVP sistem");
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(EntityEquipmentSlot.MAINHAND);
            multimap.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 96D, 0));
            return multimap;
        }
        return super.getItemAttributeModifiers(equipmentSlot);
    }
}
