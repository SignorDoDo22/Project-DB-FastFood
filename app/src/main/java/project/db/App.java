package project.db;

import project.db.controller.DAOUtils;
import project.db.controller.MainController;
import project.db.model.ReadingModel;
import project.db.model.WritingModel;

public class App {

    private static final String USER = "appuser";
    private static final String PASSWORD = "StrongP@ssw0rd";

    public static void main(String[] args) {

        final var connection = DAOUtils.localMySQLConnection("schema_rel_fastfood", USER, PASSWORD);
        final var readingModel = new ReadingModel(connection);
        final var writingModel = new WritingModel(connection);
        final var mainController = new MainController(readingModel, writingModel, connection);

    }
}