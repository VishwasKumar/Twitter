package Reader;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/*
    The main program to read the directories in the repo, extract the owners and dependencies and build a json file
    with this structure
 */
public class Reader {
    public static void main(String[] args) throws IOException {
        HashMap<String, DataStore> files = new HashMap<>();
        Node root = new Node("src");
        files.put(root.file.getPath(), new DataStore(new ArrayList<>(), new ArrayList<>(), root.file.getParent()));
        JsonBuilder builder = new JsonBuilder();
        builder.build(root, files);
    }
}
