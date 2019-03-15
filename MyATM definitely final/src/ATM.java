import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class ATM extends Application {
	String result;
	int position = 0;
	int numOfTransactions = 0;
	double amount;
	int flag = 0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {

		// Login scene
		LinkedList<Transaction> history = new LinkedList<Transaction>();
		Stage window1 = new Stage();
		VBox layout1 = new VBox(20);

		Label label1 = new Label("Account Number:");
		TextField accNumInput = new TextField("123456");
		Button login = new Button("Continue");

		layout1.getChildren().addAll(label1, accNumInput, login);
		layout1.setPadding(new Insets(10, 10, 10, 10));
		layout1.setAlignment(Pos.CENTER);

		Scene scene1 = new Scene(layout1, 300, 300);

		window1.setScene(scene1);
		window1.setTitle("ATM");
		window1.show();

		Client omar = new Client("123456", 0.0);

		// Menu

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setAlignment(Pos.CENTER);
		grid.setPrefWidth(150);
		grid.setVgap(10);
		grid.setHgap(10);

		Button withdrawButton = new Button("Withdraw");
		Button depositButton = new Button("Deposit");
		Button checkBalance = new Button("Balance Inquiry");
		Button next = new Button("Next");
		Button prev = new Button("Previous");
		Button exit = new Button("Exit");
		Button back = new Button("Back");
		Label historyLabel = new Label();

		withdrawButton.setMinWidth(grid.getPrefWidth());
		depositButton.setMinWidth(grid.getPrefWidth());
		checkBalance.setMinWidth(grid.getPrefWidth());
		next.setMinWidth(grid.getPrefWidth());
		prev.setMinWidth(grid.getPrefWidth());
		exit.setMinWidth(grid.getPrefWidth());
		back.setMinWidth(grid.getPrefWidth());
		historyLabel.setMinWidth(grid.getPrefWidth());
		historyLabel.setAlignment(Pos.CENTER);

		grid.getChildren().addAll(withdrawButton, depositButton, checkBalance, prev, historyLabel, next, back, exit);
		grid.setConstraints(withdrawButton, 0, 0);
		grid.setConstraints(depositButton, 1, 0);
		grid.setConstraints(checkBalance, 2, 0);
		grid.setConstraints(prev, 0, 2);
		grid.setConstraints(historyLabel, 1, 2);
		grid.setConstraints(next, 2, 2);
		grid.setConstraints(back, 1, 5);
		grid.setConstraints(exit, 1, 4);

		Scene scene2 = new Scene(grid, 500, 300);
		login.setOnAction(e -> {
			result = accNumInput.getText();
			if (result.equals(omar.getAccNumber()))
				window1.setScene(scene2);
			else
				displayAlert("Error", "Invalid Account Number");
		});
		window1.show();

		// Withdraw

		withdrawButton.setOnAction(e -> {
			double withdrawAmount = getAmount("Withdraw", "Enter the amount you would like to withdraw");
			if (flag == 0) {
				if (amount < 0) {
					displayAlert("Error", "Amount can not be less than zero!");
					return;
				}
				if (amount > omar.getBalance()) {
					displayAlert("Error", "Insufficient Funds");
					return;
				} else {
					if (withdrawAmount <= omar.getBalance()) {
						omar.setBalance(omar.getBalance() - withdrawAmount);
						if (history.size() < 5)
							history.addLast(new Transaction(amount, "Withdraw"));
						else {
							history.remove(0);
							history.addLast(new Transaction(amount, "Withdraw"));
						}
						if (!history.isEmpty())
							prev.setDisable(false);
						displayAlert("Success", "Withdraw Successful");
						if (numOfTransactions < 5)
							numOfTransactions++;
						position = numOfTransactions;
						historyLabel.setText("");
						next.setDisable(true);
					}
				}
			}

		});

		// Deposit

		depositButton.setOnAction(e -> {
			double depositAmount = getAmount("Deposit", "Enter the amount you would like to deposit");
			if (flag == 0) {
				if (amount < 0) {
					displayAlert("Error", "Amount can not be less than zero!");
					return;
				} else {
					omar.setBalance(omar.getBalance() + depositAmount);
					if (history.size() < 5)
						history.addLast(new Transaction(amount, "Deposit"));
					else {
						history.remove(0);
						history.addLast(new Transaction(amount, "Deposit"));
					}
					if (numOfTransactions < 5)
						numOfTransactions++;
					position = numOfTransactions;
					historyLabel.setText("");
					if (!history.isEmpty())
						prev.setDisable(false);
					displayAlert("Success", "Deposit Successful");
					next.setDisable(true);
				}
			}
		});
		// Balance Inquiry

		checkBalance.setOnAction(e -> {
			historyLabel.setText("Current Balance: $ " + Double.toString(omar.getBalance()));
			position = numOfTransactions;
			if (history.size() < 5) {
				history.addLast(new Transaction(omar.getBalance(), "Inquiry"));
			} else {
				history.remove(0);
				history.addLast(new Transaction(omar.getBalance(), "Inquiry"));
			}
			if (numOfTransactions < 5)
				numOfTransactions++;
			position = numOfTransactions;
			if (!history.isEmpty())
				prev.setDisable(false);
			next.setDisable(true);
		});

		// History Navigation

		next.setDisable(true);
		if (history.isEmpty())
			prev.setDisable(true);
		prev.setOnAction(e -> {
			if (position > 0) {
				position--;
				historyLabel
						.setText(history.get(position).getTransactionType() + "	$ " + history.get(position).getValue());
				if (position == 0)
					prev.setDisable(true);
			} else {
				prev.setDisable(true);
				displayAlert("Error", "End of Transations");
			}
			next.setDisable(false);
			if (position == numOfTransactions - 1)
				next.setDisable(true);
		});

		next.setOnAction(e -> {
			if (position < numOfTransactions - 1) {
				position++;
				if (position == numOfTransactions - 1)
					next.setDisable(true);
				historyLabel
						.setText(history.get(position).getTransactionType() + "	$ " + history.get(position).getValue());
			} else {
				next.setDisable(true);
				displayAlert("Error", "End of Transations");
			}

			prev.setDisable(false);
		});
		back.setOnAction(e -> window1.setScene(scene1));
		exit.setOnAction(e -> window1.close());
	}

	private void displayAlert(String title, String message) {
		Stage window2 = new Stage();
		window2.setMinWidth(250);
		window2.setTitle(title);
		window2.initModality(Modality.APPLICATION_MODAL);
		VBox layout2 = new VBox(20);
		layout2.setPadding(new Insets(10, 10, 10, 10));
		Button closeAlert = new Button("OK");
		closeAlert.setOnAction(e -> window2.close());
		Label alertLabel = new Label(message);
		layout2.getChildren().addAll(alertLabel, closeAlert);
		layout2.setAlignment(Pos.CENTER);
		Scene alertScene = new Scene(layout2, 200, 200);
		window2.setScene(alertScene);
		window2.showAndWait();
	}

	private double getAmount(String title, String message) {
		int i;
		Stage window3 = new Stage();
		window3.initModality(Modality.APPLICATION_MODAL);
		window3.setTitle(title);
		window3.setMinWidth(250);
		VBox layout3 = new VBox(20);
		layout3.setAlignment(Pos.CENTER);
		layout3.setPadding(new Insets(10, 10, 10, 10));
		Label label2 = new Label(message);
		TextField amountInput = new TextField();
		amountInput.setPromptText("amount");
		Button amountButton = new Button(title);
		Button exitButton = new Button("Cancel");
		Button[] numPad = new Button[10];
		for (i = 0; i < 10; i++) {
			numPad[i] = new Button(Integer.toString(i));
			numPad[i].setMinSize(30, 30);
		}

		numPad[0].setOnAction(e -> amountInput.setText(amountInput.getText() + 0));
		numPad[1].setOnAction(e -> amountInput.setText(amountInput.getText() + 1));
		numPad[2].setOnAction(e -> amountInput.setText(amountInput.getText() + 2));
		numPad[3].setOnAction(e -> amountInput.setText(amountInput.getText() + 3));
		numPad[4].setOnAction(e -> amountInput.setText(amountInput.getText() + 4));
		numPad[5].setOnAction(e -> amountInput.setText(amountInput.getText() + 5));
		numPad[6].setOnAction(e -> amountInput.setText(amountInput.getText() + 6));
		numPad[7].setOnAction(e -> amountInput.setText(amountInput.getText() + 7));
		numPad[8].setOnAction(e -> amountInput.setText(amountInput.getText() + 8));
		numPad[9].setOnAction(e -> amountInput.setText(amountInput.getText() + 9));
		GridPane numberPad = new GridPane();
		for (i = 1; i < 10; i++) {
			numberPad.getChildren().add(numPad[i]);
			numberPad.setConstraints(numPad[i], (i-1) % 3, (i-1) / 3);
		}
		numberPad.getChildren().add(numPad[0]);
		numberPad.setConstraints(numPad[0], 1, 3);
		numberPad.setVgap(5);
		numberPad.setHgap(5);
		BorderPane layout4 = new BorderPane();
		layout4.setTop(layout3);
		layout4.setCenter(numberPad);
		numberPad.setAlignment(Pos.BASELINE_CENTER);
		layout3.getChildren().addAll(label2, amountInput, amountButton);
		StackPane layout5 = new StackPane();
		exitButton.setAlignment(Pos.TOP_CENTER);
		layout5.getChildren().add(exitButton);
		layout5.setAlignment(Pos.TOP_CENTER);
		layout5.setPadding(new Insets(10,10,10,10));
		layout4.setBottom(layout5);
		amountButton.setOnAction(e -> {
			String x = amountInput.getText();
			if (x.isEmpty() || !isNumeric(x))
				displayAlert("Error", "Please Enter a valid number.");
			else {
				amount = Double.parseDouble(x);
				window3.close();
			}
		});
		exitButton.setOnAction(e -> {
			flag = 1;
			window3.close();
		});
		Scene scene3 = new Scene(layout4, 300, 310);
		window3.setOnCloseRequest(e -> flag = 1);
		window3.setScene(scene3);
		window3.showAndWait();
		return amount;
	}

	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
}
