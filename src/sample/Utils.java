package sample;

import java.io.File;
import java.util.concurrent.Semaphore;

public class Utils {
    private final static int threadsLimit = 3;
    private static final Semaphore semaphore = new Semaphore(threadsLimit);

    static public int listFilesForFolderWithSemaphore(final File folder){
        int numOfFiles = -1;
        try {
//            System.out.println("waiting for semaphore");
            semaphore.acquire();
//            System.out.println("running");
            numOfFiles = listFilesForFolder(folder);
//            System.out.println("releasing");
            semaphore.release();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        return numOfFiles;
    }

    static private int listFilesForFolder(final File folder) {
        int numOfFiles = 0;
        if (folder.listFiles() == null){
            return 0;
        }
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                numOfFiles += listFilesForFolder(fileEntry);
            } else {
                numOfFiles++;
            }
        }
        return numOfFiles;
    }

}
