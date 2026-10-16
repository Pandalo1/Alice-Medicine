package com.alicia.medicamentos

import android.app.*
import android.media.*
import android.os.*
import android.widget.*

class AlarmActivity:Activity(){
 private var player:MediaPlayer?=null;private var vibrator:Vibrator?=null
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setShowWhenLocked(true);setTurnScreenOn(true)
  val i=intent.getIntExtra("dose",-1);if(i !in 0..20){finish();return}
  val root=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(32,80,32,32)}
  root.addView(TextView(this).apply{text="💊 ALICIA\n¡Hora de tomar el medicamento!\nToma ${i+1} de 21";textSize=27f})
  root.addView(Button(this).apply{text="YA TOMÓ SU MEDICAMENTO";setOnClickListener{Scheduler.mark(this@AlarmActivity,i);stopAlarm();finish()}})
  root.addView(Button(this).apply{text="Silenciar (dejar pendiente)";setOnClickListener{stopAlarm();finish()}})
  setContentView(root)
  try{ val uri=android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
   player=MediaPlayer.create(this,uri)?.apply{isLooping=true;start()}
   vibrator=getSystemService(VIBRATOR_SERVICE) as Vibrator
   vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0,500,400,500),0))
  }catch(_:Exception){}
 }
 private fun stopAlarm(){try{player?.stop();player?.release()}catch(_:Exception){};player=null;vibrator?.cancel()}
 override fun onDestroy(){stopAlarm();super.onDestroy()}
}
