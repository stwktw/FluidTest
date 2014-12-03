package com.taylor.synth.fluid;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

/**
 * fluidysynth를 안드로이드에서 사용 할 수 있는 클래스입니다.
 * @author stwktw
 *
 */
public class FluidAndroidSynth {

	static {
	    System.loadLibrary("fluidsynth-android");
	}
	
	String m_currMIDIfile = "";
	 
	private Thread m_streamThread[];
	private final int THREAD_MAX = 2;
	private final int THREAD_MIDIFILE_PLAYER_INDEX = 0;
	
	public boolean m_bIsStreaming = true;
	public boolean m_bIsPlayingNote = false;
	private SparseArray<Integer> noteManager;
	private int noteSynthNum;
	
	
	/**
	 * synth 를 초기화하고 시작합니다.
	 */
	public void startSynth() {
		noteSynthNum = 0;
		noteManager = new SparseArray<Integer>();
		
		m_streamThread = new Thread[THREAD_MAX];
		
		// not running yet
		if (m_bIsStreaming)
		{
			m_bIsStreaming = false;
			
			for(int i = 0; i < THREAD_MAX; i++){
				m_streamThread[i] = new FluidAndroidSynthThread(i, this);
				m_streamThread[i].setPriority(Thread.MAX_PRIORITY);
				m_streamThread[i].start();
			}
		    
		    // need a small timeout to enable synth to start
		    try {
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * synth 를 멈추고 종료합니다.
	 */
	public void destroy()
	{
		m_bIsStreaming = true;

		try
		{	
			for(int i = 0; i < THREAD_MAX; i++){
				m_streamThread[i].join();
			}
		}
		catch(final Exception ex)
		{
		    ex.printStackTrace();
		}	
	}
	
	/**
	 * 지정한 미디 파일을 재생합니다.
	 * @param path 파일의 전체 경로 
	 * @param time 재생하고자 하는 시간 위치 
	 */
	public void playMIDIFile(String path, final int time)
	{
		m_currMIDIfile = path;
		Thread playThread = new Thread(new Runnable() {
	        public void run() {
	        	m_bIsPlayingNote = true;
	        	APlayMIDIFile(THREAD_MIDIFILE_PLAYER_INDEX, m_currMIDIfile, time);
	        	m_bIsPlayingNote = false;
	        }
	    });
		playThread.setPriority(Thread.MAX_PRIORITY);
		playThread.start();
	}
	
	/**
	 * 미디 재생을 멈춥니다.
	 * @return 플레이가 정지 될 때의 시간 
	 */
	public int stopPlaying()
	{
		return AStopPlayer(THREAD_MIDIFILE_PLAYER_INDEX);
	}
	
	/**
	 * 해당 음표 소리를 냅니다.
	 * @param channel
	 * @param key
	 * @param velocity
	 */
	public void noteOn(int channel, int key, int velocity)
	{
		int synthNum = getSynthNumNoteOn();
		noteManager.append(key, synthNum);
		ANoteOn(synthNum, channel, key, velocity);
	}
	
	/**
	 * 해당 음표 소리를 멈춥니다.
	 * @param channel
	 * @param key
	 */
	public void noteOff(int channel, int key)
	{
		int synthNum = (Integer) noteManager.get(key);
		ANoteOff(synthNum, channel, key);
	}
	
	/**
	 * 사운드 폰트를 불러옵니다.
	 * @param filePath - 파일의 전체 경로
	 */
	public void loadSoundFont(String filePath){
		for(int i = 0; i < THREAD_MAX; i++){
			ALoadSoundfont(i, filePath);
		}
	}
	
	/**
	 * 채널 별로 볼륨 값을 지정합니다.
	 * @param channel
	 * @param value 범위 0~128
	 */
	public void setChannelVolume(int channel, int value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetChannelVolume(i, channel, value);
		}
	}
	
	/**
	 * 신디사이저의 gain 값을 지정합니다. 신디사이저, 믹싱의 gain 이 지나치게 크면 소리가 찌그러지는 현상 발생 할 수 있습니다.
	 * @param value 범위 0.0f~10.0f
	 */
	public void setSynthGain(float value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetSynthGain(i, value);
		}
	}
	
	/**
	 * 채널 별로 팬 값을 지정합니다.
	 * @param channel
	 * @param value 범위 0(left)~64(right)
	 */
	public void setChannelPan(int channel, int value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetChannelPan(i, channel, value);
		}
	}
	
	/**
	 * 채널 별로 서스테인 값을 지정합니다.
	 * @param channel
	 * @param value 0~63(off), 64~128(on)
	 */
	public void setSustainSwitch(int channel, int value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetSustainSwitch(i, channel, value);
		}
	}
	
	private native void AInitSynth(int synthNum);
	
	private native void ADestroySynth(int synthNum);
	
	private native void ALoadSoundfont(int synthNum, String path);
	
	private native void ANoteOn(int synthNum, int channel, int key, int velocity);
	
	private native void ANoteOff(int synthNum, int channel, int key);
	
	private native void AFillBuffer(int synthNum, short[] buffer, int buffLen);
	
	private native void APlayMIDIFile(int synthNum, String path, int tick);
	
	private native int AStopPlayer(int synthNum);
	
	private native void ASetChannelVolume(int synthNum, int channel, int value);
	
	private native void ASetSynthGain(int synthNum, float value);
	
	private native void ASetChannelPan(int synthNum, int channel, int value);
	
	private native void ASetSustainSwitch(int synthNum, int channel, int value);
	
	private synchronized int getSynthNumNoteOn(){
		int synthNum = 0;
		noteSynthNum++;
		if(noteSynthNum >= THREAD_MAX){
			noteSynthNum = 0;
		}
		synthNum = noteSynthNum;
		return synthNum;
	}
	
	public class FluidAndroidSynthThread extends Thread {
		
		private int synthNum;
		private FluidAndroidSynth fluidSynth;
		private AudioTrack m_Track;

		public FluidAndroidSynthThread(int synthNum, FluidAndroidSynth fluidSynth){
			this.synthNum = synthNum;
			this.fluidSynth = fluidSynth;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			int minBufferSize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
	        m_Track = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM);
	        m_Track.play();
	      
//	        int bufferSize = minBufferSize * 5;
//	        int bufferSize = minBufferSize / 16 ;
//	        int bufferSize = 10 ;
//	        int bufferSize = 100 ;
	        int bufferSize = 2 ;
//	        int bufferSize = minBufferSize; 
//	        Log.i("BRYAN", "Buffer size is " + bufferSize);
	        short[] streamBuffer = new short[bufferSize];

	        fluidSynth.AInitSynth(synthNum);
	        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
	        String filePath = sdCardPath + "/GeneralUser.sf2";
	        fluidSynth.ALoadSoundfont(synthNum, filePath);
	        
	        while (!m_bIsStreaming)
	        {
	            // Have fluidSynth fill our buffer...
	        	fluidSynth.AFillBuffer(synthNum, streamBuffer, bufferSize);

	        	// ... then feed that data to the AudioTrack
	        	m_Track.write(streamBuffer, 0, bufferSize);
	        }

	        m_Track.flush();
	        m_Track.stop();
	        m_Track.release();
	        
	        fluidSynth.ADestroySynth(synthNum);
		}
		
		
	}
	
}
