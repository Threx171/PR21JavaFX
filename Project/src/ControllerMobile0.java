import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.Initializable;

public class ControllerMobile0  implements Initializable{

    @FXML
    private ListView<String> lPane;

    @FXML
    private VBox ymPane;
    @FXML
    private ScrollPane scPane;
    @FXML
    private Button backBtn;

    @FXML
    private AnchorPane info;

    String opcions[] = {"Personatges", "Jocs", "Consoles"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scPane.setVisible(false);
        info.setVisible(false);
        lPane.getItems().addAll(opcions);
        lPane.setOnMouseClicked(event -> {loadList();});
        backBtn.setOnAction(event -> {
            info.setVisible(false);
            ymPane.getChildren().clear();
            lPane.getItems().clear();
            lPane.getItems().addAll(opcions);
            scPane.setVisible(false);
        });
    }
    public void loadList() {
        String opcio = lPane.getSelectionModel().getSelectedItem();
        AppData appData = AppData.getInstance();
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
        String opcioSeleccionada = lPane.getSelectionModel().getSelectedItem();
        if (opcioCarregada.compareTo(opcioSeleccionada) != 0) {
            return;
        }
        AppData appData = AppData.getInstance();
        JSONArray dades = appData.getData(opcioSeleccionada);

        URL resource = this.getClass().getResource("assets/template_list_item.fxml");

        lPane.getItems().removeAll(opcions);
        scPane.setVisible(true);
        scPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

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
                ymPane.getChildren().add(itemTemplate);
            }
        }
    }

    public void showInfo(String type, int index) {
        AppData appData = AppData.getInstance();
        JSONObject dades = appData.getItemData(type, index);
        URL resource = null;
        if ("Jocs".equals(type)) {
            resource = this.getClass().getResource("assets/layout_info_item.fxml");
        }else if ("Personatges".equals(type)) {
            resource = this.getClass().getResource("assets/layout_info_chara.fxml");
        }else if ("Consoles".equals(type)) {
            resource = this.getClass().getResource("assets/layout_info_console.fxml");
        }
        info.getChildren().clear();
        try {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerInfoItem itemController = loader.getController();
            itemController.setImage("assets/images/" + dades.getString("imatge"));
            itemController.setTitle(dades.getString("nom"));
            switch (type) {
                case "Consoles": itemController.setDataText(dades.getString("data"));
                                 itemController.setProcesadorText(dades.getString("procesador"));
                                 itemController.setColorText(dades.getString("color"));
                                 itemController.setVenudesText(String.valueOf(dades.getInt("venudes")));break;
                case "Jocs": itemController.setText(dades.getString("descripcio"));break;
                case "Personatges": itemController.setText(dades.getString("nom_del_videojoc"));
                                    itemController.setColorText(dades.getString("color"));break;
                default: break;
            }
            info.setVisible(true);
            scPane.setVisible(false);
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
