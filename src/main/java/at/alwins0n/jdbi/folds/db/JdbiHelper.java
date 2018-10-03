package at.alwins0n.jdbi.folds.db;

import io.vavr.collection.List;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.result.ResultIterable;

public class JdbiHelper {

    static <T, K> T getRowById(Handle h, K id, Class<T> rowClass, String table) {
        return getRowBy(h, id, rowClass, table, "id");
    }

    static <T, K> T getRowBy(Handle h, K id, Class<T> rowClass, String table, String byColumn) {
        return getByProperty(h, id, rowClass, table, byColumn).findOnly();
    }

    static <T, K> List<T> getRowsBy(Handle h, K id, Class<T> rowClass, String table, String byColumn) {
        return getByProperty(h, id, rowClass, table, byColumn).collect(List.collector());
    }

    private static <T, K> ResultIterable<T> getByProperty(Handle h, K id, Class<T> rowClass, String table, String idColumn) {
        return h.createQuery("select * from " + table + " where " + idColumn + " = :id")
                .bind("id", id)
                .map(ConstructorMapper.of(rowClass));
    }

}
