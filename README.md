# DataClassActivitiesApp - Prototipo Móviles IC2026

Este proyecto es una aplicación Android desarrollada con **Jetpack Compose** que implementa un sistema de gestión (CRUD) para Clientes y Productos, integrando autenticación y almacenamiento con **Firebase**.

## 🚀 Características

- **Autenticación**: Registro e inicio de sesión utilizando Firebase Auth.
- **Gestión de Productos**: Listado, creación, edición y detalle de productos.
- **Gestión de Clientes**: Listado, creación, edición y detalle de clientes.
- **Arquitectura**: Uso de Navigation Compose para el flujo de la aplicación.
- **Persistencia**: Integración con Firebase Firestore para los datos y Firebase Storage para archivos (si aplica).
- **UI Moderna**: Desarrollado íntegramente con Jetpack Compose y Material 3.
- **Networking**: Retrofit configurado para posibles integraciones con APIs externas.

## 🛠️ Tecnologías Utilizadas

- **Kotlin** & **Jetpack Compose**
- **Firebase** (Auth, Firestore, Storage)
- **Navigation Compose**
- **Retrofit** & **Kotlinx Serialization**
- **Coil** (Carga de imágenes)
- **Corrutinas de Kotlin**

## 📋 Requisitos Previos

- **Android Studio Koala** (2024.1.1) o superior.
- **JDK 17**.
- **Android SDK 34** (API Level 34).

## ⚙️ Instrucciones de Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/Ivan/Prototipo_moviles_IC2026.git
   ```

2. **Abrir en Android Studio:**
   - Abre Android Studio y selecciona "Open".
   - Navega hasta la carpeta del proyecto y selecciónala.
   - El proyecto ya incluye el archivo `google-services.json` configurado.

3. **Sincronizar Gradle:**
   - Espera a que Android Studio termine de descargar las dependencias y sincronizar el proyecto.

4. **Ejecutar:**
   - Conecta un dispositivo físico o inicia un emulador (API 24 o superior).
   - Haz clic en el botón **Run** (icono de play verde) en la barra de herramientas superior.

## 📂 Estructura del Proyecto

- `ui/navigation/`: Configuración de rutas y navegación de la app.
- `ui/screens/`: Pantallas de la interfaz de usuario (Login, Home, Clientes, Productos).
- `data/`: Modelos de datos y repositorios.
- `viewmodel/`: Lógica de negocio y gestión de estado de la UI.
