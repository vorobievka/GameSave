import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    static int itm = 1;

    public static void main(String[] args) {

        List<GameProgress> savedprogress = new ArrayList<GameProgress>();
        List<String> paths = new ArrayList<String>();

        GameProgress one = new GameProgress(10, 10, 10, 10);
        savedprogress.add(one);
        GameProgress two = new GameProgress(5, 15, 11, 20);
        savedprogress.add(two);
        GameProgress three = new GameProgress(2, 17, 12, 40);
        savedprogress.add(three);

        savedprogress.forEach((n) -> {
            paths.add(saveGame(n));
        });

        zipFiles(paths);

    }

    public static String saveGame(GameProgress gameProgress) {
        String path = "I://Games/savegames/save" + itm + ".dat";
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Прогресс сохранен " + path);
            itm += 1;
            return path;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ошибка при сохранении");
            return "";
        }
    }

    public static void zipFiles(List<String> paths) {
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("I://Games/savegames/zip_output.zip"));
            paths.forEach((n) -> {
                String string = n;
                String shortstring = string.substring(string.length() - 9);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(string);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                ZipEntry entry = new ZipEntry(shortstring);
                try {
                    zout.putNextEntry(entry);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                byte[] buffer = new byte[0];
                try {
                    buffer = new byte[fis.available()];
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fis.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    zout.write(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    zout.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fis.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            zout.close();

            paths.forEach((n) -> {
                String string = n;
                System.out.println("Файл " + string + " добавлен к архиву");
                File f = new File(string);
                if (f.delete()) {
                    System.out.println("Файл " + string + " удален");
                }
            });

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        }
    }
}