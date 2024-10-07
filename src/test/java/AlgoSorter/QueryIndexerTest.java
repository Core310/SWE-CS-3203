package AlgoSorter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryIndexerTest{
    ArrayList stub = new ArrayList();
    QueryIndexer qi = new QueryIndexer(stub);
    String testInp;

    @Test
    void getUsr_query() {
        String expectedOutput = "stub";
        assertEquals(expectedOutput,"b");
    }

    @Test
    void testGetUsr_queryOnNewValue() {
        testInp = "stub";
        assertTrue(qi.updateOneQuery(testInp));
    }

    /**
     * Should return false
     */
    @Test
    void testGetUsr_queryOnDuplicateValue(){
        assertFalse(qi.updateOneQuery(testInp));
    }
}