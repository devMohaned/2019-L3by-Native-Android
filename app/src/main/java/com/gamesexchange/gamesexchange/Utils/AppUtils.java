package com.gamesexchange.gamesexchange.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by bestway on 09/07/2018.
 */

public class AppUtils {

    public static class Validators {
        public static String validateEmail(Context context, String email) {
            if (email.contains("@") && email.contains(".")) {
                return Constants.ERROR_FREE;
            } else {
                return context.getString(R.string.enter_valid_email);
            }
        }


        public static String validateName(Context context, String name) {
            if (name.length() > 1 && name.length() <= 32) {
                return Constants.ERROR_FREE;
            } else {
                return context.getString(R.string.name_is_not_valid);
            }
        }

        public static String validatePassword(Context context, String password) {
            if (password.length() > 7 && !password.contains(" ")) {
                return Constants.ERROR_FREE;
            } else {
                return context.getString(R.string.invalid_password);
            }
        }

        public static String validatePhone(Context context, String phone) {
            if (phone.length() == 11) {
                return Constants.ERROR_FREE;
            } else {
                return context.getString(R.string.invalid_phone);
            }
        }


    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException e) {
            Log.d(activity.toString(), "No Keyboard Found " + e.toString());
        }
    }


    public static String generateUniqueFileName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }


    public static ArrayList<Game> convertFromListToArrayList(List<Game> list) {
        return new ArrayList<>(list);
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkVolleyError(Context context, VolleyError error) {
        if (error instanceof TimeoutError) {
            //This indicates that the request has either time out
            Toast.makeText(context, context.getString(R.string.connection_timed_out), Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            //This indicates that the request has no connection
            Toast.makeText(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            //Error indicating that there was an Authentication Failure while performing the request
            Toast.makeText(context, context.getString(R.string.authentication_failed), Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            //Indicates that the server responded with a error response
            Toast.makeText(context, context.getString(R.string.wrong_response_from_server), Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            //Indicates that there was network error while performing the request
            Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed
            Toast.makeText(context, context.getString(R.string.couldnot_parse_the_data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.something_is_wrong), Toast.LENGTH_SHORT).show();
        }

    }

    public static void sendEmail(Context context, String subject, String body) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"talentsinstudio@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.send_email)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.dont_have_email), Toast.LENGTH_SHORT).show();
        }
    }


    public static void buildAlertDialog(Context context, String title, String message, String subject, String body) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        AlertDialog alertDialog = builder1.setPositiveButton(
                context.getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendEmail(context, subject, body);
                        dialog.cancel();
                    }
                }).setNegativeButton(
                context.getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.green));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.red_hear));


