package com.gamesexchange.gamesexchange.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {
   /* public static final String LINODE_SERVER_BASE = "http://139.162.150.233/rel/";
    public static final String SERVER_BASE = "http://l3byyy.000webhostapp.com/rel/";
*/
//   public static final String SERVER_BASE = "http://139.162.150.233/rel/";
   public static final String SERVER_BASE = "http://l3byyy.000webhostapp.com/rel/";



    public static final String SERVER_GET_API_KEY = "?api_key=com_trade_api_key";
    public static final String SERVER_AND_GET_API_KEY = "&api_key=com_trade_api_key";

//        public static final String SERVER_BASE = "http://192.168.1.4/";
    public static final String DB_GAME_NAME = "my_wished_game_name";
    public static final String DB_MY_GAME_NAME = "my_game_name";
    public static final String DB_MY_USER_ID = "my_user_id";
    public static final String DB_COMMON_USER_ID = "common_user_id";
    public static final String DB_COMMON_USER_NAME = "game_owner_name";
    public static final String DB_COMMON_USER_LOCATION = "game_owner_location";
    public static final String DB_COMMOM_USER_DISTRICT = "game_owner_district";
    public static final DatabaseReference FIREBASE_DB_BASE = FirebaseDatabase.getInstance().getReference();
    public static final String FIREBASE_DB_GAME_POINTS = "gp";
    public static final String FIREBASE_DB_WISH_POINTS = "wp";
    public static final int GAMES_COUNT = 15729;
    public static final String INTENT_COMMON_USER_NAME = "intent_common_user_name";
    public static final String INTENT_COMMON_USER_LOCATION = "intent_common_user_location";
    public static final String INTENT_COMMON_USER_GAMES_YOU_WANT = "intent_common_user_games_you_want";
    public static final String INTENT_COMMON_USER_GAMES_YOU_OWN = "intent_common_user_games_you_own";
    public static final String INTENT_COMMON_USER_ID = "intent_common_user_id";

    public static final String ACTIVE = "active";
    public static final String INACTIVE = "inactive";
    public static final String FIREBASE_DB_ROOMS_IDS = "room_ids";
    public static final String FIREBASE_DB_MESSAGES = "messages";
    public static final String INTENT_USER_ID = "intent_user_id";
    public static final String INTENT_SENDER_NAME = "intent_sender_id";
    public static final String INTENT_ROOM_ID = "intent_room_id";
    public static final String FIREBASE_DB_TOKENS = "tokens";
    public static final String INTENT_RECEIVER_NAME = "intent_receiver_name";
    public static final String INTENT_RECIEVER_ID = "intent_receiver_id";
    public static final String FIREBASE_DB_ONLINE_STATUS = "status";
    public static final int FIREBASE_DB_ONLINE_STATUS_IS_ONLINE = 1;
    public static final int FIREBASE_DB_ONLINE_STATUS_IS_OFFLINE = 0;
    public static final String SHARED_PREF_ONLINE_STATUS = "online_status";
    public static final String FIREBASE_DB_ROOMS_WITH = "rooms_with";

    public static final String FIREBASE_DB_PROMO = "promo";
    public static final String DB_COMMON_USER_ONLINE_STATUS = "game_owner_online_status";
    public static final String FIREBASE_DB_REPORTS = "reports";
    public static final int ADDITION_OF_GAMES_COST = 10;
    public static final String SHARED_PREF_KEY_LAST_COLLECTED = "last_collected";
    public static final String SHARED_PREF_DAILY_LOGIN = "shared_pref_daily_login";
    public static final String SHARED_PREF_KEY_ACCUMLATED_DAYS = "accumlated_days";
    public static final String APP_URL = "https://bit.ly/2CQBxqg";
    public static final int TRADE_REFRESH_TIME = 200;
    public static final String SHARED_PREF_MESSAGES = "new_messages";
    public static final String SHARED_PREF_MESSAGING_READ = "read";
    public static final String SHARED_PREF_MESSAGING_UNREAD = "unread";
    public static final String INTENT_DIFFERENT_USER_NAME = "d_name";
    public static final String INTENT_DIFFERENT_USER_LOCATION = "d_loc";
    public static final String INTENT_DIFFERENT_USER_ID = "d_uid";
    public static final String INTENT_DIFFERENT_GAMES = "d_games";
    public static final String INTENT_PROFILE_NAME = "p_name";
    public static final String INTENT_PROFILE_USER_ID = "p_uid";
    public static final String INTENT_DIFFERENT_USER_DISTRICT = "d_district";
    public static final String ALREADY_COLLECTED = "2";
    public static final String DAILY_LOGIN = "1";
    public static final String FIRST_LOGIN = "0";
    public static final String NO_PROFILE_IMAGE_FOUND = "no_image_found";
    public static final String INTENT_RECIEVER_PHOTO = "intent_reciever_photo";
    public static final String INTENT_RECEIVER_FIRST_NAME = "intent_rec_first_name";
    public static final String INTENT_RECEIVER_LAST_NAME = "intent_rec_last_name";
    public static final String INTENT_PROFILE_PHOTO = "intent_profile_photo";
    public static final String INTENT_PROFILE_FIRST_NAME = "intent_profile_first_name";
    public static final String INTENT_PROFILE_LAST_NAME = "intent_profile_last_name";
    public static final String INTENT_RECIEVER_TOKEN = "intent_rec_token";
    public static final String AMAZON_PROFILE_P = "p";


    public static final String USER_STATE_DOES_NOT_EXIST = "DOES_NOT_EXIST";
    public static final String USER_STATE_DOES_EXIST = "DOES_EXIST";
    public static final String USER_STATE_NEED_UPDATE = "NEED_UPDATE";
    public static final String USER_STATE_UNDER_MAINTENANCE = "UNDER_MAINTENANCE";

    public static final String SHARED_PREF_USER_DATA = "pref_user_data";
    public static final String ERROR_FREE = "no_error";
    public static final int TRADE_LIMIT = 2500;
    public static final long DEFAULT_LAST_CLICKED = 20000;
    public static long DEFAULT_DIFFERENCE_LAST_CLICKED = 30;


    public  class INTENTS {




        public static final String INTENT_DIFFERENT_USER_NAME = "d_name";
        public static final String INTENT_DIFFERENT_USER_LOCATION = "d_loc";
        public static final String INTENT_DIFFERENT_USER_ID = "d_uid";
        public static final String INTENT_DIFFERENT_GAMES = "d_games";
        public static final String INTENT_DIFFERENT_USER_DISTRICT = "d_district";
        public static final String INTENT_DIFFERENT_USER_PROFILE_IMAGE = "intent_profile_image";
        public static final String INTENT_DIFFERENT_USER_FIRST_NAME = "d_first_name";
        public static final String INTENT_DIFFERENT_USER_LAST_NAME = "d_last_name";
        public static final String INTENT_DIFFERENT_USER_GAMES = "d_user_games";
        public static final String INTENT_DIFFERENT_USER_WISHES = "d_user_wishes";
        public static final String INTENT_DIFFERENT_USER_TOKEN = "d_user_token";
    }


    public class SQL_DATABASE {

        public static final String DB_GAME_NAME = "my_wished_game_name";
        public static final String DB_MY_USER_ID = "my_user_id";
        public static final String DB_COMMON_USER_ID = "common_user_id";
        public static final String DB_COMMON_USER_FIRST_NAME = "game_owner_first_name";
        public static final String DB_COMMON_USER_LAST_NAME = "game_owner_last_name";
        public static final String DB_COMMON_USER_LOCATION = "game_owner_location";
        public static final String DB_COMMOM_USER_DISTRICT = "game_owner_district";
        public static final String DB_COMMOM_USER_PROFILE_IMAGE = "game_owner_profile_image";
        public static final String DB_COMMON_USER_ONLINE_STATUS = "game_owner_online_status";
        public static final String DB_COMMON_USER_GAMES = "game_owner_games";
        public static final String DB_COMMON_USER_WISHES = "game_owner_wishes";
        public static final String DB_COMMOM_USER_TOKEN = "game_owner_token";
        public static final String GAME_NAME = "game_name";
        public static final String FLAG = "flag";
        public static final String INSERTED = "inserted";
        public static final String ROOM_ID = "room_id";
        public static final String ROOM_RECIEVER_ID = "room_reciever_id";
    }

}


