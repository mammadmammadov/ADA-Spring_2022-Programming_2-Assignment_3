import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    static int indentations = 0;

    public static void main(String... args) throws IOException, InterruptedException {

        File dest = new File("dest");

        //creating the directory "dest"
        if (!dest.exists()) {
            dest.mkdir();
        }

        //putting all paths to an array of File
        int length = args.length;
        File[] allFiles = new File[length];
        int cnt = 0;
        while (length-- > 0) {
            allFiles[cnt] = new File(args[cnt++]);
        }

        //in case the last element is "dest", deleting it by converting the array to list
        List<File> listOfAllFiles = new ArrayList<>();
        Collections.addAll(listOfAllFiles, allFiles);

        //part shows part1 or part2 of the problem
        int part = 1;
        if (args[cnt - 1].equals("dest")) {
            listOfAllFiles.remove(listOfAllFiles.size() - 1);
            part++;
        }

        System.out.println("Hello, dear user! Right now your files and/or directories are being copied to the folder named \"dest\".");
        Thread.sleep(750);
        System.out.println("We'll let you know if something wrong happens.");
        Thread.sleep(750);
        System.out.println("Enjoy the process!");
        Thread.sleep(1500);
        System.out.println();
        for (File singleFileInSource : listOfAllFiles) {
            if (!singleFileInSource.exists()) {
                System.out.println(singleFileInSource + " DOES NOT EXIST IN THE SPECIFIED DIRECTORY!");
            } else {
                if (part == 1) {

                    //creating all files in "dest" (according to the instructions); first part of the problem
                    if (singleFileInSource.isDirectory()) {
                        System.out.println(singleFileInSource + " IS DIRECTORY, IT IS NOT COPIED!");
                    } else {
                        File destFile = new File(dest, singleFileInSource.getName());
                        try {
                            destFile.createNewFile();

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        copyFile(singleFileInSource, destFile);
                        System.out.println("\t>Started: " + singleFileInSource);
                        System.out.println("\t>Finished FILE: " + singleFileInSource);
                        System.out.println("\t>Total: " + sizeOfFile(singleFileInSource.length()) + " were copied!");
                    }
                } else {
                    if (singleFileInSource.isDirectory()) {
                        File directory = new File(dest, singleFileInSource.getName());
                        directory.mkdir();
                        copyDirectory(singleFileInSource, directory);
                    } else {
                        File destFile = new File(dest, singleFileInSource.getName());
                        destFile.createNewFile();
                        copyFile(singleFileInSource, destFile);
                        System.out.println("\t>Started: " + singleFileInSource);
                        System.out.println("\t>Finished FILE: " + singleFileInSource);
                        System.out.println("\t>Total: " + sizeOfFile(singleFileInSource.length()) + " were copied!");
                    }
                }
            }
        }
    }

    public static void copyFile(File source, File destination) {
        try (FileInputStream input = new FileInputStream(source)) {
            FileOutputStream output = new FileOutputStream(destination);
            int result;
            byte[] bytes = new byte[1024];
            while ((result = input.read(bytes)) != -1) {
                output.write(bytes, 0, result);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void copyDirectory(File source, File destination) {
        if (source.isDirectory()) {
            ArrayList<String> files = new ArrayList<String>();
            Collections.addAll(files, source.list());
            destination.mkdirs();
            indentations++;
            for (int i = 0; i < indentations; i++) {
                System.out.print("\t");
            }
            System.out.println(">Started: " + source + "...");
            boolean finishedAllFilesInDirectory = false;
            for (String file : files) {
                File fileInDestination = new File(destination, file);
                File fileInSource = new File(source, file);
                //recursively copying
                copyDirectory(fileInSource, fileInDestination);
                finishedAllFilesInDirectory = true;
            }
            if (finishedAllFilesInDirectory) {
                indentations--;
                for (int i = 0; i < indentations; i++) {
                    System.out.print("\t");
                }
                System.out.println("\t>Finished FOLDER: " + source);
            }
        } else {
            String empty = "";
            for (int i = 0; i < indentations; i++) {
                empty += "\t";
            }
            copyFile(source, destination);
            System.out.println(empty + "\t>Started: " + source + "...");
            System.out.println(empty + "\t>Finished FILE: " + source + "...");
            System.out.println(empty + "\t>Total " + sizeOfFile(source.length()) + " were copied!");

        }

    }

    public static String sizeOfFile(long bytes) {
        if (bytes < 1024) {
            return bytes + "B";
        } else if (bytes < Math.pow(1024, 2)) {
            return (int) Math.ceil(bytes / 1024.0) + "KB";
        } else if (bytes < Math.pow(1024, 3)) {
            return (int) Math.ceil(bytes / (1024 * 1024.0)) + "MB";
        } else if (bytes < Math.pow(1024, 4)) {
            return (int) Math.ceil(bytes / (1024 * 1024 * 1024.0)) + "GB";
        }
        return null;
    }
}

