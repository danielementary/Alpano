/**
 * 
 * @author Samuel Chassot (270955)
 * @author Daniel Filipe Nunes Silva (275197)
 *
 */

package ch.epfl.alpano.gui;

import static javafx.scene.paint.Color.color;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView ;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Alpano extends Application {
    
    private static DiscreteElevationModel dDem1;
    private static DiscreteElevationModel dDem3;
    private static DiscreteElevationModel dDem5;
    private static DiscreteElevationModel dDem7;
    private static DiscreteElevationModel dDem2;
    private static DiscreteElevationModel dDem4;
    private static DiscreteElevationModel dDem6;
    private static DiscreteElevationModel dDem8;

    /**
     * launch the application Alpano
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {

        ContinuousElevationModel cem = loadHGT();
        
        List<Summit> summitsList = GazetteerParser.readSummitsFrom(new File("alps.txt"));
        
        List<PanoramaUserParameters> predefined = new ArrayList<>();
        
        predefined.add(PredefinedPanoramas.NIESEN);
        predefined.add(PredefinedPanoramas.ALPES_DU_JURA);
        predefined.add(PredefinedPanoramas.MONT_RACINE);
        predefined.add(PredefinedPanoramas.FINSTERAARHORN);
        predefined.add(PredefinedPanoramas.TOUR_DE_SAUVABELIN);
        predefined.add(PredefinedPanoramas.PLAGE_DU_PELICAN);
        predefined.add(PredefinedPanoramas.BULLE);
        predefined.add(PredefinedPanoramas.LA_ROCHE);
        predefined.add(PredefinedPanoramas.LE_JORDIL);
        
        PanoramaParametersBean panoParamBean = new PanoramaParametersBean(PredefinedPanoramas.ALPES_DU_JURA);
        PanoramaComputerBean panoCompBean = new PanoramaComputerBean(cem, summitsList);
        
        ObjectProperty<String> mouseInfoProperty = new SimpleObjectProperty<>();
        
        ImageView panoView = createImageView(panoParamBean, panoCompBean, mouseInfoProperty);
        Pane labelsPane = createLabelsPane(panoParamBean, panoCompBean);
        
        StackPane panoGroup = createPanoGroup(panoView, labelsPane, panoParamBean, panoCompBean);
        ScrollPane scrollPane = createPanoScrollPane(panoGroup, panoParamBean, panoCompBean);
        
        StackPane updateNotice = createUpdateNotice(panoParamBean, panoCompBean);
        StackPane computeNotice = createComputeNotice(panoParamBean, panoCompBean);
        
        StackPane panoPane = createPanoPane(panoParamBean, panoCompBean, updateNotice, scrollPane, computeNotice);

        GridPane paramsGrid = createParamsGrid(panoParamBean, panoCompBean, mouseInfoProperty, panoGroup,
                                               primaryStage, predefined, labelsPane);

        BorderPane root = new BorderPane();
        
        root.setCenter(panoPane);
        root.setBottom(paramsGrid);

        Scene scene = new Scene(root);
        
        primaryStage.setHeight(800);
        primaryStage.setWidth(1200);
        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView createImageView(PanoramaParametersBean pUP,
                                      PanoramaComputerBean pCB,
                                      ObjectProperty<String> mouseInfoProp) {
        
        ImageView panoView = new ImageView();
        
        panoView.fitWidthProperty().bind(pUP.widthProperty());
        panoView.imageProperty().bind(pCB.imageProp());
        
        panoView.setSmooth(true);
        panoView.setPreserveRatio(true);
        
        panoView.setOnMouseMoved((event) -> getMouseInfos(event.getX(),
                                                          event.getY(),
                                                          pUP, pCB, mouseInfoProp));
        
        panoView.setOnMouseClicked((event) -> {
            if (event.getClickCount()==2)
                openUrl(event.getX(), event.getY(), pUP, pCB);
        });
        
        return panoView;
    }


    private Pane createLabelsPane(PanoramaParametersBean pUP,
                                  PanoramaComputerBean pCB) {
        
        Pane labelsPane = new Pane();
        
        labelsPane.prefHeightProperty().bind(pUP.heightProperty());
        labelsPane.prefWidthProperty().bind(pUP.widthProperty());
                
        Bindings.bindContent(labelsPane.getChildren(), pCB.getLabels());
          
        labelsPane.setMouseTransparent(true);
        
        return labelsPane;
    }
    
    private StackPane createPanoGroup(ImageView panoView, 
                                      Pane labelsPane,
                                      PanoramaParametersBean pUP,
                                      PanoramaComputerBean pCB) {
        
        StackPane panoGroup = new StackPane();
        panoGroup.getChildren().addAll(panoView, labelsPane);
        
        return panoGroup;
    }
    
    private ScrollPane createPanoScrollPane(StackPane panoGroup, 
                                            PanoramaParametersBean pUP,
                                            PanoramaComputerBean pCB) {
        
        ScrollPane panoScrollPane = new ScrollPane();
        panoScrollPane.setContent(panoGroup);
        
        return panoScrollPane;
    }
    
    private StackPane createComputeNotice(PanoramaParametersBean pUP, PanoramaComputerBean pCB) {
        
        StackPane computeNotice= new StackPane();
        
        Text text = new Text();
        text.setText("Calcul en cours ...");
        text.setFont(new Font(40));
        text.setTextAlignment(TextAlignment.CENTER);
        
        ProgressBar bar = new ProgressBar();
        bar.progressProperty().bind(pCB.getProgressProp());
        
        GridPane textBar = new GridPane();
        textBar.addRow(0, text);
        textBar.addRow(2, bar);
        
        GridPane.setHalignment(bar, HPos.CENTER);
        GridPane.setHalignment(text, HPos.CENTER);
        textBar.setAlignment(Pos.CENTER);
        
        computeNotice.getChildren().add(textBar);
        computeNotice.setCursor(Cursor.WAIT);
        computeNotice.visibleProperty().bind(pCB.getComputeInProg());
        
        Background backg = new Background(new BackgroundFill(color(1,1,1,0.9), CornerRadii.EMPTY, Insets.EMPTY));
        
        computeNotice.setBackground(backg);
        
        return computeNotice;
    }
    
    private StackPane createUpdateNotice(PanoramaParametersBean pUP, PanoramaComputerBean pCB) {
        
        double textSize = 40;
        
        StackPane updateNotice = new StackPane();
        
        Text text = new Text();
        text.setText("Les paramètres du panorama ont changé.\nCliquez ici pour mettre le dessin à jour.");
        text.setFont(new Font(textSize));
        text.setTextAlignment(TextAlignment.CENTER);
        
        updateNotice.getChildren().add(text);
        
        BooleanBinding booleanCondition = pUP.parametersProperty().isEqualTo(pCB.parametersProp()).or(pCB.getComputeInProg()).not();
        
        updateNotice.visibleProperty().bind(booleanCondition);
        updateNotice.setOnMouseClicked((event)-> {
           Thread th = new Thread() {
                public void run() {
                    pCB.setParameters(pUP.parametersProperty().get());
                }
            };
            
            th.setDaemon(true);
            th.start();
        });
        
        Background backg = new Background(new BackgroundFill(color(1,1,1,0.9), CornerRadii.EMPTY, Insets.EMPTY));
        updateNotice.setBackground(backg);
        
        return updateNotice;
    }
    
    private StackPane createPanoPane(PanoramaParametersBean pUP, PanoramaComputerBean pCB,
                                     StackPane updateNotice, ScrollPane panoScrollPane, StackPane compute) {
        
        StackPane panoPane = new StackPane();
        panoPane.getChildren().addAll(panoScrollPane, updateNotice, compute);
        
        return panoPane;
    }
    
    private GridPane createParamsGrid(PanoramaParametersBean pUP, 
                                      PanoramaComputerBean pCB, 
                                      ObjectProperty<String> mouseInfoProp, Pane panoPane,
                                      Stage primary, List<PanoramaUserParameters> predefined,
                                      Pane labelsPane) {
        
        GridPane paramsGrid = new GridPane();

        Label latLab = new Label("Latitude (°) : ");
        Label longLab = new Label("Longitude (°) : ");
        Label azLab = new Label("Azimuth (°) : ");
        Label viewAngleLab = new Label("Angle de vue (°) : ");
        Label altLab = new Label("Altitude (m) : ");
        Label visiLab = new Label("Visibilité (km) : ");
        Label widthLab = new Label("Largeur (px) : ");
        Label heightLab = new Label("Hauteur (px) : ");
        Label superSamplingLab = new Label("Suréchantillonnage : ");
        
        GridPane.setHalignment(latLab, HPos.RIGHT);
        GridPane.setHalignment(longLab, HPos.RIGHT);
        GridPane.setHalignment(azLab, HPos.RIGHT);
        GridPane.setHalignment(viewAngleLab, HPos.RIGHT);
        GridPane.setHalignment(altLab, HPos.RIGHT);
        GridPane.setHalignment(visiLab, HPos.RIGHT);
        GridPane.setHalignment(widthLab, HPos.RIGHT);
        GridPane.setHalignment(heightLab, HPos.RIGHT);
        GridPane.setHalignment(superSamplingLab, HPos.RIGHT);
        
        StringConverter<Integer> stringConverterFixedPoint = new FixedPointStringConverter(4);
        StringConverter<Integer> stringConverterFixedPointZero = new FixedPointStringConverter(0);
        
        //for saving image
        Button saveImageButton = new Button("Sauver panorama");
        saveImageButton.setOnAction((e)-> {
            if (pUP.parametersProperty().isEqualTo(pCB.parametersProp()).get() && !pCB.getComputeInProg().get()) {
                saveImage(panoPane, primary);
            } else {
                if (!pCB.getComputeInProg().get()) {
                    showWarning("Veuillez d'abord calculer le panorama");
                }else{
                    showWarning("Un calcul est en cours");
                }
            }
        });
        
        //For labels visible or not
        CheckBox labelsVisibleCheck = new CheckBox();
        Label labelsVisibleLabel = new Label("Afficher les labels : ");
        labelsVisibleCheck.selectedProperty().set(true);
        labelsPane.visibleProperty().bind(labelsVisibleCheck.selectedProperty());

        //For changing ImagePainter
        Label painterChoiceLabel = new Label("Peintre d'image : ");
        ChoiceBox<Integer> painterChoiceBox = new ChoiceBox<>();
        painterChoiceBox.getItems().addAll(0, 1, 2, 3, 4, 5, 6);
        StringConverter<Integer> stringPainterChoice = 
                new LabeledListStringConverter("Défaut", "Minimaliste",
                                               "Coloré", "Aléatoire", "Petites distances", 
                                               "Couleurs verticales", "Echiquier");
        
        painterChoiceBox.setConverter(stringPainterChoice);
        painterChoiceBox.getSelectionModel().selectFirst();
        pCB.getChoicePainterProp().bind(painterChoiceBox.valueProperty());
        
        painterChoiceBox.valueProperty().addListener(e-> {
            if (!pCB.getComputeInProg().get() && pCB.getPanorama() != null) {
                pCB.updateImage();
            }
        });

        //For predefined selector
        Label predefinedLab = new Label("Paramètres prédéfinis : ");
        ChoiceBox<Integer> predefinedBox = new ChoiceBox<>();
        predefinedBox.getItems().addAll(0,1,2,3,4,5,6,7,8);
        predefinedBox.getSelectionModel().select(1);

        StringConverter<Integer> stringPredifined =
                new LabeledListStringConverter("Niesen","Alpes du Jura", "Mont Racine", "Finsteraarhorn",
                                               "Tour de Sauvabelin", "Plage du pélican",
                                               "Bulle", "La Roche", "Le Jordil");
        
        predefinedBox.setConverter(stringPredifined);

        predefinedBox.valueProperty().addListener((e)-> {
            PanoramaUserParameters choosen = predefined.get((int)predefinedBox.valueProperty().get());
            
            pUP.widthProperty().set(choosen.getWidth());
            pUP.heightProperty().set(choosen.getHeight());
            pUP.observerLatitudeProperty().set(choosen.getOberserverLati());
            pUP.observerLongitudeProperty().set(choosen.getOberserverLong());
            pUP.horizontalFieldOfViewProperty().set(choosen.getHoriFieldOfView());
            pUP.maxDistanceProperty().set(choosen.getMaxDist());
            pUP.CenterAzimuthProperty().set(choosen.getCenterAzim());
            pUP.observerElevationProperty().set(choosen.getObserverElev());
            pUP.SuperSamplingExponentProperty().set(choosen.getSuperSamp());
        });
        
        TextField latField = createTextField(stringConverterFixedPoint, 7, pUP.observerLatitudeProperty());
        TextField longField = createTextField(stringConverterFixedPoint, 7, pUP.observerLongitudeProperty());
        TextField azField = createTextField(stringConverterFixedPointZero, 3, pUP.CenterAzimuthProperty());
        TextField viewAngleField = createTextField(stringConverterFixedPointZero, 3, pUP.horizontalFieldOfViewProperty());
        TextField altField = createTextField(stringConverterFixedPointZero, 4, pUP.observerElevationProperty());
        TextField visiField = createTextField(stringConverterFixedPointZero, 3, pUP.maxDistanceProperty());
        TextField widthField = createTextField(stringConverterFixedPointZero, 4, pUP.widthProperty());
        TextField heightField = createTextField(stringConverterFixedPointZero, 4, pUP.heightProperty());
        
        TextArea mouseInfo = new TextArea();
        
        mouseInfo.setEditable(false);
        mouseInfo.setPrefRowCount(2);
        mouseInfo.textProperty().bind(mouseInfoProp);
        
        ChoiceBox<Integer> superSamplingBox = new ChoiceBox<>();
        superSamplingBox.getItems().addAll(0,1,2);

        StringConverter<Integer> stringConverterSampling = new LabeledListStringConverter("non", "2x", "4x");
        superSamplingBox.valueProperty().bindBidirectional(pUP.SuperSamplingExponentProperty());
        superSamplingBox.setConverter(stringConverterSampling);
        
        paramsGrid.addRow(0, latLab, latField, longLab, longField, altLab, altField);
        paramsGrid.addRow(1, azLab, azField, viewAngleLab, viewAngleField, visiLab, visiField);
        paramsGrid.addRow(2, widthLab, widthField, heightLab, heightField, superSamplingLab, superSamplingBox);
        
        paramsGrid.add(painterChoiceLabel, 9, 1);
        paramsGrid.add(painterChoiceBox, 10, 1);
        paramsGrid.add(predefinedLab, 9, 0);
        paramsGrid.add(predefinedBox, 10, 0);
        
        paramsGrid.add(saveImageButton, 9, 2);
        paramsGrid.addRow(3, labelsVisibleLabel, labelsVisibleCheck);
        
        paramsGrid.add(mouseInfo, 7, 0, 1, 4);
        paramsGrid.setAlignment(Pos.CENTER);
        
        //little margin between texts and fields
        for (Node n : paramsGrid.getChildren()) {
            GridPane.setMargin(n, new Insets(2));
        }
        
        return paramsGrid;
    }
    
    private TextField createTextField(StringConverter<Integer> strConv, 
                                      int columnNum,
                                      ObjectProperty<Integer> objProp) {
        
        TextField textField = new TextField();
        TextFormatter<Integer> formatter = new TextFormatter<>(strConv);
        
        formatter.valueProperty().bindBidirectional(objProp);
        
        textField.setTextFormatter(formatter);
        textField.setAlignment(Pos.CENTER_RIGHT);
        textField.setPrefColumnCount(columnNum);
        
        return textField;
    }
    
    private void getMouseInfos(double x, double y,
                               PanoramaParametersBean pUP,
                               PanoramaComputerBean pCB, 
                               ObjectProperty<String> mouseInfoProp) {
        
        Panorama pano = pCB.getPanorama();
        PanoramaParameters param = pano.parameters();
        
        int superSampling = pUP.SuperSamplingExponentProperty().get();
        double mouseX = Math.scalb(x, superSampling);
        double mouseY = Math.scalb(y, superSampling);
        
        StringBuilder builder = new StringBuilder();
        
        double latitude = Math.toDegrees(pano.latitudeAt((int) mouseX, (int)mouseY));
        double longitude = Math.toDegrees(pano.longitudeAt((int) mouseX, (int)mouseY));
        String str = String.format((Locale)null,"Position : %.4f°N %.4f°E\n", latitude, longitude);
        builder.append(str);
  
        double distance = pano.distanceAt((int)mouseX, (int)mouseY)/1000;
        str = String.format((Locale) null, "Distance : %.1f km\n", distance);
        builder.append(str);
        
        double elevation = pano.elevationAt((int)mouseX, (int)mouseY);
        str = String.format((Locale) null, "Altitude : %.0f m\n", elevation);
        builder.append(str);
        
        double az = param.azimuthForX(x);
        String octant = Azimuth.toOctantString(az, "N", "E", "S", "O");
        az = Math.toDegrees(az);
        
        double altitude = Math.toDegrees(param.altitudeForY(y));
        str = String.format((Locale) null, "Azimuth : %.1f° (%s)   Elévation : %.1f°", az, octant, altitude);
        builder.append(str);
        
        mouseInfoProp.set(builder.toString());  
    }
    
    private void openUrl(double x, double y,
                         PanoramaParametersBean pUP,
                         PanoramaComputerBean pCB) {
        
        Panorama pano = pCB.getPanorama();
        
        int superSampling = pUP.SuperSamplingExponentProperty().get();
        double mouseX = Math.scalb(x, superSampling);
        double mouseY = Math.scalb(y, superSampling);
        
        double latitude = Math.toDegrees(pano.latitudeAt((int) mouseX, (int)mouseY));
        double longitude = Math.toDegrees(pano.longitudeAt((int) mouseX, (int)mouseY));
        
        String qy = String.format((Locale) null, "mlat=%.4f&mlon=%.4f", latitude, longitude);
        String fg = String.format((Locale)null, "map=15/%.4f$/%.4f$" , latitude, longitude);  
        
        URI url;
        
        try {
            url = new URI("http", "www.openstreetmap.org", "/", qy, fg);
            java.awt.Desktop.getDesktop().browse(url);
            
        } catch (URISyntaxException e) {
            e.printStackTrace();
            
        } catch (IOException e) {
            e.printStackTrace();
            
        } 
    }
    
    private void saveImage(Pane panoPane, Stage stage) {
        try {
            FileChooser fileChooser = new FileChooser();
            
            Image img = panoPane.snapshot(null,
                                          new WritableImage((int)panoPane.widthProperty().get(),
                                          (int)panoPane.heightProperty().get()));
    
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.png");
            
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName("monPanorama");
            
            File file = fileChooser.showSaveDialog(stage);
            
            if (ImageIO.write(SwingFXUtils.fromFXImage(img, null),
                              "png",
                              file)) {
                showInfo("Sauvegarde réussie !");
            }
        } catch (IOException e) {
            showError("La sauvegarde a échouée. Veuillez réessayer");
        } catch (IllegalArgumentException e) {
            showError("La sauvegarde a échouée ! Vous n'avez probablement pas sélectionné de fichier");
        } catch(OutOfMemoryError e) {
            showError("La mémoire disponible est trop faible."
                      +" Veuillez calculer un panorama avec une résolution plus faible.");
        }
    }
    
    private final static ContinuousElevationModel loadHGT() {
        
        dDem1 = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        dDem2 = new HgtDiscreteElevationModel(new File("N45E007.hgt"));
        dDem3 = new HgtDiscreteElevationModel(new File("N45E008.hgt"));
        dDem4 = new HgtDiscreteElevationModel(new File("N45E009.hgt"));
        dDem5 = new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        dDem6 = new HgtDiscreteElevationModel(new File("N46E007.hgt"));
        dDem7 = new HgtDiscreteElevationModel(new File("N46E008.hgt"));
        dDem8 = new HgtDiscreteElevationModel(new File("N46E009.hgt"));

        DiscreteElevationModel dDem12 = dDem1.union(dDem2);
        DiscreteElevationModel dDem34 = dDem3.union(dDem4);
        DiscreteElevationModel dDem56 = dDem5.union(dDem6);
        DiscreteElevationModel dDem78 = dDem7.union(dDem8);

        DiscreteElevationModel dDem1234 = dDem12.union(dDem34);
        DiscreteElevationModel dDem5678 = dDem56.union(dDem78);

        DiscreteElevationModel dDemAll = dDem1234.union(dDem5678);
        
        return new ContinuousElevationModel(dDemAll);
    }

    
    private void showError(String str) {
        Alert error = new Alert(AlertType.ERROR);
       
        error.setContentText(str);
        error.setHeaderText("");
        error.show();
    }
    
    private void showWarning(String str) {
        Alert warning = new Alert(AlertType.WARNING);
        
        warning.setContentText(str);
        warning.setHeaderText("");
        warning.show();
    }
    
    private void showInfo(String str) {
        Alert info = new Alert(AlertType.INFORMATION);
        
        info.setHeaderText("");
        info.setContentText(str);
        info.show();
    }
}
