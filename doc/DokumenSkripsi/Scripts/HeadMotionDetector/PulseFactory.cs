using System;

public class PulseFactory {

    private long lastTime;
    private float lastData;
    private double simpangan;
    private long rangeTime;
    private double startTime;
    private double endTime;
    private float peak;
    private PulseType currentPulseType;
    private Axis axis;

    public static Pulse STANDARD_PULSE = new Pulse(0, 0, 0, 0, PulseType.HILL, Axis.X);

    public PulseFactory(Axis axis)
    {
        this.axis = axis;
    }

    private void resetAttributes(PulseType type)
    {
        this.simpangan = 0;
        this.peak = 0;
        this.currentPulseType = type;
    }

    private Pulse finishPulse(float data, double currentTime)
    {
        this.endTime = ((-lastData / (data - lastData)) * (currentTime - lastTime)) + lastTime;
        double areaBeforeIntersect = ((endTime - lastTime) / 1000) * lastData / 2;
        this.simpangan += areaBeforeIntersect;
        Pulse newPulse = new Pulse(this.simpangan, this.startTime, this.endTime, this.peak,
            this.currentPulseType,
            this.axis);
        this.startTime = endTime;
        return newPulse;
    }

    public Pulse inputData(float data)
    {
        Pulse result = null;
        long currentTime = DateTime.Now.Millisecond;
      
        if (currentPulseType == PulseType.HILL)
        {
            if (peak < data)
            {
                peak = data;
            }
            if (data < 0)
            {//if values intersect data=0
                result = finishPulse(data, currentTime);
                resetAttributes(PulseType.VALLEY);
            }
            else
            {
                this.simpangan += ((lastData + data) / 1000) * (currentTime - lastTime) / 2;
            }
        }
        else if (currentPulseType == PulseType.VALLEY)
        {
            if (peak > data)
            {
                peak = data;
            }
            if (data > 0)
            {//if values intersect data=0
                result = finishPulse(data, currentTime);
                resetAttributes(PulseType.HILL);
            }
            else
            {
                this.simpangan += ((lastData + data) / 1000) * (currentTime - lastTime) / 2;
            }
        }
       
        lastData = data;
        lastTime = currentTime;
        return result;
    }
}
