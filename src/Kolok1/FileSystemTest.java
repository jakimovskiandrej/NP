package Kolok1;

import java.util.*;
import java.util.stream.Collectors;

interface IFile {
    String getFileName();
    long getFileSize();
    String getFileInfo(int i);
    void sortBySize();
    long findLargestFile();
}

class FileNameExistsException extends Exception {
    public FileNameExistsException(String message) {
        super(message);
    }
}

class File implements IFile {

    String fileName;
    long size;

    public File(String fileName, long size) {
        this.fileName = fileName;
        this.size = size;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public long getFileSize() {
        return size;
    }
    //File name [името на фајлот со 10 места порамнето на десно] File size: [големината на фајлот со 10 места пораменета на десно ]
    @Override
    public String getFileInfo(int i) {
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < i; j++) {
            sb.append("    ");
        }
        sb.append(String.format("File name: %10s File size: %10d\n", fileName, size));
        return sb.toString();
    }

    @Override
    public void sortBySize() {

    }

    @Override
    public long findLargestFile() {
        return size;
    }
}

class Folder extends File {

    List<IFile> files;

    public Folder(String fileName) {
        super(fileName, 0);
        files = new ArrayList<>();
    }

    void addFile (IFile file) throws FileNameExistsException {
        if(files.isEmpty()) {
            files.add(file);
            return;
        }
        Optional<IFile> opt = files.stream().filter(f -> f.getFileName().equals(file.getFileName())).findAny();
        if(opt.isPresent()) {
            throw new FileNameExistsException(String.format("There is already a file named %s in the folder %s",file.getFileName(),getFileName()));
        }
        files.add(file);
    }

    @Override
    public long getFileSize() {
        return files.stream().mapToLong(IFile::getFileSize).sum();
    }

    @Override
    public String getFileInfo(int i) {
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < i; j++) {
            sb.append("  ");
        }
        sb.append(String.format("Folder name: %10s Folder size: %10d\n",getFileName(),getFileSize()));
        files.forEach(a->sb.append(a.getFileInfo(i+1)));
        return sb.toString();
    }

    @Override
    public void sortBySize() {
        files = files.stream().sorted(Comparator.comparing(IFile::getFileSize)).collect(Collectors.toList());
        files.stream().sorted(Comparator.comparingLong(IFile::getFileSize)).forEach(IFile::sortBySize);
    }

    @Override
    public long findLargestFile() {
        OptionalLong result = files.stream().mapToLong(IFile::findLargestFile).max();
        if(result.isPresent()) {
            return result.getAsLong();
        } else {
            return 0;
        }
    }
}

class FileSystem {

    Folder directory;

    public FileSystem() {
        directory = new Folder("root");
    }

    void addFile (IFile file) throws FileNameExistsException {
        directory.addFile(file);
    }

    long findLargestFile () {
        return directory.findLargestFile();
    }

    void sortBySize() {
        directory.sortBySize();
    }

    @Override
    public String toString() {
        return directory.getFileInfo(0);
    }
}

public class FileSystemTest {
    public static Folder readFolder (Scanner sc)  {

        Folder folder = new Folder(sc.nextLine());
        int totalFiles = Integer.parseInt(sc.nextLine());

        for (int i=0;i<totalFiles;i++) {
            String line = sc.nextLine();

            if (line.startsWith("0")) {
                String fileInfo = sc.nextLine();
                String [] parts = fileInfo.split("\\s+");
                try {
                    folder.addFile(new File(parts[0], Long.parseLong(parts[1])));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
            else {
                try {
                    folder.addFile(readFolder(sc));
                } catch (FileNameExistsException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return folder;
    }

    public static void main(String[] args)  {

        //file reading from input

        Scanner sc = new Scanner (System.in);

        System.out.println("===READING FILES FROM INPUT===");
        FileSystem fileSystem = new FileSystem();
        try {
            fileSystem.addFile(readFolder(sc));
        } catch (FileNameExistsException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("===PRINTING FILE SYSTEM INFO===");
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING FILE SYSTEM INFO AFTER SORTING===");
        fileSystem.sortBySize();
        System.out.println(fileSystem.toString());

        System.out.println("===PRINTING THE SIZE OF THE LARGEST FILE IN THE FILE SYSTEM===");
        System.out.println(fileSystem.findLargestFile());




    }
}
