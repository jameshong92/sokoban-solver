import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import static javax.swing.ScrollPaneConstants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

/**
 * The MainFrame class handles the main GUI of the program, and manipulates
 * user input/output.
 * @author Hyun Seung Hong (hh2473)
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private SokobanSolver solver;
	private String questionSelected = " ";
	private JButton submit;
	private JTextArea answerText;
	private JLabel loadingLabel;
	private JComboBox searchMenu; //scroll-down menu for the search methods
	private JComboBox heuristicsMenu; //scroll-down menu for the heuristics
	private char hChoice = ' ';
	private JPanel questionPanel;
	
	// labels and text fields for each search method
	private JLabel questionLabel1 = new JLabel();
	private JLabel questionLabel2 = new JLabel();
	private JTextField questionField1 = new JTextField();
	
	// choices for scroll-down menu
	private String[] choices = {"Breadth-First", "Depth-First", "Uniform-Cost", 
			"Greedy", "A*"}; 
	
	private String[] hChoices = {"Manhattan", "Euclidean", 
			"Hungarian", "Max{h1, h2, h3}"};
	
	/**
	 * Calls init() method, and sets the values for main frame
	 * @throws IOException
	 */
	public MainFrame() throws IOException {
		init();
		setSize(700, 500);
		setTitle("Sokoban Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false); //disabling the ability to change the frame size
		setVisible(true);
		submit.setEnabled(false); // disable submit button while parsing data
		solver = new SokobanSolver();
		submit.setEnabled(true); // enable button after parsing is finished
	}

	/**
	 * Initializes main frame and its panels
	 * @throws IOException
	 */
	private void init() throws IOException {
		JPanel mainPanel = (JPanel) getContentPane();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getTopPanel(), BorderLayout.NORTH); // question and logo
		mainPanel.add(getAnswerPanel(), BorderLayout.CENTER); //answer panel
		mainPanel.add(getLoadingPanel(), BorderLayout.SOUTH); //loading panel
	}

	/**
	 * Adds loading status message at the bottom (south) of the frame
	 * @return loadingPanel
	 */
	private JPanel getLoadingPanel() {
		JPanel loadingPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		loadingPanel.setLayout(layout);
		loadingPanel.setPreferredSize(new Dimension(650,50));
		
		//set appropriate string to each label and add to panel
		loadingLabel = new JLabel();
		loadingLabel.setText(" ");
		loadingPanel.add(loadingLabel);
		
		// set constraints and spaces between labels
		layout.putConstraint(SpringLayout.WEST, loadingLabel,
                10, SpringLayout.WEST, loadingPanel);
		layout.putConstraint(SpringLayout.NORTH, loadingLabel,
                15, SpringLayout.NORTH, loadingPanel);
		return loadingPanel;
	}

	/**
	 * Adds JTextArea to the center of the frame to display 
	 * answers for selected question
	 * @return answerPanel
	 */
	private JScrollPane getAnswerPanel() {
		JPanel answerPanel = new JPanel();
		answerPanel.setLayout(new BorderLayout());
		answerText = new JTextArea();
		answerText.setText("");
		answerText.setSize(new Dimension(650, 350));
		answerText.setEditable(false); //disable user editing function
		answerText.setLineWrap(true); //wrap lines when item name is too long
		answerPanel.add(answerText);
		JScrollPane answerPane = new JScrollPane(answerPanel);
		//putting answerPanel into JScrollPane to allow scrolls to appear
		answerPane.setSize(new Dimension(650,350));
		answerPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		answerPane.setBorder(BorderFactory.createTitledBorder("Answer"));
		return answerPane;
	}

	/**
	 * Adds banner and question selecting menu to the top (north) of the frame
	 * @return topPanel
	 * @throws IOException
	 */
	private JPanel getTopPanel() throws IOException {
		JPanel topPanel = new JPanel();
		topPanel.setSize(new Dimension(650, 100));
		topPanel.setLayout(new GridLayout(2, 1));
		
		// add Factbook logo on the top grid
		Image logo = ImageIO.read(new File("factbook-logo.png"));
		Image resizedLogo = logo.getScaledInstance(650, 50,
				Image.SCALE_SMOOTH); // resize image to fit the frame
		JLabel picLabel = new JLabel(new ImageIcon(resizedLogo));
		picLabel.setSize(650,50);
		topPanel.add(picLabel);
		
		// add questionPanel on the bottom grid
		questionPanel = new JPanel();
		questionPanel.setLayout(new BorderLayout());
		questionPanel.setSize(650, 50);
		
		// add drop-down menu for questions into questionPanel
		searchMenu = new JComboBox(choices);
		searchMenu.setSize(80, 40);
		searchMenu.setEditable(false);
		updateQuestion("default");
		
		// add actionListener to the drop-down menu
		searchMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JComboBox comboBox = (JComboBox) event.getSource();
					questionSelected = comboBox.getSelectedItem().toString();
					updateQuestion(questionSelected.toLowerCase());
				}
			});
		
		heuristicsMenu = new JComboBox(hChoices);
		heuristicsMenu.setSize(80, 40);
		heuristicsMenu.setEditable(false);
		heuristicsMenu.setVisible(false);
		
		// add actionListener to the drop-down menu
		heuristicsMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					JComboBox comboBox = (JComboBox) event.getSource();
					String selected = comboBox.getSelectedItem().toString();
					if (selected.contains("Max")) {
						hChoice = 'x';
					}
					else
						hChoice = selected.charAt(0);
				}
			});
		
		// add submit button to questionPanel
		submit = new JButton("Solve");
		
		// add mouseListner to submit button
		submit.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
			}
			public void mouseEntered(MouseEvent arg0) {
			}
			public void mouseExited(MouseEvent arg0) {
			}
			public void mouseReleased(MouseEvent arg0) {
			}

			// display loading message when mouse is pressed
			public void mousePressed(MouseEvent arg0) {
				if (submit.isEnabled()) {
					displaySolvingMessage(questionSelected);
				}
			}
		});
		
		// add actionListner to submit button
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// get answer from the SokobanSolver, and display it
				// on the center panel
				try {
					int numPlayer = solver.loadFile(questionField1.getText(), hChoice);
					if (numPlayer != 1) {
						displayMessage("Please have only 1 player in the puzzle");
					}
					else {
						char method = Character.toLowerCase(questionSelected.charAt(0));
						String answer = solver.solve(method);
						System.out.println(answer);
						answerText.setText(answer);
						String runtime = answer.substring(answer.indexOf("units")+7);
						String message = questionSelected + " search. Total runtime : " + runtime;
						if (answer.contains("Failed"))
							displayMessage("No solution found using " + message);
						else
							displayMessage("Solution found using " + message);
						repaint();
					}
				} catch (NumberFormatException e) {
					displayMessage("File incorrectly formatted! First line needs to " +
							"contain total number of rows in the puzzle");
				} catch (FileNotFoundException e) {
					displayMessage("File: \"" + questionField1.getText() + "\" not found!");
				} catch (NoSuchElementException e) {
					displayMessage("File incorrectly formatted! First line needs to " +
							"contain total number of rows in the puzzle");
				}
			}
		});
		
		// add question labels and fields to questionPanel
		SpringLayout layout = new SpringLayout();
		JPanel labelPanel = new JPanel(layout);
		labelPanel.add(questionLabel1);
		labelPanel.add(questionField1);
		labelPanel.add(questionLabel2);
		labelPanel.add(heuristicsMenu);
		setLayoutBounds(layout, labelPanel); // set bounds between each labels
		questionPanel.add(searchMenu, BorderLayout.WEST);	
		questionPanel.add(labelPanel, BorderLayout.CENTER);
		questionPanel.add(submit, BorderLayout.EAST);
		topPanel.add(questionPanel);
		return topPanel;
	}

	/**
	 * Displays appropriate solving message while system is solving 
	 * the puzzle using selected search method
	 * @param message
	 */
	private void displaySolvingMessage(String message) {
		loadingLabel.setText("Solving the puzzle using " + message + " search...");
		repaint();
	}

	/**
	 * Displays appropriate message
	 */
	private void displayMessage(String message) {
		loadingLabel.setText(message);
		repaint();
	}

	/**
	 * Sets bounds between the labels and textFields to neatly 
	 * display each question
	 * @param layout
	 * @param labelPanel
	 */
	private void setLayoutBounds(SpringLayout layout, JPanel labelPanel) {
		layout.putConstraint(SpringLayout.WEST, questionLabel1,
                5, SpringLayout.WEST, labelPanel);
		layout.putConstraint(SpringLayout.NORTH, questionLabel1,
                17, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, questionField1,
                1, SpringLayout.EAST, questionLabel1);
		layout.putConstraint(SpringLayout.NORTH, questionField1,
                15, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, questionLabel2,
                1, SpringLayout.EAST, questionField1);
		layout.putConstraint(SpringLayout.NORTH, questionLabel2,
                17, SpringLayout.NORTH, labelPanel);
		layout.putConstraint(SpringLayout.WEST, heuristicsMenu,
                1, SpringLayout.EAST, questionLabel2);
		layout.putConstraint(SpringLayout.NORTH, heuristicsMenu,
                11, SpringLayout.NORTH, labelPanel);
	}

	/**
	 * Updates labels and textFields according to the selected question 
	 * to display appropriate string
	 * @param selectedQuestion
	 */
	private void updateQuestion(String selected) {
		questionLabel1.setText("Enter the filename: ");
		questionField1.setText("[filename]");
		questionField1.setPreferredSize(new Dimension(80, 20));
		questionField1.setVisible(true);
		if (selected.equals("a*")||selected.equals("greedy")) {
			questionLabel2.setText(". Heuristics: ");
			heuristicsMenu.setVisible(true);
		}
		else if (selected.equals("breadth-first")||selected.equals("depth-first")||
				selected.equals("uniform-cost")) {
			questionLabel2.setText(".");
			heuristicsMenu.setVisible(false);
		}
		else {
			questionLabel1.setText("Please select a search method from the left.");
			questionField1.setVisible(false);
			questionLabel2.setText("");
		}
		repaint();
	}
}