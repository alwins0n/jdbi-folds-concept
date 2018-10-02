package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.folds.domain.DomainObject;
import at.alwins0n.jdbi.folds.domain.SubObject;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jdbi.v3.core.Jdbi;

@AllArgsConstructor
public class DomainObjectRepository {

    private final Jdbi jdbi;

    static DomainObject toDomain(DomainObjectRow row, List<SubObjectRow> subs) {
        return DomainObject.fromPersistence(row.getId(), row.getName(),
                subs.map(SubObjectRow::getType).map(SubObject::new));
    }

    public DomainObject findById(int id) {
        val row = JdbiHelper.getRowById(jdbi, id, DomainObjectRow.class, "domain_objects");
        val subObjectRows = JdbiHelper.getManyRowsById(jdbi, id, SubObjectRow.class,
                "sub_objects", "domain_object_id");

        return toDomain(row, subObjectRows);
    }

}
