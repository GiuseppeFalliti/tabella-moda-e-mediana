import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import java.util.prefs.Preferences;
import java.net.Socket;

public class Application implements Runnable {
    private JFrame frame;
    private static final String WIDTH_KEY = "width";
    private static final String HEIGHT_KEY = "height";
    private static final String POS_X = "x";
    private static final String POS_Y = "y";
    private Container cp;
    private int[][] matrix;
    private JButton Gen_matrix;
    private JButton Median_moda;
    private JTextField modaField;
    private JTextField medianaField;
    private Preferences preferences;
    private JTable table;

    public Application() {
        super();
        frame = new JFrame();
        cp = frame.getContentPane();
        cp.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setTitle("Application");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        preferences = Preferences.userNodeForPackage(Application.class);
        int width = preferences.getInt(WIDTH_KEY, 300);
        int height = preferences.getInt(HEIGHT_KEY, 400);
        int posx = preferences.getInt(POS_X, 100);
        int posy = preferences.getInt(POS_Y, 100);

        frame.setSize(800, 300);
        frame.setLocation(posx, posy);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveUserDimensions();
                System.exit(0);
            }
        });
        this.setupApp();
    }

    private void setupApp() {
        JLabel titleLabel = new JLabel("Gestione matrice");
        Gen_matrix = new JButton("Genera matrice");
        Median_moda = new JButton("Calcola Moda e Mediana");
        modaField = new JTextField(10);
        medianaField = new JTextField(10);
        medianaField.setEditable(false);
        modaField.setEditable(false);

        DefaultTableModel model = new DefaultTableModel(10, 10);
        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 200));

        cp.add(scrollPane);
        cp.add(titleLabel);
        cp.add(Gen_matrix);
        cp.add(Median_moda);
        cp.add(new JLabel("Moda:"));
        cp.add(modaField);
        cp.add(new JLabel("Mediana:"));
        cp.add(medianaField);

        Gen_matrix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread matrixThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        generaMatrix();
                    }
                });
                matrixThread.start();
            }
        });

        Median_moda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread calculationThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mediana_moda(matrix);
                    }
                });
                calculationThread.start();
            }
        });
    }

    private void generaMatrix() {
        matrix = new int[10][10];
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(2001) - 1000;
            }
        }
        // Creazione e popolamento della tabella con i dati della matrice
        DefaultTableModel model = new DefaultTableModel(matrix.length, matrix[0].length);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                model.setValueAt(matrix[i][j], i, j);
            }
        }

        // Aggiornamento della tabella
        table.setModel(model);
    }

    private void mediana_moda(int[][] matrix) {

        int[] mediana = new int[matrix.length * matrix[0].length];
        int index = 0;

        // riempie l array mediana con i valori della matrice
        for (int[] riga : matrix) {
            for (int number : riga) {
                mediana[index++] = number;
            }
        }

        Arrays.sort(mediana);

        // Trova la mediana
        double median;
        int length = mediana.length;
        if (length % 2 == 0) {
            median = (mediana[length / 2 - 1] + mediana[length / 2]) / 2.0;
        } else {
            median = mediana[length / 2];
        }

        medianaField.setText(String.valueOf(median));
    }

    public void saveUserDimensions() {
        preferences.putInt(WIDTH_KEY, frame.getWidth());
        preferences.putInt(HEIGHT_KEY, frame.getHeight());
        preferences.putInt(POS_X, frame.getX());
        preferences.putInt(POS_Y, frame.getY());
    }

    public void startApp(boolean packElements) {
        if (packElements)
            frame.pack();
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Application().startApp(false);
        });
    }

    @Override
    public void run() {

        throw new UnsupportedOperationException("metodo non implementato");
    }
}
