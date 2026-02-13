import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        String basePath = "D:/Games/";
        StringBuilder log = new StringBuilder();

        // Список директорий
        List<String> directories = new ArrayList<>();
        directories.add("src");
        directories.add("src/main");
        directories.add("src/test");
        directories.add("res");
        directories.add("res/drawables");
        directories.add("res/vectors");
        directories.add("res/icons");
        directories.add("savegames");
        directories.add("temp");

        // Создание директорий
        for (String dir : directories) {
            createDir(new File(basePath + dir), log);
        }

        // Список файлов
        List<String> files = new ArrayList<>();
        files.add("src/main/Main.java");
        files.add("src/main/Utils.java");
        files.add("temp/temp.txt");

        // Создание файлов
        for (String file : files) {
            createFile(new File(basePath + file), log);
        }

        // Запись лога
        File tempFile = new File(basePath + "temp/temp.txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(log.toString());
        } catch (IOException e) {
            System.out.println("Ошибка записи лога: " + e.getMessage());
        }

        System.out.println("Установка завершена!");
    }

    // Метод создания директории
    public static void createDir(File dir, StringBuilder log) {
        if (dir.mkdirs()) {
            log.append("Каталог создан: ")
                    .append(dir.getAbsolutePath())
                    .append("\n");
        } else {
            log.append("Каталог уже существует или ошибка: ")
                    .append(dir.getAbsolutePath())
                    .append("\n");
        }
    }

    // Метод создания файла
    public static void createFile(File file, StringBuilder log) {
        try {
            if (file.createNewFile()) {
                log.append("Файл создан: ")
                        .append(file.getAbsolutePath())
                        .append("\n");
            } else {
                log.append("Файл уже существует: ")
                        .append(file.getAbsolutePath())
                        .append("\n");
            }
        } catch (IOException e) {
            log.append("Ошибка создания файла: ")
                    .append(file.getAbsolutePath())
                    .append(" — ")
                    .append(e.getMessage())
                    .append("\n");
        }
    }
}