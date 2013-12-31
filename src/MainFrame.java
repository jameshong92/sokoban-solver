import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import static javax.swing.ScrollPaneConstants.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
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
	private JButton submit, next, prev;
	private JTextArea answerText;
	private JLabel loadingLabel, stepLabel;
	private JComboBox searchMenu, heuristicsMenu; //scroll-down menus for search methods and heuristics
	private char hChoice = ' ';
	private JPanel questionPanel;
	
	// labels and text fields for each search method
	private JLabel questionLabel1 = new JLabel();
	private JLabel questionLabel2 = new JLabel();
	private JTextField questionField1 = new JTextField();
	
	private int numRow, numCol, currentStep;
	private String questionSelected = " ";
	private String solution = "";
	private String[] steps;
	
	private HashSet<Coordinate> walls;
	private HashSet<Coordinate> goals;
	private HashSet<Coordinate> boxes;
	private Coordinate player;
	private String[] puzzleStates; // string containing puzzle state for each step
	
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
		updateQuestion("default");
		setSize(700, 500);
		setTitle("Sokoban Solver");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false); //disabling the ability to change the frame size
		setVisible(true);
		solver = new SokobanSolver();
	}

	/**
	 * Initializes main frame and its panels
	 * @throws IOException
	 */
	private void init() throws IOException {
		JPanel mainPanel = (JPanel) getContentPane();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(getTopPanel(), BorderLayout.NORTH); // question panel and logo
		mainPanel.add(getAnswerPanel(), BorderLayout.CENTER); // answer panel
		mainPanel.add(getLoadingPanel(), BorderLayout.SOUTH); // loading panel
		addListeners(); // add listeners for buttons
	}
	

	/**
	 * Adds loading status message at the bottom (south) of the frame
	 * @return loadingPanel
	 */
	private JPanel getLoadingPanel() {
		JPanel loadingPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		loadingPanel.setLayout(layout);
		loadingPanel.setPreferredSize(new Dimension(700,50));
		
		// set appropriate string to each label and add to panel
		loadingLabel = new JLabel();
		loadingLabel.setText(" ");
		
		// set step label
		stepLabel = new JLabel();
		stepLabel.setText("Show steps:");
		stepLabel.setVisible(false);
		
		// add prev & next buttons to control each step
		prev = new JButton("Previous");
		prev.setVisible(false);
		next = new JButton("Next");
		next.setVisible(false);
		
		loadingPanel.add(loadingLabel);
		loadingPanel.add(stepLabel);
		loadingPanel.add(prev);
		loadingPanel.add(next);
		
		// set constraints and spaces between labels
		layout.putConstraint(SpringLayout.WEST, loadingLabel,
                10, SpringLayout.WEST, loadingPanel);
		layout.putConstraint(SpringLayout.NORTH, loadingLabel,
                15, SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, next, -10, 
				SpringLayout.EAST, loadingPanel);
		layout.putConstraint(SpringLayout.NORTH, next, 10, 
				SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, prev, 10, 
				SpringLayout.WEST, next);
		layout.putConstraint(SpringLayout.NORTH, prev, 10, 
				SpringLayout.NORTH, loadingPanel);
		layout.putConstraint(SpringLayout.EAST, stepLabel, 0, 
				SpringLayout.WEST, prev);
		layout.putConstraint(SpringLayout.NORTH, stepLabel, 15, 
				SpringLayout.NORTH, loadingPanel);
		
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
		answerText.setSize(new Dimension(650, 100));
		answerText.setEditable(false); //disable user editing function
		answerText.setLineWrap(true); //wrap lines when item name is too long
		Font font = new Font("Monaco", Font.PLAIN, 12);
        answerText.setFont(font);
		
		answerPanel.add(answerText);
		JScrollPane answerPane = new JScrollPane(answerPanel);
		//putting answerPanel into JScrollPane to allow scrolls to appear
		answerPane.setSize(new Dimension(700,350));
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
		topPanel.setSize(new Dimension(700, 100));
		topPanel.setLayout(new GridLayout(2, 1));
		
		// add Sokoban logo on the top grid
		Image logo = ImageIO.read(new File("img/sokoban-logo.png"));
		Image resizedLogo = logo.getScaledInstance(700, 50,
				Image.SCALE_SMOOTH); // resize image to fit the frame
		JLabel picLabel = new JLabel(new ImageIcon(resizedLogo));
		picLabel.setSize(700,50);
		topPanel.add(picLabel);
		
		// add questionPanel on the bottom grid
		questionPanel = new JPanel();
		questionPanel.setLayout(new BorderLayout());
		questionPanel.setSize(700, 50);
		
		// add drop-down menu for questions into questionPanel
		searchMenu = new JComboBox(choices);
		searchMenu.setSize(80, 40);
		searchMenu.setEditable(false);
		
		heuristicsMenu = new JComboBox(hChoices);
		heuristicsMenu.setSize(80, 40);
		heuristicsMenu.setEditable(false);
		heuristicsMenu.setVisible(false);
		
		// add submit button to the questionPanel
		submit = new JButton("Solve");
		
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
	 * Adds appropriate listeners to buttons and drop-down menus
	 */

	private void addListeners() {
		// add actionListener to the search menu
		searchMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JComboBox comboBox = (JComboBox) event.getSource();
				questionSelected = comboBox.getSelectedItem().toString();
				updateQuestion(questionSelected.toLowerCase());
			}
		});

		// add actionListener to the heuristics menu
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
					stepLabel.setVisible(false);
					prev.setVisible(false);
					next.setVisible(false);
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
						numRow = solver.getRow();
						numCol = solver.getCol();
						goals = solver.getGoals();
						walls = solver.getWalls();
						boxes = solver.getBoxes();
						player = solver.getPlayer();
						currentStep = 0;
						char method = Character.toLowerCase(questionSelected.charAt(0));
						String answer = solver.solve(method);
						System.out.println(answer);
						String[] lines = answer.split("\\r?\\n");
						solution = lines[1];
						steps = solution.split(" ");
						puzzleStates = new String[steps.length+1];
						String totalSteps = lines[2];
						answerText.setText("Solution: " + solution + " " + totalSteps);
						String runtime = answer.substring(answer.indexOf("units")+7);
						String message = questionSelected + " search. Total runtime : " + runtime;
						if (answer.contains("Failed")) {
							displayMessage("No solution found using " + message);
							repaint();
						}
						else {
							displayMessage("Solution found using " + message);
							stepLabel.setVisible(true);
							prev.setVisible(true);
							next.setVisible(true);
							updatePuzzle();
						}
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
		
		// add actionListener to prev button
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (currentStep>0) {
					currentStep -= 1;
					updatePuzzle();
				}
			}
		});
		
		// add actionListener to next button
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// check if out of bounds
				if (currentStep<steps.length) {
					// check if puzzle state has been seen
					if (puzzleStates[currentStep+1]==null) {
						int row = player.row;
						int col = player.col;
						// update player and box (if needed) positions
						if (steps[currentStep].equals("u")) {
							Coordinate checkBox = new Coordinate(row-1, col);
							if (boxes.contains(checkBox)) {
								boxes.remove(checkBox);
								boxes.add(new Coordinate(row-2, col));
							}
							player = new Coordinate(row-1, col);
						}
						else if (steps[currentStep].equals("d")) {
							Coordinate checkBox = new Coordinate(row+1, col);
							if (boxes.contains(checkBox)) {
								boxes.remove(checkBox);
								boxes.add(new Coordinate(row+2, col));
							}
							player = new Coordinate(row+1, col);
						}
						else if (steps[currentStep].equals("l")) {
							Coordinate checkBox = new Coordinate(row, col-1);
							if (boxes.contains(checkBox)) {
								boxes.remove(checkBox);
								boxes.add(new Coordinate(row, col-2));
							}
							player = new Coordinate(row, col-1);
						}
						else if (steps[currentStep].equals("r")) {
							Coordinate checkBox = new Coordinate(row, col+1);
							if (boxes.contains(checkBox)) {
								boxes.remove(checkBox);
								boxes.add(new Coordinate(row, col+2));
							}
							player = new Coordinate(row, col+1);
						}
					}
					currentStep += 1;
					updatePuzzle();
				}
			}
		});
	
	}

	/**
	 * Updates puzzle according to player and box positions
	 */
	private void updatePuzzle() {
		int totalSteps = steps.length;
		String output = "Solution: " + solution + "(total of " + totalSteps + " steps)\n\n";
		output += "Showing step " + currentStep + ":\n";
		// if puzzle state for the current step is never stored, set new puzzle state
		if (puzzleStates[currentStep] == null) {
			String position = "";
			for (int i=0; i<numRow; i++) {
				for (int j=0; j<numCol; j++) {
					Coordinate c = new Coordinate(i, j);
					if (player.equals(c))
						position += "@";
					else if (boxes.contains(c))
						position += "$";
					else if (goals.contains(c))
						position += ".";
					else if (walls.contains(c))
						position += "#";
					else
						position += " ";
				}
				position += "\n";
			}
			output += position;
			puzzleStates[currentStep] = position;
		}
		else
			output += puzzleStates[currentStep];
		answerText.setText(output);
		repaint();
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
                12, SpringLayout.NORTH, labelPanel);
	}
	

	/**
	 * Updates labels and textFields according to the selected question 
	 * to display appropriate string
	 * @param selected String that contains the selected search method
	 */
	private void updateQuestion(String selected) {
		questionLabel1.setText("Enter the filename: ");
		questionField1.setText("[filename]");
		questionField1.setPreferredSize(new Dimension(100, 20));
		questionField1.setVisible(true);
		submit.setEnabled(true);
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
			submit.setEnabled(false);
		}
		repaint();
	}
}