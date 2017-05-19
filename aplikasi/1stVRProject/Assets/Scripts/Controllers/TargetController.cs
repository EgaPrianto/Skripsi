using UnityEngine;

[RequireComponent(typeof(Collider))]
public class TargetController : MonoBehaviour, IGvrGazeResponder,HeadMotionListener
{
    private Vector3 startingPosition;

    public Material inactiveMaterial;
    public Material gazedAtMaterial;
    private MotionRecorder mMotionRecorder;
    private bool isDetecting;

    void Start()
    {
        startingPosition = transform.localPosition;
        SetGazedAt(false);
        Input.gyro.enabled = true;
        Input.gyro.updateInterval = 0.00002F;
    }

    void LateUpdate()
    {
        GvrViewer.Instance.UpdateState();
        if (GvrViewer.Instance.BackButtonPressed)
        {
            Application.Quit();
        }
    }

    void FixedUpdate()
    {
        if (isDetecting)
        {
            this.mMotionRecorder.inputGryoData(Input.gyro.rotationRate.y, Input.gyro.rotationRate.x);
        }
    }

    public void SetGazedAt(bool gazedAt)
    {
        if (inactiveMaterial != null && gazedAtMaterial != null)
        {
            GetComponent<Renderer>().material = gazedAt ? gazedAtMaterial : inactiveMaterial;
            return;
        }
        GetComponent<Renderer>().material.color = gazedAt ? Color.green : Color.red;
    }

    public void Reset()
    {
        transform.localPosition = startingPosition;
    }

    public void StartDetectHeadMotion()
    {
        mMotionRecorder = new MotionRecorder();
        mMotionRecorder.addHeadMotionListener(this);
        isDetecting = true;
    }


    public void StopDetectHeadMotion()
    {
        mMotionRecorder = null;
        mMotionRecorder.removeHeadMotionListener(this);
        isDetecting = false;
    }

    public void TeleportRandomly()
    {
        Vector3 direction = Random.onUnitSphere;
        direction.y = Mathf.Clamp(direction.y, 0.5f, 1f);
        float distance = 2 * Random.value + 1.5f;
        transform.localPosition = direction * distance;
    }

    #region IGvrGazeResponder implementation

    /// Called when the user is looking on a GameObject with this script,
    /// as long as it is set to an appropriate layer (see GvrGaze).
    public void OnGazeEnter()
    {
        //SetGazedAt(true);
    }

    /// Called when the user stops looking on the GameObject, after OnGazeEnter
    /// was already called.
    public void OnGazeExit()
    {
        //SetGazedAt(false);
    }

    /// Called when the viewer's trigger is used, between OnGazeEnter and OnPointerExit.
    public void OnGazeTrigger()
    {
        //TeleportRandomly();
        GetComponent<Renderer>().material = inactiveMaterial;
        StartDetectHeadMotion();
    }

    #endregion

    #region HeadMotionListener implementation
    public void onMotionDetected(Motion motion)
    {
        if (motion==Motion.NOD)
        {
            GetComponent<Renderer>().material.color = Color.cyan;
        }
        else if (motion==Motion.SHOOK)
        {

            GetComponent<Renderer>().material.color = Color.yellow;
        }
    }
    #endregion
}
