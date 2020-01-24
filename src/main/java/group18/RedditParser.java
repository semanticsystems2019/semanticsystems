package group18;

import org.apache.commons.io.FileUtils;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class RedditParser {
    public ArrayList<RedditPost> redditPostArrayList;
    private Repository repo;
    Map<String, IRI> iris;

    RedditParser(Repository repo, Map<String, IRI> iris) throws IOException, ParseException {
        this.redditPostArrayList = new ArrayList<>();
        for (int i=0; i<this.FILEPATHS.size(); i++){
            RedditPost redditPost = createRedditPost(this.loadJSON(this.FILEPATHS.get(i)));
            redditPost.setReferenceMovie(this.MOVIENAMES.get(i));
            redditPostArrayList.add(redditPost);
        }
        this.repo = repo;
        this.iris = iris;
    }

    public static final List<String> FILEPATHS = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("src/main/resources/reddit/EDIT_a_quiet_place_2018_official_teaser_trailer.json");
                add("src/main/resources/reddit/EDIT_a_star_is_born_official_trailer_bradley_cooper.json");
                //add("src/main/resources/reddit/bohemian_rhapsody_official_trailer.json");
                //add("src/main/resources/reddit/crazy_rich_asians_official_trailer.json");
                add("src/main/resources/reddit/EDIT_deadpool_2_official_trailer.json");
                //add("src/main/resources/reddit/fifty_shades_freed_official_trailer_hd_jamie.json");
                add("src/main/resources/reddit/EDIT_incredibles_2_new_official_trailer.json");
                add("src/main/resources/reddit/EDIT_joker_final_trailer.json");
                add("src/main/resources/reddit/EDIT_mamma_mia_here_we_go_again_trailer.json");
                //add("src/main/resources/reddit/marvel_studios_avengers_endgame_official_trailer.json");
                add("src/main/resources/reddit/EDIT_vice_official_trailer.json");
                add("src/main/resources/reddit/EDIT_zombieland_double_tap_official_trailer_hd.json");
            }});

    public static final List<String> MOVIENAMES = Collections.unmodifiableList(
            new ArrayList<String>() {{
               add("A quiet place");
                add("A star is born");
                add("Deadpool 2");
                add("Incredibles 2");
                add("Joker");
                add("Mamma mia here we go again");
                add("Vice");
                add("Zombieland");
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
            commentPost.setReferenceMovie(redditPost.getReferenceMovie());
            commentPostArrayList.add(commentPost);
        }
        redditPost.setComments(commentPostArrayList);
        return redditPost;
    }

    void uploadRedditPosts(){
        ValueFactory valueFactory = repo.getValueFactory();
        int counter = 0;
        int commentCounter = 0;
        try {
            RepositoryConnection conn = repo.getConnection();
            for (RedditPost post : redditPostArrayList)  {

                // ADDING POSTS
                IRI postIRI = valueFactory.createIRI(Util.NS, "reddit/post#" + (counter++));
                conn.add(postIRI, RDF.TYPE, iris.get("RedditPost"));
                conn.add(postIRI, iris.get("hasId"), valueFactory.createLiteral( post.getId() ));
                conn.add(postIRI, iris.get("hasTitle"), valueFactory.createLiteral( post.getTitle() ));
                conn.add(postIRI, iris.get("createdBy"), valueFactory.createLiteral( post.getUsername() ));
                conn.add(postIRI, iris.get("hasEmotion"), valueFactory.createLiteral( post.getEmotion() ));
                conn.add(postIRI, iris.get("hasSource"), valueFactory.createLiteral( post.getSource() ));
                conn.add(postIRI, iris.get("refersToMovie"), valueFactory.createLiteral( post.getReferenceMovie() ));
                conn.add(postIRI, iris.get("hasDate"), valueFactory.createLiteral( post.getDate() ));
                conn.add(postIRI, iris.get("hasLikes"), valueFactory.createLiteral( post.getUpvotes() ));

                // ADDING POST USER

                // ADDING COMMENTS
                for (RedditPost comment : post.getComments()){
                    IRI commentIRI = valueFactory.createIRI(Util.NS, "reddit/comment#" + (commentCounter++));
                    conn.add(commentIRI, RDF.TYPE, iris.get("Comment"));
                    if (comment.getId() == null) break;
                    conn.add(commentIRI, iris.get("hasId"), valueFactory.createLiteral( comment.getId() ));
                    conn.add(commentIRI, iris.get("isCommentOn"), valueFactory.createLiteral( comment.getParentId() ));
                    conn.add(commentIRI, iris.get("createdBy"), valueFactory.createLiteral( comment.getUsername() ));
                    conn.add(commentIRI, iris.get("hasEmotion"), valueFactory.createLiteral( comment.getEmotion() ));
                    conn.add(commentIRI, iris.get("hasSource"), valueFactory.createLiteral( comment.getSource() ));
                    conn.add(commentIRI, iris.get("hasDate"), valueFactory.createLiteral( comment.getDate() ));
                    conn.add(commentIRI, iris.get("hasLikes"), valueFactory.createLiteral( comment.getUpvotes() ));
                    conn.add(commentIRI, iris.get("hasText"), valueFactory.createLiteral( comment.getText() ));
                    // conn.add(commentIRI, iris.get("refersToMovie"), valueFactory.createLiteral( comment.getReferenceMovie() ));

                    // ADDING COMMENT USER
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
