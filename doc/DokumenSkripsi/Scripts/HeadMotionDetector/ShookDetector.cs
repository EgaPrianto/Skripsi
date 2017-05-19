using System;

public class ShookDetector :Detector
{
    /*
    private double limitTime;//in millisecons
    private float limitSpeed = 2;
    private double limitSimpangan = 0.25;
    */
    public ShookDetector(double limitTime, float limitSpeed, double limitSimpangan) : base(limitTime, limitSpeed, limitSimpangan)
    {
        mightHaveActivites = false;
        listPulseToDetect = new Pulse[3];
        listPulseToDetect[0] = PulseFactory.STANDARD_PULSE;
        listPulseToDetect[1] = PulseFactory.STANDARD_PULSE;
        listPulseToDetect[2] = PulseFactory.STANDARD_PULSE;
    }

    public override bool addPulse(Pulse xPulse)
    {
        bool result = false;
        listPulseToDetect[2] = listPulseToDetect[1];
        listPulseToDetect[1] = listPulseToDetect[0];
        listPulseToDetect[0] = xPulse;//currentPulse
        if (Math.Abs(listPulseToDetect[0].getPeak()) > limitSpeed)
        {
            mightHaveActivites = true;
            bool isPassLimitSimpangan = Math.Abs(listPulseToDetect[0].getSimpangan()) > limitSimpangan
                && Math.Abs(listPulseToDetect[1].getSimpangan()) > limitSimpangan
                && Math.Abs(listPulseToDetect[2].getSimpangan()) > limitSimpangan;
            bool isPassLimitTimeBetween =
                (listPulseToDetect[1].getStartTime() - listPulseToDetect[2].getEndTime() == 0) && (
                    listPulseToDetect[0].getStartTime() - listPulseToDetect[1].getEndTime() == 0);
            bool isPassLimitTimeRange =
                listPulseToDetect[0].getRangeTime() < limitTime && listPulseToDetect[1].getRangeTime() < limitTime
                    && listPulseToDetect[2].getRangeTime() < limitTime;
            bool isDifferentTypePulse =
                (listPulseToDetect[1].getType() != listPulseToDetect[2].getType()) && (listPulseToDetect[0].getType() != listPulseToDetect[1]
                    .getType());
            result = isPassLimitSimpangan && isPassLimitTimeBetween && isDifferentTypePulse
                && isPassLimitTimeRange;
        }
        else
        {
            if (Math.Abs(listPulseToDetect[2].getPeak()) < limitSpeed
                && Math.Abs(listPulseToDetect[1].getPeak()) < limitSpeed)
            {
                mightHaveActivites = false;
            }
        }
        return result;
    }
    
}