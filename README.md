# DataClassActivitiesApp - Prototipo Móviles 2026

Este proyecto es una aplicación Android moderna desarrollada con **Jetpack Compose** y **Firebase**, diseñada para la gestión de productos y clientes con persistencia de imágenes en almacenamiento local.

## 🚀 Características Principales

- **Autenticación:** Registro e inicio de sesión integrados con Firebase Auth.
- **Gestión de Productos (CRUD):** Creación, lectura, actualización y borrado de productos sincronizados con Firebase Firestore.
- **Gestión de Clientes (CRUD):** Módulo completo para administración de clientes.
- **Almacenamiento de Imágenes Híbrido:**
    - Los datos (texto) se guardan en la nube (Firestore).
    - Las imágenes se procesan localmente (`Internal Storage`) para garantizar velocidad e independencia de red.
- **Tecnologías:** Jetpack Compose, Material 3, ViewModel, Corrutinas, Retrofit (configurado) y Coil.

## 📋 Requisitos Previos

- **Android Studio** (Koala o superior recomendado).
- **JDK 17**.
- **Android SDK 34** (Target).
- Conexión a internet (para la sincronización con Firebase).

## 🛠️ Instrucciones de Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Ivan/Prototipo_moviles_IC2026.git
   ```
2. **Abrir en Android Studio:**
   Selecciona la carpeta raíz del proyecto y espera a que la sincronización de **Gradle** finalice.
3. **Ejecutar la Aplicación:**
   - Selecciona un emulador o dispositivo físico.
   - Presiona el botón **Run** (Triángulo verde) o usa el atajo `Shift + F10`.
4. **Primer Inicio:**
   - Regístrate con un correo y contraseña válidos.
   - Accede al catálogo de productos para comenzar a gestionar el inventario.

## 🧪 Pruebas y Reportes

La aplicación cuenta con una suite de pruebas para asegurar la integridad de los datos.

### Ejecutar Pruebas Unitarias (Lógica)
Verifica la conversión de modelos y serialización de datos.
```bash
./gradlew :app:testDebugUnitTest
```
*Reporte en:* `app/build/reports/tests/testDebugUnitTest/index.html`

### Ejecutar Pruebas Funcionales (Instrumentación)
Verifica el guardado físico de imágenes en el dispositivo.
```bash
./gradlew :app:connectedDebugAndroidTest
```
*Reporte en:* `app/build/reports/androidTests/connected/index.html`

### Reporte de Evidencia
Se ha generado un archivo consolidado con los últimos resultados de éxito:
- Ver archivo: `reporte_pruebas_funcionales.txt`

## 📂 Estructura de Archivos Clave

- `ui/screens/`: Pantallas desarrolladas en Jetpack Compose.
- `viewmodel/`: Lógica de estado y comunicación con repositorios.
- `data/repository/`:
    - `ProductoRepository.kt`: Conexión con Firestore.
    - `StorageRepository.kt`: Gestión de archivos de imagen locales.
- `data/model/`: Definición de `data classes` del sistema.

## 🔑 Permisos Utilizados

- `CAMERA`: Para capturar fotos de productos.
- `INTERNET`: Para sincronización con Firebase.
- `ACCESS_NETWORK_STATE`: Para detección de conectividad.
