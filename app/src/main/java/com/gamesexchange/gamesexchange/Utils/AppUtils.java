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
                        return "????????????????????";
                    case 1:
                        return "??????????";
                    case 2:
                        return "??????????";
                    case 3:
                        return "??????????????";
                    case 4:
                        return "?????? ????????";
                    case 5:
                        return "??????????????";
                    case 6:
                        return "????????????????";
                    case 7:
                        return "??????????";
                    case 8:
                        return "????????????";
                    case 9:
                        return "??????????????";
                    case 10:
                        return "????????????";
                    case 11:
                        return "??????????????????????";
                    case 12:
                        return "?????? ??????????";
                    case 13:
                        return "????????????";
                    case 14:
                        return "??????????";
                    case 15:
                        return "????????????";
                    case 16:
                        return "????????????????";
                    case 17:
                        return "???????????? ????????????";
                    case 18:
                        return "???????? ??????????";
                    case 19:
                        return "??????????????";
                    case 20:
                        return "??????????????????";
                    case 21:
                        return "??????";
                    case 22:
                        return "?????????? ????????????";
                    case 23:
                        return "??????????????";
                    case 24:
                        return "??????????";
                    case 25:
                        return "???????? ??????????";
                    case 26:
                        return "????????????";
                    default:
                        // Others
                        return "??????????";
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
                case "????????????????????":
                case "Alexandria":
                    locationCount = 0;
                    break;
                case "??????????":
                case "Aswan":
                    locationCount = 1;
                    break;
                case "??????????":
                case "Asyut":
                    locationCount = 2;
                    break;
                case "??????????????":
                case "Beheira":
                    locationCount = 3;
                    break;
                case "?????? ????????":
                case "Beni Suef":
                    locationCount = 4;
                    break;
                case "??????????????":
                case "Cairo":
                    locationCount = 5;
                    break;
                case "????????????????":
                case "Dakahlia":
                    locationCount = 6;
                    break;
                case "??????????":
                case "Damietta":
                    locationCount = 7;
                    break;
                case "????????????":
                case "Fayoum":
                    locationCount = 8;
                    break;
                case "??????????????":
                case "Gharbia":
                    locationCount = 9;
                    break;
                case "????????????":
                case "Giza":
                    locationCount = 10;
                    break;
                case "??????????????????????":
                case "Ismailia":
                    locationCount = 11;
                    break;
                case "?????? ??????????":
                case "Kafr al-Sheikh":
                    locationCount = 12;
                    break;
                case "????????????":
                case "Luxor":
                    locationCount = 13;
                    break;
                case "??????????":
                case "Matruh":
                    locationCount = 14;
                    break;
                case "????????????":
                case "Minya":
                    locationCount = 15;
                    break;
                case "????????????????":
                case "Monufia":
                    locationCount = 16;
                    break;
                case "???????????? ????????????":
                case "New Valley":
                    locationCount = 17;
                    break;
                case "???????? ??????????":
                case "North Sinai":
                    locationCount = 18;
                    break;
                case "??????????????":
                case "Port Said":
                    locationCount = 19;
                    break;
                case "??????????????????":
                case "Qalyubia":
                    locationCount = 20;
                    break;
                case "??????":
                case "Qena":
                    locationCount = 21;
                    break;
                case "?????????? ????????????":
                case "Red Sea":
                    locationCount = 22;
                    break;
                case "??????????????":
                case "Sharqia":
                    locationCount = 23;
                    break;
                case "??????????":
                case "Sohag":
                    locationCount = 24;
                    break;
                case "???????? ??????????":
                case "South Sinai":
                    locationCount = 25;
                    break;
                case "????????????":
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
                        return "?????? ??????";
                    case 1:
                        return "??????????????????";
                    case 2:
                        return "??????????????????????";
                    case 3:
                        return "????????????????????";
                    case 4:
                        return "????????????";
                    case 5:
                        return "????????????";
                    case 6:
                        return "??????????????";
                    case 7:
                        return "????????????";
                    case 8:
                        return "????????????????";
                    case 9:
                        return "????????????????";
                    case 10:
                        return "????????????????";
                    case 11:
                        return "????????????????";
                    case 12:
                        return "????????????????";
                    case 13:
                        return "??????????????";
                    case 14:
                        return "??????????????";
                    case 15:
                        return "????????????";
                    case 16:
                        return "????????????????";
                    case 17:
                        return "??????????";
                    case 18:
                        return "??????????????";
                    case 19:
                        return "??????????????";
                    case 20:
                        return "??????????????";
                    case 21:
                        return "????????????";
                    case 23:
                        return "????????????????";
                    case 24:
                        return "??????????";
                    case 25:
                        return "???????? ??????????????????";
                    case 26:
                        return "?????? ??????????";
                    case 27:
                        return "????????????";
                    case 28:
                        return "????????";
                    case 29:
                        return "??????????????";
                    case 30:
                        return "?????? ??????????";
                    case 31:
                        return "????????";
                    case 32:
                        return "??????????????";
                    case 33:
                        return "???????? ????????";
                    case 34:
                        return "?????? ??????????????";
                    case 35:
                        return "??????????????";
                    case 36:
                        return "????????????";
                    case 37:
                        return "??????????";
                    case 38:
                        return "???????? ??????";
                    case 39:
                        return "???????? ????????";
                    case 40:
                        return "??????????";
                    case 41:
                        return "??????";
                    case 42:
                        return "??????????";
                    case 43:
                        return "????????";
                    case 44:
                        return "??????????";
                    case 45:
                        return "????????????????";
                    case 46:
                        return "???????? ??????????";
                    case 47:
                        return "??????????";
                    case 48:
                        return "?????? ????????";
                    case 49:
                        return "??????????????????";
                    case 50:
                        return "?????? ??????????";
                    case 51:
                        return "??????????";
                    case 52:
                        return "?????????? ??????";
                    case 53:
                        return "???????? ??????????";
                    case 54:
                        return "??????????";
                    case 55:
                        return "???????? ??????????";
                    case 56:
                        return "?????? ??????????";
                    case 57:
                        return "?????? ????????";
                    case 58:
                        return "????????";
                    case 59:
                        return "????????????????";
                    case 60:
                        return "????????????????";
                    case 61:
                        return "????????????????";
                    case 62:
                        return "????????";
                    case 63:
                        return "??????????";
                    case 64:
                        return "????????????";
                    case 65:
                        return "?????? ????????";
                    case 66:
                        return "?????????? ??????????";
                    case 67:
                        return "?????? ????????????";
                    case 68:
                        return "??????????";
                    case 69:
                        return "?????? ??????";
                    case 70:
                        return "?????????? ??????????????";
                    case 71:
                        return "??????????????";
                    case 72:
                        return "??????????????";
                    case 73:
                        return "??????????";
                    case 74:
                        return "??????????????";
                    case 75:
                        return "??????????";
                    case 76:
                        return "???????? ????????";
                    case 77:
                        return "????????";
                    case 78:
                        return "?????????? ??????????";
                    case 79:
                        return "????????????";
                    case 80:
                        return "?????? ????????????????";
                    case 81:
                        return "?????? ??????";
                    case 82:
                        return "????????";
                    case 83:
                        return "????????????????";
                    case 84:
                        return "??????????????????";
                    case 85:
                        return "??????????????????";
                    case 86:
                        return "?????????????????? ??????????????";
                    case 87:
                        return "?????????? ??????????????";
                    case 88:
                        return "??????";
                    case 89:
                        return "?????? ????????";
                    case 90:
                        return "????????????";
                    case 91:
                        return "????????";
                    case 92:
                        return "??????????????";
                    case 93:
                        return "?????? ????????????";
                    case 94:
                        return "?????? ??????????";
                    case 95:
                        return "???????? ??????????????";
                    case 96:
                        return "??????????????";
                    case 97:
                        return "??????????";
                    case 98:
                        return "??????????????";
                    case 99:
                        return "??????";
                    case 100:
                        return "?????? ???????? ??????????????";
                    case 101:
                        return "??????????";
                    case 102:
                        return "?????????? ?????? ????????";
                    case 103:
                        return "????????";
                    case 104:
                        return "????????????????";
                    case 105:
                        return "????????????????";
                    case 106:
                        return "????????????";
                    case 107:
                        return "???????????? ????????????";
                    case 108:
                        return "????????????????";
                    case 109:
                        return "??????????????";
                    case 110:
                        return "?????????????? ??????????????";
                    case 111:
                        return "??????????????";
                    case 112:
                        return "??????????????";
                    case 113:
                        return "?????????? ????????????";
                    case 114:
                        return "????????????";
                    case 115:
                        return "?????????????? ??????????????";
                    case 116:
                        return "??????????????";
                    case 117:
                        return "??????????????";
                    case 118:
                        return "????????????";
                    case 119:
                        return "???????????? ????????";
                    case 120:
                        return "????????????????";
                    case 121:
                        return "???????????? ???? ??????????";
                    case 122:
                        return "?????????????? ???????????????? ??????????????";
                    case 123:
                        return "????????????????";
                    case 124:
                        return "????????????";
                    case 125:
                        return "????????????";
                    case 126:
                        return "?????????????? - ????????";
                    case 127:
                        return "?????????????? ?????????????? -???????????? ??????????";
                    case 128:
                        return "?????????????? ?????????????? - ????????";
                    case 129:
                        return "?????????????? ?????????????? - ??????????????";
                    case 130:
                        return "?????????????? ?????????????? - ???????????? ????????????";
                    case 131:
                        return "?????????????? ?????????????? - ????????";
                    case 132:
                        return "?????????????? ?????????????? - ?????????? ????????????";
                    case 133:
                        return "????????????????";
                    case 134:
                        return "????????????";
                    case 135:
                        return "??????????";
                    case 137:
                        return "??????????????";
                    case 138:
                        return "??????????????";
                    case 139:
                        return "????????????";
                    case 140:
                        return "?????????????? ??????????????";
                    case 141:
                        return "????????????";
                    case 142:
                        return "??????????????";
                    case 143:
                        return "????????????";
                    case 144:
                        return "??????????????";
                    case 145:
                        return "?????? ??????????????";
                    case 146:
                        return "??????????";
                    case 147:
                        return "?????? ??????????";
                    case 148:
                        return "?????????? ????????";
                    case 149:
                        return "?????????? ??????????";
                    case 150:
                        return "?????????? ??????????";
                    case 151:
                        return "??????????";
                    case 152:
                        return "?????? ????????????";
                    case 153:
                        return "?????????? ?? ???????????? ??????????";
                    case 154:
                        return "?????? ??????????";
                    case 155:
                        return "?????????? ??????????????";
                    case 156:
                        return "????????";
                    case 157:
                        return "??????????????";
                    case 158:
                        return "??????";
                    case 159:
                        return "???????? ??????????";
                    case 160:
                        return "?????? ??????";
                    case 161:
                        return "?????? ??????????";
                    case 162:
                        return "?????????? 15 ????????";
                    case 163:
                        return "?????????? ????????????";
                    case 164:
                        return "?????????? ????????????";
                    case 165:
                        return "?????????? ????????????????";
                    case 166:
                        return "?????????? ??????";
                    case 167:
                        return "?????????? ??????";
                    case 168:
                        return "????????????";
                    case 169:
                        return "??????????";
                    case 170:
                        return "?????? ??????????????";
                    case 171:
                        return "?????? ??????????????";
                    case 172:
                        return "?????????????????? ??????????????";
                    case 173:
                        return "?????? ??????????????";
                    case 174:
                        return "??????";
                    case 175:
                        return "??????????";
                    case 177:
                        return "????????????????????";
                    case 178:
                        return "??????????????";
                    case 179:
                        return "??????????????";
                    case 180:
                        return "????????????????";
                    case 181:
                        return "??????????";
                    case 182:
                        return "?????? ????????";
                    case 183:
                        return "?????? ??????????????";
                    case 184:
                        return "????????";
                    case 185:
                        return "??????????";
                    case 186:
                        return "??????????";
                    case 187:
                        return "????????";
                    case 188:
                        return "???????? ??????????";
                    case 189:
                        return "?????? ??????????";
                    case 190:
                        return "?????? ??????";
                    case 191:
                        return "??????????";
                    case 193:
                        return "????????????";
                    case 194:
                        return "??????????";
                    case 195:
                        return "?????????? ??????????????";
                    case 196:
                        return "?????? ????????";
                    case 197:
                        return "???????? ??????????";
                    case 198:
                        return "??????????????";
                    case 199:
                        return "?????? ????????????";
                    case 200:
                        return "?????? ??????";
                    case 201:
                        return "?????????? ??????????";
                    case 202:
                        return "?????? ??????????????";
                    case 203:
                        return "????????";
                    case 204:
                        return "????????????";
                    case 205:
                        return "???????????? ??????????????";
                    case 206:
                        return "??????????";
                    case 207:
                        return "??????????";
                    case 208:
                        return "?????????? ????????????";
                    case 209:
                        return "???????? ????????????";
                    case 210:
                        return "????????????";
                    case 211:
                        return "???????????? ????????????";
                    case 212:
                        return "??????????";
                    case 213:
                        return "????????";
                    case 214:
                        return "??????????";
                    case 215:
                        return "????????";
                    case 216:
                        return "????????";
                    case 217:
                        return "?????? ????????????";
                    case 218:
                        return "6 ????????????";
                    case 219:
                        return "?????? ????????";
                    case 220:
                        return "?????? ????????????";
                    case 221:
                        return "??????????";
                    case 222:
                        return "????????????";
                    case 223:
                        return "????????????????";
                    case 224:
                        return "????????????????";
                    case 225:
                        return "????????????????";
                    case 226:
                        return "??????????????????";
                    case 227:
                        return "??????????";
                    case 228:
                        return "??????????????";
                    case 229:
                        return "?????????? ????????";
                    case 230:
                        return "????????????????";
                    case 231:
                        return "????????";
                    case 232:
                        return "??????????????";
                    case 233:
                        return "????????????????";
                    case 234:
                        return "??????????????????";
                    case 235:
                        return "????????????";
                    case 236:
                        return "???????????? ??????????????????";
                    case 237:
                        return "?????????? ??????";
                    case 238:
                        return "??????????????????";
                    case 239:
                        return "????????????";
                    case 240:
                        return "??????????????????";
                    case 241:
                        return "??????????";
                    case 242:
                        return "????????????";
                    case 243:
                        return "??????????";
                    case 244:
                        return "?????? ??????????";
                    case 245:
                        return "?????????? ??????????????";
                    case 246:
                        return "????????";
                    case 247:
                        return "?????????? ?????????? ??????????????";
                    case 248:
                        return "?????????? ??????????????";
                    case 249:
                        return "???? ????????????";
                    case 250:
                        return "??????????";
                    case 251:
                        return "?????????? ??????";
                    case 252:
                        return "??????????";
                    case 253:
                        return "?????? ??????????";
                    case 254:
                        return "??????";
                    case 255:
                        return "?????? ??????????";
                    case 256:
                        return "????????";
                    case 257:
                        return "????????????";
                    case 258:
                        return "?????? ??????????";
                    case 259:
                        return "???????? ????????????";
                    case 260:
                        return "??????????????";
                    case 261:
                        return "?????? ????????";
                    case 262:
                        return "??????????";
                    case 263:
                        return "??????????";
                    case 264:
                        return "??????????????";
                    case 265:
                        return "???????? ????????????";
                    case 266:
                        return "????????????????";
                    case 267:
                        return "?????????????? ??????";
                    case 268:
                        return "?????????????? ??????";
                    case 269:
                        return "????????";
                    case 270:
                        return "?????????? ??????????????????????";
                    case 271:
                        return "????????????";
                    case 272:
                        return "??????????????";
                    case 273:
                        return "????????????";
                    case 274:
                        return "??????????";
                    case 275:
                        return "????????";
                    case 276:
                        return "????????";
                    case 277:
                        return "???????? ????????";
                    case 278:
                        return "??????";
                    case 279:
                        return "????????";
                    case 280:
                        return "?????????? ?????? ??????????";
                    case 281:
                        return "??????????";
                    case 282:
                        return "??????????";
                    case 283:
                        return "????????";
                    case 284:
                        return "????????????????";
                    case 285:
                        return "??????????????";
                    case 286:
                        return "??????????";
                    case 287:
                        return "????????????";
                    case 288:
                        return "?????????? ????????????";
                    case 289:
                        return "????????????";
                    case 290:
                        return "???????????? ??????????????";
                    case 291:
                        return "????????????";
                    case 292:
                        return "????????????";
                    case 293:
                        return "??????????????";
                    case 294:
                        return "??????????????";
                    case 295:
                        return "??????????";
                    case 296:
                        return "????????";
                    case 297:
                        return "???????????? ??????????????";
                    case 298:
                        return "???????? ??????????";
                    case 299:
                        return "?????? ??????????";
                    case 300:
                        return "????????????";
                    case 301:
                        return "???????????? ??????????????";
                    case 302:
                        return "?????? ????????";
                    case 303:
                        return "?????? ????????";
                    case 304:
                        return "????????????";
                    case 305:
                        return "?????????? ????????????";
                    case 306:
                        return "????????";
                    case 307:
                        return "??????????";
                    case 308:
                        return "????????";
                    case 309:
                        return "??????????";
                    case 310:
                        return "??????????????";
                    case 311:
                        return "??????????????";
                    case 312:
                        return "??????????????";
                    case 313:
                        return "???????? ??????????";
                    case 314:
                        return "??????";
                    case 315:
                        return "?????? ????????????";
                    case 316:
                        return "???????? ??????????";
                    case 317:
                        return "????????????";
                    case 318:
                        return "????????";
                    case 319:
                        return "?????? ????????";
                    case 320:
                        return "??????????????";
                    case 321:
                        return "??????????????";
                    case 322:
                        return "????????????????";
                    case 323:
                        return "??????????";
                    case 324:
                        return "????????";
                    case 325:
                        return "?????????? ??????";
                    case 326:
                        return "????????????";
                    case 327:
                        return "?????????? ????????";
                    case 328:
                        return "????????????";
                    case 329:
                        return "?????? ??????????";
                    case 330:
                        return "??????";
                    case 331:
                        return "??????";
                    case 332:
                        return "???? ????????????";
                    case 333:
                        return "???? ????????????";
                    case 334:
                        return "???? ??????????";
                    case 335:
                        return "???? ??????????????";
                    case 336:
                        return "???? ??????????";
                    case 337:
                        return "???? ????????????";
                    case 338:
                        return "?????????? ??????????????";
                    case 339:
                        return "??????????????";
                    case 340:
                        return "????????????";
                    case 342:
                        return "?????????????? ??????????????";
                    case 343:
                        return "????????";
                    case 344:
                        return "??????????";
                    case 345:
                        return "?????????? ????????????";
                    case 346:
                        return "???????? ????????????";
                    case 347:
                        return "???????? ??????????????";
                    case 348:
                        return "??????";
                    case 349:
                        return "??????????";
                    case 350:
                        return "??????";
                    case 351:
                        return "?????? ??????";
                    case 352:
                        return "?????? ??????";
                    case 353:
                        return "??????????";
                    case 354:
                        return "??????";
                    case 355:
                        return "??????????";
                    case 356:
                        return "??????";
                    case 357:
                        return "??????";
                    case 358:
                        return "?????????? ??????";
                    case 359:
                        return "?????? ??????????";
                    case 360:
                        return "??????????";
                    case 361:
                        return "?????????????? - ????????";
                    case 362:
                        return "?????????????? - ????????????";
                    case 363:
                        return "????????????";
                    case 364:
                        return "?????? ????????";
                    case 365:
                        return "??????????";
                    case 366:
                        return "?????? ????????";
                    case 367:
                        return "????????????";
                    case 368:
                        return "???????? ??????";
                    case 369:
                        return "?????? ????????";
                    case 370:
                        return "?????? ????????";
                    case 371:
                        return "?????????? ??????";
                    case 373:
                        return "????????????????";
                    case 374:
                        return "????????????????";
                    case 375:
                        return "???????????????? ??????????????";
                    case 377:
                        return "????????????";
                    case 378:
                        return "????????????????";
                    case 379:
                        return "??????????";
                    case 380:
                        return "???????? ??????";
                    case 381:
                        return "??????????";
                    case 382:
                        return "?????? ??????";
                    case 383:
                        return "?????????? ??????????";
                    case 384:
                        return "???????? ??????????";
                    case 385:
                        return "????????";
                    case 386:
                        return "??????????";
                    case 387:
                        return "??????????????";
                    case 388:
                        return "????????????????";
                    case 389:
                        return "??????????????";
                    case 390:
                        return "??????????????";
                    case 391:
                        return "????????";
                    case 392:
                        return "??????????";
                    case 393:
                        return "??????????????????";
                    case 394:
                        return "????????????";
                    case 395:
                        return "??????";
                    case 396:
                        return "????????";
                    case 397:
                        return "?????????? ??????????";
                    case 398:
                        return "?????? ????????";
                    case 399:
                        return "?????? ??????????";
                    case 400:
                        return "??????";
                    case 401:
                        return "?????? ??????";
                    case 402:
                        return "?????? ??????";
                    case 403:
                        return "???????? ????????????";
                    case 404:
                        return "?????? ??????????";
                    case 405:
                        return "????????";
                    case 406:
                        return "?????? ??????????";
                    case 407:
                        return "??????????";
                    case 408:
                        return "?????????? ????????????";
                    case 409:
                        return "???? ??????????????";
                    case 410:
                        return "???? ????????????????";
                    case 411:
                        return "???? ????????????";
                    case 412:
                        return "???? ??????????";
                    case 413:
                        return "??????????????????";
                    default:
                        return "??????????";
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
                case "?????? ??????":
                case "Abu Qir":
                    districtCount = 0;
                    break;
                case "??????????????????":
                case "Azarita":
                    districtCount = 1;
                    break;
                case "??????????????????????": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Al Ibrahimiyyah":
                case "Ibrahemyah":
                    districtCount = 2;
                    break;
                case "????????????????????":
                case "Alexandria":
                    districtCount = 3;
                    break;
                case "????????????":
                case "Gomrok":
                    districtCount = 4;
                    break;
                case "????????????":
                case "Al Hadrah":
                    districtCount = 5;
                    break;
                case "??????????????":
                case "Dekheila":
                    districtCount = 6;
                    break;
                case "????????????":
                case "Seyouf":
                    districtCount = 7;
                    break;
                case "????????????????":
                case "Salehia":
                    districtCount = 8;
                    break;
                case "????????????????":
                case "Dhahria":
                    districtCount = 9;
                    break;
                case "????????????????":
                case "Amreya":
                    districtCount = 10;
                    break;
                case "????????????????":
                case "Asafra":
                    districtCount = 11;
                    break;
                case "????????????????":
                case "Attarin":
                    districtCount = 12;
                    break;
                case "??????????????":
                case "Awayed":
                    districtCount = 13;
                    break;
                case "??????????????":
                case "Kabbary":
                    districtCount = 14;
                    break;
                case "????????????":
                case "Labban":
                    districtCount = 15;
                    break;
                case "????????????????":
                case "Maamoura":
                    districtCount = 16;
                    break;
                case "??????????":
                case "El Max":
                    districtCount = 17;
                    break;
                case "??????????????":
                case "Montazah":
                    districtCount = 18;
                    break;
                case "??????????????":
                case "Mandara":
                    districtCount = 19;
                    break;
                case "??????????????":
                case "Manshiyya":
                    districtCount = 20;
                    break;
                case "????????????":
                case "Nakheel":
                    districtCount = 21;
                    break;
            /*case "????????????":
            case "Nozha":
                districtCount = 22;
                break;*/
                case "????????????????":
                case "Wardian":
                    districtCount = 23;
                    break;
                case "??????????":
                case "Bacchus":
                    districtCount = 24;
                    break;
                case "???????? ??????????????????":
                case "Bahray - Anfoshy":
                    districtCount = 25;
                    break;
                case "?????? ??????????":
                case "Borg al-Arab":
                    districtCount = 26;
                    break;
                case "????????????":
                case "Bolkly":
                    districtCount = 27;
                    break;
                case "????????":
                case "Glim":
                    districtCount = 28;
                    break;
                case "??????????????":
                case "Gianaclis":
                    districtCount = 29;
                    break;
                case "?????? ??????????":
                case "Ras El Tin":
                    districtCount = 30;
                    break;
                case "????????":
                case "Roushdy":
                    districtCount = 31;
                    break;
                case "??????????????":
                case "Zezenia":
                    districtCount = 32;
                    break;
                case "???????? ????????":
                case "Saba Pasha":
                    districtCount = 33;
                    break;
                case "?????? ??????????????":
                case "San Stefano":
                    districtCount = 34;
                    break;
                case "??????????????":
                case "Sporting":
                    districtCount = 35;
                    break;
                case "????????????":
                case "Stanley":
                    districtCount = 36;
                    break;
                case "??????????":
                case "Smoha":
                    districtCount = 37;
                    break;
                case "???????? ??????":
                case "Sidi Beshr":
                    districtCount = 38;
                    break;
                case "???????? ????????":
                case "Sidi Gaber":
                    districtCount = 39;
                    break;
                case "??????????":
                case "Shatby":
                    districtCount = 40;
                    break;
                case "??????":
                case "Schutz":
                    districtCount = 41;
                    break;
                case "??????????":
                case "Tosson":
                    districtCount = 42;
                    break;
                case "????????":
                case "Agami":
                    districtCount = 43;
                    break;
                case "??????????":
                case "Fleming":
                    districtCount = 44;
                    break;
                case "????????????????":
                case "Victoria":
                    districtCount = 45;
                    break;
                case "???????? ??????????":
                case "Camp Caesar":
                    districtCount = 46;
                    break;
                case "??????????":
                case "Karmous":
                    districtCount = 47;
                    break;
                case "?????? ????????":
                case "Kafr Abdo":
                    districtCount = 48;
                    break;
                case "??????????????????":
                case "Cleopatra":
                    districtCount = 49;
                    break;
                case "?????? ??????????":
                case "Koum al-Dikka":
                    districtCount = 50;
                    break;
                case "??????????":
                case "Laurent":
                    districtCount = 51;
                    break;
                case "?????????? ??????":
                case "Moharam Bik":
                    districtCount = 52;
                    break;
                case "???????? ??????????":
                case "Raml Station":
                    districtCount = 53;
                    break;
                case "??????????":
                case "Miami":
                    districtCount = 54;
                    break;
                case "???????? ??????????":
                case "Mina al-Basal":
                    districtCount = 55;
                    break;
                case "?????? ??????????":
                case "Abou al-Reish":
                    districtCount = 56;
                    break;
                case "?????? ????????":
                case "Abou Simbel":
                    districtCount = 57;
                    break;
                case "????????":
                case "Edfu":
                    districtCount = 58;
                    break;
                case "????????????????":
                case "Basiliah":
                    districtCount = 59;
                    break;
                case "????????????????":
                case "Rdesiah":
                    districtCount = 60;
                    break;
                case "????????????????":
                case "Sebaeiah":
                    districtCount = 61;
                    break;
                case "????????":
                case "Daraw":
                    districtCount = 62;
                    break;
                case "??????????":
                case "Sahara":
                    districtCount = 63;
                    break;
                case "????????????":
                case "Kalabsha":
                    districtCount = 64;
                    break;
                case "?????? ????????":
                case "Kom Ombo":
                    districtCount = 65;
                    break;
                case "?????????? ??????????":
                case "Aswan City":
                    districtCount = 66;
                    break;
                case "?????? ????????????":
                case "Nasr al-Noba":
                    districtCount = 67;
                    break;
                case "??????????":
                case "Abnub":
                    districtCount = 68;
                    break;
                case "?????? ??????":
                case "Abu Teeg":
                    districtCount = 69;
                    break;
                case "?????????? ??????????????":
                case "New Assiut":
                    districtCount = 70;
                    break;
                case "??????????????":
                case "Badari":
                    districtCount = 71;
                    break;
                case "??????????????":
                case "Ghanayem":
                    districtCount = 72;
                    break;
                case "??????????":
                case "Fateh":
                    districtCount = 73;
                    break;
                case "??????????????":
                case "Qusiya":
                    districtCount = 74;
                    break;
                case "??????????":
                case "Dairut":
                    districtCount = 75;
                    break;
                case "???????? ????????":
                case "Sahel Selim":
                    districtCount = 76;
                    break;
                case "????????":
                case "Sedfa":
                    districtCount = 77;
                    break;
                case "?????????? ??????????":
                case "Asyut City":
                    districtCount = 78;
                    break;
                case "????????????":
                case "Manfalut":
                    districtCount = 79;
                    break;
                case "?????? ????????????????":
                case "Abuu al-Matamer":
                    districtCount = 80;
                    break;
                case "?????? ??????":
                case "Abou Homs":
                    districtCount = 81;
                    break;
                case "????????":
                case "Edko":
                    districtCount = 82;
                    break;
                case "????????????????":
                case "Delengat":
                    districtCount = 83;
                    break;
                case "??????????????????":
                case "Rahmaniya":
                    districtCount = 84;
                    break;
                case "??????????????????":
                case "Mahmoudiyah":
                    districtCount = 85;
                    break;
                case "?????????????????? ??????????????":
                case "Nubariyah":
                    districtCount = 86;
                    break;
                case "?????????? ??????????????":
                case "Etay al-Barud":
                    districtCount = 87;
                    break;
                case "??????":
                case "Badr":
                    districtCount = 88;
                    break;
                case "?????? ????????":
                case "Hosh Essa":
                    districtCount = 89;
                    break;
                case "????????????":
                case "Damanhour":
                    districtCount = 90;
                    break;
                case "????????":
                case "Rashid":
                    districtCount = 91;
                    break;
                case "??????????????":
                case "Shubrakhit":
                    districtCount = 92;
                    break;
                case "?????? ????????????":
                case "Kafr al-Dawwar":
                    districtCount = 93;
                    break;
                case "?????? ??????????":
                case "Kom Hamadah":
                    districtCount = 94;
                    break;
                case "???????? ??????????????":
                case "Wadi al-Natrun":
                    districtCount = 95;
                    break;
                case "??????????????":
                case "Ehnasia":
                    districtCount = 96;
                    break;
                case "??????????":
                case "Al Feshn":
                    districtCount = 97;
                    break;
                case "??????????????":
                case "Al Wasty":
                    districtCount = 98;
                    break;
                case "??????":
                case "Beba":
                    districtCount = 99;
                    break;
                case "?????? ???????? ??????????????":
                case "New Beni Suef":
                    districtCount = 100;
                    break;
                case "??????????":
                case "Samasta":
                    districtCount = 101;
                    break;
                case "?????????? ?????? ????????":
                case "Beni Suef City":
                    districtCount = 102;
                    break;
                case "????????":
                case "Nasser":
                    districtCount = 103;
                    break;
                case "????????????????":
                case "Al Amiriyyah":
                    districtCount = 104;
                    break;
                case "????????????????":
                case "Basateen":
                    districtCount = 105;
                    break;
                case "????????????":
                case "Tebeen":
                    districtCount = 106;
                    break;
                case "???????????? ????????????":
                case "The 3rd District":
                    districtCount = 107;
                    break;
                case "????????????????":
                case "Gamaleya":
                    districtCount = 108;
                    break;
                case "??????????????":
                case "Helmeya":
                    districtCount = 109;
                    break;
                case "?????????????? ??????????????":
                case "Helmeya al-Gadeda":
                    districtCount = 110;
                    break;
                case "??????????????":
                case "Khalifa":
                    districtCount = 111;
                    break;
                case "??????????????":
                case "Darrasa":
                    districtCount = 112;
                    break;
                case "?????????? ????????????":
                case "Darb al-Ahmar":
                    districtCount = 113;
                    break;
                case "????????????": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Roda":
                case "Rodah":
                    districtCount = 114;
                    break;
                case "?????????????? ??????????????":
                case "Zawya al-Hamra":
                    districtCount = 115;
                    break;
                case "??????????????":
                case "Zamalek":
                    districtCount = 116;
                    break;
                case "??????????????":
                case "Zaytoun":
                    districtCount = 117;
                    break;
                case "????????????":
                case "Sahel":
                    districtCount = 118;
                    break;
                case "???????????? ????????":
                case "Sayeda Zeinab":
                    districtCount = 119;
                    break;
                case "????????????????":
                case "Sharabeya":
                    districtCount = 120;
                    break;
                case "???????????? ???? ??????????": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "10th Ramadan City":
                case "10th of Ramadan":
                    districtCount = 121;
                    break;
                case "?????????????? ???????????????? ??????????????":
                case "New Capital City":
                    districtCount = 122;
                    break;
                case "????????????????":
                case "Abasiya":
                    districtCount = 123;
                    break;
                case "????????????": // HEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEELP
                case "Obour City":
                case "El Ubour":
                    districtCount = 124;
                    break;
                case "????????????":
                case "Ataba":
                    districtCount = 125;
                    break;
                case "?????????????? - ????????":
                case "Cairo - Other":
                    districtCount = 126;
                    break;
                case "?????????????? ?????????????? -???????????? ??????????":
                case "New Cairo - First Settlement":
                    districtCount = 127;
                    break;
                case "?????????????? ?????????????? - ????????":
                case "New Cairo - Other":
                    districtCount = 128;
                    break;
                case "?????????????? ?????????????? - ??????????????":
                case "New Cairo - Andalous":
                    districtCount = 129;
                    break;
                case "?????????????? ?????????????? - ???????????? ????????????":
                case "New Cairo - Fifth Settlement":
                    districtCount = 130;
                    break;
                case "?????????????? ?????????????? - ????????":
                case "New Cairo - Lotus":
                    districtCount = 131;
                    break;
                case "?????????????? ?????????????? - ?????????? ????????????":
                case "New Cairo - Rehab City":
                    districtCount = 132;
                    break;
                case "????????????????":
                case "Katameya":
                    districtCount = 133;
                    break;
                case "????????????":
                case "Almazah":
                    districtCount = 134;
                    break;
                case "??????????":
                case "Marg":
                    districtCount = 135;
                    break;
            /*case "??????????????":
            case "Matareya":
                districtCount = 136;
                break;*/
                case "??????????????":
                case "Maadi":
                    districtCount = 137;
                    break;
                case "??????????????":
                case "Ma'sara":
                    districtCount = 138;
                    break;
                case "????????????":
                case "Mokattam":
                    districtCount = 139;
                    break;
                case "?????????????? ??????????????":
                case "Mounira al-Gadeda":
                    districtCount = 140;
                    break;
                case "????????????":
                case "Al Manial":
                    districtCount = 141;
                    break;
                case "??????????????":
                case "Moski":
                    districtCount = 142;
                    break;
                case "????????????":
                case "Nozha":
                    districtCount = 143;
                    break;
                case "??????????????":
                case "Waili":
                    districtCount = 144;
                    break;
                case "?????? ??????????????":
                case "Bab al-Shereia":
                    districtCount = 145;
                    break;
                case "??????????":
                case "Boulaq":
                    districtCount = 146;
                    break;
                case "?????? ??????????":
                case "Bait El Watan":
                    districtCount = 147;
                    break;
                case "?????????? ????????":
                case "Garden City":
                    districtCount = 148;
                    break;
                case "?????????? ??????????":
                case "Hadayek al-Kobba":
                    districtCount = 149;
                    break;
                case "?????????? ??????????":
                case "Hadayek Helwan":
                    districtCount = 150;
                    break;
                case "??????????":
                case "Helwan":
                    districtCount = 151;
                    break;
                case "?????? ????????????":
                case "Dar al-Salaam":
                    districtCount = 152;
                    break;
                case "?????????? ?? ???????????? ??????????":
                case "Ramses + Ramses Extension":
                    districtCount = 153;
                    break;
                case "?????? ??????????":
                case "Rod al-Farag":
                    districtCount = 154;
                    break;
                case "?????????? ??????????????":
                case "Zahraa Al Maadi":
                    districtCount = 155;
                    break;
                case "????????":
                case "Shubra":
                    districtCount = 156;
                    break;
                case "??????????????":
                case "Sheraton":
                    districtCount = 157;
                    break;
                case "??????":
                case "Tura":
                    districtCount = 158;
                    break;
                case "???????? ??????????":
                case "Izbat an Nakhl":
                    districtCount = 159;
                    break;
                case "?????? ??????":
                case "Ain Shams":
                    districtCount = 160;
                    break;
                case "?????? ??????????":
                case "Qasr al-Nil":
                    districtCount = 161;
                    break;
                case "?????????? 15 ????????":
                case "15 May City":
                    districtCount = 162;
                    break;
                case "?????????? ????????????":
                case "Salam City":
                    districtCount = 163;
                    break;
                case "?????????? ????????????":
                case "Shorouk City":
                    districtCount = 164;
                    break;
                case "?????????? ????????????????":
                case "Future City":
                    districtCount = 165;
                    break;
                case "?????????? ??????":
                case "Badr City":
                    districtCount = 166;
                    break;
                case "?????????? ??????":
                case "Nasr City":
                    districtCount = 167;
                    break;
                case "????????????":
                case "Madinaty":
                    districtCount = 168;
                    break;
                case "??????????":
                case "Musturad":
                    districtCount = 169;
                    break;
                case "?????? ??????????????":
                case "New Cairo":// Should be new egypt
                    districtCount = 170;
                    break;
                case "?????? ??????????????":
                case "Masr al-Kadema":
                    districtCount = 171;
                    break;
                case "?????????????????? ??????????????":
                case "New Heliopolis":
                    districtCount = 172;
                    break;
                case "?????? ??????????????":
                case "Downtown Cairo":
                    districtCount = 173;
                    break;
                case "??????":
                case "Aga":
                    districtCount = 174;
                    break;
                case "??????????":
                case "Akhtab":
                    districtCount = 175;
                    break;
          /*  case "????????????????":
            case "Gamaleya":
                districtCount = 176;
                break;*/
                case "????????????????????":
                case "Sinbillawain":
                    districtCount = 177;
                    break;
                case "??????????????":
                case "Matareya":
                    districtCount = 178;
                    break;
                case "??????????????":
                case "Manzala":
                    districtCount = 179;
                    break;
                case "????????????????":
                case "Mansura":
                    districtCount = 180;
                    break;
                case "??????????":
                case "Belqas":
                    districtCount = 181;
                    break;
                case "?????? ????????":
                case "Bani Ubayd":
                    districtCount = 182;
                    break;
                case "?????? ??????????????":
                case "Tama al-Amdeed":
                    districtCount = 183;
                    break;
                case "????????":
                case "Gamasa":
                    districtCount = 184;
                    break;
                case "??????????":
                case "Dikirnis":
                    districtCount = 185;
                    break;
                case "??????????":
                case "Shirbin":
                    districtCount = 186;
                    break;
                case "????????":
                case "Talkha":
                    districtCount = 187;
                    break;
                case "???????? ??????????":
                case "Minat al-Nasr":
                    districtCount = 188;
                    break;
                case "?????? ??????????":
                case "Mit Slseel":
                    districtCount = 189;
                    break;
                case "?????? ??????":
                case "Mit Ghamr":
                    districtCount = 190;
                    break;
                case "??????????":
                case "Nabaruh":
                    districtCount = 191;
                    break;
            /*case "????????????":
            case "Rodah":
                districtCount = 192;
                break;*/
                case "????????????":
                case "Zarqa":
                    districtCount = 193;
                    break;
                case "??????????":
                case "Saro":
                    districtCount = 194;
                    break;
                case "?????????? ??????????????":
                case "New Damietta":
                    districtCount = 195;
                    break;
                case "?????? ????????":
                case "Ras al-Bar":
                    districtCount = 196;
                    break;
                case "???????? ??????????":
                case "Ezbet al-Burj":
                    districtCount = 197;
                    break;
                case "??????????????":
                case "Fareskour":
                    districtCount = 198;
                    break;
                case "?????? ????????????":
                case "Kafr al-Bateekh":
                    districtCount = 199;
                    break;
                case "?????? ??????":
                case "Kafr Saad":
                    districtCount = 200;
                    break;
                case "?????????? ??????????":
                case "Damietta City":
                    districtCount = 201;
                    break;
                case "?????? ??????????????":
                case "Mit Abughalb":
                    districtCount = 202;
                    break;
                case "????????":
                case "Atssa":
                    districtCount = 203;
                    break;
                case "????????????":
                case "Ibshway":
                    districtCount = 204;
                    break;
                case "???????????? ??????????????":
                case "New Fayoum":
                    districtCount = 205;
                    break;
                case "??????????":
                case "Sinnuras":
                    districtCount = 206;
                    break;
                case "??????????":
                case "Tamiya":
                    districtCount = 207;
                    break;
                case "?????????? ????????????":
                case "Fayoum City":
                    districtCount = 208;
                    break;
                case "???????? ????????????":
                case "Yusuf al-Sadiq":
                    districtCount = 209;
                    break;
                case "????????????":
                case "Santa":
                    districtCount = 210;
                    break;
                case "???????????? ????????????":
                case "Mahalla al-Kobra":
                    districtCount = 211;
                    break;
                case "??????????":
                case "Basyoun":
                    districtCount = 212;
                    break;
                case "????????":
                case "Zefta":
                    districtCount = 213;
                    break;
                case "??????????":
                case "Samanoud":
                    districtCount = 214;
                    break;
                case "????????":
                case "Tanta":
                    districtCount = 215;
                    break;
                case "????????":
                case "Qutour":
                    districtCount = 216;
                    break;
                case "?????? ????????????":
                case "Kafr al-Zayat":
                    districtCount = 217;
                    break;
                case "6 ????????????":
                case "6th of October":
                    districtCount = 218;
                    break;
                case "?????? ????????":
                case "Abu Rawash":
                    districtCount = 219;
                    break;
                case "?????? ????????????":
                case "Ard El Lewa":
                    districtCount = 220;
                    break;
                case "??????????":
                case "Oseem":
                    districtCount = 221;
                    break;
                case "????????????":
                case "Imbaba":
                    districtCount = 222;
                    break;
                case "????????????????":
                case "Badrasheen":
                    districtCount = 223;
                    break;
                case "????????????????":
                case "Baragil":
                    districtCount = 224;
                    break;
                case "????????????????":
                case "Haraneya":
                    districtCount = 225;
                    break;
                case "??????????????????":
                case "Hawamdeya":
                    districtCount = 226;
                    break;
                case "??????????":
                case "Dokki":
                    districtCount = 227;
                    break;
                case "??????????????":
                case "Remaia":
                    districtCount = 228;
                    break;
                case "?????????? ????????":
                case "Sheikh Zayed":
                    districtCount = 229;
                    break;
                case "????????????????":
                case "Sahafieen":
                    districtCount = 230;
                    break;
                case "????????":
                case "Saf":
                    districtCount = 231;
                    break;
                case "??????????????":
                case "Agouza":
                    districtCount = 232;
                    break;
                case "????????????????":
                case "Azizia":
                    districtCount = 233;
                    break;
                case "??????????????????":
                case "Omrania":
                    districtCount = 234;
                    break;
                case "????????????":
                case "El Ayyat":
                    districtCount = 235;
                    break;
                case "???????????? ??????????????????":
                case "Pharaonic Village":
                    districtCount = 236;
                    break;
                case "?????????? ??????":
                case "Kit Kat":
                    districtCount = 237;
                    break;
                case "??????????????????":
                case "Mansuriyya":
                    districtCount = 238;
                    break;
                case "????????????":
                case "Moneeb":
                    districtCount = 239;
                    break;
                case "??????????????????":
                case "Mohandessin":
                    districtCount = 240;
                    break;
                case "??????????":
                case "Haram":
                    districtCount = 241;
                    break;
                case "????????????":
                case "Warraq":
                    districtCount = 242;
                    break;
                case "??????????":
                case "Bashtil":
                    districtCount = 243;
                    break;
                case "?????? ??????????":
                case "Bani Salamah":
                    districtCount = 244;
                    break;
                case "?????????? ??????????????":
                case "Boulaq Dakrour":
                    districtCount = 245;
                    break;
                case "????????":
                case "Tersa":
                    districtCount = 246;
                    break;
                case "?????????? ?????????? ??????????????":
                case "Dahab Island and Coldair":
                    districtCount = 247;
                    break;
                case "?????????? ??????????????":
                case "Hadayek al-Ahram":
                    districtCount = 248;
                    break;
                case "???? ????????????":
                case "Giza District":
                    districtCount = 249;
                    break;
                case "??????????":
                case "Dahshur":
                    districtCount = 250;
                    break;
                case "?????????? ??????":
                case "Sakyet Mekky":
                    districtCount = 251;
                    break;
                case "??????????":
                case "Saqqara":
                    districtCount = 252;
                    break;
                case "?????? ??????????":
                case "Souk al-Ahad":
                    districtCount = 253;
                    break;
                case "??????":
                case "Saft":
                    districtCount = 254;
                    break;
                case "?????? ??????????":
                case "West Somid":
                    districtCount = 255;
                    break;
                case "????????":
                case "Faisal":
                    districtCount = 256;
                    break;
                case "????????????":
                case "Kerdasa":
                    districtCount = 257;
                    break;
                case "?????? ??????????":
                case "Kafr Tohormos":
                    districtCount = 258;
                    break;
                case "???????? ????????????":
                case "Markaz al-Giza":
                    districtCount = 259;
                    break;
                case "??????????????":
                case "Maryotaya":
                    districtCount = 260;
                    break;
                case "?????? ????????":
                case "Mit Oqba":
                    districtCount = 261;
                    break;
                case "??????????":
                case "Nahia":
                    districtCount = 262;
                    break;
                case "??????????":
                case "Wardan":
                    districtCount = 263;
                    break;
                case "??????????????":
                case "Abu Swear":
                    districtCount = 264;
                    break;
                case "???????? ????????????":
                case "Tal al-Kebeer":
                    districtCount = 265;
                    break;
                case "????????????????":
                case "Qassaseen":
                    districtCount = 266;
                    break;
                case "?????????????? ??????":
                case "Kantara East":
                    districtCount = 267;
                    break;
                case "?????????????? ??????":
                case "Kantara West":
                    districtCount = 268;
                    break;
                case "????????":
                case "Fayed":
                    districtCount = 269;
                    break;
                case "?????????? ??????????????????????":
                case "Ismailia City":
                    districtCount = 270;
                    break;
                case "????????????":
                case "Brolos":
                    districtCount = 271;
                    break;
                case "??????????????":
                case "Hamoul":
                    districtCount = 272;
                    break;
                case "????????????":
                case "Riyadh":
                    districtCount = 273;
                    break;
                case "??????????":
                case "Baltim":
                    districtCount = 274;
                    break;
                case "????????":
                case "Bella":
                    districtCount = 275;
                    break;
                case "????????":
                case "Desouk":
                    districtCount = 276;
                    break;
                case "???????? ????????":
                case "Sidi Salem":
                    districtCount = 277;
                    break;
                case "??????":
                case "Fouh":
                    districtCount = 278;
                    break;
                case "????????":
                case "Qaleen":
                    districtCount = 279;
                    break;
                case "?????????? ?????? ??????????":
                case "Kafr al-Sheikh City":
                    districtCount = 280;
                    break;
                case "??????????":
                case "Motobas":
                    districtCount = 281;
                    break;
                case "??????????":
                case "Armant":
                    districtCount = 282;
                    break;
                case "????????":
                case "Isna":
                    districtCount = 283;
                    break;
                case "????????????????":
                case "Bayadeya":
                    districtCount = 284;
                    break;
                case "??????????????":
                case "Zinnia":
                    districtCount = 285;
                    break;
                case "??????????":
                case "Tod":
                    districtCount = 286;
                    break;
                case "????????????":
                case "Qurna":
                    districtCount = 287;
                    break;
                case "?????????? ????????????":
                case "Luxor City":
                    districtCount = 288;
                    break;
                case "????????????":
                case "Hammam":
                    districtCount = 289;
                    break;
                case "???????????? ??????????????":
                case "North Coast":
                    districtCount = 290;
                    break;
                case "????????????":
                case "Salloum":
                    districtCount = 291;
                    break;
                case "????????????":
                case "Dabaa":
                    districtCount = 292;
                    break;
                case "??????????????":
                case "Alamein":
                    districtCount = 293;
                    break;
                case "??????????????":
                case "Nagela":
                    districtCount = 294;
                    break;
                case "??????????":
                case "Barany":
                    districtCount = 295;
                    break;
                case "????????":
                case "Siwa":
                    districtCount = 296;
                    break;
                case "???????????? ??????????????":
                case "Marina El Alamein":
                    districtCount = 297;
                    break;
                case "???????? ??????????":
                case "Marsa Matrouh":
                    districtCount = 298;
                    break;
                case "?????? ??????????":
                case "Abu Qurqas":
                    districtCount = 299;
                    break;
                case "????????????":
                case "Adwa":
                    districtCount = 300;
                    break;
                case "???????????? ??????????????":
                case "New Minya":
                    districtCount = 301;
                    break;
                case "?????? ????????":
                case "Beni Mazar":
                    districtCount = 302;
                    break;
                case "?????? ????????":
                case "Deir Mawas":
                    districtCount = 303;
                    break;
                case "????????????":
                case "Samalut":
                    districtCount = 304;
                    break;
                case "?????????? ????????????":
                case "Minya City":
                    districtCount = 305;
                    break;
                case "????????":
                case "Matay":
                    districtCount = 306;
                    break;
                case "??????????":
                case "Maghagha":
                    districtCount = 307;
                    break;
                case "????????":
                case "Malawi":
                    districtCount = 308;
                    break;
                case "??????????":
                case "Ashmon":
                    districtCount = 309;
                    break;
                case "??????????????":
                case "Bagour":
                    districtCount = 310;
                    break;
                case "??????????????":
                case "Sadat":
                    districtCount = 311;
                    break;
                case "??????????????":
                case "Shohadaa":
                    districtCount = 312;
                    break;
                case "???????? ??????????":
                case "Berket al-Sabaa":
                    districtCount = 313;
                    break;
                case "??????":
                case "Tala":
                    districtCount = 314;
                    break;
                case "?????? ????????????":
                case "Sers al-Lyan":
                    districtCount = 315;
                    break;
                case "???????? ??????????":
                case "Shebin al-Koum":
                    districtCount = 316;
                    break;
                case "????????????":
                case "Quesna":
                    districtCount = 317;
                    break;
                case "????????":
                case "Menouf":
                    districtCount = 318;
                    break;
                case "?????? ????????":
                case "Mit El Ezz":
                    districtCount = 319;
                    break;
                case "??????????????":
                case "Kharga":
                    districtCount = 320;
                    break;
                case "??????????????":
                case "Dakhla":
                    districtCount = 321;
                    break;
                case "????????????????":
                case "Farafra":
                    districtCount = 322;
                    break;
                case "??????????":
                case "Paris":
                    districtCount = 323;
                    break;
                case "????????":
                case "Balat":
                    districtCount = 324;
                    break;
                case "?????????? ??????":
                case "Mut":
                    districtCount = 325;
                    break;
                case "????????????":
                case "Hasana":
                    districtCount = 326;
                    break;
                case "?????????? ????????":
                case "Sheikh Zoweyd":
                    districtCount = 327;
                    break;
                case "????????????":
                case "Arish":
                    districtCount = 328;
                    break;
                case "?????? ??????????":
                case "Bir al-Abed":
                    districtCount = 329;
                    break;
                case "??????":
                case "Rafah":
                    districtCount = 330;
                    break;
                case "??????":
                case "Nakhl":
                    districtCount = 331;
                    break;
                case "???? ????????????":
                case "Ganoub District":
                    districtCount = 332;
                    break;
                case "???? ????????????":
                case "Zohour District":
                    districtCount = 333;
                    break;
                case "???? ??????????":
                case "Sharq District":
                    districtCount = 334;
                    break;
                case "???? ??????????????":
                case "Dawahy District":
                    districtCount = 335;
                    break;
                case "???? ??????????":
                case "Arab District":
                    districtCount = 336;
                    break;
                case "???? ????????????":
                case "Manakh District":
                    districtCount = 337;
                    break;
                case "?????????? ??????????????":
                case "Port Fouad":
                    districtCount = 338;
                    break;
                case "??????????????":
                case "Khanka":
                    districtCount = 339;
                    break;
                case "????????????":
                case "Khosous":
                    districtCount = 340;
                    break;
            /*case "????????????":
            case "El Ubour":
                districtCount = 341;
                break;*/
                case "?????????????? ??????????????":
                case "Qanater al-Khairia":
                    districtCount = 342;
                    break;
                case "????????":
                case "Banha":
                    districtCount = 343;
                    break;
                case "??????????":
                case "Bahtim":
                    districtCount = 344;
                    break;
                case "?????????? ????????????":
                case "Zawya El-Naggar":
                    districtCount = 345;
                    break;
                case "???????? ????????????":
                case "Shubra al-Khaimah":
                    districtCount = 346;
                    break;
                case "???????? ??????????????":
                case "Shebin al-Qanater":
                    districtCount = 347;
                    break;
                case "??????":
                case "Tookh":
                    districtCount = 348;
                    break;
                case "??????????":
                case "Qalyub":
                    districtCount = 349;
                    break;
                case "??????":
                case "Qaha":
                    districtCount = 350;
                    break;
                case "?????? ??????":
                case "Kafr Shukr":
                    districtCount = 351;
                    break;
                case "?????? ??????":
                case "Abu Tisht":
                    districtCount = 352;
                    break;
                case "??????????":
                case "Wakf":
                    districtCount = 353;
                    break;
                case "??????":
                case "Shna":
                    districtCount = 354;
                    break;
                case "??????????":
                case "Farshout":
                    districtCount = 355;
                    break;
                case "??????":
                case "Keft":
                    districtCount = 356;
                    break;
                case "??????":
                case "Quos":
                    districtCount = 357;
                    break;
                case "?????????? ??????":
                case "Qena City":
                    districtCount = 358;
                    break;
                case "?????? ??????????":
                case "Nag Hammadi":
                    districtCount = 359;
                    break;
                case "??????????":
                case "Nakada":
                    districtCount = 360;
                    break;
                case "?????????????? - ????????":
                case "Hurghada - Other":
                    districtCount = 361;
                    break;
                case "?????????????? - ????????????":
                case "Hurghada - Gouna":
                    districtCount = 362;
                    break;
                case "????????????":
                case "Qusair":
                    districtCount = 363;
                    break;
                case "?????? ????????":
                case "Ras Gharib":
                    districtCount = 364;
                    break;
                case "??????????":
                case "Safaga":
                    districtCount = 365;
                    break;
                case "?????? ????????":
                case "Sahl Hasheesh":
                    districtCount = 366;
                    break;
                case "????????????":
                case "Shalateen":
                    districtCount = 367;
                    break;
                case "???????? ??????":
                case "Marsa Alam":
                    districtCount = 368;
                    break;
                case "?????? ????????":
                case "Abu Hammad":
                    districtCount = 369;
                    break;
                case "?????? ????????":
                case "Abu Kabir":
                    districtCount = 370;
                    break;
                case "?????????? ??????":
                case "Awlad Saqr":
                    districtCount = 371;
                    break;
          /*  case "??????????????????????":
            case "Ibrahemyah":
                districtCount = 372;
                break;*/
                case "????????????????":
                case "Husseiniya":
                    districtCount = 373;
                    break;
                case "????????????????":
                case "Zagazig":
                    districtCount = 374;
                    break;
                case "???????????????? ??????????????":
                case "New Salhia":
                    districtCount = 375;
                    break;
           /* case "???????????? ???? ??????????":
            case "10th of Ramadan":
                districtCount = 376;
                break;*/
                case "????????????":
                case "Qareen":
                    districtCount = 377;
                    break;
                case "????????????????":
                case "Alqnayat":
                    districtCount = 378;
                    break;
                case "??????????":
                case "Bilbeis":
                    districtCount = 379;
                    break;
                case "???????? ??????":
                case "Deyerb Negm":
                    districtCount = 380;
                    break;
                case "??????????":
                case "Faqous":
                    districtCount = 381;
                    break;
                case "?????? ??????":
                case "Kafr Saqr":
                    districtCount = 382;
                    break;
                case "?????????? ??????????":
                case "Mashtool al-Souk":
                    districtCount = 383;
                    break;
                case "???????? ??????????":
                case "Minya al-Qamh":
                    districtCount = 384;
                    break;
                case "????????":
                case "Hihya":
                    districtCount = 385;
                    break;
                case "??????????":
                case "Akhmim":
                    districtCount = 386;
                    break;
                case "??????????????":
                case "Baliana":
                    districtCount = 387;
                    break;
                case "????????????????":
                case "Alasirat":
                    districtCount = 388;
                    break;
                case "??????????????":
                case "Maragha":
                    districtCount = 389;
                    break;
                case "??????????????":
                case "Monsha'a":
                    districtCount = 390;
                    break;
                case "????????":
                case "Girga":
                    districtCount = 391;
                    break;
                case "??????????":
                case "Juhaynah":
                    districtCount = 392;
                    break;
                case "??????????????????":
                case "Dar es-Salam":
                    districtCount = 393;
                    break;
                case "????????????":
                case "Sakaltah":
                    districtCount = 394;
                    break;
                case "??????":
                case "Tama":
                    districtCount = 395;
                    break;
                case "????????":
                case "Tahta":
                    districtCount = 396;
                    break;
                case "?????????? ??????????":
                case "Sohag City":
                    districtCount = 397;
                    break;
                case "?????? ????????":
                case "Abu Rudeis":
                    districtCount = 398;
                    break;
                case "?????? ??????????":
                case "Abu Zenimah":
                    districtCount = 399;
                    break;
                case "??????":
                case "Dahab":
                    districtCount = 400;
                    break;
                case "?????? ??????":
                case " Ras Sedr":
                    districtCount = 401;
                    break;
                case "?????? ??????":
                case "Ras Sidr":
                    districtCount = 402;
                    break;
                case "???????? ????????????":
                case "St. Catherine":
                    districtCount = 403;
                    break;
                case "?????? ??????????":
                case "Sharm al-Sheikh":
                    districtCount = 404;
                    break;
                case "????????":
                case "Taba":
                    districtCount = 405;
                    break;
                case "?????? ??????????":
                case "Tor Sinai":
                    districtCount = 406;
                    break;
                case "??????????":
                case "Nuweiba":
                    districtCount = 407;
                    break;
                case "?????????? ????????????":
                case "Ain Sukhna":
                    districtCount = 408;
                    break;
                case "???? ??????????????":
                case "Ganayen":
                    districtCount = 409;
                    break;
                case "???? ????????????????":
                case "Arbaeen":
                    districtCount = 410;
                    break;
                case "???? ????????????":
                case "Suez District":
                    districtCount = 411;
                    break;
                case "???? ??????????":
                case "Attaka":
                    districtCount = 412;
                    break;
           /* case "????????":
            case "Faisal":
                districtCount = 413;
                break;*/
                case "??????????????????":
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
