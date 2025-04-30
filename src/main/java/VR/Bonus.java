package VR;

import java.util.Random;
import com.jogamp.opengl.GL2;

public class Bonus extends GameObject {
    private float speed;
    private Random random;

    public Bonus() {
        random = new Random();
        x = (random.nextFloat() - 0.5f) * 5;
        y = 0.0f;
        z = -70.0f;
        speed = 0.15f;
    }

    @Override
    public void update() {
        z += speed;
        if (z > 5) {
            reset();
        }
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        // ✅ Définir les propriétés de matériau métallique
        float[] ambient = {0.25f, 0.25f, 0.25f, 1.0f};
        float[] diffuse = {0.4f, 0.4f, 0.4f, 1.0f};
        float[] specular = {0.774597f, 0.774597f, 0.774597f, 1.0f};
        float shininess = 76.8f;

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, ambient, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, shininess);

        // ✅ Dessiner un cube métal gris
        gl.glBegin(GL2.GL_QUADS);

        // Face avant
        gl.glVertex3f(-0.3f, -0.3f, 0.3f);
        gl.glVertex3f( 0.3f, -0.3f, 0.3f);
        gl.glVertex3f( 0.3f,  0.3f, 0.3f);
        gl.glVertex3f(-0.3f,  0.3f, 0.3f);

        // Ajoute les autres faces pour un vrai cube (optionnel mais conseillé)
        // Face arrière
        gl.glVertex3f(-0.3f, -0.3f, -0.3f);
        gl.glVertex3f( 0.3f, -0.3f, -0.3f);
        gl.glVertex3f( 0.3f,  0.3f, -0.3f);
        gl.glVertex3f(-0.3f,  0.3f, -0.3f);

        // Côtés
        gl.glVertex3f(-0.3f, -0.3f, -0.3f);
        gl.glVertex3f(-0.3f, -0.3f,  0.3f);
        gl.glVertex3f(-0.3f,  0.3f,  0.3f);
        gl.glVertex3f(-0.3f,  0.3f, -0.3f);

        gl.glVertex3f(0.3f, -0.3f, -0.3f);
        gl.glVertex3f(0.3f, -0.3f,  0.3f);
        gl.glVertex3f(0.3f,  0.3f,  0.3f);
        gl.glVertex3f(0.3f,  0.3f, -0.3f);

        // Dessus
        gl.glVertex3f(-0.3f, 0.3f, -0.3f);
        gl.glVertex3f( 0.3f, 0.3f, -0.3f);
        gl.glVertex3f( 0.3f, 0.3f,  0.3f);
        gl.glVertex3f(-0.3f, 0.3f,  0.3f);

        // Dessous
        gl.glVertex3f(-0.3f, -0.3f, -0.3f);
        gl.glVertex3f( 0.3f, -0.3f, -0.3f);
        gl.glVertex3f( 0.3f, -0.3f,  0.3f);
        gl.glVertex3f(-0.3f, -0.3f,  0.3f);

        gl.glEnd();
        gl.glPopMatrix();
    }

    public void reset() {
        x = (random.nextFloat() - 0.5f) * 5;
        z = -70.0f;
    }
}
