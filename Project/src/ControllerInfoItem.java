import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerInfoItem {
    @FXML
    private ImageView img;

    @FXML
    private Label title = new Label();

    @FXML
    private Label text = new Label();

    @FXML
    private Label dataText = new Label();

    @FXML
    private Label procesadorText = new Label();

    @FXML
    private Label colorText = new Label();

    @FXML
    private Label venudesText = new Label();
    

    public void setImage(String resourceName) {
        ClassLoader classLoader = getClass().getClassLoader();
        Image image = new Image(classLoader.getResourceAsStream(resourceName));
        img.setImage(image);
    }
    public void setTitle(String text) {
        this.title.setText(text);
    }
    public void setText(String text) {
        this.text.setText(text);
    }
    public void setDataText(String text) {
        this.dataText.setText(text);
    }
    public void setProcesadorText(String text) {
        this.procesadorText.setText(text);
    }
    public void setColorText(String text) {
        this.colorText.setText(text);
    }
    public void setVenudesText(String text) {
        this.venudesText.setText(text);
    }
}
