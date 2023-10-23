package fr.gab400.improvidentlauncher;

import javafx.application.Application;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		try {
			Class.forName("javafx.application.Application");
			Application.launch(ImprovidentLauncher.class, args);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
