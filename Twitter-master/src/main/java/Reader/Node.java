package Reader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    All directories are considered as Nodes in a graph, Each node holds the file, its parent directory, its owners
    and its dependencies
 */


public class Node {
    List<String> owners = new ArrayList<>();
    List<String> dependencies = new ArrayList<>();
    File file;
    String parent;

    Node(String str){
        this.file = new File(str);
        this.parent = this.file.getParent();
    }

    // Based on the type of file extract the owners and the dependency for each node
    public void getOwnersAndDependencies(File file, HashMap<String, DataStore> files){
        if (file.getName().equals("OWNERS")) {
            readFileAndExtract(this.owners, file);
            updateOwners(files);
        } else if (file.getName().equals("DEPENDENCIES")) {
            readFileAndExtract(this.dependencies, file);
            updateDependencies(files);
        }

    }

    private void updateOwners(HashMap<String, DataStore> files) {
        DataStore dataStore;
        if (files.containsKey(this.file.getPath())){
            dataStore = files.get(this.file.getPath());
            dataStore.owners = this.owners;
        }
        else {
            dataStore = new DataStore(this.owners, new ArrayList<>(), this.parent);
            files.put(this.file.getPath(), dataStore);
        }
    }

    private void updateDependencies(HashMap<String, DataStore> files) {
        List<String> directDependency;
        while (this.dependencies.size() != 0){
            File dependency = new File(this.dependencies.remove(0));
            DataStore dataStore;
            if (!files.containsKey(dependency.getPath())){
                directDependency = new ArrayList<>();
                directDependency.add(this.file.getPath());
                dataStore = new DataStore(new ArrayList<>(), directDependency, this.parent);
                files.put(dependency.getPath(), dataStore);
            }
            else {
                dataStore = files.get(dependency.getPath());
                dataStore.dependencies.add(this.file.getPath());
                files.put(dependency.getPath(), dataStore);
            }
        }
    }

    // Read and extract the owners or dependency
    private void readFileAndExtract(List<String> list, File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                list.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
