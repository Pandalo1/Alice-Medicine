package com.alicia.medicamentos

import android.app.*
import android.content.*
import android.os.*

class AlarmReceiver: android.content.BroadcastReceiver(){
 override fun onReceive(c:Context,intent:Intent){
  val i=intent.getIntExtra("dose",-1);if(i !in 0..20 || Scheduler.done(c,i))return
  Scheduler.channel(c)
  val launch=Intent(c,AlarmActivity::class.java).putExtra("dose",i).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
  val pi=PendingIntent.getActivity(c,3000+i,launch,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
  val notification=Notification.Builder(c,Scheduler.CHANNEL).setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
   .setContentTitle("💊 Alicia: hora del medicamento").setContentText("Toma ${i+1} de 21. Confirma después de tomarlo.")
   .setCategory(Notification.CATEGORY_ALARM).setPriority(Notification.PRIORITY_MAX).setAutoCancel(false)
   .setOngoing(true).setContentIntent(pi).setFullScreenIntent(pi,true).build()
  c.getSystemService(NotificationManager::class.java).notify(1000+i,notification)
 }
}
