package com.taylor.synth.fluid;

import jp.kshoji.driver.midi.activity.AbstractSingleMidiActivity;
import jp.kshoji.driver.midi.device.MidiInputDevice;
import android.app.Activity;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends AbstractSingleMidiActivity {
	int time = 0;
	
	private FluidAndroidSynth fds;
	private long lltime = 0;
	
	int getNote(int y){
		int result = y / 200;
		switch(result){
		case 0: return 60;
		case 1: return 62;
		case 2: return 64;
		case 3: return 65;
		case 4: return 67;
		case 5: return 69;
		case 6: return 71;
		case 7: return 72;
		default: return 72;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		View view = findViewById(R.id.layout);
		view.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
		        int action = arg1.getAction();
		        int pointerIndex = ((action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
		        int actionBits = (action & MotionEvent.ACTION_MASK);
		        
		        float x = arg1.getX(pointerIndex);
		        float y = arg1.getY(pointerIndex);
		        int note = getNote((int) y);
		        
		        switch(actionBits){
		        case MotionEvent.ACTION_DOWN:
		        case MotionEvent.ACTION_POINTER_DOWN:
		        	fds.noteOn(0, note, 100);
		        	break;
		        case MotionEvent.ACTION_UP:
		        case MotionEvent.ACTION_POINTER_UP:
		        	fds.noteOff(0, note);
		            break;
		        }

				return true;
			}
			
		});
		
		Button b = (Button) findViewById(R.id.test0);
		b.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				fds.noteOn(0, 60, 100);
				fds.playMIDIFile("/mnt/sdcard/ApaSynth/Untitled_MIX.mid", time);
			}
			
		});
		
		Button b1 = (Button) findViewById(R.id.test1);
		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				fds.noteOn(0, 60, 100);
				time = fds.stopPlaying();
				if(time >100)
					time = time - 100;
				else
					time = 0;
			}
			
		});
		
		Button btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 64);
//					fds.setSustainSwitch(0, 20);
					fds.noteOn(0, 60, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 60);
				}
				return false;
			}
			
		});
		
		Button btn2 = (Button) findViewById(R.id.button2);
		btn2.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 48);
//					fds.setSustainSwitch(0, 20);
					fds.noteOn(0, 62, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 62);
				}
				return false;
			}
			
		});
		
		Button btn3 = (Button) findViewById(R.id.button3);
		btn3.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 16);
//					fds.setSustainSwitch(0, 67);
					fds.noteOn(0, 64, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 64);
				}
				return false;
			}
			
		});
		
		Button btn4 = (Button) findViewById(R.id.button4);
		btn4.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 0);
//					fds.setSustainSwitch(0, 65);
					fds.noteOn(0, 65, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 65);
				}
				return false;
			}
			
		});
		
		Button btn5 = (Button) findViewById(R.id.button5);
		btn5.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 0);
//					fds.setSustainSwitch(0, 65);
					fds.noteOn(0, 67, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 67);
				}
				return false;
			}
			
		});
		
		Button btn6 = (Button) findViewById(R.id.button6);
		btn6.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 0);
//					fds.setSustainSwitch(0, 65);
					fds.noteOn(0, 69, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 69);
				}
				return false;
			}
			
		});
		
		Button btn7 = (Button) findViewById(R.id.button7);
		btn7.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
//					fds.setChannelPan(0, 0);
//					fds.setSustainSwitch(0, 65);
					fds.noteOn(0, 71, 100);
				}else if(arg1.getAction() == MotionEvent.ACTION_UP){
					fds.noteOff(0, 71);
				}
				return false;
			}
			
		});
		
		Button btn8 = (Button) findViewById(R.id.button8);
		btn8.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				long time = 80;
				if(arg1.getAction() == MotionEvent.ACTION_DOWN){
					fds.noteOn(0, 67, 100);
					sleep(time);
					fds.noteOff(0, 67);
					fds.noteOn(0, 67, 100);
					sleep(time);
					fds.noteOff(0, 67);
					fds.noteOn(0, 69, 100);
					sleep(time);
					fds.noteOff(0, 69);
					fds.noteOn(0, 69, 100);
					sleep(time);
					fds.noteOff(0, 69);
					fds.noteOn(0, 67, 100);
					sleep(time);
					fds.noteOff(0, 67);
					fds.noteOn(0, 67, 100);
					sleep(time);
					fds.noteOff(0, 67);
					fds.noteOn(0, 64, 100);
					sleep(time);
					fds.noteOff(0, 64);
				}
				return false;
			}
			
		});
	}
	
	private void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		fds.destroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fds = new FluidAndroidSynth();
		fds.startSynth();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDeviceDetached(UsbDevice usbDevice) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onDeviceAttached(UsbDevice usbDevice) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiMiscellaneousFunctionCodes(MidiInputDevice sender,
			int cable, int byte1, int byte2, int byte3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiCableEvents(MidiInputDevice sender, int cable, int byte1,
			int byte2, int byte3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiSystemCommonMessage(MidiInputDevice sender, int cable,
			byte[] bytes) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiSystemExclusive(MidiInputDevice sender, int cable,
			byte[] systemExclusive) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiNoteOff(MidiInputDevice sender, int cable, int channel,
			int note, int velocity) {
		// TODO Auto-generated method stub
		fds.noteOff(1, note + 24);
	}
	@Override
	public void onMidiNoteOn(MidiInputDevice sender, int cable, int channel,
			int note, int velocity) {
		// TODO Auto-generated method stub
		fds.noteOn(1, note + 24, velocity);
	}
	@Override
	public void onMidiPolyphonicAftertouch(MidiInputDevice sender, int cable,
			int channel, int note, int pressure) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiControlChange(MidiInputDevice sender, int cable,
			int channel, int function, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiProgramChange(MidiInputDevice sender, int cable,
			int channel, int program) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiChannelAftertouch(MidiInputDevice sender, int cable,
			int channel, int pressure) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiPitchWheel(MidiInputDevice sender, int cable,
			int channel, int amount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onMidiSingleByte(MidiInputDevice sender, int cable, int byte1) {
		// TODO Auto-generated method stub
		
	}
	
}

