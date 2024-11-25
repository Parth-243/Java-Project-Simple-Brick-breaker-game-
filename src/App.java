import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        JFrame obj = new JFrame();
        Gameplay gameplay = new Gameplay();

        obj.setBounds(100, 100, Gameplay.FRAME_WIDTH, Gameplay.FRAME_HEIGHT);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        obj.add(gameplay);
        obj.setVisible(true);
    }
}
