package phoenix.utils;

import org.apache.http.util.TextUtils;
import phoenix.client.gui.diaryPages.DiaryChapter;

public class DiaryUtils
{
    public static DiaryChapter makeChapter(String... text)
    {
        return new DiaryChapter(text);
    }


    //принимает ключи параграфоф
    public static DiaryChapter makeChapterFromTranslate(String... keys)
    {
        return new DiaryChapter(StringUtils.translateAll(keys));
    }
}
