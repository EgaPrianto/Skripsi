using System;

public class Pulse
{
    private double simpangan;
    private double rangeTime;
    private double startTime;
    private double endTime;
    private float peak;
    private PulseType type;
    private Axis axis;

    public Pulse(double simpangan, double startTime, double endTime, float peak, PulseType type,
        Axis axis)
    {
        this.simpangan = simpangan;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rangeTime = endTime - startTime;
        this.peak = peak;
        this.type = type;
        this.axis = axis;
    }

    public double getSimpangan()
    {
        return simpangan;
    }

    public double getRangeTime()
    {
        return rangeTime;
    }

    public double getStartTime()
    {
        return startTime;
    }

    public double getEndTime()
    {
        return endTime;
    }

    public float getPeak()
    {
        return peak;
    }

    public PulseType getType()
    {
        return type;
    }

    public Axis getAxis()
    {
        return axis;
    }
}