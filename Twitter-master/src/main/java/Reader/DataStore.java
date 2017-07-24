package Reader;

import java.util.List;

/*
    A POJO to hold data to write into the json
 */

public class DataStore {
    List<String> owners;
    List<String> dependencies;
    String parent;

    public DataStore(List<String> owners, List<String> dependencies, String parent) {
        this.owners = owners;
        this.dependencies = dependencies;
        this.parent = parent;
    }
}
