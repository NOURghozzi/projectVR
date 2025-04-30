package VR;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;
import static java.lang.Math.*;

import com.jogamp.opengl.util.gl2.GLUT;
;

public class Car {
    // Position and movement
    private float x, y, z;
    private float speed = 0.2f;
    private boolean moveLeft, moveRight;
    private final float roadLimit = 4.5f;

    // Visual state
    private float tilt = 0;
    private float wheelRot = 0;

    // GLUT helper
    private final GLUT glut = new GLUT();

    public Car() {
        x = y = z = 0;
    }

    public void update() {
        if (moveLeft) {
            x -= speed;
            tilt = max(tilt - 1, -5);
            wheelRot = (wheelRot + 15) % 360;
        } else if (moveRight) {
            x += speed;
            tilt = min(tilt + 1, 5);
            wheelRot = (wheelRot - 15) % 360;
        } else {
            tilt = (abs(tilt) < 0.5f) ? 0 : (tilt > 0 ? tilt - 0.5f : tilt + 0.5f);
        }
        x = max(-roadLimit, min(roadLimit, x));
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y + 0.2f, z);
        gl.glRotatef(tilt, 0, 0, 1);

        // Body: small cuboid
        gl.glColor3f(0.0f, 0.0f, 1.0f);  // Bleu
        gl.glPushMatrix();
        gl.glScalef(1.0f, 0.3f, 0.5f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();

        // Front bumper
        gl.glColor3f(0.4f, 0.4f, 0.4f);  // Gris foncé
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.1f, 0.35f);
        gl.glScalef(0.9f, 0.15f, 0.1f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();



        // Cabin: small cube
        gl.glColor3f(0.7f, 0.7f, 0.7f);  // Gris clair
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0.3f, -0.05f);
        gl.glScalef(0.6f, 0.25f, 0.4f);
        glut.glutSolidCube(1f);
        gl.glPopMatrix();

        // Windshield
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glColor4f(0.3f, 0.5f, 0.8f, 0.4f);  // Bleu translucide
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.3f, 0.4f, 0.05f);
        gl.glVertex3f( 0.3f, 0.4f, 0.05f);
        gl.glVertex3f( 0.25f,0.6f, -0.15f);
        gl.glVertex3f(-0.25f,0.6f, -0.15f);
        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);

        // Side intakes
        gl.glColor3f(0.2f, 0.2f, 0.2f);  // Gris foncé
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.4f, 0.1f,  0.0f);
        gl.glVertex3f( 0.4f, 0.1f,  0.0f);
        gl.glVertex3f( 0.4f, 0.2f, -0.1f);
        gl.glVertex3f(-0.4f, 0.2f, -0.1f);
        gl.glEnd();

        // Side mirrors
        gl.glColor3f(0.75f, 0.75f, 0.75f);  // Argenté
        for (float s : new float[]{-1f, 1f}) {
            gl.glPushMatrix();
            gl.glTranslatef(s * 0.4f, 0.45f, 0f);
            gl.glScalef(0.1f, 0.05f, 0.05f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
        }

        // Tail lights
        gl.glColor3f(1f, 0f, 0f);  // Rouge
        for (float dx : new float[]{-0.2f, 0.2f}) {
            gl.glPushMatrix();
            gl.glTranslatef(dx, 0.2f, -0.3f);
            glut.glutSolidSphere(0.05f, 8, 8);
            gl.glPopMatrix();
        }



        // Wheels: wider and thicker
        gl.glColor3f(0.0f, 0.0f, 0.0f);  // Noir mat
        for (float dx : new float[]{-0.4f, 0.4f}) {
            for (float dz : new float[]{-0.25f, 0.25f}) {
                gl.glPushMatrix();
                gl.glTranslatef(dx, -0.1f, dz);
                gl.glRotatef(90, 0, 1, 0);
                gl.glRotatef(wheelRot, 0, 0, 1);
                glut.glutSolidTorus(0.06f, 0.15f, 12, 8);
                gl.glPopMatrix();
            }
        }

        gl.glPopMatrix();
    }
// touche du clavier est pressée.
    public void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
    }
  //  réagir au relâchement d'une touche et arrêter le mouvement du joueur
    public void handleKeyRelease(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
    }

    public void reset() {
        x = y = z = tilt = wheelRot = 0;
        moveLeft = moveRight = false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
}
