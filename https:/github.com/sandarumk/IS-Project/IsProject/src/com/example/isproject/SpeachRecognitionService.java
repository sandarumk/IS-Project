package com.example.isproject;




import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.HeterogeneousExpandableList;
import android.widget.Toast;

public class SpeachRecognitionService extends Service {
	
	 protected AudioManager mAudioManager; 
	    protected SpeechRecognizer mSpeechRecognizer;
	    protected Intent mSpeechRecognizerIntent;
	    protected final Messenger mServerMessenger = new Messenger(new IncomingHandler(this));

	    protected boolean mIsListening;
	    protected volatile boolean mIsCountDownOn;

	    

	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Speach Recognition Service started", Toast.LENGTH_SHORT).show();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                         RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                                         this.getPackageName());

	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Speach Recognition Service started", Toast.LENGTH_SHORT).show();
		int result = super.onStartCommand(intent, flags, startId);
		mSpeechRecognizer= SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
	SpeechRecognitionListener listener = new SpeechRecognitionListener();
	mSpeechRecognizer.setRecognitionListener(listener);
	mSpeechRecognizer.startListening(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()));
		return result;
		
	}
	
	@Override
	public void onDestroy() {

		Toast.makeText(getApplicationContext(), "Speach Recognition Service destroyed", Toast.LENGTH_LONG).show();
	
		
		  if (mIsCountDownOn)
	        {
	            mNoSpeechCountDown.cancel();
	        }
	        if (mSpeechRecognizer != null)
	        {
	            mSpeechRecognizer.destroy();
	        }
	}
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	protected CountDownTimer mNoSpeechCountDown = new CountDownTimer(5000, 5000)
    {

        @Override
        public void onTick(long millisUntilFinished)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onFinish()
        {
            mIsCountDownOn = false;
            Message message = Message.obtain(null, Constants.MSG_RECOGNIZER_CANCEL);
            try
            {
                mServerMessenger.send(message);
                message = Message.obtain(null, Constants.MSG_RECOGNIZER_START_LISTENING);
                mServerMessenger.send(message);
            }
            catch (RemoteException e)
            {

            }
        }
    };
    
    protected class SpeechRecognitionListener implements RecognitionListener
    {

        @Override
        public void onBeginningOfSpeech()
        {
            // speech input will be processed, so there is no need for count down anymore
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }   
            Toast.makeText(getApplicationContext(), "On Begining of speech", Toast. LENGTH_SHORT).show();
            //Log.d(TAG, "onBeginingOfSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onBufferReceived(byte[] buffer)
        {

        }

        @Override
        public void onEndOfSpeech()
        {
        	Toast.makeText(getApplicationContext(), "End Of Speech", Toast. LENGTH_SHORT).show();;
            //Log.d(TAG, "onEndOfSpeech"); //$NON-NLS-1$
         }

        @Override
        public void onError(int error)
        {
            if (mIsCountDownOn)
            {
                mIsCountDownOn = false;
                mNoSpeechCountDown.cancel();
            }
             mIsListening = false;
             Message message = Message.obtain(null, Constants.MSG_RECOGNIZER_START_LISTENING);
             try
             {
                    mServerMessenger.send(message);
             }
             catch (RemoteException e)
             {

             }
             Toast.makeText(getApplicationContext(), "error"+error, Toast. LENGTH_SHORT).show();
            //Log.d(TAG, "error = " + error); //$NON-NLS-1$
        }

        @Override
        public void onEvent(int eventType, Bundle params)
        {

        }

        @Override
        public void onPartialResults(Bundle partialResults)
        {

        }

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                mIsCountDownOn = true;
                mNoSpeechCountDown.start();
                mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            }
            Toast.makeText(getApplicationContext(), "On Ready for Speech", Toast.LENGTH_SHORT).show();;
           // Log.d(TAG, "onReadyForSpeech"); //$NON-NLS-1$
        }

        @Override
        public void onResults(Bundle results)
        {
        	Toast.makeText(getApplicationContext(), "On Results", Toast. LENGTH_SHORT).show();;
        	ArrayList strlist = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        	 for (int i = 0; i < strlist.size();i++ ) {
        	  Log.d("Speech", "result=" + strlist.get(i));
        	  Toast.makeText(getApplicationContext(), "result=" +strlist.get(i), Toast. LENGTH_SHORT).show();;
        	 }
            //Log.d(TAG, "onResults"); //$NON-NLS-1$

        }

        @Override
        public void onRmsChanged(float rmsdB)
        {

        }

    }

}
