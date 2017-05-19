using System;

public class NodDetector : Detector
{
    /*
    private double limitTime;//in millisecons
    private float limitSpeed = 2;
    private double limitSimpangan = 0.25;
    */
    public NodDetector(double limitTime,float limitSpeed, double limitSimpangan) : base(limitTime,limitSpeed,limitSimpangan)
    {
        
        this.mightHaveActivites = false;
        this.listPulseToDetect = new Pulse[2];
        this.listPulseToDetect[0] = PulseFactory.STANDARD_PULSE;//firstPulse
        this.listPulseToDetect[1] = PulseFactory.STANDARD_PULSE;//lastPulse
    }

    public override bool addPulse(Pulse yPulse)
    {
        bool result = false;
        this.listPulseToDetect[0] = yPulse;
        if (Math.Abs(this.listPulseToDetect[0].getPeak()) > limitSpeed)
        {
            mightHaveActivites = true;
            bool isPassLimitSimpangan = Math.Abs(this.listPulseToDetect[0].getSimpangan()) > limitSimpangan
                && Math.Abs(listPulseToDetect[1].getSimpangan()) > limitSimpangan;
            bool isPassLimitTimeRange =
               listPulseToDetect[0].getRangeTime() < limitTime && listPulseToDetect[1].getRangeTime() < limitTime;
            bool isDifferentTypePulse = this.listPulseToDetect[0].getType() != listPulseToDetect[1].getType();
            result = isPassLimitSimpangan && isPassLimitTimeRange && isDifferentTypePulse;
        }
        else
        {
            if (Math.Abs(listPulseToDetect[1].getPeak()) < limitSpeed)
            {
                mightHaveActivites = false;
            }
        }
        this.listPulseToDetect[1] = this.listPulseToDetect[0];
        return result;
    }
}