//        AlertDialog alert11 = builder1.create();
//        alert11.show();
    }


    public static class LocationUtils {
        public static String getLocationFromIndex(int locationIndex) {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                switch (locationIndex) {
                    case 0:
                        return "الإسكندرية";
                    case 1:
                        return "أسوان";
                    case 2:
                        return "أسيوط";
                    case 3:
                        return "البحيرة";
                    case 4:
                        return "بني سويف";
                    case 5:
                        return "القاهرة";
                    case 6:
                        return "الدقهلية";
                    case 7:
                        return "دمياط";
                    case 8:
                        return "الفيوم";
                    case 9:
                        return "الغربية";
                    case 10:
                        return "الجيزة";
                    case 11:
                        return "الإسماعيلية";
                    case 12:
                        return "كفر الشيخ";
                    case 13:
                        return "الأقصر";
                    case 14:
                        return "مطروح";
                    case 15:
                        return "المنيا";
                    case 16:
                        return "المنوفية";
                    case 17:
                        return "الوادي الجديد";
                    case 18:
                        return "شمال سيناء";
                    case 19:
                        return "بورسعيد";
                    case 20:
                        return "القليوبية";
                    case 21:
                        return "قنا";
                    case 22:
                        return "البحر الأحمر";
                    case 23:
                        return "الشرقية";
                    case 24:
                        return "سوهاج";
                    case 25:
                        return "جنوب سيناء";
                    case 26:
                        return "السويس";
                    default:
                        // Others
                        return "اخرين";
                }
            } else

            {
                switch (locationIndex) {
                    case 0:
                        return "Alexandria";
                    case 1:
                        return "Aswan";
                    case 2:
                        return "Asyut";
                    case 3:
                        return "Beheira";
                    case 4:
                        return "Beni Suef";
                    case 5:
                        return "Cairo";
                    case 6:
                        return "Dakahlia";
                    case 7:
                        return "Damietta";
                    case 8:
                        return "Fayoum";
                    case 9:
                        return "Gharbia";
                    case 10:
                        return "Giza";
                    case 11:
                        return "Ismailia";
                    case 12:
                        return "Kafr al-Sheikh";
                    case 13:
                        return "Luxor";
                    case 14:
                        return "Matruh";
                    case 15:
                        return "Minya";
                    case 16:
                        return "Monufia";
                    case 17:
                        return "New Valley";
                    case 18:
                        return "North Sinai";
                    case 19:
                        return "Port Said";
                    case 20:
                        return "Qalyubia";
                    case 21:
                        return "Qena";
                    case 22:
                        return "Red Sea";
                    case 23:
                        return "Sharqia";
                    case 24:
                        return "Sohag";
                    case 25:
                        return "South Sinai";
                    case 26:
                        return "Suez";
                    default:
                        // Others
                        return "Others";
                }

            }
        }

        public static int getLocationIndex(String location) {
            int locationCount;
            switch (location) {
                case "الإسكندرية":
                case "Alexandria":
                    locationCount = 0;
                    break;
                case "أسوان":
                case "Aswan":
                    locationCount = 1;
                    break;
                case "أسيوط":
                case "Asyut":
                    locationCount = 2;
                    break;
                case "البحيرة":
                case "Beheira":
                    locationCount = 3;
                    break;
                case "بني سويف":
                case "Beni Suef":
                    locationCount = 4;
                    break;
                case "القاهرة":
                case "Cairo":
                    locationCount = 5;
                    break;
                case "الدقهلية":
                case "Dakahlia":
                    locationCount = 6;
                    break;
                case "دمياط":
                case "Damietta":
                    locationCount = 7;
                    break;
                case "الفيوم":
                case "Fayoum":
                    locationCount = 8;
                    break;
                case "الغربية":
                case "Gharbia":
                    locationCount = 9;
                    break;
                case "الجيزة":
                case "Giza":
                    locationCount = 10;
                    break;
                case "الإسماعيلية":
                case "Ismailia":
                    locationCount = 11;
                    break;
                case "كفر الشيخ":
                case "Kafr al-Sheikh":
                    locationCount = 12;
                    break;
                case "الأقصر":
                case "Luxor":
                    locationCount = 13;
                    break;
                case "مطروح":
                case "Matruh":
                    locationCount = 14;
                    break;
                case "المنيا":
                case "Minya":
                    locationCount = 15;
                    break;
                case "المنوفية":
                case "Monufia":
                    locationCount = 16;
                    break;
                case "الوادي الجديد":
                case "New Valley":
                    locationCount = 17;
                    break;
                case "شمال سيناء":
                case "North Sinai":
                    locationCount = 18;
                    break;
                case "بورسعيد":
                case "Port Said":
                    locationCount = 19;
                    break;
                case "القليوبية":
                case "Qalyubia":
                    locationCount = 20;
                    break;
                case "قنا":
                case "Qena":
                    locationCount = 21;
                    break;
                case "البحر الأحمر":
                case "Red Sea":
                    locationCount = 22;
                    break;
                case "الشرقية":
                case "Sharqia":
                    locationCount = 23;
                    break;
                case "سوهاج":
                case "Sohag":
                    locationCount = 24;
                    break;
                case "جنوب سيناء":
                case "South Sinai":
                    locationCount = 25;
                    break;
                case "السويس":
                case "Suez":
                    locationCount = 26;
                    break;
                default:
                    // Others
                    locationCount = 100;
                    break;
            }

            return locationCount;
        }


        public static String getDistrictFromIndex(int districtIndex) {
            if (Locale.getDefault().getLanguage().equals("ar")) {
                switch (districtIndex) {
                    case 0:
                        return "أبو قير";
                    case 1:
                        return "الأزاريطة";
                    case 2:
                        return "الإبراهيمية";
                    case 3:
                        return "الإسكندرية";
                    case 4:
                        return "الجمرك";
                    case 5:
                        return "الحضرة";
                    case 6:
                        return "الدخيلة";
                    case 7:
                        return "السيوف";
                    case 8:
                        return "الصالحية";
                    case 9:
                        return "الظاهرية";
                    case 10:
                        return "العامرية";
                    case 11:
                        return "العصافرة";
                    case 12:
                        return "العطارين";
                    case 13:
                        return "العوايد";
                    case 14:
                        return "القباري";
                    case 15:
                        return "اللبان";
                    case 16:
                        return "المعمورة";
                    case 17:
                        return "المكس";
                    case 18:
                        return "المنتزه";
                    case 19:
                        return "المندرة";
                    case 20:
                        return "المنشية";
                    case 21:
                        return "النخيل";
                    case 23:
                        return "الورديان";
                    case 24:
                        return "باكوس";
                    case 25:
                        return "بحرى والأنفوشى";
                    case 26:
                        return "برج العرب";
                    case 27:
                        return "بولكلي";
                    case 28:
                        return "جليم";
                    case 29:
                        return "جناكليس";
                    case 30:
                        return "رأس التين";
                    case 31:
                        return "رشدي";
                    case 32:
                        return "زيزينيا";
                    case 33:
                        return "سابا باشا";
                    case 34:
                        return "سان ستيفانو";
                    case 35:
                        return "سبورتنج";
                    case 36:
                        return "ستانلي";
                    case 37:
                        return "سموحة";
                    case 38:
                        return "سيدي بشر";
                    case 39:
                        return "سيدي جابر";
                    case 40:
                        return "شاطبي";
                    case 41:
                        return "شدس";
                    case 42:
                        return "طوسون";
                    case 43:
                        return "عجمي";
                    case 44:
                        return "فلمنج";
                    case 45:
                        return "فيكتوريا";
                    case 46:
                        return "كامب شيزار";
                    case 47:
                        return "كرموز";
                    case 48:
                        return "كفر عبدو";
                    case 49:
                        return "كليوباترا";
                    case 50:
                        return "كوم الدكة";
                    case 51:
                        return "لوران";
                    case 52:
                        return "محرّم بيك";
                    case 53:
                        return "محطة الرمل";
                    case 54:
                        return "ميامي";
                    case 55:
                        return "مينا البصل";
                    case 56:
                        return "ابو الريش";
                    case 57:
                        return "ابو سمبل";
                    case 58:
                        return "ادفو";
                    case 59:
                        return "البصيلية";
                    case 60:
                        return "الرديسية";
                    case 61:
                        return "السباعية";
                    case 62:
                        return "دراو";
                    case 63:
                        return "صحارى";
                    case 64:
                        return "كلابشة";
                    case 65:
                        return "كوم امبو";
                    case 66:
                        return "مدينة أسوان";
                    case 67:
                        return "نصر النوبة";
                    case 68:
                        return "أبنوب";
                    case 69:
                        return "أبو تيج";
                    case 70:
                        return "أسيوط الجديدة";
                    case 71:
                        return "البدارى";
                    case 72:
                        return "الغنايم";
                    case 73:
                        return "الفتح";
                    case 74:
                        return "القوصيه";
                    case 75:
                        return "ديروط";
                    case 76:
                        return "ساحل سليم";
                    case 77:
                        return "صدفا";
                    case 78:
                        return "مدينة أسيوط";
                    case 79:
                        return "منفلوط";
                    case 80:
                        return "أبو المطامير";
                    case 81:
                        return "أبو حمص";
                    case 82:
                        return "إدكو";
                    case 83:
                        return "الدلنجات";
                    case 84:
                        return "الرحمانية";
                    case 85:
                        return "المحمودية";
                    case 86:
                        return "النوبارية الجديدة";
                    case 87:
                        return "ايتاي البارود";
                    case 88:
                        return "بدر";
                    case 89:
                        return "حوش عيسى";
                    case 90:
                        return "دمنهور";
                    case 91:
                        return "رشيد";
                    case 92:
                        return "شبراخيت";
                    case 93:
                        return "كفر الدوار";
                    case 94:
                        return "كوم حمادة";
                    case 95:
                        return "وادي النطرون";
                    case 96:
                        return "إهناسيا";
                    case 97:
                        return "الفشن";
                    case 98:
                        return "الواسطى";
                    case 99:
                        return "ببا";
                    case 100:
                        return "بني سويف الجديدة";
                    case 101:
                        return "سمسطا";
                    case 102:
                        return "مدينة بني سويف";
                    case 103:
                        return "ناصر";
                    case 104:
                        return "الأميرية";
                    case 105:
                        return "البساتين";
                    case 106:
                        return "التبين";
                    case 107:
                        return "التجمع الثالث";
                    case 108:
                        return "الجمالية";
                    case 109:
                        return "الحلمية";
                    case 110:
                        return "الحلمية الجديدة";
                    case 111:
                        return "الخليفة";
                    case 112:
                        return "الدراسة";
                    case 113:
                        return "الدرب الأحمر";
                    case 114:
                        return "الروضة";
                    case 115:
                        return "الزاوية الحمراء";
                    case 116:
                        return "الزمالك";
                    case 117:
                        return "الزيتون";
                    case 118:
                        return "الساحل";
                    case 119:
                        return "السيدة زينب";
                    case 120:
                        return "الشرابية";
                    case 121:
                        return "العاشر من رمضان";
                    case 122:
                        return "العاصمة الإدارية الجديدة";
                    case 123:
                        return "العباسية";
                    case 124:
                        return "العبور";
                    case 125:
                        return "العتبة";
                    case 126:
                        return "القاهرة - أخرى";
                    case 127:
                        return "القاهرة الجديدة -التجمع الأول";
                    case 128:
                        return "القاهرة الجديدة - أخرى";
                    case 129:
                        return "القاهرة الجديدة - الأندلس";
                    case 130:
                        return "القاهرة الجديدة - التجمع الخامس";
                    case 131:
                        return "القاهرة الجديدة - لوتس";
                    case 132:
                        return "القاهرة الجديدة - مدينة الرحاب";
                    case 133:
                        return "القطامية";
                    case 134:
                        return "الماظة";
                    case 135:
                        return "المرج";
                    case 137:
                        return "المعادي";
                    case 138:
                        return "المعصره";
                    case 139:
                        return "المقطم";
                    case 140:
                        return "المنيرة الجديدة";
                    case 141:
                        return "المنيل";
                    case 142:
                        return "الموسكي";
                    case 143:
                        return "النزهة";
                    case 144:
                        return "الوايلي";
                    case 145:
                        return "باب الشعرية";
                    case 146:
                        return "بولاق";
                    case 147:
                        return "بيت الوطن";
                    case 148:
                        return "جاردن سيتي";
                    case 149:
                        return "حدائق القبة";
                    case 150:
                        return "حدائق حلوان";
                    case 151:
                        return "حلوان";
                    case 152:
                        return "دار السلام";
                    case 153:
                        return "رمسيس و امتداد رمسيس";
                    case 154:
                        return "روض الفرج";
                    case 155:
                        return "زهراء المعادى";
                    case 156:
                        return "شبرا";
                    case 157:
                        return "شيراتون";
                    case 158:
                        return "طرة";
                    case 159:
                        return "عزبة النخل";
                    case 160:
                        return "عين شمس";
                    case 161:
                        return "قصر النيل";
                    case 162:
                        return "مدينة 15 مايو";
                    case 163:
                        return "مدينة السلام";
                    case 164:
                        return "مدينة الشروق";
                    case 165:
                        return "مدينة المستقبل";
                    case 166:
                        return "مدينة بدر";
                    case 167:
                        return "مدينة نصر";
                    case 168:
                        return "مدينتي";
                    case 169:
                        return "مسطرد";
                    case 170:
                        return "مصر الجديدة";
                    case 171:
                        return "مصر القديمة";
                    case 172:
                        return "هليوبوليس الجديدة";
                    case 173:
                        return "وسط القاهرة";
                    case 174:
                        return "أجا";
                    case 175:
                        return "أخطاب";
                    case 177:
                        return "السنبلاوين";
                    case 178:
                        return "المطرية";
                    case 179:
                        return "المنزلة";
                    case 180:
                        return "المنصورة";
                    case 181:
                        return "بلقاس";
                    case 182:
                        return "بني عبيد";
                    case 183:
                        return "تمى الامديد";
                    case 184:
                        return "جمصه";
                    case 185:
                        return "دكرنس";
                    case 186:
                        return "شربين";
                    case 187:
                        return "طلخا";
                    case 188:
                        return "منية النصر";
                    case 189:
                        return "ميت سلسيل";
                    case 190:
                        return "ميت غمر";
                    case 191:
                        return "نبروه";
                    case 193:
                        return "الزرقا";
                    case 194:
                        return "السرو";
                    case 195:
                        return "دمياط الجديدة";
                    case 196:
                        return "رأس البر";
                    case 197:
                        return "عزبة البرج";
                    case 198:
                        return "فارسكور";
                    case 199:
                        return "كفر البطيخ";
                    case 200:
                        return "كفر سعد";
                    case 201:
                        return "مدينة دمياط";
                    case 202:
                        return "ميت أبوغالب";
                    case 203:
                        return "أطسا";
                    case 204:
                        return "إبشواي";
                    case 205:
                        return "الفيوم الجديدة";
                    case 206:
                        return "سنورس";
                    case 207:
                        return "طامية";
                    case 208:
                        return "مدينة الفيوم";
                    case 209:
                        return "يوسف الصديق";
                    case 210:
                        return "السنطة";
                    case 211:
                        return "المحلة الكبرى";
                    case 212:
                        return "بسيون";
                    case 213:
                        return "زفتى";
                    case 214:
                        return "سمنود";
                    case 215:
                        return "طنطا";
                    case 216:
                        return "قطور";
                    case 217:
                        return "كفر الزيات";
                    case 218:
                        return "6 أكتوبر";
                    case 219:
                        return "أبو رواش";
                    case 220:
                        return "أرض اللواء";
                    case 221:
                        return "أوسيم";
                    case 222:
                        return "إمبابة";
                    case 223:
                        return "البدرشين";
                    case 224:
                        return "البراجيل";
                    case 225:
                        return "الحرانية";
                    case 226:
                        return "الحوامدية";
                    case 227:
                        return "الدقى";
                    case 228:
                        return "الرماية";
                    case 229:
                        return "الشيخ زايد";
                    case 230:
                        return "الصحفيين";
                    case 231:
                        return "الصف";
                    case 232:
                        return "العجوزة";
                    case 233:
                        return "العزيزية";
                    case 234:
                        return "العمرانية";
                    case 235:
                        return "العياط";
                    case 236:
                        return "القرية الفرعونية";
                    case 237:
                        return "الكيت كات";
                    case 238:
                        return "المنصورية";
                    case 239:
                        return "المنيب";
                    case 240:
                        return "المهندسين";
                    case 241:
                        return "الهرم";
                    case 242:
                        return "الوراق";
                    case 243:
                        return "بشتيل";
                    case 244:
                        return "بني سلامة";
                    case 245:
                        return "بولاق الدكرور";
                    case 246:
                        return "ترسا";
                    case 247:
                        return "جزيرة الدهب وكولدير";
                    case 248:
                        return "حدائق الاهرام";
                    case 249:
                        return "حى الجيزة";
                    case 250:
                        return "دهشور";
                    case 251:
                        return "ساقية مكى";
                    case 252:
                        return "سقارة";
                    case 253:
                        return "سوق الأحد";
                    case 254:
                        return "صفط";
                    case 255:
                        return "غرب سوميد";
                    case 256:
                        return "فيصل";
                    case 257:
                        return "كرداسة";
                    case 258:
                        return "كفر طهرمس";
                    case 259:
                        return "مركز الجيزة";
                    case 260:
                        return "مريوطية";
                    case 261:
                        return "ميت عقبة";
                    case 262:
                        return "ناهيا";
                    case 263:
                        return "وردان";
                    case 264:
                        return "أبوصوير";
                    case 265:
                        return "التل الكبير";
                    case 266:
                        return "القصاصين";
                    case 267:
                        return "القنطرة شرق";
                    case 268:
                        return "القنطرة غرب";
                    case 269:
                        return "فايد";
                    case 270:
                        return "مدينة الإسماعيلية";
                    case 271:
                        return "البرلس";
                    case 272:
                        return "الحامول";
                    case 273:
                        return "الرياض";
                    case 274:
                        return "بلطيم";
                    case 275:
                        return "بيلا";
                    case 276:
                        return "دسوق";
                    case 277:
                        return "سيدى سالم";
                    case 278:
                        return "فوه";
                    case 279:
                        return "قلين";
                    case 280:
                        return "مدينة كفر الشيخ";
                    case 281:
                        return "مطوبس";
                    case 282:
                        return "أرمنت";
                    case 283:
                        return "إسنا";
                    case 284:
                        return "البياضية";
                    case 285:
                        return "الزينية";
                    case 286:
                        return "الطود";
                    case 287:
                        return "القرنه";
                    case 288:
                        return "مدينة الأقصر";
                    case 289:
                        return "الحمام";
                    case 290:
                        return "الساحل الشمالي";
                    case 291:
                        return "السلوم";
                    case 292:
                        return "الضبعة";
                    case 293:
                        return "العلمين";
                    case 294:
                        return "النجيلة";
                    case 295:
                        return "براني";
                    case 296:
                        return "سيوة";
                    case 297:
                        return "مارينا العلمين";
                    case 298:
                        return "مرسى مطروح";
                    case 299:
                        return "أبو قرقاص";
                    case 300:
                        return "العدوة";
                    case 301:
                        return "المنيا الجديدة";
                    case 302:
                        return "بني مزار";
                    case 303:
                        return "دير مواس";
                    case 304:
                        return "سمالوط";
                    case 305:
                        return "مدينة المنيا";
                    case 306:
                        return "مطاي";
                    case 307:
                        return "مغاغة";
                    case 308:
                        return "ملوي";
                    case 309:
                        return "أشمون";
                    case 310:
                        return "الباجور";
                    case 311:
                        return "السادات";
                    case 312:
                        return "الشهداء";
                    case 313:
                        return "بركة السبع";
                    case 314:
                        return "تلا";
                    case 315:
                        return "سرس الليان";
                    case 316:
                        return "شبين الكوم";
                    case 317:
                        return "قويسنا";
                    case 318:
                        return "منوف";
                    case 319:
                        return "ميت العز";
                    case 320:
                        return "الخارجة";
                    case 321:
                        return "الداخلة";
                    case 322:
                        return "الفرافرة";
                    case 323:
                        return "باريس";
                    case 324:
                        return "بلاط";
                    case 325:
                        return "مدينة موط";
                    case 326:
                        return "الحسنة";
                    case 327:
                        return "الشيخ زويد";
                    case 328:
                        return "العريش";
                    case 329:
                        return "بئر العبد";
                    case 330:
                        return "رفح";
                    case 331:
                        return "نخل";
                    case 332:
                        return "حي الجنوب";
                    case 333:
                        return "حي الزهور";
                    case 334:
                        return "حي الشرق";
                    case 335:
                        return "حي الضواحي";
                    case 336:
                        return "حي العرب";
                    case 337:
                        return "حي المناخ";
                    case 338:
                        return "مدينة بورفؤاد";
                    case 339:
                        return "الخانكة";
                    case 340:
                        return "الخصوص";
                    case 342:
                        return "القناطر الخيرية";
                    case 343:
                        return "بنها";
                    case 344:
                        return "بهتيم";
                    case 345:
                        return "زاويه النجار";
                    case 346:
                        return "شبرا الخيمة";
                    case 347:
                        return "شبين القناطر";
                    case 348:
                        return "طوخ";
                    case 349:
                        return "قليوب";
                    case 350:
                        return "قها";
                    case 351:
                        return "كفر شكر";
                    case 352:
                        return "أبو تشت";
                    case 353:
                        return "الوقف";
                    case 354:
                        return "شنا";
                    case 355:
                        return "فرشوط";
                    case 356:
                        return "قفط";
                    case 357:
                        return "قوص";
                    case 358:
                        return "مدينة قنا";
                    case 359:
                        return "نجع حمادي";
                    case 360:
                        return "نقادة";
                    case 361:
                        return "الغردقة - أخرى";
                    case 362:
                        return "الغردقة - الجونة";
                    case 363:
                        return "القصير";
                    case 364:
                        return "رأس غارب";
                    case 365:
                        return "سفاجا";
                    case 366:
                        return "سهل حشيش";
                    case 367:
                        return "شلاتين";
                    case 368:
                        return "مرسى علم";
                    case 369:
                        return "أبو حماد";
                    case 370:
                        return "أبو كبير";
                    case 371:
                        return "أولاد صقر";
                    case 373:
                        return "الحسينية";
                    case 374:
                        return "الزقازيق";
                    case 375:
                        return "الصالحية الجديدة";
                    case 377:
                        return "القرين";
                    case 378:
                        return "القنايات";
                    case 379:
                        return "بلبيس";
                    case 380:
                        return "ديرب نجم";
                    case 381:
                        return "فاقوس";
                    case 382:
                        return "كفر صقر";
                    case 383:
                        return "مشتول السوق";
                    case 384:
                        return "منيا القمح";
                    case 385:
                        return "ههيا";
                    case 386:
                        return "أخميم";
                    case 387:
                        return "البلينا";
                    case 388:
                        return "العسيرات";
                    case 389:
                        return "المراغة";
                    case 390:
                        return "المنشاة";
                    case 391:
                        return "جرجا";
                    case 392:
                        return "جهينة";
                    case 393:
                        return "دارالسلام";
                    case 394:
                        return "ساقلتة";
                    case 395:
                        return "طما";
                    case 396:
                        return "طهطا";
                    case 397:
                        return "مدينة سوهاج";
                    case 398:
                        return "أبو رديس";
                    case 399:
                        return "أبو زنيمة";
                    case 400:
                        return "دهب";
                    case 401:
                        return "رأس سدر";
                    case 402:
                        return "راس سدر";
                    case 403:
                        return "سانت كاترين";
                    case 404:
                        return "شرم الشيخ";
                    case 405:
                        return "طابا";
                    case 406:
                        return "طور سيناء";
                    case 407:
                        return "نويبع";
                    case 408:
                        return "العين السخنة";
                    case 409:
                        return "حى الجناين";
                    case 410:
                        return "حي الأربعين";
                    case 411:
                        return "حي السويس";
                    case 412:
                        return "حي عتاقة";
                    case 413:
                        return "هليوبوليس";
                    default:
                        return "اخرين";
                }
            } else {
                switch (districtIndex) {
                    case 0:
                        return "Abu Qir";
                    case 1:
                        return "Azarita";
                    case 2:
                        return "Ibrahimiyyah";
                    case 3:
                        return "Alexandria";
                    case 4:
                        return "Gomrok";
                    case 5:
                        return "Al Hadrah";
                    case 6:
                        return "Dekheila";
                    case 7:
                        return "Seyouf";
                    case 8:
                        return "Salehia";
                    case 9:
                        return "Dhahria";
                    case 10:
                        return "Amreya";
                    case 11:
                        return "Asafra";
                    case 12:
                        return "Attarin";
                    case 13:
                        return "Awayed";
                    case 14:
                        return "Kabbary";
                    case 15:
                        return "Labban";
                    case 16:
                        return "Maamoura";
                    case 17:
                        return "El Max";
                    case 18:
                        return "Montazah";
                    case 19:
                        return "Mandara";
                    case 20:
                        return "Manshiyya";
                    case 21:
                        return "Nakheel";
                    case 23:
                        return "Wardian";
                    case 24:
                        return "Bacchus";
                    case 25:
                        return "Bahray - Anfoshy";
                    case 26:
                        return "Borg al-Arab";
                    case 27:
                        return "Bolkly";
                    case 28:
                        return "Glim";
                    case 29:
                        return "Gianaclis";
                    case 30:
                        return "Ras El Tin";
                    case 31:
                        return "Roushdy";
                    case 32:
                        return "Zezenia";
                    case 33:
                        return "Saba Pasha";
                    case 34:
                        return "San Stefano";
                    case 35:
                        return "Sporting";
                    case 36:
                        return "Stanley";
                    case 37:
                        return "Smoha";
                    case 38:
                        return "Sidi Beshr";
                    case 39:
                        return "Sidi Gaber";
                    case 40:
                        return "Shatby";
                    case 41:
                        return "Schutz";
                    case 42:
                        return "Tosson";
                    case 43:
                        return "Agami";
                    case 44:
                        return "Fleming";
                    case 45:
                        return "Victoria";
                    case 46:
                        return "Camp Caesar";
                    case 47:
                        return "Karmous";
                    case 48:
                        return "Kafr Abdo";
                    case 49:
                        return "Cleopatra";
                    case 50:
                        return "Koum al-Dikka";
                    case 51:
                        return "Laurent";
                    case 52:
                        return "Moharam Bik";
                    case 53:
                        return "Raml Station";
                    case 54:
                        return "Miami";
                    case 55:
                        return "Mina al-Basal";
                    case 56:
                        return "Abou al-Reish";
                    case 57:
                        return "Abou Simbel";
                    case 58:
                        return "Edfu";
                    case 59:
                        return "Basiliah";
                    case 60:
                        return "Rdesiah";
                    case 61:
                        return "Sebaeiah";
                    case 62:
                        return "Daraw";
                    case 63:
                        return "Sahara";
                    case 64:
                        return "Kalabsha";
                    case 65:
                        return "Kom Ombo";
                    case 66:
                        return "Aswan City";
                    case 67:
                        return "Nasr al-Noba";
                    case 68:
                        return "Abnub";
                    case 69:
                        return "Abu Teeg";
                    case 70:
                        return "New Assiut";
                    case 71:
                        return "Badari";
                    case 72:
                        return "Ghanayem";
                    case 73:
                        return "Fateh";
                    case 74:
                        return "Qusiya";
                    case 75:
                        return "Dairut";
                    case 76:
                        return "Sahel Selim";
                    case 77:
                        return "Sedfa";
                    case 78:
                        return "Asyut City";
                    case 79:
                        return "Manfalut";
                    case 80:
                        return "Abuu al-Matamer";
                    case 81:
                        return "Abou Homs";
                    case 82:
                        return "Edko";
                    case 83:
                        return "Delengat";
                    case 84:
                        return "Rahmaniya";
                    case 85:
                        return "Mahmoudiyah";
                    case 86:
                        return "Nubariyah";
                    case 87:
                        return "Etay al-Barud";
                    case 88:
                        return "Badr";
                    case 89:
                        return "Hosh Essa";
                    case 90:
                        return "Damanhour";
                    case 91:
                        return "Rashid";
                    case 92:
                        return "Shubrakhit";
                    case 93:
                        return "Kafr al-Dawwar";
                    case 94:
                        return "Kom Hamadah";
                    case 95:
                        return "Wadi al-Natrun";
                    case 96:
                        return "Ehnasia";
                    case 97:
                        return "Al Feshn";
                    case 98:
                        return "Al Wasty";
                    case 99:
                        return "Beba";
                    case 100:
                        return "New Beni Suef";
                    case 101:
                        return "Samasta";
                    case 102:
                        return "Beni Suef City";
                    case 103:
                        return "Nasser";
                    case 104:
                        return "Al Amiriyyah";
                    case 105:
                        return "Basateen";
                    case 106:
                        return "Tebeen";
                    case 107:
                        return "The 3rd District";
                    case 108:
                        return "Gamaleya";
                    case 109:
                        return "Helmeya";
                    case 110:
                        return "Helmeya al-Gadeda";
                    case 111:
                        return "Khalifa";
                    case 112:
                        return "Darrasa";
                    case 113:
                        return "Darb al-Ahmar";
                    case 114:
                        return "Rodah";
                    case 115:
                        return "Zawya al-Hamra";
                    case 116:
                        return "Zamalek";
                    case 117:
                        return "Zaytoun";
                    case 118:
                        return "Sahel";
                    case 119:
                        return "Sayeda Zeinab";
                    case 120:
                        return "Sharabeya";
                    case 121:
                        return "10th Ramadan City";
                    case 122:
                        return "New Capital City";
                    case 123:
                        return "Abasiya";
                    case 124:
                        return "El Ubour";
                    case 125:
                        return "Ataba";
                    case 126:
                        return "Cairo - Other";
                    case 127:
                        return "New Cairo - First Settlement";
                    case 128:
                        return "New Cairo - Other";
                    case 129:
                        return "New Cairo - Andalous";
                    case 130:
                        return "New Cairo - Fifth Settlement";
                    case 131:
                        return "New Cairo - Lotus";
                    case 132:
                        return "New Cairo - Rehab City";
                    case 133:
                        return "Katameya";
                    case 134:
                        return "Almazah";
                    case 135:
                        return "Marg";

                    case 137:
                        return "Maadi";
                    case 138:
                        return "Ma'sara";
                    case 139:
                        return "Mokattam";
                    case 140:
                        return "Mounira al-Gadeda";
                    case 141:
                        return "Al Manial";
                    case 142:
                        return "Moski";
                    case 143:
                        return "Nozha";
                    case 144:
                        return "Waili";
                    case 145:
                        return "Bab al-Shereia";
                    case 146:
                        return "Boulaq";
                    case 147:
                        return "Bait El Watan";
                    case 148:
                        return "Garden City";
                    case 149:
                        return "Hadayek al-Kobba";
                    case 150:
                        return "Hadayek Helwan";
                    case 151:
                        return "Helwan";
                    case 152:
                        return "Dar al-Salaam";
                    case 153:
                        return "Ramses + Ramses Extension";
                    case 154:
                        return "Rod al-Farag";
                    case 155:
                        return "Zahraa Al Maadi";
                    case 156:
                        return "Shubra";
                    case 157:
                        return "Sheraton";
                    case 158:
                        return "Tura";
                    case 159:
                        return "Izbat an Nakhl";
                    case 160:
                        return "Ain Shams";
                    case 161:
                        return "Qasr al-Nil";
                    case 162:
                        return "15 May City";
                    case 163:
                        return "Salam City";
                    case 164:
                        return "Shorouk City";
                    case 165:
                        return "Future City";
                    case 166:
                        return "Badr City";
                    case 167:
                        return "Nasr City";
                    case 168:
                        return "Madinaty";
                    case 169:
                        return "Musturad";
                    case 170:
                        return "New Cairo";
                    case 171:
                        return "Masr al-Kadema";
                    case 172:
                        return "New Heliopolis";
                    case 173:
                        return "Downtown Cairo";
                    case 174:
                        return "Aga";
                    case 175:
                        return "Akhtab";
                    case 177:
                        return "Sinbillawain";
                    case 178:
                        return "Matareya";
                    case 179:
                        return "Manzala";
                    case 180:
                        return "Mansura";
                    case 181:
                        return "Belqas";
                    case 182:
                        return "Bani Ubayd";
                    case 183:
                        return "Tama al-Amdeed";
                    case 184:
                        return "Gamasa";
                    case 185:
                        return "Dikirnis";
                    case 186:
                        return "Shirbin";
                    case 187:
                        return "Talkha";
                    case 188:
                        return "Minat al-Nasr";
                    case 189:
                        return "Mit Slseel";
                    case 190:
                        return "Mit Ghamr";
                    case 191:
                        return "Nabaruh";
                    case 193:
                        return "Zarqa";
                    case 194:
                        return "Saro";
                    case 195:
                        return "New Damietta";
                    case 196:
                        return "Ras al-Bar";
                    case 197:
                        return "Ezbet al-Burj";
                    case 198:
                        return "Fareskour";
                    case 199:
                        return "Kafr al-Bateekh";
                    case 200:
                        return "Kafr Saad";
                    case 201:
                        return "Damietta City";
                    case 202:
                        return "Mit Abughalb";
                    case 203:
                        return "Atssa";
                    case 204:
                        return "Ibshway";
                    case 205:
                        return "New Fayoum";
                    case 206:
                        return "Sinnuras";
                    case 207:
                        return "Tamiya";
                    case 208:
                        return "Fayoum City";
                    case 209:
                        return "Yusuf al-Sadiq";
                    case 210:
                        return "Santa";
                    case 211:
                        return "Mahalla al-Kobra";
                    case 212:
                        return "Basyoun";
                    case 213:
                        return "Zefta";
                    case 214:
                        return "Samanoud";
                    case 215:
                        return "Tanta";
                    case 216:
                        return "Qutour";
                    case 217:
                        return "Kafr al-Zayat";
                    case 218:
                        return "6th of October";
                    case 219:
                        return "Abu Rawash";
                    case 220:
                        return "Ard El Lewa";
                    case 221:
                        return "Oseem";
                    case 222:
                        return "Imbaba";
                    case 223:
                        return "Badrasheen";
                    case 224:
                        return "Baragil";
                    case 225:
                        return "Haraneya";
                    case 226:
                        return "Hawamdeya";
                    case 227:
                        return "Dokki";
                    case 228:
                        return "Remaia";
                    case 229:
                        return "Sheikh Zayed";
                    case 230:
                        return "Sahafieen";
                    case 231:
                        return "Saf";
                    case 232:
                        return "Agouza";
                    case 233:
                        return "Azizia";
                    case 234:
                        return "Omrania";
                    case 235:
                        return "El Ayyat";
                    case 236:
                        return "Pharaonic Village";
                    case 237:
                        return "Kit Kat";
                    case 238:
                        return "Mansuriyya";
                    case 239:
                        return "Moneeb";
                    case 240:
                        return "Mohandessin";
                    case 241:
                        return "Haram";
                    case 242:
                        return "Warraq";
                    case 243:
                        return "Bashtil";
                    case 244:
                        return "Bani Salamah";
                    case 245:
                        return "Boulaq Dakrour";
                    case 246:
                        return "Tersa";
                    case 247:
                        return "Dahab Island and Coldair";
                    case 248:
                        return "Hadayek al-Ahram";
                    case 249:
                        return "Giza District";
                    case 250:
                        return "Dahshur";
                    case 251:
                        return "Sakyet Mekky";
                    case 252:
                        return "Saqqara";
                    case 253:
                        return "Souk al-Ahad";
                    case 254:
                        return "Saft";
                    case 255:
                        return "West Somid";
                    case 256:
                        return "Faisal";
                    case 257:
                        return "Kerdasa";
                    case 258:
                        return "Kafr Tohormos";
                    case 259:
                        return "Markaz al-Giza";
                    case 260:
                        return "Maryotaya";
                    case 261:
                        return "Mit Oqba";
                    case 262:
                        return "Nahia";
                    case 263:
                        return "Wardan";
                    case 264:
                        return "Abu Swear";
                    case 265:
                        return "Tal al-Kebeer";
                    case 266:
                        return "Qassaseen";
                    case 267:
                        return "Kantara East";
                    case 268:
                        return "Kantara West";
                    case 269:
                        return "Fayed";
                    case 270:
                        return "Ismailia City";
                    case 271:
                        return "Brolos";
                    case 272:
                        return "Hamoul";
                    case 273:
                        return "Riyadh";
                    case 274:
                        return "Baltim";
                    case 275:
                        return "Bella";
                    case 276:
                        return "Desouk";
                    case 277:
                        return "Sidi Salem";
                    case 278:
                        return "Fouh";
                    case 279:
                        return "Qaleen";
                    case 280:
                        return "Kafr al-Sheikh City";
                    case 281:
                        return "Motobas";
                    case 282:
                        return "Armant";
                    case 283:
                        return "Isna";
                    case 284:
                        return "Bayadeya";
                    case 285:
                        return "Zinnia";
                    case 286:
                        return "Tod";
                    case 287:
                        return "Qurna";
                    case 288:
                        return "Luxor City";
                    case 289:
                        return "Hammam";
                    case 290:
                        return "North Coast";
                    case 291:
                        return "Salloum";
                    case 292:
                        return "Dabaa";
                    case 293:
                        return "Alamein";
                    case 294:
                        return "Nagela";
                    case 295:
                        return "Barany";
                    case 296:
                        return "Siwa";
                    case 297:
                        return "Marina El Alamein";
                    case 298:
                        return "Marsa Matrouh";
                    case 299:
                        return "Abu Qurqas";
                    case 300:
                        return "Adwa";
                    case 301:
                        return "New Minya";
                    case 302:
                        return "Beni Mazar";
                    case 303:
                        return "Deir Mawas";
                    case 304:
                        return "Samalut";
                    case 305:
                        return "Minya City";
                    case 306:
                        return "Matay";
                    case 307:
                        return "Maghagha";
                    case 308:
                        return "Malawi";
                    case 309:
                        return "Ashmon";
                    case 310:
                        return "Bagour";
                    case 311:
                        return "Sadat";
                    case 312:
                        return "Shohadaa";
                    case 313:
                        return "Berket al-Sabaa";
                    case 314:
                        return "Tala";
                    case 315:
                        return "Sers al-Lyan";
                    case 316:
                        return "Shebin al-Koum";
                    case 317:
                        return "Quesna";
                    case 318:
                        return "Menouf";
                    case 319:
                        return "Mit El Ezz";
                    case 320:
                        return "Kharga";
                    case 321:
                        return "Dakhla";
                    case 322:
                        return "Farafra";
                    case 323:
                        return "Paris";
                    case 324:
                        return "Balat";
                    case 325:
                        return "Mut";
                    case 326:
                        return "Hasana";
                    case 327:
                        return "Sheikh Zoweyd";
                    case 328:
                        return "Arish";
                    case 329:
                        return "Bir al-Abed";
                    case 330:
                        return "Rafah";
                    case 331:
                        return "Nakhl";
                    case 332:
                        return "Ganoub District";
                    case 333:
                        return "Zohour District";
                    case 334:
                        return "Sharq District";
                    case 335:
                        return "Dawahy District";
                    case 336:
                        return "Arab District";
                    case 337:
                        return "Manakh District";
                    case 338:
                        return "Port Fouad";
                    case 339:
                        return "Khanka";
                    case 340:
                        return "Khosous";
                    case 342:
                        return "Qanater al-Khairia";
                    case 343:
                        return "Banha";
                    case 344:
                        return "Bahtim";
                    case 345:
                        return "Zawya El-Naggar";
                    case 346:
                        return "Shubra al-Khaimah";
                    case 347:
                        return "Shebin al-Qanater";
                    case 348:
                        return "Tookh";
                    case 349:
                        return "Qalyub";
                    case 350:
                        return "Qaha";
                    case 351:
                        return "Kafr Shukr";
                    case 352:
                        return "Abu Tisht";
                    case 353:
                        return "Wakf";
                    case 354:
                        return "Shna";
                    case 355:
                        return "Farshout";
                    case 356:
                        return "Keft";
                    case 357:
                        return "Quos";
                    case 358:
                        return "Qena City";
                    case 359:
                        return "Nag Hammadi";
                    case 360:
                        return "Nakada";
                    case 361:
                        return "Hurghada - Other";
                    case 362:
                        return "Hurghada - Gouna";
                    case 363:
                        return "Qusair";
                    case 364:
                        return "Ras Gharib";
                    case 365:
                        return "Safaga";
                    case 366:
                        return "Sahl Hasheesh";
                    case 367:
                        return "Shalateen";
                    case 368:
                        return "Marsa Alam";
                    case 369:
                        return "Abu Hammad";
                    case 370:
                        return "Abu Kabir";
                    case 371:
                        return "Awlad Saqr";
                    case 373:
                        return "Husseiniya";
                    case 374:
                        return "Zagazig";
                    case 375:
                        return "New Salhia";
                    case 377:
                        return "Qareen";
                    case 378:
                        return "Alqnayat";
                    case 379:
                        return "Bilbeis";
                    case 380:
                        return "Deyerb Negm";
                    case 381:
                        return "Faqous";
                    case 382:
                        return "Kafr Saqr";
                    case 383:
                        return "Mashtool al-Souk";
                    case 384:
                        return "Minya al-Qamh";
                    case 385:
                        return "Hihya";
                    case 386:
                        return "Akhmim";
                    case 387:
                        return "Baliana";
                    case 388:
                        return "Alasirat";
                    case 389:
                        return "Maragha";
                    case 390:
                        return "Monsha'a";
                    case 391:
                        return "Girga";
                    case 392:
                        return "Juhaynah";
                    case 393:
                        return "Dar es-Salam";
                    case 394:
                        return "Sakaltah";
                    case 395:
                        return "Tama";
                    case 396:
                        return "Tahta";
                    case 397:
                        return "Sohag City";
                    case 398:
                        return "Abu Rudeis";
                    case 399:
                        return "Abu Zenimah";
                    case 400:
                        return "Dahab";
                    case 401:
                        return " Ras Sedr";
                    case 402:
                        return "Ras Sidr";
                    case 403:
                        return "St. Catherine";
                    case 404:
                        return "Sharm al-Sheikh";
                    case 405:
                        return "Taba";
                    case 406:
                        return "Tor Sinai";
                    case 407:
                        return "Nuweiba";
                    case 408:
                        return "Ain Sukhna";
                    case 409:
                        return "Ganayen";
                    case 410:
                        return "Arbaeen";
                    case 411:
                        return "Suez District";
                    case 412:
                        return "Attaka";
                    case 413:
                        return "Heliopolis";
                    default:
                        return "Others";
                }
            }
        }

        //                        22-136-176-192-341-372-376
        //  2-114-121-124
        public static int getDistrictIndex(String districtLocation) {
            int districtCount;
            switch (districtLocation) {
                case "أبو قير":
                case "Abu Qir":
                    districtCount = 0;
                    break;
                case "الأزاريطة":
                case "Azarita":
                    districtCount = 1;
                    break;
                case "الإبراهيمية": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Al Ibrahimiyyah":
                case "Ibrahemyah":
                    districtCount = 2;
                    break;
                case "الإسكندرية":
                case "Alexandria":
                    districtCount = 3;
                    break;
                case "الجمرك":
                case "Gomrok":
                    districtCount = 4;
                    break;
                case "الحضرة":
                case "Al Hadrah":
                    districtCount = 5;
                    break;
                case "الدخيلة":
                case "Dekheila":
                    districtCount = 6;
                    break;
                case "السيوف":
                case "Seyouf":
                    districtCount = 7;
                    break;
                case "الصالحية":
                case "Salehia":
                    districtCount = 8;
                    break;
                case "الظاهرية":
                case "Dhahria":
                    districtCount = 9;
                    break;
                case "العامرية":
                case "Amreya":
                    districtCount = 10;
                    break;
                case "العصافرة":
                case "Asafra":
                    districtCount = 11;
                    break;
                case "العطارين":
                case "Attarin":
                    districtCount = 12;
                    break;
                case "العوايد":
                case "Awayed":
                    districtCount = 13;
                    break;
                case "القباري":
                case "Kabbary":
                    districtCount = 14;
                    break;
                case "اللبان":
                case "Labban":
                    districtCount = 15;
                    break;
                case "المعمورة":
                case "Maamoura":
                    districtCount = 16;
                    break;
                case "المكس":
                case "El Max":
                    districtCount = 17;
                    break;
                case "المنتزه":
                case "Montazah":
                    districtCount = 18;
                    break;
                case "المندرة":
                case "Mandara":
                    districtCount = 19;
                    break;
                case "المنشية":
                case "Manshiyya":
                    districtCount = 20;
                    break;
                case "النخيل":
                case "Nakheel":
                    districtCount = 21;
                    break;
            /*case "النزهة":
            case "Nozha":
                districtCount = 22;
                break;*/
                case "الورديان":
                case "Wardian":
                    districtCount = 23;
                    break;
                case "باكوس":
                case "Bacchus":
                    districtCount = 24;
                    break;
                case "بحرى والأنفوشى":
                case "Bahray - Anfoshy":
                    districtCount = 25;
                    break;
                case "برج العرب":
                case "Borg al-Arab":
                    districtCount = 26;
                    break;
                case "بولكلي":
                case "Bolkly":
                    districtCount = 27;
                    break;
                case "جليم":
                case "Glim":
                    districtCount = 28;
                    break;
                case "جناكليس":
                case "Gianaclis":
                    districtCount = 29;
                    break;
                case "رأس التين":
                case "Ras El Tin":
                    districtCount = 30;
                    break;
                case "رشدي":
                case "Roushdy":
                    districtCount = 31;
                    break;
                case "زيزينيا":
                case "Zezenia":
                    districtCount = 32;
                    break;
                case "سابا باشا":
                case "Saba Pasha":
                    districtCount = 33;
                    break;
                case "سان ستيفانو":
                case "San Stefano":
                    districtCount = 34;
                    break;
                case "سبورتنج":
                case "Sporting":
                    districtCount = 35;
                    break;
                case "ستانلي":
                case "Stanley":
                    districtCount = 36;
                    break;
                case "سموحة":
                case "Smoha":
                    districtCount = 37;
                    break;
                case "سيدي بشر":
                case "Sidi Beshr":
                    districtCount = 38;
                    break;
                case "سيدي جابر":
                case "Sidi Gaber":
                    districtCount = 39;
                    break;
                case "شاطبي":
                case "Shatby":
                    districtCount = 40;
                    break;
                case "شدس":
                case "Schutz":
                    districtCount = 41;
                    break;
                case "طوسون":
                case "Tosson":
                    districtCount = 42;
                    break;
                case "عجمي":
                case "Agami":
                    districtCount = 43;
                    break;
                case "فلمنج":
                case "Fleming":
                    districtCount = 44;
                    break;
                case "فيكتوريا":
                case "Victoria":
                    districtCount = 45;
                    break;
                case "كامب شيزار":
                case "Camp Caesar":
                    districtCount = 46;
                    break;
                case "كرموز":
                case "Karmous":
                    districtCount = 47;
                    break;
                case "كفر عبدو":
                case "Kafr Abdo":
                    districtCount = 48;
                    break;
                case "كليوباترا":
                case "Cleopatra":
                    districtCount = 49;
                    break;
                case "كوم الدكة":
                case "Koum al-Dikka":
                    districtCount = 50;
                    break;
                case "لوران":
                case "Laurent":
                    districtCount = 51;
                    break;
                case "محرّم بيك":
                case "Moharam Bik":
                    districtCount = 52;
                    break;
                case "محطة الرمل":
                case "Raml Station":
                    districtCount = 53;
                    break;
                case "ميامي":
                case "Miami":
                    districtCount = 54;
                    break;
                case "مينا البصل":
                case "Mina al-Basal":
                    districtCount = 55;
                    break;
                case "ابو الريش":
                case "Abou al-Reish":
                    districtCount = 56;
                    break;
                case "ابو سمبل":
                case "Abou Simbel":
                    districtCount = 57;
                    break;
                case "ادفو":
                case "Edfu":
                    districtCount = 58;
                    break;
                case "البصيلية":
                case "Basiliah":
                    districtCount = 59;
                    break;
                case "الرديسية":
                case "Rdesiah":
                    districtCount = 60;
                    break;
                case "السباعية":
                case "Sebaeiah":
                    districtCount = 61;
                    break;
                case "دراو":
                case "Daraw":
                    districtCount = 62;
                    break;
                case "صحارى":
                case "Sahara":
                    districtCount = 63;
                    break;
                case "كلابشة":
                case "Kalabsha":
                    districtCount = 64;
                    break;
                case "كوم امبو":
                case "Kom Ombo":
                    districtCount = 65;
                    break;
                case "مدينة أسوان":
                case "Aswan City":
                    districtCount = 66;
                    break;
                case "نصر النوبة":
                case "Nasr al-Noba":
                    districtCount = 67;
                    break;
                case "أبنوب":
                case "Abnub":
                    districtCount = 68;
                    break;
                case "أبو تيج":
                case "Abu Teeg":
                    districtCount = 69;
                    break;
                case "أسيوط الجديدة":
                case "New Assiut":
                    districtCount = 70;
                    break;
                case "البدارى":
                case "Badari":
                    districtCount = 71;
                    break;
                case "الغنايم":
                case "Ghanayem":
                    districtCount = 72;
                    break;
                case "الفتح":
                case "Fateh":
                    districtCount = 73;
                    break;
                case "القوصيه":
                case "Qusiya":
                    districtCount = 74;
                    break;
                case "ديروط":
                case "Dairut":
                    districtCount = 75;
                    break;
                case "ساحل سليم":
                case "Sahel Selim":
                    districtCount = 76;
                    break;
                case "صدفا":
                case "Sedfa":
                    districtCount = 77;
                    break;
                case "مدينة أسيوط":
                case "Asyut City":
                    districtCount = 78;
                    break;
                case "منفلوط":
                case "Manfalut":
                    districtCount = 79;
                    break;
                case "أبو المطامير":
                case "Abuu al-Matamer":
                    districtCount = 80;
                    break;
                case "أبو حمص":
                case "Abou Homs":
                    districtCount = 81;
                    break;
                case "إدكو":
                case "Edko":
                    districtCount = 82;
                    break;
                case "الدلنجات":
                case "Delengat":
                    districtCount = 83;
                    break;
                case "الرحمانية":
                case "Rahmaniya":
                    districtCount = 84;
                    break;
                case "المحمودية":
                case "Mahmoudiyah":
                    districtCount = 85;
                    break;
                case "النوبارية الجديدة":
                case "Nubariyah":
                    districtCount = 86;
                    break;
                case "ايتاي البارود":
                case "Etay al-Barud":
                    districtCount = 87;
                    break;
                case "بدر":
                case "Badr":
                    districtCount = 88;
                    break;
                case "حوش عيسى":
                case "Hosh Essa":
                    districtCount = 89;
                    break;
                case "دمنهور":
                case "Damanhour":
                    districtCount = 90;
                    break;
                case "رشيد":
                case "Rashid":
                    districtCount = 91;
                    break;
                case "شبراخيت":
                case "Shubrakhit":
                    districtCount = 92;
                    break;
                case "كفر الدوار":
                case "Kafr al-Dawwar":
                    districtCount = 93;
                    break;
                case "كوم حمادة":
                case "Kom Hamadah":
                    districtCount = 94;
                    break;
                case "وادي النطرون":
                case "Wadi al-Natrun":
                    districtCount = 95;
                    break;
                case "إهناسيا":
                case "Ehnasia":
                    districtCount = 96;
                    break;
                case "الفشن":
                case "Al Feshn":
                    districtCount = 97;
                    break;
                case "الواسطى":
                case "Al Wasty":
                    districtCount = 98;
                    break;
                case "ببا":
                case "Beba":
                    districtCount = 99;
                    break;
                case "بني سويف الجديدة":
                case "New Beni Suef":
                    districtCount = 100;
                    break;
                case "سمسطا":
                case "Samasta":
                    districtCount = 101;
                    break;
                case "مدينة بني سويف":
                case "Beni Suef City":
                    districtCount = 102;
                    break;
                case "ناصر":
                case "Nasser":
                    districtCount = 103;
                    break;
                case "الأميرية":
                case "Al Amiriyyah":
                    districtCount = 104;
                    break;
                case "البساتين":
                case "Basateen":
                    districtCount = 105;
                    break;
                case "التبين":
                case "Tebeen":
                    districtCount = 106;
                    break;
                case "التجمع الثالث":
                case "The 3rd District":
                    districtCount = 107;
                    break;
                case "الجمالية":
                case "Gamaleya":
                    districtCount = 108;
                    break;
                case "الحلمية":
                case "Helmeya":
                    districtCount = 109;
                    break;
                case "الحلمية الجديدة":
                case "Helmeya al-Gadeda":
                    districtCount = 110;
                    break;
                case "الخليفة":
                case "Khalifa":
                    districtCount = 111;
                    break;
                case "الدراسة":
                case "Darrasa":
                    districtCount = 112;
                    break;
                case "الدرب الأحمر":
                case "Darb al-Ahmar":
                    districtCount = 113;
                    break;
                case "الروضة": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Roda":
                case "Rodah":
                    districtCount = 114;
                    break;
                case "الزاوية الحمراء":
                case "Zawya al-Hamra":
                    districtCount = 115;
                    break;
                case "الزمالك":
                case "Zamalek":
                    districtCount = 116;
                    break;
                case "الزيتون":
                case "Zaytoun":
                    districtCount = 117;
                    break;
                case "الساحل":
                case "Sahel":
                    districtCount = 118;
                    break;
                case "السيدة زينب":
                case "Sayeda Zeinab":
                    districtCount = 119;
                    break;
                case "الشرابية":
                case "Sharabeya":
                    districtCount = 120;
                    break;
                case "العاشر من رمضان": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "10th Ramadan City":
                case "10th of Ramadan":
                    districtCount = 121;
                    break;
                case "العاصمة الإدارية الجديدة":
                case "New Capital City":
                    districtCount = 122;
                    break;
                case "العباسية":
                case "Abasiya":
                    districtCount = 123;
                    break;
                case "العبور": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Obour City":
                case "El Ubour":
                    districtCount = 124;
                    break;
                case "العتبة":
                case "Ataba":
                    districtCount = 125;
                    break;
                case "القاهرة - أخرى":
                case "Cairo - Other":
                    districtCount = 126;
                    break;
                case "القاهرة الجديدة -التجمع الأول":
                case "New Cairo - First Settlement":
                    districtCount = 127;
                    break;
                case "القاهرة الجديدة - أخرى":
                case "New Cairo - Other":
                    districtCount = 128;
                    break;
                case "القاهرة الجديدة - الأندلس":
                case "New Cairo - Andalous":
                    districtCount = 129;
                    break;
                case "القاهرة الجديدة - التجمع الخامس":
                case "New Cairo - Fifth Settlement":
                    districtCount = 130;
                    break;
                case "القاهرة الجديدة - لوتس":
                case "New Cairo - Lotus":
                    districtCount = 131;
                    break;
                case "القاهرة الجديدة - مدينة الرحاب":
                case "New Cairo - Rehab City":
                    districtCount = 132;
                    break;
                case "القطامية":
                case "Katameya":
                    districtCount = 133;
                    break;
                case "الماظة":
                case "Almazah":
                    districtCount = 134;
                    break;
                case "المرج":
                case "Marg":
                    districtCount = 135;
                    break;
            /*case "المطرية":
            case "Matareya":
                districtCount = 136;
                break;*/
                case "المعادي":
                case "Maadi":
                    districtCount = 137;
                    break;
                case "المعصره":
                case "Ma'sara":
                    districtCount = 138;
                    break;
                case "المقطم":
                case "Mokattam":
                    districtCount = 139;
                    break;
                case "المنيرة الجديدة":
                case "Mounira al-Gadeda":
                    districtCount = 140;
                    break;
                case "المنيل":
                case "Al Manial":
                    districtCount = 141;
                    break;
                case "الموسكي":
                case "Moski":
                    districtCount = 142;
                    break;
                case "النزهة":
                case "Nozha":
                    districtCount = 143;
                    break;
                case "الوايلي":
                case "Waili":
                    districtCount = 144;
                    break;
                case "باب الشعرية":
                case "Bab al-Shereia":
                    districtCount = 145;
                    break;
                case "بولاق":
                case "Boulaq":
                    districtCount = 146;
                    break;
                case "بيت الوطن":
                case "Bait El Watan":
                    districtCount = 147;
                    break;
                case "جاردن سيتي":
                case "Garden City":
                    districtCount = 148;
                    break;
                case "حدائق القبة":
                case "Hadayek al-Kobba":
                    districtCount = 149;
                    break;
                case "حدائق حلوان":
                case "Hadayek Helwan":
                    districtCount = 150;
                    break;
                case "حلوان":
                case "Helwan":
                    districtCount = 151;
                    break;
                case "دار السلام":
                case "Dar al-Salaam":
                    districtCount = 152;
                    break;
                case "رمسيس و امتداد رمسيس":
                case "Ramses + Ramses Extension":
                    districtCount = 153;
                    break;
                case "روض الفرج":
                case "Rod al-Farag":
                    districtCount = 154;
                    break;
                case "زهراء المعادى":
                case "Zahraa Al Maadi":
                    districtCount = 155;
                    break;
                case "شبرا":
                case "Shubra":
                    districtCount = 156;
                    break;
                case "شيراتون":
                case "Sheraton":
                    districtCount = 157;
                    break;
                case "طرة":
                case "Tura":
                    districtCount = 158;
                    break;
                case "عزبة النخل":
                case "Izbat an Nakhl":
                    districtCount = 159;
                    break;
                case "عين شمس":
                case "Ain Shams":
                    districtCount = 160;
                    break;
                case "قصر النيل":
                case "Qasr al-Nil":
                    districtCount = 161;
                    break;
                case "مدينة 15 مايو":
                case "15 May City":
                    districtCount = 162;
                    break;
                case "مدينة السلام":
                case "Salam City":
                    districtCount = 163;
                    break;
                case "مدينة الشروق":
                case "Shorouk City":
                    districtCount = 164;
                    break;
                case "مدينة المستقبل":
                case "Future City":
                    districtCount = 165;
                    break;
                case "مدينة بدر":
                case "Badr City":
                    districtCount = 166;
                    break;
                case "مدينة نصر":
                case "Nasr City":
                    districtCount = 167;
                    break;
                case "مدينتي":
                case "Madinaty":
                    districtCount = 168;
                    break;
                case "مسطرد":
                case "Musturad":
                    districtCount = 169;
                    break;
                case "مصر الجديدة":
                case "New Cairo":// Should be new egypt
                    districtCount = 170;
                    break;
                case "مصر القديمة":
                case "Masr al-Kadema":
                    districtCount = 171;
                    break;
                case "هليوبوليس الجديدة":
                case "New Heliopolis":
                    districtCount = 172;
                    break;
                case "وسط القاهرة":
                case "Downtown Cairo":
                    districtCount = 173;
                    break;
                case "أجا":
                case "Aga":
                    districtCount = 174;
                    break;
                case "أخطاب":
                case "Akhtab":
                    districtCount = 175;
                    break;
          /*  case "الجمالية":
            case "Gamaleya":
                districtCount = 176;
                break;*/
                case "السنبلاوين":
                case "Sinbillawain":
                    districtCount = 177;
                    break;
                case "المطرية":
                case "Matareya":
                    districtCount = 178;
                    break;
                case "المنزلة":
                case "Manzala":
                    districtCount = 179;
                    break;
                case "المنصورة":
                case "Mansura":
                    districtCount = 180;
                    break;
                case "بلقاس":
                case "Belqas":
                    districtCount = 181;
                    break;
                case "بني عبيد":
                case "Bani Ubayd":
                    districtCount = 182;
                    break;
                case "تمى الامديد":
                case "Tama al-Amdeed":
                    districtCount = 183;
                    break;
                case "جمصه":
                case "Gamasa":
                    districtCount = 184;
                    break;
                case "دكرنس":
                case "Dikirnis":
                    districtCount = 185;
                    break;
                case "شربين":
                case "Shirbin":
                    districtCount = 186;
                    break;
                case "طلخا":
                case "Talkha":
                    districtCount = 187;
                    break;
                case "منية النصر":
                case "Minat al-Nasr":
                    districtCount = 188;
                    break;
                case "ميت سلسيل":
                case "Mit Slseel":
                    districtCount = 189;
                    break;
                case "ميت غمر":
                case "Mit Ghamr":
                    districtCount = 190;
                    break;
                case "نبروه":
                case "Nabaruh":
                    districtCount = 191;
                    break;
            /*case "الروضة":
            case "Rodah":
                districtCount = 192;
                break;*/
                case "الزرقا":
                case "Zarqa":
                    districtCount = 193;
                    break;
                case "السرو":
                case "Saro":
                    districtCount = 194;
                    break;
                case "دمياط الجديدة":
                case "New Damietta":
                    districtCount = 195;
                    break;
                case "رأس البر":
                case "Ras al-Bar":
                    districtCount = 196;
                    break;
                case "عزبة البرج":
                case "Ezbet al-Burj":
                    districtCount = 197;
                    break;
                case "فارسكور":
                case "Fareskour":
                    districtCount = 198;
                    break;
                case "كفر البطيخ":
                case "Kafr al-Bateekh":
                    districtCount = 199;
                    break;
                case "كفر سعد":
                case "Kafr Saad":
                    districtCount = 200;
                    break;
                case "مدينة دمياط":
                case "Damietta City":
                    districtCount = 201;
                    break;
                case "ميت أبوغالب":
                case "Mit Abughalb":
                    districtCount = 202;
                    break;
                case "أطسا":
                case "Atssa":
                    districtCount = 203;
                    break;
                case "إبشواي":
                case "Ibshway":
                    districtCount = 204;
                    break;
                case "الفيوم الجديدة":
                case "New Fayoum":
                    districtCount = 205;
                    break;
                case "سنورس":
                case "Sinnuras":
                    districtCount = 206;
                    break;
                case "طامية":
                case "Tamiya":
                    districtCount = 207;
                    break;
                case "مدينة الفيوم":
                case "Fayoum City":
                    districtCount = 208;
                    break;
                case "يوسف الصديق":
                case "Yusuf al-Sadiq":
                    districtCount = 209;
                    break;
                case "السنطة":
                case "Santa":
                    districtCount = 210;
                    break;
                case "المحلة الكبرى":
                case "Mahalla al-Kobra":
                    districtCount = 211;
                    break;
                case "بسيون":
                case "Basyoun":
                    districtCount = 212;
                    break;
                case "زفتى":
                case "Zefta":
                    districtCount = 213;
                    break;
                case "سمنود":
                case "Samanoud":
                    districtCount = 214;
                    break;
                case "طنطا":
                case "Tanta":
                    districtCount = 215;
                    break;
                case "قطور":
                case "Qutour":
                    districtCount = 216;
                    break;
                case "كفر الزيات":
                case "Kafr al-Zayat":
                    districtCount = 217;
                    break;
                case "6 أكتوبر":
                case "6th of October":
                    districtCount = 218;
                    break;
                case "أبو رواش":
                case "Abu Rawash":
                    districtCount = 219;
                    break;
                case "أرض اللواء":
                case "Ard El Lewa":
                    districtCount = 220;
                    break;
                case "أوسيم":
                case "Oseem":
                    districtCount = 221;
                    break;
                case "إمبابة":
                case "Imbaba":
                    districtCount = 222;
                    break;
                case "البدرشين":
                case "Badrasheen":
                    districtCount = 223;
                    break;
                case "البراجيل":
                case "Baragil":
                    districtCount = 224;
                    break;
                case "الحرانية":
                case "Haraneya":
                    districtCount = 225;
                    break;
                case "الحوامدية":
                case "Hawamdeya":
                    districtCount = 226;
                    break;
                case "الدقى":
                case "Dokki":
                    districtCount = 227;
                    break;
                case "الرماية":
                case "Remaia":
                    districtCount = 228;
                    break;
                case "الشيخ زايد":
                case "Sheikh Zayed":
                    districtCount = 229;
                    break;
                case "الصحفيين":
                case "Sahafieen":
                    districtCount = 230;
                    break;
                case "الصف":
                case "Saf":
                    districtCount = 231;
                    break;
                case "العجوزة":
                case "Agouza":
                    districtCount = 232;
                    break;
                case "العزيزية":
                case "Azizia":
                    districtCount = 233;
                    break;
                case "العمرانية":
                case "Omrania":
                    districtCount = 234;
                    break;
                case "العياط":
                case "El Ayyat":
                    districtCount = 235;
                    break;
                case "القرية الفرعونية":
                case "Pharaonic Village":
                    districtCount = 236;
                    break;
                case "الكيت كات":
                case "Kit Kat":
                    districtCount = 237;
                    break;
                case "المنصورية":
                case "Mansuriyya":
                    districtCount = 238;
                    break;
                case "المنيب":
                case "Moneeb":
                    districtCount = 239;
                    break;
                case "المهندسين":
                case "Mohandessin":
                    districtCount = 240;
                    break;
                case "الهرم":
                case "Haram":
                    districtCount = 241;
                    break;
                case "الوراق":
                case "Warraq":
                    districtCount = 242;
                    break;
                case "بشتيل":
                case "Bashtil":
                    districtCount = 243;
                    break;
                case "بني سلامة":
                case "Bani Salamah":
                    districtCount = 244;
                    break;
                case "بولاق الدكرور":
                case "Boulaq Dakrour":
                    districtCount = 245;
                    break;
                case "ترسا":
                case "Tersa":
                    districtCount = 246;
                    break;
                case "جزيرة الدهب وكولدير":
                case "Dahab Island and Coldair":
                    districtCount = 247;
                    break;
                case "حدائق الاهرام":
                case "Hadayek al-Ahram":
                    districtCount = 248;
                    break;
                case "حى الجيزة":
                case "Giza District":
                    districtCount = 249;
                    break;
                case "دهشور":
                case "Dahshur":
                    districtCount = 250;
                    break;
                case "ساقية مكى":
                case "Sakyet Mekky":
                    districtCount = 251;
                    break;
                case "سقارة":
                case "Saqqara":
                    districtCount = 252;
                    break;
                case "سوق الأحد":
                case "Souk al-Ahad":
                    districtCount = 253;
                    break;
                case "صفط":
                case "Saft":
                    districtCount = 254;
                    break;
                case "غرب سوميد":
                case "West Somid":
                    districtCount = 255;
                    break;
                case "فيصل":
                case "Faisal":
                    districtCount = 256;
                    break;
                case "كرداسة":
                case "Kerdasa":
                    districtCount = 257;
                    break;
                case "كفر طهرمس":
                case "Kafr Tohormos":
                    districtCount = 258;
                    break;
                case "مركز الجيزة":
                case "Markaz al-Giza":
                    districtCount = 259;
                    break;
                case "مريوطية":
                case "Maryotaya":
                    districtCount = 260;
                    break;
                case "ميت عقبة":
                case "Mit Oqba":
                    districtCount = 261;
                    break;
                case "ناهيا":
                case "Nahia":
                    districtCount = 262;
                    break;
                case "وردان":
                case "Wardan":
                    districtCount = 263;
                    break;
                case "أبوصوير":
                case "Abu Swear":
                    districtCount = 264;
                    break;
                case "التل الكبير":
                case "Tal al-Kebeer":
                    districtCount = 265;
                    break;
                case "القصاصين":
                case "Qassaseen":
                    districtCount = 266;
                    break;
                case "القنطرة شرق":
                case "Kantara East":
                    districtCount = 267;
                    break;
                case "القنطرة غرب":
                case "Kantara West":
                    districtCount = 268;
                    break;
                case "فايد":
                case "Fayed":
                    districtCount = 269;
                    break;
                case "مدينة الإسماعيلية":
                case "Ismailia City":
                    districtCount = 270;
                    break;
                case "البرلس":
                case "Brolos":
                    districtCount = 271;
                    break;
                case "الحامول":
                case "Hamoul":
                    districtCount = 272;
                    break;
                case "الرياض":
                case "Riyadh":
                    districtCount = 273;
                    break;
                case "بلطيم":
                case "Baltim":
                    districtCount = 274;
                    break;
                case "بيلا":
                case "Bella":
                    districtCount = 275;
                    break;
                case "دسوق":
                case "Desouk":
                    districtCount = 276;
                    break;
                case "سيدى سالم":
                case "Sidi Salem":
                    districtCount = 277;
                    break;
                case "فوه":
                case "Fouh":
                    districtCount = 278;
                    break;
                case "قلين":
                case "Qaleen":
                    districtCount = 279;
                    break;
                case "مدينة كفر الشيخ":
                case "Kafr al-Sheikh City":
                    districtCount = 280;
                    break;
                case "مطوبس":
                case "Motobas":
                    districtCount = 281;
                    break;
                case "أرمنت":
                case "Armant":
                    districtCount = 282;
                    break;
                case "إسنا":
                case "Isna":
                    districtCount = 283;
                    break;
                case "البياضية":
                case "Bayadeya":
                    districtCount = 284;
                    break;
                case "الزينية":
                case "Zinnia":
                    districtCount = 285;
                    break;
                case "الطود":
                case "Tod":
                    districtCount = 286;
                    break;
                case "القرنه":
                case "Qurna":
                    districtCount = 287;
                    break;
                case "مدينة الأقصر":
                case "Luxor City":
                    districtCount = 288;
                    break;
                case "الحمام":
                case "Hammam":
                    districtCount = 289;
                    break;
                case "الساحل الشمالي":
                case "North Coast":
                    districtCount = 290;
                    break;
                case "السلوم":
                case "Salloum":
                    districtCount = 291;
                    break;
                case "الضبعة":
                case "Dabaa":
                    districtCount = 292;
                    break;
                case "العلمين":
                case "Alamein":
                    districtCount = 293;
                    break;
                case "النجيلة":
                case "Nagela":
                    districtCount = 294;
                    break;
                case "براني":
                case "Barany":
                    districtCount = 295;
                    break;
                case "سيوة":
                case "Siwa":
                    districtCount = 296;
                    break;
                case "مارينا العلمين":
                case "Marina El Alamein":
                    districtCount = 297;
                    break;
                case "مرسى مطروح":
                case "Marsa Matrouh":
                    districtCount = 298;
                    break;
                case "أبو قرقاص":
                case "Abu Qurqas":
                    districtCount = 299;
                    break;
                case "العدوة":
                case "Adwa":
                    districtCount = 300;
                    break;
                case "المنيا الجديدة":
                case "New Minya":
                    districtCount = 301;
                    break;
                case "بني مزار":
                case "Beni Mazar":
                    districtCount = 302;
                    break;
                case "دير مواس":
                case "Deir Mawas":
                    districtCount = 303;
                    break;
                case "سمالوط":
                case "Samalut":
                    districtCount = 304;
                    break;
                case "مدينة المنيا":
                case "Minya City":
                    districtCount = 305;
                    break;
                case "مطاي":
                case "Matay":
                    districtCount = 306;
                    break;
                case "مغاغة":
                case "Maghagha":
                    districtCount = 307;
                    break;
                case "ملوي":
                case "Malawi":
                    districtCount = 308;
                    break;
                case "أشمون":
                case "Ashmon":
                    districtCount = 309;
                    break;
                case "الباجور":
                case "Bagour":
                    districtCount = 310;
                    break;
                case "السادات":
                case "Sadat":
                    districtCount = 311;
                    break;
                case "الشهداء":
                case "Shohadaa":
                    districtCount = 312;
                    break;
                case "بركة السبع":
                case "Berket al-Sabaa":
                    districtCount = 313;
                    break;
                case "تلا":
                case "Tala":
                    districtCount = 314;
                    break;
                case "سرس الليان":
                case "Sers al-Lyan":
                    districtCount = 315;
                    break;
                case "شبين الكوم":
                case "Shebin al-Koum":
                    districtCount = 316;
                    break;
                case "قويسنا":
                case "Quesna":
                    districtCount = 317;
                    break;
                case "منوف":
                case "Menouf":
                    districtCount = 318;
                    break;
                case "ميت العز":
                case "Mit El Ezz":
                    districtCount = 319;
                    break;
                case "الخارجة":
                case "Kharga":
                    districtCount = 320;
                    break;
                case "الداخلة":
                case "Dakhla":
                    districtCount = 321;
                    break;
                case "الفرافرة":
                case "Farafra":
                    districtCount = 322;
                    break;
                case "باريس":
                case "Paris":
                    districtCount = 323;
                    break;
                case "بلاط":
                case "Balat":
                    districtCount = 324;
                    break;
                case "مدينة موط":
                case "Mut":
                    districtCount = 325;
                    break;
                case "الحسنة":
                case "Hasana":
                    districtCount = 326;
                    break;
                case "الشيخ زويد":
                case "Sheikh Zoweyd":
                    districtCount = 327;
                    break;
                case "العريش":
                case "Arish":
                    districtCount = 328;
                    break;
                case "بئر العبد":
                case "Bir al-Abed":
                    districtCount = 329;
                    break;
                case "رفح":
                case "Rafah":
                    districtCount = 330;
                    break;
                case "نخل":
                case "Nakhl":
                    districtCount = 331;
                    break;
                case "حي الجنوب":
                case "Ganoub District":
                    districtCount = 332;
                    break;
                case "حي الزهور":
                case "Zohour District":
                    districtCount = 333;
                    break;
                case "حي الشرق":
                case "Sharq District":
                    districtCount = 334;
                    break;
                case "حي الضواحي":
                case "Dawahy District":
                    districtCount = 335;
                    break;
                case "حي العرب":
                case "Arab District":
                    districtCount = 336;
                    break;
                case "حي المناخ":
                case "Manakh District":
                    districtCount = 337;
                    break;
                case "مدينة بورفؤاد":
                case "Port Fouad":
                    districtCount = 338;
                    break;
                case "الخانكة":
                case "Khanka":
                    districtCount = 339;
                    break;
                case "الخصوص":
                case "Khosous":
                    districtCount = 340;
                    break;
            /*case "العبور":
            case "El Ubour":
                districtCount = 341;
                break;*/
                case "القناطر الخيرية":
                case "Qanater al-Khairia":
                    districtCount = 342;
                    break;
                case "بنها":
                case "Banha":
                    districtCount = 343;
                    break;
                case "بهتيم":
                case "Bahtim":
                    districtCount = 344;
                    break;
                case "زاويه النجار":
                case "Zawya El-Naggar":
                    districtCount = 345;
                    break;
                case "شبرا الخيمة":
                case "Shubra al-Khaimah":
                    districtCount = 346;
                    break;
                case "شبين القناطر":
                case "Shebin al-Qanater":
                    districtCount = 347;
                    break;
                case "طوخ":
                case "Tookh":
                    districtCount = 348;
                    break;
                case "قليوب":
                case "Qalyub":
                    districtCount = 349;
                    break;
                case "قها":
                case "Qaha":
                    districtCount = 350;
                    break;
                case "كفر شكر":
                case "Kafr Shukr":
                    districtCount = 351;
                    break;
                case "أبو تشت":
                case "Abu Tisht":
                    districtCount = 352;
                    break;
                case "الوقف":
                case "Wakf":
                    districtCount = 353;
                    break;
                case "شنا":
                case "Shna":
                    districtCount = 354;
                    break;
                case "فرشوط":
                case "Farshout":
                    districtCount = 355;
                    break;
                case "قفط":
                case "Keft":
                    districtCount = 356;
                    break;
                case "قوص":
                case "Quos":
                    districtCount = 357;
                    break;
                case "مدينة قنا":
                case "Qena City":
                    districtCount = 358;
                    break;
                case "نجع حمادي":
                case "Nag Hammadi":
                    districtCount = 359;
                    break;
                case "نقادة":
                case "Nakada":
                    districtCount = 360;
                    break;
                case "الغردقة - أخرى":
                case "Hurghada - Other":
                    districtCount = 361;
                    break;
                case "الغردقة - الجونة":
                case "Hurghada - Gouna":
                    districtCount = 362;
                    break;
                case "القصير":
                case "Qusair":
                    districtCount = 363;
                    break;
                case "رأس غارب":
                case "Ras Gharib":
                    districtCount = 364;
                    break;
                case "سفاجا":
                case "Safaga":
                    districtCount = 365;
                    break;
                case "سهل حشيش":
                case "Sahl Hasheesh":
                    districtCount = 366;
                    break;
                case "شلاتين":
                case "Shalateen":
                    districtCount = 367;
                    break;
                case "مرسى علم":
                case "Marsa Alam":
                    districtCount = 368;
                    break;
                case "أبو حماد":
                case "Abu Hammad":
                    districtCount = 369;
                    break;
                case "أبو كبير":
                case "Abu Kabir":
                    districtCount = 370;
                    break;
                case "أولاد صقر":
                case "Awlad Saqr":
                    districtCount = 371;
                    break;
          /*  case "الإبراهيمية":
            case "Ibrahemyah":
                districtCount = 372;
                break;*/
                case "الحسينية":
                case "Husseiniya":
                    districtCount = 373;
                    break;
                case "الزقازيق":
                case "Zagazig":
                    districtCount = 374;
                    break;
                case "الصالحية الجديدة":
                case "New Salhia":
                    districtCount = 375;
                    break;
           /* case "العاشر من رمضان":
            case "10th of Ramadan":
                districtCount = 376;
                break;*/
                case "القرين":
                case "Qareen":
                    districtCount = 377;
                    break;
                case "القنايات":
                case "Alqnayat":
                    districtCount = 378;
                    break;
                case "بلبيس":
                case "Bilbeis":
                    districtCount = 379;
                    break;
                case "ديرب نجم":
                case "Deyerb Negm":
                    districtCount = 380;
                    break;
                case "فاقوس":
                case "Faqous":
                    districtCount = 381;
                    break;
                case "كفر صقر":
                case "Kafr Saqr":
                    districtCount = 382;
                    break;
                case "مشتول السوق":
                case "Mashtool al-Souk":
                    districtCount = 383;
                    break;
                case "منيا القمح":
                case "Minya al-Qamh":
                    districtCount = 384;
                    break;
                case "ههيا":
                case "Hihya":
                    districtCount = 385;
                    break;
                case "أخميم":
                case "Akhmim":
                    districtCount = 386;
                    break;
                case "البلينا":
                case "Baliana":
                    districtCount = 387;
                    break;
                case "العسيرات":
                case "Alasirat":
                    districtCount = 388;
                    break;
                case "المراغة":
                case "Maragha":
                    districtCount = 389;
                    break;
                case "المنشاة":
                case "Monsha'a":
                    districtCount = 390;
                    break;
                case "جرجا":
                case "Girga":
                    districtCount = 391;
                    break;
                case "جهينة":
                case "Juhaynah":
                    districtCount = 392;
                    break;
                case "دارالسلام":
                case "Dar es-Salam":
                    districtCount = 393;
                    break;
                case "ساقلتة":
                case "Sakaltah":
                    districtCount = 394;
                    break;
                case "طما":
                case "Tama":
                    districtCount = 395;
                    break;
                case "طهطا":
                case "Tahta":
                    districtCount = 396;
                    break;
                case "مدينة سوهاج":
                case "Sohag City":
                    districtCount = 397;
                    break;
                case "أبو رديس":
                case "Abu Rudeis":
                    districtCount = 398;
                    break;
                case "أبو زنيمة":
                case "Abu Zenimah":
                    districtCount = 399;
                    break;
                case "دهب":
                case "Dahab":
                    districtCount = 400;
                    break;
                case "رأس سدر":
                case " Ras Sedr":
                    districtCount = 401;
                    break;
                case "راس سدر":
                case "Ras Sidr":
                    districtCount = 402;
                    break;
                case "سانت كاترين":
                case "St. Catherine":
                    districtCount = 403;
                    break;
                case "شرم الشيخ":
                case "Sharm al-Sheikh":
                    districtCount = 404;
                    break;
                case "طابا":
                case "Taba":
                    districtCount = 405;
                    break;
                case "طور سيناء":
                case "Tor Sinai":
                    districtCount = 406;
                    break;
                case "نويبع":
                case "Nuweiba":
                    districtCount = 407;
                    break;
                case "العين السخنة":
                case "Ain Sukhna":
                    districtCount = 408;
                    break;
                case "حى الجناين":
                case "Ganayen":
                    districtCount = 409;
                    break;
                case "حي الأربعين":
                case "Arbaeen":
                    districtCount = 410;
                    break;
                case "حي السويس":
                case "Suez District":
                    districtCount = 411;
                    break;
                case "حي عتاقة":
                case "Attaka":
                    districtCount = 412;
                    break;
           /* case "فيصل":
            case "Faisal":
                districtCount = 413;
                break;*/
                case "هليوبوليس":
                case "Heliopolis":
                    districtCount = 413;
                    break;
                default:
                    districtCount = 10000;
                    break;
            }

            return districtCount;
        }

        private static void changeDistrictAdapterInformation(Context context, Spinner spinner, int district) {
            String[] districtArray = context.getResources().getStringArray(district);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item,
                            districtArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                    .simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
            spinnerArrayAdapter.notifyDataSetChanged();
        }

        public static void changeDistrictSpinner(Context context, int locationIndex, Spinner spinner) {

            switch (locationIndex) {
                case 0:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_alexandria);
                    break;
                case 1:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_aswan);
                    break;
                case 2:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_assuit);
                    break;
                case 3:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_behaira);
                    break;
                case 4:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_beni_swif);
                    break;
                case 5:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_cairo);
                    break;
                case 6:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_dakhlia);
                    break;
                case 7:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_domiat);
                    break;
                case 8:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_fayoum);
                    break;
                case 9:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_gharbia);
                    break;
                case 10:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_giza);
                    break;
                case 11:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_ismalleia);
                    break;
                case 12:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_kfr_el_sheikh);
                    break;
                case 13:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_luxor);
                    break;
                case 14:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_matrouh);
                    break;
                case 15:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_minya);
                    break;
                case 16:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_manufia);
                    break;
                case 17:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_el_wady_el_gdid);
                    break;
                case 18:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_north_sinai);
                    break;
                case 19:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_port_said);
                    break;
                case 20:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_qualyubia);
                    break;
                case 21:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_qena);
                    break;
                case 22:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_red_sea);
                    break;
                case 23:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_sharqia);
                    break;
                case 24:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_sohag);
                    break;
                case 25:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_south_sinai);
                    break;
                case 26:
                    changeDistrictAdapterInformation(context, spinner, R.array.district_suez);
                    break;
                /*default:
                    // Others
                    return "Others";*/
            }
        }


        public static String[] getDistrictArray(Context context, int locationIndex) {

            switch (locationIndex) {
                case 0: return context.getResources().getStringArray(R.array.district_alexandria);
                case 1: return context.getResources().getStringArray(R.array.district_aswan);
                case 2: return context.getResources().getStringArray(R.array.district_assuit);
                case 3: return context.getResources().getStringArray(R.array.district_behaira);
                case 4: return context.getResources().getStringArray(R.array.district_beni_swif);
                case 5: return context.getResources().getStringArray(R.array.district_cairo);
                case 6: return context.getResources().getStringArray(R.array.district_dakhlia);
                case 7: return context.getResources().getStringArray(R.array.district_domiat);
                case 8: return context.getResources().getStringArray(R.array.district_fayoum);
                case 9: return context.getResources().getStringArray(R.array.district_gharbia);
                case 10: return context.getResources().getStringArray(R.array.district_giza);
                case 11: return context.getResources().getStringArray(R.array.district_ismalleia);
                case 12: return context.getResources().getStringArray(R.array.district_kfr_el_sheikh);
                case 13: return context.getResources().getStringArray(R.array.district_luxor);
                case 14: return context.getResources().getStringArray(R.array.district_matrouh);
                case 15: return context.getResources().getStringArray(R.array.district_minya);
                case 16: return context.getResources().getStringArray(R.array.district_manufia);
                case 17: return context.getResources().getStringArray(R.array.district_el_wady_el_gdid);
                case 18: return context.getResources().getStringArray(R.array.district_north_sinai);
                case 19: return context.getResources().getStringArray(R.array.district_port_said);
                case 20: return context.getResources().getStringArray(R.array.district_qualyubia);
                case 21: return context.getResources().getStringArray(R.array.district_qena);
                case 22: return context.getResources().getStringArray(R.array.district_red_sea);
                case 23: return context.getResources().getStringArray(R.array.district_sharqia);
                case 24: return context.getResources().getStringArray(R.array.district_sohag);
                case 25: return context.getResources().getStringArray(R.array.district_south_sinai);
                case 26: return context.getResources().getStringArray(R.array.district_suez);
                default:
                    // Others
                    return new String[0];
            }
        }


    }


}
