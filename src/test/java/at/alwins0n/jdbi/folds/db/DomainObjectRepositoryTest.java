package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.JdbiTests;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DomainObjectRepositoryTest extends JdbiTests {

    private DomainObjectRepository unit;

    @Before
    public void setUp() {
        unit = new DomainObjectRepository(dbRule.getJdbi());
    }

    @Test
    public void test() {
        assertNotNull(unit.findById(1));
    }

    @Test
    public void testMany() {
        assertEquals(0, unit.findByName("asdf").size());
        assertEquals(1, unit.findByName("Bla").size());
    }

}