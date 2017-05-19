
using System.Collections.Generic;
using UnityEngine;

public class MotionRecorder {
    
    private PulseFactory pulseFactoryXAxis;
    private PulseFactory pulseFactoryYAxis;
    private Detector nodDetector;
    private Detector shookDetector;

    private LinkedList<HeadMotionListener> listListener;

    public MotionRecorder()
    {
        listListener = new LinkedList<HeadMotionListener>();
        pulseFactoryXAxis = new PulseFactory(Axis.X);
        pulseFactoryYAxis = new PulseFactory(Axis.Y);
        nodDetector = new NodDetector(700, 2, 0.25);
        shookDetector = new ShookDetector(700, 2, 0.25);
    }

    public void addHeadMotionListener(HeadMotionListener listener)
    {
        listListener.AddFirst(listener);
    }

    public void removeHeadMotionListener(HeadMotionListener listener)
    {
        listListener.Remove(listener);
    }

    public void inputGryoData(float x,float y)
    {
        Pulse xPulse = pulseFactoryXAxis.inputData(x);
        Pulse yPulse = pulseFactoryYAxis.inputData(y);
        if (xPulse != null)
        {
            Debug.Log("PULSE");
            bool shookDetected = shookDetector.addPulse(xPulse);
            if (shookDetected && !nodDetector
                .isMightHaveActivites())
            {// terdeteksi gelengan tanpa ada pergerakan pada sumbu y
                notifyAllMotionListener(Motion.SHOOK);
                //Log.d("MotionDetectedPulsesX", shookDetector.toString());
            }
        }
        if (yPulse != null)
        {
            bool nodDetected = nodDetector.addPulse(yPulse);
            if (nodDetected && !shookDetector
                .isMightHaveActivites())
            {// terdeteksi gelengan tanpa ada pergerakan pada sumbu x
                notifyAllMotionListener(Motion.NOD);
            }
        }
    }

    public void notifyAllMotionListener(Motion motion)
    {
        foreach (HeadMotionListener motionListener in listListener)
        {
            motionListener.onMotionDetected(motion);
        }
    }
}
