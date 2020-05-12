package projectend.util;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.world.World;

public class ParticleConfEndRodFactory implements IParticleFactory
{
    public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... parameters)
    {
        float r, g, b, speed;
        if (parameters.length >= 4) {
            r = (float) parameters[0] / 255;//0 to 255
            g = (float) parameters[1] / 255;
            b = (float) parameters[2] / 255;
            speed = parameters[3];
        } else {
            r = 255;
            g = 255;
            b = 255;
            speed = 60;
        }
        ParticleEndRod p = new ParticleEndRod(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        p.setRBGColorF(r, g, b);
        p.setMaxAge((int)speed);
        return p;
    }
}