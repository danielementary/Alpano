package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.PredefinedPanoramas.*;

import ch.epfl.alpano.gui.PanoramaParametersBean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.stage.Stage;

public final class BeansTestsSD extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    PanoramaParametersBean bean =
      new PanoramaParametersBean(NIESEN);
    ObjectProperty<Integer> prop =
      bean.observerLatitudeProperty();

    prop.addListener((o, oV, nV) ->
      System.out.printf("  %d -> %d (%s)%n", oV, nV, o));
    System.out.println("set to 1");
    prop.set(1);
    System.out.println("set to 2");
    prop.set(2);

    Platform.exit();
  }
}