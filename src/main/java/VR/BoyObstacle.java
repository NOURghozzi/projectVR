package VR;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class BoyObstacle extends Obstacle {
    private final GLUT glut;

    public BoyObstacle() {
        super();
        size = 1.0f;
        y = size / 2;
        glut = new GLUT();
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        // Dessiner la tête (sphère)
        gl.glColor3f(1.0f, 0.8f, 0.6f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.7f, 0f);
        glut.glutSolidSphere(0.2f, 20, 20);
        gl.glPopMatrix();

        // Dessiner le corps (cylindre)
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.2f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.15f, 0.4f, 10, 10);
        gl.glPopMatrix();

        // Dessiner les bras (deux cylindres)
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        for (float i = -1f; i <= 1f; i += 2f) {
            gl.glPushMatrix();
            gl.glTranslatef(i * 0.25f, 0.4f, 0f);
            gl.glRotatef(90, 1, 0, 0);
            glut.glutSolidCylinder(0.05f, 0.3f, 10, 10);
            gl.glPopMatrix();
        }

        // Dessiner les jambes (deux cylindres)
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        for (float i = -1f; i <= 1f; i += 2f) {
            gl.glPushMatrix();
            gl.glTranslatef(i * 0.2f, -0.2f, 0f);
            gl.glRotatef(90, 1, 0, 0);
            glut.glutSolidCylinder(0.05f, 0.3f, 10, 10);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}
