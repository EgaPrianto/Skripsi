
public abstract class Detector  {

    protected bool mightHaveActivites;

    protected Pulse[] listPulseToDetect;
    protected double limitTime;
    protected float limitSpeed;
    protected double limitSimpangan;

    public Detector(double limitTime,float limitSpeed, double limitSimpangan)
    {
        this.limitTime = limitTime;
        this.limitSpeed = limitSpeed;
        this.limitSimpangan = limitSimpangan;
    }

    public bool isMightHaveActivites()
    {
        return mightHaveActivites;
    }


    abstract public bool addPulse(Pulse yPulse);
}
