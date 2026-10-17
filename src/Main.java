
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {

    public static void main(String[] args) {
       //
        SwingUtilities.invokeLater(() -> {
            //o Jframe abaixo é o titulo que aparece no topo da janela do jogo
            JFrame janela = new JFrame("Mini Mario Java");

            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setResizable(false);
            janela.add(new Jogo());

            janela.pack();
            janela.setLocationRelativeTo(null); //faz a janela ficar no meio da tela
            janela.setVisible(true); //sem esse comando a janela não fica visivel ao usuario
        });
    }

    static class Jogo extends JPanel {

        private final int largura = 800;
        private final int altura = 600;

        private int x = 100;
        private int y = 400;

        private final int tamanho = 32;

        private double velocidadeY = 0; //garante o movimento apenas para os lados quando está em 0, se fosse positivo o personagem se mexeria para baixo e neegativo andaria para cima

        private final double gravidade = 0.6;
        private final double forcaPulo = -12; //precisa ser negativo se não o personagem não pula

        private boolean esquerda = false; //a tecla não está pressionada no momento
        private boolean direita = false;  //a tecla não está pressionada no momento
        private boolean noChao = false;  //o personagem nasce no ar e depois vai para o chão
        private final int chaoY = 500;

        public Jogo() {
            setPreferredSize(new Dimension(largura, altura));
            setFocusable(true); //permite interação com comandos do teclado(ir para direita esquerda e pular)

            configurarTeclas();

            Timer timer = new Timer(16, e -> atualizar());
            timer.start();
        }

        private void configurarTeclas() {
            // Andar para a esquerda
            getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("pressed LEFT"), "esquerdaOn");
            getActionMap().put("esquerdaOn", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    esquerda = true;
                }
            });

            getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("released LEFT"), "esquerdaOff");
            getActionMap().put("esquerdaOff", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    esquerda = false;
                }
            });

            // Andar para a direita
            getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("pressed RIGHT"), "direitaOn");
            getActionMap().put("direitaOn", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    direita = true;
                }
            });

            getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("released RIGHT"), "direitaOff");
            getActionMap().put("direitaOff", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    direita = false;
                }
            });

            // Pular com a barra de espaço
            getInputMap(WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke("pressed SPACE"), "pular");
            getActionMap().put("pular", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (noChao) {
                        velocidadeY = forcaPulo;
                        noChao = false;
                    }
                }
            });
        }

        private void atualizar() {
            // Movimento horizontal
            if (esquerda) {
                x -= 4;
            }

            if (direita) {
                x += 4;
            }

            // Impedir que o personagem saia da janela
            x = Math.max(0, Math.min(x, largura - tamanho));

            // Gravidade
            velocidadeY += gravidade;
            y += (int) velocidadeY;

            // Colisão com o chão
            if (y + tamanho >= chaoY) {
                y = chaoY - tamanho;
                velocidadeY = 0;
                noChao = true;
            } else {
                noChao = false;
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Céu
            g.setColor(new Color(100, 180, 255));
            g.fillRect(0, 0, largura, altura);

            // Nuvens
            g.setColor(Color.WHITE);
            g.fillRect(100, 80, 60, 20);
            g.fillRect(120, 60, 30, 20);
            g.fillRect(500, 120, 70, 20);
            g.fillRect(520, 100, 30, 20);

            // Chão de grama
            g.setColor(new Color(70, 190, 60));
            g.fillRect(0, chaoY, largura, 20);

            // Terra
            g.setColor(new Color(150, 90, 45));
            g.fillRect(0, chaoY + 20, largura, altura - chaoY);

            // Blocos do chão
            g.setColor(new Color(110, 60, 30));
            for (int bx = 0; bx < largura; bx += 40) {
                g.drawRect(bx, chaoY + 25, 40, 30);
            }

            // Personagem em pixel art
            desenharPersonagem(g);

            // Instruções na tela
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 18));
            g.drawString("MINI MARIO JAVA", 20, 30);

            g.setFont(new Font("Monospaced", Font.PLAIN, 14));
            g.drawString("Setas: mover | ESPACO: pular", 20, 55);
        }

        private void desenharPersonagem(Graphics g) {
            // Boné
            g.setColor(Color.RED);
            g.fillRect(x + 4, y, 24, 8);
            g.fillRect(x, y + 8, 32, 5);

            // Rosto
            g.setColor(new Color(255, 190, 130));
            g.fillRect(x + 6, y + 13, 20, 10);

            // Cabelo
            g.setColor(new Color(100, 50, 20));
            g.fillRect(x + 3, y + 10, 5, 10);

            // Olho
            g.setColor(Color.BLACK);
            g.fillRect(x + 21, y + 15, 3, 3);

            // Camisa
            g.setColor(Color.RED);
            g.fillRect(x + 4, y + 23, 24, 5);

            // Macacão
            g.setColor(new Color(30, 70, 220));
            g.fillRect(x + 6, y + 27, 20, 3);

            // Pernas
            g.setColor(new Color(80, 40, 20));
            g.fillRect(x + 4, y + 29, 10, 3);
            g.fillRect(x + 19, y + 29, 10, 3);
        }
    }
}
