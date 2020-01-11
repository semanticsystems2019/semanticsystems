package group18;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class RedditParser {

    public ArrayList<RedditPost> redditPostArrayList;

    RedditParser() throws IOException, ParseException {
        this.redditPostArrayList = new ArrayList<>();

        for (String file : this.FILEPATHS){
            RedditPost redditPost = createRedditPost(this.loadJSON(file));
            redditPostArrayList.add(redditPost);
        }
    }

    public static final List<String> FILEPATHS = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("data/reddit/EDIT_a_quiet_place_2018_official_teaser_trailer.json");
                add("data/reddit/EDIT_a_star_is_born_official_trailer_bradley_cooper.json");
                //add("data/reddit/bohemian_rhapsody_official_trailer.json");
                //add("data/reddit/crazy_rich_asians_official_trailer.json");
                add("data/reddit/EDIT_deadpool_2_official_trailer.json");
                //add("data/reddit/fifty_shades_freed_official_trailer_hd_jamie.json");
                add("data/reddit/EDIT_incredibles_2_new_official_trailer.json");
                add("data/reddit/EDIT_joker_final_trailer.json");
                add("data/reddit/EDIT_mamma_mia_here_we_go_again_trailer.json");
                //add("data/reddit/marvel_studios_avengers_endgame_official_trailer.json");
                add("data/reddit/EDIT_vice_official_trailer.json");
                add("data/reddit/EDIT_zombieland_double_tap_official_trailer_hd.json");
            }});

    JSONObject loadJSON(String path) throws IOException, ParseException {
        File file = new File(path);
        String content = FileUtils.readFileToString(file, "utf-8");
        return new JSONObject(content);
    }

    RedditPost createRedditPost(JSONObject data) throws IOException, ParseException {
        RedditPost redditPost = new RedditPost();

        // SET TOP LEVEL POST FIELDS
        redditPost.setTopLevelPost(true);
        redditPost.setId((String) data.toMap().get("id"));
        redditPost.setTitle((String) data.toMap().get("title"));
        redditPost.setUpvotes((Integer) data.toMap().get("score"));
        try {
            redditPost.setDate((int) data.toMap().get("date"));
        } catch (ClassCastException e) {
            redditPost.setDate((int)Math.round((Double) data.toMap().get("date")));
        }
        redditPost.setUsername((String) data.toMap().get("created_by"));

        // SET COMMENTS
        ArrayList<RedditPost> commentPostArrayList = new ArrayList<>();
        ArrayList<Object> comments = (ArrayList<Object>) data.toMap().get("comments");
        for (Object com : comments){
            RedditPost commentPost = new RedditPost();
            commentPost.setId((String)((HashMap) com).get("id"));
            commentPost.setTitle((String)((HashMap) com).get("title"));
            try {
                commentPost.setUpvotes((int)((HashMap) com).get("score"));
            } catch (NullPointerException e ){
                commentPost.setUpvotes(0);
            }
            try {
                commentPost.setDate((int)((HashMap) com).get("date"));
            } catch (ClassCastException e) {
                commentPost.setDate((int)Math.round(((double)((HashMap) com).get("date"))));
            } catch (Exception e) {
                commentPost.setDate(0);
            }
            commentPost.setUsername((String)((HashMap) com).get("created_by"));
            commentPost.setText((String)((HashMap) com).get("text"));
            String rawParentId = (String)((HashMap) com).get("parent_id");
            try {
                String editedParentId = rawParentId.replaceAll("t\\d_", "");
                commentPost.setParentId(editedParentId);
            } catch (Exception e) {
                commentPost.setParentId(rawParentId);
            }
            commentPostArrayList.add(commentPost);
        }
        redditPost.setComments(commentPostArrayList);
        return redditPost;
    }
}
