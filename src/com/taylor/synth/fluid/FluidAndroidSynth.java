package com.taylor.synth.fluid;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;

/**
 * fluidysynth�� �ȵ���̵忡�� ��� �� �� �ִ� Ŭ�����Դϴ�.
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
	 * synth �� �ʱ�ȭ�ϰ� �����մϴ�.
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
	 * synth �� ���߰� �����մϴ�.
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
	 * ������ �̵� ������ ����մϴ�.
	 * @param path ������ ��ü ��� 
	 * @param time ����ϰ��� �ϴ� �ð� ��ġ 
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
	 * �̵� ����� ����ϴ�.
	 * @return �÷��̰� ���� �� ���� �ð� 
	 */
	public int stopPlaying()
	{
		return AStopPlayer(THREAD_MIDIFILE_PLAYER_INDEX);
	}
	
	/**
	 * �ش� ��ǥ �Ҹ��� ���ϴ�.
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
	 * �ش� ��ǥ �Ҹ��� ����ϴ�.
	 * @param channel
	 * @param key
	 */
	public void noteOff(int channel, int key)
	{
		int synthNum = (Integer) noteManager.get(key);
		ANoteOff(synthNum, channel, key);
	}
	
	/**
	 * ���� ��Ʈ�� �ҷ��ɴϴ�.
	 * @param filePath - ������ ��ü ���
	 */
	public void loadSoundFont(String filePath){
		for(int i = 0; i < THREAD_MAX; i++){
			ALoadSoundfont(i, filePath);
		}
	}
	
	/**
	 * ä�� ���� ���� ���� �����մϴ�.
	 * @param channel
	 * @param value ���� 0~128
	 */
	public void setChannelVolume(int channel, int value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetChannelVolume(i, channel, value);
		}
	}
	
	/**
	 * �ŵ�������� gain ���� �����մϴ�. �ŵ������, �ͽ��� gain �� ����ġ�� ũ�� �Ҹ��� ��׷����� ���� �߻� �� �� �ֽ��ϴ�.
	 * @param value ���� 0.0f~10.0f
	 */
	public void setSynthGain(float value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetSynthGain(i, value);
		}
	}
	
	/**
	 * ä�� ���� �� ���� �����մϴ�.
	 * @param channel
	 * @param value ���� 0(left)~64(right)
	 */
	public void setChannelPan(int channel, int value){
		for(int i = 0; i < THREAD_MAX; i++){
			ASetChannelPan(i, channel, value);
		}
	}
	
	/**
	 * ä�� ���� �������� ���� �����մϴ�.
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
