package phoenix.utils;


import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.EnhancedRuntimeException;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class EnumHelper {
    private static Object reflectionFactory = null;
    private static Method newConstructorAccessor = null;
    private static Method newInstance = null;
    private static Method newFieldAccessor = null;
    private static Method fieldAccessorSet = null;
    private static boolean isSetup = false;
    private static Class<?>[][] commonTypes;

    public EnumHelper() {
    }

    @Nullable
    public static ActionResultType addAction(String name) {
        return addEnum(ActionResultType.class, name);
    }

    @Nullable
    public static RayTraceResult.Type addMovingObjectType(String name) {
        return addEnum(RayTraceResult.Type.class, name);
    }

    @Nullable
    public static PlayerEntity.SleepResult addStatus(String name) {
        return addEnum(PlayerEntity.SleepResult.class, name);
    }

    @Nullable
    public static Rarity addRarity(String name, TextFormatting color, String displayName) {
        return (Rarity)addEnum(Rarity.class, name, color, displayName);
    }

    @Nullable
    public static RecipeBookCategories addRecipeBookCategory(String name, ItemStack[] stacks) {
        return addEnum(RecipeBookCategories.class, name, stacks);
    }


    private static void setup() {
        if (!isSetup) {
            try {
                Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory");
                reflectionFactory = getReflectionFactory.invoke((Object)null);
                newConstructorAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newConstructorAccessor", Constructor.class);
                newInstance = Class.forName("sun.reflect.ConstructorAccessor").getDeclaredMethod("newInstance", Object[].class);
                newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, Boolean.TYPE);
                fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
            } catch (Exception var1) {
                LogManager.error(EnumHelper.class, "Error setting up EnumHelper.");
            }

            isSetup = true;
        }
    }
    private static <T extends Enum<?>> T makeEnum(Class<T> enumClass) throws Exception {
        ItemStack[] params2 = {new ItemStack(Blocks.OBSERVER)};
        Class<ItemStack[]> clazz = ItemStack[].class;
        return enumClass.cast(newInstance.invoke(newConstructorAccessor.invoke(reflectionFactory, RecipeBookCategories.class.getDeclaredConstructor(clazz)), (Object) params2));
    }

    public static void setFailsafeFieldValue(Field field, @Nullable Object target, @Nullable Object value) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & -17);
        Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, field, false);
        fieldAccessorSet.invoke(fieldAccessor, target, value);
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws Exception {
        Field[] var2 = Class.class.getDeclaredFields();
        int var3 = var2.length;

        for (Field field : var2)
        {
            if (field.getName().contains(fieldName))
            {
                field.setAccessible(true);
                setFailsafeFieldValue(field, enumClass, null);
                break;
            }
        }

    }

    private static void cleanEnumCache(Class<?> enumClass) throws Exception {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Object... paramValues) {
        setup();
        return addEnum(commonTypes, enumType, enumName, paramValues);
    }

    @Nullable
    protected static <T extends Enum<?>> T addEnum(Class<?>[][] map, Class<T> enumType, String enumName, Object... paramValues) {
        int var5 = map.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Class<?>[] lookup = ((Class[][]) map)[var6];
            if (lookup[0] == enumType) {
                Class<?>[] paramTypes = new Class[lookup.length - 1];
                if (paramTypes.length > 0) {
                    System.arraycopy(lookup, 1, paramTypes, 0, paramTypes.length);
                }

                return addEnum(enumType, enumName, paramTypes, paramValues);
            }
        }

        return null;
    }

    public static void testEnum(Class<? extends Enum<?>> enumType, Class<?>[] paramTypes) {
        addEnum(true, enumType, (String)null, paramTypes, (Object[])null);
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        return addEnum(false, enumType, enumName, paramTypes, paramValues);
    }

    @Nullable
    private static <T extends Enum<?>> T addEnum(boolean test, final Class<T> enumType, @Nullable String enumName, final Class<?>[] paramTypes, @Nullable Object[] paramValues) {
        if (!isSetup) {
            setup();
        }

        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        Field[] var7 = fields;
        int var8 = fields.length;

        for(int var9 = 0; var9 < var8; ++var9) {
            Field field = var7[var9];
            String name = field.getName();
            if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) {
                valuesField = field;
                break;
            }
        }

        int flags = 1 | 8 | 16 | 4096;
        Field field;
        Field[] var21;
        int var22;
        int var27;
        if (valuesField == null) {
            String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));
            var21 = fields;
            var22 = fields.length;

            for(var27 = 0; var27 < var22; ++var27) {
                field = var21[var27];
                if ((field.getModifiers() & flags) == flags && field.getType().getName().replace('.', '/').equals(valueType)) {
                    valuesField = field;
                    break;
                }
            }
        }

        if (valuesField != null) {
            if (test) {
                Object ctr = null;
                Exception ex = null;

                try {
                    ctr = newConstructorAccessor.invoke(reflectionFactory, enumType.getDeclaredConstructor(paramTypes));
                } catch (Exception var14) {
                    ex = var14;
                }

                if (ctr != null && ex == null) {
                    return null;
                } else {
                    throw new EnhancedRuntimeException(String.format("Could not find constructor for Enum %s", enumType.getName()), ex) {
                        private String toString(Class<?>[] cls) {
                            StringBuilder b = new StringBuilder();

                            for(int x = 0; x < cls.length; ++x) {
                                b.append(cls[x].getName());
                                if (x != cls.length - 1) {
                                    b.append(", ");
                                }
                            }

                            return b.toString();
                        }

                        protected void printStackTrace(WrappedPrintStream stream) {
                            stream.println("Target Arguments:");
                            stream.println("    java.lang.String, int, " + this.toString(paramTypes));
                            stream.println("Found Constructors:");

                            for (Constructor<?> ctr : enumType.getDeclaredConstructors())
                            {
                                stream.println("    " + this.toString(ctr.getParameterTypes()));
                            }

                        }
                    };
                }
            } else
            {
                valuesField.setAccessible(true);
                T[] previousValues;
                T newValue;
                try
                {
                    previousValues = (T[]) valuesField.get(enumType);

                } catch (Exception e)
                {
                    LogManager.error(EnumHelper.class, "Error adding enum with EnumHelper.");
                    throw new RuntimeException(e);
                }
                try
                {
                    newValue = makeEnum(enumType);
                } catch (Exception e)
                {
                    LogManager.error(EnumHelper.class, "Error adding enum with EnumHelper.");
                    throw new RuntimeException(e);
                }

                try
                {

                    setFailsafeFieldValue(valuesField, null, ArrayUtils.add(previousValues, newValue));
                    cleanEnumCache(enumType);
                    return newValue;
                }
                catch (Exception e)
                {}
                return newValue;
            }
        } else {
            final List<String> lines = Lists.newArrayList();
            lines.add(String.format("Could not find $VALUES field for enum: %s", enumType.getName()));
            lines.add(String.format("Runtime Deobf: %s", false));
            lines.add(String.format("Flags: %s", String.format("%16s", Integer.toBinaryString(flags)).replace(' ', '0')));
            lines.add("Fields:");
            var21 = fields;
            var22 = fields.length;

            for(var27 = 0; var27 < var22; ++var27) {
                field = var21[var27];
                String mods = String.format("%16s", Integer.toBinaryString(field.getModifiers())).replace(' ', '0');
                lines.add(String.format("       %s %s: %s", mods, field.getName(), field.getType().getName()));
            }

            for (String line : lines)
            {
                LogManager.error(EnumHelper.class, line);
            }

            if (test) {
                throw new EnhancedRuntimeException("Could not find $VALUES field for enum: " + enumType.getName()) {
                    protected void printStackTrace(WrappedPrintStream stream) {

                        for (String line : lines)
                        {
                            stream.println(line);
                        }

                    }
                };
            } else {
                return null;
            }
        }
    }

    static {
        commonTypes = new Class[][]{{RecipeBookCategories.class, String.class, ItemStack[].class}};
        setup();

    }
}

