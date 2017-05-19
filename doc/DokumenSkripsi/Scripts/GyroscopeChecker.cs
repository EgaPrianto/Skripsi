
using TheNextFlow.UnityPlugins;
using UnityEngine;
using UnityEngine.SceneManagement;

public class GyroscopeChecker : MonoBehaviour {

    // Use this for initialization
    void Start()
    {
        //Debug.Log("init");
        
        if (!SystemInfo.supportsGyroscope)
        {
            MobileNativePopups.OpenAlertDialog(
                   "No Gyroscope", "Your device didn't have Gyroscope sensor, you can't play this game!",
                   "OK", () => { Application.Quit(); });
        }else { 
            SceneManager.LoadScene("Gameplay", LoadSceneMode.Single);
        }
    }
}
