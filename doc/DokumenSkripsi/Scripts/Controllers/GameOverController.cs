using System;
using UnityEngine;
using UnityEngine.UI;

public class GameOverController : MonoBehaviour,IGvrGazeResponder {

    public QuestionMessageController[] itemMessages;
    public Image pointer;
    public Animator animator;

    public void OnGazeEnter()
    {
        //Do nothing
    }

    public void OnGazeExit()
    {
        //Do nothing
    }

    public void OnGazeTrigger()
    {
        //print("trigger");
        restartGame();
    }
    public void restartGame()
    {
        //print("restart");
        foreach(QuestionMessageController itemMessage in itemMessages)
        {
            itemMessage.resetAll();
        }
        animator.SetBool("poppedOut", false);
        pointer.transform.Rotate(new Vector3(0,0,(-1*pointer.transform.rotation.eulerAngles.z)));
    }
    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update ()
    {
        //print(Math.Abs(pointer.transform.rotation.eulerAngles.z));
        //RectTransform gameOverTransform= this.GetComponent<RectTransform>();
        //gameOverTransform.rotation.eulerAngles.Set(gameOverTransform.rotation.eulerAngles.x, gameOverTransform.rotation.eulerAngles.y,0);
        if (isGameOver())
        {
            animator.SetBool("poppedOut", true);
            haltAllGameplay();
        }
	}
    void LateUpdate()
    {
        GvrViewer.Instance.UpdateState();
        if (GvrViewer.Instance.BackButtonPressed)
        {
            Application.Quit();
        }
    }

    public bool isGameOver()
    {
        return Math.Abs(pointer.transform.rotation.eulerAngles.z) >= 59 && Math.Abs(pointer.transform.rotation.eulerAngles.z) <= 309;
    }
    public void haltAllGameplay()
    {
        foreach (QuestionMessageController itemMessage in itemMessages)
        {
            itemMessage.stopWorking();
        }
    }
}
