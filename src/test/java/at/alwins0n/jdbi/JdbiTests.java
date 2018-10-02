package at.alwins0n.jdbi;

import org.jdbi.v3.core.rule.H2DatabaseRule;
import org.junit.Before;
import org.junit.Rule;

public class JdbiTests {
    @Rule
    public H2DatabaseRule dbRule = new H2DatabaseRule().withPlugins();

    @Before
    public void initData() {
        dbRule.getSharedHandle()
                .execute("create table domain_objects (id identity primary key, name varchar(50))");
        dbRule.getSharedHandle()
                .execute("create table sub_objects (domain_object_id int, type varchar(50))");

        dbRule.getSharedHandle().execute("insert into domain_objects values (1, 'Bla')");
        dbRule.getSharedHandle().execute("insert into sub_objects values (1, 'A')");
        dbRule.getSharedHandle().execute("insert into sub_objects values (1, 'B')");
    }
}
