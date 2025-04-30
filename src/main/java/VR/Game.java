package VR;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.BorderLayout;

public class Game implements GLEventListener, KeyListener {
    private GLJPanel canvas;
    private FPSAnimator animator;
    private Car car;
    private Road road;
    private Camera camera;
    private Sky sky;
    private long gameStartTime = System.currentTimeMillis();

    private Bonus bonus;
    private long lastBonusSpawnTime = System.currentTimeMillis();
    private final long BONUS_SPAWN_INTERVAL = 10000; // toutes les 10s

    private List<GameObject> gameObjects;
    private ScoreManager scoreManager;
    private ForestBackground forestBackground;
    private boolean gameOver = false;
    private JFrame frame;

    private long lastSpawnTime = System.currentTimeMillis();
    private float spawnInterval = 4.0f;
    private final int MAX_OBSTACLES = 4;
    private List<float[]> activeObstaclePositions = new ArrayList<>();

    public Game(JFrame frame) {
        this.frame = frame;

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);

        animator = new FPSAnimator(canvas, 60);

        car = new Car();
        road = new Road();
        camera = new Camera(car);
        scoreManager = new ScoreManager();
        forestBackground = new ForestBackground();
        gameObjects = new ArrayList<>();
        sky = new Sky();
        bonus = new Bonus();

        gameObjects.add(new BoyObstacle());
        gameObjects.add(new TomatoObstacle());
        gameObjects.add(new CatObstacle());
    }

    public GLJPanel getCanvas() {
        return canvas;
    }
//Démarre l'animation du jeu.
    public void start() {
        animator.start();
        canvas.requestFocusInWindow();
    }
//Initialisation d'OpenGL.
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }
//la fermeture du jeu.
    @Override
    public void dispose(GLAutoDrawable drawable) {}
//l'intervalle de génération des obstacles.
    private void updateSpawnInterval() {
        Random rand = new Random();
        spawnInterval = 4.0f + rand.nextFloat(); // 4.0 to 5.0
    }
// déjà occupée par un autre obstacle.
    private boolean isPositionOccupied(float x, float z) {
        final float MIN_DISTANCE = 0.5f;
        for (float[] pos : activeObstaclePositions) {
            float dx = pos[0] - x;
            float dz = pos[1] - z;
            if (Math.sqrt(dx * dx + dz * dz) < MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }
   // Génère un nouvel obstacle à une position aléatoire.
    private void spawnNewObstacle() {
        if (gameObjects.size() < MAX_OBSTACLES) {
            Random rand = new Random();
            float x = rand.nextInt(3) - 1;
            float z = car.getZ() - 40.0f;
            float y = 0.35f;

            if (!isPositionOccupied(x, z)) {
                GameObject newObstacle = getRandomObstacle(x, y, z);
                gameObjects.add(newObstacle);
                activeObstaclePositions.add(new float[]{x, z});
            }
        }
    }
//Génère un obstacle aléatoire parmi trois types différents
    private GameObject getRandomObstacle(float x, float y, float z) {
        Random rand = new Random();
        int choice = rand.nextInt(3);

        GameObject obj;
        switch (choice) {
            case 0: obj = new TomatoObstacle(); break;
            case 1: obj = new BoyObstacle(); break;
            default: obj = new CatObstacle(); break;
        }

        obj.setX(x);
        obj.setY(y);
        obj.setZ(z);
        return obj;
    }

    private void showWinScreen() {
        JOptionPane.showMessageDialog(null, "You Win!");
    }

    private void showRestartMenu() {
        frame.getContentPane().removeAll();
        RestartMenu restartMenu = new RestartMenu(frame, animator);
        frame.getContentPane().add(restartMenu.getPanel(), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        sky.draw(gl);

        if (!gameOver) {
            road.update();
            car.update();
            forestBackground.update();

            // Gestion du bonus
            long bonusDelta = System.currentTimeMillis() - lastBonusSpawnTime;
            if (bonusDelta >= BONUS_SPAWN_INTERVAL) {
                bonus.reset();
                lastBonusSpawnTime = System.currentTimeMillis();
            }

            bonus.update();

            if (CollisionDetector.checkCollision(car, bonus)) {
                scoreManager.incrementScoreBy(5); // Ajoute 5 points
                System.out.println("Bonus collected!");
                Sound.playEffect("/sons/Bonus.wav");
                bonus.reset();
            }

            long currentTime = System.currentTimeMillis();
            long elapsedTime = (currentTime - gameStartTime) / 1000;
            float deltaTime = (currentTime - lastSpawnTime) / 1000f;

            if (elapsedTime >= 60) {
                gameOver = true;
                animator.stop();
                System.out.println("Win!");
                Sound.playEffect("/sons/Win.wav");
                showWinScreen();
                showRestartMenu();
                return;
            }

            if (deltaTime >= spawnInterval) {
                spawnNewObstacle();
                lastSpawnTime = currentTime;
                updateSpawnInterval();
            }

            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject obj = gameObjects.get(i);
                obj.update();

                if (CollisionDetector.checkCollision(car, obj)) {
                    gameOver = true;
                    animator.stop();
                    Sound.playEffect("/sons/Lose.wav");
                    showRestartMenu();
                    break;
                } else if (!obj.isPassed() && obj.getZ() > car.getZ() + 1.0f) {
                    scoreManager.incrementScore();
                    obj.setPassed(true);
                }
            }

            scoreManager.update();
        }

        camera.update();
        camera.applyView(gl);

        forestBackground.draw(gl);
        road.draw(gl);
        car.draw(gl);

        for (GameObject obj : gameObjects) {
            obj.draw(gl);
        }

        // Dessiner le bonus
        bonus.draw(gl);

        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        scoreManager.render(gl, width, height);

        if (gameOver) {
            scoreManager.renderGameOver(gl, width, height);
        }
    }

    private void restartGame() {
        frame.getContentPane().removeAll();
        Game newGame = new Game(frame);
        GLJPanel canvas = newGame.getCanvas();
        frame.getContentPane().add(canvas);
        frame.revalidate();
        frame.repaint();
        newGame.start();
        canvas.requestFocusInWindow();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(60.0, (double) width / height, 0.1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        car.handleKeyPress(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        car.handleKeyRelease(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
