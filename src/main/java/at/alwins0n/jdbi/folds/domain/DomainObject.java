package at.alwins0n.jdbi.folds.domain;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(staticName = "fromPersistence")
@ToString
public class DomainObject {
    private final int id;
    private String name;
    private List<SubObject> subs;

    public void changeName(String name) {
        this.name = name;
    }

    public void addSub(SubObject sub) {
        subs = subs.prepend(sub);
    }

}
