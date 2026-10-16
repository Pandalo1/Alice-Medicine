# Compilar gratis con GitHub Actions

1. En github.com crea un repositorio privado vacío (sin README).
2. Sube **el contenido de la carpeta AliciaMedicamentos**, no la carpeta contenedora, a la raíz del repositorio. Activa mostrar archivos ocultos para incluir `.github/workflows/build-apk.yml`.
3. Abre la pestaña **Actions**. Si hace falta, habilita workflows y abre **Compilar APK Alicia** → **Run workflow**.
4. Espera a que termine y descarga **AliciaMedicamentos-APK** en Artifacts. Descomprime para obtener `app-debug.apk`.
5. Instala la APK en Android, autoriza **Alarmas y recordatorios**, **Notificaciones**, y prueba sonido/vibración con la pantalla bloqueada.

El proyecto inicia con la primera toma del 15/oct/2026 10:00 AM registrada y programa las restantes cada 8 horas, hasta el 22/oct/2026 02:00 AM. Usa la zona horaria configurada en el teléfono.

**Advertencia:** No se ha compilado ni probado en un dispositivo real. Android puede limitar pantallas completas o restringir actividad en segundo plano. Usa alarmas convencionales de respaldo hasta verificarlo. Si una dosis se omite, consulta las instrucciones del profesional sanitario; no dupliques dosis automáticamente.
