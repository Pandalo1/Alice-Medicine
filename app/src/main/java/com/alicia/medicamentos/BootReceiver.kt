package com.alicia.medicamentos

import android.content.*
class BootReceiver:BroadcastReceiver(){override fun onReceive(c:Context,i:Intent){Scheduler.schedule(c)}}
