package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.JdbiTests;
import org.junit.Test;

public class DomainObjectRepositoryTest extends JdbiTests {

    private DomainObjectRepository unit;

    @Test
    public void test() {
        unit = new DomainObjectRepository(dbRule.getJdbi());

        System.out.println(unit.findById(1));
    }

}