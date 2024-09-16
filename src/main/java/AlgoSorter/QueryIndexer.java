package AlgoSorter;

import java.util.ArrayList;

/**
 * Obtain user query (from constructor), use fuzzy search to index CSV file and return list of ALL classes that the user inputted
 * @see QueryIndexer for the actual implementation of
 * return Arraylist<Arraylist<classes>>, where classes may be string s
 */
public class QueryIndexer {
    ArrayList<String> usr_query;
    public QueryIndexer(ArrayList al) {
        al = usr_query;
    }


    /**
     * Do NOT create a setter for usr_query (w/o good reason)
     * @return @{@link #usr_query}
     */
    public ArrayList<String> getUsr_query() {
        return usr_query;
    }
}