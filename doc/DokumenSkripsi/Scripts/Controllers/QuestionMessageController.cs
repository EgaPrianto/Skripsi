
using System;
using TheNextFlow.UnityPlugins;
using UnityEngine;
using UnityEngine.UI;

public class QuestionMessageController : MonoBehaviour, IGvrGazeResponder, HeadMotionListener
{
    public Canvas messageCanvas;
    public Canvas notificationCanvas;
    public String[] questions;
    public bool[] answers;
    public Image pointer;
    private Animator animatorMessage;
    private Animator animatorNotification;
    private Text messageText;
    private bool startCounting;
    private bool isDetecting;
    private bool clickable;
    private float timeAvailable;
    private MotionRecorder mMotionRecorder;
    private int selectedIndexQuestions;

    public void OnGazeEnter()
    {
    }

    public void OnGazeExit()
    {
    }

    public void OnGazeTrigger()
    {
        if (clickable && !GameplayStatistic.isAnswering)
        {
            Input.gyro.enabled = true;
            Input.gyro.updateInterval = 0.00002F;
            mMotionRecorder = new MotionRecorder();
            mMotionRecorder.addHeadMotionListener(this);
            isDetecting = true;
            selectedIndexQuestions = (int)(UnityEngine.Random.Range(0.0f, 0.9999f) * questions.Length);
            messageText.text = questions[selectedIndexQuestions];
            animatorMessage.SetBool("poppedOut", !animatorMessage.GetBool("poppedOut"));
            animatorNotification.SetBool("poppedOut", !animatorNotification.GetBool("poppedOut"));
            clickable = false;
            GameplayStatistic.isAnswering = true;
        }
    }

    // Use this for initialization
    void Start() {
        timeAvailable = UnityEngine.Random.Range(5.0f, 10.0f);
        clickable = false;
        animatorMessage = messageCanvas.GetComponentInChildren<Animator>();
        animatorNotification = notificationCanvas.GetComponentInChildren<Animator>();
        messageText = messageCanvas.GetComponentInChildren<Text>();
        startCounting = true;
    }

    // Update is called once per frame
    void Update() {
        if (startCounting)
        {
            timeAvailable -= Time.deltaTime;
        }
        if (timeAvailable < 0)
        {
            startQuestion();
        }
        if (isDetecting)
        {
            this.mMotionRecorder.inputGryoData(Input.gyro.rotationRate.y, Input.gyro.rotationRate.x);
            if (Input.GetKey("d")) debugOnNodMotionDetected();
            if (Input.GetKey("f")) debugOnShookMotionDetected();
        }
    }

    public void resetAll()
    {
        print("resetting object");
        animatorMessage.SetBool("poppedOut", false);
        animatorNotification.SetBool("poppedOut", false);
        startCounting = true;
        isDetecting = false;
        clickable = false;
        timeAvailable = UnityEngine.Random.Range(5.0f, 10.0f);
        mMotionRecorder = null;
    }

    public void stopWorking()
    {
        isDetecting = false;
        clickable = false;
    }
    public void debugOnNodMotionDetected()
    {
        this.onMotionDetected(Motion.NOD);
    }

    public void debugOnShookMotionDetected()
    {
        this.onMotionDetected(Motion.SHOOK);
    }

    public void startQuestion()
    {
        startCounting = false;
        animatorNotification.SetBool("poppedOut", !animatorNotification.GetBool("poppedOut"));
        clickable = true;
        timeAvailable = UnityEngine.Random.Range(5.0f, 10.0f);
    }

    public void onMotionDetected(Motion motion)
    {
        if (isDetecting)
        {
            if (motion == Motion.NOD)
            {
                messageText.text = "YES";
                if (answers[selectedIndexQuestions])
                {
                    pointer.transform.Rotate(new Vector3(0, 0, 10));
                }
                else
                {
                    pointer.transform.Rotate(new Vector3(0, 0, -10));
                }
            }
            else
            {
                messageText.text = "NO";
                if (!answers[selectedIndexQuestions])
                {
                    pointer.transform.Rotate(new Vector3(0, 0, 10));
                }
                else
                {
                    pointer.transform.Rotate(new Vector3(0, 0, -10));
                }
            }
            clickable = false;
            startCounting = true;
            GameplayStatistic.isAnswering = false;
            animatorMessage.SetBool("poppedOut", false);
            Input.gyro.enabled = false;
            isDetecting = false;
            mMotionRecorder.removeHeadMotionListener(this);
            mMotionRecorder = null;
        }
    }
}

