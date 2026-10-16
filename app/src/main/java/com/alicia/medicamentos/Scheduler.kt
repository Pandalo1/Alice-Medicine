package com.alicia.medicamentos

import android.app.*
import android.content.*
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

object Scheduler {
 const val CHANNEL = "medication_alarms_v1"
 fun prefs(c: Context) = c.getSharedPreferences("alicia", Context.MODE_PRIVATE)
 fun start(c: Context): Long = prefs(c).getLong("start", 0)
 fun initialize(c: Context) {
  if (prefs(c).getBoolean("initialized", false)) return
  val cal = Calendar.getInstance().apply { set(2026, Calendar.OCTOBER, 15, 10, 0, 0); set(Calendar.MILLISECOND, 0) }
  prefs(c).edit().putLong("start", cal.timeInMillis).putLong("done_0", cal.timeInMillis).putBoolean("initialized", true).apply()
  schedule(c)
 }
 fun doseAt(start: Long, index: Int) = start + index * 8L * 60 * 60 * 1000
 fun fmt(t: Long) = SimpleDateFormat("EEE dd/MM/yyyy hh:mm a", Locale("es", "MX")).format(Date(t))
 fun done(c: Context, i: Int) = prefs(c).contains("done_$i")
 fun mark(c: Context, i: Int) { prefs(c).edit().putLong("done_$i", System.currentTimeMillis()).apply(); cancel(c,i) }
 fun channel(c: Context) { val nm=c.getSystemService(NotificationManager::class.java); val ch=NotificationChannel(CHANNEL,"Alarmas de medicamentos",NotificationManager.IMPORTANCE_HIGH); ch.enableVibration(true); ch.setSound(android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI, android.media.AudioAttributes.Builder().setUsage(android.media.AudioAttributes.USAGE_ALARM).build()); nm.createNotificationChannel(ch) }
 fun pending(c: Context,i: Int): PendingIntent { val intent=Intent(c,AlarmReceiver::class.java).putExtra("dose",i); return PendingIntent.getBroadcast(c,i,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE) }
 fun cancel(c: Context,i:Int) { (c.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pending(c,i)); c.getSystemService(NotificationManager::class.java).cancel(1000+i) }
 fun schedule(c: Context) {
  val start=start(c); if(start==0L)return
  val am=c.getSystemService(Context.ALARM_SERVICE) as AlarmManager
  for(i in 0 until 21) {
   cancel(c,i)
   val t=doseAt(start,i)
   if(!done(c,i) && t>System.currentTimeMillis()) {
    if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()) continue
    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,t,pending(c,i))
   }
  }
 }
 fun reset(c:Context,start:Long) { for(i in 0 until 21)cancel(c,i); prefs(c).edit().clear().putBoolean("initialized",true).putLong("start",start).apply(); schedule(c) }
}
