package at.alwins0n.jdbi.folds.db;

import at.alwins0n.jdbi.folds.domain.DomainObject;
import at.alwins0n.jdbi.folds.domain.SubObject;
import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.val;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import static at.alwins0n.jdbi.folds.db.JdbiHelper.getRowById;
import static at.alwins0n.jdbi.folds.db.JdbiHelper.getRowsBy;

@AllArgsConstructor
public class DomainObjectRepository {

    private final Jdbi jdbi;

    static DomainObject toDomain(DomainObjectRow row, List<SubObjectRow> subs) {
        return DomainObject.fromPersistence(row.getId(), row.getName(),
                subs.map(SubObjectRow::getType).map(SubObject::new));
    }

    public DomainObject findById(int id) {
        try (Handle h = jdbi.open()) {
            val row = getRowById(h, id, DomainObjectRow.class, "domain_objects");
            val subObjectRows = getSubObjectsByJoinId(h, id);

            return toDomain(row, subObjectRows);
        }
    }

    public List<DomainObject> findByName(String name) {
        try (Handle h = jdbi.open()) {
            val rows = getRowsBy(h, name, DomainObjectRow.class, "domain_objects", "name");
            return rows.map(r -> toDomain(r, getSubObjectsByJoinId(h, r.getId())));
        }
    }

    private List<SubObjectRow> getSubObjectsByJoinId(Handle h, int id) {
        return getRowsBy(h, id, SubObjectRow.class, "sub_objects", "domain_object_id");
    }

}
