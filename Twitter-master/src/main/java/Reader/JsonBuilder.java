package Reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
    This module is responsible for writing the json based on all the nodes that have been read
 */

public class JsonBuilder {
    private static void buildJson(HashMap<String, DataStore> files) throws IOException {
        Iterator itr = files.entrySet().iterator();
        try {
            FileWriter writer = new FileWriter("dependencyGraph.json");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write("{\n");
            while (itr.hasNext()) {
                Map.Entry<String, DataStore> pair = (Map.Entry) itr.next();
                bufferedWriter.write("\"" + replacer(pair.getKey()) + "\" : {\n");
                bufferedWriter.write("\"parent\" : \"" + replacer(pair.getValue().parent) + "\",\n");
                bufferedWriter.write("\"owners\" :  " + replacer(getValues(pair.getValue().owners)) + ", \n");
                bufferedWriter.write("\"dependencies\" :  " + replacer(getValues(pair.getValue().dependencies)) + "\n");
                if (itr.hasNext())
                    bufferedWriter.write("},\n");
                else
                    bufferedWriter.write("}\n");
            }
            bufferedWriter.write("}");
            bufferedWriter.close();
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String replacer(String value){
        if (value == null)
            return "";
        if (value.contains("\\")){
            return value.replaceAll("\\\\", "/");
        }
        return value;
    }

    private static String getValues(List<String> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < list.size(); i++) {
            builder.append("\"").append(list.get(i)).append("\"");
            if (i != list.size() - 1)
                builder.append(",");
        }
        builder.append("]");
        return builder.toString();
    }

    public static void listf(Node root, HashMap<String, DataStore> files) {
        File directory = root.file;
        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                root.getOwnersAndDependencies(file, files);
            } else if (file.isDirectory()) {
                Node child = new Node(file.getPath());
                if (!files.containsKey(child.file.getPath()))
                    files.put(child.file.getPath(), new DataStore(new ArrayList<>(), new ArrayList<>(),
                            child.file.getParent()));
                listf(child, files);
            }
        }
    }

    public void build(Node root, HashMap<String, DataStore> files) {
        listf(root, files);
        try {
            buildJson(files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
