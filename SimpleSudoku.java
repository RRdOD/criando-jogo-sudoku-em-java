import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimpleSudoku {
    private static final int SIZE = 9;
    private JFrame frame;
    private JTextField[][] cells = new JTextField[SIZE][SIZE];

    public SimpleSudoku(String[] args) {
        frame = new JFrame("Sudoku Simples");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(SIZE, SIZE));

        initializeBoard(args);
        
        JButton solveButton = new JButton("Verificar Solução");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSolution();
            }
        });
        
        frame.add(solveButton);
        frame.pack();
        frame.setVisible(true);
    }

    private void initializeBoard(String[] args) {
        // Inicializa o tabuleiro vazio
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                cells[row][col].setEditable(true);
                frame.add(cells[row][col]);
            }
        }

        // Preenche com os valores dos argumentos, se fornecidos
        if (args.length > 0) {
            for (String arg : args) {
                try {
                    String[] parts = arg.split(";");
                    String[] coord = parts[0].split(",");
                    int x = Integer.parseInt(coord[0]);
                    int y = Integer.parseInt(coord[1]);
                    
                    String[] valueInfo = parts[1].split(",");
                    String value = valueInfo[0];
                    boolean fixed = Boolean.parseBoolean(valueInfo[1]);
                    
                    cells[y][x].setText(value);
                    cells[y][x].setEditable(!fixed);
                    if (fixed) {
                        cells[y][x].setBackground(new Color(220, 220, 220));
                    }
                } catch (Exception e) {
                    System.err.println("Argumento inválido: " + arg);
                }
            }
        }
    }

    private void checkSolution() {
        // Verifica se a solução está correta
        if (isValidSolution()) {
            JOptionPane.showMessageDialog(frame, "Parabéns! Solução correta!", "Sudoku", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Ainda não está correto. Continue tentando!", "Sudoku", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean isValidSolution() {
        // Verifica linhas
        for (int row = 0; row < SIZE; row++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int col = 0; col < SIZE; col++) {
                String text = cells[row][col].getText();
                if (text.isEmpty()) return false;
                try {
                    int num = Integer.parseInt(text);
                    if (num < 1 || num > 9 || seen[num]) return false;
                    seen[num] = true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        // Verifica colunas
        for (int col = 0; col < SIZE; col++) {
            boolean[] seen = new boolean[SIZE + 1];
            for (int row = 0; row < SIZE; row++) {
                int num = Integer.parseInt(cells[row][col].getText());
                if (seen[num]) return false;
                seen[num] = true;
            }
        }

        // Verifica subgrades 3x3
        for (int box = 0; box < SIZE; box++) {
            boolean[] seen = new boolean[SIZE + 1];
            int startRow = (box / 3) * 3;
            int startCol = (box % 3) * 3;
            for (int row = startRow; row < startRow + 3; row++) {
                for (int col = startCol; col < startCol + 3; col++) {
                    int num = Integer.parseInt(cells[row][col].getText());
                    if (seen[num]) return false;
                    seen[num] = true;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleSudoku(args);
            }
        });
    }
}