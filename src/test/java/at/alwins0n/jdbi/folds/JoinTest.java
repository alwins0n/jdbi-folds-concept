package at.alwins0n.jdbi.folds;

import at.alwins0n.jdbi.folds.db.DomainObjectRow;
import at.alwins0n.jdbi.folds.db.SubObjectRow;
import at.alwins0n.jdbi.folds.domain.DomainObject;
import at.alwins0n.jdbi.folds.domain.SubObject;
import io.vavr.collection.*;
import lombok.val;
import org.jdbi.v3.core.generic.GenericType;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.rule.H2DatabaseRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.function.BiFunction;

public class JoinTest {

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

    @Test
    public void test() {
        // preserve ordering
        val type = new GenericType<LinkedHashMultimap<DomainObjectRow, SubObjectRow>>() {
        };

        final Multimap<DomainObjectRow, SubObjectRow> result = dbRule.getJdbi().withHandle(h -> h
                .registerRowMapper(ConstructorMapper.factory(DomainObjectRow.class))
                .registerRowMapper(ConstructorMapper.factory(SubObjectRow.class))
                .createQuery("select * from domain_objects do join sub_objects so " +
                        "on do.id = so.domain_object_id")
                .collectInto(type));

        val retVal = transform(result, this::toDomain);

        System.out.println(retVal);
    }

    private List<DomainObject> transform(Multimap<DomainObjectRow, SubObjectRow> result,
                                        BiFunction<DomainObjectRow, List<SubObjectRow>, DomainObject> fn) {
        return result.keySet()
                .map(k -> toDomain(k, result.getOrElse(k, List.empty()).toList()))
                .toList();
    }

    private DomainObject toDomain(DomainObjectRow row, List<SubObjectRow> subs) {
        return DomainObject.fromPersistence(row.getId(), row.getName(),
                subs.map(s -> new SubObject(s.getType())));
    }


}
