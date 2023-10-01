import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.Initializable;

public class ControllerDesktop implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private VBox yPane;

    @FXML
    private AnchorPane info;

    String opcions[] = {"Personatges", "Jocs", "Consoles"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choiceBox.getItems().addAll(opcions);
        choiceBox.setValue(opcions[0]);
        choiceBox.setOnAction((event) -> { loadList(); });
        loadList();
    }
    public void loadList() {
        String opcio = choiceBox.getValue();
        AppData appData = AppData.getInstance();
        showLoading();
        appData.load(opcio, (result) -> {
            if (result == null) {
                System.out.println("ControllerDesktop: Error loading.");
            } else {
                try {
                    showList(opcio);
                } catch (Exception e) {
                    System.out.println("ControllerDesktop: Error showing list.");
                }
            }
        });
    }
    public void showList(String opcioCarregada) throws Exception {
        String opcioSeleccionada = choiceBox.getValue();
        if (opcioCarregada.compareTo(opcioSeleccionada) != 0) {
            return;
        }
        AppData appData = AppData.getInstance();
        JSONArray dades = appData.getData(opcioSeleccionada);

        URL resource = this.getClass().getResource("assets/template_list_item.fxml");

        yPane.getChildren().clear();

        for (int i = 0; i < dades.length(); i++) {
            JSONObject consoleObject = dades.getJSONObject(i);

            if (consoleObject.has("nom")) {
                String nom = consoleObject.getString("nom");
                String imatge = "assets/images/" + consoleObject.getString("imatge");
                FXMLLoader loader = new FXMLLoader(resource);
                Parent itemTemplate = loader.load();
                ControllerListItem itemController = loader.getController();
                itemController.setText(nom);
                itemController.setImage(imatge);

                final String type = opcioSeleccionada;
                final int index = i;
                itemTemplate.setOnMouseClicked(event -> {
                    showInfo(type,index);
                });
                yPane.getChildren().add(itemTemplate);
            }
        }
    }
    public void showLoading() {
        yPane.getChildren().clear();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        yPane.getChildren().add(progressIndicator);
    }
    public void showInfo(String type, int index) {
        AppData appData = AppData.getInstance();
        JSONObject dades = appData.getItemData(type, index);
        URL resource = null;
        if (type == "Jocs") {
            resource = this.getClass().getResource("assets/layout_info_item.fxml");
        }else if (type == "Personatges") {
            resource = this.getClass().getResource("assets/template_chara_info.fxml");
        }else if (type == "Consoles") {
            resource = this.getClass().getResource("assets/layout_info_item.fxml");
        }
        info.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerInfoItem itemController = loader.getController();
            itemController.setImage("assets/images/" + dades.getString("imatge"));
            itemController.setTitle(dades.getString("nom"));
            switch (type) {
                case "Consoles": itemController.setText(dades.getString("procesador"));break;
                case "Jocs": itemController.setText(dades.getString("descripcio"));break;
                case "Personatges": itemController.setText(dades.getString("nom_del_videojoc"));break;
            }
            info.getChildren().add(itemTemplate);
            AnchorPane.setTopAnchor(itemTemplate, 0.0);
            AnchorPane.setRightAnchor(itemTemplate, 0.0);
            AnchorPane.setBottomAnchor(itemTemplate, 0.0);
            AnchorPane.setLeftAnchor(itemTemplate, 0.0);
        } catch (Exception e) {
            System.out.println("ControllerDesktop: Error showing info.");
            System.out.println(e);
        }
    }

}