import javax.imageio.IIOException;
import javax.swing.*;
import java.awt.*;
import java.io.*;

import static jdk.jfr.consumer.EventStream.openFile;

public class Notepad extends JFrame {
    private JTextArea text;
    private JFileChooser fileChooser;
    private JDialog findReplaceDialog;
    private JTextField findField;
    private JTextField replaceField;

    public Notepad(){
        super("Notepad Application");
        setSize(800,600);

        text = new JTextArea();
        fileChooser = new JFileChooser();


        add(new JScrollPane(text), BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
        setVisible(true);
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar =new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...");
        JMenuItem exitMenuItem = new JMenuItem("Exit");


        openMenuItem.addActionListener(e -> openFile());
        saveMenuItem.addActionListener(e -> saveFile());
        saveAsMenuItem.addActionListener(e -> saveFileAs());

        //Keyboard shortcuts
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke("control O"));
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke("control S"));
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke("control shift S"));

        JMenu editMenu = new JMenu("Edit");
        JMenuItem findReplaceMenuItem = new JMenuItem("Find & Replace");

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(exitMenuItem);
        editMenu.add(findReplaceMenuItem);

        findReplaceMenuItem.addActionListener(e -> showFindReplaceDialog());


        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;

    }

    private void showFindReplaceDialog() {
        if (findReplaceDialog == null) {
            findReplaceDialog = new JDialog(this, "Find & Replace", false);
            findReplaceDialog.setLayout(new FlowLayout());
            findReplaceDialog.setSize(300, 300);
            findField = new JTextField(10);
            replaceField = new JTextField(10);
            JButton findButton = new JButton("Find");
            JButton replaceButton = new JButton("Replace");
            findButton.addActionListener(e -> findText());
            replaceButton.addActionListener(e -> replaceText());

            findReplaceDialog.add(new JLabel("Find:"));
            findReplaceDialog.add(findField);
            findReplaceDialog.add(new JLabel("Replace"));
            findReplaceDialog.add(replaceField);
            findReplaceDialog.add(findButton);
            findReplaceDialog.add(replaceButton);

            findReplaceDialog.setVisible(true);
        }
    }

        private Object replaceText(){
            return null;
        }

        private void findText(){
            String find = findField.getText();
            if(find.isEmpty())
                return;

            int startIndex = text.getText().indexOf(find,text.getCaretPosition());
            if(startIndex >= 0){
                text.setCaretPosition(startIndex + find.length());
                text.select(startIndex,startIndex + find.length());
                text.requestFocusInWindow();
            }

        }

        private void openFile(){
            if(fileChooser.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                try{
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    text.read(reader, null);
                }catch(FileNotFoundException e){
                    JOptionPane.showMessageDialog(this, "Can not open file !","Error",JOptionPane.ERROR_MESSAGE);
                }catch (IOException e){
                    JOptionPane.showMessageDialog(this, "Reading file I/O Error !","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    private void saveFile() {
        if(fileChooser.getSelectedFile() == null) {
            saveFileAs();
        } else {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                text.write(writer);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Can not save file !", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }


    }
        private void saveFileAs() {
            if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    text.write(writer);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Can not save this file !", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad());

    }
}
