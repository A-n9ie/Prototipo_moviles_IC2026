# DataClassActivitiesApp

Proyecto Android Studio con dos Activities que se comunican mediante una `data class` de Kotlin (`Persona`) usando `Parcelable`.

## Requisitos
- Android Studio Koala o superior
- JDK 17
- Android SDK 34

## Cómo ejecutar
1. Abrir la carpeta `DataClassActivitiesApp` en Android Studio.
2. Esperar a que Gradle sincronice dependencias.
3. Ejecutar la app en emulador o dispositivo.

## Flujo
- **MainActivity**: solicita nombre, edad, correo, teléfono y dirección.
- **SecondActivity**: recibe el objeto `Persona` y muestra los datos.

## Archivos clave
- `Persona.kt`: data class
- `MainActivity.kt`: captura y envío de información
- `SecondActivity.kt`: recuperación y visualización
