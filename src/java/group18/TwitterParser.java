package group18;

import com.fasterxml.jackson.core.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *	?tweet a movies:Tweet ;
 * 		movies:__-_id ?id ;
 * 		movies:__-_text ?text ;
 * 		movies:__-_lang ?lang ;
 * 		movies:__-_source ?source ;
 * 		movies:__-_created_at ?created_at ;
 * 		movies:__-_retweet_count ?retweet_count ;
 * 		movies:__-_favorite_count ?favorite_count ;
 * 		movies:__-_in_reply_to_user_id ?in_reply_to_user_id ;
 * 		movies:__-_in_reply_to_status_id ?in_reply_to_status_id ;
 * 		movies:__-_in_reply_to_user_id_str ?in_reply_to_user_id_str ;
 * 		movies:__-_in_reply_to_screen_name ?in_reply_to_screen_name ;
 * 		movies:__-_user_-_id ?user_id ;
 * 		movies:__-_user_-_screen_name ?user_name .
 * */

public class TwitterParser{

    public JSONArray parser() throws IOException, ParseException {

        JSONArray ret = new JSONArray();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data/twitter/all.json"));
        JSONArray fullJSON = new JSONArray(obj.toString());

        for (int i = 0; i < fullJSON.length(); i++) {

            JSONObject currJSON = fullJSON.getJSONObject(i);
            JSONObject reducedJSON = new JSONObject();
            reducedJSON.put("id", currJSON.get("id"));
            reducedJSON.put("")

            ret.put(reducedJSON);
        }

        System.out.println(ret.toString());
        return ret;
    }

}