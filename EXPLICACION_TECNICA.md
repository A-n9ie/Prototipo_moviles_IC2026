# Documento de Explicación Técnica - DataClassApp

## 1. Arquitectura del Sistema
La aplicación sigue el patrón de diseño **MVVM (Model-View-ViewModel)**, lo que permite una separación clara entre la interfaz de usuario y la lógica de negocio:
- **Model:** Data classes en Kotlin (`Producto`, `Cliente`) que definen la estructura de los datos.
- **View:** Pantallas desarrolladas íntegramente en **Jetpack Compose** utilizando componentes de **Material 3**.
- **ViewModel:** Encargado de gestionar el estado de la UI y comunicarse con los repositorios mediante **Corrutinas**.

## 2. Infraestructura de Backend y Base de Datos Remota
La aplicación utiliza los servicios de **Google Firebase** como infraestructura de backend. Los detalles técnicos de la conexión remota son:

- **Nombre del Proyecto:** `lab04app-moviles`
- **ID del Proyecto:** `lab04app-moviles`
- **Servicio de Base de Datos:** **Firebase Firestore** (Base de datos NoSQL basada en documentos para el almacenamiento de texto en tiempo real).
- **Servicio de Autenticación:** **Firebase Authentication** (Gestión de usuarios y seguridad de acceso).
- **Bucket Original de Storage:** `lab04app-moviles.firebasestorage.app` (Referenciado en la configuración, aunque la aplicación actual prioriza el almacenamiento local para optimizar fiabilidad).

## 3. Tecnologías de Persistencia (Híbrida)
Se implementó una solución de almacenamiento mixta para optimizar la fiabilidad:

### A. Datos Estructurados (Nube)
- **Servicio:** Firebase Firestore.
- **Uso:** Almacena la información textual (nombres, precios, stock, datos de clientes).
- **Ventaja:** Sincronización en tiempo real y disponibilidad multidispositivo.

### B. Archivos Multimedia (Local)
- **Servicio:** Almacenamiento Interno Privado (`context.filesDir`).
- **Uso:** Almacena las fotografías tomadas con la cámara o seleccionadas de la galería.
- **Implementación:** El `StorageRepository` copia los archivos de imagen a una carpeta interna y guarda la **ruta absoluta** en Firestore. Esto garantiza una carga instantánea de imágenes mediante la librería **Coil**.

## 4. Componentes Técnicos Clave

### Gestión de Imágenes
- **Coil:** Utilizado para la carga asíncrona de imágenes. Soporta nativamente rutas de archivos locales y URLs de internet.
- **FileProvider:** Configurado para permitir que la aplicación de la Cámara del sistema guarde fotos en el espacio de almacenamiento de nuestra app de forma segura.

### Red y API
- **Retrofit:** Configurado en el proyecto para futuras expansiones hacia APIs REST externas.
- **Firebase Auth:** Gestiona el ciclo de vida de la sesión del usuario (Token-based authentication).

## 5. Manejo de Permisos y Seguridad
La aplicación implementa **Runtime Permissions** (permisos en tiempo de ejecución) para la Cámara. 
- Al usar el almacenamiento interno privado, **no se requieren permisos de lectura/escritura externa**, lo que hace a la aplicación más segura y menos invasiva para el usuario.

## 6. Decisiones de Ingeniería
Durante el desarrollo, se detectó que las imágenes descargadas de navegadores (Chrome) perdían el acceso a la URI original tras un tiempo debido a las restricciones de seguridad de Android. Se resolvió implementando un flujo de **"Copia Segura"**, donde la app duplica la imagen seleccionada en su propio directorio antes de procesarla, garantizando que el archivo nunca se pierda.
