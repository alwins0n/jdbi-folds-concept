package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.JdbiTests;
import at.alwins0n.jdbi.folds.domain.DomainObject;
import io.vavr.collection.LinkedHashMultimap;
import io.vavr.collection.List;
import io.vavr.collection.Multimap;
import lombok.val;
import org.jdbi.v3.core.generic.GenericType;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.junit.Test;

import java.util.function.BiFunction;

public class JoinTest extends JdbiTests {

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

        val retVal = transform(result, DomainObjectRepository::toDomain);

        System.out.println(retVal);
    }

    private List<DomainObject> transform(Multimap<DomainObjectRow, SubObjectRow> result,
                                         BiFunction<DomainObjectRow, List<SubObjectRow>, DomainObject> fn) {
        return result.keySet()
                .map(k -> fn.apply(k, result.getOrElse(k, List.empty()).toList()))
                .toList();
    }



}
