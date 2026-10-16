# Medicamentos de Alicia — Android nativo

Abrir carpeta en Android Studio (JDK 17), esperar sincronización Gradle y usar **Build > Build APK(s)**. APK de depuración: `app/build/outputs/apk/debug/app-debug.apk`.

- Fecha inicial elegible; primera toma a las 10:00 AM, siguientes cada 8 horas (21 dosis en 7 días).
- Alarmas exactas con AlarmManager, notificación urgente, pantalla de alarma con sonido y vibración, confirmación y persistencia local.
- Permitir notificaciones y **Alarmas y recordatorios**. En Android 14+ la pantalla completa puede requerir habilitación adicional en Ajustes; tocar la notificación abre la alarma si no aparece automáticamente.
- Desactivar restricciones agresivas de batería para mejorar confiabilidad. Probar en teléfono real con pantalla bloqueada y tras reinicio.
- Las alarmas que ya pasaron al iniciar el tratamiento no se disparan retroactivamente; aparecen pendientes.
- La confirmación registra el momento real, no modifica los horarios posteriores.
- IMPORTANTE: verificar fecha de la primera toma y pauta médica. No es dispositivo médico. Si una dosis se omitió, no duplicarla sin indicación profesional.
