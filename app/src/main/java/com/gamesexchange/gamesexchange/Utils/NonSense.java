package com.gamesexchange.gamesexchange.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonSense {
    /*
    private void makeDistrictIndexFunction()
    {
        String valueText = "case \"القرين\":\n" +
                "            case \"Qareen\":\n" +
                "                districtCount = 377;\n" +
                "                break;\n" +
        ";



    int count = 377;

    Pattern p = Pattern.compile("\"([^\"]*)\"");
    Matcher m = p.matcher(valueText);
        while (m.find()) {
        String wantedText = m.group(1);
        if (!isProbablyArabic(wantedText)) {
            System.out.println("case " + count + ": ");
            System.out.println("return \"" + wantedText + "\"" + ";");
            count++;
        }
    }




}
    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length();) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }


    private void makeDistrictFunction()
    {
        List<String> list = new ArrayList<String>();

        list.add("أبو قير");
        list.add("الأزاريطة");
        list.add("الإبراهيمية");
        list.add("الإسكندرية");
        list.add("الجمرك");
        list.add("الحضرة");
        list.add("الدخيلة");
        list.add("السيوف");
        list.add("الصالحية");
        list.add("الظاهرية");
        list.add("العامرية");
        list.add("العصافرة");
        list.add("العطارين");
        list.add("العوايد");
        list.add("القباري");
        list.add("اللبان");
        list.add("المعمورة");
        list.add("المكس");
        list.add("المنتزه");
        list.add("المندرة");
        list.add("المنشية");
        list.add("النخيل");
        list.add("النزهة");
        list.add("الورديان");
        list.add("باكوس");
        list.add("بحرى والأنفوشى");
        list.add("برج العرب");
        list.add("بولكلي");
        list.add("جليم");
        list.add("جناكليس");
        list.add("رأس التين");
        list.add("رشدي");
        list.add("زيزينيا");
        list.add("سابا باشا");
        list.add("سان ستيفانو");
        list.add("سبورتنج");
        list.add("ستانلي");
        list.add("سموحة");
        list.add("سيدي بشر");
        list.add("سيدي جابر");
        list.add("شاطبي");
        list.add("شدس");
        list.add("طوسون");
        list.add("عجمي");
        list.add("فلمنج");
        list.add("فيكتوريا");
        list.add("كامب شيزار");
        list.add("كرموز");
        list.add("كفر عبدو");
        list.add("كليوباترا");
        list.add("كوم الدكة");
        list.add("لوران");
        list.add("محرّم بيك");
        list.add("محطة الرمل");
        list.add("ميامي");
        list.add("مينا البصل");


        list.add("ابو الريش");
        list.add("ابو سمبل");
        list.add("ادفو");
        list.add("البصيلية");
        list.add("الرديسية");
        list.add("السباعية");
        list.add("دراو");
        list.add("صحارى");
        list.add("كلابشة");
        list.add("كوم امبو");
        list.add("مدينة أسوان");
        list.add("نصر النوبة");



        list.add("أبنوب");
        list.add("أبو تيج");
        list.add("أسيوط الجديدة");
        list.add("البدارى");
        list.add("الغنايم");
        list.add("الفتح");
        list.add("القوصيه");
        list.add("ديروط");
        list.add("ساحل سليم");
        list.add("صدفا");
        list.add("مدينة أسيوط");
        list.add("منفلوط");


        list.add("أبو المطامير");
        list.add("أبو حمص");
        list.add("إدكو");
        list.add("الدلنجات");
        list.add("الرحمانية");
        list.add("المحمودية");
        list.add("النوبارية الجديدة");
        list.add("ايتاي البارود");
        list.add("بدر");
        list.add("حوش عيسى");
        list.add("دمنهور");
        list.add("رشيد");
        list.add("شبراخيت");
        list.add("كفر الدوار");
        list.add("كوم حمادة");
        list.add("وادي النطرون");


        list.add("إهناسيا");
        list.add("الفشن");
        list.add("الواسطى");
        list.add("ببا");
        list.add("بني سويف الجديدة");
        list.add("سمسطا");
        list.add("مدينة بني سويف");
        list.add("ناصر");


        list.add("الأميرية");
        list.add("البساتين");
        list.add("التبين");
        list.add("التجمع الثالث");
        list.add("الجمالية");
        list.add("الحلمية");
        list.add("الحلمية الجديدة");
        list.add("الخليفة");
        list.add("الدراسة");
        list.add("الدرب الأحمر");
        list.add("الروضة");
        list.add("الزاوية الحمراء");
        list.add("الزمالك");
        list.add("الزيتون");
        list.add("الساحل");
        list.add("السيدة زينب");
        list.add("الشرابية");
        list.add("العاشر من رمضان");
        list.add("العاصمة الإدارية الجديدة");
        list.add("العباسية");
        list.add("العبور");
        list.add("العتبة");
        list.add("القاهرة - أخرى");
        list.add("القاهرة الجديدة -التجمع الأول");
        list.add("القاهرة الجديدة - أخرى");
        list.add("القاهرة الجديدة - الأندلس");
        list.add("القاهرة الجديدة - التجمع الخامس");
        list.add("القاهرة الجديدة - لوتس");
        list.add("القاهرة الجديدة - مدينة الرحاب");
        list.add("القطامية");
        list.add("الماظة");
        list.add("المرج");
        list.add("المطرية");
        list.add("المعادي");
        list.add("المعصره");
        list.add("المقطم");
        list.add("المنيرة الجديدة");
        list.add("المنيل");
        list.add("الموسكي");
        list.add("النزهة");
        list.add("الوايلي");
        list.add("باب الشعرية");
        list.add("بولاق");
        list.add("بيت الوطن");
        list.add("جاردن سيتي");
        list.add("حدائق القبة");
        list.add("حدائق حلوان");
        list.add("حلوان");
        list.add("دار السلام");
        list.add("رمسيس و امتداد رمسيس");
        list.add("روض الفرج");
        list.add("زهراء المعادى");
        list.add("شبرا");
        list.add("شيراتون");
        list.add("طرة");
        list.add("عزبة النخل");
        list.add("عين شمس");
        list.add("قصر النيل");
        list.add("مدينة 15 مايو");
        list.add("مدينة السلام");
        list.add("مدينة الشروق");
        list.add("مدينة المستقبل");
        list.add("مدينة بدر");
        list.add("مدينة نصر");
        list.add("مدينتي");
        list.add("مسطرد");
        list.add("مصر الجديدة");
        list.add("مصر القديمة");
        list.add("هليوبوليس الجديدة");
        list.add("وسط القاهرة");


        list.add("أجا");
        list.add("أخطاب");
        list.add("الجمالية");
        list.add("السنبلاوين");
        list.add("المطرية");
        list.add("المنزلة");
        list.add("المنصورة");
        list.add("بلقاس");
        list.add("بني عبيد");
        list.add("تمى الامديد");
        list.add("جمصه");
        list.add("دكرنس");
        list.add("شربين");
        list.add("طلخا");
        list.add("منية النصر");
        list.add("ميت سلسيل");
        list.add("ميت غمر");
        list.add("نبروه");

        list.add("الروضة");
        list.add("الزرقا");
        list.add("السرو");
        list.add("دمياط الجديدة");
        list.add("رأس البر");
        list.add("عزبة البرج");
        list.add("فارسكور");
        list.add("كفر البطيخ");
        list.add("كفر سعد");
        list.add("مدينة دمياط");
        list.add("ميت أبوغالب");


        list.add("أطسا");
        list.add("إبشواي");
        list.add("الفيوم الجديدة");
        list.add("سنورس");
        list.add("طامية");
        list.add("مدينة الفيوم");
        list.add("يوسف الصديق");


        list.add("السنطة");
        list.add("المحلة الكبرى");
        list.add("بسيون");
        list.add("زفتى");
        list.add("سمنود");
        list.add("طنطا");
        list.add("قطور");
        list.add("كفر الزيات");


        list.add("6 أكتوبر");
        list.add("أبو رواش");
        list.add("أرض اللواء");
        list.add("أوسيم");
        list.add("إمبابة");
        list.add("البدرشين");
        list.add("البراجيل");
        list.add("الحرانية");
        list.add("الحوامدية");
        list.add("الدقى");
        list.add("الرماية");
        list.add("الشيخ زايد");
        list.add("الصحفيين");
        list.add("الصف");
        list.add("العجوزة");
        list.add("العزيزية");
        list.add("العمرانية");
        list.add("العياط");
        list.add("القرية الفرعونية");
        list.add("الكيت كات");
        list.add("المنصورية");
        list.add("المنيب");
        list.add("المهندسين");
        list.add("الهرم");
        list.add("الوراق");
        list.add("بشتيل");
        list.add("بني سلامة");
        list.add("بولاق الدكرور");
        list.add("ترسا");
        list.add("جزيرة الدهب وكولدير");
        list.add("حدائق الاهرام");
        list.add("حى الجيزة");
        list.add("دهشور");
        list.add("ساقية مكى");
        list.add("سقارة");
        list.add("سوق الأحد");
        list.add("صفط");
        list.add("غرب سوميد");
        list.add("فيصل");
        list.add("كرداسة");
        list.add("كفر طهرمس");
        list.add("مركز الجيزة");
        list.add("مريوطية");
        list.add("ميت عقبة");
        list.add("ناهيا");
        list.add("وردان");


        list.add("أبوصوير");
        list.add("التل الكبير");
        list.add("القصاصين");
        list.add("القنطرة شرق");
        list.add("القنطرة غرب");
        list.add("فايد");
        list.add("مدينة الإسماعيلية");



        list.add("البرلس");
        list.add("الحامول");
        list.add("الرياض");
        list.add("بلطيم");
        list.add("بيلا");
        list.add("دسوق");
        list.add("سيدى سالم");
        list.add("فوه");
        list.add("قلين");
        list.add("مدينة كفر الشيخ");
        list.add("مطوبس");

        list.add("أرمنت");
        list.add("إسنا");
        list.add("البياضية");
        list.add("الزينية");
        list.add("الطود");
        list.add("القرنه");
        list.add("مدينة الأقصر");


        list.add("الحمام");
        list.add("الساحل الشمالي");
        list.add("السلوم");
        list.add("الضبعة");
        list.add("العلمين");
        list.add("النجيلة");
        list.add("براني");
        list.add("سيوة");
        list.add("مارينا العلمين");
        list.add("مرسى مطروح");




        list.add("أبو قرقاص");
        list.add("العدوة");
        list.add("المنيا الجديدة");
        list.add("بني مزار");
        list.add("دير مواس");
        list.add("سمالوط");
        list.add("مدينة المنيا");
        list.add("مطاي");
        list.add("مغاغة");
        list.add("ملوي");



        list.add("أشمون");
        list.add("الباجور");
        list.add("السادات");
        list.add("الشهداء");
        list.add("بركة السبع");
        list.add("تلا");
        list.add("سرس الليان");
        list.add("شبين الكوم");
        list.add("قويسنا");
        list.add("منوف");
        list.add("ميت العز");




        list.add("الخارجة");
        list.add("الداخلة");
        list.add("الفرافرة");
        list.add("باريس");
        list.add("بلاط");
        list.add("مدينة موط");




        list.add("الحسنة");
        list.add("الشيخ زويد");
        list.add("العريش");
        list.add("بئر العبد");
        list.add("رفح");
        list.add("نخل");



        list.add("حي الجنوب");
        list.add("حي الزهور");
        list.add("حي الشرق");
        list.add("حي الضواحي");
        list.add("حي العرب");
        list.add("حي المناخ");
        list.add("مدينة بورفؤاد");


        list.add("الخانكة");
        list.add("الخصوص");
        list.add("العبور");
        list.add("القناطر الخيرية");
        list.add("بنها");
        list.add("بهتيم");
        list.add("زاويه النجار");
        list.add("شبرا الخيمة");
        list.add("شبين القناطر");
        list.add("طوخ");
        list.add("قليوب");
        list.add("قها");
        list.add("كفر شكر");


        list.add("أبو تشت");
        list.add("الوقف");
        list.add("شنا");
        list.add("فرشوط");
        list.add("قفط");
        list.add("قوص");
        list.add("مدينة قنا");
        list.add("نجع حمادي");
        list.add("نقادة");


        list.add("الغردقة - أخرى");
        list.add("الغردقة - الجونة");
        list.add("القصير");
        list.add("رأس غارب");
        list.add("سفاجا");
        list.add("سهل حشيش");
        list.add("شلاتين");
        list.add("مرسى علم");




        list.add("أبو حماد");
        list.add("أبو كبير");
        list.add("أولاد صقر");
        list.add("الإبراهيمية");
        list.add("الحسينية");
        list.add("الزقازيق");
        list.add("الصالحية الجديدة");
        list.add("العاشر من رمضان");
        list.add("القرين");
        list.add("القنايات");
        list.add("بلبيس");
        list.add("ديرب نجم");
        list.add("فاقوس");
        list.add("كفر صقر");
        list.add("مشتول السوق");
        list.add("منيا القمح");
        list.add("ههيا");



        list.add("أخميم");
        list.add("البلينا");
        list.add("العسيرات");
        list.add("المراغة");
        list.add("المنشاة");
        list.add("جرجا");
        list.add("جهينة");
        list.add("دارالسلام");
        list.add("ساقلتة");
        list.add("طما");
        list.add("طهطا");
        list.add("مدينة سوهاج");




        list.add("أبو رديس");
        list.add("أبو زنيمة");
        list.add("دهب");
        list.add("رأس سدر");
        list.add("راس سدر");
        list.add("سانت كاترين");
        list.add("شرم الشيخ");
        list.add("طابا");
        list.add("طور سيناء");
        list.add("نويبع");


        list.add("العين السخنة");
        list.add("حى الجناين");
        list.add("حي الأربعين");
        list.add("حي السويس");
        list.add("حي عتاقة");
        list.add("فيصل");


        System.out.println("int districtCount;");
        System.out.println(" switch (location) {");

        for (int i = 0;i < list.size(); i++)
        {
            String trimmedLine = list.get(i).trim();
            System.out.println("case \"" +trimmedLine +"\"" + ":");
            System.out.println("case \"" +"dummy" +"\"" + ":");
            System.out.println("districtCount = " + i +";");
            System.out.println("break;");


        }


    }
     */
}
