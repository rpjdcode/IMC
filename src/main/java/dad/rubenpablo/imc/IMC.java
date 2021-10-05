package dad.rubenpablo.imc;


import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class IMC extends Application{
	
	// View
	private VBox panel;
	private HBox pesoPanel;
	private HBox alturaPanel;
	private HBox infoPanel;
	
	private Scene escena;
	
	private TextField peso;
	private TextField altura;
	
	private Label pesoLabel;
	private Label alturaLabel;
	private Label uPeso;
	private Label uAltura;
	private Label infoIMC;
	private Label resLabel;
	private Label rangoPeso;
	
	// Model
	private DoubleProperty pesoProperty = new SimpleDoubleProperty();
	private DoubleProperty alturaProperty = new SimpleDoubleProperty();
	private DoubleProperty metrosProperty = new SimpleDoubleProperty();
	private DoubleProperty resultado = new SimpleDoubleProperty();
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Campos de Texto
		peso = new TextField();
		peso.setMaxWidth(80);
		altura = new TextField();
		altura.setMaxWidth(80);
		
		// Labels
		pesoLabel = new Label("Peso:");
		alturaLabel = new Label("Altura:");
		uPeso = new Label("kg");
		uAltura = new Label("cm");
		infoIMC = new Label("IMC: ");
		resLabel = new Label();
		rangoPeso = new Label();
		
		// HBox
		pesoPanel = new HBox(5, pesoLabel, peso, uPeso);
		alturaPanel = new HBox(5, alturaLabel, altura, uAltura);
		infoPanel = new HBox(5, infoIMC, resLabel);
		// VBox
		panel = new VBox(5, pesoPanel, alturaPanel, infoPanel, rangoPeso);
		panel.setFillWidth(false);
		panel.setAlignment(Pos.CENTER);
		
		// Escena
		escena = new Scene(panel, 320, 200);
		
		// Ventana
		primaryStage.setTitle("Calculadora IMC");
		primaryStage.setScene(escena);
		primaryStage.show();
		
		// Listener para reemplazar '.' por ','
		peso.textProperty().addListener((o, ov, nv) ->{
			peso.setText(peso.getText().replace('.', ','));
		});
		
		altura.textProperty().addListener((o, ov, nv) ->{
			altura.setText(altura.getText().replace('.', ','));
		});
		
		// Bindings
		Bindings.bindBidirectional(peso.textProperty(), pesoProperty, new NumberStringConverter());
		Bindings.bindBidirectional(altura.textProperty(), alturaProperty, new NumberStringConverter());
		metrosProperty.bind(alturaProperty.divide(100));
		resultado.bind(
				pesoProperty.divide(metrosProperty.multiply(metrosProperty))
				);
		
		
		// Inicialmente, la label que representará el resultado se mostrará en blanco para no mostrar NaN al usuario
		resLabel.textProperty().bind(resultado.asString(""));
		
		// Agregamos un listener para cuando cambie el valor de la doubleproperty resultado
		resultado.addListener((o, ov , nv) -> {
			StringBinding msg;
			msg = (Double.isNaN(resultado.get()) ? resultado.asString("") : resultado.asString("%.2f"));
			resLabel.textProperty().unbind();
			resLabel.textProperty().bind(msg);
		});

		
		BooleanProperty obeso = new SimpleBooleanProperty();
		BooleanProperty sobrepeso = new SimpleBooleanProperty();
		BooleanProperty normal = new SimpleBooleanProperty();
		BooleanProperty bajo = new SimpleBooleanProperty();
		
		obeso.bind(resultado.greaterThanOrEqualTo(30f));
		sobrepeso.bind(resultado.greaterThanOrEqualTo(25f).and(resultado.lessThan(30)));
		normal.bind(resultado.greaterThanOrEqualTo(18.5f).and(resultado.lessThan(25)));
		bajo.bind(resultado.lessThan(18.5f));
		StringExpression expr = (
				Bindings.concat(
						Bindings.when(obeso).then("Obeso").otherwise("")
						).concat(
								Bindings.when(sobrepeso).then("Sobrepeso").otherwise("")
								).concat(
										Bindings.when(normal).then("Peso Normal").otherwise("")
										).concat(
												Bindings.when(bajo).then("Bajo peso").otherwise("")
												)
				
				);
		
		rangoPeso.textProperty().bind(expr);

	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
