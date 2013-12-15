package com.example.isproject;

import java.lang.ref.WeakReference;

import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

class IncomingHandler extends Handler
{
    private WeakReference<SpeachRecognitionService> mtarget;

    IncomingHandler(SpeachRecognitionService target)
    {
        mtarget = new WeakReference<SpeachRecognitionService>(target);
    }


    @Override
    public void handleMessage(Message msg)
    {
        final SpeachRecognitionService target = mtarget.get();

        switch (msg.what)
        {
            case Constants.MSG_RECOGNIZER_START_LISTENING:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    // turn off beep sound  
                    target.mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                }
                 if (!target.mIsListening)
                 {
                     target.mSpeechRecognizer.startListening(target.mSpeechRecognizerIntent);
                     target.mIsListening = true;
                     Toast.makeText(target.getApplicationContext(), "Message start Listnening", Toast.LENGTH_LONG).show();
             		
                    //Log.d(TAG, "message start listening"); //$NON-NLS-1$
                 }
                 break;

             case Constants.MSG_RECOGNIZER_CANCEL:
                  target.mSpeechRecognizer.cancel();
                  target.mIsListening = false;
                  //Log.d(TAG, "message canceled recognizer"); //$NON-NLS-1$
                  break;
         }
        
   } 
} 
