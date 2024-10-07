package AlgoSorter;

import java.util.ArrayList;

/**
 * Obtain user query (from constructor), use fuzzy search to index CSV file and return list of ALL classes that the user inputted
 * @see QueryIndexer for the actual implementation of
 * return Arraylist<Arraylist<classes>>, where classes may be string s
 */

public class QueryIndexer {
    @lombok.Getter
    ArrayList<String> usr_query;

    /**
     * Input constructor for usr query
     * @param al
     */
    public QueryIndexer(ArrayList al) {
        al = usr_query;
    }

    /**
     *
     * @param query
     * @return true if inner class userQuery arraylist has no duplicates
     */
    public boolean updateOneQuery(String query){
        if (usr_query.contains(query)) return  false;
        usr_query.add(query);
        return true;
    }


}