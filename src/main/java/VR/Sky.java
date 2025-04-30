package VR;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Sky {
    private float radius = 100.0f; // Taille de la sphère représentant le ciel
    private float red = 0.5f, green = 0.7f, blue = 1.0f; // Couleur du ciel (bleu clair)
    private float sunRadius = 8.0f; // Rayon du soleil

    // Méthode pour dessiner le ciel et le soleil
    public void draw(GL2 gl) {
        GLUT glut = new GLUT(); // Crée un objet GLUT (si tu ne l’as pas comme attribut)

        gl.glPushMatrix();
        // Déplacer la sphère du ciel pour qu'elle entoure la scène
        gl.glTranslatef(0f, 0f, -10f);

        // Couleur du ciel
        gl.glColor3f(red, green, blue);
        glut.glutSolidSphere(radius, 50, 50); // Dessiner le ciel
        gl.glPopMatrix();

        // Dessiner le soleil
        drawSun(gl, glut);
    }

    private void drawSun(GL2 gl, GLUT glut) {
        gl.glPushMatrix();

        // Position du soleil dans le ciel (ajuste les valeurs si nécessaire)
        gl.glTranslatef(50.0f, 80.0f, -60.0f);

        // Couleur du soleil (jaune)
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        glut.glutSolidSphere(sunRadius, 20, 20); // Soleil sous forme de sphère

        gl.glPopMatrix();
    }
}
