package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.folds.domain.DomainObject;
import at.alwins0n.jdbi.folds.domain.SubObject;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.generic.GenericType;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;

@AllArgsConstructor
public class DomainObjectRepository {

    private final Jdbi jdbi;

    static DomainObject toDomain(DomainObjectRow row, List<SubObjectRow> subs) {
        return DomainObject.fromPersistence(row.getId(), row.getName(),
                subs.map(s -> new SubObject(s.getType())));
    }

    public DomainObject findById(int id) {
        final DomainObjectRow domainObjectRow = jdbi.withHandle(h -> h
                .createQuery("select * from domain_objects where id = :id")
                .bind("id", id)
                .map(ConstructorMapper.of(DomainObjectRow.class))
                .findOnly());

        final List<SubObjectRow> subObjectRows = jdbi.withHandle(h -> h
                .registerRowMapper(ConstructorMapper.factory(SubObjectRow.class))
                .createQuery("select * from sub_objects where domain_object_id = :id")
                .bind("id", id)
                .collectInto(new GenericType<List<SubObjectRow>>() {
                }));

        return toDomain(domainObjectRow, subObjectRows);
    }

}
