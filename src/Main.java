import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        List<GameProgress> savedprogress = new ArrayList<GameProgress>();
        List<String> paths = new ArrayList<String>();

        GameProgress one = new GameProgress(10, 10, 10, 10);
        savedprogress.add(one);
        GameProgress two = new GameProgress(5, 15, 11, 20);
        savedprogress.add(two);
        GameProgress three = new GameProgress(2, 17, 12, 40);
        savedprogress.add(three);

        int itm = 1;

        Iterator<GameProgress> itr = null;
        itr = savedprogress.iterator();

        while (itr.hasNext()) {
            String path = "I://Games/savegames/save" + itm + ".dat";
            paths.add(path);
            if (saveGame(itr.next(), path)) {
                System.out.println("Прогресс сохранен " + path);
                itm += 1;
            } else {
                System.out.println("Ошибка при сохранении");
            }
        }

        Iterator<String> iterator = null;
        Iterator<String> iterator2 = null;
        iterator = paths.iterator();
        iterator2 = paths.iterator();

        try {
            if (zipFiles(iterator)) {
                while (iterator2.hasNext()) {
                    String string = iterator2.next();
                    System.out.println("Файл " + string + " добавлен к архиву");
                    File f = new File(string);
                    if (f.delete()) {
                        System.out.println("Файл " + string + " удален");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean saveGame(GameProgress gameProgress, String path) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean zipFiles(Iterator<String> iterator) {

        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream("I://Games/savegames/zip_output.zip"));

            while (iterator.hasNext()) {
                String string = iterator.next();
                String shortstring = string.substring(string.length() - 9);

                FileInputStream fis = new FileInputStream(string);

                ZipEntry entry = new ZipEntry(shortstring);
                zout.putNextEntry(entry);

                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);

                zout.write(buffer);

                zout.closeEntry();
                fis.close();

            }
            zout.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }

    }

}