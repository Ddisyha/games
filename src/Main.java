import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {

        // Папка savegames
        String saveDir = "D:/Games/savegames/";

        // Создаем три экземпляра игрового прогресса
        GameProgress gp1 = new GameProgress(100, 2, 5, 250.5);
        GameProgress gp2 = new GameProgress(80, 3, 10, 500.0);
        GameProgress gp3 = new GameProgress(50, 5, 15, 1200.7);

        // Пути для сохранений
        String save1 = saveDir + "save1.dat";
        String save2 = saveDir + "save2.dat";
        String save3 = saveDir + "save3.dat";

        // Сохраняем игры
        saveGame(save1, gp1);
        saveGame(save2, gp2);
        saveGame(save3, gp3);

        // Список файлов для архивации
        List<String> filesToZip = new ArrayList<>();
        filesToZip.add(save1);
        filesToZip.add(save2);
        filesToZip.add(save3);

        // Архив
        String zipPath = saveDir + "saves.zip";
        zipFiles(zipPath, filesToZip);

        // Удаляем исходные файлы сохранений
        for (String filePath : filesToZip) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println(file.getName() + " удалён после архивации.");
                } else {
                    System.out.println("Не удалось удалить " + file.getName());
                }
            }
        }

        System.out.println("Все операции с сохранениями завершены!");
    }

    // Метод для сериализации объекта GameProgress
    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(gameProgress);
            System.out.println(filePath + " сохранён успешно.");

        } catch (IOException e) {
            System.out.println("Ошибка при сохранении " + filePath + ": " + e.getMessage());
        }
    }

    // Метод для архивирования файлов
    public static void zipFiles(String zipPath, List<String> filePaths) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {

            for (String filePath : filePaths) {
                File file = new File(filePath);
                if (!file.exists()) continue;

                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zos.write(buffer);

                    zos.closeEntry();
                    System.out.println(file.getName() + " добавлен в архив.");
                } catch (IOException e) {
                    System.out.println("Ошибка при добавлении " + file.getName() + " в архив: " + e.getMessage());
                }
            }

            System.out.println("Архив " + zipPath + " создан успешно!");

        } catch (IOException e) {
            System.out.println("Ошибка при создании архива: " + e.getMessage());
        }
    }
}