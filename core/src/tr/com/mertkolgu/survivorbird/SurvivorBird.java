package tr.com.mertkolgu.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture background;
    private Texture bird;
    private Texture bee1;
    private Texture bee2;
    private Texture bee3;
    private float birdX = 0;
    private float birdY = 0;
    private float width = 0;
    private float height = 0;
    private int gameState = 0;
    private float velocity = 0;
    private float gravity = 0.1f;
    private float enemyVelocity = 2;
    private Random random;
    private Circle birdCircle;
    private ShapeRenderer shapeRenderer;
    private int score = 0;
    private int scoredEnemy = 0;
    private int numberOfEnemiesSet = 4;
    private float[] enemyX = new float[numberOfEnemiesSet];
    private float distance = 0;
    private float[] enemyOffSet1 = new float[numberOfEnemiesSet];
    private float[] enemyOffSet2 = new float[numberOfEnemiesSet];
    private float[] enemyOffSet3 = new float[numberOfEnemiesSet];
    private Circle[] enemyCircles1;
    private Circle[] enemyCircles2;
    private Circle[] enemyCircles3;
    private BitmapFont font1;
    private BitmapFont font2;

    // oyun açıldığında çalışan metot
    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("background.png");
        bird = new Texture("bird.png");
        bee1 = new Texture("bee.png");
        bee2 = new Texture("bee.png");
        bee3 = new Texture("bee.png");

        birdX = Gdx.graphics.getWidth() / 3 - bird.getHeight() / 3;
        birdY = Gdx.graphics.getHeight() / 3;
        width = Gdx.graphics.getWidth() / 15;
        height = Gdx.graphics.getHeight() / 10;
        distance = Gdx.graphics.getWidth() / 2;
        random = new Random();
        birdCircle = new Circle();
        enemyCircles1 = new Circle[numberOfEnemiesSet];
        enemyCircles2 = new Circle[numberOfEnemiesSet];
        enemyCircles3 = new Circle[numberOfEnemiesSet];
        shapeRenderer = new ShapeRenderer();

        font1 = new BitmapFont();
        font1.setColor(Color.WHITE);
        font1.getData().scale(4);

        font2 = new BitmapFont();
        font2.setColor(Color.WHITE);
        font2.getData().scale(6);

        for (int i = 0; i < numberOfEnemiesSet; i++) {
            enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
            enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

            enemyX[i] = Gdx.graphics.getWidth() + i * distance;

            enemyCircles1[i] = new Circle();
            enemyCircles2[i] = new Circle();
            enemyCircles3[i] = new Circle();
        }
    }

    // oyun devam ettiği sürece devamlı çağrılan metot
    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        } else if (gameState == 1) {
            if (enemyX[scoredEnemy] < birdX) {
                score++;

                if (scoredEnemy < numberOfEnemiesSet - 1) {
                    scoredEnemy++;
                } else {
                    scoredEnemy = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = Gdx.graphics.getHeight() * -0.005f;
            }

            for (int i = 0; i < numberOfEnemiesSet; i++) {
                if (enemyX[i] < -width) {
                    enemyX[i] = enemyX[i] + numberOfEnemiesSet * distance;

                    enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                } else {
                    enemyX[i] = enemyX[i] - enemyVelocity;
                }

                batch.draw(bee1, enemyX[i], (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet1[i], width, height);
                batch.draw(bee2, enemyX[i], (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet2[i], width, height);
                batch.draw(bee3, enemyX[i], (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet3[i], width, height);

                enemyCircles1[i] = new Circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet1[i] + height / 2, width / 2);
                enemyCircles2[i] = new Circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet2[i] + height / 2, width / 2);
                enemyCircles3[i] = new Circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet3[i] + height / 2, width / 2);
            }

            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY = birdY - velocity;
            } else {
                gameState = 2;
            }
        } else if (gameState == 2) {
            font2.draw(batch, "Game Over! Tap to play Again!", 100, Gdx.graphics.getHeight() / 2);

            if (Gdx.input.justTouched()) {
                gameState = 1;
                birdY = Gdx.graphics.getHeight() / 3;

                for (int i = 0; i < numberOfEnemiesSet; i++) {
                    enemyOffSet1[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
                    enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

                    enemyX[i] = Gdx.graphics.getWidth() + i * distance;

                    enemyCircles1[i] = new Circle();
                    enemyCircles2[i] = new Circle();
                    enemyCircles3[i] = new Circle();
                }
                velocity = 0;
                scoredEnemy = 0;
                score = 0;
            }
        }
        batch.draw(bird, birdX, birdY, width, height);
        font1.draw(batch, String.valueOf(score), 100, 200);
        batch.end();

        birdCircle.set(birdX + width / 2, birdY + height / 2, width / 2);
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.BLACK);
        // shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        // shapeRenderer.end();

        for (int i = 0; i < numberOfEnemiesSet; i++) {
            // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // shapeRenderer.setColor(Color.BLACK);
            // shapeRenderer.circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet1[i] + height / 2, width / 2);
            // shapeRenderer.circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet2[i] + height / 2, width / 2);
            // shapeRenderer.circle(enemyX[i] + width / 2, (float) (Gdx.graphics.getHeight() / 2) + enemyOffSet3[i] + height / 2, width / 2);

            if (Intersector.overlaps(birdCircle, enemyCircles1[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
                gameState = 2;
            }
            // shapeRenderer.end();
        }
    }


    @Override
    public void dispose() {
        //
    }
}
