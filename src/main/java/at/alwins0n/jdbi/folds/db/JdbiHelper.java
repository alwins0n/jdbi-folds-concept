package at.alwins0n.jdbi.folds.db;

import io.vavr.collection.List;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

public class JdbiHelper {

    static <T> T getRowById(Jdbi jdbi, int id, Class<T> rowClass, String table) {
        return jdbi.withHandle(h -> h
                .createQuery("select * from " + table + " where id = :id")
                .bind("id", id)
                .map(ConstructorMapper.of(rowClass))
                .findOnly());
    }

    static <T> List<T> getManyRowsById(Jdbi jdbi, int id, Class<T> rowClass, String table, String joinIdProperty) {
        return jdbi.withHandle(h -> h
                .createQuery("select * from " + table + " where " + joinIdProperty + " = :id")
                .bind("id", id)
                .map(ConstructorMapper.of(rowClass))
                .collect(List.collector()));
    }

}
