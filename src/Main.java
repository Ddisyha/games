import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String root = "D:/Games/";
        StringBuilder log = new StringBuilder();

        // 1. Создание директорий
        File src = new File(root + "src");
        File res = new File(root + "res");
        File savegames = new File(root + "savegames");
        File temp = new File(root + "temp");

        File srcMain = new File(src, "main");
        File srcTest = new File(src, "test");

        File resDrawables = new File(res, "drawables");
        File resVectors = new File(res, "vectors");
        File resIcons = new File(res, "icons");

        createDir(src, log);
        createDir(res, log);
        createDir(savegames, log);
        createDir(temp, log);

        createDir(srcMain, log);
        createDir(srcTest, log);

        createDir(resDrawables, log);
        createDir(resVectors, log);
        createDir(resIcons, log);

        // 2. Создание файлов
        createFile(new File(srcMain, "Main.java"), log);
        createFile(new File(srcMain, "Utils.java"), log);

        File tempFile = new File(temp, "temp.txt");
        createFile(tempFile, log);

        // 3. Создаем объекты игрового прогресса
        GameSave gp1 = new GameSave(100, 2, 5, 250.5);
        GameSave gp2 = new GameSave(80, 3, 10, 500.0);
        GameSave gp3 = new GameSave(50, 5, 15, 1200.7);

        // Пути для сохранений
        String save1 = savegames + "/save1.dat";
        String save2 = savegames + "/save2.dat";
        String save3 = savegames + "/save3.dat";

        saveGame(save1, gp1, log);
        saveGame(save2, gp2, log);
        saveGame(save3, gp3, log);

        // Архивирование
        List<String> filesToZip = new ArrayList<>();
        filesToZip.add(save1);
        filesToZip.add(save2);
        filesToZip.add(save3);

        String zipPath = savegames + "/saves.zip";
        zipFiles(zipPath, filesToZip, log);

        // Удаляем исходники
        for (String filePath : filesToZip) {
            File file = new File(filePath);
            if (file.exists() && file.delete()) {
                log.append(file.getName()).append(" удалён после архивации.\n");
            }
        }

        // 4. Записываем лог в temp.txt
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(log.toString());
            System.out.println("Лог записан в temp.txt");
        } catch (IOException e) {
            System.out.println("Ошибка записи лога: " + e.getMessage());
        }

        System.out.println("Установка завершена!");
    }

    // Метод создания директории
    public static void createDir(File dir, StringBuilder log) {
        if (dir.mkdir()) {
            log.append("Каталог ").append(dir.getAbsolutePath()).append(" создан.\n");
        } else {
            log.append("Не удалось создать каталог ").append(dir.getAbsolutePath()).append(".\n");
        }
    }

    // Метод создания файла
    public static void createFile(File file, StringBuilder log) {
        try {
            if (file.createNewFile()) {
                log.append("Файл ").append(file.getAbsolutePath()).append(" создан.\n");
            } else {
                log.append("Файл ").append(file.getAbsolutePath()).append(" уже существует.\n");
            }
        } catch (IOException e) {
            log.append("Ошибка создания файла ").append(file.getAbsolutePath())
                    .append(": ").append(e.getMessage()).append("\n");
        }
    }

    // Метод сериализации игрового прогресса
    public static void saveGame(String path, GameSave game, StringBuilder log) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
            log.append(path).append(" сохранён успешно.\n");
        } catch (IOException e) {
            log.append("Ошибка сохранения ").append(path).append(": ").append(e.getMessage()).append("\n");
        }
    }

    // Метод архивирования
    public static void zipFiles(String zipPath, List<String> filePaths, StringBuilder log) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String path : filePaths) {
                File file = new File(path);
                if (!file.exists()) continue;

                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zos.write(buffer);

                    zos.closeEntry();
                    log.append(file.getName()).append(" добавлен в архив.\n");
                }
            }
            log.append("Архив ").append(zipPath).append(" создан успешно.\n");
        } catch (IOException e) {
            log.append("Ошибка архивации: ").append(e.getMessage()).append("\n");
        }
    }
}