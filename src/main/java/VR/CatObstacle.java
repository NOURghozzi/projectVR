package VR;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class CatObstacle extends Obstacle {

    private final GLUT glut;

    public CatObstacle() {
        super();
        size = 1.0f;
        y = size / 2;
        glut = new GLUT();
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);  // Positionner l'obstacle

        // Dessiner la tête du chat (sphère)
        gl.glColor3f(0.8f, 0.7f, 0.6f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.7f, 0f);
        glut.glutSolidSphere(0.2f, 20, 20);
        gl.glPopMatrix();

        // Dessiner le corps du chat (cylindre)
        gl.glColor3f(0.8f, 0.7f, 0.6f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, 0f);
        gl.glRotatef(90, 1, 0, 0);
        glut.glutSolidCylinder(0.2f, 0.6f, 10, 10);
        gl.glPopMatrix();

        // Dessiner les oreilles du chat (deux cônes)
        gl.glColor3f(0.8f, 0.7f, 0.6f);
        for (float i = -0.1f; i <= 0.1f; i += 0.2f) {
            gl.glPushMatrix();
            gl.glTranslatef(i, 1.0f, 0f);
            gl.glRotatef(-45, 0, 0, 1);
            glut.glutSolidCone(0.1f, 0.2f, 10, 10);
            gl.glPopMatrix();
        }

        // Dessiner les yeux (deux petites sphères)
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        for (float i = -0.08f; i <= 0.08f; i += 0.16f) {
            gl.glPushMatrix();
            gl.glTranslatef(i, 0.8f, 0.18f);
            glut.glutSolidSphere(0.03f, 10, 10);
            gl.glPopMatrix();
        }

        // Dessiner le nez (petite sphère)
        gl.glColor3f(0.9f, 0.2f, 0.2f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.75f, 0.2f);
        glut.glutSolidSphere(0.05f, 10, 10);
        gl.glPopMatrix();

        // Dessiner la queue du chat (cylindre courbé)
        gl.glColor3f(0.8f, 0.7f, 0.6f);
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -0.4f);
        gl.glRotatef(90, 1, 0, 0);
        gl.glRotatef(30, 0, 0, 1);
        glut.glutSolidCylinder(0.05f, 0.4f, 10, 10);
        gl.glPopMatrix();

        // Dessiner les pattes (cylindres)
        gl.glColor3f(0.8f, 0.7f, 0.6f);
        for (float i = -0.25f; i <= 0.25f; i += 0.5f) {
            gl.glPushMatrix();
            gl.glTranslatef(i, -0.2f, 0f);
            gl.glRotatef(90, 1, 0, 0);
            glut.glutSolidCylinder(0.05f, 0.2f, 10, 10);
            gl.glPopMatrix();
        }

        gl.glPopMatrix();
    }
}