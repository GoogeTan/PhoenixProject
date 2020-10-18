package statemachine.client.models;

public class TransitionRotationScale
{
    double scaleX, scaleY, scaleZ;
    double rotationX, rotationY, rotationZ;
    double transitionX, transitionY, transitionZ;

    public TransitionRotationScale(double scale)
    {
        this.scaleX = this.scaleY = this.scaleZ =scale;
    }

    public void setRotations(double rotationX, double rotationY, double rotationZ)
    {
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
    }

    public void setTransitions(double transitionX, double transitionY, double transitionZ)
    {
        this.transitionX = transitionX;
        this.transitionY = transitionY;
        this.transitionZ = transitionZ;
    }

    public void setScales(double scaleX, double scaleY, double scaleZ)
    {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    public TransitionRotationScale blend(TransitionRotationScale other)
    {
        TransitionRotationScale res = new TransitionRotationScale(1);
        res.setScales((scaleX + other.scaleX) / 2, (scaleY + other.scaleY) / 2, (scaleZ + other.scaleZ) / 2);
        res.setRotations((rotationX + other.rotationX) / 2, (rotationY + other.rotationY) / 2, (rotationZ + other.rotationZ) / 2);
        res.setTransitions((transitionX + other.transitionX) / 2, (transitionY + other.transitionY) / 2, (transitionZ + other.transitionZ) / 2);
        return res;
    }

    public TransitionRotationScale blend(TransitionRotationScale other, double alpha)
    {
        TransitionRotationScale res = new TransitionRotationScale(1);
        res.setScales(scaleX * alpha + other.scaleX * (1 - alpha), scaleY * alpha + other.scaleY * (1 - alpha), scaleZ * alpha + other.scaleZ * (1 - alpha));
        res.setRotations(rotationX * alpha + other.rotationX * (1 - alpha), rotationX * alpha + other.rotationX * (1 - alpha), rotationX * alpha + other.rotationX * (1 - alpha));
        res.setTransitions(transitionX * alpha + other.transitionX * (1 - alpha), transitionY * alpha + other.transitionY * (1 - alpha), transitionZ * alpha + other.transitionZ * (1 - alpha));
        return res;
    }

    public double getScaleX()
    {
        return scaleX;
    }

    public double getScaleY()
    {
        return scaleY;
    }

    public double getScaleZ()
    {
        return scaleZ;
    }

    public double getRotationX()
    {
        return rotationX;
    }

    public double getRotationY()
    {
        return rotationY;
    }

    public double getRotationZ()
    {
        return rotationZ;
    }

    public double getTransitionX()
    {
        return transitionX;
    }

    public double getTransitionY()
    {
        return transitionY;
    }

    public double getTransitionZ()
    {
        return transitionZ;
    }
}
