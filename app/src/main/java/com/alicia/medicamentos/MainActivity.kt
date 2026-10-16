package com.alicia.medicamentos

import android.Manifest
import android.app.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.net.Uri
import android.view.*
import android.widget.*
import java.util.*

class MainActivity: Activity() {
 private lateinit var layout: LinearLayout
 override fun onCreate(savedInstanceState:Bundle?) { super.onCreate(savedInstanceState); Scheduler.channel(this); Scheduler.initialize(this)
  if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)!=android.content.pm.PackageManager.PERMISSION_GRANTED) requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS),1)
  render()
 }
 override fun onResume(){super.onResume(); if(::layout.isInitialized){Scheduler.schedule(this);render()}}
 private fun button(text:String,action:()->Unit){ Button(this).apply{this.text=text;setOnClickListener{action()};layout.addView(this)} }
 private fun label(s:String,size:Float=17f){TextView(this).apply{text=s;textSize=size;setPadding(4,9,4,9);layout.addView(this)}}
 private fun render(){
  layout=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(22,25,22,25)}
  val scrollView=ScrollView(this)
  scrollView.addView(layout)
  setContentView(scrollView)
  label("💊 Medicamentos de Alicia",25f)
  label("Cada 8 horas • 7 días • 21 tomas")
  val am=getSystemService(ALARM_SERVICE) as AlarmManager
  if(Build.VERSION.SDK_INT>=31 && !am.canScheduleExactAlarms()){
   label("⚠️ Autoriza 'Alarmas y recordatorios' para recibir avisos exactos.")
   button("Permitir alarmas exactas") { startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:$packageName"))) }
  }
  val start=Scheduler.start(this)
  if(start==0L) label("Todavía no has iniciado el tratamiento.") else {
   label("Inicio: ${Scheduler.fmt(start)}")
   val completed=(0 until 21).count{Scheduler.done(this,it)}
   label("Confirmadas: $completed / 21")
   val next=(0 until 21).firstOrNull{!Scheduler.done(this,it) && Scheduler.doseAt(start,it)>System.currentTimeMillis()}
   label(if(next==null) "Sin próximas alarmas programadas" else "Próxima alarma: ${Scheduler.fmt(Scheduler.doseAt(start,next))}")
   for(i in 0 until 21){ val taken=Scheduler.done(this,i); val time=Scheduler.doseAt(start,i)
    label("Toma ${i+1}: ${Scheduler.fmt(time)} ${if(taken) "✅" else if(time<System.currentTimeMillis()) "⚠️ Pendiente" else "⏳"}",15f)
    if(!taken) button("Confirmar toma ${i+1}"){Scheduler.mark(this,i);render()}
    else label("Confirmada: ${Scheduler.fmt(Scheduler.prefs(this).getLong("done_$i",0))}",13f)
   }
  }
  button("Iniciar / reiniciar tratamiento (elegir fecha)") {
   val now=Calendar.getInstance(); DatePickerDialog(this,{_,year,month,day->
    val cal=Calendar.getInstance().apply{set(year,month,day,10,0,0);set(Calendar.MILLISECOND,0)}
    AlertDialog.Builder(this).setTitle("Confirmar inicio").setMessage("Primera toma: ${Scheduler.fmt(cal.timeInMillis)}. Esto borrará el historial anterior. ¿Continuar?")
     .setPositiveButton("Sí") {_,_->Scheduler.reset(this,cal.timeInMillis);render()}.setNegativeButton("Cancelar",null).show()
   },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH)).show()
  }
  button("Reprogramar alarmas pendientes"){Scheduler.schedule(this);Toast.makeText(this,"Alarmas pendientes revisadas",Toast.LENGTH_SHORT).show()}
 }
}
