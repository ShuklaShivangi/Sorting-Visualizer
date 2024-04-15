import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SortingVisualizer extends JFrame {
    private static final int DEFAULT_ARRAY_SIZE = 50;
    private static final int DEFAULT_ARRAY_MAX_VALUE = 500;
    private static final int DEFAULT_DELAY_MS = 50;
    
    private int arraySize;
    private int arrayMaxValue;
    
    @SuppressWarnings("unused")
    private int delayMs;

    private int[] array;
    private SortAlgorithm sortAlgorithm;
    private JLabel statusLabel;
    private JButton startButton;
    private JComboBox<String> algorithmComboBox;
    private JSlider arraySizeSlider;
    private JSlider algorithmSpeedSlider;
    private JLabel timeComplexityLabel;
    private JLabel spaceComplexityLabel;

    public SortingVisualizer() {
        setTitle("Sorting Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize default values
        arraySize = DEFAULT_ARRAY_SIZE;
        arrayMaxValue = DEFAULT_ARRAY_MAX_VALUE;
        delayMs = DEFAULT_DELAY_MS;

        initializeArray();
        initializeUI();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeArray() {
        array = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            array[i] = (int) (Math.random() * arrayMaxValue);
        }
    }

    private void initializeUI() {
        // Create the main panel to hold the visualizer and control panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Create the visualizer panel on the right side (3/4th of the screen width)
        JPanel visualizerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int barWidth = getWidth() / array.length;
                for (int i = 0; i < array.length; i++) {
                    int barHeight = (int) (((double) array[i] / arrayMaxValue) * getHeight());
                    g.fillRect(i * barWidth, getHeight() - barHeight, barWidth, barHeight);
                }
            }
        };
        visualizerPanel.setBackground(Color.WHITE);
        mainPanel.add(visualizerPanel);

        // Create the control panel on the left side (1/4th of the screen width)
        JPanel controlPanel = new JPanel(new GridLayout(0, 1));

        startButton = new JButton("Start Sorting");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSorting();
            }
        });
        controlPanel.add(startButton);

        algorithmComboBox = new JComboBox<>();
        algorithmComboBox.addItem("Bubble Sort");
        algorithmComboBox.addItem("Selection Sort");
        algorithmComboBox.addItem("Insertion Sort");
        algorithmComboBox.addItem("Merge Sort");
        algorithmComboBox.addItem("Quick Sort");
        controlPanel.add(algorithmComboBox);

        arraySizeSlider = new JSlider(JSlider.HORIZONTAL, 10, 200, arraySize);
        arraySizeSlider.setMajorTickSpacing(50);
        arraySizeSlider.setPaintTicks(true);
        arraySizeSlider.setPaintLabels(true);
        arraySizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                arraySize = arraySizeSlider.getValue();
                initializeArray();
                repaint();
            }
        });
        controlPanel.add(new JLabel("Array Size:"));
        controlPanel.add(arraySizeSlider);

        algorithmSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 100, DEFAULT_DELAY_MS);
        algorithmSpeedSlider.setMajorTickSpacing(25);
        algorithmSpeedSlider.setPaintTicks(true);
        algorithmSpeedSlider.setPaintLabels(true);
        algorithmSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                delayMs = algorithmSpeedSlider.getValue();
            }
        });
        controlPanel.add(new JLabel("Algorithm Speed:"));
        controlPanel.add(algorithmSpeedSlider);

        timeComplexityLabel = new JLabel("Time Complexity:");
        spaceComplexityLabel = new JLabel("Space Complexity:");
        controlPanel.add(timeComplexityLabel);
        controlPanel.add(spaceComplexityLabel);

        statusLabel = new JLabel("Press 'Start Sorting' to begin");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        controlPanel.add(statusLabel);

        mainPanel.add(controlPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void startSorting() {
        if (sortAlgorithm != null && sortAlgorithm.isSorting()) {
            return;
        }
        sortAlgorithm = new SortAlgorithm();
        sortAlgorithm.start();
    }

    private class SortAlgorithm extends Thread {
        private boolean sorting;

        public boolean isSorting() {
            return sorting;
        }

        @Override
        public void run() {
            sorting = true;
            String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
            switch (selectedAlgorithm) {
                case "Bubble Sort":
                    bubbleSort();
                    break;
                case "Selection Sort":
                    selectionSort();
                    break;
                case "Insertion Sort":
                    insertionSort();
                    break;
                case "Merge Sort":
                    mergeSort(0, array.length - 1);
                    break;
                case "Quick Sort":
                    quickSort(0, array.length - 1);
                    break;
            }
            repaint();
            statusLabel.setText("Sorting complete");
            sorting = false;
        }

        private void bubbleSort() {
            long startTime = System.nanoTime();
            boolean swapped;
            do {
                swapped = false;
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] > array[i + 1]) {
                        int temp = array[i];
                        array[i] = array[i + 1];
                        array[i + 1] = temp;
                        swapped = true;
                    }
                }
            } while (swapped);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            timeComplexityLabel.setText("Bubble Sort - Time Complexity: Best: O(n), Average: O(n^2), Worst: O(n^2)");
            spaceComplexityLabel.setText("Bubble Sort - Space Complexity: O(1)");
            System.out.println("Time taken for Bubble Sort: " + timeElapsed + " nanoseconds");
        }

        private void selectionSort() {
            long startTime = System.nanoTime();
            for (int i = 0; i < array.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                }
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            timeComplexityLabel.setText("Selection Sort - Time Complexity: Best: O(n^2), Average: O(n^2), Worst: O(n^2)");
            spaceComplexityLabel.setText("Selection Sort - Space Complexity: O(1)");
            System.out.println("Time taken for Selection Sort: " + timeElapsed + " nanoseconds");
        }

        private void insertionSort() {
            long startTime = System.nanoTime();
            for (int i = 1; i < array.length; ++i) {
                int key = array[i];
                int j = i - 1;

                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j = j - 1;
                }
                array[j + 1] = key;
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            timeComplexityLabel.setText("Insertion Sort - Time Complexity: Best: O(n), Average: O(n^2), Worst: O(n^2)");
            spaceComplexityLabel.setText("Insertion Sort - Space Complexity: O(1)");
            System.out.println("Time taken for Insertion Sort: " + timeElapsed + " nanoseconds");
        }

        private void mergeSort(int low, int high) {
            long startTime = System.nanoTime();
            if (low < high) {
                int mid = (low + high) / 2;
                mergeSort(low, mid);
                mergeSort(mid + 1, high);
                merge(low, mid, high);
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            timeComplexityLabel.setText("Merge Sort - Time Complexity: O(n log n)");
            spaceComplexityLabel.setText("Merge Sort - Space Complexity: O(n)");
            System.out.println("Time taken for Merge Sort: " + timeElapsed + " nanoseconds");
        }

        private void merge(int low, int mid, int high) {
            int[] temp = new int[array.length];
            for (int i = low; i <= high; i++) {
                temp[i] = array[i];
            }

            int i = low, j = mid + 1, k = low;
            while (i <= mid && j <= high) {
                if (temp[i] <= temp[j]) {
                    array[k++] = temp[i++];
                } else {
                    array[k++] = temp[j++];
                }
            }

            while (i <= mid) {
                array[k++] = temp[i++];
            }
        }

        private void quickSort(int low, int high) {
            long startTime = System.nanoTime();
            if (low < high) {
                int pi = partition(low, high);
                quickSort(low, pi - 1);
                quickSort(pi + 1, high);
            }
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            timeComplexityLabel.setText("Quick Sort - Time Complexity: Best: O(n log n), Average: O(n log n), Worst: O(n^2)");
            spaceComplexityLabel.setText("Quick Sort - Space Complexity: O(log n)");
            System.out.println("Time taken for Quick Sort: " + timeElapsed + " nanoseconds");
        }

        private int partition(int low, int high) {
            int pivot = array[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (array[j] < pivot) {
                    i++;
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
            int temp = array[i + 1];
            array[i + 1] = array[high];
            array[high] = temp;
            return i + 1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortingVisualizer::new);
    }
}