package projectend.init;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projectend.ProjectEnd;
import projectend.client.render.RenderEntityUterus;
import projectend.entity.unit02.EntityUterus;

@Mod.EventBusSubscriber(modid = ProjectEnd.MOD_ID)
public class EntityRegister
{
    private static int ID = 0;//Для айди
    public static EntityEntry UTERUS = EntityEntryBuilder
            .create()//Создаём новый EntityEntry
            .entity(EntityUterus.class)//Какой моб в EntityEntry
            .name("Uterus")//Имя
            .id("uterus", ID++)//имя регистрации и Айди
            .egg( 0x1E90FF, 0x87CEEB)//Цвет яйца, первое значение - фона, второе - "точек"(можно не добавлять)
            .tracker(120, 3, false)//Трекер моба(Первое значение - радиус для которого моб будет обновлятся, второе - частота обновлений за секунду, третье - будет ли отправляться пакет с обновление позиции игрокам)
            .build();//Устанавливаем параметры

    @SubscribeEvent
    public static void registryEntity(RegistryEvent.Register<EntityEntry> event)
    {
        event.getRegistry().registerAll
        (
                UTERUS
        );
    }

    @SideOnly(Side.CLIENT)//Только клиент
    public static void initModels() {
        /*Регистрируем рендер, 1 параметр = класс моба, 2 параметр = НАШ РЕНДЕР ФЭКТОРИ */
        RenderingRegistry.registerEntityRenderingHandler(EntityUterus.class, RenderEntityUterus.FACTORY);
        //RenderingRegistry.registerEntityRenderingHandler(EntityUterusSegment.class, RenderEntityUterus.FACTORY);
    }
}
