package App;
import java.util.ArrayList;

public class QueryResponse {

    ArrayList<String> keys;
    ArrayList<String> values;

    public QueryResponse(ArrayList<String> keys, ArrayList<String> values) {
        this.keys = new ArrayList<>(keys);
        this.values = new ArrayList<>(values);
    }

    public int getLength(){ return keys.size();}

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }
}
